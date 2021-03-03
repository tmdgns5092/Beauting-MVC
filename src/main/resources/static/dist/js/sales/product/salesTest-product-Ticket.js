/* 티켓 사용 */

function productTicketMake(){
    var tmp = '';
    if(uk(client_idx) != "") {
        $.each(ticket_map, function (index, value) {
            if (value.count > 0) {
                tmp += '<button type="button" onclick="productTicketButtonsClicked(this)" class="ticket-buttons">' +
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
        $('div[name=product-ticket-body]').empty();
        $('div[name=product-ticket-body]').append(tmp);
    }
}

function productTicketButtonsClicked(obj){
    // 횟수권 활성화
    if(!$(obj).hasClass('active')){
        $(obj).css({'background-color' : '#e1e1e1', 'color' : '#555555'});
        $(obj).addClass('active');
        ticket_btn_object = obj;
        productTicketChoiceMake(obj);
        $('#product-ticket-choice-modal').modal('show');
    }
    // 횟수권 비활성화
    else{
        $(obj).css({'background-color' : '', 'color' : ''});
        $(obj).find('input[name=use-count]').val(0);
        $(obj).removeClass('active');
        var idx = $(obj).find('input[name=idx]').val();
        var sale_idx = $(obj).find('input[name=sal_idx]').val();

        // 서비스 테이블 티켓 삭제
        $('table.product-sale-main-table > tbody > tr').each(function(index, value){
            if($(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val() == sale_idx){
                var dc_cost = parseInt($(value).find('td[name=dc]').find('input[name=dc]').val());
                var one_cost = parseInt($(value).find('td[name=one_cost]').text());
                $(value).find('td[name=ticket]').find('div[name=ticket-info]').empty();
                $(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val('0');
                $(value).find('td[name=cost]').find('input[name=cost_text]').val(one_cost - dc_cost);
            }
        });
        // 횟수권 테이블 삭제
        $(obj).closest('.box-body').find('table[name=product-ticket-table] > tbody > tr').each(function(index, value){
            if($(value).find('input[name=sal_idx]').val() == sale_idx && $(value).find('td').eq(0).attr('data-value') == idx){
                $(value).remove();
            }
        });

        $.each(ticket_map, function(index, value){
            if(value.name == $(obj).find('input[name=name]').val() && value.sale_idx == $(obj).find('input[sal_idx]').val()){
                ticket_map[index].subCount = 0;
            }
        });

        // 티켓 소진 값 복원
        $.each(ticket_map, function(index, value){
            if(value.name == $(obj).find('input[name=name]').val() && value.sale_idx == $(obj).find('input[name=sal_idx]').val()){
                //console.log('서비스 티켓 복원 ... ');
                ticket_map[index].subCount = Number(value.subCount) + Number(value.productUse) + "";
                ticket_map[index].productUse = "0";
            }
        });
        //console.log(JSON.stringify(ticket_map));

        productCalculationFunction();
    }
}

function productTicketChoiceMake(obj){
    // 적용할 서비스 선택
    $('#ticket-choice-table').empty();
    var idx, name, one_cost, count, dc, empl, cost, disable_check, sub_count;
    var tmp = '<thead>' +
        '<th><input type="checkbox" class="allCheck-product-ticket-modal"></th>' +
        '<th>서비스명</th>' +
        '<th>단가</th>' +
        '<th>수량</th>' +
        '<th>할인</th>' +
        '<th>직원</th>' +
        '<th>금액</th>' +
        '</thead><tbody>';
    $('table.product-sale-main-table > tbody > tr').each(function(index, value){
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
    $('#product-ticket-choice-table').empty();
    $('#product-ticket-choice-table').append(tmp);

    $.each(ticket_map, function(index, value){
        if(value.name == $(obj).find('input[name=name]').val() && value.sale_idx == $(obj).find('input[name=sal_idx]').val()){
            if(uk_noshow(value.subCount) > 0){
                sub_count = value.subCount;
            } else{
                sub_count = $(obj).find('input[name=count]').val();
            }
        }
    });

    $('#product-ticket-has-info').empty(tmp2);
    var tmp2 = '<thead>' +
        '<th>티켓 이름</th>' +
        '<th>유효기간</th>' +
        '<th>남은 횟수</th>' +
        '<th>소진 횟수</th>' +
        '</thead><tbody>' +
        '<tbody>' +
        '<tr data-value="' + $(obj).find('input[name=sal_idx]').val() + '">' +
        '   <td name="name"> '+ $(obj).find('input[name=name]').val() + '</td>' +
        '   <td name="vali">' + $(obj).find('input[name=vali]').val() + '</td>' +
        '   <td name="count">' + sub_count + '</td>' +
        '   <td>' +
        '       <input type="text" id="product-ticket-modal-use-count" name="use-count" value="1">' +
        '       <input type="hidden" id="product-ticket-modal-use-name" name="use-name" value="' + $(obj).find('input[name=name]').val() + '">' +
        '</td>' +
        '</tr>' +
        '</tbody>';
    $('#product-ticket-has-info').append(tmp2);



    // 티켓 정보 및 사용횟수 선택
    // var idx = $(ticket_btn_object).find('input[name=idx]').val();
    // var count = $(ticket_btn_object).find('input[name=count]').val();
    // $(ticket_btn_object).find('input[name=vali]').val();
    // $(ticket_btn_object).find('input[name=name]').val();
    // $(ticket_btn_object).find('input[name=sal_idx]').val();
    // $(ticket_btn_object).find('input[name=use-count]').val();

}

function productTicketTableRemove(idx, ticket_sale_idx){
    var count = 0;
    var ticket_button_up_flag = true;
    $('table.product-sale-main-table > tbody > tr').each(function(index, value){
        if($(value).attr('data-value') == idx){
            $(value).remove();
        }
    });
    $('table.product-sale-main-table > tbody > tr').each(function(index, value){
        if($(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val() == ticket_sale_idx){
            ticket_button_up_flag = false;
            count += parseInt(uk_noshow($(value).find('td[name=count]').find('input[name=exhaust_cost]').val()));
        }
    });

    if(ticket_button_up_flag){
        $('div[name=product-ticket-body]').find('button').each(function(index, value){
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


    $('.check-product').find('div').find('button').each(function(index, value){
        if($(value).find('input[name=idx]').val() == idx){
            $(value).closest('div').remove();
        }
    });
}

function productChoiceFromTicket(obj){
    //console.log(uk_noshow($('#product-ticket-modal-use-count').val()));
    if(uk_noshow($('#product-ticket-modal-use-count').val()) == "0"){
        alert("소진할 횟수를 입력해 주세요.");
        $('#product-ticket-modal-use-count').focus();
        return false;
    }
    var check_obj = new Array();
    $('#product-ticket-choice-table > tbody > tr').each(function(index, value){
        if($(value).find('td[name=checkbox]').find('input[name=check]').is(":checked")){
            check_obj.push(value);
        }
    });
    if(check_obj.length < 1) {
        productTicketButtonUp(this);
    } else {
        // 티켓 소진 값 적용
        $.each(ticket_map, function(index, value){
            if(value.name == $(ticket_btn_object).find('input[name=name]').val() && value.sale_idx == $(ticket_btn_object).find('input[name=sal_idx]').val()){
                if(uk(value.subCount) != ""){
                    var tmp_count = value.subCount;
                } else{
                    var tmp_count = value.count;
                }

                var tmp_obj1 = {subCount : Number(tmp_count) - Number($('#product-ticket-modal-use-count').val()) + ""};
                var tmp_obj2 = {productUse : Number($('#product-ticket-modal-use-count').val()) + ""};
                jQuery.extend(ticket_map[index], tmp_obj1);
                jQuery.extend(ticket_map[index], tmp_obj2);
            }
        });
        //console.log(JSON.stringify(ticket_map));
    }
    for(var i = 0; i < check_obj.length; i++){
        $('table.product-sale-main-table > tbody > tr').each(function(index, value){
            if($(value).attr('data-value') == $(check_obj[i]).attr('data-value')){
                var ticket_name = $(ticket_btn_object).find('input[name=name]').val();
                var sale_idx = $(ticket_btn_object).find('input[name=sal_idx]').val();
                var ticket_info = '<small class="label label-success"><i class="fa fa-clock-o"></i>' + ticket_name + '</small>';
                var exhaust_count = $('#product-ticket-modal-use-count').val();
                var exhaust_name = $('#product-ticket-modal-use-name').val();

                $(value).find('td[name=ticket]').find('div[name=ticket-info]').empty();
                $(value).find('td[name=ticket]').find('div[name=ticket-info]').append(ticket_info);
                $(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val(sale_idx);
                $(value).find('td[name=cost]').find('input[name=cost_text]').val('0');
                $(value).find('td[name=cost]').find('input[name=exhaust_type]').val(2);
                $(value).find('td[name=cost]').find('input[name=exhaust_cost]').val('');
                $(value).find('td[name=cost]').find('input[name=exhaust_name]').val(exhaust_name);
            }
        });
    }

    var count = $('#product-ticket-modal-use-count').val();
    $(ticket_btn_object).find('input[name=use-count]').val(count);
    $(obj).css({'background-color' : '#e1e1e1', 'color' : '#555555'});
    $('.modal.in').modal('hide');

    productTotalCostsReset();
    productCalculationFunction();
}

// 버튼 올리기
function productTicketButtonUp(){
    $(ticket_btn_object).css({'background-color' : '', 'color' : ''});
    $(ticket_btn_object).removeClass('active');
    $(ticket_btn_object).find('input[name=use-count]').val(0);
}

$(document).on('keyup', '#product-ticket-modal-use-count', function(){
    var max_count = parseInt($(this).closest('tr').find('td[name=count]').text());
    var this_val = parseInt($(this).val());
    if(this_val > max_count) $(this).val(max_count)
});
$(document).on('click', '.allCheck-product-ticket-modal', function(){
    if ($(this).prop("checked")) {
        $('#product-ticket-choice-table > tbody > tr').each(function(index, value){
            if(!$(value).find('td[name=checkbox]').find('input[name=check]').is(':disabled')){
                $(value).find('td[name=checkbox]').find('input[name=check]').prop("checked", true);
            }
        });
    } else {
        $('#product-ticket-choice-table > tbody > tr').each(function(index, value){
            if(!$(value).find('td[name=checkbox]').find('input[name=check]').is(':disabled'))
                $(value).find('td[name=checkbox]').find('input[name=check]').prop("checked", false);
        });
    }
});

/* 선불권 확인 및 product-sale-main-table 테이블에 없는 선불권 버튼 업 */
function productSaleTicketMainTableCheck(){
    var product_ticket_array = new Array();
    var button_up_flag = false;

    $('table.product-sale-main-table > tbody > tr').each(function(index, value){
        if(uk($(value).find('td[name=ticket]').find('div[name=ticket-info]').text() != "") &&
            uk_noshow($(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val()) != "0"){
            product_ticket_array.push($(value).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val());
        }
    });

    $('div[name=product-ticket-body] > button').each(function(index, value){
        if($(value).hasClass('active')){
            for(var i = 0; i < product_ticket_array.length; i++){
                if($(value).find('input[name=sal_idx]').val() == product_ticket_array[i]){
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