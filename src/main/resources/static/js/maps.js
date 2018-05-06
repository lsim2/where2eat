// initializes map and centers it on the USA. 
function initMap() {
  let uluru = {lat: -37.0902, lng: 95.7129};
  let map = new google.maps.Map(document.getElementById('map'), {
    zoom: 4,
    center: uluru
  });

}
