const totalImagesNumber = 6;

function getGallery() {
    wrapper.textContent = `Here are some photos!`;
    const imageContainer = document.createElement('div');
    imageContainer.setAttribute('class', 'image-container')

    for (let imageIndex = 1; imageIndex <= totalImagesNumber; imageIndex++) {
        const imgUrl = 'images/image-' + imageIndex + '.jpg';
        const imgElement = document.createElement('img');
        imgElement.src = imgUrl;
        imageContainer.appendChild(imgElement);
    }
    wrapper.appendChild(imageContainer);
}