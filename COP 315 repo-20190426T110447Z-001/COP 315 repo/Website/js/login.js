var attempt = 3; // Variable to count number of attempts.
// Below function Executes on click of login button.
function validate(){
var username = document.getElementById("username").value;
var password = document.getElementById("password").value;
if ( username == "admin" && password == "admin"){

window.location = "select.html"; // Redirecting to other page.
return false;
}
else{
attempt --;// Decrementing by one.



}
}