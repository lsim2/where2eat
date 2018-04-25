
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

         <nav class="navbar navbar-fixed-top">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">YELP 2.0</a>
    </div>
  </div>
</nav>
</head>
<br><br><br>
<body>
<div class="myForm">
<div class="flip-container">
<div class="flipper">
<div class="front">
<div class="invite">
<h1 style="text-align:center"><i>Invitation</i></h1>
<h2 id="title"><i>Lina</i> has invited you for <i>Lunch</i> at <i>Kabob and Curry</i> on <i>Thursday April 27th</i> and wants your input!</h2>
<h2 style="text-align:center">Lina says: <i>"Let's eat!"</i></h2>
<br><br>
<form id="signin-form">
<span class="signbox">Name: <input id="sign-in" type="text" placeholder="Sign in with just your name!" autocomplete="off" required>
<input type="submit" id="submit-name"></span><br><br>
</form>
	<span class="enter"><button type="button" class="flip" title="Please sign in!">Go to form!</button></span>
</div>
</div>
<div class="back">
<form method="POST" action="/res">
<h2 id="title">Choose your preferences</h2>
<div class="ranking">
<p><b>My preferred price range is:</b></p><br>
<div id="flat-slider-vertical-1"></div>
</div>
<div class="ranking">
<p><b>I'm available from </b><i><span id="starttime">2pm</span></i><b> to </b><i><span id="endtime">4pm</span></i> </p><br>
<div id="flat-slider"></div>
</div>
<div class="ranking">
<p><b>Cuisine preferences (choose up to 3)</b>:</p>
    <select id="cuisine" class="select" multiple>
      <option value="0">Chinese</option>
      <option value="1">Indian</option>
      <option value="2">Mexican</option>
      <option value="3">Korean</option>
      <option value="4">Italian</option>
    </select>
</div>
<div class="ranking">
<p><b>Any dietary restrictions?</b></p>
    <select id="restrictions" class="select" multiple>
      <option value="0">Vegan</option>
      <option value="1">Vegetarian</option>
      <option value="2">Kosher</option>
      <option value="3">Halal</option>
      <option value="4">Gluten-Free</option>
    </select>
</div>
<div class="ranking">
<p><b>I'm willing to travel <i><span id="dist">12</span></i> miles.</b></p>
<div id="flat-slider-vertical-2"></div>
</div>
<div class="ranking">
<p><b>Any additional preferences?</b></p>
    <select id="misc" class="select" multiple>
      <option value="0">Pizza</option>
      <option value="1">Wraps</option>
      <option value="2">Noodles</option>
      <option value="3">Spicy</option>
      <option value="4">Alcohol</option>
    </select>
</div>
</form>
<button type="button" class="flip">Re-sign in</button>
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