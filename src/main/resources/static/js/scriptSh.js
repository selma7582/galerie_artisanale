/**
 *
 */

// $(document).ready(function() {
//     $('.delete-shape').on('click', function (){
//         /*<![CDATA[*/
//         var path = /*[[@{/}]]*/'removeS';
//         /*]]>*/
//
//         var id=$(this).attr('id');
//
//
//         bootbox.confirm({
//             message: "supprimer une forme 'id'? It can't be undone ."+id,
//             buttons: {
//                 cancel: {
//                     label:'<i class="fa fa-times"></i> Cancel'
//                 },
//                 confirm: {
//                     label:'<i class="fa fa-check"></i> Confirm'
//                 }
//             },
//             callback: function(confirmed) {
//                 if(confirmed) {
//                     console.log(id);
//
//                     $.post(path, {'id':id}, function(res) {
//                         location.reload();
//                     });
//                 }
//             }
//         });
//     });
//
//
//
//
//     $('#deleteSelected').click(function() {
//         var idList= $('.checkboxShape');
//         var shapeIdList=[];
//         for (var i = 0; i < idList.length; i++) {
//             if(idList[i].checked==true) {
//                 shapeIdList.push(idList[i]['id'])
//             }
//         }
//
//         console.log(shapeIdList);
//
//         /*<![CDATA[*/
//         var path = /*[[@{/}]]*/'removeList';
//         /*]]>*/
//
//         bootbox.confirm({
//             message: "Are you sure to remove all selected shape? It can't be undone.",
//             buttons: {
//                 cancel: {
//                     label:'<i class="fa fa-times"></i> Cancel'
//                 },
//                 confirm: {
//                     label:'<i class="fa fa-check"></i> Confirm'
//                 }
//             },
//             callback: function(confirmed) {
//                 if(confirmed) {
//                     $.ajax({
//                         type: 'POST',
//                         url: path,
//                         data: JSON.stringify(shapeIdList),
//                         contentType: "application/json",
//                         success: function(res) {
//                             console.log(res);
//                             location.reload()
//                         },
//                         error: function(res){
//                             console.log(res);
//                             location.reload();
//                         }
//                     });
//                 }
//             }
//         });
//     });
//
//     $("#selectAllShape").click(function() {
//         if($(this).prop("checked")==true) {
//             $(".checkboxShape").prop("checked",true);
//         } else if ($(this).prop("checked")==false) {
//             $(".checkboxShape").prop("checked",false);
//         }
//     })
//
//
// });
var deleteShapeUrl = $('#js-template-global').attr('data-delete-shape-url');
$(document).ready(function () {
    initButtons();
    initModalButtons();

});

function initButtons() {
    $('#deleteShape').on('click', function (e) {
        $('#confirm').removeClass('required');
        e.preventDefault();
        $('#checkboxAll').attr('data-substitute', $(this).attr('id'));
        $('#deleteShapeConfirmationModal').modal('show');

    });
}

function validateAtLeastOneChecked(form) {
    var form = $(form);
    var checkboxAll = form.find('.checkboxAll');

    checkboxAll.addClass('atLeastOneChecked');

    var valid = form.length && form.valid;

    checkboxAll.removeClass('atLeastOneChecked');

    // remove red color from checkboxAll
    checkboxAll.parent().parent().removeClass('has-error');

    return valid;
}

function initModalButtons() {
    $('#yesButton').on('click', function (e) {
        e.preventDefault();
        var form = $($('#deleteShape').attr('data-target'));
        if (validateAtLeastOneChecked(form)) {
            var shapes = $('input[name*=shape]:checked');
            $('#shapeForm').attr('action', deleteShapeUrl).submit();

        }
    });

    $('#noButton').on('click', function () {
        $('#deleteShapeConfirmationModal').modal('hide');
    });
}
