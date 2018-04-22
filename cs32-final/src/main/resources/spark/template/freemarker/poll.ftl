<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/10.0.0/css/bootstrap-slider.css" />
<link rel="stylesheet" href="/css/form.css">
<br><br><br>
<body>
<h2 id="title">Choose your preferences</h2>
<a href="/poll/:id?${pollId}">Click</a>
<form method="POST" action="/res">

<div class="form">
<div class="ranking">
<p><b>Price Range</b></p>
<input id="price" type="text"
          data-provide="slider"
          data-slider-ticks="[1, 2, 3]"
          data-slider-ticks-labels='["$", "$$", "$$$"]'
          data-slider-min="1"
          data-slider-max="3"
          data-slider-step="1"
          data-slider-value="3"
          data-slider-tooltip="hide" />
</div>

<div class="ranking">
<p><b>Distance</b>: How far are you willing to travel?</p>
<input id="distance" type="text"
          data-provide="slider"
          data-slider-ticks="[1, 2, 3, 4]"
          data-slider-ticks-labels='["5 miles", "10 miles", "15 miles", "20 miles"]'
          data-slider-min="1"
          data-slider-max="4"
          data-slider-step="1"
          data-slider-value="4"
          data-slider-tooltip="hide" />
</div>
<div class="ranking">
<p><b>Time availability</b>:</p>
<input id="time" type="text" 
  data-provide="slider"
          data-slider-ticks="[1, 2, 3, 4, 5, 6, 7, 8, 9]"
          data-slider-ticks-labels='["9am", "10am", "11am", "12pm", "1pm", "2pm", "3pm", "4pm", "5pm"]'
          data-slider-min="1"
          data-slider-max="9"
          data-slider-step="1"
          data-slider-value="9"  data-slider-tooltip="hide"/><br/>
</div>
<div class="ranking">
<p><b>Select a cuisine</b>:</p>
<input type='text'
       placeholder='Cuisine'
       id='flexdatalist'
       data-min-length='1'
       data-selection-required='true'
       list='languages'
       value='Japanese'
       name='language2'>

<datalist id="cuisine">
    <option value="Japanese">Japanese</option>
    <option value="Indian">Indian</option>
    <option value="Candadian">Candadian</option>
    <option value="American">American</option>
    <option value="Chinese">Chinese</option>
    <option value="Syrian">Syrian</option>
    <option value="Pizza">Pizza</option>
    <option value="Noodles">Noodles</option>
    <option value="Wraps">Wraps</option>
    <option value="Sushi">Sushi</option>
</datalist>
</div>
</div>
</form>
<script src="/js/jquery-3.1.1.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-slider/10.0.0/bootstrap-slider.js"></script>
<script src="/js/poll.js"></script>
</body>
<#include "main.ftl">