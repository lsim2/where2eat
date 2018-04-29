const MESSAGE_TYPE = {
  CONNECT: 0,
  SEND: 1,
  UPDATE: 2,
  DELETE: 3,
  UPDATEALLNAMES: 4
};
// note DELETE is deleting an already disconnected socket

let conn;
let myId = -1;
let myName;

var myMap = new Map();

// Setup the WebSocket connection for live updating of scores.
const setup_chatter = () => {
  // TODO Create the WebSocket connection and assign it to `conn`
  conn = new WebSocket("ws://localhost:1234/chatting"); // only 1 server <-- eveyr client has a connection to that server
 
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
        break;

      case MESSAGE_TYPE.CONNECT:
        console.log("connected and my id is: " + data.payload.id);
        myId = data.payload.id;
        myName = data.payload.myName;

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
          $('#chatMsgs').append("<li>" + date + " and id: " + txtId + " & name: "+ nameTxt + " and txt: " + txt +"</li>");
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
        $('#chatMsgs').append("<li>" + date + " and id: " + txtId + " & name: "+ nameTxt + " and txt: " + txt +"</li>");
    }
  };
}




const send_chat = chat => {
  console.log("we received the chat and it is: " + chat);

  let payLoad = {"name": myName, "id": myId, "text": chat}; 
  let jsonObject = { "type": MESSAGE_TYPE.SEND, "payload": payLoad} 
  let jsonString = JSON.stringify(jsonObject)
  conn.send(jsonString); 
}

