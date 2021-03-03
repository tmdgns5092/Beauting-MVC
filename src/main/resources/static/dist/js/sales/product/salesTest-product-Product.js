/* 시술 판매 */

function productCall(obj){
    $('#product-detail-table').empty();
    $('#product_empl_idx1').val(''); $('#product_empl_name1').val(''); $('#product_empl_idx2').val(''); $('#product_empl_name2').val('');
    $('div[name=product-employee-table] > ul > li').each(function (index, value) {
        $(value).css({'border' : '1px solid rgb(225, 225, 225)', 'color' : 'rgb(102, 102, 102)', 'background-color' : 'rgb(255, 255, 255)'});
    });
    $('div[name=product-cate-table] > ul > li').each(function (index, value) {
        $(value).css({'border' : '1px solid rgb(225, 225, 225)', 'color' : 'rgb(102, 102, 102)', 'background-color' : 'rgb(255, 255, 255)'});
    });
    if(uk(product_cate) == "" && uk(product_detail) == ""){
        $.ajax({
            url: "productSelectCall",
            type: "post",
            dataType: "json",
            async: false,
            success: function (data) {
                if (data.code == 200) {
                    /* 카테고리 그리기 */
                    product_cate = data.cateList;
                    product_detail = data.detailList;
                    makeProductCate(obj, data.cateList);
                    productEmplMack();
                    $('#product-modal').modal('show');
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
        $('#product-modal').modal('show');
    }
}

function productEmplMack(){
    $('#product-employee-table').empty();
    var tmp = '<ul>';

    $.each(product_empl_list, function(index, value){
        tmp += '<li onclick="productEmplCheck(this)">' +
            '<input type="hidden" name="idx" value="' + value.idx + '">' +
            '<input type="hidden" name="name" value="' + value.name + '">' +
            value.name +
            '</li>';
    });

    tmp += '</ul>';

    $('#product-employee-table').append(tmp);


    // $('#product-employee-table').empty();
    // var tmp = '<tr>';
    //
    // $.each(product_empl_list, function(index, value){
    //     tmp += '<td onclick="productEmplCheck(this)">' +
    //         '<input type="hidden" name="idx" value="' + value.idx + '">' +
    //         '<input type="hidden" name="name" value="' + value.name + '">' +
    //         value.name +
    //         '</td>';
    // });
    //
    // tmp += '</tr>';
    //
    // $('#product-employee-table').append(tmp);
}

function productEmplCheck(obj){
    var idx = $(obj).find('input[name=idx]').val();
    var name = $(obj).find('input[name=name]').val();

    if(uk($('#product_empl_idx1').val()) == idx){
        $('#product_empl_idx1').val("");
        $('#product_empl_name1').val("");
        $(obj).css('border', '1px solid #6f86d6');
        $(obj).css('color', '#6f86d6');
    }else if(uk($('#product_empl_idx2').val()) == idx){
        $('#product_empl_idx2').val("");
        $('#product_empl_name2').val("");
        $(obj).css('border', '1px solid #6f86d6');
        $(obj).css('color', '#6f86d6');
    } else if(uk($('#product_empl_idx1').val()) == ""){
        $('#product_empl_idx1').val(idx);
        $('#product_empl_name1').val(name);
    } else if(uk($('#product_empl_idx1').val()) != "" && uk($('#product_empl_idx2').val()) == ""){
        $('#product_empl_idx2').val(idx);
        $('#product_empl_name2').val(name);
    } else if(uk($('#product_empl_idx1').val()) != "" && uk($('#product_empl_idx2').val()) != ""){
        $('#product_empl_idx1').val($('#product_empl_idx2').val());
        $('#product_empl_name1').val($('#product_empl_name2').val());
        $('#product_empl_idx2').val(idx);
        $('#product_empl_name2').val(name);
    }

    var empl_idx1 = $('#product_empl_idx1').val();
    var empl_idx2 = $('#product_empl_idx2').val();

    $('#product-employee-table > ul > li').each(function(index, value){
        if($(value).find('input[name=idx]').val() == empl_idx1 || $(value).find('input[name=idx]').val() == empl_idx2){
            $(value).css('border', '1px solid #6f86d6');
            $(value).css('color', '#6f86d6');
        }else{
            $(value).css('border', '1px solid #e1e1e1');
            $(value).css('color', '#666666');
        }
    });


    // $('#product-employee-table > tbody > tr > td').each(function(index, value){
    //     if($(value).find('input[name=idx]').val() == empl_idx1 || $(value).find('input[name=idx]').val() == empl_idx2){
    //         $(value).css('background-color', '#bababa');
    //     }else{
    //         $(value).css('background-color', '#ffffff');
    //     }
    // });

}

function makeProductCate(obj, data){
    var tmp = '<ul>';
    $.each(data, function(index, value){
        tmp += '<li data-value="' + value.idx + '" onclick="makeProductDetail(this)">' + value.category + '</li>';
    });
    tmp += '</ul>';
    $(obj).closest('div').find('div[name=product-cate-table]').append(tmp);


    // var tmp = '<tr>';
    // $.each(data, function(index, value){
    //     tmp += '<td data-value="' + value.idx + '" onclick="makeProductDetail(this)">' + value.category + '</td>';
    // });
    // tmp += '</tr>';
    // $(obj).closest('div').find('table[name=product-cate-table]').append(tmp);
}

function makeProductDetail(obj) {
    $('div[name=product-cate-table] > ul > li').each(function(index, value){

        $(value).css('border', '1px solid #e1e1e1');
        $(value).css('color', '#666666');
    });
    $(obj).css('border', '1px solid #6f86d6');
    $(obj).css('color', '#6f86d6');

    $(obj).closest('.modal-body').find('div[name=product-detail-table]').empty();
    var cate_name = $(obj).text();
    var tmp = '<ul>';
    $.each(product_detail, function (index, value) {
        if (value.category == cate_name) {
            tmp += '<li data-value="idx" onclick="productDetailCheck(this)">' +
                '<input type="hidden" name="idx" value="' + value.idx + '">' +
                '<input type="hidden" name="name" value="' + value.name + '">' +
                '<input type="hidden" name="category" value="' + value.category + '">' +
                '<input type="hidden" name="cost" value="' + value.cost + '">' +
                value.name +
                '</li>';
        }
    });
    tmp += '</ul>';
    $(obj).closest('.modal-body').find('div[name=product-detail-table]').append(tmp);

}

function productDetailCheck(obj){
    var idx = $(obj).find('input[name=idx]').val();
    var name = $(obj).find('input[name=name]').val();
    var category = $(obj).find('input[name=category]').val();
    var cost = $(obj).find('input[name=cost]').val();
    var flag = false;

    var empl_idx1 = $('#product_empl_idx1').val();
    var empl_name1 = $('#product_empl_name1').val();
    var empl_idx2 = "";
    var empl_name2 = "";
    var empl_type = "0";
    var split = "";

    if(uk($('#product_empl_idx2').val()) != "" && uk($('#product_empl_name2').val()) != ""){
        empl_idx2 = $('#product_empl_idx2').val();
        empl_name2 = $('#product_empl_name2').val();
        empl_type = "1";
    }
    if(uk($('#product_empl_idx1').val()) != "" && uk($('#product_empl_name1').val()) != "" &&
        uk($('#product_empl_idx2').val()) != "" && uk($('#product_empl_name2').val()) != ""){
        split = ", ";
    }

    $(obj).closest('.modal-content').find('.check-product > div').each(function(){
        if($(this).find('button').find('input[name=name]').val() == name && $(this).find('button').find('input[name=idx]').val() == idx){
            flag = true;
        }
    });
    if(uk(empl_idx1) == "" && uk(empl_idx2) == ""){ alert("직원을 선택해 주세요"); flag = true;}
    if(flag) return false;

    var tmp = '<div>' +
        '<button class="">' +
        '<input type="hidden" name="empl_type" value="' + empl_type + '">' +
        '<input type="hidden" name="idx" value="' + idx + '">' +
        '<input type="hidden" name="category" value="' + category + '">' +
        '<input type="hidden" name="name" value="' + name + '">' +
        '<input type="hidden" name="cost" value="' + cost + '">' +
        '<input type="hidden" name="empl_idx1" value="' + empl_idx1 + '">' +
        '<input type="hidden" name="empl_name1" value="' + empl_name1 + '">' +
        '<input type="hidden" name="empl_idx2" value="' + empl_idx2+ '">' +
        '<input type="hidden" name="empl_name2" value="' + empl_name2 + '">' +
        '<p>' + name + ' - (' + empl_name1 + split + empl_name2 + ')' + '</p>' +
        '<button onclick="checkedProductRemove(this)"><i class="glyphicon glyphicon-remove"></i></button>' +
        '</button>' +
        '</div>';
    $(obj).closest('.modal-body').find('.check-product').append(tmp);
}

function checkedProductRemove(obj){
    //console.log($(obj).closest('div').html());
    var idx = $(obj).closest('div').find('button').find('input[name=idx]').val();
    var category = $(obj).closest('div').find('button').find('input[name=category]').val();
    var name = $(obj).closest('div').find('button').find('input[name=name]').val();
    var empl_idx1 = $(obj).closest('div').find('button').find('input[name=empl_idx1]').val();
    var empl_idx2 = uk($(obj).closest('div').find('button').find('input[name=empl_idx2]').val());

    $('table.product-sale-main-table > tbody > tr').each(function(index, value){
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

function appendProduct(obj){
    var tmp_list = new Array();
    var tmp = '';
    var thead_tmp = '';
    var table_tmp = '';
    var dc_param = "'All'";
    var dc_obj_param = "'None'";
    var split = "";

    $(obj).closest('.modal-content').find('.check-product > div').each(function(index, value){
        var type = "0";
        if(uk($(this).find('button').find('input[empl_idx2]').val()) == "" && uk($(this).find('button').find('input[empl_name2]').val()) == "" ){
            type = "1";
        }
        tmp = {
            idx : $(this).find('button').find('input[name=idx]').val(),
            category : $(this).find('button').find('input[name=category]').val(),
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
    if($('table.product-sale-main-table > tbody').length > 0){
        $.each(tmp_list, function(index, value){
            var append_flag = true;
            $('table.product-sale-main-table > tbody > tr').each(function (k, v){
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
                    '<td name="name" data-value="' + value.category + '">' + value.name + '</td>' +
                    '<td name="one_cost">' + value.cost + '</td>' +
                    '<td name="count"><p name="count">1</p><i class="glyphicon glyphicon-plus-sign" onclick="productCountPlus(this);"/><i class="glyphicon glyphicon-minus-sign" onclick="productCountMinus(this);"/></td>' +
                    '<td name="dc" onclick="productDcClick(' + dc_obj_param + ', this)"><p name="dc_text">할인</p><input type="hidden" name="dc" value="0"/></td>' +
                    '<td name="empl">' + empl_tmp + '</td>' +
                    '<td name="cost">' +
                    '   <input type="text" name="cost_text" onkeyup="productCostTextChange(this);" value="' + value.cost + '">' +
                    '   <input type="hidden" name="exhaust_type" value="0"/>' +
                    '   <input type="hidden" name="exhaust_cost" value="0"/>' +
                    '   <input type="hidden" name="exhaust_name"/>' +
                    '</td>' +
                    '<td name="ticket"><div name="ticket-info"></div><input type="hidden" name="ticket-sale-idx" value="0"/></td>' +
                    '<td><button class="service-product-tab-remove-btn"><i class="glyphicon glyphicon-remove"></i></button></td>' +
                    '</tr>';
            }
        });

        $(obj).closest('.box-body').parent().closest('.box-body').find('.product-sale-main-table').append(table_tmp);
    }
    // table empty append
    else{
        thead_tmp = '<thead>' +
            '<th>제품명</th>' +
            '<th>단가</th>' +
            '<th>수량</th>' +
            '<th onclick="productDcClick(' + dc_param + ')">할인</th>' +
            '<th>직원</th>' +
            '<th>금액</th>' +
            '<th></th>' +
            '<th>삭제</th>' +
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
                '<td name="name" data-value="' + value.category + '">' + value.name + '</td>' +
                '<td name="one_cost">' + value.cost + '</td>' +
                '<td name="count"><p class="service-num" name="count">1</p><i class="glyphicon glyphicon-plus-sign" onclick="productCountPlus(this);"/><i class="glyphicon glyphicon-minus-sign" onclick="productCountMinus(this);"/></td>' +
                '<td name="dc" onclick="productDcClick(' + dc_obj_param + ', this)"><p name="dc_text">할인</p><input type="hidden" name="dc" value="0"/></td>' +
                '<td name="empl">' + empl_tmp + '</td>' +
                '<td name="cost">' +
                '   <input type="text" name="cost_text" onkeyup="productCostTextChange(this);" value="' + value.cost + '">' +
                '   <input type="hidden" name="exhaust_type" value="0"/>' +
                '   <input type="hidden" name="exhaust_cost" value="0"/>' +
                '   <input type="hidden" name="exhaust_name"/>' +
                '</td>' +
                '<td name="ticket"><div name="ticket-info"></div><input type="hidden" name="ticket-sale-idx" value="0"/></td>' +
                '<td><button class="service-product-tab-remove-btn"><i class="glyphicon glyphicon-remove"></i></button></td>' +
                '</tr>';
        });
        table_tmp += '</tbody>';

        $(obj).closest('.box-body').parent().closest('.box-body').find('.product-sale-main-table').empty();
        $(obj).closest('.box-body').parent().closest('.box-body').find('.product-sale-main-table').append(thead_tmp + table_tmp);
    }

    $('.modal.in').modal('hide');
    productCalculationFunction();
}

function productRemove(obj){
    var idx = $(obj).closest('tr').attr('data-value');
    var ticket_sale_idx = $(obj).closest('tr').find('td[name=ticket]').find('input[name=ticket-sale-idx]').val();

    $('#product-sale-main-total-money').text('0');
    $('#product-sale-main-total-card').text('0');
    $('#product-sale-main-total-gift').text('0');
    $('#product-sale-main-total-other').text('0');
    $('#product-sale-main-total-point').text('0');

    productTicketTableRemove(idx, ticket_sale_idx);
    productPreTableRemove(idx, ticket_sale_idx);
    $(obj).closest('tr').remove();
    productCalculationFunction();
}






function productCountPlus(obj){
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

    productSaleMainTableCheck();
    productSaleTicketMainTableCheck();
    productCalculationFunction();
}

function productCountMinus(obj){
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

        productSaleMainTableCheck();
        productSaleTicketMainTableCheck();
        productCalculationFunction();
    }
}

// param TR
function productCountEquals(obj){
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

    productSaleMainTableCheck();
    productSaleTicketMainTableCheck();
}

function productCostTextChange(obj){
    obj = $(obj).closest('tr');
    //console.log('service cost ... ');
    $(obj).find('td[name=cost]').find('input[name=exhaust_type]').val(0);          // 소진 초기화
    $(obj).find('td[name=cost]').find('input[name=exhaust_cost]').val(0);
    $(obj).find('td[name=dc]').find('p[name=dc_text]').text('할인');                // 할인 초기화
    $(obj).find('td[name=dc]').find('input[name=dc]').val(0);
    $(obj).find('td[name=ticket]').find('div[name=ticket-info]').text('');         // 회원권 초기화
    $(obj).find('td[name=ticket]').find('input[name=ticket-sale-idx]').val(0);

    productSaleMainTableCheck();
    productSaleTicketMainTableCheck();
    productCalculationFunction();
}

$(document).on('click', '.service-product-tab-remove-btn', function(){
    productRemove(this);
});