/**
 * Creates a map and adds it to the page.
 */
function createMap() {
  const homeCoordinates = new google.maps.LatLng(44.425541, 26.104567);
  const universityCoordinates = new google.maps.LatLng(44.435534, 26.099586);

  const map = new google.maps.Map(
    document.getElementById('map'),
    {
      center: {lat: 44.431410, lng: 26.103635},
      zoom: 13,
      // Style the map in Dark Mode
      styles: [                 
        {elementType: 'geometry', stylers: [{color: '#242f3e'}]},
        {elementType: 'labels.text.stroke', stylers: [{color: '#242f3e'}]},
        {elementType: 'labels.text.fill', stylers: [{color: '#746855'}]},
        {
          featureType: 'administrative.locality',
          elementType: 'labels.text.fill',
          stylers: [{color: '#d59563'}],
        },
        {
          featureType: 'poi',
          elementType: 'labels.text.fill',
          stylers: [{color: '#d59563'}],
        },
        {
          featureType: 'poi.park',
          elementType: 'geometry',
          stylers: [{color: '#263c3f'}],
        },
        {
          featureType: 'poi.park',
          elementType: 'labels.text.fill',
          stylers: [{color: '#6b9a76'}],
        },
        {
          featureType: 'road',
          elementType: 'geometry',
          stylers: [{color: '#38414e'}],
        },
        {
          featureType: 'road',
          elementType: 'geometry.stroke',
          stylers: [{color: '#212a37'}],
        },
        {
          featureType: 'road',
          elementType: 'labels.text.fill',
          stylers: [{color: '#9ca5b3'}],
        },
        {
          featureType: 'road.highway',
          elementType: 'geometry',
          stylers: [{color: '#746855'}],
        },
        {
          featureType: 'road.highway',
          elementType: 'geometry.stroke',
          stylers: [{color: '#1f2835'}],
        },
        {
          featureType: 'road.highway',
          elementType: 'labels.text.fill',
          stylers: [{color: '#f3d19c'}],
        },
        {
          featureType: 'transit',
          elementType: 'geometry',
          stylers: [{color: '#2f3948'}],
        },
        {
          featureType: 'transit.station',
          elementType: 'labels.text.fill',
          stylers: [{color: '#d59563'}],
        },
        {
          featureType: 'water',
          elementType: 'geometry',
          stylers: [{color: '#17263c'}],
        },
        {
          featureType: 'water',
          elementType: 'labels.text.fill',
          stylers: [{color: '#515c6d'}],
        },
        {
          featureType: 'water',
          elementType: 'labels.text.stroke',
          stylers: [{color: '#17263c'}],
        },
      ],
    }
  );

  // Add markers
  const homeMarker = new google.maps.Marker({
    position: homeCoordinates, 
    map: map,
    title: 'Home',
    animation: google.maps.Animation.DROP,
  });
  const universityMarker = new google.maps.Marker({
    position: universityCoordinates, 
    map: map,
    title: 'University',
    animation: google.maps.Animation.DROP,
  });
  
  // Add info windows to markers
  addInfoWindow(map, homeMarker, 'I have lived here since 2015.');
  addInfoWindow(map, universityMarker, '<h3>University of Bucharest</h3>' + 
                                       '<p>I am currently studying at the' +
                                       'Faculty of Mathematics and Informatics.</p>');

  // Add animation to markers
  homeMarker.addListener('click', () => toggleBounce(homeMarker));
  universityMarker.addListener('click', () => toggleBounce(universityMarker));
}

/**
 * Adds an info window to a marker.
 */
function addInfoWindow(map, marker, text) {
  // Create the info window
  const infoWindow = new google.maps.InfoWindow({
    content: text
  });

  // Open the info window when mouse over marker
  marker.addListener('mouseover', function() {
    infoWindow.open(map, marker);
  });

  // Close the info window when mouse out
  marker.addListener('mouseout', function() {
    infoWindow.close(map, marker);
  });
}

/**
 * Adds bouncing animation to a marker.
 */
function toggleBounce(marker) {
  if (marker.getAnimation() !== null) {
    marker.setAnimation(null);
  } else {
    marker.setAnimation(google.maps.Animation.BOUNCE);
  }
}
