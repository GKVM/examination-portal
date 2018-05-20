$(document).ready(function () {
    console.log("fetching list of exams");
    showCandidateInfo();
    loadExamList();
});

let user;

function showMockTest() {
    window.location = "/exam.html";
}

function showAuthorize() {
    window.location = "/authorize.html";
}

function showCandidateInfo() {
    let serializedData = localStorage.getItem('user');
    user = JSON.parse(serializedData);
    $('#username').text(user.name);
    $('#user-email-value').text(user.email);
}

function loadExamList() {
    $.ajax({
        type: "GET",
        url: `${baseUrl}/candidate/list`,
        success: function (response) {
            console.log("success.");
            console.log(response);
            let list = response;
            let pos = 0;
            /*while (pos < 4) {
                pos = pos + 1;
                $('#list-container').append(`
                    <tr>
                        <td>${pos}</td>
                        <td>exam ${pos}</td>
                        <td></td>
                    </tr>
                `)
            }*/
        },
        error: function (xhr, ajaxOptions, thrownError) {
            $('#list-errors').html(xhr.responseText);
            console.log(`Error in sign up ${xhr.responseText}`);
        }
    });
}