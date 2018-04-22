let ractive = new Ractive({
  el: '#container',
  template: '#template',
  data: {}
});
let pollId = "";

$("#submit").click(function(e) {
    let postParameter = {
        name: document.getElementById('name').value, 
        title: document.getElementById('title').value, 
        location: document.getElementById('location').value, 
        date: document.getElementById('date-format').value, 
        message: document.getElementById('message').value
     };
    for (var key in postParameter) {
        if (postParameter[key] == "" && key != message) {
          alert("Please enter a " + key);
        ractive.toggle( 'flipCard' );
          return;
        }
    }
    $.post("/home", postParameter, response => { 
        const responseObject = JSON.parse(response);
        console.log(responseObject);
        pollId = "" + responseObject.pollId; 
        let title = "" + responseObject.pollTitle; 
        let location = "" + responseObject.location;
        let date = "" + responseObject.date; 
        document.getElementById('pollTitle').innerHTML = "Poll for " + title + " at " + location + " on " + date; 
        document.getElementById('pollInfo').innerHTML = 'This is your URL:  ';
        let a = document.createElement('a');
        let linkText = document.createTextNode('localhost:4567/poll/:id?'+pollId);
        a.appendChild(linkText);
        a.title = 'localhost:4567/poll/:id?'+pollId;
        a.href = '/poll/:id?'+pollId;
        document.getElementById('pollInfo').appendChild(a);
    });
     setTimeout(function(){
    let targeted_popup_class = jQuery('[data-popup-open]').attr('data-popup-open');
    $('[data-popup="' + targeted_popup_class + '"]').fadeIn(350);
    e.preventDefault(); } , 2000);
}
);

$(function() {
//----- CLOSE
$('[data-popup-close]').on('click', function(e)  {
let targeted_popup_class = jQuery(this).attr('data-popup-close');
$('[data-popup="' + targeted_popup_class + '"]').fadeOut(350);
e.preventDefault();
});
});

$("#goToPoll").click(function(e){
        location.href = 'localhost:4567/poll/:id?'+pollId;
});

function initAutocomplete() {
  var input = document.getElementById('location');
  var searchBox = new google.maps.places.SearchBox(input);
  // Listen for the event fired when the user selects a prediction and retrieve
  // more details for that place.
  searchBox.addListener('places_changed', function() {
    var places = searchBox.getPlaces();

    if (places.length == 0) {
      return;
    }
    // For each place, get the icon, name and location.
    var bounds = new google.maps.LatLngBounds();
    places.forEach(function(place) {
      if (!place.geometry) {
        console.log("Returned place contains no geometry");
        return;
      }
      var icon = {
        url: place.icon,
        size: new google.maps.Size(71, 71),
        origin: new google.maps.Point(0, 0),
        anchor: new google.maps.Point(17, 34),
        scaledSize: new google.maps.Size(25, 25)
      };
      console.log(place.geometry.location.lat());
      console.log(place.geometry.location.lng());

      if (place.geometry.viewport) {
        // Only geocodes have viewport.
        bounds.union(place.geometry.viewport);
      } else {
        bounds.extend(place.geometry.location);
      }
    });
  });
}