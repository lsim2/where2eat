$(document).ready(function() {
  setup_chatter();
});

// user preferences
let preferences = {user:$('#user').html(), cuisine:JSON.stringify([]), restrictions:JSON.stringify([]), misc:JSON.stringify([]), price: 1, startTime: '2pm', endTime: '4pm', distance: 12, pollURL: window.location.href};


function myF() {
  let targeted_popup_class = jQuery('[data-popup-open]').attr('data-popup-open');
  $('[data-popup="' + targeted_popup_class + '"]').fadeIn(350);
}

$(function() {
    $('#toResults').on('click', function(e)  {
        let targeted_popup_class = jQuery('[data-popup-close]').attr('data-popup-close');
        $('[data-popup="' + targeted_popup_class + '"]').fadeOut(350);
        e.preventDefault();
    });
});

// chat windown opens when the chat button is clicked and slides onto screen
$(function() {
//----- CLOSE
    $('[data-popup-close]').on('click', function(e)  {
    let targeted_popup_class = jQuery(this).attr('data-popup-close');
    $('[data-popup="' + targeted_popup_class + '"]').fadeOut(350);
    e.preventDefault();
    });

     $("#chat").on('click', function (e) {
        e.stopPropagation();
        e.preventDefault();
        if ($('#myChat').css("margin-left") == "0px") {
              $('#myChat').css('display', 'none');
             $('.chat-box').css('display', 'none');
             $('#myChat').animate({'marginLeft':"150%"}, 500);
             $('#map').animate({'width': "780px"}, 500);
        }
        else {
            $('#myChat').animate({'marginLeft':0, 'display':'block'}, 500);
            $('.chat-box').css('display', 'block');
            $('#map').animate({'width': "400px"}, 500);
            $('#myChat').css('display', 'block');
        }
    });
});

// dropdown for cuisine/restriction/food selection.
$(function() {

  $('#cuisine').selectize({
      plugins: ['remove_button'],
      maxItems: 3,
  });
  $('#restrictions').selectize({
      plugins: ['remove_button'],
  });
  $('#misc').selectize({
      plugins: ['remove_button'],
      maxItems: 3,
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
        preferences.startTime = times[ui.values[0]];
        preferences.endTime = times[ui.values[1]];
    }
    })
    .slider("pips", {
        rest: "label",
        labels: times,
        step: 2
    });

// slider for price selection
let prices = ["$", "$$", "$$$", "$$$$"];
$("#flat-slider-vertical-1")
    .slider({
        max: prices.length - 1,
        min: 0,
        range: "min",
        value: 0,
     change: function(event, ui) {
        preferences.price = ui.value;
    }
    })
    .slider("pips", {
        rest: "label",
        labels: prices
    });

    $("#flat-slider-vertical-2")
    .slider({
        max: 25,
        min: 1,
        range: "min",
        value: preferences.distance,
        change: function(event, ui) {
            document.getElementById("dist").innerHTML = ui.value;
            preferences.distance = ui.value;
        }
    }) .slider("pips", {
        first: "pip",
        last: "pip"
    })
    .slider("float");



$('#signin-form').submit(function(){
    $(".flip").attr("disabled",false);
});

// fills in form preferneces when submit button is clicked.
$('#toResults').click(function() {
    if ($("#cuisine").val() != null) {preferences.cuisine = JSON.stringify($("#cuisine").val());}
    if ($("#restrictions").val() != null) {preferences.restrictions = JSON.stringify($("#restrictions").val());}
    if ($("#misc").val() != null) {preferences.misc = JSON.stringify($("#misc").val());}
    preferences.url = window.location.href;
    for (let key in preferences) {
      $('#form').append("<input name='" + key +"' value='" + preferences[key] +"' type='hidden'/>");
    }
    $("#form").submit();
});

// Changes text of buttons when event details are shown/hidden
$("#getEvent").click(function() {
    $("#details").toggle();
    if($("#details").is(":hidden") == true) {
        $("#getEvent").html("Show Details");
    } else {
        $("#getEvent").html("Hide Details");
    }
});
