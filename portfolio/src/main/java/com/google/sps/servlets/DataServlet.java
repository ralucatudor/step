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
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
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
      Double sentimentScore = (Double) entity.getProperty("sentimentScore");

      Comment comment = Comment.create(id, date, text, author, sentimentScore);
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
    // Take the input from the POST request 
    String text = request.getParameter("text-input");
    String author = request.getParameter("author");
    // The added date for the comment will be the current date.
    Date date = new Date();

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("text", text);
    commentEntity.setProperty("author", author);
    commentEntity.setProperty("date", date);
    commentEntity.setProperty("sentimentScore", getSentimentScore(text));

    // Store comment data in the database
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    // Redirect the user back to the Comments page, which shows all the added comments
    response.sendRedirect("/#comments");
  }

  /**
   * Returns a value between -1 and 1, representing how negative or positive text is
   */
  private static float getSentimentScore(String text) throws IOException {
      // Create a new Document that contains the parameter - text - as content
      Document doc =
          Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();

      LanguageServiceClient languageService = LanguageServiceClient.create();

      // Pass the Document into the LanguageServiceClient created, 
      // which returns the analysis in a Sentiment instance
      Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
      // Get the score of the sentiment
      float score = sentiment.getScore();

      languageService.close();

      return score;
  }
}
