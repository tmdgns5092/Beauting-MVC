/* 티켓 사용 */

function ticketMake(){
    var tmp = '';
    $('div[name=ticket-body]').empty();
    if(uk(client_idx) != "") {
        $.each(ticket_map, function (index, value) {
            //console.log("ticket count : " + value.count);
            if (value.count > 0) {
                tmp += '<button type="button" onclick="ticketButtonsClicked(this)" class="ticket-buttons">' +
                    '<input type="hidden" name="idx" value="' + value.idx + '"/> ' +
                    '<input type="hidden" name="name" value="' + value.name + '"/> ' +
                    '<input type="hidden" name="count" value="' + value.count + '"/> ' +
                    '<input type="hidden" name="cost" value="' + uk_noshow(value.cost) + '"/> ' +
                    '<input type="hidden" name="vali" value="' + value.validity + '"/> ' +
                    '<input type="hidden" name="sal_idx" value="' + value.sale_idx + '"/> ' +
                    '<input type="hidden" name="use-count" value="0"/> ' +
                    value.name +
                    '</button>';
            }
        });
        $('div[name=ticket-body]').append(tmp);
    }
}

function ticketButtonsClicked(obj){
    // 횟수권 활성화
    if(!$(obj).hasClass('active')){
        $(obj).css({'background-color' : '#e1e1e1', 'color' : '#555555'});
        $(obj).addClass('active');
        ticket_btn_object = obj;
        ticketChoiceMake(obj);
        $('#ticket-choice-modal').modal('show');
    }
    // 횟수권 비활성화
    else{
        $(obj).css({'background-color' : '', 'color' : ''});
        $(obj).find('input[name=use-count]').val(0);
        $(obj).removeClass('active');
        var idx = $(obj).find('input[name=idx]').val();
        var sale_idx = $(obj).find('input[name=sal_idx]').val();

        // 서비스 테이블 티켓 삭제
        $('table.sale-main-table > tbody > tr').each(function(index, value){
            if($(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val() == sale_idx){
                var dc_cost = parseInt($(value).find('td[name=dc]').find('input[name=dc]').val());
                var one_cost = parseInt($(value).find('td[name=one_cost]').text());
                $(value).find('td[name=ticket]').find('div[name=ticket-info]').empty();
                $(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val('0');
                $(value).find('td[name=cost]').find('input[name=cost_text]').val(one_cost - dc_cost);
            }
        });
        // 횟수권 테이블 삭제
        $(obj).closest('.box-body').find('table[name=ticket-table] > tbody > tr').each(function(index, value){
            if($(value).find('input[name=sal_idx]').val() == sale_idx && $(value).find('td').eq(0).attr('data-value') == idx){
                $(value).remove();
            }
        });

        // 티켓 소진 값 복원
        $.each(ticket_map, function(index, value){
            if(value.name == $(obj).find('input[name=name]').val() && value.sale_idx == $(obj).find('input[name=sal_idx]').val()){
                //console.log('서비스 티켓 복원 ... ');
                ticket_map[index].subCount = Number(value.subCount) + Number(value.serviceUse) + "";
                ticket_map[index].serviceUse = "0";
            }
        });
        //console.log(JSON.stringify(ticket_map));

        calculationFunction();
    }
}

function ticketChoiceMake(obj){
    //console.log("ticketChoiceMake()");
    // 적용할 서비스 선택
    $('#ticket-choice-table').empty();
    var idx, name, one_cost, count, dc, empl, cost, disable_check, sub_count;
    var tmp = '<thead>' +
        '<th width="5%"><input type="checkbox" class="allCheck-ticket-modal"></th>' +
        '<th width="35%">서비스명</th>' +
        '<th width="12%">단가</th>' +
        '<th width="8%">수량</th>' +
        '<th width="12%">할인</th>' +
        '<th width="16%">직원</th>' +
        '<th width="12%">금액</th>' +
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
            '</tr>';
    });
    tmp += '</tbody>';
    $('#ticket-choice-table').append(tmp);

    //console.log('json : ' + JSON.stringify(ticket_map));
   $.each(ticket_map, function(index, value){
       //console.log(value.name);
        if(value.name == $(obj).find('input[name=name]').val() && value.sale_idx == $(obj).find('input[name=sal_idx]').val()){
            //console.log("subCount" + value.subCount);
            if(uk_noshow(value.subCount) > 0){
                sub_count = value.subCount;
            } else{
                sub_count = $(obj).find('input[name=count]').val();
            }
        }
   });

    $('#ticket-has-info').empty();
    var tmp2 = '<thead>' +
        '<th width="40%">티켓 이름</th>' +
        '<th width="20%">유효기간</th>' +
        '<th width="20%">남은 횟수</th>' +
        '<th width="20%">소진 횟수</th>' +
        '</thead><tbody>' +
        '<tbody>' +
        '<tr data-value="' + $(obj).find('input[name=sal_idx]').val() + '">' +
        '   <td name="name"> '+ $(obj).find('input[name=name]').val() + '</td>' +
        '   <td name="vali">' + $(obj).find('input[name=vali]').val() + '</td>' +
        '   <td name="count">' + sub_count + '</td>' +
        '   <td>' +
        '       <input type="text" id="ticket-modal-use-count" name="use-count" value="1">' +
        '       <input type="hidden" id="ticket-modal-use-name" name="use-name" value="' + $(obj).find('input[name=name]').val() + '">' +
        '</td>' +
        '</tr>' +
        '</tbody>';
    $('#ticket-has-info').append(tmp2);



    // 티켓 정보 및 사용횟수 선택
    // var idx = $(ticket_btn_object).find('input[name=idx]').val();
    // var count = $(ticket_btn_object).find('input[name=count]').val();
    // $(ticket_btn_object).find('input[name=vali]').val();
    // $(ticket_btn_object).find('input[name=name]').val();
    // $(ticket_btn_object).find('input[name=sal_idx]').val();
    // $(ticket_btn_object).find('input[name=use-count]').val();

}

function ticketTableRemove(idx, ticket_sale_idx){
    var count = 0;
    var ticket_button_up_flag = true;
    $('table.sale-main-table > tbody > tr').each(function(index, value){
        if($(value).attr('data-value') == idx){
            $(value).remove();
        }
    });
    $('table.sale-main-table > tbody > tr').each(function(index, value){
        if($(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val() == ticket_sale_idx){
            ticket_button_up_flag = false;
            count += parseInt(uk_noshow($(value).find('td[name=count]').find('input[name=exhaust_cost]').val()));
        }
    });

    if(ticket_button_up_flag){
        $('div[name=ticket-body]').find('button').each(function(index, value){
            if($(value).find('input[name=sal_idx]').val() == ticket_sale_idx){
                $(value).removeClass('active');
                $(value).css({'background-color' : '', 'color' : ''});
                $(value).find('input[name=use-count]').val(0);
            }
        });
    }

    $.each(ticket_map, function(index, value){
        if(value.sale_idx == ticket_sale_idx){
            ticket_map[index].subCount = ticket_map[index].subCount - count;
        }
    });

    $('.check-service').find('div').find('button').each(function(index, value){
        if($(value).find('input[name=idx]').val() == idx){
            $(value).closest('div').remove();
        }
    });
}

function serviceChoiceFromTicket(obj){
    if(uk_noshow($('#ticket-modal-use-count').val()) == "0"){
        alert("소진할 횟수를 입력해 주세요.");
        $('#ticket-modal-use-count').focus();
        return false;
    }
    var check_obj = new Array();
    $('#ticket-choice-table > tbody > tr').each(function(index, value){
        if($(value).find('td[name=checkbox]').find('input[name=check]').is(":checked")){
            check_obj.push(value);
        }
    });
    if(check_obj.length < 1) {
        ticketButtonUp(this);
    } else {
        // 티켓 소진 값 적용
        $.each(ticket_map, function(index, value){
            if(value.name == $(ticket_btn_object).find('input[name=name]').val() && value.sale_idx == $(ticket_btn_object).find('input[name=sal_idx]').val()){
                if(uk(value.subCount) != ""){
                    var tmp_count = value.subCount;
                } else{
                    var tmp_count = value.count;
                }

                var tmp_obj1 = {subCount : Number(tmp_count) - Number($('#ticket-modal-use-count').val()) + ""};
                var tmp_obj2 = {serviceUse : Number($('#ticket-modal-use-count').val()) + ""};
                jQuery.extend(ticket_map[index], tmp_obj1);
                jQuery.extend(ticket_map[index], tmp_obj2);
            }
        });
        //console.log(JSON.stringify(ticket_map));
    }
    for(var i = 0; i < check_obj.length; i++){
        $('table.sale-main-table > tbody > tr').each(function(index, value){
            if($(value).attr('data-value') == $(check_obj[i]).attr('data-value')){
                var ticket_name = $(ticket_btn_object).find('input[name=name]').val();
                var sale_idx = $(ticket_btn_object).find('input[name=sal_idx]').val();
                var ticket_info = '<small class="label label-success"><i class="fa fa-clock-o"></i>' + ticket_name + '</small>';
                var exhaust_count = $('#ticket-modal-use-count').val();
                var exhaust_name = $('#ticket-modal-use-name').val();

                // 티켓을 사용한 소진 가격을 판매금액으로 object 작성
                // var exhaust_cost = $(value).find('td[name=cost]').find('input[name=cost_text]').val();
                // 티켓을 사용한 소진 가격을 공백으로 object 작성
                var exhaust_cost = '';

                $(value).find('td[name=ticket]').find('div[name=ticket-info]').empty();
                $(value).find('td[name=ticket]').find('div[name=ticket-info]').append(ticket_info);
                $(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val(sale_idx);
                $(value).find('td[name=cost]').find('input[name=cost_text]').val('0');
                $(value).find('td[name=cost]').find('input[name=exhaust_type]').val(2);
                $(value).find('td[name=cost]').find('input[name=exhaust_cost]').val(exhaust_cost);
                $(value).find('td[name=cost]').find('input[name=exhaust_name]').val(exhaust_name);
            }
        });
    }

    var count = $('#ticket-modal-use-count').val();
    $(ticket_btn_object).find('input[name=use-count]').val(count);
    $(obj).css({'background-color' : '#e1e1e1', 'color' : '#555555'});
    $('.modal.in').modal('hide');

    totalCostsReset();
    calculationFunction();
}

// 버튼 올리기
function ticketButtonUp(){
    $(ticket_btn_object).css({'background-color' : '', 'color' : ''});
    $(ticket_btn_object).removeClass('active');
    $(ticket_btn_object).find('input[name=use-count]').val(0);
}

$(document).on('keyup', '#ticket-modal-use-count', function(){
    var max_count = parseInt($(this).closest('tr').find('td[name=count]').text());
    var this_val = parseInt($(this).val());
    if(this_val > max_count) $(this).val(max_count)
});
$(document).on('click', '.allCheck-ticket-modal', function(){
    if ($(this).prop("checked")) {
        $('#ticket-choice-table > tbody > tr').each(function(index, value){
            if(!$(value).find('td[name=checkbox]').find('input[name=check]').is(':disabled')){
                $(value).find('td[name=checkbox]').find('input[name=check]').prop("checked", true);
            }
        });
    } else {
        $('#ticket-choice-table > tbody > tr').each(function(index, value){
            if(!$(value).find('td[name=checkbox]').find('input[name=check]').is(':disabled'))
                $(value).find('td[name=checkbox]').find('input[name=check]').prop("checked", false);
        });
    }
});

/* 선불권 확인 및 sale-main-table 테이블에 없는 선불권 버튼 업 */
function saleTicketMainTableCheck(){
    var service_ticket_array = new Array();
    var button_up_flag = false;

    $('table.sale-main-table > tbody > tr').each(function(index, value){
        if(uk($(value).find('td[name=ticket]').find('div[name=ticket-info]').text() != "") &&
            uk_noshow($(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val()) != "0"){
            service_ticket_array.push($(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val());
        }
    });

    $('div[name=ticket-body] > button').each(function(index, value){
        if($(value).hasClass('active')){
            for(var i = 0; i < service_ticket_array.length; i++){
                if($(value).find('input[name=sal_idx]').val() == service_ticket_array[i]){
                    button_up_flag = true;
                }
            }
            if(!button_up_flag){
                $(value).find('input[name=use-count]').val(0);
                $(value).removeClass('active');
                $(value).css({'background-color' : '#fff', 'color' : '#575962'});
            }
        }
        button_up_flag = false;
    });
}