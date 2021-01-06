/**
 *
 */


    $(document).ready(function() {
    $("#productListTable").DataTable({
        "lengthMenu": [[5,10,15,20,-1],[5,10,15,20,"All"]],
        "ordering": false,
        stateSave: true,
        language: {

            url:"https://cdn.datatables.net/plug-ins/1.10.16/i18n/French.json"
        }


    });

});
/*var table = $('#example').DataTable( {
    "paging": true,
    language: { url: "https://cdn.datatables.net/plug-ins/1.10.16/i18n/French.json" },
    pagingType: "simple_numbers",
    lengthMenu:[10,20,30,40,50],
    pageLength: 50
} );*/



