let preferences = {user:"", cuisine:JSON.stringify([]), restrictions:JSON.stringify([]), misc:JSON.stringify([]), price: 1, startTime: '2pm', endTime: '4pm', distance: 12, pollURL: window.location.href};
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
      maxItems: 3
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
        value: 12,
        change: function(event, ui) {
            document.getElementById("dist").innerHTML = ui.value;
            preferences.distance = ui.value;
        }
    }) .slider("pips", {
        first: "pip",
        last: "pip"
    })
    .slider("float");


let currentUser = $('#sign-in').val();
$(document).keydown(
    function(e)
    {
        if (e.keyCode == 13) {
            e.preventDefault();
        if ($('#sign-in').val()=="") {
            alert("Please sign in first!");
        } else {
            if ($('.flip-container .flipper').closest('.flip-container').attr("class") != "flip-container hover") {
             currentUser = $('#sign-in').val();
              validate(currentUser);
            }
        }
        }
    });
$('.flip').click(function() {
    if ($('#sign-in').val()=="") {
        alert("Please sign in first!");
    } else {
        currentUser = $('#sign-in').val();
        validate(currentUser);
    }
      
});

$('.goback').click(function() {
     let reload = false;
     if ($('#sign-in').val() != currentUser) {
            currentUser = $('#sign-in').val();
            reload = true;
        }
        preferences.user = currentUser;
        $('#username').html("Hello " + currentUser + "! ");
        $('.flip-container .flipper').closest('.flip-container').toggleClass('hover');
        $('.flip-container .flipper').css('transform, rotateY(180deg)');
        if (reload) {
            location.reload();
        }
});

$('#signin-form').submit(function(){
    //TODO: make post request here and fill in the information if the user has signed in before!
    $(".flip").attr("disabled",false);
});

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

function validate(currentUser) {
    const postParameter = {"user": currentUser, "url": window.location.href};
    console.log(postParameter);
    let userAnswer = "";
    let reSignIn = false; 
    $.post("/validate", postParameter, response => {
        const responseObject = JSON.parse(response);
        console.log(responseObject);
        userAnswer = responseObject.answer;
        reSignIn = responseObject.oldUser ;
        if (reSignIn == true) {
            goToChat(userAnswer);
        } else {
            preferences.user = currentUser;
            $('#username').html("Hello " + $('#sign-in').val() + "! ");
            $('.flip-container .flipper').closest('.flip-container').toggleClass('hover');
            $('.flip-container .flipper').css('transform, rotateY(180deg)');
        }
    });
}

function goToChat(userAnswer) {
    preferences = {
        user: userAnswer.userId,
        cuisine: JSON.stringify(userAnswer.cuisine),
        restrictions: JSON.stringify(userAnswer.restrictions),
        misc: JSON.stringify(userAnswer.foodTerms),
        pollURL: window.location.href,
        price:  userAnswer.price,
        startTime: "2pm",
        endTime: "4pm",
        distance: Math.round(parseFloat(userAnswer.radius)*0.000621371192),
        url: window.location.href
    };
    for (let key in preferences) {
        $('#form').append("<input name='" + key +"' value='" + preferences[key] +"' type='hidden'/>");
    }
    $("#form").submit();
}
