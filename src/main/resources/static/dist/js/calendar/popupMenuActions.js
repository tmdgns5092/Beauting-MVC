/* 전화걸기 */
$(document).on('click', '#td_client_makeCall', function () {
    console.log(c_info.phone);
    divCallShow(c_info.phone);
});
/* 문자보내기*/
$(document).on('click', '#td_client_message1,#td_client_message2,#td_client_message3', function () {
    if (c_info.phone == "") {
        alert('전화번호를 등록해주세요.');
    } else if (c_info.sms_check == "1") {
        form_submit('/Messages/message', {filter: 'phone', text: c_info.phone});
    } else alert('SMS수신동의 체크를 해주세요.');
});

$(document).on("click", "#td_salse", function () {
    //console.log("click");
    var form = document.createElement("form");
    $(form).attr("action", "/Sales/sales").attr("method", "post");
    $(form).html('<input type="hidden" name="payment" value="' + $(this).attr("data-value") + '" />' + '<input type="hidden" name="forDate" value="' + forDate + '" />');
    document.body.appendChild(form);
    $(form).submit();
    document.body.removeChild(form);
});

$(document).on("click", "#td_mf_reservation", function () {
    var res_idx = $(this).attr("data-value");
    var refund_data = "";
    $.ajax({
        url: "/Reservation/modifiedReservation",
        type: "post",
        data: {"idx": res_idx},
        dataType: "json",
        async: false,
        success: function (data) {
            refund_data = data;
        },
        error: function () {
            alert("에러가 발생했습니다.");
            location.href = document.URL;
        }
    });
    //console.log(JSON.stringify(refund_data));
    if (refund_data.code == "200") {
        makeMFReservation(refund_data);
        $("#reservation-mf").modal("show");
    } else {
        alert("잠시 후 다시 시도해 주세요.");
        form_submit("/Reservation/calendar", {forDate: $("#forDate").val()}, "post");
    }
});
$(document).on("click", "#td_noshow", function () {
    var res_idx = $(this).attr("data-value");
    var con_test = confirm("예약을 불이행 하시겠습니까?");
    if (con_test) {
        $.ajax({
            url: "/Reservation/reservationCancel",
            type: "post",
            data: {"idx": res_idx},
            dataType: "json",
            success: function (data) {
                if (data.code == "200") {
                    form_submit("/Reservation/calendar", {forDate: $("#forDate").val()}, "post");
                } else {
                    alert("잠시 후 다시 시도해 주세요.");
                    form_submit("/Reservation/calendar", {forDate: $("#forDate").val()}, "post");
                    return false;
                }
            },
            error: function () {
                alert("에러가 발생했습니다.");
                form_submit("/Reservation/calendar", {forDate: $("#forDate").val()}, "post");
            }
        });
    } else {
        if (!con_test) {
            return false;
        }
    }
});
$(document).on("click", "#td_rm_reservation", function () {
    var res_idx = $(this).attr("data-value");
    var con_test = confirm("예약을 취소 하시겠습니까?");
    if (con_test) {
        $.ajax({
            url: "/Reservation/reservationRemove",
            type: "post",
            data: {"idx": res_idx},
            dataType: "json",
            success: function (data) {
                if (data.code == "200") {
                    form_submit("/Reservation/calendar", {forDate: $("#forDate").val()}, "post");
                } else {
                    alert("잠시 후 다시 시도해 주세요.");
                    form_submit("/Reservation/calendar", {forDate: $("#forDate").val()}, "post");
                    return false;
                }
            },
            error: function () {
                alert("에러가 발생했습니다.");
                form_submit("/Reservation/calendar", {forDate: $("#forDate").val()}, "post");
            }
        });
    } else {
        if (!con_test) {
            return false;
        }
    }
});
/* 모달 */
var iModal = $('#c-info-modal');

$(document).on("click", "#td_client_info1, #td_client_info2, #td_client_info3", function () {
    var idx;
    var res_idx = $(this).attr("data-value");
    var ajax_data = {res_idx: res_idx};
    var data = submitAjax("/Reservation/clientInfoIdxCall", ajax_data);
    if (data.code == "200") {
        if (data.client_idx == "0" || data.type == "1") {
            alert("등록된 고객이 아닙니다.");
            return false;
        }
        idx = data.client_idx;
    } else {
        /*return false;*/
    }
    client_Info(idx);
    $("#c-info-modal").modal("show");
});
$(document).on("click", "#td_noshow_cancel", function () {
    var res_idx = $(this).attr("data-value");
    var con_test = confirm("예약 불이행을 취소 하시겠습니까?");
    if (con_test) {
        $.ajax({
            url: "/Reservation/reservationStatusUpdateDefault",
            type: "post",
            data: {"idx": res_idx},
            dataType: "json",
            success: function (data) {
                if (data.code == "200") {
                    form_submit("/Reservation/calendar", {forDate: $("#forDate").val()}, "post");
                } else {
                    alert("잠시 후 다시 시도해 주세요.");
                    form_submit("/Reservation/calendar", {forDate: $("#forDate").val()}, "post");
                    return false;
                }
            },
            error: function () {
                alert("에러가 발생했습니다.");
                form_submit("/Reservation/calendar", {forDate: $("#forDate").val()}, "post");
            }
        });
    } else {
        return false;
    }
});
$(document).on("click", "#cancel-sales", function () {
    var res_idx = $(this).attr("data-value");
    var con_test = confirm("판매를 취소 하시겠습니까?");
    if (con_test) {
        $.ajax({
            url: "/Sales/cancelSales",
            type: "post",
            data: {"res_idx": res_idx, "forDate": $('#forDate').val()},
            dataType: "json",
            success: function (data) {
                if (data.code == "200") {
                    form_submit("/Reservation/calendar", {forDate: $("#forDate").val()}, "post");
                } else {
                    alert("잠시 후 다시 시도해 주세요.");
                    form_submit("/Reservation/calendar", {forDate: $("#forDate").val()}, "post");
                    return false;
                }
            },
            error: function () {
                alert("에러가 발생했습니다.");
                form_submit("/Reservation/calendar", {forDate: $("#forDate").val()}, "post");
            }
        });
    } else {
        return false;
    }
});