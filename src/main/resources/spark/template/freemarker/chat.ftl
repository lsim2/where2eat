<head>
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" />

<link rel='stylesheet prefetch' href='https://simeydotme.github.io/jQuery-ui-Slider-Pips/dist/css/jqueryui.min.css'>
<link rel='stylesheet prefetch' href='https://simeydotme.github.io/jQuery-ui-Slider-Pips/dist/css/jquery-ui-slider-pips.min.css'>
<link rel='stylesheet prefetch' href='http://cdnjs.cloudflare.com/ajax/libs/selectize.js/0.8.5/css/selectize.default.css'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/10.0.0/css/bootstrap-slider.css" />
<link rel="stylesheet" href="/css/chat.css">
<link rel="stylesheet" href="/css/form.css">
<nav class="navbar navbar-fixed-top">
      <div class="container-fluid">
        <div class="navbar-header">
          <a class="navbar-brand" href="/home">Where2Eat &nbsp; |</a>
          <a class="navbar-brand" href="javascript:{}" id="getEvent">Hide Details</a>
          <a class="navbar-brand dropdown-toggle" data-toggle="dropdown" href=""> | &nbsp;&nbsp;Sort Suggestions<span class="caret"></span></a>
            <ul style="margin-left: 250px;" class="dropdown-menu">
              <li> <a class="dropdown-item" id="pRanker" href="javascript:{}">Order by Price</a></li>
              <li><a class="dropdown-item" id="distRanker" href="javascript:{}">Order by Distance</a></li>
              <li><a class="dropdown-item" id="resetOrder" href="javascript:{}">Reset Order</a></li>
            </ul>
          <a class="navbar-brand" style="margin-left: 400px;" href="" id="shareURL" target="_blank">Share Poll &nbsp; |</a>
          <a class="navbar-brand" data-popup-open="popup-1" href="javascript:{}" onclick="myF()">Change Preferences  &nbsp;|</a>
          <a id="chat" class="navbar-brand" href="javascript:{}">Chat</a>
        </div>
      </div>
    </nav>
</head>
<br><br>
<div class="container bootstrap snippet">
    <div class="row">
    <div id="details">
    <h4 id="event"><b>${author}</b> invites you for <b>${pollTitle}</b> at <b>${pollLoc}</b> on <b>${pollDate}</b></h4></div>
    <br>
        <div class="col-md-4">
                <div id="suggestions"></div>
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
                  <a onclick="removeCard(this"class="popup-close-card" data-popup-close="popup-1">x</a>
                </div>
                </template>

          </div>

        <!--=========================================================-->
        <!-- selected chat -->
        <div id="user" hidden>${user}</div>
        <div id="prevAns" hidden>${prevAns}</div>
     <div class="col-md-5" id='map'>
        <!--stuff i added -->

        <script src="/js/maps.js"></script>
          <script async defer
          src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBwQc3YSCaD2g-U32PUbY4ZtoM2VocxYB8&callback=initMap">
          </script>

        <!--stuff i added -->

    </div>
        <div id="myChat" class="col-md-3">
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
<div class="popup" data-popup="popup-1">
<div class="popup-inner">
<h3 id="pollTitle"></h3>
<p id="pollInfo"></p>
<div class="submit">
<form id="form" method="POST" action="/chat/:id?${pollId}">
<h2 id="title"><span id="username"></span>Choose your preferences</h2>
<div class="ranking distance">
<p><b>I'm willing to travel <i><span id="dist">12</span></i> mile(s).</b></p><br>
<div id="flat-slider-vertical-2"></div>
</div>
<div class="ranking price">
<p><b>My preferred price range is:</b></p><br>
<div id="flat-slider-vertical-1"></div>
</div> 
<div class="ranking restrictions">
<p><b>Any dietary restrictions?</b></p>
    <select id="restrictions" class="select" multiple>
     <#list restrictions?keys as id>
            <option value="${id}">${restrictions[id]}</option>
      </#list>
    </select>
</div>
<div class="ranking cuisines">
<p><b>Cuisine preferences (choose up to 3)</b>:</p>
    <select id="cuisine" class="select" multiple>
      <#list cuisines?keys as id>
            <option value="${id}">${cuisines[id]}</option>
      </#list>
    </select>
</div>
<div class="ranking misc">
<p><b>Any additional preferences?</b></p>
    <select id="misc" class="select" multiple>
      <#list food?keys as id>
            <option value="${id}">${food[id]}</option>
      </#list>
    </select>
</div>
<div class="submitButton">
<button type="button" id="toResults" class="btn submitB">Submit</button>
</div>
</form>
</div>
<a class="popup-close" data-popup-close="popup-1">x</a>
</div>
</div>

<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
<script src='https://simeydotme.github.io/jQuery-ui-Slider-Pips/dist/js/jquery-plus-ui.min.js'></script>
<script src='https://simeydotme.github.io/jQuery-ui-Slider-Pips/dist/js/jquery-ui-slider-pips.js'></script>
<script src='http://cdnjs.cloudflare.com/ajax/libs/selectize.js/0.8.5/js/standalone/selectize.min.js'></script>
<script src="/js/chat.js"></script>

<#include "main.ftl">