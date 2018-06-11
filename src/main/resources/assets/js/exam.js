let infoData;
let questions;
let numberOfQuestions;
let currentQuestionNumber = 1;
let responses = [];
let isExamQCovered = false
let testName = "Exam Portal"

window.onload = function () {
    initializeVideoRendering();
    fetchInitialData();
    updateTime()
};

var eventDuration = new Date().getTime() + 30 * 60 * 1000;
let durationUndetected = 0;
let durationNotDetectedTime = 1 * 60 * 1000;

let isdetected = true;
let isverified = true;

function updateTime() {
    var currentTime = new Date().getTime();
    var diffTime = eventDuration - currentTime;
    var duration = moment.duration(diffTime);
    $('#test-remaining-time').text(duration.hours() + ":" + duration.minutes() + ":" + duration.seconds())
    var durationFaceUndetected = moment.duration(durationUndetected);
    $('#test-face-not-detected-duration').text(durationFaceUndetected.hours() + ":" + durationFaceUndetected.minutes() + ":" + durationFaceUndetected.seconds())
    processFaceDetectionTime()
}

function fetchInitialData() {
    setInterval(updateTime, 1000)
    let serializedData = localStorage.getItem('login');
    infoData = JSON.parse(serializedData);
    console.log(infoData);
    if(infoData.testName!=undefined){
        testName = infoData.testName
    }
    $('#logo-container').text(testName);
    showCandidateInfo();
    questions = JSON.parse(localStorage.getItem("questions"));
    numberOfQuestions = questions.length;
    renderFromNumber(1);
}

function showCandidateInfo() {
    $('#username').text(infoData.name);
}

document.getElementById("exam-authorize").addEventListener("click", function () {
    photoCheck()
});

setInterval(function () {
    photoCheck()
}, 20000);

var canvas = document.getElementById('canvas');

//Initializing image
let image;
var examDiv = document.getElementsByClassName("exam-container")[0]
var overlay = document.getElementsByClassName("overlay")[0]
let verifyFaceWhenDetected=false;

function photoCheck() {
    if(!isdetected){
        verifyFaceWhenDetected = true;
        return
    }
    console.log("Checking image");
    image = canvas.toDataURL("image/jpg");
    let base64ImageContent = image.replace(/^data:image\/(png|jpg);base64,/, "");
    let blob = base64ToBlob(base64ImageContent, 'image/jpg');
    let formData = new FormData();
    formData.append('photo', blob, "ce.jpg");
    formData.append('user_id', infoData.userId);
    console.log(infoData.userId)
    var resource = baseUrl + '/device/verify?user=' + infoData.userId
    $.ajax({
        type: "POST",
        url: resource,
        cache: false,
        contentType: false,
        processData: false,
        data: formData,
        success: function success(json) {
            console.log(json.verified)
            console.log("success.");
            if (json.verified) {
                console.log("Verified")
                isverified = true;
                verifyFaceWhenDetected = false;
                blockExam()
            } else {
                isverified = false;
                console.log("Wrong face")
                unblock();
            }
        },
        error: function error(xhr, ajaxOptions, thrownError) {
            console.log('Error upload ' + xhr.responseText);
        }
    });
}

function blockExam(){
    if (isExamQCovered) {
        isExamQCovered = false;
        examDiv.style.display = "block";
        overlay.style.display = "none";
    }
}

function unblock(){
    if (!isExamQCovered) {
        isExamQCovered = true
        examDiv.style.display = "none";
        overlay.style.display = "block";
    }
}

let alertCount = 0;
let alert1 = false;
let alert2 = false;
let alert3 = false;

function initializeVideoRendering() {
    let video = document.getElementById('video');
    let canvas = document.getElementById('canvas');
    let context = canvas.getContext('2d');
    let tracker = new tracking.ObjectTracker('face');
    tracker.setInitialScale(4);
    tracker.setStepSize(2);
    tracker.setEdgesDensity(0.1);
    tracking.track(video, tracker, {
        camera: true
    });
    tracker.on('track', function (event) {
        context.clearRect(0, 0, canvas.width, canvas.height);
        event.data.forEach(function (rect) {
            context.strokeStyle = '#a64ceb';
            context.font = '11px Helvetica';
            context.fillStyle = "#fff";
        });

    

        if (event.data.length === 1) {
            isdetected = true;
            $('#isDetected').text("Face Detected");
            
            $('#recognized').text(isverified?"Authorized":"Unauthorized");
            
            
            context.drawImage(video, 0, 0, canvas.width, canvas.height);
            if(verifyFaceWhenDetected){
                console.log("verifying face as soon as it is detected.")
                photoCheck();
            }
            //photoCheck()
        } else {
            //console.log(event.data.length);
            $('#isDetected').text("Face Not Detected");
            isdetected = false;
            if (alertCount == 0) {
                if (!alert1 && durationUndetected > durationNotDetectedTime) {
                    console.log("Face not detected for a minute")
                    alert1 = true;
                    alertCount = 1;
                    blockExam()
                    alert("Face not detected for a minute")
                }
            } else if (alertCount == 1) {
                if (!alert2 && durationUndetected > 2 * durationNotDetectedTime) {
                    console.log("Face not detected for 2 minutes")
                    alert2 = true;
                    alertCount = 2;
                    blockExam();
                    alert("Face not detected for 2 minutes. Exam will end if face is not detected for 4 minutes.")
                }
            } else {
                if (!alert1 && durationUndetected > 4 * durationNotDetectedTime) {
                    console.log("Face not detected for 4 min ")
                    blockExam();
                    alert("Submitting response.")
                }
            }
            //context.clearRect(0, 0, canvas.width, canvas.height)
        }
        //context.clearRect(0, 0, canvas.width, canvas.height);
    });
    // let gui = new dat.GUI();
    // gui.add(tracker, 'edgesDensity', 0.1, 0.5).step(0.01);
    // gui.add(tracker, 'initialScale', 1.0, 10.0).step(0.1);
    // gui.add(tracker, 'stepSize', 1, 5).step(0.1);
}

function processFaceDetectionTime() {
    if (!isverified || (isverified && !isdetected)) {
        durationUndetected += 1000;
    }
}

function showQuestionLinks() {
    // let questionsSerialized = localStorage.getItem('questions');
    //let questionsResponse = JSON.parse(questionsSerailized);
    console.log("Loaded questions");
    //show question links
    questions.forEach(function (question) {})
}

function renderFromNumber(number) {
    currentQuestionNumber = number;
    let question = questions.find(x => x.number === number);

    $('#previous-question-btn').off('click');
    $('#clear-response-btn').off('click');
    $('#submit-next-question-btn').off('click');

    $('#question-number').text(number);

    $('#question').text(question.question);
    $('#option1').text(question.optiona);
    $('#option2').text(question.optionb);
    $('#option3').text(question.optionc);
    $('#option4').text(question.optiond);
    $('.options').prop('checked', false);

    $('#previous-question-btn').on('click', function () {
        previousQuestion()
    });
    $('#clear-response-btn').on('click', function () {
        clearResponse()
    });
    $('#submit-next-question-btn').on('click', function () {
        submitAndNext()
    });
}

function submitResponseFinal() {
    console.log("Submitting response");
    $.ajax({
        type: "POST",
        url: baseUrl + '/device/submit-reply',
        data: {
            "test": infoData.testId,
            "user": infoData.userId,
        },
        dataType: "json",
        success: function success(json) {
            console.log("submitted answer.");
        },
        error: function error(xhr, ajaxOptions, thrownError) {
            $('#login-form-error').html(JSON.parse(xhr.responseText).message);
            console.log('Error sending answer' + xhr.responseText);
        }
    });
    localStorage.removeItem('questions');
    localStorage.removeItem('user');
}

function submitOneResponse() {
    console.log("submit response");
    //@todo Save response in local storage.
    let responseOptional = responses.search(r => r.number === currentQuestionNumber);
    if (responseOptional == null) {
        //create new object
    } else {
        //update
    }
    /*$.ajax({
        type: "POST",
        url: baseUrl + '/device/upload',
        data: {
            "test": infoData.testId,
            "user": infoData.userId
        },
        dataType: "json",
        success: function success(json) {
            console.log("submitted answer.");
        },
        error: function error(xhr, ajaxOptions, thrownError) {
            $('#login-form-error').html(JSON.parse(xhr.responseText).message);
            console.log('Error sending answer' + xhr.responseText);
        }
    });*/
}

function submitResponse(){
    console.log("submit response");

    $.ajax({
        type: "POST",
        url: baseUrl + '/device/submit-all',
        data: {
            "test": infoData.testId,
            "user": infoData.userId
        },
        dataType: "json",
        success: function success(json) {
            console.log("submitted all answer.");
        },
        error: function error(xhr, ajaxOptions, thrownError) {
            $('#login-form-error').html(JSON.parse(xhr.responseText).message);
            console.log('Error submitting all answer' + xhr.responseText);
        }
    });
}

function previousQuestion() {
    console.log("previous");
    if (currentQuestionNumber <= 1) return;
    renderFromNumber(currentQuestionNumber - 1)
}

function submitAndNext() {
    console.log("Clicked submit and next");
    nextQuestion();
    submitOneResponse();
}

function nextQuestion() {
    console.log("Next");
    if (currentQuestionNumber >= numberOfQuestions) return;
    renderFromNumber(currentQuestionNumber + 1)
}

function clearResponse() {
    console.log("Clear response");
    $('.options').prop('checked', false);
}

function base64ToBlob(base64, mime) {
    mime = mime || '';
    var sliceSize = 1024;
    var byteChars = window.atob(base64);
    var byteArrays = [];
    for (var offset = 0, len = byteChars.length; offset < len; offset += sliceSize) {
        var slice = byteChars.slice(offset, offset + sliceSize);

        var byteNumbers = new Array(slice.length);
        for (var i = 0; i < slice.length; i++) {
            byteNumbers[i] = slice.charCodeAt(i);
        }
        var byteArray = new Uint8Array(byteNumbers);
        byteArrays.push(byteArray);
    }
    return new Blob(byteArrays, {
        type: mime
    });
}

function getBase64Image(img) {
    let canvas = document.createElement("canvas");
    canvas.width = img.width;
    canvas.height = img.height;

    let ctx = canvas.getContext("2d");
    ctx.drawImage(img, 0, 0);
    let dataURL = canvas.toDataURL("image/png");

    return dataURL.replace(/^data:image\/(png|jpg);base64,/, "");
}

//===internal
function filteredArray(arr, key, value) {
    const newArray = [];
    for (i = 0, l = arr.length; i < l; i++) {
        if (arr[i][key] === value) {
            newArray.push(arr[i]);
        }
    }
    return newArray;
}