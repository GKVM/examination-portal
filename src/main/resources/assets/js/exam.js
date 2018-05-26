let user = {
    'name': "Demo user1",
    'exam_name': "Example examination",
    'question_id': "Example examination",
};
let test = {
    'id': "",
    'name': "Mock test name"
};
let questions = [
    {
        'number': 1,
        'question': "ushdfsn1 csudhfsiud fsidufhs dfisudfhs dfisuhdf aoisdj aoia saoiseasidjaos oaisjdoa",
        'optiona': "sdfs oidfos",
        'optionb': "sdfert oidfos",
        'optionc': "kfpokd oidfos",
        'optiond': "pdfpgo oidfos"
    },
    {
        'number': 2,
        'question': "eijoirjg2 csudhfsiud fsidufhs dfisudfhs dfisuhdf aoisdj aoia saoiseasidjaos oaisjdoa",
        'optiona': "poepgok oidfos",
        'optionb': "psokpdfo oidfos",
        'optionc': "dpjpgofkp oidfos",
        'optiond': "eopogkpo oidfos"
    },
    {
        'number': 3,
        'question': "wpkwe 3csudhfsiud fsidufhs dfisudfhs dfisuhdf aoisdj aoia saoiseasidjaos oaisjdoa",
        'optiona': "poepgok oidfos",
        'optionb': "psokpdfo oidfos",
        'optionc': "dpjpgofkp oidfos",
        'optiond': "eopogkpo oidfos"
    },
    {
        'number': 4,
        'question': "woijwoie4 csudhfsiud fsidufhs dfisudfhs dfisuhdf aoisdj aoia saoiseasidjaos oaisjdoa",
        'optiona': "poepgok oidfos",
        'optionb': "psokpdfo oidfos",
        'optionc': "dpjpgofkp oidfos",
        'optiond': "eopogkpo oidfos"
    },
    {
        'number': 5,
        'question': "woijwoie4 csudhfsiud fsidufhs dfisudfhs dfisuhdf aoisdj aoia saoiseasidjaos oaisjdoa",
        'optiona': "poepgok oidfos",
        'optionb': "psokpdfo oidfos",
        'optionc': "dpjpgofkp oidfos",
        'optiond': "eopogkpo oidfos"
    },
    {
        'number': 6,
        'question': "woijwoie4 csudhfsiud fsidufhs dfisudfhs dfisuhdf aoisdj aoia saoiseasidjaos oaisjdoa",
        'optiona': "poepgok oidfos",
        'optionb': "psokpdfo oidfos",
        'optionc': "dpjpgofkp oidfos",
        'optiond': "eopogkpo oidfos"
    },
    {
        'number': 7,
        'question': "woijwoie4 csudhfsiud fsidufhs dfisudfhs dfisuhdf aoisdj aoia saoiseasidjaos oaisjdoa",
        'optiona': "poepgok oidfos",
        'optionb': "psokpdfo oidfos",
        'optionc': "dpjpgofkp oidfos",
        'optiond': "eopogkpo oidfos"
    }
];
let responses = [];
let currentQuestionNumber = 1;
const numberOfQuestions = questions.length;

window.onload = function () {
    /*initializeVideoRendering();*/
    showCandidateInfo();
    getQuestionsAndShowQuestions();
    renderFromNumber(1);
};

function fetchInitialData() {

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

function showCandidateInfo() {
    /*    let serializedData = localStorage.getItem('user');
        user = JSON.parse(serializedData);
        console.log("User " + user);
        $('#username').text(user.name);*/
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
    $('#previous-question-btn').click( null);
    $('#clear-response-btn').click( null);
    $('#submit-next-question-btn').click( function () {});

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
        url: baseUrl + '/hub/submit-all',
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
    /*$.ajax({
        type: "POST",
        url: baseUrl + '/hub/upload',
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