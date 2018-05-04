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
        let suggestions = data.suggestions;
        priceSuggestions = data.priceSuggestions;
        distSuggestions = data.distSuggestions;

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
        console.log(restaurant);
          let temp = document.getElementById("suggestion");
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
            console.log(clone);
            $('#suggestions').append(clone);
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
        console.log("received an update msg and the msg is: " + txt);
        console.log("received an update msg and the msg id is: " + txtId);
        addChatMsg(nameTxt,date,txt);
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
   $("#suggestions").empty();
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
   $("#suggestions").empty();
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

