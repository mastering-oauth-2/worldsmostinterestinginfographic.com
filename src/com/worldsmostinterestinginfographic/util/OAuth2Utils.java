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

package com.worldsmostinterestinginfographic.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * OAuth 2 utilities.
 *
 * This enum contains static utility methods for invoking OAuth 2 workflows.
 */
public enum OAuth2Utils {
  INSTANCE;

  private static final Logger log = Logger.getLogger(OAuth2Utils.class.getName());

  /**
   * Request an access token from the given token endpoint.
   *
   * The token endpoint given must contain all of the required properties necessary for the request.  At a minimum,
   * this will include:
   *
   *   grant_type
   *   code
   *   redirect_uri
   *   client_id
   *
   * If the authorization code is valid and the request is successful, the access token value will be parsed from the
   * response and returned.  If the request has failed for any reason, null will be returned.
   *
   * @param tokenEndpoint The full token endpoint, with required parameters for making the access token request
   * @return A valid access token if the request was successful; null otherwise.
   */
  public static String requestAccessToken(String tokenEndpoint) {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    try {
      // Exchange authorization code for access token
      HttpPost httpPost = new HttpPost(tokenEndpoint);
      HttpResponse httpResponse = httpClient.execute(httpPost);
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
      String line = bufferedReader.readLine();

      // Detect error message
      if (line.toLowerCase().contains("\"error\"")) {
        log.severe("Fatal exception occurred while making the access token request: " + line);
        return null;
      }

      // Extract access token
      String accessToken = line.split("&")[0].split("=")[1];
      if (StringUtils.isEmpty(accessToken)) {
        log.severe("Fatal exception occurred while making the access token request: Access token value in response is empty");
        return null;
      }

      return accessToken;
    } catch(Exception e) {
      log.severe("Fatal exception occurred while making the access token request: " + e.getMessage());
      e.printStackTrace();
    } finally {
      try {
        httpClient.close();
      } catch (IOException e) {
        log.severe("Fatal exception occurred while closing HTTP client connection: " + e.getMessage());
        e.printStackTrace();
      }
    }

    return null;
  }

  /**
   * Make a protected resource request at the given endpoint with the given access token.
   *
   * This method will attempt to access the protected resource with the given access token using the authorization
   * request header field method.  If successful, the full response string will be returned.
   *
   * @param resourceEndpoint The endpoint of the protected resource to access
   * @param accessToken A valid access token with the necessary scopes required to access the protected resource
   * @return The result string returned in response to the request to access the protected resource with the given token
   */
  public static String makeProtectedResourceRequest(String resourceEndpoint, String accessToken) {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    try {
      httpClient = HttpClients.createDefault();

      // Add authorization header to POST request
      HttpPost httpPost = new HttpPost(resourceEndpoint);
      httpPost.addHeader("Authorization", "Bearer " + accessToken);

      /*
       * Note: The addition of the "method=get" URL-encoded form parameter is necessary for the Facebook Graph APIs.
       *       Other OAuth 2 facebook providers may not require this, and some may even reject it.
       */
      List<NameValuePair> urlParameters = new ArrayList<>();
      urlParameters.add(new BasicNameValuePair("method", "get"));
      httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));

      // Make the call
      HttpResponse httpResponse = httpClient.execute(httpPost);

      // Process the response
      String response = "";
      String currentLine;
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
      while ((currentLine = bufferedReader.readLine()) != null) {
        response += currentLine;
      }

      return response;
    } catch (IOException e) {
      log.severe("Fatal exception occurred while making the protected resource request (access token " +
                 LoggingUtils.anonymize(accessToken) + "): " + e.getMessage());
      e.printStackTrace();
    } finally {
      try {
        httpClient.close();
      } catch (IOException e) {
        log.severe("Fatal exception occurred while closing HTTP client connection (access token=" +
                   LoggingUtils.anonymize(accessToken) + "): " + e.getMessage());
        e.printStackTrace();
      }
    }

    return null;
  }
}
