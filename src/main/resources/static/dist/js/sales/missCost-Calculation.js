/* 미수금 계산 */

// 미수금 직원 선택 스크립트 동작
function missCostModalEmplClick(obj){
    var idx = $(obj).attr('data-value');
    var name = $(obj).find('c').text();

    // click
    if(!$(obj).hasClass("active")){
        // 첫 번째 행 삭제
        var table_tr = $("#miss-cost-modal-empl-table tbody tr");
        //console.log($(table_tr).html());
        //console.log($(table_tr).attr('data-value'));
        var first_idx = parseInt($(table_tr).attr('data-value'));
        if(first_idx < 0)
            $(table_tr).remove();

        // tr append
        var tmp = '<tr data-value="' + idx + '">' +
            '<td name="name">' + name + '</td>' +
            '<td name="cost"><input type="text" onkeydown="thisTextCommaMissCostEmplVersionm(this)" placeholder="금액을 입력해주세요." value="0"></td>';
        $('#miss-cost-modal-empl-table').append(tmp);
    }
    // click 해제
    else {
        // tr remove
        $('#miss-cost-modal-empl-table tbody > tr').each(function (index, value){
            if(idx == $(value).attr('data-value')){
                $(value).remove();
            }
        });

        // 첫 번째 행 삽입
        if($('#miss-cost-modal-empl-table tbody > tr').length == 0){
            var tmp = '<tr data-value="-1">' +
                '<td colspan="2">직원을 선택해 주세요</td>' +
                '</tr>';
            $('#miss-cost-modal-empl-table').append(tmp);
        }
    }
}
// Pay The Miss Cost
function submitPayTheMissCost(){
    var input_total_cost = parseInt(uk_noshow(uncomma($('#pay-the-misscost').val())));
    var miss_array = new Array();
    var total_pay_cost = 0;
    var breck_flag = false;

    $('#miss-cost-modal-empl-table tbody tr').each(function(index, value){
        var idx = $(value).attr('data-value');
        var name = $(value).find('td[name=name]').text();
        var cost = parseInt(uk_noshow(uncomma($(value).find('td[name=cost]').find('input').val())));

        //console.log("idx : " + idx + ", name : " + name + ", cost : " + cost);

        if(idx < 0){
            breck_flag = true;
            return false;
        }

        total_pay_cost += cost;
        var tmp = {
            empl_idx : idx + "",
            name : name + "",
            cost : cost + ""
        };
        miss_array.push(tmp);
    });
    //console.log(JSON.stringify(miss_array));

    if(breck_flag){
        alert("직원을 선택해 주세요.");
        return false;
    }
    if(input_total_cost != total_pay_cost){
        alert("직원들이 가져갈 매출을 확인해 주세요.");
        return false;
    }


    $.ajax({
        url: "/Sales/payTheMissCost",
        type: "post",
        dataType: "json",
        data: {
            "memo" : $('#miss-cost-modal').val(),
            "client_idx" : client_idx,
            "now" : dateNOW(),
            "resources" : JSON.stringify(miss_array) + "",
            "total_cost" : total_pay_cost
        },
        success: function (data) {
            if (data.code == 200) {
                alert("미수금 처리가 완료되었습니다.");

                var be_miss = parseInt(uk_noshow(uncomma($('#has_miss_cost').text())));
                $('#has_miss_cost').text(comma(be_miss - total_pay_cost));

                $('.modal.in').modal('hide');
            } else {
                alert("잠시 후 다시 시도해 주세요");
                location.href = document.URL;
            }
        },
        error: function () {
            alert("에러가 발생했습니다.");
            location.href = document.URL;
        }
    });

}