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
    try {
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

        var wings = 0;
        var antenna = 0;
        var hindleg = 0;
        var furry = 0;
        var thin = 0;
        if (document.getElementById("wings").value == "True")
            wings = 1;
        if (document.getElementById("antenna").value == "True")
            antenna = 1;
        if (document.getElementById("hind_legs").value == "True")
            hindleg = 1;
        if (document.getElementById("hairy_furry").value == "True")
            furry = 1;
        if (document.getElementById("thin_body").value == "True")
            thin = 1;

    } catch (e) {
        $(document.getElementById("addMessage")).append('<div class="alert alert-danger alert-dismissible" id="errorRegister">  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>  <strong>Error!</strong> Could not add bug. </div>');
    }
    

    // check if any fields are empty
    if (commonName == "" || scientificName == "" || clss == "" || order == "" || family == "" || genus == "" || color1 == "" || color2 == "" || type == "" || mouth == "" || descrip == "" || additional == "")
    {
        $(document.getElementById("addMessage")).append('<div class="alert alert-danger alert-dismissible" id="missingIngo">  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>  <strong>Missing information!</strong> Make sure all of the fields are filled out to add a bug. </div>');
    }


    var jsonPayload = '{"common_name":"' + commonName + '", "scientific_name":"' + scientificName + '", "class":"' + clss +
        '", "order":"' + order + '", "family":"' + family + '", "genus":"' + genus + '", "color_1":"' + color1 +
        '", "color_2":"' + color2 + '", "general_type":"' + type + '", "mouth_parts":"' + mouth + '", "wings":' + wings +
        ', "antenna":' + antenna + ', "hind_legs_jump":' + hindleg + ', "hairy_furry":' + furry + ', "thin_body":' + thin +
        ', "description":"' + descrip + '", "additional_advice":"' + additional + '"}';
    var url = urlBase + 'api/registerBug';

    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, false);
    xhr.setRequestHeader("Content-type", "application/json; charset=UTF-8");

    try {
        xhr.send(jsonPayload);
        var result = JSON.parse(xhr.responseText).success;

        if (result == 1) {
            // successfully added bug
            $(document.getElementById("addMessage")).append('<div class="alert alert-success alert-dismissible">  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>  <strong>Success!</strong> Added bug. </div>');

        }
        else {
            $(document.getElementById("addMessage")).append('<div class="alert alert-danger alert-dismissible">  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>  <strong>Could not add bug!</strong> Is it already in the database? </div>');

        }

    }
    catch (err) {
        alert(err.message)
    }
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
