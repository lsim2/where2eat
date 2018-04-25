$("#price").slider({});
$("#distance").slider({});
$("#time").slider({ id: "time", range: true});
$(function() {
  $('#cuisine').selectize({
      plugins: ['remove_button'],
      maxItems: 3
  });
  $('#restrictions').selectize({
      plugins: ['remove_button']
  });
  $('#misc').selectize({
      plugins: ['remove_button']
  });
});

$.extend( $.ui.slider.prototype.options, { 
    animate: 300
});

let times = ["2pm", "2.30pm", "3pm", "3.30pm", "4pm", "4.30pm", "5pm"];

$("#flat-slider")
    .slider({
        max: times.length-1,
        min: 0,
        range: true,
        values: [0, 4],
    change: function(event, ui) { 
         document.getElementById("starttime").innerHTML = times[ui.values[0]]; 
        document.getElementById("endtime").innerHTML = times[ui.values[1]]; 
    } 
    })
    .slider("pips", {
        rest: "label",
        labels: times,
        step: 2
    });

let prices = ["$", "$$", "$$$", "$$$$"];
$("#flat-slider-vertical-1")
    .slider({
        max: prices.length - 1,
        min: 0,
        range: "min",
        value: 0,
    })
    .slider("pips", {
        rest: "label",
        labels: prices
    });

    $("#flat-slider-vertical-2")
    .slider({
        max: 25,
        min: 0,
        range: "min",
        value: 12,
        change: function(event, ui) { 
        document.getElementById("dist").innerHTML = ui.value; 
    } 
    }) .slider("pips", {
        first: "pip",
        last: "pip"
    })
    .slider("float");


$('.flip').click(function() {
    if ($('#sign-in').val()=="") {
        alert("Please sign in first!");
    } else {
        $('.flip-container .flipper').closest('.flip-container').toggleClass('hover');
        $('.flip-container .flipper').css('transform, rotateY(180deg)');
    }
});

$('#signin-form').submit(function(){
    $(".flip").attr("disabled",false);
});
