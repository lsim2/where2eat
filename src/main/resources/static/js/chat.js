$(document).ready(function() {
  setup_chatter();
});

function thumbUp(x) {
    event.preventDefault();
    let id = x.id; 
    $(".fa.thumb.fa-thumbs-down." + id).removeClass('active');
    x.classList.add("active");
}

function thumbDown(x) {
    event.preventDefault();
    let id = x.id; 
    $(".fa.thumb.fa-thumbs-up." + id).removeClass('active');
    x.classList.add("active");
}