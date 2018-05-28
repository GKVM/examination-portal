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
    image = canvas.toDataURL("image/jpg");
    console.log(image);
    let base64ImageContent = image.replace(/^data:image\/(png|jpg);base64,/, "");
    let blob = base64ToBlob(base64ImageContent, 'image/jpg');
    let formData = new FormData();
    formData.append('photo', blob, "i.jpg");
    $.ajax({
        type: "POST",
        url: baseUrl + '/device/verify',
        cache: false,
        contentType: false,
        processData: false,
        data: formData,
        headers: {
        },
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
        console.log('\n track ', event.data);

        context.clearRect(0, 0, canvas.width, canvas.height);
        event.data.forEach(function (rect) {
            console.log('\n rect ', rect);

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