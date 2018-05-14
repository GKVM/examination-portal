function showSignUpForm() {
    $('#register-button').hide();
    $('#login').hide();
    $('#register').show(10)
}

function showLogin() {
    $('#register-button').show();
    $('#register').hide();
    $('#login').show();
}

/*$('#register-form').onsubmit = signUp();
$('#login-form').onsubmit = signIn();*/

function signUp() {
    console.log("sign up");
    $.ajax({
        type: "POST",
        url: `${baseUrl}/candidate/signup`,
        data: $('#register-form').serialize(),
        dataType: "json",
        success: function (response) {
            console.log("success.");
            window.location = "/list.html";
        },
        error: function (xhr, ajaxOptions, thrownError) {
            $('#register-form-error').html(JSON.parse(xhr.responseText).message);
            console.log(`Error in sign up ${xhr.responseText}`);
        }
    });
}

function signIn() {
    console.log("sign in");
    $.ajax({
        type: "POST",
        url: `${baseUrl}/candidate/signin`,
        data: $('#login-form').serialize(),
        dataType: "json",
        success: function (response) {
            console.log("success.");
            window.location = "/list.html";
        },
        error: function (xhr, ajaxOptions, thrownError) {
            $('#login-form-error').html(JSON.parse(xhr.responseText).message);
            console.log(`Error in sign in ${xhr.responseText}`);
        }
    });
}