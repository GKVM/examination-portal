let info;
let test = {
    'id': "",
    'name': "Mock test name"
};
let questions;
let numberOfQuestions;
let currentQuestionNumber = 1;
let responses = [];

window.onload = function () {
    initializeVideoRendering();
    fetchInitialData();
};

function fetchInitialData() {
    let serializedData = localStorage.getItem('login');
    info = JSON.parse(serializedData);
    console.log("User ");
    console.log(info);
    showCandidateInfo();
    questions = JSON.parse(localStorage.getItem("questions"));
    numberOfQuestions = questions.length;
    renderFromNumber(1);
}

function showCandidateInfo() {
    $('#username').text(info.name);
}

function photoCheck() {
    console.log("Checking image");
    image = canvas.toDataURL("image/jpg");
    let base64ImageContent = image.replace(/^data:image\/(png|jpg);base64,/, "");
    let blob = base64ToBlob(base64ImageContent, 'image/jpg');
    let formData = new FormData();
    formData.append('photo', blob, "c.jpg");
    formData.append('user_id', infoData.userId);
    console.log(infoData.userId)
    $.ajax({
        type: "POST",
        url: baseUrl + '/device/verify',
        cache: false,
        contentType: false,
        processData: false,
        data: formData,
        success: function success(json) {
            console.log(json.verified)
            console.log("success.");
            if(json.verified){
                console.log("Verified")
                window.location = "/exam.html";
            }
            wait(1000)
            setTimeout(rotator,5000);

        },
        error: function error(xhr, ajaxOptions, thrownError) {
            console.log('Error upload ' + xhr.responseText);
            wait(1000)
            setTimeout(rotator,5000);

        }
    });
}

function initializeVideoRendering() {
    let video = document.getElementById('video');
    let canvas = document.getElementById('canvas');
    let context = canvas.getContext('2d');
    let tracker = new tracking.ObjectTracker('face');
    tracker.setInitialScale(4);
    tracker.setStepSize(2);
    tracker.setEdgesDensity(0.1);
    tracking.track(video, tracker, {camera: true});
    tracker.on('track', function (event) {
        context.clearRect(0, 0, canvas.width, canvas.height);
        event.data.forEach(function (rect) {
            context.strokeStyle = '#a64ceb';
            context.strokeRect(rect.x, rect.y, rect.width, rect.height);
            context.font = '11px Helvetica';
            context.fillStyle = "#fff";
            context.fillText(`x: ${rect.x}px`, rect.x + rect.width + 5, rect.y + 11);
            context.fillText(`y: ${rect.y}px`, rect.x + rect.width + 5, rect.y + 22);
        });
    });
    let gui = new dat.GUI();
    gui.add(tracker, 'edgesDensity', 0.1, 0.5).step(0.01);
    gui.add(tracker, 'initialScale', 1.0, 10.0).step(0.1);
    gui.add(tracker, 'stepSize', 1, 5).step(0.1);
}

function showQuestionLinks() {
    // let questionsSerialized = localStorage.getItem('questions');
    //let questionsResponse = JSON.parse(questionsSerailized);
    console.log("Loaded questions");
    //show question links
    questions.forEach(function (question) {
    })
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
            "test": test.id,
            "user": user.id,
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
            "test": test.id,
            "user": user.id
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