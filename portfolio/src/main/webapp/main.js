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

// Add Navigation Bar
const navbar = document.getElementById("navigation-bar");

const navbarList = document.createElement("ul");
navbarList.setAttribute("class", "navbar-list");
navbarList.innerHTML = `
    <li><a href="javascript:getInfo()">About Me</a></li>
    <li><a href="javascript:getProjects()">Projects</a></li>
    <li><a href="javascript:getContact()">Contact</a></li>
    <li><a href="javascript:getGallery()">Gallery</a></li>
    `;

navbar.appendChild(navbarList);