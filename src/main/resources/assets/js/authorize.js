window.onload = function () {
    loadInfo();
    initializeVideoRendering();
};

let infoData;

function loadInfo() {
    let serializedData = localStorage.getItem('user');
    infoData = JSON.parse(serializedData);
    console.log("User ");
    console.log(infoData);
}

document.getElementById("save-button").addEventListener("click", function () {
    uploadPhoto()
});

function photoCheck() {
    console.log("Checking image");
    image = canvas.toDataURL("image/jpg");
    let base64ImageContent = image.replace(/^data:image\/(png|jpg);base64,/, "");
    let blob = base64ToBlob(base64ImageContent, 'image/jpg');
    let formData = new FormData();
    formData.append('photo', blob, "c.jpg");
    formData.append('user_id', infoData.userId);
    console.log(infoData.userId)
    $.ajax({
        type: "POST",
        url: baseUrl + '/device/verify',
        cache: false,
        contentType: false,
        processData: false,
        data: formData,
        success: function success(json) {
            console.log("success.");
            window.location = "/exam.html";
        },
        error: function error(xhr, ajaxOptions, thrownError) {
            console.log('Error upload ' + xhr.responseText);
        }
    });
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
            photoCheck();
            //console.log('\n rect ', rect)
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

function saveFullFrame() {
    let canvas = document.getElementById('canvas');
    console.log("Saving image");
    /*let img = canvas.toDataURL();*/
    let imgData = canvas.toDataURL();
    localStorage.setItem("imgData", imgData);
    /*document.getElementById("theimage").src = canvas.toDataURL();*/
}

function takeSnap() {
    console.log("taking snap");
    let snap = captureCanvas();
    console.log(snap);
    let bannerImg = document.getElementById('11-img');
    bannerImg.src = snap
}

function captureCanvas() {
    let canvas = document.getElementById('canvas');
    if (canvas.getContext) {
        let ctx = canvas.getContext("2d");
        return canvas.toDataURL("image/png");
    }
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