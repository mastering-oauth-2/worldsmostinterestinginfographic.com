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
 * This class encapsulates the response from the collection of a user's monthly post frequency via the
 * <code>com.worldsmostinterestinginfographic.statistics.collect.MonthlyPostFrequencyCollector</code>.
 *
 * The result contains an array holding the count of posts per month of the year where index 0 represents January (i.e.
 * the value at index 0 represents the number of posts made in the month of January, value at index 1 for February,
 * 3 for March, and so on).
 */
public class MonthlyPostFrequencyResult implements StatisticsResult, InfographicResult {

  private int[] postsByMonthOfYear;
  private String error;

  public MonthlyPostFrequencyResult(int[] postsByMonthOfYear) {
    if (postsByMonthOfYear == null) {
      throw new IllegalArgumentException();
    }

    this.postsByMonthOfYear = postsByMonthOfYear;
  }

  public MonthlyPostFrequencyResult(String error) {
    this.error = error;
  }

  @Override
  public int[] getResult() {
    return postsByMonthOfYear;
  }

  @Override
  public String getError() {
    return error;
  }

  /**
   * Returns the result data in JSON format for the client to use in the rendering of the infographic.
   *
   * Returns the monthly post frequency of the user.  This is represented as an array of 12 integers, where the value is
   * the number of posts made for that month of the year, with index 0 being January, index 1 being February, etc.  This
   * will be returned in a particular JSON response format expected by the "Monthly Post Frequency" infographic on the
   * client.  An example JSON response looks like:
   *
   * {
   *   "frequency":[
   *     {
   *       "value":1,
   *       "x":0
   *     },
   *     {
   *       "value":2,
   *       "x":11
   *     },
   *     {
   *       "value":1,
   *       "x":22
   *     },
   *     {
   *       "value":0,
   *       "x":33
   *     },
   *     {
   *       "value":1,
   *       "x":44
   *     },
   *     {
   *       "value":5,
   *       "x":55
   *     },
   *     {
   *       "value":1,
   *       "x":66
   *     },
   *     {
   *       "value":4,
   *       "x":77
   *     },
   *     {
   *       "value":2,
   *       "x":88
   *     },
   *     {
   *       "value":2,
   *       "x":99
   *     },
   *     {
   *       "value":2,
   *       "x":110
   *     },
   *     {
   *       "value":3,
   *       "x":120
   *     }
   *   ],
   *   "color":"#3a5897"
   * }
   *
   * This data will be used by the "Monthly Post Frequency" infographic to populate and render.
   *
   * @return A JSON representation of the user's monthly post frequency data
   */
  @Override
  public String getInfographicJson() {
    String json = "{" +
                  "  \"frequency\": [" +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[0] + "," +
                  "      \"x\": 0" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[1] + "," +
                  "      \"x\": 11" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[2] + "," +
                  "      \"x\": 22" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[3] + "," +
                  "      \"x\": 33" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[4] + "," +
                  "      \"x\": 44" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[5] + "," +
                  "      \"x\": 55" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[6] + "," +
                  "      \"x\": 66" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[7] + "," +
                  "      \"x\": 77" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[8] + "," +
                  "      \"x\": 88" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[9] + "," +
                  "      \"x\": 99" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[10] + "," +
                  "      \"x\": 110" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + postsByMonthOfYear[11] + "," +
                  "      \"x\": 120" +
                  "    }" +
                  "  ]," +
                  "  \"color\": \"#3a5897\"" +
                  "}";

    return new Minify().minify(json);
  }
}
