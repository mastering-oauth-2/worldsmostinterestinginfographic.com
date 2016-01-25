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

import com.whoischarles.util.json.Minify;

/**
 * This class encapsulates the response from the collection of a user's daily post frequency via the
 * <code>com.worldsmostinterestinginfographic.statistics.collect.DailyPostFrequencyCollector</code>.
 *
 * The result contains an array holding the count of posts per day of the week where array index 0 represents Sunday
 * (i.e. the value at index 0 represents the number of posts made on a Sunday, value at index 1 for Monday, 2 for
 * Tuesday, and so on).
 */
public class DailyPostFrequencyResult implements StatisticsResult, InfographicResult {

  private int[] postsByDayOfWeek;
  private String error;

  public DailyPostFrequencyResult(int[] postsByDayOfWeek) {
    if (postsByDayOfWeek == null) {
      throw new IllegalArgumentException();
    }

    this.postsByDayOfWeek = postsByDayOfWeek;
  }

  @Override
  public int[] getResult() {
    return postsByDayOfWeek;
  }

  @Override
  public String getError() {
    return error;
  }

  /**
   * Returns the result data in JSON format for the client to use in the rendering of the infographic.
   *
   * Returns the daily post frequency of the user.  This is represented as an array of 7 integers, where the value is
   * the number of posts made for that day of the week, with index 0 being Sunday, index 1 being Monday, etc.  This will
   * be returned in a particular JSON response format expected by the "Daily Post Frequency" infographic on the client.
   * An example JSON response looks like:
   *
   * {
   *   "frequency":[
   *     {
   *       "dayofweek":"Mon",
   *       "count":6
   *     },
   *     {
   *       "dayofweek":"Tue",
   *       "count":1
   *     },
   *     {
   *       "dayofweek":"Wed",
   *       "count":3
   *     },
   *     {
   *       "dayofweek":"Thu",
   *       "count":2
   *     },
   *     {
   *       "dayofweek":"Fri",
   *       "count":7
   *     },
   *     {
   *       "dayofweek":"Sat",
   *       "count":4
   *     },
   *     {
   *       "dayofweek":"Sun",
   *       "count":6
   *     }
   *   ]
   * }
   *
   * This data will be used by the "Daily Post Frequency" infographic to populate and render.
   *
   * @return A JSON representation of the user's daily post frequency data
   */
  @Override
  public String getInfographicJson() {
    String json = "{" +
                  "  \"frequency\": [" +
                  "    {" +
                  "      \"dayofweek\": \"Mon\"" + "," +
                  "      \"count\": " + postsByDayOfWeek[1] +
                  "    }," +
                  "    {" +
                  "      \"dayofweek\": \"Tue\"" + "," +
                  "      \"count\": " + postsByDayOfWeek[2] +
                  "    }," +
                  "    {" +
                  "      \"dayofweek\": \"Wed\"" + "," +
                  "      \"count\": " + postsByDayOfWeek[3] +
                  "    }," +
                  "    {" +
                  "      \"dayofweek\": \"Thu\"" + "," +
                  "      \"count\": " + postsByDayOfWeek[4] +
                  "    }," +
                  "    {" +
                  "      \"dayofweek\": \"Fri\"" + "," +
                  "      \"count\": " + postsByDayOfWeek[5] +
                  "    }," +
                  "    {" +
                  "      \"dayofweek\": \"Sat\"" + "," +
                  "      \"count\": " + postsByDayOfWeek[6] +
                  "    }," +
                  "    {" +
                  "      \"dayofweek\": \"Sun\"" + "," +
                  "      \"count\": " + postsByDayOfWeek[0] +
                  "    }" +
                  "  ]" +
                  "}";

    return new Minify().minify(json);
  }
}
