let ractive = new Ractive({
  el: '#container',
  template: '#template',
  data: {}
});
let pollId = "";
let postParameter = {};

$("#flip").click(function(e){
    document.getElementById('name').value = ""; 
    document.getElementById('title').value = "";
    document.getElementById('location').value = "";
    document.getElementById('date-format').value = "";
    document.getElementById('message').value = "";
});

$("#submit").click(function(e) {
    postParameter.name = document.getElementById('name').value; 
    postParameter.title = document.getElementById('title').value;
    postParameter.location = document.getElementById('location').value;
    postParameter.date = document.getElementById('date-format').value;
    postParameter.message = document.getElementById('message').value;
    for (var key in postParameter) {
        if (postParameter[key] == "" && key != message) {
          alert("Please enter a " + key);
          ractive.toggle( 'flipCard' );
          return;
        }
    }
    console.log(postParameter);
    $.post("/home", postParameter, response => { 
        const responseObject = JSON.parse(response);
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

$("#update").click(function(e){
        ractive.toggle( 'flipCard' );
        let targeted_popup_class = jQuery('[data-popup-close]').attr('data-popup-close');
        $('[data-popup="' + targeted_popup_class + '"]').fadeOut(350);
        e.preventDefault();
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
      postParameter.lat = place.geometry.location.lat();
      postParameter.lng = place.geometry.location.lng();
      if (place.geometry.viewport) {
        // Only geocodes have viewport.
        bounds.union(place.geometry.viewport);
      } else {
        bounds.extend(place.geometry.location);
      }
    });
  });
}