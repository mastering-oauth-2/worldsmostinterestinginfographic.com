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
import com.worldsmostinterestinginfographic.statistics.result.MonthlyPostFrequencyResult;

import java.util.Calendar;
import java.util.List;

/**
 * A statistics collector, this class will collect data regarding a given user's monthly post frequency by analyzing
 * their feed data.  Their post frequency is represented as the number of posts posted per month of the year.
 */
public class MonthlyPostFrequencyCollector implements StatisticsCollector {

  private final int MONTHS_PER_YEAR = 12;

  /**
   * This method will iterate through the given posts generating statistics about the given user's monthly post
   * frequency represented as the number of posts posted per month of the year.
   *
   * @param user The user for whom to collect statistics for
   * @param posts The posts to analyze to gather desired statistics
   * @return A <code>com.worldsmostinterestinginfographic.statistics.result.MonthlyPostFrequencyResult</code> which
   * encapsulates the response to a request to collect statistics about a user's monthly post frequency
   */
  @Override
  public MonthlyPostFrequencyResult collect(User user, List<Post> posts) {

    // Populate monthly post-frequency array
    int[] postsByMonthOfYear = new int[MONTHS_PER_YEAR];
    for (Post post : posts) {

      // Only look at my posts
      if (!post.getFrom().equals(user)) {
        continue;
      }

      if (post.getCreatedDate() != null) {
        postsByMonthOfYear[post.getCreatedDate().get(Calendar.MONTH)] += 1;
      }
    }

    return new MonthlyPostFrequencyResult(postsByMonthOfYear);
  }
}
