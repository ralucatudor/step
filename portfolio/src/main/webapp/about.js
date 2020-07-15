getInfo();

function getInfo() {
    var wrapper = document.getElementById("wrapper");
    wrapper.innerHTML = `
        <p>This is my portfolio.</p>
        <p>Click here to get a random message:</p>
        <button onclick="addRandomGreeting()">Hello</button>
        <div id="greeting-container"></div>
    `;
}

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}