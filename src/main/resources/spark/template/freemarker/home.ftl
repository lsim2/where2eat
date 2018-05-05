<head>
  <meta charset="UTF-8">
      <link rel="stylesheet" href="css/home.css">
      	<nav class="navbar navbar-fixed-top">
	  <div class="container-fluid">
	    <div class="navbar-header">
	      <a class="navbar-brand" href="/home">Where2Eat</a>
	    </div>
	  </div>
	</nav>
</head>

<body>

  <body>

  <div id="container"></div>
  <script id="template">
    <div id="newForm" class="flip-card">New form</div>
    <div class="contact-wrapper">
        <div class="envelope {{ flipCard ? 'active' : '' }}">
          <div class="back paper"></div>
          <div class="content">
            <div class="form-wrapper">
              <form id="form_id" method="POST" action="/poll">
                <div class="top-wrapper">
                  <div class="input">
                    <label style="color: white">Name</label>
                    <input type="text" class="home_input" name="name" id="name" autocomplete="off" placeholder="Your name" required />
                  </div>
                  <div class="input">
                    <label style="color: white">Title</label>
                    <input type="text" name="title" id="title" autocomplete="off" placeholder="What meal are you having?" required/>
                  </div>
                  <div class="input">
                    <label style="color: white">Location</label>
                    <input id="location" name="location" class="controls" type="text" placeholder="Where would you like to go?">
                    <!--<input type="text" name="location" id="location" autocomplete="off" required/>-->
                  </div>
                </div>
                <div class="bottom-wrapper">
                  <div class="input">
                    <label style="color: white">Date and Time</label>
                    <input type="text" name="date" id="date-format" autocomplete="off" placeholder="When are you planning to meet?" required/>
                  </div>
                  <div class="input">
                    <label style="color: white">Message</label>
                    <textarea rows="5" name="message" id="message" value="Let's eat!"></textarea>
                  </div>
                  <div class="submit">
                    <div id="submit" class="submit-card" on-click="toggle('flipCard')">Create poll!</div>
                  </div>
                </div>
              </form>
            </div>
          </div>
          <div class="front paper"></div>
        </div>
      </div>

  </script>
  <a class="btn" data-popup-open="popup-1" href="#" style="display:none">Open Popup #1</a>
	<div class="popup" data-popup="popup-1">
		<div class="popup-inner">
			<h3>Poll created!</h3>
			<h4 id="pollTitle"></h4>
			<br>
			<div id = "urlInfo" style="text-align: center">
				<p> This is your URL </p>
				<h4 id="pollInfo"></h4>
			</div>
			<br>
			<div class="submit" style="text-align: right">
				<a id="update" class="submit-card">Change poll information</a>
			</div>
			<a class="popup-close" data-popup-close="popup-1">x</a>
		</div>
	</div>
</body>
  <script src='https://cdn.jsdelivr.net/npm/ractive@0.9'></script>
  <script src="/js/jquery-3.1.1.js"></script>

      <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBLgFAXSqI8kWNeYgYQw3jv3llyRSQy9z0&libraries=places&callback=initAutocomplete"
  type="text/javascript"></script>
      <script  src="js/home.js"></script>



</body>

<#include "main.ftl">
