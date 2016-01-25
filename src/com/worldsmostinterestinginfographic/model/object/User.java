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

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

import java.io.Serializable;

/**
 * Represents a Facebook User object.
 *
 * @see <a href="https://developers.facebook.com/docs/graph-api/reference/v2.5/user">https://developers.facebook.com/docs/graph-api/reference/v2.5/user</a>
 */
public final class User implements Serializable {

  private long id;
  private String name;

  public User(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public User(String userJson) {
    if (userJson == null) {
      throw new IllegalArgumentException();
    }

    try {
      JSONObject userObject = new JSONObject(userJson);
      this.id = Long.valueOf(userObject.getString("id"));
      this.name = userObject.getString("name");
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof User)) {
      return false;
    }

    return id == ((User)obj).getId();
  }

  @Override
  public int hashCode() {
    return (int)id;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
