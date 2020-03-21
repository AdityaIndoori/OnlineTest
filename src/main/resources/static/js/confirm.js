const NavigateTo = {
    NEXT: 'NEXT',
    PREV: 'PREV',
    CUST: 'CUST'
}

function startTimer(duration, display) {
    try{
        var timer = parseInt(duration);
        if(isNaN(timer)) throw "not a number";
            setInterval(function () {
                minutes = parseInt(timer/60);
                seconds = ("0" + timer%60).slice(-2);
                display.textContent = minutes+":"+seconds;
                document.getElementById("remainingTime").value = timer;
                remainingTime = timer
                if (--timer < 0) {
                    //TODO: Call the timeOut method in Java
                    postAsForm('result', 'get', null);
                }
            }, 1000);
    }catch(err){
        alert("Timer error: " + err);
    }
}

function navigateToQuestion(quesNum){
    //Todo: Navigate to the clicked question by sending a post request to question() method with the given question number.
    console.log("Question Number : " + quesNum);
    var navigateTo = NavigateTo.CUST

    postAsForm('question', 'post', {
                                       currentQuestionIndex: quesNum-1,
                                       selectedOption: null,
                                       testId: testId,
                                       remainingTime: remainingTime,
                                       navigateTo: navigateTo
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

function addButton(value) {
  //Create an input type dynamically.
  var element = document.createElement("input");
  //Assign different attributes to the element.
  element.type = "button";
  element.value = value; // Really? You want the default value to be the type string?
  element.className = "btn btn-primary"; // And the name too?
  element.addEventListener("click", function () {navigateToQuestion(value)});

  var buttonSpace = document.getElementById("questionButtonSpace");
  //Append the element in page (in span).
  buttonSpace.appendChild(element);
}

window.onload = function () {
    var timeRem = document.getElementById("remainingTime").value;

    display = document.getElementById("remainingTimeDisp");
    startTimer(timeRem, display);

    for(let i = 1; i <= totalQuestions; i++){
        addButton(i);
    }
};