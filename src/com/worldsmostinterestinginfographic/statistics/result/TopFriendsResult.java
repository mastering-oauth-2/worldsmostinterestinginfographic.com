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
import com.worldsmostinterestinginfographic.model.object.User;

import java.util.List;
import java.util.Map;

/**
 * This class encapsulates the response from the collection of a user's top friends via the
 * <code>com.worldsmostinterestinginfographic.statistics.collectTopFriendsCollector</code>.
 *
 * The result contains an ordered list of the user's friends where the front of the list (i.e. index 0) is the user's
 * top friend (i.e. the friend who has liked his posts the most).  The 2nd position in the list represents the user's
 * 2nd best friend, the 3rd for the 3rd, and so on.
 */
public class TopFriendsResult implements StatisticsResult, InfographicResult {

  private List<Map.Entry<User, Integer>> topFriends;
  private String error;

  public TopFriendsResult(List<Map.Entry<User, Integer>> topFriends) {
    if (topFriends == null) {
      throw new IllegalArgumentException();
    }

    this.topFriends = topFriends;
  }

  public TopFriendsResult(String error) {
    this.error = error;
  }

  @Override
  public List<Map.Entry<User, Integer>> getResult() {
    return topFriends;
  }

  @Override
  public String getError() {
    return error;
  }

  /**
   * Returns the result data in JSON format for the client to use in the rendering of the infographic.
   *
   * Returns the user's top 4 friends represented in a particular JSON response format expected by the "Top Friends"
   * infographic on the client.  An example JSON response looks like:
   *
   * {
   *   "friends":[
   *     {
   *       "imgSrc":"https://graph.facebook.com/55555101217077671/picture?width=85&height=85",
   *       "likes":14,
   *       "name":"Richard Stewart",
   *       "color":"#3b5998"
   *     },
   *     {
   *       "imgSrc":"https://graph.facebook.com/5555514772458858/picture?width=85&height=85",
   *       "likes":12,
   *       "name":"Rachel Gibson",
   *       "color":"#5bc0bd"
   *     },
   *     {
   *       "imgSrc":"https://graph.facebook.com/55555101535405879/picture?width=85&height=85",
   *       "likes":12,
   *       "name":"Horace Greenfield",
   *       "color":"#f08a4b"
   *     },
   *     {
   *       "imgSrc":"https://graph.facebook.com/55555101552585413/picture?width=85&height=85",
   *       "likes":11,
   *       "name":"Leslie Peters",
   *       "color":"#1c2541"
   *     }
   *   ]
   * }
   *
   * This data will be used by the "Top Friends" infographic to populate and render.
   *
   * @return A JSON representation of the user's top-friends data
   */
  @Override
  public String getInfographicJson() {

    if (topFriends.size() < 4) {
      return null;
    }

    String json = "{" +
                  "  \"friends\": [" +
                  "    {" +
                  "      \"imgSrc\": \"https://graph.facebook.com/" + topFriends.get(0).getKey().getId()
                  + "/picture?width=85&height=85\"," +
                  "      \"likes\": " + topFriends.get(0).getValue() + "," +
                  "      \"name\": \"" + topFriends.get(0).getKey().getName() + "\"," +
                  "      \"color\": \"#3b5998\"" +
                  "    }," +
                  "    {" +
                  "      \"imgSrc\": \"https://graph.facebook.com/" + topFriends.get(1).getKey().getId()
                  + "/picture?width=85&height=85\"," +
                  "      \"likes\": " + topFriends.get(1).getValue() + "," +
                  "      \"name\": \"" + topFriends.get(1).getKey().getName() + "\"," +
                  "      \"color\": \"#5bc0bd\"" +
                  "    }," +
                  "    {" +
                  "      \"imgSrc\": \"https://graph.facebook.com/" + topFriends.get(2).getKey().getId()
                  + "/picture?width=85&height=85\"," +
                  "      \"likes\": " + topFriends.get(2).getValue() + "," +
                  "      \"name\": \"" + topFriends.get(2).getKey().getName() + "\"," +
                  "      \"color\": \"#f08a4b\"" +
                  "    }," +
                  "    {" +
                  "      \"imgSrc\": \"https://graph.facebook.com/" + topFriends.get(3).getKey().getId()
                  + "/picture?width=85&height=85\"," +
                  "      \"likes\": " + topFriends.get(3).getValue() + "," +
                  "      \"name\": \"" + topFriends.get(3).getKey().getName() + "\"," +
                  "      \"color\": \"#1c2541\"" +
                  "    }" +
                  "  ]" +
                  "}";

    return new Minify().minify(json);
  }
}
