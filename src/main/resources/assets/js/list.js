$(document).ready(function () {
    console.log("fetching list of exams");
    showCandidateInfo();
    loadExamList();
});

let user;
let tests = [
    {
        "name": "test one",
        "organization": "test organization",
        "is_applied": "false"
    },
    {
        "name": "test two",
        "organization": "test organization",
        "is_applied": "false"
    },
    {
        "name": "test one",
        "organization": "test organization",
        "is_applied": "false"
    }
];

function showMockTest() {
    window.location = "/login.html?mock-test=true";
}

function showAuthorize() {
    window.location = "/authorize.html";
}

function showCandidateInfo() {
    let serializedData = localStorage.getItem('user');
    user = JSON.parse(serializedData);
    $('#username').text(user.name);
    $('#user-email-value').text(user.email);
}

function loadExamList() {
    $.ajax({
        type: "GET",
        url: `${baseUrl}/candidate/list`,
        success: function (response) {
            console.log("success.");
            console.log(response);
            test = response;

            tests.forEach(function (test) {
                showExam(test)
            })
        },
        error: function (xhr, ajaxOptions, thrownError) {
            $('#list-errors').html(xhr.responseText);
            console.log(`Error in sign up ${xhr.responseText}`);
        }
    });
}

function showExam(exam) {
    let statusString = "";
    switch (exam.is_applied) {
        case "false":
            statusString = '<a href="javascript:;" onclick="" class="btn secondary-content">Apply</a>';
            break;
        case "true":
            statusString = '<a href="javascript:;" class="btn secondary-content">Already Applied</a>';
            break;
    }

    $('#list-collection').append(`<li class="collection-item avatar">
                            <img src="images/yuna.jpg" alt="" class="circle">
                            <span class="title">` + exam.name + `</span>
                            <p>` + exam.organization + ` <br>
                            </p>` + statusString+`</li>`)
}

function registerForExam(examId){
    console.log("")
}
