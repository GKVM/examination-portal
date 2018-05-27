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
    /*initializeVideoRendering();*/
    fetchInitialData();
    showCandidateInfo();
};

function fetchInitialData() {
    let serializedData = localStorage.getItem('user');
    info = JSON.parse(serializedData);
    console.log("User ");
    console.log(info);
    getQuestions()
    //getMockTestInfo();
}

function getMockTestInfo() {
    //this is mock
    //move to login.js
    $.ajax({
        type: "POST",
        data: {
            'registration': "",
            'password': ""
        },
        url: baseUrl + '/device/login',
        dataType: "json",
        success: function success(json) {
            if (json != null) {
                info = json;
                console.log(json);
                localStorage.setItem('user', JSON.stringify(json));
                getQuestions();
            } else {
                console.log('')
            }
        },
        error: function error(xhr, ajaxOptions, thrownError) {
            //alert("Error" + xhr.responseText);
            console.log('Error in singin ' + xhr.responseText);
        }
    });
}

function getQuestions() {
    //this is mock
    //move to login.js
    $.ajax({
        type: "GET",
        data: {
            'test': info.testId
        },
        url: baseUrl + '/device/questions',
        success: function success(json) {
            if (json != null) {
                localStorage.setItem('questions', JSON.stringify(questions));
                questions = json.questionList;
                renderFromNumber(1)
            }
        },
        error: function error(xhr, ajaxOptions, thrownError) {
            //alert("Error" + xhr.responseText);
            console.log('Error in getting questions in ' + xhr.responseText);
        }
    });
}

function showCandidateInfo() {
    /*    let serializedData = localStorage.getItem('user');
        user = JSON.parse(serializedData);
        console.log("User " + user);*/
    $('#username').text(info.name);
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

function getQuestionsAndShowQuestions() {
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

    $('#previous-question-btn').prop("onclick", null);
    $('#clear-response-btn').prop("onclick", null);
    $('#submit-next-question-btn').prop("onclick", null);
    $('#previous-question-btn').click(null);
    $('#clear-response-btn').click(null);
    $('#submit-next-question-btn').click(function () {
    });

    $('#number').text(number);

    $('#question').text(question.question);
    $('#option1').text(question.optiona);
    $('#option2').text(question.optionb);
    $('#option3').text(question.optionc);
    $('#option4').text(question.optiond);

    $('#previous-question-btn').click(function () {
        previousQuestion()
    });
    $('#clear-response-btn').click(function () {
        clearResponse()
    });
    $('#submit-next-question-btn').click(function () {
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