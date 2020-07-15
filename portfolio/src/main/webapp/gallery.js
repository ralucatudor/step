const totalImagesNumber = 6;

function getGallery() {
    wrapper.innerHTML = `
        <p>Photography sparks my interest - to me, taking a photo means freezing a moment that would, otherwise, be impossible to reproduce.</p>
        
        <p>Here are some photos I took or were taken of me:</p>
    `;
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