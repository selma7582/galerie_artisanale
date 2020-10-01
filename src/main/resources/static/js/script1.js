/**
 *
 */


    $(document).ready(function() {
    $("#productListTable").DataTable({
        "lengthMenu": [[5,10,15,20,-1],[5,10,15,20,"All"]],
        "ordering": false,
        stateSave: true
    });
});



