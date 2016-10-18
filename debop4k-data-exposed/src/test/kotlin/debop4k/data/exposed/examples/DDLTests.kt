/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package debop4k.data.exposed.examples

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.*
import org.junit.Test
import javax.sql.rowset.serial.SerialBlob

class DDLTests : DatabaseTestBase() {

  @Test
  fun tableExists01() {
    val TestTable = object : Table("test") {
      val id = integer("id").primaryKey().autoIncrement()
      val name = varchar("name", length = 42)
    }

    withTables {
      assertThat(TestTable.exists()).isFalse()
    }
  }

  @Test
  fun tableExists02() {
    val TestTable = object : Table("test") {
      val id = integer("id").primaryKey().autoIncrement()
      val name = varchar("name", length = 42)
    }

    withTables(TestTable) {
      assertThat(TestTable.exists()).isTrue()
    }
  }

  @Test
  fun testCreateMissingTablesAndColumns01() {
    val TestTable = object : Table("test") {
      val id = integer("id").primaryKey().autoIncrement()
      val name = varchar("name", length = 42)
    }

    withDb {
      SchemaUtils.createMissingTablesAndColumns(TestTable)
      try {
        assertThat(TestTable.exists()).isTrue()
      } finally {
        SchemaUtils.drop(TestTable)
      }
    }
  }

  @Test
  fun unnamedTableWithQuotesSQL() {
    val TestTable = object : Table() {
      val id = integer("id").primaryKey()
      val name = varchar("name", length = 42)
    }

    withTables(TestTable) {
      val q = db.identityQuoteString
      assertThat(TestTable.ddl.single())
          .isEqualToIgnoringCase("CREATE TABLE IF NOT EXISTS ${q}unnamedTableWithQuotesSQL\$TestTable$1$q (id INT PRIMARY KEY, name VARCHAR(42) NOT NULL)")
    }
  }

  @Test
  fun namedEmptyTableWithoutQuotesSQL() {
    val TestTable = object : Table("test_named_table") {
    }

    // MySQL, PostgreSQL, SQLITE 는 하나 이상의 컬럼이 있어야 합니다.
    withTables(excludeSettings = listOf(TestDB.MYSQL, TestDB.POSTGRESQL), tables = TestTable) {
      assertThat(TestTable.ddl.single()).isEqualToIgnoringCase("CREATE TABLE IF NOT EXISTS test_named_table")
    }
  }

  @Test
  fun tableWithDifferentColumnTypesSQL01() {
    val TestTable = object : Table("test_table_with_different_column_types") {
      val id = integer("id").autoIncrement()
      val name = varchar("name", 42).primaryKey()
      val age = integer("age").nullable()
      // not applicable in H2 database
      //            val testCollate = varchar("testCollate", 2, "ascii_general_ci")
    }

    withTables(excludeSettings = listOf(TestDB.MYSQL), tables = TestTable) {
      assertThat(TestTable.ddl.single())
          .containsIgnoringCase("CREATE TABLE IF NOT EXISTS test_table_with_different_column_types (id ")
          .containsIgnoringCase("name VARCHAR(42) PRIMARY KEY, age INT NULL")
    }
  }

  @Test fun tableWithDifferentColumnTypesSQL02() {
    val TestTable = object : Table("test_table_with_different_column_types") {
      val id = integer("id").primaryKey()
      val name = varchar("name", 42).primaryKey()
      val age = integer("age").nullable()
    }

    withTables(excludeSettings = listOf(TestDB.MYSQL), tables = TestTable) {
      assertThat(TestTable.ddl.single())
          .isEqualToIgnoringCase("CREATE TABLE IF NOT EXISTS test_table_with_different_column_types (id INT, name VARCHAR(42), age INT NULL, CONSTRAINT pk_test_table_with_different_column_types PRIMARY KEY (id, name))")
    }
  }

  @Test
  fun testDefaults01() {
    val TestTable = object : Table("t") {
      val s = varchar("s", 100).default("test")
      val l = long("l").default(42L)
    }

    withTables(TestTable) {
      assertThat(TestTable.ddl.single())
          .isEqualToIgnoringCase("CREATE TABLE IF NOT EXISTS t (s VARCHAR(100) NOT NULL DEFAULT 'test', l BIGINT NOT NULL DEFAULT 42)")
    }
  }


  @Test fun testIndices01() {
    val t = object : Table("t1") {
      val id = integer("id").primaryKey()
      val name = varchar("name", 255).index()
    }

    withTables(t) {
      val alter = SchemaUtils.createIndex(t.indices[0].first, t.indices[0].second)
      assertEquals("CREATE INDEX t1_name ON t1 (name)", alter)
    }
  }

  @Test fun testIndices02() {
    val t = object : Table("t2") {
      val id = integer("id").primaryKey()
      val lvalue = integer("lvalue")
      val rvalue = integer("rvalue");
      val name = varchar("name", 255).index()

      init {
        index(false, lvalue, rvalue)
      }
    }

    withTables(t) {
      val a1 = SchemaUtils.createIndex(t.indices[0].first, t.indices[0].second)
      assertEquals("CREATE INDEX t2_name ON t2 (name)", a1)

      val a2 = SchemaUtils.createIndex(t.indices[1].first, t.indices[1].second)
      assertEquals("CREATE INDEX t2_lvalue_rvalue ON t2 (lvalue, rvalue)", a2)
    }
  }

  @Test fun testIndices03() {
    val t = object : Table("t1") {
      val id = integer("id").primaryKey()
      val name = varchar("name", 255).uniqueIndex()
    }

    withTables(t) {
      val alter = SchemaUtils.createIndex(t.indices[0].first, t.indices[0].second)
      assertEquals("CREATE UNIQUE INDEX t1_name_unique ON t1 (name)", alter)

    }
  }

  @Test fun testBlob() {
    val t = object : Table("t1") {
      val id = integer("id").autoIncrement().primaryKey()
      val b = blob("blob")
    }

    withTables(t) {
      val bytes = "Hello there!".toByteArray()
//      val blob = if (currentDialect.dataTypeProvider.blobAsStream) {
//        SerialBlob(bytes)
//      } else connection.createBlob().apply {
//        setBytes(1, bytes)
//      }
      val blob = try {
        SerialBlob(bytes)
      } catch(e: Exception) {
        connection.createBlob().apply { setBytes(1, bytes) }
      }

      val id = t.insert {
        it[t.b] = blob
      } get (t.id)


      val readOn = t.select { t.id eq id }.first()[t.b]
      val text = readOn.binaryStream.reader().readText()

      assertEquals("Hello there!", text)
    }
  }

  @Test fun testBinary() {
    val t = object : Table() {
      val binary = binary("bytes", 10)
    }

    withTables(t) {
      t.insert { it[t.binary] = "Hello!".toByteArray() }

      val bytes = t.selectAll().single()[t.binary]

      assertEquals("Hello!", String(bytes))

    }
  }

  @Test fun addAutoPrimaryKey() {
    val tableName = "Foo"
    val initialTable = object : Table(tableName) {
      val bar = text("bar")
    }
    val t = IntIdTable(tableName)


    withDb(TestDB.H2) {
      SchemaUtils.createMissingTablesAndColumns(initialTable)
      assertEquals("ALTER TABLE $tableName ADD COLUMN id ${t.id.columnType.sqlType()}", t.id.ddl.first())
      assertEquals("ALTER TABLE $tableName ADD CONSTRAINT pk_$tableName PRIMARY KEY (id)", t.id.ddl[1])
      //assertEquals(1, currentDialect.tableColumns(t)[t]!!.size)
      SchemaUtils.createMissingTablesAndColumns(t)
      //assertEquals(2, currentDialect.tableColumns(t)[t]!!.size)
    }

  }
}