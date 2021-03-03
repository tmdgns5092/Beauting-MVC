/* 할인 */

function productDcClick(type, obj){
    if(type == "All") product_dc_type_all = true;
    else if (type == 'None'){
        product_dc_type_all = false;
        product_dc_object = obj;
    }


    $('#product-main-dc-cost').val('0');
    $('#product-main-dc-precnet').val('0');
    $('li[name=product-cost-tab]').addClass('active');
    $('li[name=product-precent-tab]').removeClass('active');
    $('#product-cost-cd').addClass('active');
    $('#product-precent-dc').removeClass('active');
    $('#product-dc-modal').modal('show');
}

/* 금액 할인 */
$('table[name=product-dc-table-cost] > tbody > tr> td').click(function(){
    var this_dc = $('#product-main-dc-cost').val();
    if(this_dc == "0") this_dc = '';

    if($(this).text() == "초기화"){
        // $('#main-dc-cost').val(this_dc.substring(0, this_dc.length -1));
        $('#product-main-dc-cost').val('0');
    } else{
        $('#product-main-dc-cost').val(this_dc + $(this).text());
    }
});
/* 퍼센트 할인 */
$('table[name=product-dc-table-precent] > tbody > tr> td').click(function(){
    var this_dc = $('#product-main-dc-precnet').val();
    if(this_dc == "0") this_dc = '';

    if($(this).text() == "초기화"){
        // $('#main-dc-precnet').val(this_dc.substring(0, this_dc.length -1));
        $('#product-main-dc-precnet').val('0');
    } else{
        var dc_check = parseInt(this_dc + $(this).text());
        if(dc_check > 100){
            $('#product-main-dc-precnet').val('100');
        } else{
            $('#product-main-dc-precnet').val(this_dc + $(this).text());
        }
    }
});

function productSubmittDC(){

    // cost dc submit
    if($('li[name=product-cost-tab]').hasClass('active') && $('#product-cost-cd').hasClass('active')){
        var cost = parseInt($('#product-main-dc-cost').val());
        if(product_dc_type_all) productAllDCSubmit(cost, 'cost');          // all
        else productOneDCSubmit(cost, 'cost');             // one
    }
    // precent dc submit
    else{
        var precnet = parseInt($('#product-main-dc-precnet').val());
        if(product_dc_type_all) productAllDCSubmit(precnet, 'precent');       // all
        else productOneDCSubmit(precnet, 'precent');       // one
    }
    $('.modal.in').modal('hide');
}


function productAllDCSubmit(cost, type){
    $('table.product-sale-main-table > tbody > tr').each(function (index, value){
        productCountEquals(value);
        var one_cost = parseInt($(value).find('td[name=one_cost]').text());
        var count = parseInt($(value).find('td[name=count]').find('p[name=count]').text());
        one_cost = one_cost * count;
        if(type == 'cost'){
            if(one_cost - cost < 1){
                $(value).find('td[name=cost]').find('input[name=cost_text]').val('0');
                $(value).find('td[name=dc]').find('p[name=dc_text]').text(one_cost + '원');
                $(value).find('td[name=dc]').find('input[name=dc]').val(one_cost);
            } else{
                $(value).find('td[name=cost]').find('input[name=cost_text]').val(one_cost - cost);
                $(value).find('td[name=dc]').find('p[name=dc_text]').text(cost + '원');
                $(value).find('td[name=dc]').find('input[name=dc]').val(cost);
            }
        }
        else if(type == 'precent'){
            var dc_cost = (cost / 100) * one_cost;
            $(value).find('td[name=cost]').find('input[name=cost_text]').val(one_cost - dc_cost);
            $(value).find('td[name=dc]').find('p[name=dc_text]').text(cost + '%');
            $(value).find('td[name=dc]').find('input[name=dc]').val(dc_cost);
        }
    });
    productCalculationFunction();
}

function productOneDCSubmit(cost, type){
    productCountEquals($(product_dc_object).closest('tr'));
    var one_cost = parseInt($(product_dc_object).closest('tr').find('td[name=one_cost]').text());
    var count = parseInt($(product_dc_object).closest('tr').find('td[name=count]').find('p[name=count]').text());
    one_cost = one_cost * count;
    if(type == 'cost'){
        if(one_cost - cost < 1){
            $(product_dc_object).closest('tr').find('td[name=cost]').find('input[name=cost_text]').val('0');
            $(product_dc_object).find('p[name=dc_text]').text(one_cost + '원');
            $(product_dc_object).find('input[name=dc]').val(one_cost);
        } else{
            $(product_dc_object).closest('tr').find('td[name=cost]').find('input[name=cost_text]').val(one_cost - cost);
            $(product_dc_object).find('p[name=dc_text]').text(cost + '원');
            $(product_dc_object).find('input[name=dc]').val(cost);
        }
    }
    else if(type == 'precent'){
        var dc_cost = (cost / 100) * one_cost;
        //console.log("dc_cost : " + dc_cost);
        //console.log("one_cost : " + one_cost);
        //console.log("count : " + count);
        $(product_dc_object).closest('tr').find('td[name=cost]').find('input[name=cost_text]').val(one_cost - dc_cost);
        $(product_dc_object).find('p[name=dc_text]').text(cost + '%');
        $(product_dc_object).find('input[name=dc]').val(dc_cost);
    }
    calculationFunction();
}