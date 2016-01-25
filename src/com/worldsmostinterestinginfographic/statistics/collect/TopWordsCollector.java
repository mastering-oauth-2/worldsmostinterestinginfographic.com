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
import com.worldsmostinterestinginfographic.statistics.result.TopWordsResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A statistics collector, this class will collect data regarding a given user's most used words.  This collector will
 * analyze the user's feed posts counting each word as it appears.  The result of the collection will return an ordered
 * list of entries where the key is the word and the value is the count of occurrences of that word among the user's
 * posts.
 */
public class TopWordsCollector implements StatisticsCollector {

  private static final int MIN_WORD_LENGTH = 4;
  private static final String WORD_FINDER_REGEX = "\\b[A-Za-z]+\\b";

  /**
   * This method will iterate through the given posts generating statistics about the given user's most frequently used
   * words.
   *
   * The user's most frequently used words are counted based on their number of occurrences in the user's posts.  Words
   * are recognized as alphabetic strings with a length greater than 3.Only posts made by the user will be counted.
   * Posts made by other users that appear in their feed will not be included.  This collect method will return an
   * ordered list of entries where the key is the word and the value is the count of occurrences of that word among the
   * user's posts.
   *
   * @param user The user for whom to collect statistics for
   * @param posts The posts to analyze to gather desired statistics
   * @return A <code>com.worldsmostinterestinginfographic.statistics.result.TopWordsResult</code> which encapsulates
   * the response to a request to collect statistics about a user's most frequently used words
   */
  @Override
  public TopWordsResult collect(User user, List<Post> posts) {

    // Populate word-count map
    Map<String, Integer> wordsCountMap = new HashMap<String, Integer>();
    for (Post post : posts) {

      // Only look at your own posts
      if (!post.getFrom().equals(user)) {
        continue;
      }

      Pattern pattern = Pattern.compile(WORD_FINDER_REGEX, Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(post.getMessage());

      while (matcher.find()) {

        String word = matcher.group();

        // Only accept words greater than a certain minimum length
        if (word.length() < MIN_WORD_LENGTH) {
          continue;
        }

        if (!wordsCountMap.containsKey(word)) {
          wordsCountMap.put(word, 1);
          continue;
        }

        wordsCountMap.put(word, wordsCountMap.get(word) + 1);
      }
    }

    // Convert word-count map from map to sorted list ordered by occurrence count (i.e. index 0 has word with most occurrences)
    List<Map.Entry<String, Integer>>  topWordsList = new LinkedList<Map.Entry<String, Integer>>(wordsCountMap.entrySet());
    Collections.sort(topWordsList, new Comparator<Map.Entry<String,Integer>>() {
      public int compare(Map.Entry<String, Integer> o1,
                         Map.Entry<String, Integer> o2) {
        return (o2.getValue()).compareTo(o1.getValue());
      }
    });

    return new TopWordsResult(topWordsList);
  }
}
