
window.onload = function () {

    console.log("TimeTaken = " + timeTaken);
    mins = parseInt(timeTaken/60);
    secs = ("0" + timeTaken%60).slice(-2);
    $("#timeTakenDisp").html(mins+":"+secs);
    alert("Result JS");
};