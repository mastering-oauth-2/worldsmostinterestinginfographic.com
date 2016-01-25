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
import com.worldsmostinterestinginfographic.model.object.Post;

import java.util.Map;

/**
 * This class encapsulates the response from the collection of a user's preferred post type (e.g. status update, shared
 * link, photo, etc) via the <code>com.worldsmostinterestinginfographic.statistics.collect.PostTypesCollector</code>.
 *
 * The result contains a map of post-type to usage-count.
 */
public class PostTypesResult implements StatisticsResult, InfographicResult {

  private Map<Post.Type, Integer> postTypesCount;
  private String error;

  public PostTypesResult(Map<Post.Type, Integer> postTypesCount) {
    if (postTypesCount == null) {
      throw new IllegalArgumentException();
    }

    this.postTypesCount = postTypesCount;
  }

  public PostTypesResult(String error) {
    this.error = error;
  }

  @Override
  public Object getResult() {
    return postTypesCount;
  }

  @Override
  public String getError() {
    return error;
  }

  /**
   * Returns the result data in JSON format for the client to use in the rendering of the infographic.
   *
   * Returns the user's usage count of the following post types:
   *
   *   STATUS
   *   PHOTO
   *   LINK
   *   VIDEO
   *
   *   Note: We are intentionally omitting OFFER and EVENT due to their infrequency
   *
   * This will be returned in a particular JSON response format expected by the "Post Types" infographic on the client.
   * An example JSON response looks like:
   *
   * {
   *   "types":[
   *     {
   *       "value":6,
   *       "description":"Status Update",
   *       "shortname":"status updates",
   *       "color":"#3b5998",
   *       "colorclass":"blue"
   *     },
   *     {
   *       "value":14,
   *       "description":"Image Post",
   *       "shortname":"photos",
   *       "color":"#5bc0bd",
   *       "colorclass":"green"
   *     },
   *     {
   *       "value":1,
   *       "description":"Shared Link",
   *       "shortname":"shared links",
   *       "color":"#2ebaeb",
   *       "colorclass":"blue-light"
   *     },
   *     {
   *       "value":3,
   *       "description":"Video Post",
   *       "shortname":"videos",
   *       "color":"#f08a4b",
   *       "colorclass":"orange"
   *     }
   *   ]
   * }
   *
   * This data will be used by the "Post Types" infographic to populate and render.
   *
   * @return A JSON representation of the user's post-types data
   */
  @Override
  public String getInfographicJson() {
    String json = "{" +
                  "  \"types\": [" +
                  "    {" +
                  "      \"value\": " + (postTypesCount.containsKey(Post.Type.STATUS) ? postTypesCount.get(Post.Type.STATUS) : 0) + "," +
                  "      \"description\": \"Status Update\"," +
                  "      \"shortname\": \"status updates\"," +
                  "      \"color\": \"#3b5998\"," +
                  "      \"colorclass\": \"blue\"" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + (postTypesCount.containsKey(Post.Type.PHOTO) ? postTypesCount.get(Post.Type.PHOTO) : 0) + "," +
                  "      \"description\": \"Image Post\"," +
                  "      \"shortname\": \"photos\"," +
                  "      \"color\": \"#5bc0bd\"," +
                  "      \"colorclass\": \"green\"" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + (postTypesCount.containsKey(Post.Type.LINK) ? postTypesCount.get(Post.Type.LINK) : 0) + "," +
                  "      \"description\": \"Shared Link\"," +
                  "      \"shortname\": \"shared links\"," +
                  "      \"color\": \"#2ebaeb\"," +
                  "      \"colorclass\": \"blue-light\"" +
                  "    }," +
                  "    {" +
                  "      \"value\": " + (postTypesCount.containsKey(Post.Type.VIDEO) ? postTypesCount.get(Post.Type.VIDEO) : 0) + "," +
                  "      \"description\": \"Video Post\"," +
                  "      \"shortname\": \"videos\"," +
                  "      \"color\": \"#f08a4b\"," +
                  "      \"colorclass\": \"orange\"" +
                  "    }" +
                  "  ]" +
                  "}";

    return new Minify().minify(json);
  }
}
