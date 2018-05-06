const MESSAGE_TYPE = {
  CONNECT: 0,
  SEND: 1,
  UPDATE: 2,
  DELETE: 3,
  UPDATEALLNAMES: 4,
  ADDTOROOM: 5,
  UPDATERESTS: 6,
};

const VOTE_TYPE = {
    NONE: 0,
    UP: 1,
    DOWN: 2
};
// note DELETE is deleting an already disconnected socket

let conn;
let myId = -1;
let myName;
let regSuggestions;
//keeps track of all the restaurants displayed on the screen
let allRests  = [];
let restaurants;
let votes = {up:[], down:[]}
let upvotes = {};
let downvotes = {};

let ranking = {};
let myMap = new Map();

// Setup the WebSocket connection for live updating of scores.
const setup_chatter = () => {
  // TODO Create the WebSocket connection and assign it to `conn`
  conn = new WebSocket("ws://localhost:4567/chatting"); // only 1 server <-- every client has a connection to that server

  conn.onerror = err => {
    console.log('Connection error:', err);
  };

  //This function is called whenever the WebSocket receives a message from the server.
  //There are two possible types of messages: CONNECT and UPDATE
  conn.onmessage = msg => {
    const data = JSON.parse(msg.data);
    switch (data.type) {
      default:
        console.log('Unknown message type!', data.type);
        break;
      case MESSAGE_TYPE.DELETE:
        let nameToDelete = String(data.name);
        console.log("name to delete is: " + nameToDelete );
        //http://jsfiddle.net/m7zUh/
        $('ul#connectedUsrs li:contains(' + nameToDelete + ')').remove();
        break;
      case MESSAGE_TYPE.UPDATEALLNAMES:
        console.log("update all names called");
        $('#connectedUsrs').empty();
        // update all unique users in chat
        let namesOfUsrsInRoom = data.namesInRoom;

        let uniqueNames = [];
        $.each(namesOfUsrsInRoom, function(i, el){
            if($.inArray(el, uniqueNames) === -1) uniqueNames.push(el);
        });
        //TODO: trim names too <-- so spaces in names don't count..
        for (let i = 0; i < uniqueNames.length; i++) {
          let uniqueName = uniqueNames[i];
          console.log("unique name is: " + uniqueName);
          $('#connectedUsrs').append("<li>" + uniqueName + "</li>");
        }

        //TODO:add actual unique users

        $('#suggestions').empty();
        // update all uniqque users in chat
        regSuggestions = data.suggestions;
        updateRestaurantList(data.rests);

      let centerLat = parseFloat(JSON.parse(data.rests[0]).coordinates.latitude);
      let centerLng = parseFloat(JSON.parse(data.rests[0]).coordinates.longitude);

      let center = {lat: centerLat, lng: centerLng};

      let map = new google.maps.Map(document.getElementById('map'), {
        zoom: 18,
        center: center
      });

      let bounds = new google.maps.LatLngBounds();
     // allRests = data.rests.slice(0,5);
      let currRests = data.rests.slice(0,5);
     allRests = [];
      // updateCards(allRests);
      for(let i=0;i<currRests.length;i++){
        const restaurant = JSON.parse(data.rests[i]);
        drawRest(restaurant);
        allRests.push(restaurant);
        let pos = {lat: parseFloat(restaurant.coordinates.latitude), lng: parseFloat(restaurant.coordinates.longitude)};
        let marker = new google.maps.Marker({
          title: restaurant.name,
          position: new google.maps.LatLng(pos.lat, pos.lng),
          animation: google.maps.Animation.DROP,
          map: map,
          clicked: false
        });

        let contentString = "<b>" + restaurant.name + "</b></br>" + restaurant.location.display_address ;
        let infowindow = new google.maps.InfoWindow({
          content: contentString
        });

        google.maps.event.addListener(infowindow,'closeclick',
          function() {
            marker.clicked = false;
          })
        marker.addListener('click', function() {
          marker.clicked = true;
          infowindow.open(map, marker);
          marker.setAnimation(google.maps.Animation.BOUNCE);
          setTimeout(function () {
            marker.setAnimation(null);
          }, 500);
        });

        marker.addListener('mouseover', function() {
          infowindow.open(map, marker);
        });
        marker.addListener('mouseout', function() {
          if(marker.clicked == false){
            infowindow.close();
          }


        });
        bounds.extend(marker.position);
      }
      updateCards(allRests);
      map.fitBounds(bounds);



      let contentString = "You are here!" ;
      let herewindow = new google.maps.InfoWindow({
        content: contentString
      });
      if (navigator.geolocation) {

       navigator.geolocation.getCurrentPosition(function(position) {
         let pos = {
           lat: position.coords.latitude,
           lng: position.coords.longitude
         };

         let marker = new google.maps.Marker({
           title: "You!",
           position: new google.maps.LatLng(pos.lat, pos.lng),
           animation: google.maps.Animation.DROP,
           map: map,
           icon: 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png'
         });

          bounds.extend(marker.position);
          map.fitBounds(bounds);

          marker.addListener('mouseover', function() {
            herewindow.open(map, marker);
          });
          marker.addListener('mouseout', function() {

              herewindow.close();

          });
          let directionsDisplay;
            let directionsService = new google.maps.DirectionsService();

            directionsDisplay = new google.maps.DirectionsRenderer(
              {
                suppressMarkers: true,
                preserveViewport: true
              });


            directionsDisplay.setMap(map);
            let r = JSON.parse(data.rests[0]);
            //let endPos = {lat: parseFloat(r.coordinates.latitude), lng: parseFloat(r.coordinates.longitude)};
            let endPos = new google.maps.LatLng(parseFloat(r.coordinates.latitude), parseFloat(r.coordinates.longitude));
            let start = marker.position;
            let end = endPos;
            let request = {
                origin:start,
                destination:end,
                travelMode: google.maps.DirectionsTravelMode.DRIVING
            };
            directionsService.route(request, function(response, status) {
                if (status === google.maps.DirectionsStatus.OK) {
                    directionsDisplay.setDirections(response);
                }
            });

         //map.setCenter(pos);
       }, function() {
         handleLocationError(true, infoWindow, map.getCenter());
       });
     } else {
       // Browser doesn't support Geolocation
       handleLocationError(false, infoWindow, map.getCenter());
     }



        break;

      case MESSAGE_TYPE.CONNECT:
        // sending our info to the server, so the server can put us in the right room
        myId = data.payload.id;
        myName = data.payload.myName;
        let payLoad = {"name": myName, "id": myId, "roomURL": window.location.href};
        let jsonObject = { "type": MESSAGE_TYPE.ADDTOROOM, "payload": payLoad}
        let jsonString = JSON.stringify(jsonObject)
        conn.send(jsonString);
        break;

      case MESSAGE_TYPE.ADDTOROOM:
        let myDates = data.payload.dates;
        let myIds = data.payload.ids;
        let myContent = data.payload.content;
        let myNames = data.payload.names;
        // need to ask user for name
        for (let i = 0; i < myDates.length; i++) {
          let date = myDates[i];
          let txtId = myIds[i];
          let txt = myContent[i];
          let nameTxt = myNames[i];
          addChatMsg(nameTxt,date,txt);
        }
        break;
      case MESSAGE_TYPE.UPDATE:
        let txt;
        let txtId;
        let date;
        let nameTxt;
        txtId = data.payload.id;
        txt = data.payload.text;
        date = data.payload.date;
        nameTxt = data.payload.name;
        addChatMsg(nameTxt,date,txt);
        break;
      case MESSAGE_TYPE.UPDATERESTS:
        let newRanking = data.payload.ranking;
        let newList = [];

        for (let i = 0; i< newRanking.length; i++) {
            let restaurant = JSON.parse(newRanking[i]);
            newList.push(restaurant);
            restaurants[restaurant.id].voteType = VOTE_TYPE.NONE;
        }
        allRests = newList;
        updateCards(newList);
        break;
    }
  };
}




const send_chat = chat => {
  let payLoad = {"name": myName, "id": myId, "text": chat, "roomURL": window.location.href};
  let jsonObject = { "type": MESSAGE_TYPE.SEND, "payload": payLoad}
  let jsonString = JSON.stringify(jsonObject)
  conn.send(jsonString);
}

function initMap() {
  let uluru = {lat: -25.363, lng: 131.044};
  let map = new google.maps.Map(document.getElementById('map'), {
    zoom: 4,
    center: uluru
  });
  let marker = new google.maps.Marker({
    position: uluru,
    map: map
  });
}



$("#pRanker").click( function() {
  let priceRests = allRests.slice().sort(priceRanker);
  updateCards(priceRests);

});
$("#distRanker").click( function() {
  let distRests = allRests.slice().sort(distRanker);
  updateCards(distRests);
});
$("#resetOrder").click( function() {
  updateCards(allRests);

});

function handleLocationError(browserHasGeolocation, infoWindow, pos) {
        infoWindow.setPosition(pos);
        infoWindow.setContent(browserHasGeolocation ?
                              'Error: The Geolocation service failed.' :
                              'Error: Your browser doesn\'t support geolocation.');
        infoWindow.open(map);
}

function priceRanker(r1, r2){
  let dif = parseInt(r1.intPrice)-parseInt(r2.intPrice);
  return dif;
}
function distRanker(r1, r2){
  let dif = parseFloat(r1.dist)-parseFloat(r2.dist);
  return dif;
}

function addChatMsg(nameTxt,date,txt) {
     let temp = document.getElementById("left");
        if (nameTxt == myName) {
                temp = document.getElementById("right");
        }
        temp.content.querySelector('img').src = 'https://api.adorable.io/avatars/50/'+nameTxt+'@adorable.png';
        temp.content.querySelector(".user").innerHTML = nameTxt;
        temp.content.querySelector(".time").innerHTML = " " + date;
        temp.content.querySelector(".msg").innerHTML = txt;
        let clone = document.importNode(temp.content, true);
        document.getElementById("chatMsgs").appendChild(clone);
        let chatMsg = document.getElementById("chat-message");
        chatMsg.scrollTop = chatMsg.scrollHeight;
}

function thumbUp(x) {
    event.preventDefault();
    let id = x.id;
    $(".fa.thumb.fa-thumbs-down." + id).removeClass('active');
    x.classList.add("active");
    ranking[id]++;
    if (!(id in upvotes)) { upvotes[id] = 0 }
    upvotes[id]++;
    restaurants[id].voteType = VOTE_TYPE.UP;
    votes.up.push(id);
    for (i = 0; i < votes.down.length; i++) {
        if (votes.down[i] == id) {
            votes.down.splice(i, 1);
            break;
        }
    }
    updateRestList();
}

function thumbDown(x) {
    event.preventDefault();
    let id = x.id;
    $(".fa.thumb.fa-thumbs-up." + id).removeClass('active');
    x.classList.add("active");
    ranking[id]--;
    if (!(id in downvotes)) { downvotes[id] = 0 }
    downvotes[id]++;
    restaurants[id].voteType = VOTE_TYPE.DOWN;
    votes.down.push(id);
    for (i = 0; i < votes.up.length; i++) {
        if (votes.up[i] == id) {
            votes.up.splice(i, 1);
            break;
        }
    }
    updateRestList();
}


function updateRestList() {
     let currRanking = Object.keys(ranking).sort(function(a,b){return ranking[a]-ranking[b]});
     let restListToSend = [];
     for(i= currRanking.length-1; i >= 0; i--) {
        let id = currRanking[i];
        let restaurant = restaurants[id];
        restListToSend.push(restaurant);
     }
     console.log("Top: " + currRanking[0]);
    sendRestUpdateMsg(restListToSend);
}

function updateCards(currRanking) {
     $("#suggestions").empty();
     //console.log(currRanking.length);
    for(i= 0; i < currRanking.length; i++) {
        let restaurant = currRanking[i];
          let temp = document.getElementById("suggestion");
          if (restaurant.image_url != "" ) {
            temp.content.querySelector('.food').src = restaurant.image_url;
          } else {
              temp.content.querySelector('.food').src = "https://www.shareicon.net/download/2016/09/02/824429_fork_512x512.png";
          }
            temp.content.querySelector(".restaurant-name").innerHTML = restaurant.name;
            if (restaurant.categories.length < 2) {
                 temp.content.querySelector(".categories").innerHTML = restaurant.categories[0].title;
            } else{
                temp.content.querySelector(".categories").innerHTML = restaurant.categories[0].title + ", " + restaurant.categories[1].title;
            }
            let thumbsUp = temp.content.querySelector(".fa.thumb.fa-thumbs-up");
            let thumbsDown = temp.content.querySelector(".fa.thumb.fa-thumbs-down");
            thumbsUp.classList = "";
            thumbsUp.classList.add("fa","thumb","fa-thumbs-up", "fa-stack-2x", restaurant.id);
            thumbsUp.id = restaurant.id;
            thumbsDown.classList = "";
            thumbsDown.classList.add("fa","thumb","fa-thumbs-down", "fa-stack-2x", restaurant.id);
            thumbsDown.id = restaurant.id;
            if (votes.up.indexOf(restaurant.id) > -1) {
                thumbsUp.classList.add("active");
            } else if (votes.down.indexOf(restaurant.id) > -1) {
                thumbsDown.classList.add("active");
            }

            let uVotes = restaurant.upVotes;
            if ((restaurant.id in upvotes)) { uVotes = upvotes[restaurant.id] }
            let dVotes = restaurant.downVotes;
            if ((restaurant.id in downvotes)) { dVotes = downvotes[restaurant.id]}
            temp.content.querySelector(".fa-stack-1x.upNum").id = "thumbUp-"+ restaurant.id;
            temp.content.querySelector(".fa-stack-1x.downNum").id = "thumbDown-"+ restaurant.id

            let clone = document.importNode(temp.content, true);
            $('#suggestions').append(clone);
             console.log(restaurant.name +" : "+ restaurant.downVotes + " : " + downvotes[restaurant.id]);
            document.getElementById("thumbUp-"+ restaurant.id).innerHTML = uVotes;
            document.getElementById("thumbDown-"+ restaurant.id).innerHTML = dVotes;


        }
}

function sendRestUpdateMsg(restListToSend){
  let payLoad = {
      "name": myName,
      "id": myId,
      "text": "",
      "roomURL": window.location.href,
      "voteRank": restListToSend,
      "upvotes": upvotes,
      "downvotes": downvotes
  };
  let jsonObject = { "type": MESSAGE_TYPE.UPDATERESTS, "payload": payLoad}
  let jsonString = JSON.stringify(jsonObject)
  conn.send(jsonString);
}

// pass in data.rests as dataList
function updateRestaurantList(dataList) {
    restaurants = {};
    allRests =[];
    for (i = 0; i < dataList.length; i++) {
        restaurant = JSON.parse(dataList[i]);
        restaurants[restaurant.id] = restaurant;
       // allRests.push(restaurant);
    }
}

function drawRest(restaurant){
          let temp = document.getElementById("suggestion");
            ranking[restaurant.id] = 0;
            temp.content.querySelector('.food').src = restaurant.image_url;
            temp.content.querySelector(".restaurant-name").innerHTML = restaurant.name;
            if (restaurant.categories.length < 2) {
                 temp.content.querySelector(".categories").innerHTML = restaurant.categories[0].title;
            } else{
                temp.content.querySelector(".categories").innerHTML = restaurant.categories[0].title + ", " + restaurant.categories[1].title;
            }
            temp.content.querySelector(".fa.thumb.fa-thumbs-up").classList.add(restaurant.id);
            temp.content.querySelector(".fa.thumb.fa-thumbs-up").id = restaurant.id;
            temp.content.querySelector(".fa.thumb.fa-thumbs-down").classList.add(restaurant.id);
          temp.content.querySelector(".fa.thumb.fa-thumbs-down").id = restaurant.id;
            let clone = document.importNode(temp.content, true);
            $('#suggestions').append(clone);
}
