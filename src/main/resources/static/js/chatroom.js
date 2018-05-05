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

    	alert(pollURL + ": copied to your clipboard!!");
    });

    let currentChatURL = window.location.href;
    pollURL = currentChatURL.replace("chat", "poll");
    $("#shareURL").attr("href", pollURL);


});
