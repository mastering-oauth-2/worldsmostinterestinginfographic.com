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

package com.worldsmostinterestinginfographic.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Properties;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;

/**
 * Singleton model for statically encapsulating application properties.
 */
public enum Model {
  INSTANCE;

  public static final String CLIENT_ID;
  public static final String CLIENT_SECRET;
  public static final String REDIRECTION_ENDPOINT;
  public static final String AUTHORIZATION_ENDPOINT;
  public static final String TOKEN_ENDPOINT;

  public static final String FACEBOOK_API_ENDPOINT;
  public static final String FACEBOOK_REQUESTED_PROFILE_FIELDS;
  public static final String FACEBOOK_REQUESTED_FEED_FIELDS;
  public static final int MAX_NUMBER_OF_FACEBOOK_POSTS_TO_REQUEST;

  public static Cache cache;

  private static final Logger log = Logger.getLogger(Model.INSTANCE.getClass().getName());

  static {
    // Initialize the cache
    try {
      CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
      cache = cacheFactory.createCache(Collections.emptyMap());
    } catch (CacheException e) {
      log.severe("Exception creating cache: " + e.getMessage() + ": " + e.getStackTrace());
      cache = null;
    }

    // Read important properties from file
    Properties properties = new Properties();
    try {
      properties.load(new FileInputStream("WEB-INF/project.properties"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    CLIENT_ID = properties.getProperty("clientId");
    CLIENT_SECRET = properties.getProperty("clientSecret");
    REDIRECTION_ENDPOINT = properties.getProperty("redirectionEndpoint");
    AUTHORIZATION_ENDPOINT = properties.getProperty("authorizationEndpoint");
    TOKEN_ENDPOINT = properties.getProperty("tokenEndpoint");

    FACEBOOK_API_ENDPOINT = "https://graph.facebook.com/v2.5/";
    MAX_NUMBER_OF_FACEBOOK_POSTS_TO_REQUEST = 200;
    FACEBOOK_REQUESTED_PROFILE_FIELDS = "id,birthday,hometown,name,website,work";
    FACEBOOK_REQUESTED_FEED_FIELDS = "id,name,type,message,status_type,created_time,from,likes%7Bid,name%7D";
  }
}
