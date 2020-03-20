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
                seconds = timer%60;
                display.textContent = minutes+":"+seconds;
                document.getElementById("remainingTime").value = timer;
                remainingTime = timer
                if (--timer < 0) {
                    //TODO: Call the timeOut method in Java
                }
            }, 1000);
    }catch(err){
        alert("Timer error: " + err);
    }

}

function clickedNextButton(){
    var selectedOption = $("input[name='option']:checked").val()
    var navigateTo = NavigateTo.NEXT
    console.log("Current question index: "+currentQuestionIndex);
    console.log("Test ID: "+testId)
    console.log("Remaining Time: "+remainingTime)
    console.log("Selected Option: "+selectedOption )
    console.log("Navigate To: "+navigateTo)


    postAsForm('question', 'post', {
                                       currentQuestionIndex: currentQuestionIndex,
                                       selectedOption: selectedOption,
                                       testId: testId,
                                       remainingTime: remainingTime,
                                       navigateTo: navigateTo
                                   });
}

function clickedPrevButton(){
    var selectedOption = $("input[name='option']:checked").val()
    var navigateTo = NavigateTo.PREV
    console.log("Current question index: "+currentQuestionIndex);
    console.log("Test ID: "+testId)
    console.log("Remaining Time: "+remainingTime)
    console.log("Selected Option: "+selectedOption )
    console.log("Navigate To: "+navigateTo)


    postAsForm('question', 'post', {
                                       currentQuestionIndex: currentQuestionIndex,
                                       selectedOption: selectedOption,
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

function onPageRefresh(){
        /*<![CDATA[*/
                var currentQuestionIndex = /*[[${questionIndex}]]*/ 0;
                var testId = /*[[${testId}]]*/ 0;
                var remainingTime = /*[[${remainingTime}]]*/ 120;
                var existingSelectedOption = /*[[${existingSelectedOption}]]*/ null;
                var userName = /*[[${userName}]]*/ "";
        /*]]>*/
        var navigateTo = NavigateTo.CUST;
        alert("Refreshing!");
        postAsForm('question', 'post', {
                                           currentQuestionIndex: currentQuestionIndex,
                                           selectedOption: existingSelectedOption,
                                           testId: testId,
                                           remainingTime: remainingTime,
                                           navigateTo: navigateTo
                                       });
       return 'Page Refreshed';
}

function clickedConfirmButton(){
    postAsForm('confirm', 'post', { userName: userName, testId: testId, remainingTime: remainingTime });
}

window.onload = function () {
    var timeRem = document.getElementById("remainingTime").value;
    display = document.getElementById("remainingTimeDisp");
    startTimer(timeRem, display);
    console.log("existingSelectedOption: "+existingSelectedOption);
    //Setting the radio button to the option previously selected by the user
    if(existingSelectedOption==0)
        $("#o1").prop("checked", true);
    else if(existingSelectedOption==1)
        $("#o2").prop("checked", true);
    else if(existingSelectedOption==2)
        $("#o3").prop("checked", true);
    else if(existingSelectedOption==3)
        $("#o4").prop("checked", true);
};

//window.onbeforeunload = onPageRefresh();