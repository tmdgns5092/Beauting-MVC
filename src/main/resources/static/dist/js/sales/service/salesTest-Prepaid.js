/* 선불권 사용 */

function prepaidMake(){
    //console.log('prepaidMake');
    $('div[name=prepaid-body]').empty();
    var tmp = '';
    var now = dateNOW();

    if(uk(client_idx) != ""){
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
}


// 회원권 모달 출력
function prepaidButtonsClicked(obj){
    //console.log('prepaidButtonsClicked');
    // 횟수권 활성화
    if(!$(obj).hasClass('active')){
        $(obj).css({'background-color' : '#e1e1e1', 'color' : '#555555'});
        $(obj).addClass('active');
        prepaid_btn_object = obj;
        preChoiceMake(obj);
        $('#prepaid-choice-modal').modal('show');
    }
    // 횟수권 비활성화
    else{
        $(obj).css({'background-color' : '', 'color' : ''});
        $(obj).removeClass('active');
        var idx = $(obj).find('input[name=idx]').val();
        var sale_idx = $(obj).find('input[name=sal_idx]').val();
        var cost = 0;

        // 서비스 테이블 선불권 삭제
        $('table.sale-main-table > tbody > tr').each(function(index, value){
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
        $(obj).closest('.box-body').find('table[name=prepaid-table] > tbody > tr').each(function(index, value){
            if($(value).find('input[name=sal_idx]').val() == sale_idx && $(value).find('td').eq(0).attr('data-value') == idx){
                $(value).remove();
            }
        });

        $.each(pre_backup_array, function(index, value){
            if(uk(value) != ""){
                if(uk(value.name) == $(obj).find('input[name=name]').val() + '_service' + $(obj).find('input[name=sal_idx]').val()){
                    pre_backup_array.splice(index, 1);
                }
            }
        });

        $(obj).find('input[name=use-cost]').val('0');

        calculationFunction();
    }
}

// 회원권 모달 테이블 그리기
function preChoiceMake(obj){
    //console.log('preChoiceMake');
    // 적용할 서비스 선택 출력
    $('#prepaid-choice-table').empty();
    var idx, name, one_cost, count, dc, empl, cost, disable_check;
    var tmp = '<thead>' +
        '<th width="5%"><input type="checkbox" class="allCheck-prepaid-modal"></th>' +
        '<th width="21%">서비스명</th>' +
        '<th width="12%">단가</th>' +
        '<th width="8%">수량</th>' +
        '<th width="11%">할인</th>' +
        '<th width="16%">직원</th>' +
        '<th width="12%">금액</th>' +
        '<th width="15%">사용 금액</th>' +
        '</thead><tbody>';
    $('table.sale-main-table > tbody > tr').each(function(index, value){
        if($(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val() != "0") disable_check = 'disabled';
        else disable_check = '';
        idx = $(value).attr('data-value');
        name = $(value).find('td[name=name]').text();
        one_cost = $(value).find('td[name=one_cost]').text();
        count = $(value).find('td[name=count]').text();
        dc = $(value).find('td[name=dc]').find('input[name=dc]').val();
        empl = $(value).find('td[name=empl]').find('input[name=empl_name1]').val() + ' ' + uk($(value).find('td[name=empl]').find('input[name=empl_name2]').val());
        cost = $(value).find('td[name=cost]').find('input[name=cost_text]').val();
        tmp += '<tr data-value="' + idx + '">' +
            '<td name="checkbox"><input type="checkbox" name="check" ' + disable_check + '></td>' +
            '<td name="name">' + name + '</td>' +
            '<td name="one_cost">' + one_cost + '</td>' +
            '<td name="count">' + count + '</td>' +
            '<td name="dc">' + dc + '</td>' +
            '<td name="empl">' + empl + '</td>' +
            '<td name="cost">' + cost + '</td>' +
            '<td name="use_cost"><input type="text" name="pre-use-cost" value="0" disabled/></td>' +
            '</tr>';
    });
    tmp += '</tbody>';
    $('#prepaid-choice-table').append(tmp);
    // /. 적용할 서비스 선택 출력

    // 클릭된 현재 선불권 정보 출력
    var name = $(obj).find('input[name=name]').val();
    var cost = $(obj).find('input[name=cost]').val();
    $.each(pre_backup_array, function(index, value){
        if(value.name == $(obj).find('input[name=name]').val() + '_service' + $(obj).find('input[name=sal_idx]').val() ||
            value.name == $(obj).find('input[name=name]').val() + '_product' + $(obj).find('input[name=sal_idx]').val()){
            cost = cost - value.cost;
            //console.log('cost : ' + cost);
        }
    });


    $('#prepaid-info-name').text(name);
    $('#prepaid-info-cost').text(cost);
    $('#prepaid-info-surplus-cost').text(cost);
    pre_backup_cost = parseInt(cost);

    //console.log("pre backup array : " + JSON.stringify(pre_backup_array));
}

function preTableRemove(idx, sale_idx){
    //console.log('preTableRemove');
    var pre_button_up_flag = true;
    $('table.sale-main-table > tbody > tr').each(function(index, value){
        if($(value).attr('data-value') == idx){
            $(value).remove();
        }
    });
    $('table.sale-main-table > tbody > tr').each(function(index, value){
        if($(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val() == sale_idx){
            pre_button_up_flag = false;
        }
    });

    if(pre_button_up_flag){
        $('div[name=prepaid-body]').find('button').each(function(index, value){
            if($(value).find('input[name=sal_idx]').val() == sale_idx){
                $(value).removeClass('active');
                $(value).css({'background-color' : '', 'color' : ''});
            }
        });
    }

    $('.check-service').find('div').find('button').each(function(index, value){
        if($(value).find('input[name=idx]').val() == idx){
            $(value).closest('div').remove();
        }
    });

}

function serviceChoiceFromPrepaid(obj){
    //console.log('serviceChoiceFromPrepaid');
    var check_obj = new Array();
    $('#prepaid-choice-table > tbody > tr').each(function(index, value){
        if($(value).find('td[name=checkbox]').find('input[name=check]').is(":checked")){
            check_obj.push(value);
        }
    });
    if(check_obj.length < 1) {
        preButtonUp(this);
        return false;
        $('.modal.in').modal('hide');
    }
    for(var i = 0; i < check_obj.length; i++) {
        $('table.sale-main-table > tbody > tr').each(function (index, value) {
            if ($(value).attr('data-value') == $(check_obj[i]).attr('data-value')) {
                var pre_name = $(prepaid_btn_object).find('input[name=name]').val();
                var sale_idx = $(prepaid_btn_object).find('input[name=sal_idx]').val();
                var pre_info = '<small class="label label-primary"><i class="fa fa-clock-o"></i>' + pre_name + '</small>';
                var cost = $(check_obj[i]).find('td[name=cost]').text();
                var exhaust_cost = $(check_obj[i]).find('td[name=use_cost]').find('input[name=pre-use-cost]').val();
                var exhaust_name = $(check_obj[i]).find('td[name=name]').text();
                //console.log(pre_name);

                $(value).find('td[name=ticket]').find('div[name=ticket-info]').empty();
                $(value).find('td[name=ticket]').find('div[name=ticket-info]').append(pre_info);
                $(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val(sale_idx);
                $(value).find('td[name=cost]').find('input[name=cost_text]').val(cost);
                $(value).find('td[name=cost]').find('input[name=exhaust_type]').val(1);
                $(value).find('td[name=cost]').find('input[name=exhaust_cost]').val(exhaust_cost);
                $(value).find('td[name=cost]').find('input[name=exhaust_name]').val(pre_name);
                // $(value).find('td[name=cost]').find('input[name=exhaust_name]').val(exhaust_name);
            }
        });
    }
    $(obj).css({'background-color' : '#e1e1e1', 'color' : '#555555'});
    $('.modal.in').modal('hide');

    totalCostsReset();
    calculationFunction();

    pre_backup_array.push({
        name : $(prepaid_btn_object).find('input[name=name]').val() + '_service' + $(prepaid_btn_object).find('input[name=sal_idx]').val(),
        cost : Number($('#prepaid-info-cost').text()) - Number($('#prepaid-info-surplus-cost').text())
    });

    var use_cost = Number($('#prepaid-info-cost').text()) - Number($('#prepaid-info-surplus-cost').text());
    $(prepaid_btn_object).find('input[name=use-cost]').val(use_cost);
}

function preButtonUp(){
    //console.log('preButtonUp');
    //console.log('선택된 시술이 없습니다. pre btn object : ' + $(prepaid_btn_object).html());
    $(prepaid_btn_object).css({'background-color' : '', 'color' : ''});
    $(prepaid_btn_object).removeClass('active');
    $('#prepaid-info-surplus-cost').text('');
}

$(document).on('click', '.allCheck-prepaid-modal', function(){
    if ($(this).prop("checked")) {
        $('#prepaid-choice-table > tbody > tr').each(function(index, value){
            if(!$(value).find('td[name=checkbox]').find('input[name=check]').is(':disabled')){
                $(value).find('td[name=checkbox]').find('input[name=check]').prop("checked", true);
                $(value).find('td[name=use_cost]').find('input[name=pre-use-cost]').prop("disabled", false);
                $(value).find('td[name=checkbox]').find('input[name=check]').trigger('change');
            }
        });
    } else {
        $('#prepaid-choice-table > tbody > tr').each(function(index, value){
            if(!$(value).find('td[name=checkbox]').find('input[name=check]').is(':disabled')){
                $(value).find('td[name=checkbox]').find('input[name=check]').prop("checked", false);
                $(value).find('td[name=use_cost]').find('input[name=pre-use-cost]').prop("disabled", true);
                $(value).find('td[name=checkbox]').find('input[name=check]').trigger('change');
            }
        });
    }
});

$(document).on('change', '#prepaid-choice-table > tbody > tr > td[name=checkbox] > input[name=check]', function(){
    var this_cost = parseInt($(this).closest('tr').find('td[name=cost]').text());
    if($(this).is(':checked')){
        if((pre_backup_cost - this_cost) > 0){
            pre_backup_cost = pre_backup_cost - this_cost;
            $(this).closest('tr').find('td[name=cost]').text(0);
            $(this).closest('tr').find('td[name=use_cost]').find('input[name=pre-use-cost]').val(this_cost);
        } else{
            if(pre_backup_cost < 1){
                $(this).prop('checked', false);
            } else{
                $(this).closest('tr').find('td[name=cost]').text(this_cost - pre_backup_cost);
                $(this).closest('tr').find('td[name=use_cost]').find('input[name=pre-use-cost]').val(pre_backup_cost);
                pre_backup_cost = 0;
            }
        }
        $(this).closest('tr').find('td[name=use_cost]').find('input[name=pre-use-cost]').prop("disabled", false);
    }
    else{
        var plus_cost = parseInt($(this).closest('tr').find('td[name=use_cost]').find('input[name=pre-use-cost]').val());
        pre_backup_cost = pre_backup_cost + plus_cost;

        var origin_cost = parseInt($(this).closest('tr').find('td[name=one_cost]').text());
        var count = parseInt($(this).closest('tr').find('td[name=count]').text());
        var dc = parseInt($(this).closest('tr').find('td[name=dc]').text());

        $(this).closest('tr').find('td[name=cost]').text(origin_cost * count - dc);
        $(this).closest('tr').find('td[name=use_cost]').find('input[name=pre-use-cost]').val(0);
        $(this).closest('tr').find('td[name=use_cost]').find('input[name=pre-use-cost]').prop("disabled", true);
    }

    $('#prepaid-info-surplus-cost').text(pre_backup_cost);
});



// 선불권 할인 수동 입력
// 이전 값 저장
$(document).on('focusin', 'input[name=pre-use-cost]', function(){
    pre_backup_one = parseInt($(this).val());
});
$(document).on('keyup', 'input[name=pre-use-cost]', function(){
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
    $(this).closest('tr').find('td[name=use_cost]').find('input[name=pre-use-cost]').val(this_cost);
    $('#prepaid-info-surplus-cost').text(pre_backup_cost);

    pre_backup_one = this_cost;
});

/* 선불권 확인 및 sale-main-table 테이블에 없는 선불권 버튼 업 */
function saleMainTableCheck(){
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