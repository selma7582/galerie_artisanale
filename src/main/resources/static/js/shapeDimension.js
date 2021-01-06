/**
 *
 */
/*

function random(){
    alert('shape.id' +"coucou1");
    var a = document.getElementById('shape.id').valueOf();
    alert('shape.id' +"coucou2"+a);
    if("dimension.shape.id"==="shape.id"){
        var array = [];
        var string="";
        for(i=0 ; i<array.length ; ++i){
            string = "<option>"+array[i]+"</option>";
        }
        string = "<select name='dimension'>"+string+"</select>";
        document.getElementById('dimension').innerHTML=string;

    }
}
*/

$(document).ready(function(){
    // $(".shape").on('change', function(){
    //     var id = this.id;
    //     console.log("teeeeeeeeeeeeesssssssssssst"+id);
    //     /*$('#update-item-'+id).css('display', 'inline-block');*/
    // });
    selectShapeByDimension();
});
function selectShapeByDimension(){
    $('.selectDimension').on('input', function (){
        for (var i =0; i< $('.allDimension').length ; i++){
            $('#dimension' + i).addClass('hidden');
        }
         dimensionSelected();
        });
}
function dimensionSelected(){
for (var i =0; i< $('.allDimension').length ; i++){
    if($('#shape option:selected').val() === $('#dimension' + i).attr('name')){
        $('#dimension' + i).removeClass('hidden');
    }
}
}
// for (var i =0; i< $('.allDimension').length ; i++) {
//     if ($('#shape option:selected').val() === $('#dim' + i).attr('value')) {
//         $('.selectdim').removeClass('hidden');
//     }
// }
