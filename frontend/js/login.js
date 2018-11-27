var urlBase = 'https://poosgroup5-u.cf/';
//var urlBase = 'http://localhost:3000/';
var extension = 'py';

function doLogin()
{
    var usrbox = document.getElementById("inputUsername");
    var passbox = document.getElementById("inputPassword");
    var usr = usrbox.value;
    var pass = passbox.value;

    var jsonPayload = '{"username" : "' + usr + '", "password" : "' + pass + '"}';
    var url = urlBase + 'api/login';

    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, false);
    xhr.setRequestHeader("Content-type", "application/json; charset=UTF-8");

    try {
        xhr.send(jsonPayload);

        var jsonObject = JSON.parse(xhr.responseText);

        var success = jsonObject.success;
        if (success < 1) {
            // error
            return false;
        } else {
            //successful login
            var sessionID = jsonObject.sessionID;

            // go to next page
            window.location.replace(urlBase + "main.html");

        }
    }
    catch (err) {
        alert(err);
    }
}

function doRegister()
{
    var usr = document.getElementById("inputUsername").value;
    var pass = document.getElementById("inputPassword").value;
    var confirmpass = document.getElementById("confirmPassword").value;

    if (pass != confirmpass) {
        $(document.getElementById("addMessage")).append('<div class="alert alert-info alert-dismissible" id="passwordIssue">  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>  <strong>Passwords do not match!</strong> Did not register user. </div>');
        return;
    }

    var jsonPayload = '{"username":"' + usr + '", "password":"' + pass + '", "isAdmin":0}';
    var url = urlBase + 'api/register';

    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, false);
    xhr.setRequestHeader("Content-type", "application/json; charset=UTF-8");

    try {
        xhr.send(jsonPayload);
        var result = JSON.parse(xhr.responseText).success;

        if (result == 1) {
            // go to login
            window.location.replace(urlBase + "login.html");
        }
        else {
            $(document.getElementById("addMessage")).append('<div class="alert alert-danger alert-dismissible" id="badRegister">  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>  <strong>Error!</strong> Something went wrong! Please try again later. </div>');

        }

    }
    catch (err) {
        alert(err.message)
    }
}


//function addBug()
//{

//}

//function searchBugsAndPopulateTable()
//{

//}

//function clickBugtoEdit()
//{

//}

//function submitEdit()
//{

//}

//function approveEdit()
//{

//}

//function rejectEdit()
//{

//}
