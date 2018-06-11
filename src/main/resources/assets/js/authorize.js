window.onload = function () {
    loadInfo();
    initializeVideoRendering();
};

let infoData;
var isUploading = false;

function loadInfo() {
    let serializedData = localStorage.getItem('login');
    infoData = JSON.parse(serializedData);
    console.log("User ");
    console.log(infoData);
}

var video = document.getElementById('video');

document.getElementById("save-btn").addEventListener("click", function () {
    photoCheck()
});

var canvas = document.getElementById('canvas');

//Initializing image
let image;
function photoCheck() {
    if(isUploading){
        return;
    }
    isUploading = true;
    console.log("Checking image");
    image = canvas.toDataURL("image/jpg");
    //console.log(image)
    let base64ImageContent = image.replace(/^data:image\/(png|jpg);base64,/, "");
    let blob = base64ToBlob(base64ImageContent, 'image/jpg');

    const blobUrl = URL.createObjectURL(blob);
    const img = document.createElement('img');

    let formData = new FormData();
    formData.append('photo', blob, "c.jpg");
    //formData.append('user_id', infoData.userId);
    console.log("User id: " + infoData.userId)
    var resource =  baseUrl + '/device/verify?user=' + infoData.userId
    $.ajax({
        type: "POST",
        url: resource,
        cache: false,
        contentType: false,
        processData: false,
        data: formData,
        
        success: function success(json) {
            if(json.verified){
                console.log("Verified")
                window.location = "/exam.html";
                $("#isIdentified").text("Authorized");
            } else {
                console.log("Not identified")
                $("#isIdentified").text("Face not identified");
            }
            isUploading = false;
        },
        error: function error(xhr, ajaxOptions, thrownError) {
            isUploading = false;
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
            context.strokeStyle = '#a64ceb';
            context.font = '11px Helvetica';
            context.fillStyle = "#fff";
        });
        
        if (event.data.length === 1) {
            //photoCheck();
            $('#save-btn').removeClass("disabled");
            $('#isDetected').text("face detected");//console.log(event.data.length);
            //console.log("one detected");
            context.drawImage(video, 0, 0, canvas.width, canvas.height);
            photoCheck()
        } else {
            $('#save-btn').addClass("disabled");
            //console.log(event.data.length);
            $('#isDetected').text("face not detected");
            //context.clearRect(0, 0, canvas.width, canvas.height)
        }

        //context.clearRect(0, 0, canvas.width, canvas.height);


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

function getBase64Image(img) {
    let canvas = document.createElement("canvas");
    canvas.width = img.width;
    canvas.height = img.height;

    let ctx = canvas.getContext("2d");
    ctx.drawImage(img, 0, 0);
    let dataURL = canvas.toDataURL("image/png");

    return dataURL.replace(/^data:image\/(png|jpg);base64,/, "");
}