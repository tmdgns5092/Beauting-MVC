/* 시술 판매 */

function serviceCall(obj){
    $('#service-detail-table').empty();
    $('#empl_idx1').val(''); $('#empl_name1').val(''); $('#empl_idx2').val(''); $('#empl_name2').val('');
    $('div[name=service-employee-table] > ul > li').each(function (index, value) {
    // $('table[name=service-employee-table] > tbody > tr > td').each(function (index, value) {
        $(value).css({'border' : '1px solid rgb(225, 225, 225)', 'color' : 'rgb(102, 102, 102)', 'background-color' : 'rgb(255, 255, 255)'});
    });
    $('div[name=service-cate-table] > ul > li').each(function (index, value) {
    // $('table[name=service-cate-table] > tbody > tr > td').each(function (index, value) {
        $(value).css({'border' : '1px solid rgb(225, 225, 225)', 'color' : 'rgb(102, 102, 102)', 'background-color' : 'rgb(255, 255, 255)'});
    });
    if(uk(service_cate) == "" && uk(service_detail) == ""){
        $.ajax({
            url: "serviceSelectCall",
            type: "post",
            dataType: "json",
            async: false,
            success: function (data) {
                if (data.code == 200) {
                    /* 카테고리 그리기 */
                    service_cate = data.cateList;
                    service_detail = data.detailList;
                    makeServiceCate(obj, data.cateList);
                    serviceEmplMack();
                    $('#service-modal').modal('show');
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
    } else{
        $('#service-modal').modal('show');
    }
}

function serviceEmplMack(){
    $('#service-employee-table').empty();
    var tmp = '<ul>';

    $.each(empl_list, function(index, value){
        tmp += '<li onclick="serviceEmplCheck(this)">' +
            '<input type="hidden" name="idx" value="' + value.idx + '">' +
            '<input type="hidden" name="name" value="' + value.name + '">' +
            value.name +
            '</li>';
    });

    tmp += '</ul>';

    $('#service-employee-table').append(tmp);


    // $('#service-employee-table').empty();
    // var tmp = '<tr>';
    //
    // $.each(empl_list, function(index, value){
    //     tmp += '<td onclick="serviceEmplCheck(this)">' +
    //         '<input type="hidden" name="idx" value="' + value.idx + '">' +
    //         '<input type="hidden" name="name" value="' + value.name + '">' +
    //         value.name +
    //         '</td>';
    // });
    //
    // tmp += '</tr>';
    //
    // $('#service-employee-table').append(tmp);
}

function serviceEmplCheck(obj){
    var idx = $(obj).find('input[name=idx]').val();
    var name = $(obj).find('input[name=name]').val();

    if(uk($('#empl_idx1').val()) == idx){
        $('#empl_idx1').val("");
        $('#empl_name1').val("");
        $(obj).css('background-color', '#bababa');
    }else if(uk($('#empl_idx2').val()) == idx){
        $('#empl_idx2').val("");
        $('#empl_name2').val("");
        $(obj).css('background-color', '#bababa');
    } else if(uk($('#empl_idx1').val()) == ""){
        $('#empl_idx1').val(idx);
        $('#empl_name1').val(name);
    } else if(uk($('#empl_idx1').val()) != "" && uk($('#empl_idx2').val()) == ""){
        $('#empl_idx2').val(idx);
        $('#empl_name2').val(name);
    } else if(uk($('#empl_idx1').val()) != "" && uk($('#empl_idx2').val()) != ""){
        $('#empl_idx1').val($('#empl_idx2').val());
        $('#empl_name1').val($('#empl_name2').val());
        $('#empl_idx2').val(idx);
        $('#empl_name2').val(name);
    }

    var empl_idx1 = $('#empl_idx1').val();
    var empl_idx2 = $('#empl_idx2').val();

    $('#service-employee-table > ul > li').each(function(index, value){
    // $('#service-employee-table > tbody > tr > td').each(function(index, value){
        if($(value).find('input[name=idx]').val() == empl_idx1 || $(value).find('input[name=idx]').val() == empl_idx2){
            $(value).css('border', '1px solid  #6f86d6');
            $(value).css('color', '#6f86d6');
        }else{
            $(value).css('background-color', '#ffffff');
            $(value).css('border', '1px solid #e1e1e1');
            $(value).css('color', '#666666');
        }
    });

}

function makeServiceCate(obj, data){
    var tmp = '<ul>';
    $.each(data, function(index, value){
        tmp += '<li data-value="' + value.idx + '" onclick="makeServiceDetail(this, ' + value.idx + ')">' + value.category + '</li>';
    });
    tmp += '</ul>';

    $(obj).closest('div').find('div[name=service-cate-table]').append(tmp);


    // var tmp = '<tr>';
    // $.each(data, function(index, value){
    //     tmp += '<td data-value="' + value.idx + '" onclick="makeServiceDetail(this)">' + value.category + '</td>';
    // });
    // tmp += '</tr>';
    //
    // $(obj).closest('div').find('table[name=service-cate-table]').append(tmp);
}

function makeServiceDetail(obj, cate_idx) {
    $('div[name=service-cate-table] > ul > li').each(function(index, value){
        // $('table[name=service-cate-table] > tbody > tr > td').each(function(index, value){
        $(value).css('border', '1px solid #e1e1e1');
        $(value).css('color', '#666666');
    });
    $(obj).css('border', '1px solid  #6f86d6');
    $(obj).css('color', '#6f86d6');

    $(obj).closest('.modal-body').find('div[name=service-detail-table]').empty();
    var cate_name = $(obj).text();
    var tmp = '<ul>';

    $.each(service_detail, function (index, value) {
        if (value.category == cate_name) {
            tmp += '<li data-value="idx" onclick="serviceDetailCheck(this)">' +
                '<input type="hidden" name="idx" value="' + value.idx + '">' +
                '<input type="hidden" name="name" value="' + value.name + '">' +
                '<input type="hidden" name="category" value="' + value.category + '">' +
                '<input type="hidden" name="cost" value="' + value.cost + '">' +
                '<input type="hidden" name="cate_idx" value="' + cate_idx + '">' +
                value.name +
                '</li>';
        }
    });
    tmp += '</ul>';
    $(obj).closest('.modal-body').find('div[name=service-detail-table]').append(tmp);

}

function serviceDetailCheck(obj){
    var idx = $(obj).find('input[name=idx]').val();
    var name = $(obj).find('input[name=name]').val();
    var category = $(obj).find('input[name=category]').val();
    var cate_idx = $(obj).find('input[name=cate_idx]').val();
    var cost = $(obj).find('input[name=cost]').val();
    var flag = false;

    var empl_idx1 = $('#empl_idx1').val();
    var empl_name1 = $('#empl_name1').val();
    var empl_idx2 = "";
    var empl_name2 = "";
    var empl_type = "0";
    var split = "";

    if(uk($('#empl_idx2').val()) != "" && uk($('#empl_name2').val()) != ""){
        empl_idx2 = $('#empl_idx2').val();
        empl_name2 = $('#empl_name2').val();
        empl_type = "1";
    }
    if(uk($('#empl_idx1').val()) != "" && uk($('#empl_name1').val()) != "" &&
        uk($('#empl_idx2').val()) != "" && uk($('#empl_name2').val()) != ""){
        split = ", ";
    }

    $(obj).closest('.modal-content').find('.check-service > div').each(function(){
        if($(this).find('button').find('input[name=name]').val() == name && $(this).find('button').find('input[name=idx]').val() == idx){
            flag = true;
        }
    });
    if(uk(empl_idx1) == "" && uk(empl_idx2) == ""){ alert("직원을 선택해 주세요"); flag = true;}
    if(flag) return false;

    var tmp = '<div>' +
        '<button>' +
        '<input type="hidden" name="empl_type" value="' + empl_type + '">' +
        '<input type="hidden" name="idx" value="' + idx + '">' +
        '<input type="hidden" name="category" value="' + category + '">' +
        '<input type="hidden" name="cate_idx" value="' + cate_idx + '">' +
        '<input type="hidden" name="name" value="' + name + '">' +
        '<input type="hidden" name="cost" value="' + cost + '">' +
        '<input type="hidden" name="empl_idx1" value="' + empl_idx1 + '">' +
        '<input type="hidden" name="empl_name1" value="' + empl_name1 + '">' +
        '<input type="hidden" name="empl_idx2" value="' + empl_idx2+ '">' +
        '<input type="hidden" name="empl_name2" value="' + empl_name2 + '">' +
        '<p>' + name + ' - (' + empl_name1 + split + empl_name2 + ')' + '</p>' +
        '<button onclick="checkedServiceRemove(this)"><i class="glyphicon glyphicon-remove"></i></button>' +
        '</button>' +
        '</div>';
    $(obj).closest('.modal-body').find('.check-service').append(tmp);
}

function checkedServiceRemove(obj){
    var idx = $(obj).closest('div').find('button').find('input[name=idx]').val();
    var category = $(obj).closest('div').find('button').find('input[name=category]').val();
    var name = $(obj).closest('div').find('button').find('input[name=name]').val();
    var empl_idx1 = $(obj).closest('div').find('button').find('input[name=empl_idx1]').val();
    var empl_idx2 = uk($(obj).closest('div').find('button').find('input[name=empl_idx2]').val());

    $('table.sale-main-table > tbody > tr').each(function(index, value){
        if(idx == $(value).attr('data-value') &&
            category == $(value).find('td[name=name]').attr('data-value') &&
            name == $(value).find('td[name=name]').text() &&
            empl_idx1 == $(value).find('td[name=empl]').find('input[name=empl_idx1]').val() &&
            empl_idx2 == uk($(value).find('td[name=empl]').find('input[name=empl_idx2]').val())){
            $(value).remove();
        }
    });
    $(obj).closest('div').remove();
}


// 시술 추가
function appendService(obj){
    var tmp_list = new Array();
    var tmp = '';
    var thead_tmp = '';
    var table_tmp = '';
    var dc_param = "'All'";
    var dc_obj_param = "'None'";
    var split = "";

    $(obj).closest('.modal-content').find('.check-service > div').each(function(index, value){
        var type = "0";
        if(uk($(this).find('button').find('input[empl_idx2]').val()) == "" && uk($(this).find('button').find('input[empl_name2]').val()) == "" ){
            type = "1";
        }
        tmp = {
            idx : $(this).find('button').find('input[name=idx]').val(),
            category : $(this).find('button').find('input[name=category]').val(),
            cate_idx : $(this).find('button').find('input[name=cate_idx]').val(),
            name : $(this).find('button').find('input[name=name]').val(),
            cost : $(this).find('button').find('input[name=cost]').val(),
            type : type,
            empl_idx1 : $(this).find('button').find('input[name=empl_idx1]').val(),
            empl_name1 : $(this).find('button').find('input[name=empl_name1]').val(),
            empl_idx2 : $(this).find('button').find('input[name=empl_idx2]').val(),
            empl_name2 : $(this).find('button').find('input[name=empl_name2]').val()
        };
        tmp_list[index] = tmp;
        tmp = '';
    });


    // table append
    if($('table.sale-main-table > tbody').length > 0){
        $.each(tmp_list, function(index, value){
            var append_flag = true;
            $('table.sale-main-table > tbody > tr').each(function (k, v){
                if(value.idx == $(v).attr('data-value') && value.category == $(v).find('td[name=name]').attr('data-value') && value.name == $(v).find('td[name=name]').text()){
                    append_flag = false;
                }
            });
            if(append_flag){
                if(uk(value.empl_idx1) != "" && uk(value.empl_name1) != "" &&
                    uk(value.empl_idx2) != "" && uk(value.empl_name2) != ""){
                    split = " ";
                }
                if(value.type == "0"){
                    var empl_tmp = '<input type="hidden" name="empl_idx1" value="' + value.empl_idx1 + '"/>' +
                        '<input type="hidden" name="empl_name1" value="' + value.empl_name1 + '"/>' +
                        '<p>' + value.empl_name1 + '</p>';
                } else{
                    var empl_tmp = '<input type="hidden" name="empl_idx1" value="' + value.empl_idx1 + '"/>' +
                        '<input type="hidden" name="empl_name1" value="' + value.empl_name1 + '"/>' +
                        '<input type="hidden" name="empl_idx2" value="' + value.empl_idx2 + '"/>' +
                        '<input type="hidden" name="empl_name2" value="' + value.empl_name2 + '"/>' +
                        '<p>' + value.empl_name1 + split + value.empl_name2 + '</p>';
                }
                table_tmp += '<tr data-value="' + value.idx + '">' +
                    '<input type="hidden" name="cate_idx" value="' + value.cate_idx + '">' +
                    '<td name="name" data-value="' + value.category + '">' +
                    value.name +
                    '</td>' +
                    '<td name="one_cost"><p>' + value.cost + '</p><input type="text" name="hidden_one_cost" value="' + value.cost + '" style="display: none;" onkeydown="onlyNumber(this);"> </td>' +
                    '<td name="count"><p name="count">1</p><i class="glyphicon glyphicon-plus-sign" onclick="countPlus(this);"/><i class="glyphicon glyphicon-minus-sign" onclick="countMinus(this);"/></td>' +
                    '<td name="dc" onclick="dcClick(' + dc_obj_param + ', this)"><p name="dc_text">할인</p><input type="hidden" name="dc" value="0"/></td>' +
                    '<td name="empl">' + empl_tmp + '</td>' +
                    '<td name="cost">' +
                    '   <input type="text" name="cost_text" onkeyup="serviceCostTextChange(this);" value="' + value.cost + '">' +
                    '   <input type="hidden" name="exhaust_type" value="0"/>' +
                    '   <input type="hidden" name="exhaust_cost" value="0"/>' +
                    '   <input type="hidden" name="exhaust_name"/>' +
                    '</td>' +
                    '<td name="ticket"><div name="ticket-info"></div><input type="hidden" name="ticket-sale-idx" value="0"/></td>' +
                    '<td><button class="service-service-tab-remove-btn"><i class="glyphicon glyphicon-remove"></i></button></td>' +
                    '</tr>';
            }
        });

        $(obj).closest('.box-body').parent().closest('.box-body').find('.sale-main-table').append(table_tmp);
    }
    // table empty append
    else{
        thead_tmp = '<thead>' +
            '<th width="20%">서비스명</th>' +
            '<th width="10%">단가</th>' +
            '<th width="10%">수량</th>' +
            '<th width="10%" onclick="dcClick(' + dc_param + ')">할인</th>' +
            '<th width="16%">직원</th>' +
            '<th width="13%">금액</th>' +
            '<th width="14%">회원/횟수권</th>' +
            '<th width="7%">삭제</th>' +
            '</thead><tbody>';
        $.each(tmp_list, function(index, value){
            if(uk(value.empl_idx1) != "" && uk(value.empl_name1) != "" &&
                uk(value.empl_idx2) != "" && uk(value.empl_name2) != ""){
                split = " ";
            }
            if(value.type == "0"){
                var empl_tmp = '<input type="hidden" name="empl_idx1" value="' + value.empl_idx1 + '"/>' +
                    '<input type="hidden" name="empl_name1" value="' + value.empl_name1 + '"/>' +
                    '<p>' + value.empl_name1 + '</p>';
            } else{
                var empl_tmp = '<input type="hidden" name="empl_idx1" value="' + value.empl_idx1 + '"/>' +
                    '<input type="hidden" name="empl_name1" value="' + value.empl_name1 + '"/>' +
                    '<input type="hidden" name="empl_idx2" value="' + value.empl_idx2 + '"/>' +
                    '<input type="hidden" name="empl_name2" value="' + value.empl_name2 + '"/>' +
                    '<p>' + value.empl_name1 + split + value.empl_name2 + '</p>';
            }

            table_tmp += '<tr data-value="' + value.idx + '">' +
                '<input type="hidden" name="cate_idx" value="' + value.cate_idx + '">' +
                '<td name="name" data-value="' + value.category + '">' + value.name + '</td>' +
                '<td name="one_cost"><p>' + value.cost + '</p><input type="text" name="hidden_one_cost" value="' + value.cost + '" style="display: none;" onkeydown="onlyNumber(this);"> </td>' +
                '<td name="count"><p class="service-num" name="count">1</p><i class="glyphicon glyphicon-plus-sign" onclick="countPlus(this);"/><i class="glyphicon glyphicon-minus-sign" onclick="countMinus(this);"/></td>' +
                '<td name="dc" onclick="dcClick(' + dc_obj_param + ', this)"><p name="dc_text">할인</p><input type="hidden" name="dc" value="0"/></td>' +
                '<td name="empl">' + empl_tmp + '</td>' +
                '<td name="cost">' +
                '   <input type="text" name="cost_text" onkeyup="serviceCostTextChange(this);" value="' + value.cost + '">' +
                '   <input type="hidden" name="exhaust_type" value="0"/>' +
                '   <input type="hidden" name="exhaust_cost" value="0"/>' +
                '   <input type="hidden" name="exhaust_name"/>' +
                '</td>' +
                '<td name="ticket"><div name="ticket-info"></div><input type="hidden" name="ticket-sale-idx" value="0"/></td>' +
                '<td><button class="service-service-tab-remove-btn"><i class="glyphicon glyphicon-remove"></i></button></td>' +
                '</tr>';
        });
        table_tmp += '</tbody>';

        $(obj).closest('.box-body').parent().closest('.box-body').find('.sale-main-table').empty();
        $(obj).closest('.box-body').parent().closest('.box-body').find('.sale-main-table').append(thead_tmp + table_tmp);
    }

    $('.modal.in').modal('hide');
    calculationFunction();
}

function serviceRemove(obj){
    var idx = $(obj).closest('tr').attr('data-value');
    var ticket_sale_idx = $(obj).closest('tr').find('td[name=ticket]').find('input[name=ticket-sale-idx]').val();

    $('#sale-main-total-money').text('0');
    $('#sale-main-total-card').text('0');
    $('#sale-main-total-gift').text('0');
    $('#sale-main-total-other').text('0');
    $('#sale-main-total-point').text('0');

    ticketTableRemove(idx, ticket_sale_idx);
    preTableRemove(idx, ticket_sale_idx);
    $(obj).closest('tr').remove();
    calculationFunction();
}

function countPlus(obj){
    var one_cost = parseInt(uk_noshow(uncomma($(obj).closest('tr').find('td[name=one_cost]').text())));
    var count = parseInt($(obj).closest('td[name=count]').find('p[name=count]').text());

    $(obj).closest('td[name=count]').find('p[name=count]').text(count + 1);
    $(obj).closest('tr').find('td[name=cost]').find('input[name=cost_text]').val(one_cost * (count + 1));

    $(obj).closest('tr').find('td[name=cost]').find('input[name=exhaust_type]').val(0);          // 소진 초기화
    $(obj).closest('tr').find('td[name=cost]').find('input[name=exhaust_cost]').val(0);
    $(obj).closest('tr').find('td[name=dc]').find('p[name=dc_text]').text('할인');                // 할인 초기화
    $(obj).closest('tr').find('td[name=dc]').find('input[name=dc]').val(0);
    $(obj).closest('tr').find('td[name=ticket]').find('div[name=ticket-info]').text('');         // 회원권 초기화
    $(obj).closest('tr').find('td[name=ticket]').find('input[name=ticket-sale-idx]').val(0);

    saleMainTableCheck();
    saleTicketMainTableCheck();
    calculationFunction();
}

function countMinus(obj){
    var one_cost = parseInt(uk_noshow(uncomma($(obj).closest('tr').find('td[name=one_cost]').text())));
    var count = parseInt($(obj).closest('td[name=count]').find('p[name=count]').text());

    if(count - 1 > 0){
        $(obj).closest('td[name=count]').find('p[name=count]').text(count - 1);
        $(obj).closest('tr').find('td[name=cost]').find('input[name=cost_text]').val(one_cost * (count - 1));

        $(obj).closest('tr').find('td[name=cost]').find('input[name=exhaust_type]').val(0);          // 소진 초기화
        $(obj).closest('tr').find('td[name=cost]').find('input[name=exhaust_cost]').val(0);
        $(obj).closest('tr').find('td[name=dc]').find('p[name=dc_text]').text('할인');                // 할인 초기화
        $(obj).closest('tr').find('td[name=dc]').find('input[name=dc]').val(0);
        $(obj).closest('tr').find('td[name=ticket]').find('div[name=ticket-info]').text('');         // 회원권 초기화
        $(obj).closest('tr').find('td[name=ticket]').find('input[name=ticket-sale-idx]').val(0);

        saleMainTableCheck();
        saleTicketMainTableCheck();
        calculationFunction();
    }
}

// param TR
function countEquals(obj){
    var one_cost = parseInt(uk_noshow(uncomma($(obj).find('td[name=one_cost]').text())));
    var count = parseInt($(obj).find('td[name=count]').find('p[name=count]').text());

    $(obj).find('td[name=count]').find('p[name=count]').text(count);
    $(obj).find('td[name=cost]').find('input[name=cost_text]').val(one_cost * count);

    $(obj).find('td[name=cost]').find('input[name=exhaust_type]').val(0);          // 소진 초기화
    $(obj).find('td[name=cost]').find('input[name=exhaust_cost]').val(0);
    $(obj).find('td[name=dc]').find('p[name=dc_text]').text('할인');                // 할인 초기화
    $(obj).find('td[name=dc]').find('input[name=dc]').val(0);
    $(obj).find('td[name=ticket]').find('div[name=ticket-info]').text('');         // 회원권 초기화
    $(obj).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val(0);

    saleMainTableCheck();
    saleTicketMainTableCheck();
}

function serviceCostTextChange(obj){
    obj = $(obj).closest('tr');
    //console.log('service cost ... ');
    $(obj).find('td[name=cost]').find('input[name=exhaust_type]').val(0);          // 소진 초기화
    $(obj).find('td[name=cost]').find('input[name=exhaust_cost]').val(0);
    $(obj).find('td[name=dc]').find('p[name=dc_text]').text('할인');                // 할인 초기화
    $(obj).find('td[name=dc]').find('input[name=dc]').val(0);
    $(obj).find('td[name=ticket]').find('div[name=ticket-info]').text('');         // 회원권 초기화
    $(obj).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val(0);

    saleMainTableCheck();
    saleTicketMainTableCheck();
    calculationFunction();
}

$(document).on('click', '.service-service-tab-remove-btn', function(){
    serviceRemove(this);
});

// 원가 변경
$(document).on('click', 'td[name=one_cost] > p', function(){
    $(this).hide();
    $(this).closest('td').find('input[type=text]').show();
    $(this).closest('td').find('input[type=text]').select();
});
$(document).on('change keyup paste', 'input[name=hidden_one_cost]', function(key){
    if(key.keyCode == 13) {//키가 13이면 실행 (엔터는 13)
        $(this).trigger('focusout');
    } else {
        var change_one_cost = $(this).val();
        $(this).closest('td').find('p').text(change_one_cost);
    }
    // 초기화 함수 찾아라
    // oneDCSubmit(0, '%');
    $('table.sale-main-table > tbody > tr').each(function (index, value){
        countEquals(value);
    });
    calculationFunction();
    totalCostsReset();
});
$(document).on('focusout', 'input[name=hidden_one_cost]', function(){
    $(this).hide();
    $(this).closest('td').find('p').show();
});