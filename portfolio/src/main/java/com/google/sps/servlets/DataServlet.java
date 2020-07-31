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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private static final int REQUESTED_COMMENTS_LIMIT = 100;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    int maxCommentsNumber;

    // Check if max-comments parameter exists (it is an optional parameter)
    if (request.getParameterMap().containsKey("max-comments")) {
      // Try to convert the value from the 'max-comments' field of the query string to int
      try {
        maxCommentsNumber = Integer.parseInt(request.getParameter("max-comments"));
      } catch (NumberFormatException e) {
        maxCommentsNumber = REQUESTED_COMMENTS_LIMIT;
      }
    } else {
      // If max-comments parameter is not set, fetch the requested comments limit to support pagination
      maxCommentsNumber = REQUESTED_COMMENTS_LIMIT;
    }

    // If maxCommentsNumber is negative, send error
    if (maxCommentsNumber < 0) {
      response.sendError(400, "max-comments cannot be negative");
    }
    
    // Create query for sorting comments by date from newest to oldest
    Query query = new Query("Comment").addSort("date", SortDirection.DESCENDING);

    // Load comment data from the database
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<Comment> comments = new ArrayList<>();
    for (Entity entity : results.asIterable(FetchOptions.Builder.withLimit(maxCommentsNumber))) {
      long id = entity.getKey().getId();
      Date date = (Date) entity.getProperty("date");
      String text = (String) entity.getProperty("text");
      String author = (String) entity.getProperty("author");
      String email = (String) entity.getProperty("email");

      Comment comment = Comment.create(id, date, text, author, email);
      comments.add(comment);
    }

    /** 
     * Convert comments List into a JSON string using Gson library and
     * send the JSON as the response
     */
    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Check that the user is authenticated before adding the comment
    UserService userService = UserServiceFactory.getUserService();

    if (userService.isUserLoggedIn()) {
      // Take the comment input from the POST request 
      String text = request.getParameter("comment-text");
      
      String userId = userService.getCurrentUser().getUserId();
      String email = userService.getCurrentUser().getEmail();

      Entity commentEntity = new Entity("Comment");
      commentEntity.setProperty("text", text);
      commentEntity.setProperty("author", getUsername(userId));
      commentEntity.setProperty("email", email);
      // The added date for the comment will be the current date.
      commentEntity.setProperty("date", new Date());

      // Store comment data in the database
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(commentEntity);
    }

    // Redirect the user back to the Comments page, which shows all the added comments
    response.sendRedirect("/#comments");
  }

  /**
   * Returns the username from the database based on the user's ID
   */
  private String getUsername(String id) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query =
        new Query("User")
            .setFilter(new Query.FilterPredicate("id", Query.FilterOperator.EQUAL, id));
    PreparedQuery results = datastore.prepare(query);
    Entity entity = results.asSingleEntity();
    if (entity == null) {
      return "";
    }
    String username = (String) entity.getProperty("username");
    return username;
  }
}
