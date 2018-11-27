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

function searchBugsAndPopulateTable()
{
    var jsonPayload = '{}';
    var url = urlBase + 'api/search';

    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, false);
    xhr.setRequestHeader("Content-type", "application/json; charset=UTF-8");

    try {
        xhr.send(jsonPayload);

        var JSONObjectsArr = JSON.parse(xhr.responseText);

        if (JSONObjectsArr.success == 1)
        {
            var table = document.getElementById("bugs");
            var arr = Array.from(JSONObjectsArr.results);
            arr.forEach(function (item, index) {
                addRowOnTable(table, item, index)
            });

            $(document).ready(function () {
                $('#bugs').DataTable();
            });
        }


    }
    catch (err) {
        alert(err);
    }
}

function addRowOnTable(table, item, index)
{
    if (item != null) {
        var contactType = item.contactType;

        $(table).find('tbody').append("<tr><td>" + item.common_name +
            "</td><td>" + item.scientific_name + "</td><td> <button type='button' id='button" +
            item.id + "'>Edit!</button> </td></tr>");

        var btn = document.getElementById("button" + item.id);
        btn.onclick = function () { editBug(item.id) };

    }
}

function editBug(bugID)
{
    // go to edit bug page
    window.location.replace(urlBase + "edit2.html");

    // get the bug info
    // the get bug api is in progress


}

function addBug()
{
    var commonName = document.getElementById("common_name").value;
    var scientificName = document.getElementById("scientific_name").value;
    var clss = document.getElementById("class").value;
    var order = document.getElementById("order").value;
    var family = document.getElementById("family").value;
    var genus = document.getElementById("genus").value;
    var color1 = document.getElementById("color_1").value;
    var color2 = document.getElementById("color_2").value;
    var type = document.getElementById("general_type").value;
    var mouth = document.getElementById("mouth_parts").value;
    var descrip = document.getElementById("description").value;
    var additional = document.getElementById("additional_advice").value;

    // check if any fields are empty
}

//function submitEdit()
//{

//}

//function approveEdit()
//{

//}

//function rejectEdit()
//{

//}
