let ractive = new Ractive({
  el: '#container',
  template: '#template',
  data: {}
});
let pollId = "";
let postParameter = {};
let places;

let linkText;

$("#flip").click(function(e){
    document.getElementById('name').value = "";
    document.getElementById('title').value = "";
    document.getElementById('location').value = "";
    document.getElementById('date-format').value = "";
    document.getElementById('message').value = "";
});

$("#newForm").click(function(e){
    document.getElementById('name').value = "";
    document.getElementById('title').value = "";
    document.getElementById('location').value = "";
    document.getElementById('date-format').value = "";
    document.getElementById('message').value = "";
    ractive.toggle( 'flipCard' );
});

$("#submit").click(function(e) {
    postParameter.name = document.getElementById('name').value;
    postParameter.title = document.getElementById('title').value;
    postParameter.location = document.getElementById('location').value;
    postParameter.date = document.getElementById('date-format').value;
    postParameter.message = document.getElementById('message').value;
    for (let key in postParameter) {
        if (postParameter[key] == "" && key != message) {
          alert("Please enter a " + key);
          ractive.toggle( 'flipCard' );
          return;
        }
    }
    if(postParameter.lat == null || postParameter.lng == null){
        alert("Please enter a valid location");
        ractive.toggle( 'flipCard' );
      return ;
    }
    // let valid = false;
    // for(let place in places){
    //   console.log(places[place]);
    //   console.log(postParameter.location);
    //   if(postParameter.location == places[place].location){
    //     valid == true;
    //     break;
    //   }
    // }
    // if(valid == false){
    //   console.log("falsity");
    // }

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
        linkText = document.createTextNode('localhost:4567/poll/:id?'+pollId);
        a.appendChild(linkText);
        a.title = 'localhost:4567/poll/:id?'+pollId;
        a.href = '/poll/:id?'+pollId;
        a.target = "_blank";
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
  let input = document.getElementById('location');
  let searchBox = new google.maps.places.SearchBox(input);

  // Listen for the event fired when the user selects a prediction and retrieve
  // more details for that place.
  searchBox.addListener('places_changed', function() {
    places = searchBox.getPlaces();
    if (places.length == 0) {
      return;
    }
    // For each place, get the icon, name and location.
    let bounds = new google.maps.LatLngBounds();
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
