let loginInfo;

window.onload = function () {
    initializeVideoRendering();
    let serializedData = localStorage.getItem('user');
    loginInfo = JSON.parse(serializedData);
};

var video = document.getElementById('video');

// Get access to the camera!
if(navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
    // Not adding `{ audio: true }` since we only want video now
    navigator.mediaDevices.getUserMedia({ video: true }).then(function(stream) {
        video.src = window.URL.createObjectURL(stream);
        video.play();
    });
}

var canvas = document.getElementById('canvas');

// Trigger photo take
document.getElementById("submit").addEventListener("click", function() {
    uploadPhoto()
});

function uploadPhoto(){
    var image = new Image();
    image.src = canvas.toDataURL("image/png");
    console.log("save");
    console.log(image.src);

    $.ajax({
        type: "POST",
        url: baseUrl + '/candidate/upload-photo',
        data: {
            "Authorization": `Bearer ` + loginInfo.token
        },
        dataType: "multipart/form-data",
        success: function success(json) {
            console.log("success.");
            if (json != null) {
                console.log(json);
                localStorage.setItem('user', JSON.stringify(json));
                window.location = "/list.html";
            } else {
                $('#login-form-error').html("Something broke.");
            }
        },
        error: function error(xhr, ajaxOptions, thrownError) {
            $('#login-form-error').html(JSON.parse(xhr.responseText).message);
            console.log('Error in sending photo ' + xhr.responseText);
        }
    });
    return image;
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

function saveFullFrame() {
    let canvas = document.getElementById('canvas');
    console.log("Saving image");
    /*let img = canvas.toDataURL();*/
    let imgData = canvas.toDataURL();
    localStorage.setItem("imgData", imgData);
    /*document.getElementById("theimage").src = canvas.toDataURL();*/
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