//로딩게이지
$(document).ready(function () {
    $('#viewLoading').hide();
})
    .ajaxStart(function () {
        $('#viewLoading').show();
    })
    .ajaxStop(function () {
        $('#viewLoading').hide();
    });


