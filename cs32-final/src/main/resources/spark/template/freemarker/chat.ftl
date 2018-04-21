<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
<link rel="stylesheet" href="/css/chat.css">
<br><br>
<div class="container bootstrap snippet">
    <div class="row">
		<div class="col-md-3">
            <div class=" row border-bottom padding-sm" style="height: 40px;">
            </div>
            <br>
                <ul>
                <li>Kabob</li>
                <li>Soban</li>
                </ul>
          </div>
        
        <!--=========================================================-->
        <!-- selected chat -->
    	<div class="col-md-8">
            <div class="chat-message" id="chat-message">
                <ul id="chatList" class="chat">
                    <li class="left clearfix">
                    	<span class="chat-img pull-left">
                    		<img src="https://api.adorable.io/avatars/50/Diane@adorable.png" alt="User Avatar">
                    	</span>
                    	<div class="chat-body clearfix">
                    		<div class="header">
                    			<strong class="primary-font">Diane</strong>
                    			<small class="pull-right text-muted"><i class="fa fa-clock-o"></i> 12 mins ago</small>
                    		</div>
                    		<p>
                    			Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                    		</p>
                    	</div>
                    </li>
                    <li class="left clearfix">
                        <span class="chat-img pull-left">
                    		<img src="https://api.adorable.io/avatars/50/Matthew@adorable.png" alt="User Avatar">
                    	</span>
                    	<div class="chat-body clearfix">
                    		<div class="header">
                    			<strong class="primary-font">Matthew</strong>
                    			<small class="pull-right text-muted"><i class="fa fa-clock-o"></i> 12 mins ago</small>
                    		</div>
                    		<p>
                    			Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                    		</p>
                    	</div>
                    </li>
                    <li class="right clearfix">
                        <span class="chat-img pull-right">
                    		<img src="https://api.adorable.io/avatars/50/Mounika@adorable.png" alt="User Avatar">
                    	</span>
                    	<div class="chat-body clearfix">
                    		<div class="header">
                    			<strong class="primary-font">Mounika</strong>
                    			<small class="pull-right text-muted"><i class="fa fa-clock-o"></i> 13 mins ago</small>
                    		</div>
                    		<p>
                    			Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur bibendum ornare dolor, quis ullamcorper ligula sodales at. 
                    		</p>
                    	</div>
                    </li>                    
                    <li class="left clearfix">
                        <span class="chat-img pull-left">
                    		<img src="https://api.adorable.io/avatars/50/Lina@adorable.png" alt="User Avatar">
                    	</span>
                    	<div class="chat-body clearfix">
                    		<div class="header">
                    			<strong class="primary-font">Lina</strong>
                    			<small class="pull-right text-muted"><i class="fa fa-clock-o"></i> 12 mins ago</small>
                    		</div>
                    		<p>
                    			Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                    		</p>
                    	</div>
                    </li>                 
                </ul>
            </div>
            <div class="chat-box">
            	<div class="input-group bg-white">
            		<input id="msgbox" class="form-control border no-shadow no-rounded" placeholder="Type your message here">
            		<span class="input-group-btn">
            			<button class="btn btn-success no-rounded" type="button">Send</button>
            		</span>
            	</div><!-- /input-group -->	
            </div>            
		</div>        
	</div>
</div>
<script src="/js/jquery-3.1.1.js"></script>
<script src="/js/chat.js"></script>
<#include "main.ftl">