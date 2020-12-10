/**
 *
 */
$("#priceRange").slider({});
$("#priceRange").on("slideStop", function (stopEvent) {
    var range = stopEvent.value;
    $("#rangeMin").val(range[0]);
    $("#rangeMax").val(range[1]);

    $.get("/products?min=" + range[0] + "&max=" + range[1], function (data) {
        $("#products").html(data);
    });
});
