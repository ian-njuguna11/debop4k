/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.core.asyncs

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.util.concurrent.*

/**
 * AsyncsTest
 * @author debop sunghyouk.bae@gmail.com
 */
@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(AsyncsConfiguration::class))
class AsyncsTest {

  private val log = LoggerFactory.getLogger(javaClass)

  val action: Runnable = Runnable { Thread.sleep(10) }

  val supplier: Callable<Int> = Callable { Thread.sleep(10); 1 }

  @Test
  fun emptyRunnable() {
    val promise = async() { action.run() }
    promise.await()
    assertThat(promise.isSuccess()).isTrue()
  }

  @Test
  fun testAwait() {
    val promise = async(result = 10) { action.run() }
    promise.await()
    assertThat(promise.isSuccess()).isTrue()
    assertThat(promise.get()).isEqualTo(10)
  }

  @Test
  fun testAwaitAll() {
    val promises = (0 until 100).map { async(result = 10) { action.run() } }
    promises.awaitAll()
    assertThat(promises.all { it.isSuccess() }).isTrue()
  }

  @Test
  fun testResult() {
    val promise = async() { supplier.call() }
    val result = promise.result()
    assertThat(promise.isDone()).isTrue()
    assertThat(result).isEqualTo(1)
  }

  @Test
  fun testResultAll() {
    val promises = (0 until 100).map { async() { supplier.call() } }
    val results = promises.resultAll()
    results.forEach {
      assertThat(it).isEqualTo(1)
    }
  }
}