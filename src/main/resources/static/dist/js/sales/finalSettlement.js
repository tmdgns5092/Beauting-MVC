/* Final Settlment */

// Final Modal Show
function finalModalShow(){
    var total_exhust_cost = 0;
    var total_exhust_count = 0;
    var total_cost = 0;
    var total_money = 0;
    var total_card = 0;
    var total_gift = 0;
    var total_other = 0;
    var total_point = 0;
    var total_miss = 0;
    var total_pay = 0;

    // service
    $('table.sale-main-table > tbody > tr').each(function (index, value){
        // 선불권 사용
        if($(value).find('td[name=cost]').find('input[name=exhaust_type]').val() == "1"){
            //console.log("this ex cost : "+ parseInt(uk_noshow($(value).find('td[name=cost]').find('input[name=exhaust_cost]').val())) + ", total ex cost : " + total_exhust_cost);
            total_exhust_cost += parseInt(uk_noshow($(value).find('td[name=cost]').find('input[name=exhaust_cost]').val()));
        }
    });
    $('div[name=ticket-body] > button').each(function (index, value){
        if($(value).hasClass('active'))
            total_exhust_count += parseInt(uk_noshow($(value).find('input[name=use-count]').val()));
    });
    total_cost += parseInt(uk_noshow(uncomma($('#sale-main-total-cost').text())));
    total_money += parseInt(uk_noshow(uncomma($('#sale-main-total-money').text())));
    total_card += parseInt(uk_noshow(uncomma($('#sale-main-total-card').text())));
    total_gift += parseInt(uk_noshow(uncomma($('#sale-main-total-gift').text())));
    total_other += parseInt(uk_noshow(uncomma($('#sale-main-total-other').text())));
    total_point += parseInt(uk_noshow(uncomma($('#sale-main-total-point').text())));
    total_miss += parseInt(uk_noshow(uncomma($('#sale-main-total-miss').text())));
    total_pay += parseInt(uk_noshow(uncomma($('#sale-main-total-pay-cost').text())));

    // product
    $('table.product-sale-main-table > tbody > tr').each(function (index, value){
        // 선불권 사용
        if($(value).find('td[name=cost]').find('input[name=exhaust_type]').val() == "1"){
            //console.log("this ex cost : "+ parseInt(uk_noshow($(value).find('td[name=cost]').find('input[name=exhaust_cost]').val())) + ", total ex cost : " + total_exhust_cost);
            total_exhust_cost += parseInt(uk_noshow($(value).find('td[name=cost]').find('input[name=exhaust_cost]').val()));
        }
    });
    $('div[name=product-ticket-body] > button').each(function (index, value){
        if($(value).hasClass('active'))
            total_exhust_count += parseInt(uk_noshow($(value).find('input[name=use-count]').val()));
    });
    total_cost += parseInt(uk_noshow(uncomma($('#product-sale-main-total-cost').text())));
    total_money += parseInt(uk_noshow(uncomma($('#product-sale-main-total-money').text())));
    total_card += parseInt(uk_noshow(uncomma($('#product-sale-main-total-card').text())));
    total_gift += parseInt(uk_noshow(uncomma($('#product-sale-main-total-gift').text())));
    total_other += parseInt(uk_noshow(uncomma($('#product-sale-main-total-other').text())));
    total_point += parseInt(uk_noshow(uncomma($('#product-sale-main-total-point').text())));
    total_miss += parseInt(uk_noshow(uncomma($('#product-sale-main-total-miss').text())));
    total_pay += parseInt(uk_noshow(uncomma($('#product-sale-main-total-pay-cost').text())));

    //console.log("total ex cost : " + total_exhust_cost + ", total ex count : " + total_exhust_count);

    $('#final-total-cost').text(comma(total_cost));
    $('#final-total-money').text(comma(total_money));
    $('#final-total-card').text(comma(total_card));
    $('#final-total-gift').text(comma(total_gift));
    $('#final-total-other').text(comma(total_other));
    $('#final-total-point').text(comma(total_point));
    $('#final-total-miss').text(comma(total_miss));
    $('#final-total-pay').text(comma(total_pay));
    $('#final-modal-ex-cost').text(comma(total_exhust_cost));
    $('#final-modal-ex-count').text(comma(total_exhust_count));

    $('#last-pay-modal').modal('show');
}

// 포인트 적립 div show
function pointDIVShow(){
    $('#final-modal-in-point-div').show();
    fnSelectTextSelect();
}

// 포인트 적립 input text select() ...
function fnSelectTextSelect() {
// $('#final-modal-in-point-div').on('shown', function () {
    point_select_flag = true;
    $('#fn-modal-store-point-text').select();
// });
}

// 포인트 적립 테이블 동작
$('#fn-modal-store-point-table > tbody > tr > td').click(function(){
    if(!$(this).hasClass('exception')){
        var tmp = $(this).text();
        var store_text = $('#fn-modal-store-point-text').val();

        if (uk(tmp) == "") {
            $('#fn-modal-store-point-text').val(store_text.substring(0, store_text.length - 1));
        } else {
            if (point_select_flag) {
                //console.log("여기 ... ");
                $('#fn-modal-store-point-text').val(tmp);
                point_select_flag = false;
            } else {
                //console.log("저기 ... ");
                $('#fn-modal-store-point-text').val(store_text + tmp);
            }
        }
    }
});

// 포인트 적립 확인
function fn_point_fix(unit){
    var tmp = parseInt(uk_noshow($('#fn-modal-store-point-text').val()));

    if(unit == 'percent'){
        var total = parseInt(uk_noshow(uncomma($('#final-total-cost').text())));
        var point_cost = ((tmp / 100) * total);

        $('#store-point-view').text(comma(point_cost));
    }
    else if (unit == 'cancel') {
        $('#fn-modal-store-point-text').val('0');
        $('#store-point-view').text('0');
    }
    else if(unit == 'money') $('#store-point-view').text(comma(tmp));

    $(document).trigger('mouseup');
}

$(document).mouseup(function (e) {
    var container = $('#final-modal-in-point-div');
    if (container.has(e.target).length === 0)
        container.hide();
});