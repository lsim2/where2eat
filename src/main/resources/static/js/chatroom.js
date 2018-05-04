// Waits for DOM to load before running
$(document).ready(() => {
    $("#userForm").keypress(function(event) {
        if (event.which == 13) {
        		console.log("firing from chatroom.js");
            let userInput = $("#userInput").val();
            send_chat(userInput);
            event.preventDefault();
            $("#userInput").val('');
        }
    });
});
