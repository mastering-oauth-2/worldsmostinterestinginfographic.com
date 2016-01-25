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
import com.worldsmostinterestinginfographic.statistics.result.DailyPostFrequencyResult;

import java.util.Calendar;
import java.util.List;

/**
 * A statistics collector, this class will collect data regarding a given user's daily post frequency by analyzing their
 * feed data.  Their post frequency is represented as the number of posts posted per day of the week.
 */
public class DailyPostFrequencyCollector implements StatisticsCollector {

  private final int DAYS_PER_WEEK = 7;

  /**
   * This method will iterate through the given posts generating statistics about the given user's daily post frequency
   * represented as the number of posts posted per day of the week.
   *
   * @param user The user for whom to collect statistics for
   * @param posts The posts to analyze to gather desired statistics
   * @return A <code>com.worldsmostinterestinginfographic.statistics.result.DailyPostFrequencyResult</code> which
   * encapsulates the response to a request to collect statistics about a user's daily post frequency
   */
  @Override
  public DailyPostFrequencyResult collect(User user, List<Post> posts) {

    // Populate daily post-frequency array
    int[] postsByDayOfWeek = new int[DAYS_PER_WEEK];
    for (Post post : posts) {

      // Only look at my posts
      if (!post.getFrom().equals(user)) {
        continue;
      }

      if (post.getCreatedDate() != null) {
        postsByDayOfWeek[post.getCreatedDate().get(Calendar.DAY_OF_WEEK) - 1] += 1;
      }
    }

    return new DailyPostFrequencyResult(postsByDayOfWeek);
  }
}
