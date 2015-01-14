$(window).load(function(){
    $("ol.progtrckr").each(function(){
        $(this).attr("data-progtrckr-steps",
            $(this).children("li").length);
    });

});
function copyPage(name){
    window.location.href="/minimo/copy/"+name+"?topage="+$('#copypage').val();
}

function deletePage(name){
    window.location.href="/minimo/delete/"+name;
}