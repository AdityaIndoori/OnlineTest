function startTimer(duration, display) {
    try{
        var timer = parseInt(duration);
        if(isNaN(timer)) throw "not a number";
            setInterval(function () {
                minutes = parseInt(timer/60);
                seconds = ("0" + timer%60).slice(-2);
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

function navigateToQuestion(quesNum){
    //Todo: Navigate to the clicked question by sending a post request to question() method with the given question number.
    console.log("Question Number : " + quesNum);
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