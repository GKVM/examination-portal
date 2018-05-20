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

let password = $('#password')[0];
let confirm_password = $('#password-confirm')[0];
password.onchange = validatePassword;
confirm_password.onkeyup = validatePassword;

function validatePassword() {
    if (password.value !== confirm_password.value) {
        confirm_password.setCustomValidity("Passwords Don't Match");
    } else {
        confirm_password.setCustomValidity('');
    }
}

function signUp() {
    console.log("sign up");
    $.ajax({
        type: "POST",
        url: `${baseUrl}/candidate/signup`,
        data: $('#register-form').serialize(),
        dataType: "json",
        success: function (json) {
            console.log("success.");
            if (json != null) {
                console.log(json);
                localStorage.setItem('user', JSON.stringify(json));
                window.location = "/list.html";
            } else {
                $('#register-form-error').html("Something is not working");
            }
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
        success: function (json) {
            console.log("success.");
            if (json != null) {
                console.log(json);
                localStorage.setItem('user', JSON.stringify(json));
                window.location = "/list.html";
            } else {
                $('#login-form-error').html("Something broke.");
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            $('#login-form-error').html(JSON.parse(xhr.responseText).message);
            console.log(`Error in sign in ${xhr.responseText}`);
        }
    });
}