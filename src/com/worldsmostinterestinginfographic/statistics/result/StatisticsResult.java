/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.worldsmostinterestinginfographic.statistics.result;

/**
 * Denotes a particular object as a response to a request to collect statistics via an implementation of the
 * <code>com.worldsmostinterestinginfographic.statistics.collect.StatisticsCollector</code>.
 *
 * @param <T>  Returns a generic result depending on the statistics collected
 */
public interface StatisticsResult<T> {

  /**
   * Get result data in response to a request to collect statistics data via an implementation of the
   * <code>com.worldsmostinterestinginfographic.statistics.collect.StatisticsCollector</code>.
   *
   * @return The result data
   */
  public T getResult();

  /**
   * Get the error message returned when a collect-statistics operation has failed.
   *
   * @return The error message
   */
  public String getError();
}
