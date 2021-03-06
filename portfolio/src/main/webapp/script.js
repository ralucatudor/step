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

window.onload = function() {
  switch (window.location.hash) {
    case '#projects':
      getProjects();
      break;
    case '#contact':
      getContact();
      break;
    case '#gallery':
      getGallery();
      break;
    case '#comments':
      getComments();
      break;
    default:
      // First section that opens is About Me
      getAboutMe();
  }
}

// Get content wrapper
const wrapper = document.getElementById('wrapper');

// Add Navigation Bar
const navbar = document.getElementById('navigation-bar');

const navbarList = document.createElement('ul');
navbarList.setAttribute('class', 'navbar-list');

const sections = ['About Me', 'Projects', 'Contact', 'Gallery', 'Comments'];

sections.forEach((sectionName) => {
  const navbarSection = document.createElement('li');
  const navbarSectionLink = document.createElement('a');

  // The whitespaces in the sectionName string are removed for calling the corresponding function
  navbarSectionLink.setAttribute('href', `javascript:get${sectionName.replace(/\s/g, '')}()`);
  navbarSectionLink.textContent = sectionName;
  navbarSection.appendChild(navbarSectionLink);

  navbarList.appendChild(navbarSection);
});

navbar.appendChild(navbarList);

/**
 * Callback when the user scrolls the page: add 'fixed-navbar' class to navbar when the top
 * of the navbar is reached. Remove 'fixed-navbar' class when scrolling back to the top.
 */
window.onscroll = onWindowScrolled;

const navbarPaddingTop = $('.navbar-list a')[0].style.paddingTop;
// Get the offset position of the navbar
const navbarOffsetTop = navbar.offsetTop - navbarPaddingTop;

function onWindowScrolled() {
  if (window.pageYOffset >= navbarOffsetTop) {
    navbar.classList.add('fixed-navbar');
    /**
     * Add top padding to the content wrapper to prevent sudden quick movement,
     * as the navbar gets a new position at the top of the page.
     */
    wrapper.style.paddingTop = navbar.offsetHeight + 'px';
  } else {
    navbar.classList.remove('fixed-navbar');
    wrapper.style.paddingTop = 0;
  }
}

// Fetches a greeting from the server and adds it to the DOM.
function getGreeting() {
  fetch('/greeting').then(response => response.text()).then((greeting) => {
    document.getElementById('greeting-container').innerText = greeting;
  });
}

// Functions called by clicking on the sections from the navbar
function getAboutMe() {
  window.location.hash = 'about-me';
  $('#wrapper').load('about-me.html');
}

function getProjects() {
  window.location.hash = 'projects';
  $('#wrapper').load('projects.html');
}

function getContact() {
  window.location.hash = 'contact';
  // Use createMap() function as callback to load the map into the map div
  $('#wrapper').load('contact-me.html', createMap);
}

function getGallery() {
  window.location.hash = 'gallery';

  wrapper.textContent = '';

  const galleryTitle = document.createElement('h1');
  galleryTitle.textContent = 'Gallery';
  wrapper.appendChild(galleryTitle);

  const galleryText = document.createElement('p');
  galleryText.textContent = 'Photography sparks my interest - to me, taking a photo ' +
    'means freezing a moment that would, otherwise, be impossible to reproduce. ' +
    'Here are some photos I took or were taken of me:';
  wrapper.appendChild(galleryText);

  const imageContainer = document.createElement('div');
  imageContainer.setAttribute('class', 'image-container')

  const totalImagesNumber = 6;

  for (let imageIndex = 1; imageIndex <= totalImagesNumber; imageIndex++) {
    const imgUrl = `images/image-${imageIndex}.jpg`;
    const imgElement = document.createElement('img');
    imgElement.src = imgUrl;
    imgElement.alt = 'Photo with/ by Raluca';
    imageContainer.appendChild(imgElement);
  }
  wrapper.appendChild(imageContainer);
}

function getComments() {
  window.location.hash = 'comments';
  $('#wrapper').load('comments.html', setDisplayCommentsElements);
}

/**
 * Sets values for the CSS 'display' property for elements in the Comments section 
 * corresponding to the login status of the user.
 */
function setDisplayCommentsElements() {
  // Values of the CSS 'display' property for elements that an Authenticated User should see
  const authenticatedUserElements = {
    'new-comment-button' : 'inline',
    'logout-link-container' : 'inline',
    'change-username-button' : 'inline',
  };

  // Values of the CSS 'display' property for elements that an Anonymous User should see
  const anonymousUserElements = {
    'login-alert' : 'inline',
  };

  // Make a GET request to '/user' to get user information
  fetch('/user').then(response => response.json()).then((user) => {
    // Check user's login status
    if (user.isAuthenticated) {
      document.getElementById('logout-link').href = user.logoutURL;
      displayElements(authenticatedUserElements);

      document.getElementById('login-logout-information').innerHTML = 
          `You are successfully authenticated using the email address ${user.email}. ` +
          `Please note that you will be logged out of your Google account when clicking "Logout"!`;
    } else {
      document.getElementById('login-link').href = user.loginURL;
      displayElements(anonymousUserElements);

      document.getElementById('login-logout-information').innerHTML = ``;
    }
  });
}

/**
 * Manipulates DOM to set the value for the 'display' property of given elements
 * @param {Object} elements - elementId : displayValue pairs
 */
function displayElements(elements) {
  for (const [elementId, displayValue] of Object.entries(elements)) {
    document.getElementById(elementId).style.display = displayValue;
  }
}

function getCommentForm() {
  $('#wrapper').load('comment-form.html');
}

function getUsernameForm() {
  $('#wrapper').load('username-form.html');
}

function getCommentsFromServer() {
  // Get the maximum number of comments from the user input.
  const maxCommentsNumber = document.getElementById("max-comments-number").value;

  // Fetch comments from the server and add them to the DOM
  let fetchURL;
  if (maxCommentsNumber === null) {
    fetchURL = '/data';
  } else {
    fetchURL = `/data?max-comments=${maxCommentsNumber}`;
  }
  
  const commentsContainer = document.getElementById('comments-container');
  commentsContainer.innerHTML = '';
  
  const warningContainer = document.getElementById('warning-container');
  warningContainer.innerHTML = '';

  fetch(fetchURL)
    .then((response) => {
      if (!response.ok) {
        throw Error(response.statusText);
      }
      return response.json();
    }).then((comments) => {
      comments.forEach((comment) => {
        commentsContainer.appendChild(createCommentElement(comment));
      })
    })
    .catch(error => {
      console.log(error);
      const warning = document.createElement('p');
      warning.innerText = `Encountered "${error}".\
        Please introduce a non-negative integer or leave the input empty.`
      warningContainer.appendChild(warning);
    });

  wrapper.appendChild(commentsContainer);
}

// Creates an element that represents a comment, including its delete button
function createCommentElement(comment) {
  const commentElement = document.createElement('li');
  commentElement.className = 'comment-box';

  const commentAuthor = document.createElement('div');
  commentAuthor.innerText = comment.author;
  commentAuthor.className = 'comment-author';

  const commentText = document.createElement('div');
  commentText.innerText = comment.text;

  // Set the commentText's color property corresponding to the sentimentScore
  const commentTextColor = getSentimentColor(comment.sentimentScore);
  commentText.style.color = 
      `rgb(${commentTextColor.r}, ${commentTextColor.g}, ${commentTextColor.b})`; 

  const commentDate = document.createElement('span');
  commentDate.innerHTML = `posted on: ${comment.date}`;
  commentDate.className = 'time-span';

  // Add button that deletes the comment
  const deleteButtonElement = document.createElement('button');
  deleteButtonElement.innerText = 'Delete';
  deleteButtonElement.className = 'delete-comment-button';
  deleteButtonElement.addEventListener('click', () => {
    deleteComment(comment);

    // Remove the comment from the DOM
    commentElement.remove();
  });

  commentElement.appendChild(commentDate);
  commentElement.appendChild(commentAuthor);
  commentElement.appendChild(commentText);
  commentElement.appendChild(deleteButtonElement);
  return commentElement;
}

/**
 * Returns a color from red to green as an RGB value, corresponding to the sentimentScore.
 * @param {Number} sentimentScore - float between [-1.0, 1.0]
 */
function getSentimentColor(sentimentScore) {
  // Initialize the colors
  const red = {r : 187, g : 68, b : 48};
  const yellow = {r : 254, g : 198, b : 1};
  const green = {r : 43, g : 147, b : 72};

  // Handle wrong input cases
  sentimentScore = Math.min(1, Math.max(-1, sentimentScore));

  // If sentimentScore is in [-1, 0), get an interpolated color from red to yellow.
  if (sentimentScore < 0) {
    return interpolateColor(red, yellow, sentimentScore + 1);
  }

  // Else, if sentimentScore is in [0, 1], get an interpolated color from yellow to green.
  return interpolateColor(yellow, green, sentimentScore);
}

/**
 * Returns an interpolated color between colorStart and colorStop
 * @param {Object} colorStart - rgb() tuple
 * @param {Object} colorStop - rgb() tuple
 * @param {Number} variation - float between [0.0, 1.0]
 */
function interpolateColor(colorStart, colorStop, variation) {  
  // Interpolate every component of the colors
  return {
    r : interpolateValue(colorStart.r, colorStop.r, variation),
    g : interpolateValue(colorStart.g, colorStop.g, variation),
    b : interpolateValue(colorStart.b, colorStop.b, variation),
  };
}

/**
 * Returns an interpolated value between two values. 
 * @param {Number} variation - float between [0.0, 1.0]
 */
function interpolateValue(startValue, stopValue, variation) {
  return startValue + variation * (stopValue - startValue);
}

// Tells the server to delete the comment
function deleteComment(comment) {
  const params = new URLSearchParams();
  params.append('id', comment.id);
  fetch('/delete-comment', {
    method: 'POST',
    body: params
  });
}
