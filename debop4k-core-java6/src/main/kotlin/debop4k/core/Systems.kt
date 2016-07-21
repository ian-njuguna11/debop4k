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

@file:JvmName("SystemExtensions")

package debop4k.core

val JVM_VERSION: String by lazy {
  Runtime::class.java.`package`.specificationVersion
}
val JVM_IMPLEMENTATION_VERSION: String by lazy {
  Runtime::class.java.`package`.implementationVersion
}
val JVM_VENDOR: String by lazy {
  Runtime::class.java.`package`.specificationVendor
}
val JVM_IMPLEMENTATION_VENDOR: String by lazy {
  Runtime::class.java.`package`.implementationVendor
}

val isJava6: Boolean by lazy { JVM_VERSION.toDouble() == 1.6 }
val isJava7: Boolean by lazy { JVM_VERSION.toDouble() == 1.7 }
val isJava8: Boolean by lazy { JVM_VERSION.toDouble() == 1.8 }