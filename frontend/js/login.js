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
            $(document.getElementById("addMessage")).append('<div class="alert alert-danger alert-dismissible">  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>  <strong>Login failed!</strong> Make sure you typed everything correctly. </div>');

            return false;
        } else {
            //successful login
            localStorage.setItem("sessionID", jsonObject.sessionID);
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

        $(table).find('tbody').append("<tr><td>" + item.common_name +
            "</td><td>" + item.scientific_name + "</td><td> <a class='btn btn-sm btn-primary' style='color:white' id='button" +
            item.id + "'>Edit</a> </td></tr>");

        var btn = document.getElementById("button" + item.id);
        btn.onclick = function () { editBug(item.id) };

    }
}

function editBug(bugID)
{
    // go to edit bug page
    localStorage.setItem("editbugID", bugID);
    window.location.replace(urlBase + "edit2.html");
}

function setUpEditPage() {
    // get the bug info
    bugID = localStorage.getItem("editbugID");
    var jsonPayload = '{"id":' + bugID + '}';
    var url = urlBase + 'api/getBug';

    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, false);
    xhr.setRequestHeader("Content-type", "application/json; charset=UTF-8");

    try {
        xhr.send(jsonPayload);

        var bug = JSON.parse(xhr.responseText);

        if (bug.success == 1) {
            document.getElementById("titleBug").innerHTML = "Editing " + bug.common_name;
            document.getElementById("common_name").value = bug.common_name;
            document.getElementById("scientific_name").value = bug.characteristics.scientific_name;
            document.getElementById("class").value = bug.characteristics.class;
            document.getElementById("order").value = bug.characteristics.order;
            document.getElementById("family").value = bug.characteristics.family;
            document.getElementById("genus").value = bug.characteristics.genus;
            document.getElementById("color_1").value = bug.characteristics.color1;
            document.getElementById("color_2").value = bug.characteristics.color2;
            document.getElementById("general_type").value = bug.characteristics.general_type;
            document.getElementById("mouth_parts").value = bug.characteristics.mouth_parts;
            document.getElementById("description").value = bug.description;
            document.getElementById("additional_advice").value = bug.additional_advice;
            if (bug.characteristics.wings)
                document.getElementById("wings").options[1].selected = true;
            if (! bug.characteristics.antenna)
                document.getElementById("antenna").options[1].selected = true;
            if (bug.characteristics.hind_legs_jump)
                document.getElementById("hind_legs").options[1].selected = true;
            if (bug.characteristics.hairy_furry)
                document.getElementById("hairy_furry").options[1].selected = true;
            if (bug.characteristics.thin_body)
                document.getElementById("thin_body").options[1].selected = true;

        }
        else {
            window.location.replace(urlBase + "404.html");
        }

    }
    catch (err) {
        alert(err);
    }
}

function submitEdit()
{
    bugID = localStorage.getItem("editbugID");
    var wings, antenna, legs, furry, thin;

    if (document.getElementById("wings").options[1].selected == true)
        wings = 1;
    else
        wings = 0;
    if (document.getElementById("antenna").options[1].selected == true)
        antenna = 0;
    else
        antenna = 1;
    if (document.getElementById("hind_legs").options[1].selected == true)
        legs = 1;
    else
        legs = 0;
    if (document.getElementById("hairy_furry").options[1].selected == true)
        furry = 1;
    else
        furry = 0;
    if (document.getElementById("thin_body").options[1].selected == true)
        thin = 1;
    else
        thin = 0;



    var jsonPayload = '{"common_name":"' + document.getElementById("common_name").value +
        '", "scientific_name":"' + document.getElementById("scientific_name").value +
        '", "class":"' + document.getElementById("class").value + '", "order":"' + document.getElementById("order").value +
        '", "family":"' + document.getElementById("family").value + '", "genus":"' + document.getElementById("genus").value +
        '", "color_1":"' + document.getElementById("color_1").value + '", "color_2":"' + document.getElementById("color_2").value +
        '", "general_type":"' + document.getElementById("general_type").value + '", "mouth_parts":"' + document.getElementById("mouth_parts").value +
        '", "wings": ' + wings + ', "antenna": ' + antenna + ', "hind_legs_jump": ' + legs + ', "hairy_furry": ' + furry +
        ', "thin_body": ' + thin + ', "description":"' + document.getElementById("description").value +
        '", "additional_advice":"' + document.getElementById("additional_advice").value + '", "bugid": ' + bugID + '}';
    var url = urlBase + 'api/editBug';

    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, false);
    xhr.setRequestHeader("X-Auth-Token", localStorage.getItem("sessionID"));

    try {
        xhr.send(jsonPayload);

        var results = JSON.parse(xhr.responseText);

        if (results.success == 1) {

            $(document.getElementById("addMessage")).append('<div class="alert alert-success alert-dismissible">  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>  <strong>Success!</strong> Edit has been added to submission list. </div>');

        }
        else {
            $(document.getElementById("addMessage")).append('<div class="alert alert-danger alert-dismissible">  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>  <strong>Error!</strong> Edit could not be submitted. Try again later. </div>');
        }


    }
    catch (err) {
        alert(err);
    }
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
    xhr.setRequestHeader("X-Auth-Token", localStorage.getItem("sessionID"));

    try {
        xhr.send(jsonPayload);
        var JSONresult = JSON.parse(xhr.responseText)
        var result = JSONresult.success;

        if (result == 1) {
            // successfully added bug
            $(document.getElementById("addMessage")).append('<div class="alert alert-success alert-dismissible">  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>  <strong>Success!</strong> Added bug. </div>');
        }
        else {
            if (JSONresult.message == "This resource is for admins only") {
                $(document.getElementById("addMessage")).append('<div class="alert alert-danger alert-dismissible">  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>  <strong>Could not add bug!</strong> Only admins can add bugs! </div>');
            }
            else {
                $(document.getElementById("addMessage")).append('<div class="alert alert-danger alert-dismissible">  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>  <strong>Could not add bug!</strong> Is it already in the database? </div>');
            }

        }

    }
    catch (err) {
        alert(err.message)
    }
}

function PopulateTable()
{
    var jsonPayload = '{}';
    var url = urlBase + 'api/getEdits';

    var xhr = new XMLHttpRequest();
    xhr.open("GET", url, false);
    xhr.setRequestHeader("Content-type", "application/json; charset=UTF-8");

    try {
        xhr.send(jsonPayload);

        var JSONObjectsArr = JSON.parse(xhr.responseText);

        if (JSONObjectsArr.success == 1) {
            var table = document.getElementById("approveTable");
            var arr = Array.from(JSONObjectsArr.submissions);
            arr.forEach(function (item, index) {
                addRowOnApproveTable(table, item, index)
            });
        }

    }
    catch (err) {
        alert(err);
    }
}

function addRowOnApproveTable(table, item, index) {
    if (item != null) {
        var old = "";
        var newedits = "";
        var oldBug;
        var newBug;

        // get old bug
        var jsonPayload = '{"id":"' + item.bug_id_old + '"}';
        var url = urlBase + 'api/getBug';
        var xhr = new XMLHttpRequest();
        xhr.open("POST", url, false);
        xhr.setRequestHeader("Content-type", "application/json; charset=UTF-8");
        try {
            xhr.send(jsonPayload);
            oldBug = JSON.parse(xhr.responseText);
        }
        catch (err) {
            alert(err);
            return;
        }

        // get new bug
        var jsonPayload = '{"id":"' + item.bug_id_new + '"}';
        var xhr = new XMLHttpRequest();
        xhr.open("POST", url, false);
        xhr.setRequestHeader("Content-type", "application/json; charset=UTF-8");

        try {
            xhr.send(jsonPayload);
            newBug = JSON.parse(xhr.responseText);
        }
        catch (err) {
            alert(err);
            return;
        }

        // set up strings
        old = oldBug.common_name + " / " + oldBug.characteristics.scientific_name + "\n\n";
        newedits = newBug.common_name + " / " + newBug.characteristics.scientific_name + "\n\n";

        // add any differences to the strings
        if (oldBug.characteristics.class != newBug.characteristics.class) {
            old = old + "class: " + oldBug.characteristics.class + "\n\n";
            newedits = newedits + "class: " + newBug.characteristics.class + "\n\n";
        }
        if (oldBug.characteristics.order != newBug.characteristics.order) {
            old = old + "order: " + oldBug.characteristics.order + "\n\n";
            newedits = newedits + "order: " + newBug.characteristics.order + "\n\n";
        }
        if (oldBug.characteristics.family != newBug.characteristics.family) {
            old = old + "family: " + oldBug.characteristics.family + "\n\n";
            newedits = newedits + "family: " + newBug.characteristics.family + "\n\n";
        }
        if (oldBug.characteristics.genus != newBug.characteristics.genus) {
            old = old + "genus: " + oldBug.characteristics.genus + "\n\n";
            newedits = newedits + "genus: " + newBug.characteristics.genus + "\n\n";
        }
        if (oldBug.characteristics.color1 != newBug.characteristics.color1) {
            old = old + "Primary color: " + oldBug.characteristics.color1 + "\n\n";
            newedits = newedits + "Primary color: " + newBug.characteristics.color1 + "\n\n";
        }
        if (oldBug.characteristics.color2 != newBug.characteristics.color2) {
            old = old + "Secondary color: " + oldBug.characteristics.color2 + "\n\n";
            newedits = newedits + "Secondary color: " + newBug.characteristics.color2 + "\n\n";
        }
        if (oldBug.characteristics.antenna != newBug.characteristics.antenna) {
            old = old + "Antenna: " + oldBug.characteristics.antenna + "\n\n";
            newedits = newedits + "Antenna: " + newBug.characteristics.antenna + "\n\n";
        }
        if (oldBug.characteristics.general_type != newBug.characteristics.general_type) {
            old = old + "General type: " + oldBug.characteristics.general_type + "\n\n";
            newedits = newedits + "General type: " + newBug.characteristics.general_type + "\n\n";
        }
        if (oldBug.characteristics.hairy_furry != newBug.characteristics.hairy_furry) {
            old = old + "Furry: " + oldBug.characteristics.hairy_furry + "\n\n";
            newedits = newedits + "Furry: " + newBug.characteristics.hairy_furry + "\n\n";
        }
        if (oldBug.characteristics.hind_legs_jump != newBug.characteristics.hind_legs_jump) {
            old = old + "Jumping legs: " + oldBug.characteristics.hind_legs_jump + "\n\n";
            newedits = newedits + "Jumping legs: " + newBug.characteristics.hind_legs_jump + "\n\n";
        }
        if (oldBug.characteristics.mouth_parts != newBug.characteristics.mouth_parts) {
            old = old + "Mouth parts: " + oldBug.characteristics.mouth_parts + "\n\n";
            newedits = newedits + "Mouth parts: " + newBug.characteristics.mouth_parts + "\n\n";
        }
        if (oldBug.characteristics.wings != newBug.characteristics.wings) {
            old = old + "Wings: " + oldBug.characteristics.wings + "\n\n";
            newedits = newedits + "Wings: " + newBug.characteristics.wings + "\n\n";
        }
        if (oldBug.description != newBug.description) {
            old = old + "Description: " + oldBug.description + "\n\n";
            newedits = newedits + "Description: " + newBug.description + "\n\n";
        }
        if (oldBug.additional_advice != newBug.additional_advice) {
            old = old + "Additional advice: " + oldBug.additional_advice + "\n\n";
            newedits = newedits + "Additional advice: " + newBug.additional_advice + "\n\n";
        }


        // add row to table
        $(table).find('tbody').append("<tr><td>" + old +
            "</td><td>" + newedits + "</td><td><button type='button' id='approvebutton" +
            item.submission_id + "'>Approve!</button> </td><td> <button type='button' id='rejectbutton" +
            item.submission_id + "'>Reject!</button> </td></tr>");

        var btn = document.getElementById("approvebutton" + item.submission_id);
        btn.onclick = function () { approveEdit(item.submission_id) };
        var btn2 = document.getElementById("rejectbutton" + item.submission_id);
        btn2.onclick = function () { rejectEdit(item.submission_id) };
    }
}


function approveEdit(submissionID)
{
    var jsonPayload = '{"id":' + submissionID + ', "approve":true}';
    var url = urlBase + 'api/approveEdit';
    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, false);
    xhr.setRequestHeader("X-Auth-Token", localStorage.getItem("sessionID"));
    try {
        xhr.send(jsonPayload);
        result = JSON.parse(xhr.responseText);

        if (result.success == true) {
            $(document.getElementById("addMessage")).append('<div class="alert alert-success alert-dismissible">  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>  <strong>Success!</strong> Edit approved. </div>');

            var table = document.getElementById("approveTable");
            while (table.rows.length > 1) {
                table.deleteRow(1);
            }
            PopulateTable();

        }
        else
            $(document.getElementById("addMessage")).append('<div class="alert alert-danger alert-dismissible">  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>  <strong>Approve failed!</strong> Only admins can approve or reject edits. </div>');

    }
    catch (err) {
        alert(err);
        return;
    }
}

function rejectEdit(submissionID)
{
    var jsonPayload = '{"id":' + submissionID + ', "approve":false}';
    var url = urlBase + 'api/approveEdit';
    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, false);
    xhr.setRequestHeader("X-Auth-Token", localStorage.getItem("sessionID"));
    try {
        xhr.send(jsonPayload);
        result = JSON.parse(xhr.responseText);

        if (result.success == true) {
            $(document.getElementById("addMessage")).append('<div class="alert alert-success alert-dismissible">  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>  <strong>Success!</strong> Edit successfully rejected. </div>');

            var table = document.getElementById("approveTable");
            while (table.rows.length > 1) {
                table.deleteRow(1);
            }
            PopulateTable();
        }
        else
            $(document.getElementById("addMessage")).append('<div class="alert alert-danger alert-dismissible">  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>  <strong>Reject failed!</strong> Only admins can approve or reject edits. </div>');

    }
    catch (err) {
        alert(err);
        return;
    }
}
