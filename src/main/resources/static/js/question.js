function startTimer(duration, display) {
    try{
        var timer = parseInt(duration);
        if(isNaN(timer)) throw "not a number";
            setInterval(function () {
                minutes = parseInt(timer/60);
                seconds = timer%60;
                display.textContent = minutes+":"+seconds;
                document.getElementById("remainingTime").value = timer;
                if (--timer < 0) {
                    //TODO: Call the timeOut method in Java
                }
            }, 1000);
    }catch(err){
        alert("Timer error: " + err);
    }

}

function clickedNextButton(){
    alert("Clicked Next Button");
    //todo: post correct stuff
    postAsForm('question', 'post', {
                                       name: 'Aditya',
                                       age: 24
                                   });
}

function clickedPrevButton(){
    alert("Clicked Previous Button");
    //todo: post correct stuff
    postAsForm('question', 'post', {
                                       name: 'Aditya',
                                       age: 24
                                   });
}

function postAsForm(action, method, input) {
    'use strict';
    var form;
    form = $('<form />', {
        action: action,
        method: method,
        style: 'display: none;'
    });
    if (typeof input !== 'undefined' && input !== null) {
        $.each(input, function (name, value) {
            $('<input />', {
                type: 'hidden',
                name: name,
                value: value
            }).appendTo(form);
        });
    }
    form.appendTo('body').submit();
}

window.onload = function () {
    var timeRem = document.getElementById("remainingTime").value;

    display = document.getElementById("remainingTimeDisp");
    startTimer(timeRem, display);
};