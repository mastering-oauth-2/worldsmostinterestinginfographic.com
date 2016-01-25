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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class encapsulates the response from the collection of a user's most frequently used words via the
 * <code>com.worldsmostinterestinginfographic.statistics.collect.TopWordsCollector</code>.
 *
 * The result contains an ordered list of word-count entries.
 */
public class TopWordsResult implements StatisticsResult, InfographicResult {

  private List<Map.Entry<String, Integer>> topWords;
  private String error;

  public TopWordsResult(List<Map.Entry<String, Integer>> topWords) {
    if (topWords == null) {
      throw new IllegalArgumentException();
    }

    this.topWords = topWords;
  }

  public TopWordsResult(String error) {
    this.error = error;
  }

  @Override
  public List<Map.Entry<String, Integer>> getResult() {
    return topWords;
  }

  @Override
  public String getError() {
    return error;
  }

  /**
   * Returns the result data in JSON format for the client to use in the rendering of the infographic.
   *
   * Returns the user's most frequently used words.  This data is represented in 2 parts: HTML data used for the display
   * of the user's word cloud, and the single most used word.  These are represented as 2 properties of the result:
   * 'html' and 'topword' respectively.  An example JSON response looks like:
   *
   * {
   *   "html":"<li class=\"vvvv-popular\"><a href=\"#\"/>Annou...\"#\"/>Time</a></li>",
   *   "topword":"Bombastic"
   * }
   *
   * This data will be used by the "Top Words" infographic to populate and render.
   *
   * @return A JSON representation of the user's most frequently used words data
   */
  @Override
  public String getInfographicJson() {

    // We're only interested in the user's top used 15 words
    List<Map.Entry<String, Integer>> top15Words = topWords.subList(0, 15);

    // Build word-cloud HTML
    List<String> wordsHtml = new ArrayList<>(top15Words.size());
    int emphasis = 5;
    int previousCount = top15Words.get(0).getValue();
    for (int i = 0; i < top15Words.size(); i++) {

      if (emphasis > 0 && top15Words.get(i).getValue() < previousCount) {
        emphasis--;
      }

      wordsHtml.add("<li class=\\\"" + giveMeVees(emphasis) + (emphasis > 0 ? "-" : "") + "popular\\\"><a href=\\\"#\\\"/>" + top15Words.get(i).getKey() + "</a></li>");
    }

    // Let's shuffle them for the word-cloud
    Collections.shuffle(wordsHtml);

    // Merge
    String html = "";
    for (String wordHtml : wordsHtml) {
      html += wordHtml;
    }

    String json = "{" +
                  "	\"html\": \"" + html + "\"," +
                  "	\"topword\": \"" + top15Words.get(0).getKey() + "\"" +
                  "}";

    return json;
  }

  /**
   * Utility function to return a string of a given number of v's.  For example, an input of 3 will return "vvv", and an
   * input of 7 will return "vvvvvvv".  This is for the purpose of adding varying degrees of emphasis to words in the
   * word cloud.
   *
   * @param numVees The number of v's to appear in the result string
   * @return A string of v's who's count is equal to the input parameter
   */
  private String giveMeVees(int numVees) {
    String vees = "";
    for (int i = 0; i < numVees; i++) {
      vees += "v";
    }

    return vees;
  }
}
