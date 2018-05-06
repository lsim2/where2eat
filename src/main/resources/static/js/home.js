let ractive = new Ractive({
  el: '#container',
  template: '#template',
  data: {}
});
let pollId = "";
let postParameter = {};
let places;
let filled = false;
let linkText;
let autocomplete;

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
    document.getElementById('message').value = "Let's eat!";

    console.log(filled);
    if(filled == true){
      ractive.toggle( 'flipCard' );
        let targeted_popup_class = jQuery('[data-popup-close]').attr('data-popup-close');
            $('[data-popup="' + targeted_popup_class + '"]').fadeOut(350);
    }

    filled = false;
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
        document.getElementById('pollTitle').innerHTML = "Details: " + title + " near " + location + " on " + date;
        let a = document.createElement('a');
        linkText = document.createTextNode('localhost:4567/poll/:id?'+pollId);
        a.appendChild(linkText);
        a.title = 'localhost:4567/poll/:id?'+pollId;
        a.href = '/poll/:id?'+pollId;
        a.target = "_blank";
        let div = document.getElementById('pollInfo');
        while(div.firstChild){
            div.removeChild(div.firstChild);
        }
        document.getElementById('pollInfo').appendChild(a);
    });
     setTimeout(function(){
    let targeted_popup_class = jQuery('[data-popup-open]').attr('data-popup-open');
    $('[data-popup="' + targeted_popup_class + '"]').fadeIn(350);
    filled = true;
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
    let options = {
        componentRestrictions: {country: ['US','CA']}
    };

  autocomplete = new google.maps.places.Autocomplete((document.getElementById('location')),
            options);

  // Listen for the event fired when the user selects a prediction and retrieve
  // more details for that place.
  autocomplete.addListener('place_changed', function() {
    places = autocomplete.getPlace();
    if (places.length == 0) {
      return;
    }
    // For each place, get the icon, name and location.
    let bounds = new google.maps.LatLngBounds();
    let place = places;
      if (!place.geometry) {
        console.log("Returned place contains no geometry");
        return;
      }
      postParameter.lat = place.geometry.location.lat();
      postParameter.lng = place.geometry.location.lng();
      console.log( postParameter.lng,  postParameter.lat);
      if (place.geometry.viewport) {
        // Only geocodes have viewport.
        bounds.union(place.geometry.viewport);
      } else {
        bounds.extend(place.geometry.location);
      }
  });
}
