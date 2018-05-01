//$("#msgbox").on("change keyup paste", function(){
//    dataList.innerHTML = '';
//    const textInput = $("#ajax").val();
//        console.log(textInput);
//    if (textInput.length > 0) {
//     const postParameter = {text: textInput};
////	$.post("/autocorect", postParameter, response => { 
////	    const responseObject = JSON.parse(response);
////	    prevWord = responseObject.prevWord;  
////	    const results = responseObject.results;  
////	    results.forEach(function(item) {
////        let option = document.createElement('li');
////        option.innerHTML = "" + prevWord + " " + item;
////            console.log(item);
////        option.setAttribute("id", "option");
////        dataList.appendChild(option);
////      });
////	});
//    } 
//   
//})

// let bgColor = '#98ff98';
// index = 0; 
// let name = $("#user").html();
// $(document).keydown(
//     function(e)
//     {    
//         if (e.keyCode == 13) {   
//         	let message = document.createElement('li');
//             let avatar = "https://api.adorable.io/avatars/50/" + name + "@adorable.png";
//         	message.classList.add('right');
//             message.classList.add('clearfix');
//             message.id = "msg" + index;
//             message.innerHTML = ' <span class="chat-img pull-right"><img src=' + avatar +' alt="User Avatar"></span><div class="chat-body clearfix"><div class="header"><strong class="primary-font">'+name+'</strong><small class="pull-right text-muted"><i class="fa fa-clock-o"></i> 3 mins ago</small></div><p>'+ $("#msgbox").val() +'</p></div>';
//         $("#msgbox").val("");
//         document.getElementById("chatList").appendChild(message);
//         let chatMsg = document.getElementById("chat-message");
//         chatMsg.scrollTop = chatMsg.scrollHeight;
//         }
//     }
// );
