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

package com.google.sps.data;

import com.google.auto.value.AutoValue;
import java.util.Date;

/** Class containing comment data. */
@AutoValue
public abstract class Comment {

  /** 
   * Class constructor.
   *
   * @param id     the id of the Comment Entity held by Datastore
   * @param date   the date when the comment was added by the user
   * @param text   the comment message
   * @param author the username of the user who added the comment
   * @param email  the email of the user who added the comment
   */
  public static Comment create(long id, Date date, String text, String author, String email, Double sentimentScore) {
    return new AutoValue_Comment(id, date, text, author, email, sentimentScore);
  }

  public abstract long getId();
  public abstract Date getDate();
  public abstract String getText();
  public abstract String getAuthor();
  public abstract String getEmail();
  public abstract Double getSentimentScore();
}
