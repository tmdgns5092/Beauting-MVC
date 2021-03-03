/* 결제 결과 출력 js */
function calculationFunction(){
    var total_pay_cost = 0; // 선불권, 횟수권이 적용된 최종 결제 금액
    var total_cost = 0;     // 금액 합계 (할인이 적용된 금액)
    $('table.sale-main-table > tbody > tr').each(function(index, value){
        var one_cost = parseInt(uk_noshow($(value).find('td[name=one_cost]').text()));
        var count = parseInt(uk_noshow($(value).find('td[name=count]').text()));
        var dc_cost = parseInt(uk_noshow($(value).find('td[name=dc]').find('input[name=dc]').val()));
        var cost_text = parseInt(uk_noshow($(value).find('td[name=cost]').find('input[name=cost_text]').val()));
        var exhaust_cost = parseInt(uk_noshow($(value).find('td[name=cost]').find('input[name=exhaust_cost]').val()));

        total_cost += cost_text + exhaust_cost;
        //     total_cost += (one_cost * count) - dc_cost;
        total_pay_cost += parseInt(uk_noshow($(value).find('td[name=cost]').find('input[name=cost_text]').val()));
    });

    var money = parseInt(uk_noshow(uncomma($('#sale-main-total-money').text())));
    var card = parseInt(uk_noshow(uncomma($('#sale-main-total-card').text())));
    var gift = parseInt(uk_noshow(uncomma($('#sale-main-total-gift').text())));
    var other = parseInt(uk_noshow(uncomma($('#sale-main-total-other').text())));
    var point = parseInt(uk_noshow(uncomma($('#sale-main-total-point').text())));

    $('#sale-main-total-cost').text(comma(total_cost));        // 금액 합계
    $('#sale-main-total-money').text();                 // 현금
    $('#sale-main-total-card').text();                  // 카드
    $('#sale-main-total-gift').text();                  // 상품권
    $('#sale-main-total-other').text();                 // 기타
    $('#sale-main-total-miss').text(comma(total_pay_cost - money - card - gift - other - point));      // 미수금
    $('#sale-main-total-pay-cost').text(comma(total_pay_cost));  // 최종 결제금액

}

function showPayMethod(type){
    var miss_cost = parseInt(uncomma($('#sale-main-total-miss').text()));
    var this_cost = 0;

    var method = '';
    if (type == 'money') {
        method = '현금 계산기';
        var miss_cost = parseInt(uk_noshow(uncomma($('#sale-main-total-miss').text())));
        this_cost = parseInt(uncomma($('#sale-main-total-money').text()));
    }
    else if (type == 'card') {
        method = '카드 계산기';
        this_cost = parseInt(uncomma($('#sale-main-total-card').text()));
    }
    else if (type == 'gift') {
        method = '상품권 계산기';
        this_cost = parseInt(uncomma($('#sale-main-total-gift').text()));
    }
    else if (type == 'other') {
        method = '기타 계산기';
        this_cost = parseInt(uncomma($('#sale-main-total-other').text()));
    }
    else if (type == 'point') {
        method = '포인트 계산기';
        this_cost = parseInt(uncomma($('#sale-main-total-other').text()));
    }

    $('#pay-title').text(method);

    if(type != 'point'){
        $('#pay-modal-text').val(miss_cost + this_cost);
    }
    else {
        if(client_point < (miss_cost + this_cost)) $('#pay-modal-text').val(client_point);
        else $('#pay-modal-text').val(miss_cost + this_cost);
    }

    pay_type = type;
    salesCalculationFlag = true;
    $('#pay-modal-text').attr('data-value', miss_cost);
    $('#pay-method-modal').modal('show');
}

$('#pay-method-modal').on('shown.bs.modal', function () {
    $('#pay-modal-text').select();
});

$('#pay-modal-table > tbody > tr > td').click(function(){
    var tmp = $(this).text();
    var pay_text = $('#pay-modal-text').val();


    if (uk(tmp) == "") {
        $('#pay-modal-text').val(pay_text.substring(0, pay_text.length - 1));
    } else {
        if (salesCalculationFlag) {
            $('#pay-modal-text').val(tmp);
            salesCalculationFlag = false;
        }
        else {
            var data_value = parseInt(uk_noshow(uncomma($('#pay-modal-text').attr('data-value'))));
            var this_text_value = parseInt(pay_text + tmp);
            if(this_text_value > data_value){
                $('#pay-modal-text').val(data_value);
            } else{
                $('#pay-modal-text').val(pay_text + tmp);
            }
        }
    }
});

function payCostSubmit(){
    var cost = parseInt(uk_noshow($('#pay-modal-text').val()));
    if(pay_type == 'money'){
        $('#sale-main-total-money').text(comma(cost));
    }
    else if(pay_type == 'card'){
        $('#sale-main-total-card').text(comma(cost));
    }
    else if(pay_type == 'gift'){
        $('#sale-main-total-gift').text(comma(cost));
    }
    else if(pay_type == 'other'){
        $('#sale-main-total-other').text(comma(cost));
    }
    else if(pay_type == 'point'){
        $('#sale-main-total-point').text(comma(cost));
    }
    $('.modal.in').modal('hide');
    calculationFunction();
}

/* 선불권/횟수권 적용시 현/카/상/기/포 초기화 */
function totalCostsReset(){
    $('#sale-main-total-money').text('0');
    $('#sale-main-total-card').text('0');
    $('#sale-main-total-gift').text('0');
    $('#sale-main-total-other').text('0');
    $('#sale-main-total-point').text('0');
}