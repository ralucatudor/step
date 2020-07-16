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

let sections = ["About Me", "Projects", "Contact", "Gallery"];

sections.forEach((sectionName) => {
    const navbarSection = document.createElement("li");
    const navbarSectionLink = document.createElement("a");

    navbarSectionLink.setAttribute("href", "javascript:get" + sectionName.replace(/\s/g, '') + "()");   // \s is the regex for "whitespace"
    navbarSectionLink.textContent = sectionName;
    navbarSection.appendChild(navbarSectionLink);
    
    navbarList.appendChild(navbarSection);
});

navbar.appendChild(navbarList);

// First section that opens is About Me
getAboutMe();

// Functions called by clicking on the sections from the navbar
function getAboutMe() {
    $("#wrapper").load("about-me.html");
}

function getProjects() {
    $("#wrapper").load("projects.html");
}

function getContact() {
    $("#wrapper").load("contact-me.html");
}

function getGallery() {
    const wrapper = document.getElementById("wrapper");
    wrapper.textContent = '';

    const galleryTitle = document.createElement("h1");
    galleryTitle.textContent = "Gallery";
    wrapper.appendChild(galleryTitle);

    const galleryText = document.createElement("p");
    galleryText.textContent = "Photography sparks my interest - to me, taking a photo means freezing a moment that would, otherwise, be impossible to reproduce. Here are some photos I took or were taken of me:";
    wrapper.appendChild(galleryText);

    const imageContainer = document.createElement("div");
    imageContainer.setAttribute("class", "image-container")

    const totalImagesNumber = 6;
    
    for (let imageIndex = 1; imageIndex <= totalImagesNumber; imageIndex++) {
        const imgUrl = "images/image-" + imageIndex + ".jpg";
        const imgElement = document.createElement("img");
        imgElement.src = imgUrl;
        imgElement.alt = "Photo with/ by Raluca";
        imageContainer.appendChild(imgElement);
    }
    wrapper.appendChild(imageContainer);
}
