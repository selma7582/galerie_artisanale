/**
 *
 */
$(document).ready(function(){
    $(".orderStat").on('change', function(){
        var id = this.id;

        console.log("teeeeeeeeeeeeesssssssssssst"+id);

        $('#update-stat-'+id).css('display', 'inline-block');
    });

});



