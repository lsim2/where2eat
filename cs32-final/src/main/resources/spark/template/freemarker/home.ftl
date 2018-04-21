
<head>
  <meta charset="UTF-8">
  
  
  
      <link rel="stylesheet" href="css/home.css">

  
</head>

<body>

  <body>
  
  <div id="container"></div>
  <script id="template">
    <div class="flip-card" on-click="toggle('flipCard')">{{ flipCard ? 'Reset' : 'Create poll' }}</div>
    <div class="contact-wrapper">
        <div class="envelope {{ flipCard ? 'active' : '' }}">
          <div class="back paper"></div>
          <div class="content">
            <div class="form-wrapper">
              <form id="form_id" method="POST" action="/poll">
                <div class="top-wrapper">
                  <div class="input">
                    <label style="color: white">Name</label>
                    <input type="text" name="name" id="name" autocomplete="off"/>
                  </div>
                  <div class="input">
                    <label style="color: white">Title</label>
                    <input type="text" name="title" id="title" autocomplete="off"/>
                  </div>
                  <div class="input">
                    <label style="color: white">Location</label>
                    <input type="text" name="location" id="location" autocomplete="off"/>
                  </div>
                </div>
                <div class="bottom-wrapper">
                  <div class="input">
                    <label style="color: white">Date and Time</label>
                    <input type="text" name="date" id="date-format" autocomplete="off"/>
                  </div>
                  <div class="input">
                    <label style="color: white">Message</label>
                    <textarea rows="5" name="message" id="message"></textarea>
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
<h2>Poll created!</h2>
<h3 id="pollTitle"></h3>
<p id="pollInfo"></p>
<div class="submit">
  <a id="goToPoll" class="submit-card">Go to poll!</a>
</div>
<a class="popup-close" data-popup-close="popup-1">x</a>
</div>
</div>
  
</body>
  <script src='https://cdn.jsdelivr.net/npm/ractive@0.9'></script>
  <script src="/js/jquery-3.1.1.js"></script>
  
  
    <script  src="js/home.js"></script>




</body>

<#include "main.ftl">
