<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
<link rel="stylesheet" href="/css/chat.css">
<br><br>
<div class="container bootstrap snippet">
    <div class="row">
		<div class="col-md-3">
            <div class=" row border-bottom padding-sm" style="height: 40px;">
            </div>
            <br>
                <div>
                    <p>Suggestions</p>
                    <ul id="suggestions"></ul>
                </div>
          </div>
        
        <!--=========================================================-->
        <!-- selected chat -->
        <div id="user" hidden>${user}</div>
        <div class="col-md-5">MAPS GO HERE!</div>
    	<div class="col-md-4">
            <div class="chat-message" id="chat-message">
                <ul id="chatMsgs" class="chat">
                    <template id="left">
                    <li class="left clearfix">
                    	<span class="chat-img pull-left">
                    		<img class="avatar" src="" alt="User Avatar">
                    	</span>
                    	<div class="chat-body clearfix">
                    		<div class="header">
                    			<strong class="primary-font user"></strong>
                    			<small class="pull-right text-muted"><i class="fa fa-clock-o time"></i></small>
                    		</div>
                    		<p class="msg"></p>
                    	</div>
                    </li>
                    </template>
                    <template id="right">
                    <li class="right clearfix">
                        <span class="chat-img pull-right">
                    		<img src="" alt="User Avatar">
                    	</span>
                    	<div class="chat-body clearfix">
                    		<div class="header">
                    			<strong class="primary-font user"></strong>
                    			<small class="pull-right text-muted"><i class="fa fa-clock-o time"></i></small>
                    		</div>
                    		<p class="msg"></p>
                    	</div>
                    </li> 
                    </template>
                </ul>
            </div>
            <div class="chat-box">
            	<div class="input-group bg-white">
                <form id = "userForm">
            		<input id="userInput" name="firstInput" class="form-control border no-shadow no-rounded" placeholder="Type your message here" autocomplete="off">
                </form>
            	</div><!-- /input-group -->	
            </div>            
		</div>        
	</div>
</div>
<div>
  	<p>Currently connected users</p>
  	<ul id="connectedUsrs"></ul>
	 </div>
<script src="/js/jquery-3.1.1.js"></script>

<script>
$(document).ready(function() {
  setup_chatter();
});
</script>
<#include "main.ftl">
