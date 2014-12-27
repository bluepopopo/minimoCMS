$(window).load(function(){
    $("ol.progtrckr").each(function(){
        $(this).attr("data-progtrckr-steps",
            $(this).children("li").length);
    });
})