let ractive = new Ractive({
  el: '#container',
  template: '#template',
  data: {}
});

$("#submit").click(function(e) {
    let postParameter = {
        name: document.getElementById('name').value, 
        title: document.getElementById('title').value, 
        location: document.getElementById('location').value, 
        date: document.getElementById('date-format').value, 
        message: document.getElementById('message').innerHTML
     };
    $.post("/home", postParameter, response => { 
        const responseObject = JSON.parse(response);
        console.log(responseObject);
        let pollId = "" + responseObject.pollId; 
        let title = "" + responseObject.pollTitle; 
        let location = "" + responseObject.location;
        let date = "" + responseObject.date; 
        document.getElementById('pollTitle').innerHTML = "Poll for " + title + " at " + location + " on " + date; 
        document.getElementById('pollInfo').innerHTML = 'This is your URL:  ';
        let a = document.createElement('a');
        let linkText = document.createTextNode('localhost:4567/poll/:id?'+pollId);
        a.appendChild(linkText);
        a.title = 'localhost:4567/poll/:id?'+pollId;
        a.href = '/poll/:id?'+pollId;
        document.getElementById('pollInfo').appendChild(a);
    });
     setTimeout(function(){
    let targeted_popup_class = jQuery('[data-popup-open]').attr('data-popup-open');
    $('[data-popup="' + targeted_popup_class + '"]').fadeIn(350);
    e.preventDefault(); } , 2000);
}
);

$(function() {
//----- CLOSE
$('[data-popup-close]').on('click', function(e)  {
let targeted_popup_class = jQuery(this).attr('data-popup-close');
$('[data-popup="' + targeted_popup_class + '"]').fadeOut(350);
e.preventDefault();
});
});

$("#goToPoll").click(function(e){
        document.getElementById("form_id").submit();
});