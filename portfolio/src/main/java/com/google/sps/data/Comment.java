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

import java.util.Date;

/** Class containing comment data. */
public final class Comment {

  private final Date date;
  private final String text;
  private final String author;

  public Comment(Date date, String text, String author) {
    this.date = date;
    this.text = text;
    this.author = author;
  }

  public Comment(String text, String author) {
    this.date = new Date();
    this.text = text;
    this.author = author;
  }

  public Date getDate() {
    return date;
  }

  public String getText() {
    return text;
  }

  public String getAuthor() {
    return author;
  }
}
