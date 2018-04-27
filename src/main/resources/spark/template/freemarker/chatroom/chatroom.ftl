<#assign content>
		<div>
		  	<p>Currently connected users</p>
		  	<ul id="connectedUsrs"></ul>
	  	 </div>
		<p> this is the chatroom</p>
		<form id = "userForm">
			  Enter your text:<br>
			  <input id="userInput" type="text" name="firstInput"><br>
		</form>
		
		 <div>
		  	<p>Chat:</p>
		  	<ul id="chatMsgs"></ul>
	  	 </div>
</#assign>
<#include "../main.ftl">

<script>
$(document).ready(function() {
  setup_chatter();
});
</script>