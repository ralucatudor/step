// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.data.AnonymousUser;
import com.google.sps.data.AuthenticatedUser;
import com.google.sps.data.User;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles user accounts */
@WebServlet("/user")
public class UserServlet extends HttpServlet {

  /**
   * Method that handles the GET requests to "/user" path
   * Returns a JSON object describing the current user
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    User user;
    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {
      String userId = userService.getCurrentUser().getUserId();
      String userEmail = userService.getCurrentUser().getEmail();
      String urlToRedirectToAfterUserLogsOut = "/";
      String logoutURL = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);

      user = new AuthenticatedUser(userEmail, userEmail.split("@")[0], logoutURL);
    } else {
      String urlToRedirectToAfterUserLogsIn = "/#comments";
      String loginURL = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);

      user = new AnonymousUser(loginURL);
    }

    // Convert the user to JSON
    Gson gson = new Gson();
    String json = gson.toJson(user);

    // Send the JSON as the response
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
}
