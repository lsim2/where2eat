const MESSAGE_TYPE = {
  CONNECT: 0,
  SEND: 1,
  UPDATE: 2,
  DELETE: 3,
  UPDATEALLNAMES: 4,
  ADDTOROOM: 5
};
// note DELETE is deleting an already disconnected socket

let conn;
let myId = -1;
let myName;
let priceSuggestions;
let distSuggestions;
var myMap = new Map();

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
    console.log("WTFFFF");
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
        // update all uniqque users in chat
        let uniqueNamesOfUsrs = data.uniqueNames;
        for (let i = 0; i < uniqueNamesOfUsrs.length; i++) {
          let uniqueName = uniqueNamesOfUsrs[i];
          console.log("unique name is: " + uniqueName);
          $('#connectedUsrs').append("<li>" + uniqueName + "</li>");
        }

        $('#suggestions').empty();
        // update all uniqque users in chat
        let suggestions = data.suggestions;
        priceSuggestions = data.priceSuggestions;
        distSuggestions = data.distSuggestions;
        console.log(suggestions + "sugggestsionssssssssdf");
        for (let i = 0; i < suggestions.length; i++) {
          let restaurant = suggestions[i];
          $('#suggestions').append("<li>" + restaurant + "</li>");
        }

        let centerLat = parseFloat(JSON.parse(data.rests[0]).coordinates.latitude);
        let centerLng = parseFloat(JSON.parse(data.rests[0]).coordinates.longitude);

        let center = {lat: centerLat, lng: centerLng};

        let map = new google.maps.Map(document.getElementById('map'), {
          zoom: 18,
          center: center
        });



        let bounds = new google.maps.LatLngBounds();
        for(let i=0;i<data.rests.length;i++){
          const restaurant = JSON.parse(data.rests[i]);
          let pos = {lat: parseFloat(restaurant.coordinates.latitude), lng: parseFloat(restaurant.coordinates.longitude)};
          let marker = new google.maps.Marker({
            title: restaurant.name,
            position: new google.maps.LatLng(pos.lat, pos.lng),
            animation: google.maps.Animation.DROP,
            map: map
          });

          let contentString = "<b>" + restaurant.name + "</b></br>" + restaurant.location.display_address ;
          let infowindow = new google.maps.InfoWindow({
            content: contentString
          });

          marker.addListener('click', function() {
            infowindow.open(map, marker);
            marker.setAnimation(google.maps.Animation.BOUNCE);
            setTimeout(function () {
              marker.setAnimation(null);
            }, 500);

          });
          bounds.extend(marker.position);
        }
        map.fitBounds(bounds);


        let infoWindow = new google.maps.InfoWindow;
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


           // infoWindow.setPosition(pos);
           // infoWindow.setContent('Location found.');
           // infoWindow.open(map);
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
        console.log("werw");
        myId = data.payload.id;
        myName = data.payload.myName;
        let payLoad = {"name": myName, "id": myId, "roomURL": window.location.href};
        let jsonObject = { "type": MESSAGE_TYPE.ADDTOROOM, "payload": payLoad}
        let jsonString = JSON.stringify(jsonObject)
        conn.send(jsonString);
        break;

      case MESSAGE_TYPE.ADDTOROOM:
        console.log("connected and my id is: " + data.payload.id);

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
        console.log("received an update msg and the msg is: " + txt);
        console.log("received an update msg and the msg id is: " + txtId);
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
  };
}




const send_chat = chat => {
  console.log("we received the chat and it is: " + chat);

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
    console.log("price ranking");
    // const postParameters = {suggestions: document.getElementById("suggestions").value, 
    // rank_type: "distance"};
    // let link = window.location.href;
    // let actLink = link.substring(21, link.length);
    console.log(priceSuggestions);
   // $("#suggestions").empty();
        for (let i = 0; i < priceSuggestions.length; i++) {
            let currRest = priceSuggestions[i];
             $('#suggestions').append("<li>" + currRest + "</li>");
        }
    //})
});
$("#distRanker").click( function() {
    console.log("price ranking");
    // const postParameters = {suggestions: document.getElementById("suggestions").value, 
    // rank_type: "distance"};
    // let link = window.location.href;
    // let actLink = link.substring(21, link.length);
    console.log(distSuggestions);
   // $("#suggestions").empty();
        for (let i = 0; i < distSuggestions.length; i++) {
            let currRest = distSuggestions[i];
             $('#suggestions').append("<li>" + currRest + "</li>");
        }
    //})
});

function handleLocationError(browserHasGeolocation, infoWindow, pos) {
        infoWindow.setPosition(pos);
        infoWindow.setContent(browserHasGeolocation ?
                              'Error: The Geolocation service failed.' :
                              'Error: Your browser doesn\'t support geolocation.');
        infoWindow.open(map);
}
