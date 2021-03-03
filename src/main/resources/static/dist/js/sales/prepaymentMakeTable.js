/* prepaymentMakeTable*/
function makePrepaymentTable(data, idx, name){
    $('#prepayment_cate_box').empty();
    $('#prepayment_detail_box').empty();
    $('#payment_cost').val('');
    $('#payment_acc_cost').val('');
    $('#payment_validity').val('');
    $('#tmp_name').text('');
    $("#chk08").prop('checked', false);
    $('#idx_tmp').val('');

    $('p[name=pay-cost]').text('0');
    $('p[name=card-cost]').text('0');
    $('p[name=gift-cost]').text('0');
    $('p[name=other-cost]').text('0');

    $('#prepayment_pay_empl_list_box1').empty();
    $('#prepayment_pay_empl_list_box1').append('<option value="">선택</option>');
    $('#prepayment_pay_empl_list_box2').empty();
    $('#prepayment_pay_empl_list_box2').append('<option value="">선택</option>');
    var tmp1 = '';
    var tmp2 = '';
    $.each(data.emplList, function(index, value){
        if(idx == value.idx && name == value.name){
            tmp1 = tmp1 + '<option value="' + value.idx + '" selected>' + value.name + ' </option>';
        } else{
            tmp1 = tmp1 + '<option value="' + value.idx + '">' + value.name + ' </option>';
        }
        tmp2 = tmp2 + '<option value="' + value.idx + '">' + value.name + ' </option>';
    });

    $('#prepayment_pay_empl_list_box1').append(tmp1);
    $('#prepayment_pay_empl_list_box2').append(tmp2);
    $('select').niceSelect('update');

    prepayment_cate = data.cateList;
    prepayment_detail = data.detailList;

    // prepaymentMakeCate();
    prepayMentMakeDetail();
}

/* 카테고리 그리기 */
// function prepaymentMakeCate(){
//     var tmp = '';
//     for(var i = 0 ; i < prepayment_cate.length; i ++){
//         tmp = tmp + '<li class="pre_cate" data-value="' + prepayment_cate[i].idx + '">' + prepayment_cate[i].category + '</li>';
//     }
//
//     $('#prepayment_cate_box').append(tmp);
// }

/* 디테일 그리기 */
function prepayMentMakeDetail(cate){
    $('#prepayment_detail_box').empty();
    var tmp = '';

    for(var i = 0; i < prepayment_detail.length; i++){
        // if(prepayment_detail[i].category == cate)
        tmp = tmp + '<li class="pre_detail" data-value="' + prepayment_detail[i].idx + '">' +
            prepayment_detail[i].name +
            '<input type="hidden" name="cost" value="' + prepayment_detail[i].cost + '">'+
            '<input type="hidden" name="acc_cost" value="' + prepayment_detail[i].acc_cost + '">'+
            '<input type="hidden" name="validity" value="' + prepayment_detail[i].validity + '">'+
            '</li>';
    }

    $('#prepayment_detail_box').append(tmp);

}

/* 카테고리 클릭 */
$(document).on('click', '.pre_cate', function(){
    var cate_name = $(this).text();
    $('#pre_category_name').val(cate_name);
    prepayMentMakeDetail(cate_name)
});

/* 디테일 클릭 */
$(document).on('click', '.pre_detail', function(){
    var idx = $(this).attr('data-value');
    var cost = uncomma($(this).find('input[name="cost"]').val());
    var acc_cost = $(this).find('input[name="acc_cost"]').val();
    var validity = $(this).find('input[name="validity"]').val();

    $('#idx_tmp').val(idx);
    $('#payment_cost').val(comma(cost));
    $('#payment_acc_cost').val(comma(acc_cost));
    $('#acc_cost_back_up').val(acc_cost);
    $('#payment_validity').val(validity);
    $('#tmp_name').text($(this).text());

    $('#right_sum_cost').text(comma(cost));
    $('#right_total_pay_cost').text(comma(cost));
    $('#miss_cost_text').text(comma(cost));
    $('#prepaid_employee_one_cost').val(cost);


    $('#sale_pay_text').val('0');
    $('#sales-cost').text('0');
    $('#sale_type').text('%');
    $('#pay-cost').text('0');
    $('#card-cost').text('0');
    $('#gift-cost').text('0');
    $('#other-cost').text('0');

    $(".sale-btn").show();
    $(".money-btn").show();
    $(".card-btn").show();
    $(".gift-btn").show();
    $(".other-btn").show();
    $(".prepaid_pay_btn1").show();
    $(".prepaid_pay_btn2").show();
    // $(".point-btn").show();



    $('#total_cost_by_fee').val(cost);
    // $('#prepayment_pay_empl_list_box2').closest('div').find('.nice-select').hide();
    // $('#prepaid_employee_two_cost').val('0');
    // $('#prepaid_employee_two_cost').hide();
    $('#employeePlus').show();
    $('#employeeMinus').hide();
});

/* 하단 적립금액 수정 */
/*$(document).on('keyup','#payment_acc_cost',function(){
    var tmp = $(this).val();
    if(uk(tmp) == "") tmp = 0;
    $('#right_sum_cost').text(comma(tmp));
});*/

/* 결제방법 - 선불권 출력 */
function makeClientPrepaidList(boolean){
    $.ajax({
        url: "/Sales/callClientPrepaidList",
        type: "post",
        dataType: "json",
        data: {"client_idx" : client_idx},
        async: false,
        success: function (data) {
            if (data.code == 200) {
                if(boolean)
                    makeClientPrepaidTable(data.pMap);
                else
                    pMapReady.prepaid = data.pMap;
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

/* 선불권 사용 스크립트 */
function makeClientPrepaidTable(pMap){
    var json_list = new Array();
    var tmp1 = '';
    var tmp2 = '';

    json_list = jQuery.parseJSON(pMap.prepaid);
    console.log("click Object :  " + json_list);
    client_has_prepaid = json_list;
    $.each(json_list, function(index, value){
        tmp1 = tmp1 + '<tr data-value="' + value.idx + '" class="make_client_prepaid_talbe_tr">' +
            '<td><input type="radio" name="prepaid_sum_check1"></td>' +
            '<td><p name="name">' + value.name + '</p></td>' +
            '<td><p name="cost">' + value.cost + '</p></td>' +
            '<td><p name="validity">' + value.validity + '</p></td>' +
            '</tr>';
        tmp2 = tmp2 + '<tr data-value="' + value.idx + '" class="make_client_prepaid_talbe_tr">' +
            '<td><input type="radio" name="prepaid_sum_check2"></td>' +
            '<td><p name="name">' + value.name + '</p></td>' +
            '<td><p name="cost">' + value.cost + '</p></td>' +
            '<td><p name="validity">' + value.validity + '</p></td>' +
            '</tr>';
    });

    $('#prepaid_check_tbodt-content1').empty();
    $('#prepaid_check_tbodt-content1').append(tmp1);
    $('#prepaid_check_tbodt-content2').empty();
    $('#prepaid_check_tbodt-content2').append(tmp2);
}
$(document).on('click', '.make_client_prepaid_talbe_tr', function(){
    $(this).find('input').prop('checked', true);

    var idx = $(this).attr('data-value');
    var name = $(this).find('p[name=name]').text();
    var cost = $(this).find('p[name=cost]').text();
    var validity = $(this).find('p[name=validity]').text();

    $(this).closest('div').find('input[name=prepaid_pay_text]').val(cost);
});

/* 선불권 합치기 */
$('#merge-chk1').click(function(){
    if(sum_prepaid_falg){
        $.ajax({
            url: "/Sales/callClientPrepaidList",
            type: "post",
            dataType: "json",
            data: {"client_idx": client_idx},
            success: function (data) {
                if (data.code == 200) {
                    makeClientPrepaidSumTable(data.pMap);
                    sum_prepaid_falg = false;
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

/* 선불권 합치기 테이블 그리기 */
function makeClientPrepaidSumTable(pMap){
    var json_list = new Array();
    var tmp = '<option value="0">선택</option>';

    json_list = jQuery.parseJSON(pMap.prepaid);
    prepayment_hass_client = jQuery.parseJSON(pMap.prepaid);
    /*$.each(json_list, function(index, value){
        tmp = tmp + '<option style="display: none;" value="'+
            value.idx + '/' +
            value.cost + '/' +
            value.name + '/' +
            value.sale_idx + '/' +
            value.validity +
            '">' + value.name + '</option>';
    });*/
    $.each(json_list, function(index, value){
        tmp = tmp + '<option style="display: none;" value="'+
            index +
            '">' + value.name + '</option>';
    });

    $('#prepaid_sum_select_tag').empty();
    $('#prepaid_sum_select_tag').append(tmp);
    $('#prepaid_sum_select_tag').niceSelect('update');
    // $('#prepayment_pay_empl_list_box2').closest('div').find('.nice-select').hide();
}
/* 선불권 합치기 셀렉트 변경 */
$('#prepaid_sum_select_tag').change(function(){
    var tmp = $(this).val();
    var splits = new Array();

    /*if(tmp == "0"){
        splits[0] = '';
        splits[1] = '';
        splits[2] = '';
        splits[3] = '';
        splits[4] = '';
    }else{
        splits = tmp.split("/");
    }

    $('#sum_hss_prepaid_idx').val(splits[0]);
    $('#sum_hss_prepaid_cost').val(splits[1]);
    $('#sum_hss_prepaid_name').val(splits[2]);
    $('#sum_hss_prepaid_sales_idx').val(splits[3]);
    $('#sum_hss_prepaid_validity').val(splits[4]);

    $('#prepaid_sum_cost').text(splits[1]);
    $('#prepaid_sum_validity').text(splits[4]);*/


    $('#sum_hss_prepaid_idx').val(prepayment_hass_client[tmp].idx);
    $('#sum_hss_prepaid_cost').val(prepayment_hass_client[tmp].cost);
    $('#sum_hss_prepaid_name').val(prepayment_hass_client[tmp].name);
    $('#sum_hss_prepaid_sales_idx').val(prepayment_hass_client[tmp].sale_idx);
    $('#sum_hss_prepaid_validity').val(prepayment_hass_client[tmp].validity);

    $('#prepaid_sum_cost').text(comma(prepayment_hass_client[tmp].cost));
    $('#prepaid_sum_validity').text(prepayment_hass_client[tmp].validity);
});


/* 금액 입력 '선택'버튼 클릭 */
function choiceButtonClick(tagId, obj){
    var tmp = '';

    if(tagId == "#prepayment_pay_text" && uncomma($(obj).closest('div').find('p[name=pay-cost]').text()) != "0") tmp = uncomma($(obj).closest('div').find('p[name=pay-cost]').text());
    else if(tagId == "#prepayment_pay_text") tmp = uncomma($('#miss_cost_text').text());

    if(tagId == "#prepayment_card_text" && uncomma($(obj).closest('div').find('p[name=card-cost]').text()) != "0") tmp = uncomma($(obj).closest('div').find('p[name=card-cost]').text());
    else if(tagId == "#prepayment_card_text") tmp = uncomma($('#miss_cost_text').text());

    if (tagId == "#prepayment_gift_text" && $(obj).closest('div').find('p[name=gift-cost]').text() != "0") tmp = uncomma($(obj).closest('div').find('p[name=gift-cost]').text());
    else if (tagId == "#prepayment_gift_text") tmp = uncomma($('#miss_cost_text').text());

    if (tagId == "#prepayment_other_text" && uncomma($(obj).closest('div').find('p[name=other-cost]').text()) != "0") tmp = uncomma($(obj).closest('div').find('p[name=other-cost]').text());
    else if (tagId == "#prepayment_other_text") tmp = uncomma($('#miss_cost_text').text());

    /*if (tagId == "#prepayment_point_text" && uncomma($(obj).closest('div').find('p[name=point-cost]').text()) != "0") tmp = uncomma($(obj).closest('div').find('p[name=point-cost]').text());
    else if (tagId == "#prepayment_point_text"){
        tmp = uncomma($('#miss_cost_text').text());
        if(client_point < tmp)
            tmp = client_point;
    }*/
    $(tagId).val(tmp);
}

/* 할인 퍼센트 */
$('.percent-btns').click(function(){

    $('#sale_pay_text').text('0');
    $('#pay-cost').text('0');
    $('#card-cost').text('0');
    $('#gift-cost').text('0');
    $('#other-cost').text('0');

    var tmp = parseInt($(this).closest('div').find('input').val());
    if(tmp > 100){
        alert("할인율 100% 초과");
        return false;
    } else if(uk_noshow($(this).closest('div').find('input').val()) == 0){
        tmp = 0;
    }

    $(this).closest('tr[name=contens]').find('p[name=sale-cost]').text(tmp);
    $('#sale_type').text('%');
    $(".modal-content-close").trigger('click');
    missCostCheck("sale");
});
/* 할인 원 */
$('.won-btns').click(function(){

    $('#sale_pay_text').text('0');
    $('#pay-cost').text('0');
    $('#card-cost').text('0');
    $('#gift-cost').text('0');
    $('#other-cost').text('0');

    var pay_cost = parseInt(uncomma($('#right_sum_cost').text()));
    var tmp = parseInt($(this).closest('div').find('input').val());
    if(uk_noshow($(this).closest('div').find('input').val()) == 0){
        tmp = 0;
    } else if(pay_cost < tmp){
        tmp = pay_cost;
        $(this).closest('div').find('input').val(tmp);
    }

    $(this).closest('tr[name=contens]').find('p[name=sale-cost]').text(comma(tmp));
    $('#sale_type').text('원');
    $(".modal-content-close").trigger('click');
    missCostCheck("sale");
});

/* 미수금 계산 */
function missCostCheck(type){
    // //console.log('미수금 계산 .. ');
    var total_cost = parseInt(uncomma($('#right_sum_cost').text()));
    var sales, pay, card, gift, other, point = 0;
    var sales_type = '';

    if ($('#sale_type').text() == '%') sales_type = 0;
    else if ($('#sale_type').text() == '원') sales_type = 1;

    $('#right_tables > tbody > tr > td').each(function(idx, value){
        if ($(this).find('p').attr('name') == 'sale-cost') {
            sales = parseInt(uncomma($(this).find('p').text()));
        }
        else if ($(this).find('p').attr('name') == 'pay-cost') {
            pay = parseInt(uncomma($(this).find('p').text()));
        }
        else if ($(this).find('p').attr('name') == 'card-cost') {
            card = parseInt(uncomma($(this).find('p').text()));
        }
        else if ($(this).find('p').attr('name') == 'gift-cost') {
            gift = parseInt(uncomma($(this).find('p').text()));
        }
        else if ($(this).find('p').attr('name') == 'other-cost') {
            other = parseInt(uncomma($(this).find('p').text()));
        }
        /*else if ($(this).find('p').attr('name') == 'point-cost') {
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


        if($('#prepaid_employee_two_cost').css("display") != "none" && type == "sale") {
            $('#total_cost_by_fee').val(miss_cost);
            $('#prepaid_employee_one_cost').val(miss_cost / 2);
            $('#prepaid_employee_two_cost').val(miss_cost / 2);
        }
        else if($('#prepaid_employee_two_cost').css("display") == "none" && type == "sale") {
            $('#total_cost_by_fee').val(miss_cost);
            $('#prepaid_employee_one_cost').val(miss_cost);
            $('#prepaid_employee_two_cost').val(0);
        }


        $('#miss_cost_text').text(comma(miss_cost));
        $('#right_total_pay_cost').text(comma(real_total_cost));
        return false;
    }
}

/* prepaid point box open */
function prepaid_point_box_open(){
    if(parseInt(uk_noshow(uncomma($('#right_total_pay_cost').text()))) > 0)
        $('#prepaid_modal_point').show();
    else alert("정액 / 선불권을 선택해 주세요");
}
function prepaid_point_fix(unit){
    var tmp = parseInt(uk_noshow($('#prepaid_point_text').val()));

    if(unit == 'percent'){
        var total = parseInt(uk_noshow(uncomma($('#payment_cost').val())));
        var point_cost = ((tmp / 100) * total);
        $('#prepaid_point_cost_text').text(comma(point_cost));
    }
    else if (unit == 'cancel') {
        $('#prepaid_point_cost_text').text('0');
        $('#prepaid_point_text').val('0');
    }
    else if(unit == 'money') $('#prepaid_point_cost_text').text(comma(tmp));

    $(document).trigger('mouseup');
}

/* 포인트 오버 제한 */
/*$('.point-btns').click(function(){
    maxValueCheck(this, 0);
});*/

/* 직원 2명 금액 나누기 */
$('#prepaid_employee_one_cost, #prepaid_employee_two_cost').keyup(function(){
    if(Number($(this).val()) > Number(uk_noshow(uncomma($('#total_cost_by_fee').val()))))
        $(this).val(uk_noshow(uncomma($('#total_cost_by_fee').val())));
    if(empl2_flag){
        var empl1_val, empl2_val;
        if($(this).attr('id') == 'prepaid_employee_one_cost'){
            empl1_val = uk_noshow($(this).val());
            if(empl1_val.indexOf('0') == 0) empl1_val = empl1_val.substring(1, empl1_val.length);
            empl2_val = Number(uk_noshow(uncomma($('#total_cost_by_fee').val())) - empl1_val);
        }
        else{
            empl2_val = uk_noshow($(this).val());
            if(empl2_val.indexOf('0') == 0) empl2_val = empl2_val.substring(1, empl2_val.length);
            empl1_val = Number(uk_noshow(uncomma($('#total_cost_by_fee').val())) - empl2_val);
        }
        $('#prepaid_employee_one_cost').val(empl1_val);
        $('#prepaid_employee_two_cost').val(empl2_val);
    }
});
$('#prepaid_employee_one_cost, #prepaid_employee_two_cost').focusout(function(){
    if(uk($(this).val()) == "") $(this).val('0');
    $(this).trigger('keyup');
});



/*function maxValueCheck(obj, flag){
    var this_value;

    if(flag < 1) this_value = parseInt(uk_noshow($('#prepayment_point_text').val() + $(obj).find('p').text()));
    else this_value = parseInt(uk_noshow($(obj).val()));

    $('#prepayment_point_text').val(client_point);
}*/

/* 계산기 모달 동작 */
// 입력
$(document).on('click', '.pay-btns', function(){
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


// 현금 저장
$(document).on('click', '.pay-cost-submit', function(){
    var tmp = $(this).closest('div').find('input').val();
    $(this).closest('tr[name=contens]').find('p[name=pay-cost]').text(comma(uk_noshow(tmp)));
    if (missCostCheck()) {
        $(this).closest('tr[name=contens]').find('p[name=pay-cost]').text('0');
        alert('합계금액을 초과하였습니다');
        $(this).closest('div').find('input').val('0');
        return false;
    } else {
        $(".modal-content-close").trigger('click');
    }
});
// 카드 저장
$(document).on('click', '.card-cost-submit', function(){
    var tmp = $(this).closest('div').find('input').val();
    $(this).closest('tr[name=contens]').find('p[name=card-cost]').text(comma(uk_noshow(tmp)));
    if (missCostCheck()) {
        $(this).closest('tr[name=contens]').find('p[name=card-cost]').text('0');
        alert('합계금액을 초과하였습니다');
        $(this).closest('div').find('input').val('0');
        return false;
    } else {
        $(".modal-content-close").trigger('click');
    }
});
// 상품권 저장
$(document).on('click', '.gift-cost-submit', function(){
    var tmp = $(this).closest('div').find('input').val();
    $(this).closest('tr[name=contens]').find('p[name=gift-cost]').text(comma(uk_noshow(tmp)));
    if (missCostCheck()) {
        $(this).closest('tr[name=contens]').find('p[name=gift-cost]').text('0');
        alert('합계금액을 초과하였습니다');
        $(this).closest('div').find('input').val('0');
        return false;
    } else {
        $(".modal-content-close").trigger('click');
    }
});
// 기타 저장
$(document).on('click', '.other-cost-submit', function(){
    var tmp = $(this).closest('div').find('input').val();
    $(this).closest('tr[name=contens]').find('p[name=other-cost]').text(comma(uk_noshow(tmp)));
    if (missCostCheck()) {
        $(this).closest('tr[name=contens]').find('p[name=other-cost]').text('0');
        alert('합계금액을 초과하였습니다');
        $(this).closest('div').find('input').val('0');
        return false;
    } else {
        $(document).trigger('mouseup');
    }
});
// 포인트 저장
/*$(document).on('click', '.point-cost-submit', function(){
    if(!pointCheck()){
        alert("소지하신 포인트를 초과하였습니다.");
        $(this).closest('div').find('input').val('0');
        return false;
    }
    var tmp = $(this).closest('div').find('input').val();
    $(this).closest('tr[name=contens]').find('p[name=point-cost]').text(comma(uk_noshow(tmp)));
    if (missCostCheck()) {
        $(this).closest('tr[name=contens]').find('p[name=point-cost]').text('0');
        alert('합계금액을 초과하였습니다');
        $(this).closest('div').find('input').val('0');
        return false;
    } else {
        $(document).trigger('mouseup');
    }
});*/

/*function pointCheck(){
    var this_value = parseInt($('#prepayment_point_text').val());

    if(client_point < this_value){
        return false;
    } else{
        return true;
    }
}*/

/* 직원 추가 */
function plusEmployee(){
    empl2_flag = true;
    var payment_cost = uk_noshow(uncomma($('#total_cost_by_fee').val()));
    var cost1 = payment_cost / 2;
    var cost2 = payment_cost / 2;

    $('#employeePlus').hide();
    $('#prepayment_pay_empl_list_box2').closest('div').find('.nice-select').show();
    $('#employeeMinus').show();
    $('#prepaid_div2').show();

    $('#prepaid_employee_one_cost').val(cost1);
    $('#prepaid_employee_two_cost').val(cost2);
    $('#prepaid_employee_two_cost').show();
}
/*직원 hide*/
function minusEmployee(){
    empl2_flag = false;
    var payment_cost = parseInt(uk_noshow(uncomma($('#total_cost_by_fee').val())));

    $('#employeePlus').show();
    $('#prepayment_pay_empl_list_box2').closest('div').find('.nice-select').hide();
    $('#employeeMinus').hide();
    $('#prepaid_div2').hide();


    $('#prepaid_employee_one_cost').val(payment_cost);
    $('#prepaid_employee_two_cost').val('0');
    $('#prepaid_employee_two_cost').hide();
}

/* 선불권 결제 */
function prepayment_sale(){
    var tmp = '';
    var prepaid_sum_flag = $('#merge-chk1').is(':checked');

    /* client update */
    var append_point = parseInt(uk_noshow(uncomma($('#prepaid_point_cost_text').text())));

    /* sales col */
    var validity;
    /*var client_idx = client_idx;*/
    var memo = $('#prepaid_memo').val();              // 태그 없음
    var col_total_cost = uncomma($('#payment_cost').val());
    var total_money = uncomma($('#pay-cost').text());
    var total_card = uncomma($('#card-cost').text());
    var total_exhaust = '0';
    var total_miss_cost = uncomma($('#miss_cost_text').text());
    var total_gift_cost = uncomma($('#gift-cost').text());
    var total_other_cost = uncomma($('#other-cost').text());
    var store_point = '0';
    var col_status = '0';
    var col_type = '1';
    var use_point = uncomma($('#point-cost').text());
    /* /. sales col */

    /* sum object */
    var sum_hss_prepaid_idx = '';
    var sum_hss_prepaid_cost = '';
    var sum_hss_prepaid_name = '';
    var sum_hss_prepaid_sales_idx = '';
    var sum_hss_prepaid_validity = '';
    /* /.sum object */

    /* json object */
    var total_cost = parseInt(uk_noshow(uncomma($('#payment_cost').val())));
    var dc;
    var dc_type;
    var count;
    var services_type;
    var sales_fee;
    var status;
    var services_idx;
    var services_cost;
    var services_cate_name;
    var services_name;
    var services_cash;
    var accumulate;
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

    services_cate_name = $('#pre_category_name').val();
    dc = uncomma($('#sales-cost').text());

    if($('#sale_type').text() == '%'){
        dc_type = '1';
        sales_fee = total_cost - ((dc / 100) * total_cost) + '';
        dc = (dc / 100) * total_cost;
    } else{
        dc_type = '0';
        sales_fee = total_cost - dc + '';
    }
    dc += '';
    count = '1';
    services_type = '1';
    status = '0';
    services_idx = uk($('#idx_tmp').val());
    services_cost = uk(uncomma($('#payment_cost').val()));
    services_name = $('#tmp_name').text();
    services_cash = parseInt(uncomma($('#pay-cost').text())) + parseInt(uncomma($('#card-cost').text()));
    services_cash = services_cash.toString();
    accumulate = uk($('#payment_acc_cost').val());
    validity_date = uk_noshow($('#payment_validity').val());

    empl1_idx = $('#prepayment_pay_empl_list_box1 option:selected').val();
    empl1_name = $('#prepayment_pay_empl_list_box1 option:selected').text();
    empl1_cost = $('#prepaid_employee_one_cost').val();
    empl1_point = '0';
    empl1_exhaust = '0';

    if(empl2_flag){
        if(uk($('#prepayment_pay_empl_list_box2 option:selected').val()) == ""){
            alert("두 번째 직원을 선택해 주세요");
            $('#prepayment_pay_empl_list_box2').focus();
            return false;
        }
        else{
            empl2_idx = $('#prepayment_pay_empl_list_box2 option:selected').val();
            empl2_name = $('#prepayment_pay_empl_list_box2 option:selected').text();
            empl2_cost = $('#prepaid_employee_two_cost').val();
            empl2_point = '0';
            empl2_exhaust = '0';
        }
    }

    if(validity_date == "0"){
        alert("유효기간을 입력해 주세요.");
        $('#payment_validity').focus();
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

    var _month = Number(returnMinuteTypeNumber(month));
    if(_month > 12) _month = 12;

    var _date = new Date(year, _month, returnMinuteTypeNumber(day));
    // console.log(date.getFullYear() + "-" + ("0"+(date.getMonth()+1)).slice(-2) + "-" + ("0" + date.getDate()).slice(-2));
    // validity = year + "-" + _month + "-" + returnMinuteTypeNumber(day);
    validity = date.getFullYear() + "-" + ("0"+(date.getMonth()+1)).slice(-2) + "-" + ("0" + date.getDate()).slice(-2);

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
        alert('회원권을 선택해 주세요');
        return false;
    }
    if(parseInt(total_miss_cost) > 0){
        var con_test = confirm("미수금이 발생합니다. 계속 하시겠습니까?");
        if (con_test == false) {
            return false;
        }
    }
    if(prepaid_sum_flag){
        sum_hss_prepaid_idx = $("#sum_hss_prepaid_idx").val();
        sum_hss_prepaid_cost = $("#sum_hss_prepaid_cost").val();
        sum_hss_prepaid_name = $("#sum_hss_prepaid_name").val();
        sum_hss_prepaid_sales_idx = $("#sum_hss_prepaid_sales_idx").val();
        sum_hss_prepaid_validity = $("#sum_hss_prepaid_validity").val();

        //console.log(sum_hss_prepaid_idx);
        //console.log(sum_hss_prepaid_cost);
        //console.log(sum_hss_prepaid_name);
        //console.log(sum_hss_prepaid_sales_idx);
        //console.log(sum_hss_prepaid_validity);

        if(sum_hss_prepaid_idx == "" || sum_hss_prepaid_cost == "" || sum_hss_prepaid_name == "" || sum_hss_prepaid_sales_idx == "" || sum_hss_prepaid_validity == "") {
            alert("합칠 선불권을 선택해 주세요.");
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

    $.ajax({
        url: "/Sales/prepaidSalesInsert",
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
            "sum_hss_prepaid_idx" : sum_hss_prepaid_idx,
            "sum_hss_prepaid_cost" : sum_hss_prepaid_cost,
            "sum_hss_prepaid_name" : sum_hss_prepaid_name,
            "sum_hss_prepaid_sales_idx" : sum_hss_prepaid_sales_idx,
            "sum_hss_prepaid_validity" : sum_hss_prepaid_validity,
            "membership_flag" : membership_flag,
            "now_date" : membership_date,
            "append_point" : append_point,
            // "use_point" : use_point
            "use_point" : 0
        },
        async : false,
        success: function (data) {
            if (data.code == 200) {
                var client_info_map = callPrepaidAndPointCost();

                prepaidSalesAfterPrepaidMake();
                prepaidSalesAfterTicketMake();
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
                alert("선불권 결제가 완료되었습니다.");
                $('.modal.in').modal('hide');
            } else {
                alert("잠시 후 다시 시도해 주세요");
                return false;
            }
        },
        error: function (request,status,error) {
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
            alert("에러가 발생했습니다.");
            location.href = document.URL;
        }
    });
}

function callPrepaidAndPointCost(){
    //console.log("callPrepaidAndPointCost start ... ");
    var return_value;
    $.ajax({
        url: "/Client/callPrepaidAndPointCost",
        type: "post",
        dataType: "json",
        data: {
            "client_idx" : client_idx
        },
        async : false,
        success: function (data) {
            if (data.code == 200) {
                return_value = data.clientMap;
                pre_map = jQuery.parseJSON(data.clientMap.prepaidMap);
                ticket_map = jQuery.parseJSON(data.clientMap.ticketMap);
            } else {
                return_value = 900;
            }
        },
        error: function () {
            alert("에러가 발생했습니다.");
            location.href = document.URL;
        }
    });
    return return_value;
}

function prepaidSalesAfterPrepaidMake(){
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

function prepaidSalesAfterTicketMake(){
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