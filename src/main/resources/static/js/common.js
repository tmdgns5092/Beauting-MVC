/**
 * 
 */
//page 이동
function fn_openPage(url) {
    $(location).attr('href', url);
}

$.urlParam = function (name){
    var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    if (results==null){
        return null;
    }
    else{
        return results[1] || 0;
    }
}


