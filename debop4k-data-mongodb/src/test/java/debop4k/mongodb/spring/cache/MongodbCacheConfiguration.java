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

package debop4k.mongodb.spring.cache;

import debop4k.mongodb.AbstractMongoTest;
import debop4k.mongodb.config.AbstractMongoConfig;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching //(proxyTargetClass = true)
@ComponentScan(basePackageClasses = {UserRepository.class})
public class MongodbCacheConfiguration extends AbstractMongoConfig {

  @Override
  protected String getDatabaseName() {
    return AbstractMongoTest.DATABASE_NAME;
  }

  @Bean
  @SneakyThrows(Exception.class)
  public MongodbCacheManager mongoCacheManager() {
    return new MongodbCacheManager(mongoTemplate(), 60);
  }
}