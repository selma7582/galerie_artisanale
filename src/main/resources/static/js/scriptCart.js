/**
 *
 */
$(document).ready(function(){
    $(".cartItemQty").on('change', function(){
        var id = this.id;

        console.log("teeeeeeeeeeeeesssssssssssst"+id);

        $('#update-item-'+id).css('display', 'inline-block');
    });

});




function checkPasswordMatch() {
    var password = $("#txtNewPassword").val();
    var confirmPassword = $("#txtConfirmPassword").val();

    if(password == "" && confirmPassword =="") {
        $("#checkPasswordMatch").html("");
        $("#updateUserInfoButton").prop('disabled', false);
    } else {
        if(password != confirmPassword) {
            $("#checkPasswordMatch").html("Passwords do not match!");
            $("#updateUserInfoButton").prop('disabled', true);
        } else {
            $("#checkPasswordMatch").html("Passwords match");
            $("#updateUserInfoButton").prop('disabled', false);
        }
    }
}


$(document).ready(function(){
    $("#txtConfirmPassword").keyup(checkPasswordMatch);
    $("#txtNewPassword").keyup(checkPasswordMatch);
});

