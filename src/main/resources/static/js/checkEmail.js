/**
 *
 */



function checkEmail() {

    var email = document.getElementById('email');
    var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;

    if(email == ""){
        $("#checkEmail").html("");
        $("#inscriptionButton").prop('disabled', false);

    }
    if (!filter.test(email.value)) {
        $("#checkEmail").html("Entrer un email valide").css("color","red");
        $("#inscriptionButton").prop('disabled', true);

        return false;
    }
    else {
        $("#checkEmail").html("L'email est valide").css("color","green");
        $("#inscriptionButton").prop('disabled', false);

    }
}

$(document).ready(function(){
    $("#email").keyup(checkEmail);
});
