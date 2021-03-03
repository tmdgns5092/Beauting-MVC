/* 선불권 사용 */

function productPrepaidMake(){
    var tmp = '';
    if(uk(client_idx) != "") {
        $.each(pre_map, function (index, value) {
            if (value.cost > 0) {
                tmp += '<button type="button" onclick="productPrepaidButtonsClicked(this)" class="ticket-buttons">' +
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
        $('div[name=product-prepaid-body]').append(tmp);
    }
}


// 회원권 모달 출력
function productPrepaidButtonsClicked(obj){
    // 횟수권 활성화
    if(!$(obj).hasClass('active')){
        $(obj).css({'background-color' : '#e1e1e1', 'color' : '#555555'});
        $(obj).addClass('active');
        prepaid_btn_object = obj;
        productPreChoiceMake(obj);
        $('#product-prepaid-choice-modal').modal('show');
    }
    // 횟수권 비활성화
    else{
        $(obj).css({'background-color' : '', 'color' : ''});
        $(obj).removeClass('active');
        var idx = $(obj).find('input[name=idx]').val();
        var sale_idx = $(obj).find('input[name=sal_idx]').val();
        var cost = 0;

        // 서비스 테이블 선불권 삭제
        $('table.product-sale-main-table > tbody > tr').each(function(index, value){
            if($(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val() == sale_idx){
                cost += parseInt($(value).find('td[name=cost]').find('input[name=exhaust_cost]').val());
                var dc_cost = parseInt($(value).find('td[name=dc]').find('input[name=dc]').val());
                var one_cost = parseInt($(value).find('td[name=one_cost]').text());
                $(value).find('td[name=ticket]').find('div[name=ticket-info]').empty();
                $(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val('0');
                $(value).find('td[name=cost]').find('input[name=cost_text]').val(one_cost - dc_cost);

                $(value).find('td[name=cost]').find('input[name=exhaust_cost]').val('0');
                $(value).find('td[name=cost]').find('input[name=exhaust_name]').val('');
            }
        });
        // 선불권 테이블 삭제
        $(obj).closest('.box-body').find('table[name=product-prepaid-table] > tbody > tr').each(function(index, value){
            if ($(value).find('input[name=sal_idx]').val() == sale_idx && $(value).find('td').eq(0).attr('data-value') == idx) {
                $(value).remove();
            }
        });

        $.each(pre_backup_array, function(index, value){
            //console.log(value);
            if(uk(value) != ""){
                if(uk(value.name) == $(obj).find('input[name=name]').val() + '_product' + $(obj).find('input[name=sal_idx]').val()){
                    // pre_backup_array[index].cost = pre_backup_array[index].cost - cost;
                    // pre_backup_array.prop(index);
                    pre_backup_array.splice(index, 1);
                }
            }
        });
        //console.log(JSON.stringify(pre_backup_array));

        productCalculationFunction();
    }
}

// 회원권 모달 테이블 그리기
function productPreChoiceMake(obj){
    // 적용할 서비스 선택 출력
    $('#product-prepaid-choice-table').empty();
    var idx, name, one_cost, count, dc, empl, cost, disable_check;
    var tmp = '<thead>' +
        '<th><input type="checkbox" class="allCheck-product-prepaid-modal"></th>' +
        '<th>서비스명</th>' +
        '<th>단가</th>' +
        '<th>수량</th>' +
        '<th>할인</th>' +
        '<th>직원</th>' +
        '<th>금액</th>' +
        '<th>사용 금액</th>' +
        '</thead><tbody>';
    $('table.product-sale-main-table > tbody > tr').each(function(index, value){
        if($(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val() != "0") disable_check = 'disabled';
        else disable_check = '';
        idx = $(value).attr('data-value');
        name = $(value).find('td[name=name]').text();
        one_cost = $(value).find('td[name=one_cost]').text();
        count = $(value).find('td[name=count]').text();
        dc = $(value).find('td[name=dc]').find('input[name=dc]').val();
        empl = $(value).find('td[name=empl]').find('input[name=empl_name1]').val() + ' ' + $(value).find('td[name=empl]').find('input[name=empl_name2]').val();
        cost = $(value).find('td[name=cost]').find('input[name=cost_text]').val();
        tmp += '<tr data-value="' + idx + '">' +
            '<td name="checkbox"><input type="checkbox" name="check" ' + disable_check + '></td>' +
            '<td name="name">' + name + '</td>' +
            '<td name="one_cost">' + one_cost + '</td>' +
            '<td name="count">' + count + '</td>' +
            '<td name="dc">' + dc + '</td>' +
            '<td name="empl">' + empl + '</td>' +
            '<td name="cost">' + cost + '</td>' +
            '<td name="use_cost"><input type="text" name="product-pre-use-cost" value="0" disabled/></td>' +
            '</tr>';
    });
    tmp += '</tbody>';
    $('#product-prepaid-choice-table').append(tmp);
    // /. 적용할 서비스 선택 출력

    // 클릭된 현재 선불권 정보 출력
    var name = $(obj).find('input[name=name]').val();
    var cost = $(obj).find('input[name=cost]').val();
    $.each(pre_backup_array, function(index, value){
        if(value.name == $(obj).find('input[name=name]').val() + '_service'  + $(obj).find('input[name=sal_idx]').val()||
            value.name == $(obj).find('input[name=name]').val() + '_product' + $(obj).find('input[name=sal_idx]').val()){
            cost = cost - value.cost;
        }
    });
    //console.log(JSON.stringify(pre_backup_array));

    $('#product-prepaid-info-name').text(name);
    $('#product-prepaid-info-cost').text(cost);
    $('#product-prepaid-info-surplus-cost').text(cost);
    pre_backup_cost = parseInt(cost);

}

function productPreTableRemove(idx, sale_idx){
    var pre_button_up_flag = true;
    $('table.product-sale-main-table > tbody > tr').each(function(index, value){
        if($(value).attr('data-value') == idx){
            $(value).remove();
        }
    });
    $('table.product-sale-main-table > tbody > tr').each(function(index, value){
        if($(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val() == sale_idx){
            pre_button_up_flag = false;
        }
    });

    if(pre_button_up_flag){
        $('div[name=product-prepaid-body]').find('button').each(function(index, value){
            if($(value).find('input[name=sal_idx]').val() == sale_idx){
                $(value).removeClass('active');
                $(value).css({'background-color' : '', 'color' : ''});
            }
        });
    }

    $('.check-product').find('div').find('button').each(function(index, value){
        //console.log("loop idx : " + $(value).find('input[name=idx]').val() + ", remove idx : " + idx);
        if($(value).find('input[name=idx]').val() == idx){
            $(value).closest('div').remove();
        }
    });

}

function productServiceChoiceFromPrepaid(obj){
    var check_obj = new Array();
    $('#product-prepaid-choice-table > tbody > tr').each(function(index, value){
        if($(value).find('td[name=checkbox]').find('input[name=check]').is(":checked")){
            check_obj.push(value);
        }
    });
    if(check_obj.length < 1) {
        productPreButtonUp(this);
        return false;
        $('.modal.in').modal('hide');
    }
    for(var i = 0; i < check_obj.length; i++) {
        $('table.product-sale-main-table > tbody > tr').each(function (index, value) {
            if ($(value).attr('data-value') == $(check_obj[i]).attr('data-value')) {
                var pre_name = $(prepaid_btn_object).find('input[name=name]').val();
                var sale_idx = $(prepaid_btn_object).find('input[name=sal_idx]').val();
                var pre_info = '<small class="label label-primary"><i class="fa fa-clock-o"></i>' + pre_name + '</small>';
                var cost = $(check_obj[i]).find('td[name=cost]').text();
                var exhaust_cost = $(check_obj[i]).find('td[name=use_cost]').find('input[name=product-pre-use-cost]').val();
                var exhaust_name = $(check_obj[i]).find('td[name=name]').text();

                $(value).find('td[name=ticket]').find('div[name=ticket-info]').empty();
                $(value).find('td[name=ticket]').find('div[name=ticket-info]').append(pre_info);
                $(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val(sale_idx);
                $(value).find('td[name=cost]').find('input[name=cost_text]').val(cost);
                $(value).find('td[name=cost]').find('input[name=exhaust_type]').val(1);
                $(value).find('td[name=cost]').find('input[name=exhaust_cost]').val(exhaust_cost);
                $(value).find('td[name=cost]').find('input[name=exhaust_name]').val(exhaust_name);

            }
        });
    }
    $(obj).css({'background-color' : '#e1e1e1', 'color' : '#555555'});
    $('.modal.in').modal('hide');

    productTotalCostsReset();
    productCalculationFunction();

    pre_backup_array.push({
        name : $(prepaid_btn_object).find('input[name=name]').val() + '_product' + $(prepaid_btn_object).find('input[name=sal_idx]').val(),
        cost : Number($('#product-prepaid-info-cost').text()) - Number($('#product-prepaid-info-surplus-cost').text())
    });

    var use_cost = Number($('#product-prepaid-info-cost').text()) - Number($('#product-prepaid-info-surplus-cost').text());
    $(prepaid_btn_object).find('input[name=use-cost]').val(use_cost);
}

function productPreButtonUp(){
    $(prepaid_btn_object).css({'background-color' : '', 'color' : ''});
    $(prepaid_btn_object).removeClass('active');
    $('#product-prepaid-info-surplus-cost').text('');
}

$(document).on('click', '.allCheck-product-prepaid-modal', function(){
    if ($(this).prop("checked")) {
        $('#product-prepaid-choice-table > tbody > tr').each(function(index, value){
            if(!$(value).find('td[name=checkbox]').find('input[name=check]').is(':disabled')){
                $(value).find('td[name=checkbox]').find('input[name=check]').prop("checked", true);
                $(value).find('td[name=use_cost]').find('input[name=product-pre-use-cost]').prop("disabled", false);
                $(value).find('td[name=checkbox]').find('input[name=check]').trigger('change');
            }
        });
    } else {
        $('#product-prepaid-choice-table > tbody > tr').each(function(index, value){
            if(!$(value).find('td[name=checkbox]').find('input[name=check]').is(':disabled')){
                $(value).find('td[name=checkbox]').find('input[name=check]').prop("checked", false);
                $(value).find('td[name=use_cost]').find('input[name=product-pre-use-cost]').prop("disabled", true);
                $(value).find('td[name=checkbox]').find('input[name=check]').trigger('change');
            }
        });
    }
});

$(document).on('change', '#product-prepaid-choice-table > tbody > tr > td[name=checkbox] > input[name=check]', function(){
    var this_cost = parseInt($(this).closest('tr').find('td[name=cost]').text());
    if($(this).is(':checked')){
        if((pre_backup_cost - this_cost) > 0){
            pre_backup_cost = pre_backup_cost - this_cost;
            $(this).closest('tr').find('td[name=cost]').text(0);
            $(this).closest('tr').find('td[name=use_cost]').find('input[name=product-pre-use-cost]').val(this_cost);
        } else{
            if(pre_backup_cost < 1){
                $(this).prop('checked', false);
            } else{
                $(this).closest('tr').find('td[name=cost]').text(this_cost - pre_backup_cost);
                $(this).closest('tr').find('td[name=use_cost]').find('input[name=product-pre-use-cost]').val(pre_backup_cost);
                pre_backup_cost = 0;
            }
        }
        $(this).closest('tr').find('td[name=use_cost]').find('input[name=product-pre-use-cost]').prop("disabled", false);
    }
    else{
        var plus_cost = parseInt($(this).closest('tr').find('td[name=use_cost]').find('input[name=product-pre-use-cost]').val());
        pre_backup_cost = pre_backup_cost + plus_cost;

        var origin_cost = parseInt($(this).closest('tr').find('td[name=one_cost]').text());
        var count = parseInt($(this).closest('tr').find('td[name=count]').text());
        var dc = parseInt($(this).closest('tr').find('td[name=dc]').text());

        $(this).closest('tr').find('td[name=cost]').text(origin_cost * count - dc);
        $(this).closest('tr').find('td[name=use_cost]').find('input[name=product-pre-use-cost]').val(0);
        $(this).closest('tr').find('td[name=use_cost]').find('input[name=product-pre-use-cost]').prop("disabled", true);
    }

    $('#product-prepaid-info-surplus-cost').text(pre_backup_cost);
});



// 선불권 할인 수동 입력
// 이전 값 저장
$(document).on('focusin', 'input[name=product-pre-use-cost]', function(){
    pre_backup_one = parseInt($(this).val());
});
$(document).on('keyup', 'input[name=product-pre-use-cost]', function(){
    var this_cost = parseInt(uk_noshow($(this).val()));
    var one_cost = parseInt($(this).closest('tr').find('td[name=one_cost]').text());
    var count = parseInt($(this).closest('tr').find('td[name=count]').text());
    var dc_cost = parseInt($(this).closest('tr').find('td[name=dc]').text());
    var pay_cost = (one_cost * count) - dc_cost;
    pre_backup_cost += pre_backup_one;

    if(this_cost > pay_cost){
        if (pay_cost <= pre_backup_cost) this_cost = pay_cost;
        else if (pay_cost > pre_backup_cost) this_cost = pre_backup_cost;
    } else if (this_cost <= pay_cost){
        if (this_cost > pre_backup_cost) this_cost = pre_backup_cost;
    }

    pre_backup_cost = pre_backup_cost - this_cost;

    var print_cost = pay_cost - this_cost;
    $(this).closest('tr').find('td[name=cost]').text(print_cost);
    $(this).closest('tr').find('td[name=use_cost]').find('input[name=product-pre-use-cost]').val(this_cost);
    $('#product-prepaid-info-surplus-cost').text(pre_backup_cost);

    pre_backup_one = this_cost;

});

/* 선불권 확인 및 sale-main-table 테이블에 없는 선불권 버튼 업 */
function productSaleMainTableCheck(){
    var service_prepaid_array = new Array();
    var button_up_flag = false;

    $('table.sale-main-table > tbody > tr').each(function(index, value){
        if(uk($(value).find('td[name=ticket]').find('div[name=ticket-info]').text() != "") &&
            uk_noshow($(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val()) != "0"){
            service_prepaid_array.push($(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val());
        }
    });

    $('div[name=prepaid-body] > button').each(function(index, value){
        //console.log($(value).hasClass('active') + " : " + $(value).find('input[name=name]').val());
        if($(value).hasClass('active')){
            for(var i = 0; i < service_prepaid_array.length; i++){
                if($(value).find('input[name=sal_idx]').val() == service_prepaid_array[i]){
                    button_up_flag = true;
                }
            }
            //console.log("결과 ... 이름 : " + $(value).find('input[name=name]').val() + ", " + button_up_flag);
            if(!button_up_flag){
                //console.log($(value).find('input[name=sal_idx]').val() + " : " + service_prepaid_array[i]);
                $(value).removeClass('active');
                $(value).css({'background-color' : '#fff', 'color' : '#575962'});
            }
        }
        button_up_flag = false;
    });
}