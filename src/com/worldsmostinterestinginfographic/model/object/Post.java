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

package com.worldsmostinterestinginfographic.model.object;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Represents a Facebook Post object.
 *
 * @see <a href="https://developers.facebook.com/docs/graph-api/reference/v2.5/post">https://developers.facebook.com/docs/graph-api/reference/v2.5/post</a>
 */
public final class Post implements Serializable {

  private String id;
  private Type type;
  private User from;
  private String message;
  private String statusType;
  private List<User> likes;
  private Calendar createdDate;

  public static enum Type {
    LINK, STATUS, PHOTO, VIDEO, OFFER, EVENT
  }

  public Post(String id, Type type, User from, String message, String statusType, List<User> likes, Calendar createdDate) {
    this.id = id;
    this.type = type;
    this.from = from;
    this.message = (message == null ? "" : message);
    this.statusType = (statusType == null ? "" : statusType);
    this.likes = likes;
    this.createdDate = createdDate;
  }

  public Post(String postJson) {
    if (postJson == null) {
      throw new IllegalArgumentException();
    }

    try {
      JSONObject postObject = new JSONObject(postJson);

      // Get post ID
      String id = postObject.getString("id");

      // Get post type
      Post.Type type = Post.Type.valueOf(postObject.getString("type").toUpperCase());

      // Get post message
      String message = postObject.has("message") ? postObject.getString("message") : "";

      // Get status type
      String statusType = postObject.has("status_type") ? postObject.getString("status_type") : null;

      // Get created date
      String createdDateString = postObject.has("created_time") ? postObject.getString("created_time") : null;
      Calendar createdDate = null;
      if (createdDateString != null) {
        createdDate = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
          createdDate.setTime(sdf.parse(createdDateString));// all done
        } catch (ParseException e) {
          e.printStackTrace();
        }
      }

      // Get poster
      User from = null;
      boolean hasFrom = postObject.has("from");
      if (hasFrom) {
        JSONObject fromObject = postObject.getJSONObject("from");
        from = new User(Long.valueOf(fromObject.getString("id")), fromObject.getString("name"));
      }

      // Get likes
      List<User> likes = new ArrayList<User>();
      boolean hasLikes = postObject.has("likes");
      if (hasLikes) {
        JSONArray likesArray = postObject.getJSONObject("likes").getJSONArray("data");
        for (int i = 0; i < likesArray.length(); i++) {
          JSONObject likerObject = (JSONObject)likesArray.get(i);
          User liker = new User(Long.valueOf(likerObject.getString("id")), likerObject.getString("name"));
          likes.add(liker);
        }
      }

      this.id = id;
      this.type = type;
      this.from = from;
      this.message = message;
      this.statusType = statusType;
      this.likes = likes;
      this.createdDate = createdDate;
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public String getId() {
    return id;
  }

  public Type getType() {
    return type;
  }

  public User getFrom() {
    return from;
  }

  public String getMessage() {
    return message;
  }

  public String getStatusType() {
    return statusType;
  }

  public List<User> getLikes() {
    return likes;
  }

  public Calendar getCreatedDate() {
    return createdDate;
  }
}
