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

package com.worldsmostinterestinginfographic.servlet;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import com.worldsmostinterestinginfographic.model.Model;
import com.worldsmostinterestinginfographic.model.object.Post;
import com.worldsmostinterestinginfographic.model.object.User;
import com.worldsmostinterestinginfographic.service.FacebookService;
import com.worldsmostinterestinginfographic.statistics.collect.DailyPostFrequencyCollector;
import com.worldsmostinterestinginfographic.statistics.collect.MonthlyPostFrequencyCollector;
import com.worldsmostinterestinginfographic.statistics.collect.PostTypesCollector;
import com.worldsmostinterestinginfographic.statistics.collect.StatisticsCollector;
import com.worldsmostinterestinginfographic.statistics.collect.TopFriendsCollector;
import com.worldsmostinterestinginfographic.statistics.collect.TopWordsCollector;
import com.worldsmostinterestinginfographic.statistics.result.InfographicResult;
import com.worldsmostinterestinginfographic.statistics.result.StatisticsResult;
import com.worldsmostinterestinginfographic.util.LoggingUtils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to handle requests to fetch statistics data for a user.
 */
public class StatisticsServlet extends HttpServlet {

  private static final Logger log = Logger.getLogger(StatisticsServlet.class.getName());

  private final FacebookService facebookService;

  public StatisticsServlet() {
    facebookService = new FacebookService();
  }

  /**
   * Servlet to handle request to fetch statistics data for a user.
   *
   * Will check for valid session data from the cache, including a valid access token.  If present, will attempt to make
   * a protected resource request for the user's feed data using the access token.  Once the feed data has been
   * returned, statistics will be collected, and the responses returned.
   *
   * @param request  The HTTP request sent by the client
   * @param response The HTTP response that the server will send back to the client
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    // Fetch session data from cache
    User user = (User) Model.cache.get(request.getSession().getId() + ".profile");
    if (user == null) {
      log.severe("[" + request.getSession().getId() + "] Invalid session, no profile found in cache");
    }

    String accessToken = Objects.toString(Model.cache.get(request.getSession().getId() + ".token"));
    if (accessToken == null) {
      log.severe("[" + request.getSession().getId() + "] Invalid session, no token found in cache");
    }

    // Get feed data
    log.info("[" + request.getSession().getId() + "] Access token " + LoggingUtils.anonymize(accessToken)
             + ".  Requesting feed data.");

    List<Post> posts = facebookService.getFeedPosts(accessToken);
    if (posts.size() <= 0) {
      response.getWriter().println("[]");
      return;
    }

    log.info("[" + request.getSession().getId() + "] Received " + posts.size() + " stories for user " +
             LoggingUtils.anonymize(Objects.toString(user.getId())) + ". Collecting statistics...");

    // Create statistics collectors
    StatisticsCollector topFriendsCollector = new TopFriendsCollector();
    StatisticsCollector postTypesCollector = new PostTypesCollector();
    StatisticsCollector dailyPostFrequencyCollector = new DailyPostFrequencyCollector();
    StatisticsCollector monthlyPostFrequencyCollector = new MonthlyPostFrequencyCollector();
    StatisticsCollector topWordsCollector = new TopWordsCollector();

    // Collect statistics
    StatisticsResult topFriendsResult = topFriendsCollector.collect(user, posts);
    StatisticsResult postTypesResult = postTypesCollector.collect(user, posts);
    StatisticsResult dailyPostFrequencyResult = dailyPostFrequencyCollector.collect(user, posts);
    StatisticsResult monthlyPostFrequencyResult = monthlyPostFrequencyCollector.collect(user, posts);
    StatisticsResult topWordsResult = topWordsCollector.collect(user, posts);

    // Convert statistics objects to JSON response strings
    String topFriendsJson = ((InfographicResult) topFriendsResult).getInfographicJson();
    String postTypesJson = ((InfographicResult) postTypesResult).getInfographicJson();
    String dailyPostFrequencyJson = ((InfographicResult) dailyPostFrequencyResult).getInfographicJson();
    String monthlyPostFrequencyJson = ((InfographicResult) monthlyPostFrequencyResult).getInfographicJson();
    String topWordsJson = ((InfographicResult) topWordsResult).getInfographicJson();

    // Construct final response object containing all statistics data
    String result = "";
    try {
      JSONObject resultObject = new JSONObject();
      resultObject.put("TOP_FRIENDS", new JSONObject(topFriendsJson));
      resultObject.put("POST_TYPES", new JSONObject(postTypesJson));
      resultObject.put("DAILY_POST_FREQUENCY", new JSONObject(dailyPostFrequencyJson));
      resultObject.put("MONTHLY_POST_FREQUENCY", new JSONObject(monthlyPostFrequencyJson));
      resultObject.put("TOP_WORDS", new JSONObject(topWordsJson));

      result = resultObject.toString();
    } catch (JSONException e) {
      log.severe("[" + request.getSession().getId() + "] Error encountered while constructing response JSON: " +
                 e.getMessage());
      e.printStackTrace();
    }

    response.getWriter().println(result);
  }
}
