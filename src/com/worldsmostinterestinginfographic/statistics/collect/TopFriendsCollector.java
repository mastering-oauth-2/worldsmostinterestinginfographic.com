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
import com.worldsmostinterestinginfographic.statistics.result.TopFriendsResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A statistics collector, this class will collect data regarding a given user's top friends by analyzing their feed
 * data.  Top friends are determined by the number of likes they contribute to the user's feed where, the interpretation
 * is, the more likes they give, the better the friend.  The result of the collection will return an ordered list of
 * the user's friends where the front of the list (i.e. index 0) is the user's top friend (i.e. the friend who has liked
 * his posts the most).  The 2nd position in the list represents the user's 2nd best friend, the 3rd for the 3rd, and so
 * on.
 */
public class TopFriendsCollector implements StatisticsCollector {

  /**
   * This method will iterate through the given posts generating statistics about the given user's top friends.
   *
   * The user's top friends are based on the number of likes each friend has contributed to the user's feed.  This is
   * returned to the client in the form of an ordered list of map entries where each map entry contains a user profile
   * as the key and their contributed likes as the value.  The map is ordered by descending number of likes.  So, the
   * first entry in the list represents the friend with the most number of likes to the user's profile, second entry is
   * the friend with the second number of likes, and so on.
   *
   * @param user  The profile of the user who owns the posts
   * @param posts The posts from the wall of the user
   * @return A <code>com.worldsmostinterestinginfographic.statistics.result.TopFriendsResult</code> which encapsulates
   * the response to a request to collect statistics about a user's top friends
   */
  @Override
  public TopFriendsResult collect(User user, List<Post> posts) {

    // Populate friends-likes map
    Map<User, Integer> friendsLikesMap = new HashMap<User, Integer>();
    for (Post post : posts) {
      for (User liker : post.getLikes()) {

        // Ignore own likes
        if (liker.equals(user)) {
          continue;
        }

        if (!friendsLikesMap.containsKey(liker)) {
          friendsLikesMap.put(liker, 1);
          continue;
        }

        friendsLikesMap.put(liker, friendsLikesMap.get(liker) + 1);
      }
    }

    // Convert friends-likes map from map to sorted list ordered by like count (i.e. index 0 has friend with most likes)
    List<Map.Entry<User, Integer>>
        topFriendsList =
        new LinkedList<Map.Entry<User, Integer>>(friendsLikesMap.entrySet());
    Collections.sort(topFriendsList, new Comparator<Map.Entry<User, Integer>>() {
      public int compare(Map.Entry<User, Integer> o1,
                         Map.Entry<User, Integer> o2) {
        return (o2.getValue()).compareTo(o1.getValue());
      }
    });

    return new TopFriendsResult(topFriendsList);
  }
}
