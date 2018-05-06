// Waits for DOM to load before running
$(document).ready(() => {
    let pollURL;
    $("#userForm").keypress(function(event) {
        if (event.which == 13) {
        		console.log("firing from chatroom.js");
            let userInput = $("#userInput").val();
            send_chat(userInput);
            event.preventDefault();
            $("#userInput").val('');
        }
    });

    $("#shareURL").click(function(e) {
    	e.preventDefault();
    	const el = document.createElement('textarea');
		el.value = pollURL;
		document.body.appendChild(el);
		el.select();
		document.execCommand('copy');
	    document.body.removeChild(el);
		alert("Link to poll has been copied to your clipboard! Send it to your friends to invite them!");
    });

    let currentChatURL = window.location.href;
    pollURL = currentChatURL.replace("chat", "poll");
    $("#shareURL").attr("href", pollURL);
});
