/* Ticket Make Table Js*/

function makeTicketTableModal(data, idx, name){
    $('#ticket_cate_box').empty();
    $('#ticket_detail_box').empty();

    $('#ticket_name_modal').text('');
    $('#ticket_cost_modal').val('');
    $('#ticket_count_modal').val('');
    $('#ticket_validity_modal').val('');

    $('p[name=pay-cost]').text('0');
    $('p[name=card-cost]').text('0');
    $('p[name=gift-cost]').text('0');
    $('p[name=order-cost]').text('0');

    var tmp1 = '';
    var tmp2 = '';

    $.each(data.emplList, function(index, value){
        if (idx == value.idx && name == value.name) tmp1 = tmp1 + '<option value="' + value.idx + '" selected>' + value.name + '</option>';
        else tmp1 = tmp1 + '<option value="' + value.idx + '">' + value.name + '</option>';

        tmp2 = tmp2 + '<option value="' + value.idx + '">' + value.name + '</option>';
    });

    $('#ticket_pay_empl_list_box1').empty();
    $('#ticket_pay_empl_list_box1').append('<option value="">선택</option>');
    $('#ticket_pay_empl_list_box2').empty();
    $('#ticket_pay_empl_list_box2').append('<option value="">선택</option>');
    $('#ticket_pay_empl_list_box1').append(tmp1);
    $('#ticket_pay_empl_list_box2').append(tmp2);

    $('select').niceSelect('update');

    ticket_cate = data.cateList;
    ticket_detail = data.detailList;

    ticketMakeCate();
}

/* 카테고리 그리기 */
function ticketMakeCate(){
    var tmp = '';
    for(var i = 0 ; i < ticket_cate.length; i ++){
        tmp = tmp + '<li class="ticket_cate" data-value="' + ticket_cate[i].idx + '">' + ticket_cate[i].category + '</li>';
    }

    $('#ticket_cate_box').append(tmp);
}

/* 디테일 그리기 */
function ticketMakeDetail(cate){
    $('#ticket_detail_box').empty();
    var tmp = '';

    for(var i = 0; i < ticket_detail.length; i++){
        if(ticket_detail[i].category == cate)
            tmp = tmp + '<li class="ticket_detail" data-value="' + ticket_detail[i].idx + '">' +
                '<p name="name">' + ticket_detail[i].name + '</p>' +
                '<input type="hidden" name="cost" value="' + ticket_detail[i].cost + '"> '+
                '<input type="hidden" name="count" value="' + ticket_detail[i].count + '"> '+
                '<input type="hidden" name="validity" value="' + ticket_detail[i].validity + '"> '+
                '</li>';
    }

    $('#ticket_detail_box').append(tmp);

}

/* 카테고리 클릭 */
$(document).on('click', '.ticket_cate', function(){
    var cate_name = $(this).text();
    $('#ticket_category_name').val(cate_name);
    ticketMakeDetail(cate_name)
});

/* 디테일 클릭 */
$(document).on('click', '.ticket_detail', function(){
    $('.ticket-sale-btn').show();
    $('.ticket-pay-btn').show();
    $('.ticket-card-btn').show();
    $('.ticket-gift-btn').show();
    $('.ticket-other-btn').show();
    // $('.ticket-point-btn').show();

    var idx = $(this).attr('data-value');
    var name = $(this).find('p[name=name]').text();
    var cost = $(this).find('input[name=cost]').val();
    // var count = $(this).find('input[name=count]').val();
    var count = "1";
    var validity = $(this).find('input[name=validity]').val();

    if(uk(validity) == "") validity = 12;

    $('#ticket_idx_tmp').val(idx);
    $('#ticket_name_modal').text(name);
    $('#ticket_cost_modal').val(comma(cost));
    $('#ticket_count_modal').val(comma(count));
    $('#ticket_validity_modal').val(undefined_check(validity));
    $('#ticket_right_total_pay_cost').text(comma(cost));
    $('#ticket_miss_cost_text').text(comma(cost));
    $('#ticket_employee_one_cost').val(cost);

    $('#right_sum_count').text(comma(count));

    $('#ticket-sale-cost').text('0');
    $('#ticket-pay-cost').text('0');
    $('#ticket-card-cost').text('0');
    $('#ticket-gift-cost').text('0');
    $('#ticket-other-cost').text('0');
    // $('#ticket-point-cost').text('0');


    $('#ticket_total_cost_by_fee').val(cost);
    $('#ticket_pay_empl_list_box2').closest('div').find('.nice-select').hide();
    $('#ticket_employee_two_cost').val('0');
    $('#ticket_employee_two_cost').hide();
    $('#ticket-employeePlus').show();
    $('#ticket-employeeMinus').hide();
});

// 티켓 횟수 변경
function ticketCostChange(obj){
    var count = Number(uk_noshow($(obj).val()));
    var cost = $('#ticket_cost_modal').val();
    $(obj).val(count);

    // 금액 && 미수금 && 적립포인트 초기화
    $("#ticket-sale-cost").text("0");
    $("#ticket_sale_type").text("%");
    $("#ticket-pay-cost").text("0");
    $("#ticket-card-cost").text("0");
    $("#ticket-gift-cost").text("0");
    $("#ticket_point_cost_text").text("0");
    $("#ticket_add_point_text").text("");

    // 횟수에 맞는 값 세팅
    if($("#ticket_employee_two_cost").css("display") == "none") {
        $('#ticket_employee_one_cost').val(count * uncomma(cost));
        $('#ticket_employee_two_cost').val("0");
    } else {
        $('#ticket_employee_one_cost').val((count * uncomma(cost)) / 2);
        $('#ticket_employee_two_cost').val((count * uncomma(cost)) / 2);
    }

    $('#ticket_miss_cost_text').text(comma(count * uncomma(cost)));
    $('#ticket_right_total_pay_cost').text(comma(count * uncomma(cost)));
    
    // 직원 2명 시 금액 나누는 기준
    $('#ticket_total_cost_by_fee').val(comma(count * uncomma(cost)));
};

/* 횟수 변경에 따론 오른쪽 총 횟수 변경 */
function sumCountChange(obj){
    $('#right_sum_count').text(comma($(obj).val()));
}

/* 직원 선택 */
function ticketPlusEmployee(){
    empl2_flag = true;
    var payment_cost = uk_noshow(uncomma($('#ticket_total_cost_by_fee').val()));
    var cost1 = payment_cost / 2;
    var cost2 = payment_cost / 2;

    $('#ticket-employeePlus').hide();
    $('#ticket_pay_empl_list_box2').closest('div').find('.nice-select').show();
    $('#ticket-employeeMinus').show();
    // $('#prepaid_div2').show();

    $('#ticket_employee_one_cost').val(cost1);
    $('#ticket_employee_two_cost').val(cost2);
    $('#ticket_employee_two_cost').show();
}
function ticketMinusEmployee(){
    empl2_flag = false;
    var payment_cost = parseInt(uk_noshow(uncomma($('#ticket_total_cost_by_fee').val())));

    $('#ticket-employeePlus').show();
    $('#ticket_pay_empl_list_box2').closest('div').find('.nice-select').hide();
    $('#ticket-employeeMinus').hide();
    // $('#prepaid_div2').hide();


    $('#ticket_employee_one_cost').val(payment_cost);
    $('#ticket_employee_two_cost').val('0');
    $('#ticket_employee_two_cost').hide();
}

/* 티켓 합치기 */
$('#merge-chk').click(function(){
    if(sum_ticket_falg){
        $.ajax({
            url: "/Sales/callClientTicketList",
            type: "post",
            dataType: "json",
            data: {"client_idx": client_idx},
            success: function (data) {
                if (data.code == 200) {
                    makeClientTicketSumTable(data.pMap);
                    sum_ticket_falg = false;
                } else {
                    alert("잠시 후 다시 시도해 주세요");
                    return false;
                }
            },
            error: function () {
                alert("에러가 발생했습니다.");
                location.href = document.URL;
            }
        });
    }
});

/* 티켓 합치기 테이블 그리기 */
function makeClientTicketSumTable(pMap){
    var json_list = new Array();
    var tmp = '<option value="0">선택</option>';

    json_list = jQuery.parseJSON(pMap.ticket);
    ticket_hass_client = jQuery.parseJSON(pMap.ticket);
    $.each(json_list, function(index, value){
        if(value.count > 0){
            tmp = tmp + '<option style="display: none;" value="'+
                index +
                '">' + value.name + '</option>';
        }
    });

    $('#ticket_sum_select_tag').empty();
    $('#ticket_sum_select_tag').append(tmp);
    $('#ticket_sum_select_tag').niceSelect('update');
    $('#ticket_pay_empl_list_box2').closest('div').find('.nice-select').hide();
}

/* 티켓 합치기 셀렉트 변경 */
$('#ticket_sum_select_tag').change(function(){
    var tmp = $(this).val();

    $('#sum_has_ticket_idx').val(ticket_hass_client[tmp].idx);
    $('#sum_has_ticket_count').val(ticket_hass_client[tmp].count);
    $('#sum_has_ticket_name').val(ticket_hass_client[tmp].name);
    $('#sum_has_ticket_sales_idx').val(ticket_hass_client[tmp].sale_idx);
    $('#sum_has_ticket_validity').val(ticket_hass_client[tmp].validity);

    $('#ticket_sum_cost').text(ticket_hass_client[tmp].count);
    $('#ticket_sum_validity').text(ticket_hass_client[tmp].validity);

});


/* 직원 2명 금액 나누기 */
$('#ticket_employee_one_cost, #ticket_employee_two_cost').keyup(function(){
    if(Number($(this).val()) > Number(uk_noshow(uncomma($('#ticket_total_cost_by_fee').val())))){
        $(this).val(uk_noshow(uncomma($('#ticket_total_cost_by_fee').val())));

    }
    if(empl2_flag){
        var empl1_val, empl2_val;
        if($(this).attr('id') == 'ticket_employee_one_cost'){
            empl1_val = uk_noshow($(this).val());
            if(empl1_val.indexOf('0') == 0) empl1_val = empl1_val.substring(1, empl1_val.length);
            empl2_val = Number(uk_noshow(uncomma($('#ticket_total_cost_by_fee').val())) - empl1_val);
        }
        else{
            empl2_val = uk_noshow($(this).val());
            if(empl2_val.indexOf('0') == 0) empl2_val = empl2_val.substring(1, empl2_val.length);
            empl1_val = Number(uk_noshow(uncomma($('#ticket_total_cost_by_fee').val())) - empl2_val);
        }
        $('#ticket_employee_one_cost').val(empl1_val);
        $('#ticket_employee_two_cost').val(empl2_val);
    }
});
$('#ticket_employee_one_cost, #ticket_employee_two_cost').focusout(function(){
    if(uk($(this).val()) == "") $(this).val('0');
});

//  입력
$(document).on('click', '.ticket-pay-btns', function(){
    var check = $(this).find('p').text();

    if($(this).find('p').find('i').hasClass('fas')){
        var tmp = $(this).closest('div').find('input').val();
        tmp = tmp.substring(0, tmp.length-1);
        $(this).closest('div').find('input').val(tmp);
    }
    if(check == '천원'){
        var tmp = $(this).closest('div').find('input').val();
        $(this).closest('div').find('input').val(tmp + '000');
    }
    else if(check == '만원'){
        var tmp = $(this).closest('div').find('input').val();
        $(this).closest('div').find('input').val(tmp + '0000');
    }
    else if(check == '십만원'){
        var tmp = $(this).closest('div').find('input').val();
        $(this).closest('div').find('input').val(tmp + '00000');
    }
    else{
        var tmp = $(this).closest('div').find('input').val();
        if(tmp == 0)
            $(this).closest('div').find('input').val(check);
        else
            $(this).closest('div').find('input').val(tmp + check);
    }
    missCostCheck();
});

/*function ticketPointCheck(){
    var this_value = parseInt($('#ticket_point_text').val());
    if(client_point < this_value){
        return false;
    } else{
        return true;
    }
}*/

/* 금액 입력 '선택'버튼 클릭 */
function ticketChoiceButtonClick(tagId, obj) {
    var tmp = '';

    if (tagId == "#ticket_pay_text" && uncomma($(obj).closest('div').find('p[name=ticket-pay-cost]').text()) != "0") tmp = uncomma($(obj).closest('div').find('p[name=ticket-pay-cost]').text());
    else if (tagId == "#ticket_pay_text") tmp = uncomma($('#ticket_miss_cost_text').text());

    if (tagId == "#ticket_card_text" && uncomma($(obj).closest('div').find('p[name=ticket-card-cost]').text()) != "0") tmp = uncomma($(obj).closest('div').find('p[name=ticket-card-cost]').text());
    else if (tagId == "#ticket_card_text") tmp = uncomma($('#ticket_miss_cost_text').text());

    if (tagId == "#ticket_gift_text" && $(obj).closest('div').find('p[name=ticket-gift-cost]').text() != "0") tmp = uncomma($(obj).closest('div').find('p[name=ticket-gift-cost]').text());
    else if (tagId == "#ticket_gift_text") tmp = uncomma($('#ticket_miss_cost_text').text());

    if (tagId == "#ticket_other_text" && uncomma($(obj).closest('div').find('p[name=ticket-other-cost]').text()) != "0") tmp = uncomma($(obj).closest('div').find('p[name=ticket-other-cost]').text());
    else if (tagId == "#ticket_other_text") tmp = uncomma($('#ticket_miss_cost_text').text());

    /*if (tagId == "#ticket_point_text" && uncomma($(obj).closest('div').find('p[name=ticket-point-cost]').text()) != "0") tmp = uncomma($(obj).closest('div').find('p[name=ticket-point-cost]').text());
    else if (tagId == "#ticket_point_text") {
        tmp = uncomma($('#ticket_miss_cost_text').text());
        if (client_point < tmp)
            tmp = client_point;
    }*/
    $(tagId).val(tmp);
}


/* 할인 퍼센트 */
$('.ticket-percent-btns').click(function(){

    $('#ticket-sale_pay_text').text('0');
    $('#ticket-pay-cost').text('0');
    $('#ticket-card-cost').text('0');
    $('#ticket-gift-cost').text('0');
    $('#ticket-other-cost').text('0');

    var tmp = parseInt($(this).closest('div').find('input').val());
    if(tmp > 100){
        alert("할인율 100% 초과");
        return false;
    } else if(uk_noshow($(this).closest('div').find('input').val()) == 0){
        tmp = 0;
    }

    $(this).closest('tr[name=contens]').find('p[name=ticket-sale-cost]').text(tmp);
    $('#ticket_sale_type').text('%');
    $(".modal-content-close").trigger('click');
    ticketMissCostCheck("sale");
});
/* 할인 원 */
$('.ticket-won-btns').click(function(){

    $('#ticket-sale_pay_text').text('0');
    $('#ticket-pay-cost').text('0');
    $('#ticket-card-cost').text('0');
    $('#ticket-gift-cost').text('0');
    $('#ticket-other-cost').text('0');

    var pay_cost = parseInt(uncomma($('#ticket_cost_modal').text()));
    var tmp = parseInt($(this).closest('div').find('input').val());
    if(uk_noshow($(this).closest('div').find('input').val()) == 0){
        tmp = 0;
    } else if(pay_cost < tmp){
        tmp = pay_cost;
        $(this).closest('div').find('input').val(tmp);
    }

    $(this).closest('tr[name=contens]').find('p[name=ticket-sale-cost]').text(comma(tmp));
    $('#ticket_sale_type').text('원');
    $(".modal-content-close").trigger('click');
    ticketMissCostCheck("sale");
});

// 현금 저장
$(document).on('click', '.ticket-pay-cost-submit', function(){
    var tmp = $(this).closest('div').find('input').val();
    $(this).closest('tr[name=contens]').find('p[name=ticket-pay-cost]').text(comma(uk_noshow(tmp)));
    if (ticketMissCostCheck()) {
        $(this).closest('tr[name=contens]').find('p[name=pay-cost]').text('0');
        alert('합계금액을 초과하였습니다');
        return false;
    } else {
        $(".modal-content-close").trigger('click');
    }
});
// 카드 저장
$(document).on('click', '.ticket-card-cost-submit', function(){
    var tmp = $(this).closest('div').find('input').val();
    $(this).closest('tr[name=contens]').find('p[name=ticket-card-cost]').text(comma(uk_noshow(tmp)));
    if (ticketMissCostCheck()) {
        $(this).closest('tr[name=contens]').find('p[name=ticket-card-cost]').text('0');
        alert('합계금액을 초과하였습니다');
        return false;
    } else {
        $(".modal-content-close").trigger('click');
    }
});
// 상품권 저장
$(document).on('click', '.ticket-gift-cost-submit', function(){
    var tmp = $(this).closest('div').find('input').val();
    $(this).closest('tr[name=contens]').find('p[name=ticket-gift-cost]').text(comma(uk_noshow(tmp)));
    if (ticketMissCostCheck()) {
        $(this).closest('tr[name=contens]').find('p[name=ticket-gift-cost]').text('0');
        alert('합계금액을 초과하였습니다');
        return false;
    } else {
        $(".modal-content-close").trigger('click');
    }
});
// 기타 저장
$(document).on('click', '.ticket-other-cost-submit', function(){
    var tmp = $(this).closest('div').find('input').val();
    $(this).closest('tr[name=contens]').find('p[name=ticket-other-cost]').text(comma(uk_noshow(tmp)));
    if (ticketMissCostCheck()) {
        $(this).closest('tr[name=contens]').find('p[name=ticket-other-cost]').text('0');
        alert('합계금액을 초과하였습니다');
        return false;
    } else {
        $(document).trigger('mouseup');
    }
});
// 포인트 저장
/*$(document).on('click', '.ticket-point-cost-submit', function(){
    if(!ticketPointCheck()){
        alert("소지하신 포인트를 초과하였습니다.");
        return false;
    }
    var tmp = $(this).closest('div').find('input').val();
    $(this).closest('tr[name=contens]').find('p[name=ticket-point-cost]').text(comma(uk_noshow(tmp)));
    if (ticketMissCostCheck()) {
        $(this).closest('tr[name=contens]').find('p[name=ticket-point-cost]').text('0');
        alert('합계금액을 초과하였습니다');
        return false;
    } else {
        $(document).trigger('mouseup');
    }
});

function pointCheck(){
    var this_value = parseInt($('#prepayment_point_text').val());

    if(client_point < this_value){
        return false;
    } else{
        return true;
    }
}
*/

/* 미수금 계산 */
function ticketMissCostCheck(type){
    var total_cost = parseInt(Number(uncomma($('#ticket_cost_modal').val())) * Number($('#ticket_count_modal').val()));
    var sales, pay, card, gift, other, point = 0;
    var sales_type = '';

    if ($('#ticket_sale_type').text() == '%') sales_type = 0;
    else if ($('#ticket_sale_type').text() == '원') sales_type = 1;

    $('#ticket_right_tables > tbody > tr > td').each(function(idx, value){
        if ($(this).find('p').attr('name') == 'ticket-sale-cost') {
            sales = parseInt(uncomma($(this).find('p').text()));
        }
        else if ($(this).find('p').attr('name') == 'ticket-pay-cost') {
            pay = parseInt(uncomma($(this).find('p').text()));
        }
        else if ($(this).find('p').attr('name') == 'ticket-card-cost') {
            card = parseInt(uncomma($(this).find('p').text()));
        }
        else if ($(this).find('p').attr('name') == 'ticket-gift-cost') {
            gift = parseInt(uncomma($(this).find('p').text()));
        }
        else if ($(this).find('p').attr('name') == 'ticket-other-cost') {
            other = parseInt(uncomma($(this).find('p').text()));
        }
        /*else if ($(this).find('p').attr('name') == 'ticket-point-cost') {
            point = parseInt(uncomma($(this).find('p').text()));
        }*/
    });

    if(sales_type == 0)
        sales = ((sales / 100) * total_cost);
    var miss_cost = total_cost - sales - pay - card - gift - other - point;
    var real_total_cost = total_cost - sales - point;

    if(miss_cost < 0){
        return true;
    }else{

        if($('#ticket_employee_two_cost').css("display") != "none" && type == "sale") {
            $('#ticket_total_cost_by_fee').val(miss_cost);
            $('#ticket_employee_one_cost').val(miss_cost / 2);
            $('#ticket_employee_two_cost').val(miss_cost / 2);
        }
        else if($('#prepaid_employee_two_cost').css("display") == "none" && type == "sale") {
            $('#ticket_total_cost_by_fee').val(miss_cost);
            $('#ticket_employee_one_cost').val(miss_cost);
            $('#ticket_employee_two_cost').val(0);
        }

        $('#ticket_miss_cost_text').text(comma(miss_cost));
        $('#ticket_right_total_pay_cost').text(comma(real_total_cost));
        return false;
    }
}


/* 포인트 적립 모달 오픈 */
function ticket_point_box_open(){
    if(parseInt(uk_noshow(uncomma($('#ticket_cost_modal').val()))) > 0)
        $('#ticket_modal_point').show();
    else alert("정액 / 선불권을 선택해 주세요");
}

/* 티켓 포인트 적립 */
function ticket_point_fix(unit){
    var tmp = parseInt(uk_noshow($('#ticket_add_point_text').val()));

    if(unit == 'percent'){
        var total = parseInt(uk_noshow(uncomma($('#ticket_cost_modal').val())));
        var point_cost = ((tmp / 100) * total);
        $('#ticket_point_cost_text').text(comma(point_cost));
    }
    else if (unit == 'cancel') {
        $('#ticket_point_cost_text').text('0');
        $('#ticket_add_point_text').val('0');
    }
    else if(unit == 'money') $('#ticket_point_cost_text').text(comma(tmp));

    $(document).trigger('mouseup');
}


/* 결제 */
function ticket_sale(){
    var tmp = '';
    var prepaid_sum_flag = $('#merge-chk').is(':checked');

    /* client update */
    var append_point = parseInt(uk_noshow(uncomma($('#ticket_point_cost_text').text())));

    /* sales col */
    var validity;
    /*var client_idx = client_idx;*/
    var memo = $('#ticket_memo').val();              // 태그 없음
    var total_money = uncomma($('#ticket-pay-cost').text());
    var total_card = uncomma($('#ticket-card-cost').text());
    var total_exhaust = '0';
    var total_miss_cost = uncomma($('#ticket_miss_cost_text').text());
    var total_gift_cost = uncomma($('#ticket-gift-cost').text());
    var total_other_cost = uncomma($('#ticket-other-cost').text());
    var store_point = '0';
    var col_status = '0';
    var col_type = '2';
    var use_point = uncomma($('#ticket-point-cost').text());
    /* /. sales col */

    /* sum object */
    var sum_hss_ticket_idx = '';
    var sum_hss_ticket_count = '';
    var sum_hss_ticket_name = '';
    var sum_hss_ticket_sales_idx = '';
    var sum_hss_ticket_validity = '';
    /* /.sum object */

    /* json object */
    var total_cost = parseInt(Number(uk_noshow(uncomma($('#ticket_cost_modal').val()))) * Number($("#ticket_count_modal").val()));
    var dc;
    var dc_type;
    var count;
    var services_type;
    var sales_fee;
    var status;
    var services_idx;
    var services_cost;
    var services_name;
    var services_cate_name;
    var services_cash;
    var accumulate;
    var ticket_count;
    var validity_date;
    var empl1_idx;
    var empl1_cost;
    var empl1_name;
    var empl1_point;
    var empl1_exhaust;
    var empl2_idx;
    var empl2_cost;
    var empl2_name;
    var empl2_point;
    var empl2_exhaust;
    /* /.json object */

    dc = uncomma($('#ticket-sale-cost').text());
    services_cate_name = $('#ticket_category_name').val();

    if($('#ticket_sale_type').text() == '%'){
        dc_type = '1';
        sales_fee = total_cost - ((dc / 100) * total_cost) + '';
        dc = (dc / 100) * total_cost;
    } else{
        dc_type = '0';
        sales_fee = total_cost - dc + '';
    }
    dc += '';
    count = $('#ticket_count_modal').val();
    services_type = '2';
    status = '0';
    services_idx = uk($('#ticket_idx_tmp').val());
    services_cost = uk(uncomma($('#ticket_cost_modal').val()));
    services_name = $('#ticket_name_modal').text();
    services_cash = parseInt(uncomma($('#ticket-pay-cost').text())) + parseInt(uncomma($('#ticket-card-cost').text()));
    services_cash = services_cash.toString();
    accumulate = uk($('#ticket_count_modal').val());
    ticket_count = uk($('#ticket_count_modal').val());
    validity_date = uk_noshow($('#ticket_validity_modal').val());

    empl1_idx = $('#ticket_pay_empl_list_box1 option:selected').val();
    empl1_name = $('#ticket_pay_empl_list_box1 option:selected').text();
    empl1_cost = $('#ticket_employee_one_cost').val();
    empl1_point = '0';
    empl1_exhaust = '0';

    if(empl2_flag){
        if(uk($('#ticket_pay_empl_list_box2 option:selected').val()) == ""){
            alert("두 번째 직원을 선택해 주세요");
            $('#ticket_pay_empl_list_box2').focus();
            return false;
        }
        else{
            empl2_idx = $('#ticket_pay_empl_list_box2 option:selected').val();
            empl2_name = $('#ticket_pay_empl_list_box2 option:selected').text();
            empl2_cost = $('#ticket_employee_two_cost').val();
            empl2_point = '0';
            empl2_exhaust = '0';
        }
    }

    if(validity_date == "0"){
        alert("유효기간을 입력해 주세요.");
        $('#ticket_validity_modal').focus();
        return false;
    }

    var date = new Date();
    var year = date.getFullYear();
    var month = date.getMonth()+1;
    var day = date.getDate();

    if(Number(month) + Number(validity_date) > 12){
        year = Math.floor(year + ((Number(month) + Number(validity_date)) / 12));
        month = Math.floor(Number(month) + Number(validity_date) % 12);
    } else{
        month = Number(month) + Number(validity_date);
    }
    if(Number(month) < 10){
        month = "0" + month;
    }
    // if(Number(month) > 12){
    //     month = 12;
    // }

    var tmp = new Date();
    tmp.setMonth(tmp.getMonth() + Number(validity_date));

    var y = tmp.getFullYear();
    var m = (tmp.getMonth() + 1);
    var d = tmp.getDate();

    if(Number(m) < 10) m = "0" + m;
    if(Number(d) < 10) d = "0" + d;
    // validity = year + "-" + returnMinuteTypeNumber(month) + "-" + returnMinuteTypeNumber(day);
    validity = y + "-" + m + "-" + d;

    tmp = {
        dc : dc,
        // dc_type : dc_type,
        count : count,
        // services_type : services_type,
        sales_fee : sales_fee,
        // status : status,
        services_idx : services_idx,
        services_cost : services_cost,
        services_name : services_name,
        services_category : services_cate_name,
        // services_cash : services_cash,
        // accumulate : accumulate,
        // validity : validity,
        empl1_idx : empl1_idx,
        empl1_name : empl1_name,
        empl1_cost : empl1_cost,
        // empl1_point : empl1_point,
        empl1_exhaust :empl1_exhaust,
        empl2_idx : empl2_idx,
        empl2_name : empl2_name,
        empl2_cost : empl2_cost,
        // empl2_point : empl2_point,
        empl2_exhaust : empl2_exhaust
    };

    if(empl1_idx == ''){
        alert("직원을 선택해 주세요");
        return false;
    }
    if(services_idx == ''){
        alert('횟수권을 선택해 주세요');
        return false;
    }
    if(parseInt(total_miss_cost) > 0){
        var con_test = confirm("미수금이 발생합니다. 계속 하시겠습니까?");
        if (con_test == false) {
            return false;
        }
    }
    if(prepaid_sum_flag){
        sum_hss_ticket_idx = $("#sum_has_ticket_idx").val();
        sum_hss_ticket_count = $("#sum_has_ticket_count").val();
        sum_hss_ticket_name = $("#sum_has_ticket_name").val();
        sum_hss_ticket_sales_idx = $("#sum_has_ticket_sales_idx").val();
        sum_hss_ticket_validity = $("#sum_has_ticket_validity").val();

        if(sum_hss_ticket_idx == "" || sum_hss_ticket_count == "" || sum_hss_ticket_name == "" || sum_hss_ticket_sales_idx == "" || sum_hss_ticket_validity == "") {
            alert("합칠 횟수권을 선택해 주세요.");
            return false;
        }
    }
    if (empl2_idx == '') {
        delete tmp.empl2_idx;
        delete tmp.empl2_name;
        delete tmp.empl2_point;
        delete tmp.empl2_exhaust;
    }

    var list = [];
    list.push(tmp);
    var data = JSON.stringify(list);

    + "-" + Number(date.getMonth() + 1) + "-" + date.getDate();
    var membership_date = date.getFullYear() + "-";
    if(Number(date.getMonth() + 1) < 10) membership_date += "0" + Number(date.getMonth() + 1);
    else membership_date += Number(date.getMonth() + 1);
    membership_date += "-" + date.getDate();
    var ticket_one_cost = services_cost / accumulate;

    $.ajax({
        url: "/Sales/ticketSalesInsert",
        type: "post",
        dataType: "json",
        data: {
            "resource": data,
            "memo": memo,
            "total_cost": sales_fee,
            "total_money": total_money,
            "total_card": total_card,
            "total_exhaust": total_exhaust,
            "total_miss_cost": total_miss_cost,
            "total_gift_cost": total_gift_cost,
            "total_other_cost": total_other_cost,
            "store_point": store_point,
            "col_status": col_status,
            "col_type": col_type,
            "client_idx" : client_idx,
            "validity" : validity,
            "services_idx" : services_idx,
            "accumulate" : uncomma(accumulate),
            "services_name" : services_name,
            "prepaid_sum_flag" : prepaid_sum_flag,
            "sum_hss_ticket_idx" : sum_hss_ticket_idx,
            "sum_hss_ticket_count" : sum_hss_ticket_count,
            "sum_hss_ticket_name" : sum_hss_ticket_name,
            "sum_hss_ticket_sales_idx" : sum_hss_ticket_sales_idx,
            "sum_hss_ticket_validity" : sum_hss_ticket_validity,
            "membership_flag" : membership_flag,
            "now_date" : membership_date,
            "append_point" : append_point,
            // "use_point" : use_point
            "use_point" : 0
        },
        success: function (data) {
            if (data.code == 200) {
                var client_info_map = callPrepaidAndPointCost();

                ticketSalesAfterPrepaidMake();
                ticketSalesAfterTicketMake();
                $('div[name=product-prepaid-body]').empty();
                $('div[name=product-ticket-body]').empty();
                productPrepaidMake();
                productTicketMake();

                $('.sale-main-table > tbody > tr').each(function (index, value) {
                    $(value).find('td[name=ticket]').find('div[name=ticket-info]').empty();
                    $(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val('0');
                });
                $('.product-sale-main-table > tbody > tr').each(function (index, value) {
                    $(value).find('td[name=ticket]').find('div[name=ticket-info]').empty();
                    $(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val('0');
                });


                if(client_info_map != 900){
                    $('#has_prepaid_cost').text(comma(client_info_map.prepaid));
                    $('#has_point_cost').text(comma(client_info_map.point));
                    $('#has_miss_cost').text(comma(client_info_map.miss_cost));
                }

                alert("횟수권 결제가 완료되었습니다.");
                $('.modal.in').modal('hide');
            } else {
                alert("잠시 후 다시 시도해 주세요");
                return false;
            }
        },
        error: function () {
            alert("에러가 발생했습니다.");
            location.href = document.URL;
        }
    });
}

function ticketSalesAfterPrepaidMake(){
    //console.log("ticketSalesAfterPrepaidMake");
    //console.log('prepaidMake');
    $('div[name=prepaid-body]').empty();
    var tmp = '';
    var now = dateNOW();
    $.each(pre_map, function(index, value){
        if(value.cost > 0){
            tmp += '<button type="button" onclick="prepaidButtonsClicked(this)" class="ticket-buttons">' +
                '<input type="hidden" name="idx" value="' + value.idx + '"/> ' +
                '<input type="hidden" name="name" value="' + value.name + '"/> ' +
                '<input type="hidden" name="cost" value="' + value.cost + '"/> ' +
                '<input type="hidden" name="vali" value="' + value.validity + '"/> ' +
                '<input type="hidden" name="sal_idx" value="' + value.sale_idx + '"/> ' +
                '<input type="hidden" name="use-cost" value="0"/> ' +
                value.name +
                '</button>';
        }
    });
    $('div[name=prepaid-body]').append(tmp);
}
function ticketSalesAfterTicketMake(){
    //console.log("ticketSalesAfterTicketMake");
    var tmp = '';
    $('div[name=ticket-body]').empty();
    $.each(ticket_map, function(index, value){
        //console.log("ticket count : " + value.count);
        if(value.count > 0){
            tmp += '<button type="button" onclick="ticketButtonsClicked(this)" class="ticket-buttons">' +
                '<input type="hidden" name="idx" value="' + value.idx + '"/> ' +
                '<input type="hidden" name="name" value="' + value.name + '"/> ' +
                '<input type="hidden" name="count" value="' + value.count + '"/> ' +
                '<input type="hidden" name="vali" value="' + value.validity + '"/> ' +
                '<input type="hidden" name="sal_idx" value="' + value.sale_idx + '"/> ' +
                '<input type="hidden" name="use-count" value="0"/> ' +
                value.name +
                '</button>';
        }
    });
    $('div[name=ticket-body]').append(tmp);
}