'use strict';

function showLogin() {
}

var info;

function signIn() {
    console.log("sign in");
    console.log($('#login-form').serialize());
    $.ajax({
        type: "GET",
        url: baseUrl + '/device/login?',
        data: $('#login-form').serialize(),
        success: function success(json) {
            console.log("success.");
            if (json != null) {
                console.log(json);
                info = json;
                localStorage.setItem('user', JSON.stringify(json));
                getQuestions();
                // window.location = "/authorize.html";
            } else {
                $('#login-form-error').html("Something broke.");
            }
        },
        error: function error(xhr, ajaxOptions, thrownError) {
            $('#login-form-error').html(JSON.parse(xhr.responseText).message);
            console.log('Error in sign in ' + xhr.responseText);
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
                let questions = json.questionList;
                localStorage.setItem('questions', JSON.stringify(questions));
                window.location = "/authorize.html";
            }
        },
        error: function error(xhr, ajaxOptions, thrownError) {
            //alert("Error" + xhr.responseText);
            console.log('Error in getting questions in ' + xhr.responseText);
        }
    });
}