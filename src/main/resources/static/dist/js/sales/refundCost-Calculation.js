/* 환불 계산 */
/* 모달 오픈 */
function callHasPrepaidTicket(){
    $('#refund-select').empty();
    $('#refund-select').append("<option value='-1'>===== 선불권 =====</option>");
    $('div[name=prepaid-body] button').each(function(index, value){
        var tmp = '"<option value="';
        tmp += "1:" + $(value).find('input[name=idx]').val() + ":" +
            $(value).find('input[name=name]').val() + ":" +
            $(value).find('input[name=cost]').val() + ":" +
            $(value).find('input[name=vali]').val() + ":" +
            $(value).find('input[name=sal_idx]').val();
        tmp += '">' + $(value).find('input[name=name]').val() + '</option>';

        $('#refund-select').append(tmp);
    });

    $('#refund-select').append("<option value='-1'>===== 티켓 =====</option>");
    $('div[name=ticket-body] button').each(function(index, value){
        var tmp = '"<option value="';
        tmp += "2:" + $(value).find('input[name=idx]').val() + ":" +
            $(value).find('input[name=name]').val() + ":" +
            $(value).find('input[name=count]').val() + ":" +
            $(value).find('input[name=vali]').val() + ":" +
            $(value).find('input[name=sal_idx]').val() + ":" +
            $(value).find('input[name=cost]').val();
        tmp += '">' + $(value).find('input[name=name]').val() + '</option>';

        $('#refund-select').append(tmp);
    });
    $('select').niceSelect('update');
}

/* Select Change */
function refundChange(obj){
    // $(obj).find('option').each(function (index, value){
    var res_datas = $(obj).val();
    res_datas = res_datas.split(":");

    for(var i = 0; i < res_datas.length; i++){
        //console.log(res_datas[i]);
    }

    $('#residual-cate').closest("div").hide();
    $('#residual-sale-cost').closest("div").hide();
    if(res_datas[0] == "1"){
        $('#res_type').val(res_datas[0]);
        $('#res_idx').val(res_datas[1]);
        $('#res_name').val(res_datas[2]);
        $('#res_cost').val(res_datas[3]);
        $('#res_vali').val(res_datas[4]);
        $('#res_sale_idx').val(res_datas[5]);

        $('#residual_cost').text(comma(uk_noshow(res_datas[3])));
        $('#residual_type').text("원");
        $('#residual-cate').text();

        var data = submitAjax("/Sales/amountAtTheTimeOfSale", "idx="+res_datas[5]);
        //console.log(JSON.stringify(data));
        $('#residual-sale-cost').text(comma(uk_noshow(data.total_cost)));
        $('#residual-sale-cost').closest("div").show();
    }
    else if(res_datas[0] == "2"){
        $('#res_type').val(res_datas[0]);
        $('#res_idx').val(res_datas[1]);
        $('#res_name').val(res_datas[2]);
        $('#res_cost').val(res_datas[3]);
        $('#res_vali').val(res_datas[4]);
        $('#res_sale_idx').val(res_datas[5]);

        $('#residual_cost').text(comma(uk_noshow(res_datas[3])));
        $('#residual_type').text("회");

        $('#residual-cate').text("1회당 가격 : ");
        $('#residual-origin-cost').text(comma(res_datas[6]));
        $('#residual-origin-type').text("원");

        var data = submitAjax("/Sales/amountAtTheTimeOfSale", "idx="+res_datas[5]);
        //console.log(JSON.stringify(data));
        $('#residual-sale-cost').text(comma(uk_noshow(data.total_cost)));
        $('#residual-sale-cost').closest("div").show();

        $('#residual-cate').closest("div").show();
    } else{
        $('#res_type').val("0");
        $('#res_idx').val("");
        $('#res_name').val("");
        $('#res_cost').val("");
        $('#res_vali').val("");
        $('#res_sale_idx').val("");

        $('#residual_cost').text("0");
        $('#residual_type').text("원");
    }
    // });
}

/* 직원 클릭 */
function refundModalEmplClick(obj){
    var idx = $(obj).attr('data-value');
    var name = $(obj).find('c').text();

    // click
    if(!$(obj).hasClass("active")){
        // 첫 번째 행 삭제
        var table_tr = $("#refund-cost-modal-empl-table tbody tr");
        var first_idx = parseInt($(table_tr).attr('data-value'));
        if(first_idx < 0)
            $(table_tr).remove();

        // tr append
        var tmp = '<tr data-value="' + idx + '">' +
            '<td name="name">' + name + '</td>' +
            '<td name="cost1"><input type="text" onkeydown="onlyNumber(this)" placeholder="금액을 입력해주세요." value="0"></td>' +
            '<td name="cost2"><input type="text" onkeydown="onlyNumber(this)" placeholder="금액을 입력해주세요." value="0"></td>';
        $('#refund-cost-modal-empl-table').append(tmp);
    }
    // click 해제
    else {
        // tr remove
        $('#refund-cost-modal-empl-table tbody > tr').each(function (index, value){
            if(idx == $(value).attr('data-value')){
                $(value).remove();
            }
        });

        // 첫 번째 행 삽입
        if($('#refund-cost-modal-empl-table tbody > tr').length == 0){
            var tmp = '<tr data-value="-1">' +
                '<td colspan="2">직원을 선택해 주세요</td>' +
                '</tr>';
            $('#refund-cost-modal-empl-table').append(tmp);
        }
    }
}

// 환불 하기
function submitRefundCost(){
    var type = $('#res_type').val();
    var idx = $('#res_idx').val();
    var cost = parseInt($('#res_cost').val());
    var name = $('#res_name').val();
    var vali = $('#res_vali').val();
    var sale_idx = $('#res_sale_idx').val();

    var json_array = new Array();
    var total_cost1 = 0;


    if(type == "0"){
        alert("환불할 선불권, 횟수권을 선택해 주세요");
        return false;
    }
    if($('#refund-cost-modal-empl-table > tbody > tr').length < 1 || $('#refund-cost-modal-empl-table > tbody > tr:first > td').length < 3){
        alert("직원을 선택해 주세요.");
        return false;
    }
    $('#refund-cost-modal-empl-table > tbody > tr').each(function(index, value){
        var empl_idx = $(value).attr('data-value');
        var empl_name = $(value).find('td[name=name]').text();
        var cost1 = parseInt($(value).find('td[name=cost1]').find('input').val());
        var cost2 = parseInt($(value).find('td[name=cost2]').find('input').val());
        var type = '';
        var sum_cost = Math.abs(cost1 - cost2);
        total_cost1 += cost1;

        if(cost1 > cost2){
            type = '1';     // 직원 매출 -
        } else{
            type = '2';     // 직원 매출 +
        }

        var tmp = {
            empl_idx : empl_idx + "",
            empl_name : empl_name + "",
            cost1 : cost1 + "",
            cost2 : cost2 + "",
            type : type + "",
            final_cost : sum_cost + "",
        };
        json_array.push(tmp);
    });

    var client_price_minuse = 0;
    var client_price_sign = true;

    $.each(json_array, function(index, value){
        if(value.type == 1)
            client_price_minuse = client_price_minuse + value.final_cost;
        else
            client_price_minuse = client_price_minuse - value.final_cost;
    });
    if(client_price_minuse < 0)
        client_price_sign = false;




    var data;
    var result = confirm('환불을 하시면 선택된 선불권 / 회원권이 삭제되며\n 이번 달 직원 매출에 변동이 생깁니다. 계속 하시겠습니까?');
    if (result) {
        var tmp = {
            type : type,
            refund_name : name,
            refund_cost : total_cost1,
            client_has_sale_idx : sale_idx,
            resource : JSON.stringify(json_array),
            client_idx : client_idx,
            refund_text : $('#refund-text').val(),
            price_value : Math.abs(parseInt(client_price_minuse)),
            price_sign : client_price_sign
        };
        console.log("tmp : " + JSON.stringify(tmp));

        data = submitAjax("/Sales/refundSubmit", tmp);
    } else {
        return false;
    }

    // 비회원 전환
    if(data.code == 201){
        alert($('div.user-name').find('h3').text() + "님의 환불이 완료되었습니다. \n보유한 회원권이 없으므로 비회원으로 전환되었습니다.");
    }
    else if(data.code == 200){
        alert($('div.user-name').find('h3').text() + "님의 환불이 완료되었습니다.");
    } else{
        alert("잠시 후 다시 시도해 주세요.");
    }

    if(uk($('#reload-resource-client-idx').val()) != ""){
        form_submit('/Sales/sales', {client_idx : $('#reload-resource-client-idx').val()}, 'post');
    } else {
        form_submit('/Sales/sales', {payment : $('#reload-resource-payment').val(), forDate : $('#reload-resource-fordate').val()}, 'post');
    }

}