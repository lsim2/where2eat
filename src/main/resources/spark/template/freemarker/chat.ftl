<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="/css/chat.css">
<br><br>
<div class="container bootstrap snippet">
    <div class="row">
		<div class="col-md-4">
            <h4><b>Suggestions</b></h4>
                <button type="button" id="pRanker" class="rankers">Rank by price</button>
                <button type="button" id="distRanker" class="rankers">Rank by distance</button>
                <button type="button" id="resetOrder" class="rankers">Reset to initial order</button>
                <div id="suggestions">Suggestions</div>
                <template id="suggestion">
                <div class="card">
                  <div class="container">
                   <img class="food" src="https://www.shareicon.net/download/2016/09/02/824429_fork_512x512.png" alt="Food pic" style="width:110px; height: 100px;">
                    <h5 class="restaurant-name"><b>Name</b></h5> 
                    <p class="categories" style="font-size:10px"></p>
                    <span class="fa-stack">
                    <span onclick="thumbUp(this)" class="fa thumb fa-thumbs-up fa-stack-2x"></span>
                    <strong class="fa-stack-1x upNum" style="margin-top:24px; font-size:70%">0   
                    </strong>
                    </span>
                    <span class="fa-stack">
                     <span onclick="thumbDown(this)" class="fa thumb fa-thumbs-down fa-stack-2x"></span>
                    <strong class="fa-stack-1x downNum" style="margin-top:24px; font-size:70%">0
                    </strong>
                    </span>
                    
                  </div>
                </div>
                </template>
                
          </div>

        <!--=========================================================-->
        <!-- selected chat -->
        <div id="user" hidden>${user}</div>
        <div class="col-md-5" id='map'>
        <!--stuff i added -->

    <script src="/js/maps.js"></script>
      <script async defer
      src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBwQc3YSCaD2g-U32PUbY4ZtoM2VocxYB8&callback=initMap">
      </script>

    <!--stuff i added -->

        </div>
    	<div class="col-md-3">
            <div class="chat-message" id="chat-message">
                <ul id="chatMsgs" class="chat">
                <li class="left clearfix">
                <span class="chat-img pull-left">
                    		<img src="https://api.adorable.io/avatars/50/Foodbot@adorable.png" alt="User Avatar">
                    	</span>
                    	<div class="chat-body clearfix">
                    		<div class="header">
                    			<strong class="primary-font">FoodBot</strong>
                    		</div>
                    		<p>
                    			Welcome to Where2Eat! Chat here to talk to your fellow fooders!
                    		</p>
                    	</div>
                        </li>
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
<div hidden>
  	<p hidden>Currently connected users</p>
  	<ul id="connectedUsrs" hidden></ul>
	 </div>
     
<script src="/js/jquery-3.1.1.js"></script>
<script src="/js/chat.js"></script>

<#include "main.ftl">
