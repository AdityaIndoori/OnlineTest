
window.onload = function () {

    console.log("TimeTaken = " + timeTaken);
    mins = parseInt(timeTaken/60);
    secs = ("0" + timeTaken%60).slice(-2);
    $("#timeTakenDisp").html(mins+":"+secs);

    var table = document.getElementById("answersTable");
    var numOfRows = wrongQuestionList.length;
    for(let i = 1; i <= numOfRows; i++){
        var row = table.insertRow(i);
        var questionCell = row.insertCell(0);
        var selectedOptionCell = row.insertCell(1);
        var correctOptionCell = row.insertCell(2);
        questionCell.innerHTML = wrongQuestionList[i-1];
        selectedOptionCell.innerHTML = selectedAnswerList[i-1];
        correctOptionCell.innerHTML = correctAnswerList[i-1];
    }
};