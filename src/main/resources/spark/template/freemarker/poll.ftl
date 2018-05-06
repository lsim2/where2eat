<!DOCTYPE html>
<html lang="en" >

<head>
  <meta charset="UTF-8">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/normalize/5.0.0/normalize.min.css">

  <link rel='stylesheet prefetch' href='https://simeydotme.github.io/jQuery-ui-Slider-Pips/dist/css/jqueryui.min.css'>
<link rel='stylesheet prefetch' href='https://simeydotme.github.io/jQuery-ui-Slider-Pips/dist/css/jquery-ui-slider-pips.min.css'>
<link rel='stylesheet prefetch' href='https://simeydotme.github.io/jQuery-ui-Slider-Pips/dist/css/app.min.css'>

<link rel='stylesheet prefetch' href='http://cdnjs.cloudflare.com/ajax/libs/selectize.js/0.8.5/css/selectize.default.css'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/10.0.0/css/bootstrap-slider.css" />
<link rel="stylesheet" href="/css/normalize.css">
<link rel="stylesheet" href="/css/html5bp.css">
<link rel="stylesheet" href="/css/main.css">
<link rel="stylesheet" href="/css/form.css">

</head>
<br><br><br>
<body>
<div class="myForm">
<div class="flip-container">
<div class="flipper">
<div class="front">
<div class="invite">
<h1 style="text-align:center"><i>Invitation</i></h1>
<h2 id="title"><i>${name}</i> has invited you for <i>${meal}!!</h2>
	<div id="poll-info">
		<h3 style="text-align:center">Where?  <i>${location}</i></h3>
		<h3 style="text-align:center">When?  <i>${date}</i></h3>
		<h3 style="text-align:center">Message from ${name}:  <i>"${message}"</i></h3>
	</div>
<br><br>
<form id="signin-form">
<span class="signbox">Name: <input id="sign-in" type="text" placeholder="Sign in with just your name!" autocomplete="off" required> <br><br>
</form>
	<button type="button" class="flip" title="Please sign in!">Go to form!</button>
</div>
</div>
<div class="back">
<div id="modal">
<form id="form" method="POST" action="/chat/:id?${pollId}">
<h2 id="title"><button type="button" class="goback btn">Go back</button><span id="username"></span>Choose your preferences</h2>
<div class="ranking">
<p><b>I'm willing to travel <i><span id="dist">12</span></i> mile(s).</b></p><br>
<div id="flat-slider-vertical-2"></div>
</div>
<div class="ranking">
<p><b>My preferred price range is:</b></p><br>
<div id="flat-slider-vertical-1"></div>
</div>
<div class="ranking">
<p><b>Any dietary restrictions?</b></p>
    <select id="restrictions" class="select" multiple>
     <#list restrictions?keys as id>
            <option value="${id}">${restrictions[id]}</option>
      </#list>
    </select>
</div>
<div class="ranking">
<p><b>Cuisine preferences (choose up to 3)</b>:</p>
    <select id="cuisine" class="select" multiple>
      <#list cuisines?keys as id>
            <option value="${id}">${cuisines[id]}</option>
      </#list>
    </select>
</div>
<div class="ranking">
<p><b>Any additional preferences?</b></p>
    <select id="misc" class="select" multiple>
      <#list food?keys as id>
            <option value="${id}">${food[id]}</option>
      </#list>
    </select>
</div>
<div class="submitBtn">
<button type="button" id="toResults" class="btn">Submit</button>
</div>
</div>
</form>
</div>
</div>
</div>
</div>

<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
<script src='https://simeydotme.github.io/jQuery-ui-Slider-Pips/dist/js/jquery-plus-ui.min.js'></script>
<script src='https://simeydotme.github.io/jQuery-ui-Slider-Pips/dist/js/jquery-ui-slider-pips.js'></script>
<script src='http://cdnjs.cloudflare.com/ajax/libs/selectize.js/0.8.5/js/standalone/selectize.min.js'></script>

<script src="/js/poll.js"></script>
</body>
</html>
