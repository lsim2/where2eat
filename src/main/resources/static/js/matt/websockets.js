const MESSAGE_TYPE = {
  CONNECT: 0,
  SEND: 1,
  UPDATE: 2
};

let conn;
let myId = -1;

var myMap = new Map();

// Setup the WebSocket connection for live updating of scores.
const setup_chatter = () => {
  // TODO Create the WebSocket connection and assign it to `conn`
  conn = new WebSocket("ws://localhost:1234/score"); // only 1 server <-- eveyr client has a connection to that server
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
      case MESSAGE_TYPE.CONNECT:
        //TODO: update with pervious msgs
        console.log("connected and my id is: " + data.payload.id);
        myId = data.payload.id;
        // TODO Assign myId
        break;
      case MESSAGE_TYPE.UPDATE:
        let txt;
        let txtId;
        let date; 
        txtId = data.payload.id;
        txt = data.payload.text;
        date = data.payload.date;
        console.log("received an update msg and the msg is: " + txt);
        console.log("received an update msg and the msg id is: " + txtId);
        $('#chatMsgs').append("<li>" + date + " and id: " + txtId + ": " + txt +"</li>");
        break;
    }
  };
}


const send_chat = chat => {
  console.log("we received the chat and it is: " + chat);

  let payLoad = {"id": myId, "text": chat}; 
  let jsonObject = { "type": MESSAGE_TYPE.SEND, "payload": payLoad} 
  let jsonString = JSON.stringify(jsonObject)
  conn.send(jsonString); 
}


