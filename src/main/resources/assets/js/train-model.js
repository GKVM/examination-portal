let loginInfo;

window.onload = function () {
    initializeVideoRendering();
    let serializedData = localStorage.getItem('user');
    loginInfo = JSON.parse(serializedData);
};

var video = document.getElementById('video');

var canvas = document.getElementById('canvas');

// Trigger photo take
document.getElementById("save-button").addEventListener("click", function () {
    uploadPhoto()
});

let image;

function uploadPhoto() {
    image = canvas.toDataURL("image/jpg");
    console.log(image);
    let base64ImageContent = image.replace(/^data:image\/(png|jpg);base64,/, "");
    let blob = base64ToBlob(base64ImageContent, 'image/jpg');

    const blobUrl = URL.createObjectURL(blob);
    const img = document.createElement('img');
    // img.src = blobUrl;
    // document.body.appendChild(img);

    let formData = new FormData();
    formData.append('photo', blob, "i.jpg");
    $.ajax({
        type: "POST",
        url: baseUrl + '/candidate/upload-photo',
        cache: false,
        contentType: false,
        processData: false,
        data: formData,
        headers: {
            'Authorization': 'Bearer ' + loginInfo.token
        },
        success: function success(json) {
            console.log("success.");
            window.location = "/list.html";
        },
        error: function error(xhr, ajaxOptions, thrownError) {
            console.log('Error upload ' + xhr.responseText);
        }
    });
}

function base64ToBlob(base64, mime) {
    mime = mime || '';
    var sliceSize = 1024;
    var byteChars = window.atob(base64);
    var byteArrays = [];
    for (var offset = 0, len = byteChars.length; offset < len; offset += sliceSize) {
        var slice = byteChars.slice(offset, offset + sliceSize);

        var byteNumbers = new Array(slice.length);
        for (var i = 0; i < slice.length; i++) {
            byteNumbers[i] = slice.charCodeAt(i);
        }
        var byteArray = new Uint8Array(byteNumbers);
        byteArrays.push(byteArray);
    }
    return new Blob(byteArrays, {type: mime});
}

function photoUpload(params) {
    console.log('inside photo upload', params);
    let xhr = new XMLHttpRequest();
    let body = new FormData();
    body.append('photo', params);
    console.log(body);
    xhr.open('POST', baseUrl + '/candidate/upload-photo');
    xhr.setRequestHeader('Authorization', `Bearer ${loginInfo.token}`);
    xhr.send(body);
    console.log(xhr);
    xhr.onreadystatechange = (e) => {
        if (xhr.readyState == 4 && xhr.status == 200) {
            console.log('working', xhr.responseText);
            /*
            return callback(JSON.parse(xhr.responseText));*/
        } else {
            console.log('error', xhr.responseText);
            null
        }
    }
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

        if (event.data.length === 1) {
            $('#save-button').removeClass("disabled");
            $('#is-detected').text("face detected");console.log(event.data.length);
            console.log("one detected");
            context.drawImage(video, 0, 0, canvas.width, canvas.height);
        } else {
            $('#save-button').addClass("disabled");
            console.log(event.data.length);
            $('#is-detected').text("face not detected");
            //context.clearRect(0, 0, canvas.width, canvas.height)
        }

        event.data.forEach(function (rect) {
            context.strokeStyle = '#a64ceb';
            context.font = '11px Helvetica';
            context.fillStyle = "#fff";
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