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

package com.worldsmostinterestinginfographic.statistics.collect;

import com.worldsmostinterestinginfographic.model.object.Post;
import com.worldsmostinterestinginfographic.model.object.User;
import com.worldsmostinterestinginfographic.statistics.result.PostTypesResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A statistics collector, this class will collect data regarding a given user's preferred post type (e.g. status
 * update, shared link, photo, etc) by analyzing their feed data.
 */
public class PostTypesCollector implements StatisticsCollector {

  /**
   * This method will iterate through the given posts generating statistics about the given user's most commonly
   * used post type (e.g. status update, shared link, photo, etc).
   *
   * @param user The user for whom to collect statistics for
   * @param posts The posts to analyze to gather desired statistics
   * @return A <code>com.worldsmostinterestinginfographic.statistics.result.PostTypesResult</code> which encapsulates
   * the response to a request to collect statistics about a user's preferred post types
   */
  @Override
  public PostTypesResult collect(User user, List<Post> posts) {

    // Populate post-types map
    Map<Post.Type, Integer> postTypesCount = new HashMap<>();
    for (Post post : posts) {

      // Only look at my posts
      if (!post.getFrom().equals(user)) {
        continue;
      }

      if (!postTypesCount.containsKey(post.getType())) {
        postTypesCount.put(post.getType(), 1);
        continue;
      }

      postTypesCount.put(post.getType(), postTypesCount.get(post.getType()) + 1);
    }

    return new PostTypesResult(postTypesCount);
  }
}
