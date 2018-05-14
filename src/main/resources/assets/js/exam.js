function showSignUpForm() {
    $('#register-button').hide();
    $('#login').hide();
    $('#register').show()
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
            window.location = url;
        },
        error: function (xhr, ajaxOptions, thrownError) {
            $('#register-form-errors').html(xhr.responseText);
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
            window.location = url;
        },
        error: function (xhr, ajaxOptions, thrownError) {
            $('#login-form-errors').html(xhr.responseText);
            console.log(`Error in sign in ${xhr.responseText}`);
        }
    });
}

window.onload = function() {
    var video = document.getElementById('video');
    var canvas = document.getElementById('canvas');
    var context = canvas.getContext('2d');
    var tracker = new tracking.ObjectTracker('face');
    tracker.setInitialScale(4);
    tracker.setStepSize(2);
    tracker.setEdgesDensity(0.1);
    tracking.track('#video', tracker, { camera: true });
    tracker.on('track', function(event) {
        context.clearRect(0, 0, canvas.width, canvas.height);
        event.data.forEach(function(rect) {
            context.strokeStyle = '#a64ceb';
            context.strokeRect(rect.x, rect.y, rect.width, rect.height);
            context.font = '11px Helvetica';
            context.fillStyle = "#fff";
            context.fillText('x: ' + rect.x + 'px', rect.x + rect.width + 5, rect.y + 11);
            context.fillText('y: ' + rect.y + 'px', rect.x + rect.width + 5, rect.y + 22);
        });
    });
    var gui = new dat.GUI();
    gui.add(tracker, 'edgesDensity', 0.1, 0.5).step(0.01);
    gui.add(tracker, 'initialScale', 1.0, 10.0).step(0.1);
    gui.add(tracker, 'stepSize', 1, 5).step(0.1);
};