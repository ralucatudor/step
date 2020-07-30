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

import com.google.sps.data.User;

public final class AuthenticatedUser extends User {

  private final String email;
  private final String username;
  private final String logoutURL;

  public AuthenticatedUser(String email, String username, String logoutURL) {
    super(true);

    this.email = email;
    this.username = username;
    this.logoutURL = logoutURL;
  }

  public String getEmail() {
    return email;
  }

  public String getUsername() {
      return username;
  }

  public String getLogoutURL() {
    return logoutURL;
  }
}