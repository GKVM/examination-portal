let user = {
    'name': "Demo user1",
    'exam_name': "Example examination",
    'question_id': "Example examination",
};
let questions = [
    {
        'number': 1,
        'question': "ushdfsn csudhfsiud fsidufhs dfisudfhs dfisuhdf aoisdj aoia saoiseasidjaos oaisjdoa",
        'optiona': "sjdjsodif oidfos",
        'optionb': "sjdjsodif oidfos",
        'optionc': "sjdjsodif oidfos",
        'optiond': "sjdjsodif oidfos"
    }
];
let responses = [];
let currentQuestionNumber = 1;
const numberOfQuestions = 1;

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
    let serializedData = localStorage.getItem('user');
    user = JSON.parse(serializedData);
    console.log("User " + user);
    $('#username').text(user.name);
}

function renderFromNumber(number) {
    let question = questions.first();

/*    $('#previous-question-btn').disable();
    $('#clear-response-btn').disable();
    $('#submit-next-question-btn').disable();*/

    $('#question').text(question.question);
    $('#option1').text(question.optiona);
    $('#option2').text(question.optionb);
    $('#option3').text(question.optionc);

/*    $('#previous-question-btn').enable();
    $('#clear-response-btn').enable();
    $('#submit-next-question-btn').enable();*/
}

function submitResponseFinal(){
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
    console.log("submit all response");
    //@todo Save response in local storage.
    $.ajax({
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
    });
}

function previousQuestion() {
    console.log("previous");
    if (currentQuestionNumber !== 1) return;
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
    //call clear response endpoint to clear.
}

function getQuestionsAndShowQuestions() {
    let questionsSerialized = localStorage.getItem('questions');
    let questionsResponse = JSON.parse(questionsSerailized);
    console.log("Loaded questions");
    //show question links
    questionsResponse.questions.forEach(function (question) {
    })
}

