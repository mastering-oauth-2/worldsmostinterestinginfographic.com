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

import com.worldsmostinterestinginfographic.model.Model;
import com.worldsmostinterestinginfographic.model.object.User;
import com.worldsmostinterestinginfographic.service.FacebookService;
import com.worldsmostinterestinginfographic.util.LoggingUtils;
import com.worldsmostinterestinginfographic.util.OAuth2Utils;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet to handle callback after an authorization request.
 */
public class CallbackServlet extends HttpServlet {

  private static final Logger log = Logger.getLogger(CallbackServlet.class.getName());

  private final FacebookService facebookService;

  public CallbackServlet() {
    facebookService = new FacebookService();
  }

  /**
   * Servlet to handle initial callback in response to an authorization request.
   *
   * Will check for the presence of an authorization code.  If present, will attempt to make an access token request
   * using the authorization code.  This is all done in accordance with the authorization code grant workflow in the
   * OAuth 2 specification [RFC 6749]. If no authorization code is detected, the authorization code is expired, or any
   * other errors occur, the user will be sent to an error page.
   *
   * @param request  The HTTP request sent by the client
   * @param response The HTTP response that the server will send back to the client
   *
   * @see <a href="https://tools.ietf.org/html/rfc6749">RFC 6749 - The OAuth 2.0 Authorization Framework</a>
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    // Check for the presence of an authorization code
    String authorizationCode = request.getParameter("code");
    if (!StringUtils.isEmpty(authorizationCode)) {

      // Get access token
      log.info(
          "[" + request.getSession().getId() + "] Starting session.  Requesting access token with authorization code "
          + LoggingUtils.anonymize(authorizationCode));

      String tokenEndpoint =
          Model.TOKEN_ENDPOINT + "?grant_type=authorization_code&code=" + authorizationCode + "&redirect_uri="
          + URLEncoder.encode((request.getScheme() + "://" + request.getServerName() + Model.REDIRECTION_ENDPOINT),
                              StandardCharsets.UTF_8.name()) + "&client_id=" + Model.CLIENT_ID + "&client_secret="
          + Model.CLIENT_SECRET;
      String accessToken = OAuth2Utils.requestAccessToken(tokenEndpoint);
      if (StringUtils.isEmpty(accessToken)) {
        response.sendRedirect("/uh-oh");
        return;
      }

      // Get profile data
      log.info("[" + request.getSession().getId() + "] Access token " + LoggingUtils.anonymize(accessToken)
               + " received.  Requesting profile data.");

      User user = facebookService.getProfile(accessToken);
      if (user == null) {
        response.sendRedirect("/uh-oh");
        return;
      }

      // Here we go
      log.info("[" + request.getSession().getId() + "] Hello, " + LoggingUtils.anonymize(Objects.toString(user.getId()))
               + "!");

      Model.cache.put(request.getSession().getId() + ".profile", user);
      Model.cache.put(request.getSession().getId() + ".token", accessToken);

      response.sendRedirect("/you-rock");

    } else if (request.getParameter("error") != null) {

      // An error happened during authorization code request
      String error = request.getParameter("error");
      String errorDescription = request.getParameter("error_description");

      request.getSession().setAttribute("error", error);
      request.getSession().setAttribute("errorDescription", errorDescription);

      log.severe("[" + request.getSession().getId() + "] Error encountered during authorization code request: " +
                 error + " - " + errorDescription);
      response.sendRedirect("/uh-oh");

    } else {
      log.warning("[" + request.getSession().getId()
                  + "] No authorization code or error message detected at redirection endpoint");
      response.sendRedirect("/uh-oh");
    }
  }
}
