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

package com.worldsmostinterestinginfographic.service;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import com.worldsmostinterestinginfographic.model.Model;
import com.worldsmostinterestinginfographic.model.object.Post;
import com.worldsmostinterestinginfographic.model.object.User;
import com.worldsmostinterestinginfographic.util.OAuth2Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class used to make requests for data to Facebook via OAuth 2 workflows.
 */
public class FacebookService {

  /**
   * Fetches the user's profile.
   *
   * @param accessToken A valid access token with the 'public_profile' scope
   * @return The user profile for the user
   *
   * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions#reference-public_profile">https://developers.facebook.com/docs/facebook-login/permissions#reference-public_profile</a>
   */
  public User getProfile(String accessToken) {

    // Construct profile API request
    String requestUrl = Model.FACEBOOK_API_ENDPOINT + "me?fields=" + Model.FACEBOOK_REQUESTED_PROFILE_FIELDS;

    // Make profile request
    String profileJson = OAuth2Utils.makeProtectedResourceRequest(requestUrl, accessToken);

    // Convert profile JSON to profile object
    User user = new User(profileJson);

    return user;
  }

  /**
   * Fetches the user's feed posts.
   *
   * @param accessToken A valid access token with the 'user_posts' scope
   * @return A list of posts from the user's feed
   *
   * @see <a href="https://developers.facebook.com/docs/facebook-login/permissions#reference-user_posts">https://developers.facebook.com/docs/facebook-login/permissions#reference-user_posts</a>
   */
  public List<Post> getFeedPosts(String accessToken) {

    // Construct feed API request
    String requestUrl = Model.FACEBOOK_API_ENDPOINT + "me/feed?limit=" + Model.MAX_NUMBER_OF_FACEBOOK_POSTS_TO_REQUEST +
                        "&access_token=" + accessToken + "&fields=" + Model.FACEBOOK_REQUESTED_FEED_FIELDS;

    // Make feed request
    String postsJson = OAuth2Utils.makeProtectedResourceRequest(requestUrl, accessToken);

    // Convert posts JSON to posts list
    List<Post> posts = new ArrayList<>();
    try {
      JSONObject postsObject = new JSONObject(postsJson);
      JSONArray postsArray = new JSONArray(postsObject.getString("data"));

      for (int i = 0; i < postsArray.length(); i++) {
        Post post = new Post(postsArray.get(i).toString());
        if (post != null) {
          posts.add(post);
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return posts;
  }
}
