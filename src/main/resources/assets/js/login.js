'use strict';

function showLogin() {
}

function signIn() {
    console.log("sign in");
    $.ajax({
        type: "POST",
        url: baseUrl + '/device/login',
        data: $('#login-form').serialize(),
        dataType: "json",
        success: function success(json) {
            console.log("success.");
            if (json != null) {
                console.log(json);
                localStorage.setItem('user', JSON.stringify(json));
                window.location = "/authorize.html";
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