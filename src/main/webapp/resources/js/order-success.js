var secs = 3;
var el = document.getElementById('cdTxt');
var timer = setInterval(function() {
    secs--;
    if (secs <= 0) {
        clearInterval(timer);
        window.location.href = 'index.xhtml';
    } else {
        el.textContent = secs + ' saniye sonra menüye dönülüyor...';
    }
}, 1000);
