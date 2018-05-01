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
        for (let i = 0; i < suggestions.length; i++) {
          let restaurant = suggestions[i];
          $('#suggestions').append("<li>" + restaurant + "</li>");
        }
        break;

      case MESSAGE_TYPE.CONNECT:
        alert("CONNECTING COOKIE IS : " + document.cookie);
          // do get request (and the get request should end up with the server verifiying us
          // if we signed in before ) and the

        // sending our info to the server, so the server can put us in the right room
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

