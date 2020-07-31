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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
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
   * Handles the GET requests to "/user" path.
   * Returns a JSON object with the user information.
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

      // Load user data from the database
      Entity entity = getUser(userId);
      // Is the user data has not been stored in the database, then create the corresponding entity
      if (entity == null) {
        entity = new Entity("User", userId);
        entity.setProperty("id", userId);
        entity.setProperty("email", userEmail);
        // Bu default, set the username to the email address without the domain 
        entity.setProperty("username", (userEmail.split("@")[0]));

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(entity);
      }
    
      user = new AuthenticatedUser(userId, 
                                   userEmail, 
                                   (String) entity.getProperty("username"), 
                                   logoutURL);
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

  /**
   * Returns the User entity from the database based on the user's ID
   */
  public Entity getUser(String id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
        new Query("User")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();

    return entity;
  }

  /**
   * Handles the POST requests to "/user" path.
   * Changes the username in the database with the newly submitted username.
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    // Make sure the user is authenticated
    if (userService.isUserLoggedIn()) {
      String id = userService.getCurrentUser().getUserId();
      Entity entity = getUser(id);

      // Make sure the database entity has been created
      if (entity != null) {
        // Take the username input from the POST request and update the datastore
        String newUsername = request.getParameter("new-username");
        entity.setProperty("username", newUsername);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(entity);

        // Redirect the user back to the Comments page, which shows all the added comments
        response.sendRedirect("/#comments");
      }
    }
  }
}
