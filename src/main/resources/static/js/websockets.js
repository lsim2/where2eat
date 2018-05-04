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
let regSuggestions;
//keeps track of all the restaurants displayed on the screen
let allRests;
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
        regSuggestions = data.suggestions;
        priceSuggestions = data.priceSuggestions;
        distSuggestions = data.distSuggestions;
        console.log(suggestions + "sugggestsionssssssssdf");
        for (let i = 0; i < suggestions.length && i< 5; i++) {
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
      allRests = data.rests.slice(0,5);
      for(let i=0;i<data.rests.length;i++){
        const restaurant = JSON.parse(data.rests[i]);
        console.log(restaurant);
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
        //alert("CONNECTING COOKIE IS : " + document.cookie);
          // do get request (and the get request should end up with the server verifiying us
          // if we signed in before ) and the

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
          //$('#chatMsgs').append("<li>" + date + " and id: " + txtId + " & name: "+ nameTxt + " and txt: " + txt +"</li>");
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
  let priceRests = allRests.sort(priceRanker);
  // for(let i =0; i< restsss.length ; i++){
  //   console.log(restsss[i] );
  // }
   $("#suggestions").empty();
        for (let i = 0; i < priceRests.length && i < 5; i++) {
            let currRest = String(JSON.parse(priceRests[i]).name);
             $('#suggestions').append("<li>" + currRest + "</li>");
        }

});
$("#distRanker").click( function() {
  console.log("distance ranking");
  let distRests = allRests.sort(distRanker);
   $("#suggestions").empty();
        for (let i = 0; i < distRests.length && i < 5; i++) {
            let currRest = String(JSON.parse(distRests[i]).name);
             $('#suggestions').append("<li>" + currRest + "</li>");
        }
});
$("#resetOrder").click( function() {
   $("#suggestions").empty();
        for (let i = 0; i < regSuggestions.length && i < 5; i++) {
            let currRest = regSuggestions[i];
             $('#suggestions').append("<li>" + currRest + "</li>");
        }
});
function handleLocationError(browserHasGeolocation, infoWindow, pos) {
        infoWindow.setPosition(pos);
        infoWindow.setContent(browserHasGeolocation ?
                              'Error: The Geolocation service failed.' :
                              'Error: Your browser doesn\'t support geolocation.');
        infoWindow.open(map);
}
function priceRanker(r1, r2){
  let rest1 = JSON.parse(r1);
  let rest2 = JSON.parse(r2);
  let dif = parseInt(rest1.intPrice)-parseInt(rest2.intPrice);
  return dif;
}
function distRanker(r1, r2){
  let rest1 = JSON.parse(r1);
  let rest2 = JSON.parse(r2);
  let dif = parseFloat(rest1.dist)-parseFloat(rest2.dist);
  return dif;
}
