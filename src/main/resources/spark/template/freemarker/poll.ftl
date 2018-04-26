
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
      <a class="navbar-brand" href="/home">YELP 2.0</a>
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
<h2 id="title"><i>${name}</i> has invited you for <i>${meal}!!</h2>
<h3 style="text-align:center">Where? <i>${location}</i></h3>
<h3 style="text-align:center">When? <i>${date}</i></h3>
<h3 style="text-align:center">Message from ${name}: <i>"${message}"</i></h3>
<br><br>
<form id="signin-form">
<span class="signbox">Name: <input id="sign-in" type="text" placeholder="Sign in with just your name!" autocomplete="off" required> <br><br>
</form>
	<span class="enter"><button type="button" class="flip" title="Please sign in!">Go to form!</button></span>
</div>
</div>
<div class="back">
<form id="form" method="POST" action="/chat">
<h2 id="title"><span id="username"></span>Choose your preferences</h2><button type="button" class="goback btn">Go back</button>
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
      <option value="Chinese">Chinese</option>
      <option value="Indian">Indian</option>
      <option value="Mexican">Mexican</option>
      <option value="Korean">Korean</option>
      <option value="Italian">Italian</option>
    </select>
</div>
<div class="ranking">
<p><b>Any dietary restrictions?</b></p>
    <select id="restrictions" class="select" multiple>
      <option value="Vegan">Vegan</option>
      <option value="Vegetarian">Vegetarian</option>
      <option value="Kosher">Kosher</option>
      <option value="Halal">Halal</option>
      <option value="Gluten-Free">Gluten-Free</option>
    </select>
</div>
<div class="ranking">
<p><b>I'm willing to travel <i><span id="dist">12</span></i> miles.</b></p>
<div id="flat-slider-vertical-2"></div>
</div>
<div class="ranking">
<p><b>Any additional preferences?</b></p>
    <select id="misc" class="select" multiple>
      <option value="Pizza">Pizza</option>
      <option value="Wraps">Wraps</option>
      <option value="Noodles">Noodles</option>
      <option value="Spicy">Spicy</option>
      <option value="Alcohol">Alcohol</option>
    </select>
</div>
<div class="ranking">
<button type="button" id="toResults" class="btn">Submit</button>
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