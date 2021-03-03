/* 할인 */

function dcClick(type, obj){
    if(type == "All") dc_type_all = true;
    else if (type == 'None'){
        dc_type_all = false;
        dc_object = obj;
    }


    $('#main-dc-cost').val('0');
    $('#main-dc-precnet').val('0');
    $('li[name=cost-tab]').addClass('active');
    $('li[name=precent-tab]').removeClass('active');
    $('#cost-cd').addClass('active');
    $('#precent-dc').removeClass('active');
    $('#service-dc-modal').modal('show');
}

/* 금액 할인 */
$('table[name=dc-table-cost] > tbody > tr> td').click(function(){
    var this_dc = $('#main-dc-cost').val();
    if(this_dc == "0") this_dc = '';

    if($(this).text() == "초기화"){
        // $('#main-dc-cost').val(this_dc.substring(0, this_dc.length -1));
        $('#main-dc-cost').val('0');
    } else{
        $('#main-dc-cost').val(this_dc + $(this).text());
    }
});
/* 퍼센트 할인 */
$('table[name=dc-table-precent] > tbody > tr> td').click(function(){
    var this_dc = $('#main-dc-precnet').val();
    if(this_dc == "0") this_dc = '';

    if($(this).text() == "초기화"){
        // $('#main-dc-precnet').val(this_dc.substring(0, this_dc.length -1));
        $('#main-dc-precnet').val('0');
    } else{
        var dc_check = parseInt(this_dc + $(this).text());
        if(dc_check > 100){
            $('#main-dc-precnet').val('100');
        } else{
            $('#main-dc-precnet').val(this_dc + $(this).text());
        }
    }
});

function submittDC(){

    // cost dc submit
    if($('li[name=cost-tab]').hasClass('active') && $('#cost-cd').hasClass('active')){
        var cost = parseInt($('#main-dc-cost').val());
        if(dc_type_all) allDCSubmit(cost, 'cost');          // all
        else oneDCSubmit(cost, 'cost');             // one
    }
    // precent dc submit
    else{
        var precnet = parseInt($('#main-dc-precnet').val());
        if(dc_type_all) allDCSubmit(precnet, 'precent');       // all
        else oneDCSubmit(precnet, 'precent');       // one
    }
    $('.modal.in').modal('hide');
}


function allDCSubmit(cost, type){
    var total_exhaust_cost = 0;
    var total_exhaust_count = 0;
    pre_backup_cost = [];       // 선불권 백업 초기화
    ticket_backup_array = [];   // 횟수권 백업 초기화

    $('table.sale-main-table > tbody > tr').each(function (index, value){
        // 선불권
        if($(value).find('td[name=cost]').find('input[name=exhaust_type]').val() == '1'){
            total_exhaust_cost += parseInt(uk_noshow($(value).find('td[name=cost]').find('input[name=exhaust_cost]').val()));
        }
        // 횟수권
        if($(value).find('td[name=cost]').find('input[name=exhaust_type]').val() == '2'){
            total_exhaust_count += parseInt(uk_noshow($(value).find('td[name=cost]').find('input[name=exhaust_cost]').val()));
        }



        countEquals(value);
        var one_cost = parseInt($(value).find('td[name=one_cost]').text());
        var count = parseInt($(value).find('td[name=count]').find('p[name=count]').text());
        one_cost = one_cost * count;
        if(type == 'cost'){
            if(one_cost - cost < 1){
                $(value).find('td[name=cost]').find('input[name=cost_text]').val('0');
                $(value).find('td[name=dc]').find('p[name=dc_text]').text(one_cost + '원');
                $(value).find('td[name=dc]').find('input[name=dc]').val(one_cost);
            } else{
                $(value).find('td[name=cost]').find('input[name=cost_text]').val(one_cost * count - cost);
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
    calculationFunction();
}

function oneDCSubmit(cost, type){
    countEquals($(dc_object).closest('tr'));
    var one_cost = parseInt($(dc_object).closest('tr').find('td[name=one_cost]').text());
    var count = parseInt($(dc_object).closest('tr').find('td[name=count]').find('p[name=count]').text());
    one_cost = one_cost * count;
    if(type == 'cost'){
        if(one_cost - cost < 1){
            $(dc_object).closest('tr').find('td[name=cost]').find('input[name=cost_text]').val('0');
            $(dc_object).find('p[name=dc_text]').text(one_cost + '원');
            $(dc_object).find('input[name=dc]').val(one_cost);
        } else{
            $(dc_object).closest('tr').find('td[name=cost]').find('input[name=cost_text]').val(one_cost - cost);
            $(dc_object).find('p[name=dc_text]').text(cost + '원');
            $(dc_object).find('input[name=dc]').val(cost);
        }
    }
    else if(type == 'precent'){
        var dc_cost = (cost / 100) * one_cost;
        $(dc_object).closest('tr').find('td[name=cost]').find('input[name=cost_text]').val(one_cost - dc_cost);
        $(dc_object).find('p[name=dc_text]').text(cost + '%');
        $(dc_object).find('input[name=dc]').val(dc_cost);
    }
    calculationFunction();
}