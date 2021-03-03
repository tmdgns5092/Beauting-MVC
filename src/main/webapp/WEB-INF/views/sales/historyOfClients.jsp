<!DOCTYPE html>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../../include/include-header.jspf" %>
    <%@include file="../../include/css/include-stylesheet.jspf" %>
    <%@include file="../../include/utils/include-bootstrapmenu.jspf" %>
    <%@include file="../../include/utils/include-datepicker.jspf" %>
    <style>
        .refund-data > td { background: #ffd28e; }
    </style>
</head>
<body class="on-body">

<%@include file="../../include/include-header-view.jspf" %>
<div class="page-name">
    <div style="width: 1300px;margin: 0 auto">
        <h3>사용내역</h3>
        <p>고객님의 모든 결제내역이 기록되어 있습니다.</p>
    </div>
</div>
<div class="container mb-100 mh-764" style="width: 1300px">
    <div class="history-search-top">
        <div class="employee-select">
            <select class="wide" id="search_type" name="sort">
                <option value="date" selected>날짜검색</option>
            </select>
        </div>
        <div class="employee-input">
            <input type="text" id="search_data" name="data">
            <button class="employee-search-btn" id="search_btn" type="button">검색</button>
        </div>
    </div>

    <div class="history-body">
        <h4>${client.name}</h4>
    </div>

    <div id="memoModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="메모 모달" aria-describedby="메모 모달">
        <div class="modal-dialog">
            <div class="modal-content reservation-modal">
                <%--<div class="modal-header">--%>
                    <%--<img src="/css/test-img/close-btn.png" alt="X버튼 이미지" class="close-btn" onclick="modal_close()" style="text-align: right; float:right;" >--%>
                <%--</div>--%>
                <div class="modal-body">
                    <button class="close-btn02" data-dismiss="modal" onclick="modal_close()"><i class="fas fa-times"></i></button>
                    <h2 class="modal-title">예약 메모</h2>
                    <p style="min-height: 250px;"></p>
                </div>
                <%--<div class="modal-bottom">--%>
                    <%--<button class="modal-btn" onclick="modal_close()">닫기</button>--%>
                <%--</div>--%>
            </div>
        </div>
    </div>

    <div class="history-body02 card">
        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"><a href="#date01" id="tab-1" aria-controls="date01" role="tab" data-toggle="tab" onclick="tab_active2('noKeep');">예약 내역</a></li>
            <li role="presentation"><a href="#date02" id="tab-2" aria-controls="date02" role="tab" data-toggle="tab" onclick="tab_active2('noKeep');">시술 내역</a></li>
            <li role="presentation"><a href="#date03" id="tab-4" aria-controls="date03" role="tab" data-toggle="tab" onclick="tab_active3('noKeep');">회원권 구매내역</a></li>
            <li role="presentation"><a href="#date04" id="tab-3" aria-controls="date04" role="tab" data-toggle="tab" onclick="tab_active4('noKeep');">횟수권 구매내역</a></li>
            <li role="presentation"><a href="#date05" id="tab-5" aria-controls="date05" role="tab" data-toggle="tab" onclick="tab_active5('noKeep');">제품 판매내역</a></li>
            <li role="presentation"><a href="#date06" id="tab-6" aria-controls="date06" role="tab" data-toggle="tab" onclick="tab_active6('noKeep');">미수금 내역</a></li>
        </ul>
        <div class="tab-content">
            <!-- 예약 내역 -->
            <div role="tabpanel" class="tab-pane active" id="date01">
                <div class="upload-card">
                    <table class="history-table table-hover">
                        <thead>
                        <tr>
                            <th scope="col" width="14.2%">예약일</th>
                            <th scope="col" width="14.2%">고객명</th>
                            <th scope="col" width="14.2%">담당자</th>
                            <th scope="col" width="14.2%">예약항목</th>
                            <th scope="col" width="14.2%">예약상태</th>
                            <th scope="col" width="14.2%">소요시간</th>
                            <th scope="col" width="14.2%">메모</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
            <!-- 시술 내역 -->
            <div role="tabpanel" class="tab-pane" id="date02">
                <div class="upload-card">
                    <table class="history-table table-hover">
                        <thead>
                        <tr>
                            <th scope="col" width="12.5%">방문일</th>
                            <th scope="col" width="12.5%">판매 금액</th>
                            <th scope="col" width="12.5%">횟수권 차감</th>
                            <th scope="col" width="12.5%">회원권 차감</th>
                            <th scope="col" width="12.5%">항목 갯수</th>
                            <th scope="col" width="12.5%">결제 금액</th>
                            <th scope="col" width="12.5%">미수금</th>
                            <th scope="col" width="12.5%">상세</th>
                            <th scope="col" width="12.5%"></th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
            <!-- 회원권 구매내역 -->
            <div role="tabpanel" class="tab-pane" id="date03">
                <div class="upload-card">
                    <table class="history-table table-hover">
                        <thead>
                        <tr>
                            <th scope="col" width="12.5%">구매일</th>
                            <th scope="col" width="12.5%">판매 금액</th>
                            <th scope="col" width="12.5%">담당자</th>
                            <th scope="col" width="12.5%">회원권 명</th>
                            <th scope="col" width="12.5%">적립 금액</th>
                            <th scope="col" width="12.5%">미수금</th>
                            <th scope="col" width="12.5%">만료일</th>
                            <th scope="col" width="12.5%">상세</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
            <!--티켓 구매내역 -->
            <div role="tabpanel" class="tab-pane" id="date04">
                <div class="upload-card">
                    <table class="history-table table-hover">
                        <thead>
                        <tr>
                            <th scope="col" width="12.5%">구매일</th>
                            <th scope="col" width="12.5%">판매 금액</th>
                            <th scope="col" width="12.5%">담당자</th>
                            <th scope="col" width="12.5%">횟수권 명</th>
                            <th scope="col" width="12.5%">적립 횟수</th>
                            <th scope="col" width="12.5%">미수금</th>
                            <th scope="col" width="12.5%">만료일</th>
                            <th scope="col" width="12.5%">상세</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
            <!-- 제품 구매내역 -->
            <div role="tabpanel" class="tab-pane" id="date05">
                <div class="upload-card">
                    <table class="history-table table-hover">
                        <thead>
                        <tr>
                            <th scope="col" width="12.5%">구매일</th>
                            <th scope="col" width="12.5%">판매 금액</th>
                            <th scope="col" width="12.5%">횟수권 차감</th>
                            <th scope="col" width="12.5%">회원권 차감</th>
                            <th scope="col" width="12.5%">항목 갯수</th>
                            <th scope="col" width="12.5%">결제 금액</th>
                            <th scope="col" width="12.5%">미수금</th>
                            <th scope="col" width="12.5%">상세</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
            <div role="tabpanel" class="tab-pane" id="date06">
                <div class="upload-card">
                    <table class="history-table table-hover">
                        <thead>
                        <tr>
                            <th scope="col" width="16.6%">발생일</th>
                            <th scope="col" width="16.6%">서비스</th>
                            <th scope="col" width="16.6%">항목 갯수</th>
                            <th scope="col" width="16.6%">판매금액</th>
                            <th scope="col" width="16.6%">미수금액</th>
                            <th scope="col" width="16.6%">상세</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="page-nav">
                <input type="hidden" id="target">
                <nav aria-label="Page navigation">
                    <ul class="pagination" id="paging_ul">
                    </ul>
                </nav>
            </div>
        </div>
    </div>
    <%@include file="../../include/modals/sales/clientNormalSaleHistory.jspf" %>
    <%@include file="../../include/modals/sales/clientOtherSaleHistory.jspf" %>
    <%@include file="../../include/modals/sales/clientSimpleHistory.jspf" %>
</div>


<%@include file="../../include/include-menu-footer.jspf" %>
<script src="/dist/js/utils.js"></script>
<script>
    $('#search_data').keydown(function(key){
        if (key.keyCode == 13) {
            $('#search_btn').trigger('click');
        }
    });

    $(document).ready(function () {
        // $('#tab-1').click();
        tab_active1('noKeep');
    });

    var pageNo = 1;

    var data = {
        pageNo : pageNo,
        client_idx : '${client.idx}',
        column : $('#search_type').val(),
        data : '%'+$('#search_data').val()+'%'
    };

    function setPagingVal(paging){
        $('#paging_ul').empty();
        var prev = '<li>'+
            '<a href="#" aria-label="Previous" onclick="paging(this)" id="prev" data-value="'+paging.prevPageNo+'" class="paging_btn"><span aria-hidden="true">≪</span></a>'+
            '</li>';
        var next = '<li>'+
            '<a href="#" aria-label="Next" onclick="paging(this)" id="next" data-value="'+paging.nextPageNo+'" class="paging_btn"><span aria-hidden="true">≫</span></a>'+
            '</li>';
        $('#paging_ul').append(prev);
        for(i = paging.startPageNo; i <= paging.endPageNo; i++){
            var current = '<li>'+
                '<a href="javascript:void(0)" onclick="paging(this)" name="current" data-value="' + i + '" class="paging_btn">'+ i + '<span class="sr-only">(current)</span></a>'+
                '</li>';
            $('#paging_ul').append(current);
        }
        $('a[name=current]').each(function (k, v) {
            if(paging.pageNo == $(v).attr("data-value")) $(v).closest("li").attr("class","active");
        });
        $('#paging_ul').append(next);
    }

    // 페이징 (페이지 이동 함수)
    function paging(obj){
        var target = $("#target").val();// tab ID를 가지고있음
        data["pageNo"] = $(obj).attr("data-value");
        // $(target).click();
        eval("keep='keep';" + target+'(keep)');
    }

    // 고객검색 - 고객내역 페이지로 이동함
    $('#search_btn').click(function () {
        data["column"] = $('#search_type').val();
        data["data"] = '%'+$('#search_data').val()+'%';
        var target = $('#target').val();
        $(target).trigger('click');
    });

    // 메모 모달 열기
    function modal_open(obj) {
        $('#memoModal').modal('show');
        $('#memoModal').find('p').text($(obj).next().val());
    }
    var payment_json = {
        total_sales_fee : "금액합계",
        total_money : "현금",
        total_card : "카드",
        total_gift_cost : "상품권",
        total_other_cost : "기타",
        total_miss_cost : "미수금",
        total_payment : "최종 결제금액"
    };

    function setDetail1(item, service, memo){
        console.log("memo : " + memo);
        var service_area = $('#clientNormalSaleHistoryModal').find('div[name='+service+'_table]');
        $('#sales_date').text(item.sales_date + ' 판매');
        service_area.css('display','block');
        var resource = JSON.parse(item.sale_resource);
        var detail = $('#' + service + '_resource');
        $('#normal_store_point').text(numberWithCommas(item.store_point));
        resource.forEach(function (item) {
            var empl = item.empl1_name, use_name = '-';
            if(item.empl2_idx != null) empl += '<br>' + item.empl2_name;
            if(item.use_name != null && item.use_name != '') {
                use_name = item.use_name;
                /*if(item.use_accumulate != null && item.use_accumulate != '')
                    var use_accumulate = numberWithCommas(parseInt(item.use_accumulate)) + '원';*/
            }
            var tr = '<tr>' +
                '       <td>' + item.services_name + '</td>' +
                '       <td>' + numberWithCommas(item.services_cost) + '원</td>' +
                '       <td>' + item.count + '</td>' +
                '       <td>' + numberWithCommas(item.dc) + '원</td>' +
                '       <td>' + empl + '</td>' +
                '       <td>' + numberWithCommas(item.sales_fee) + '원</td>' +
                '       <td>' + use_name + '</td>' +
                '</tr>';
            detail.append(tr);
            // console.log("item : " + JSON.stringify(item));
            $('#service-detail-modal-memo').text(memo);
            $('#product-detail-modal-memo').text(memo);
        });

        var use_prepaid;
        var payment = $('#' + service + '_cost');
        for(var key in payment_json) {
            var tr = '<tr>'+
                '<td>' + payment_json[key] + '</td>'+
                '<td><p>' + numberWithCommas(item[key]) + '<span> 원</span></p></td>'+
                '</tr>';
            payment.append(tr);
        }
    }

    function detail1(obj) {
        var detail_modal = $('#clientNormalSaleHistoryModal');
        detail_modal.modal('show');
        var tbody = $('#clientNormalSaleHistoryModal').find('table').find('tbody');
        tbody.empty();
        $('#clientNormalSaleHistoryModal').find('div[name=prepaid]').empty();
        $('#clientNormalSaleHistoryModal').find('div[name=ticket]').empty();
        var forDate = $(obj).closest("tr").attr("data-value");
        $.post('selectSalesDetailAjax', {forDate : forDate, client_idx : "${client.idx}"}, function (item) {
            if(item.code == 902){
                alert('잠시후 다시 시도해주십시오.');
            } else {
                var surgery = item.surgery, product = item.product;
                if(surgery != null)setDetail1(surgery, 'surgery', surgery.memo);
                else $('#clientNormalSaleHistoryModal').find('div[name=surgery_table]').css('display', 'none');

                if(product != null)setDetail1(product, 'product', product.memo);
                else $('#clientNormalSaleHistoryModal').find('div[name=product_table]').css('display', 'none');
            }
        }).fail(function (error) {alert(error.status)});
    }

    function otherDetail(obj) {
        var detail_modal = $('#clientOtherHistoryModal');
        detail_modal.modal('show');
        var resource_tbody = $('#other_resource');
        var total_tbody = $('#other_cost');
        resource_tbody.empty();
        total_tbody.empty();
        var idx = $(obj).attr('data-value');
        $.post('selectSalesOneAjax', {idx : idx}, function (json) {
            if(json.code == 200){
                var memo = json.salesOne.memo;
                json = json.salesOne;
                //console.log(json);
                $('#other_date').text(json.sales_date + ' 판매');
                $('#other_store_point').text(numberWithCommas(json.store_point));
                var resource = JSON.parse(json.sale_resource);
                var detail = $('#other_resource');
                json["total_payment"] = json.total_sales_fee - json.total_miss_cost;
                var item = resource[0];
                var empl = item.empl1_name, date = new Date(json.validity_date.time);
                date = date.getUTCFullYear() + '년 ' + pad(date.getUTCMonth()+1, 2) +'월 ' + date.getUTCDate() + '일';
                if(item.empl2_idx != null) empl += '<br>' + item.empl2_name;
                var tr = '<tr>' +
                    '<td>' + item.services_name + '</td>' +
                    '<td>' + numberWithCommas(parseInt(item.services_cost)) + '원</td>' +
                    '<td>' + numberWithCommas(parseInt(item.dc)) + '원</td>' +
                    '<td>' + numberWithCommas(parseInt(item.sales_fee)) + '원</td>' +
                    '<td>' + numberWithCommas(parseInt(json.accumulate)) + '</td>' +
                    '<td>' + date + '</td>' +
                    '<td>' + empl + '</td>';
                '</tr>';
                detail.append(tr);
                var payment = $('#other_cost');
                for(var key in payment_json) {
                    var tr = '<tr>'+
                        '<td>' + payment_json[key] + '</td>'+
                        '<td><p>' + numberWithCommas(json[key]) + '<span> 원</span></p></td>'+
                        '</tr>';
                    payment.append(tr);
                    $('#prepaid-detail-modal-memo').text(memo);
                }
            } else {
                alert('잠시후 다시 시도해주십시오.');
                return false;
            }
        }, 'json').fail(function (error) {
            alert(error.status);
        });
    }

    function simpleDetail(obj) {
        var detail_modal = $('#clientSimpleHistoryModal');
        detail_modal.modal('show');
        var resource_tbody = $('#simple_resource');
        var total_tbody = $('#simple_cost');
        resource_tbody.empty();
        total_tbody.empty();
        var idx = $(obj).attr('data-value');
        $.post('selectSalesOneAjax', {idx : idx}, function (json) {
            if(json.code == 200){
                var memo = json.salesOne.memo;
                json = json.salesOne;
                $('#simple_date').text(json.sales_date + ' 판매');
                $('#simple_store_point').text(numberWithCommas(json.store_point));
                var resource = JSON.parse(json.sale_resource);
                var detail = $('#simple_resource');
                json["total_payment"] = json.total_sales_fee - json.total_miss_cost;
                resource.forEach(function (item) {
                    var empl = item.empl1_name, use_accumulate = '', use_name ='-';
                    if(item.empl2_idx != null) empl += '<br>' + item.empl2_name;
                    if(item.use_name != null && item.use_name != '') {
                        use_name = item.use_name;
                        if(item.use_accumulate != null && item.use_accumulate != '') use_accumulate = numberWithCommas(parseInt(item.use_accumulate)) + '원';
                    };
                    var tr = '<tr>' +
                        '<td>' + item.services_name + '</td>' +
                        '<td>' + numberWithCommas(parseInt(item.services_cost)) + '원</td>' +
                        '<td>' + numberWithCommas(parseInt(item.dc)) + '원</td>' +
                        '<td>' + numberWithCommas(parseInt(item.sales_fee)) + '원</td>' +
                        '<td>' + use_name + ' ' + use_accumulate + '</td>' +
                        '<td>' + empl + '</td>';
                    '</tr>';
                    detail.append(tr);
                });
                var payment = $('#simple_cost');
                for(var key in payment_json) {
                    var tr = '<tr>'+
                        '<td>' + payment_json[key] + '</td>'+
                        '<td><p>' + numberWithCommas(json[key]) + '<span> 원</span></p></td>'+
                        '</tr>';
                    payment.append(tr);
                }
                $('#miss-cost-detail-modal-memo').text(memo);
            } else {
                alert('잠시후 다시 시도해주십시오.');
                return false;
            }
        }, 'json').fail(function (error) {
            alert(error.status);
        });
    }

    // 메모 모달 닫기
    function modal_close() {
        $('#memoModal').modal('hide');
    }
    // i = sales 반복 수, row = 뿌리는 행 수
    var i;
    // $('#tab-1').click(function () {
    function tab_active1(keep) {
        var tbody = $('#date01').find('tbody');
        tbody.empty();
        // $('#target').val("#tab-1");
        if(keep == 'noKeep') data.pageNo = 1;
        $('#target').val("tab_active1");
        $.post('selectReservationHistory', data, function (json) {
            setPagingVal(json.paging);
            delete json.paging;
            var list = json.salesList;
            if (list.length == 0) tbody.append('<tr><td colspan="7">예약 내역이 없습니다</td></tr>');
            else {
                list.forEach(function (item) {
                    var btn = '', status = item.status;
                    var tr = document.createElement("tr");
                    if (item.memo != '') btn = '<button onclick="modal_open(this)">보기</button><Input type="hidden" value="' + item.memo + '">';
                    if (status == 0) status = '취소';
                    else if (status == 1) status = '등록';
                    else if (status == 3) status = '불이행';
                    else if (status == 5) status = '계산완료';
                    else status = ''; // 없을 일이다..

                    var td = '<td>' + item.date + '</td>' +
                        '<td>' + item.cName + '</td>' +
                        '<td>' + item.eName + '</td>' +
                        '<td>' + item.sName + '</td>' +
                        '<td>' + status + '</td>' +
                        '<td>' + item.work_time + '</td>' +
                        '<td>' + btn + '</td>';
                    tr.innerHTML = td;
                    tbody.append(tr);
                });
            }
        }, 'json').fail(function (error) {
            alert(error.status)
        });
        // });
    }

    // $('#tab-2').click(function () {
    function tab_active2(keep) {
        var tbody = $('#date02').find('tbody');
        tbody.empty();
        data["type"] = 0;
        // $('#target').val("#tab-2");
        if(keep == 'noKeep') data.pageNo = 1;
        $('#target').val("tab_active2");
        $.post('selectServicesTypeHistory', data, function (json) {
            //console.log(json);
            setPagingVal(json.paging);
            delete json.paging;
            var list = json.salesList;
            if (list.length == 0) tbody.append('<tr><td colspan="8">시술 내역이 없습니다</td></tr>');
            else {
                list.forEach(function (item) {
                    var sales_count = JSON.parse(item.sale_resource).length;
                    //console.log("------------------");
                    var tr = document.createElement("tr");

                    if(item.status != 3){
                        var td = '<input type="hidden" name="sale_idx" value="' +item.idx+ '">' +
                            '<td>' + item.forDate.split(' ')[0] + '</td>' +
                            '<td>' + numberWithCommas(parseInt(item.total_sales_fee)) + '원</td>' +
                            '<td>' + item.use_ticket_count + ' 회</td>' +
                            '<td>' + numberWithCommas(parseInt(item.use_prepaid_cost)) + '원</td>' +
                            '<td>' + sales_count + '개</td>' +
                            '<td>' + numberWithCommas(item.total_money + item.total_card + item.total_other_cost + item.total_gift_cost) + '원</td>' +
                            '<td>' + numberWithCommas(item.total_miss_cost) + '원</td>' +
                            '<td name="btn"><button onclick="detail1(this)">상세</button></td>' +
                            '<td><p class="glyphicon glyphicon-remove" onclick="historyDelete(this)" style="cursor: pointer"></p></td>';
                    } else {
                        var td = '<input type="hidden" name="sale_idx" value="' +item.idx+ '">' +
                            '<td style="background-color: #fafafc;">' + item.forDate.split(' ')[0] + '</td>' +
                            '<td style="background-color: #fafafc;">' + numberWithCommas(parseInt(item.total_sales_fee)) + '원</td>' +
                            '<td style="background-color: #fafafc;">' + item.use_ticket_count + ' 회</td>' +
                            '<td style="background-color: #fafafc;">' + numberWithCommas(parseInt(item.use_prepaid_cost)) + '원</td>' +
                            '<td style="background-color: #fafafc;">' + sales_count + '개</td>' +
                            '<td style="background-color: #fafafc;">' + numberWithCommas(item.total_money + item.total_card + item.total_other_cost + item.total_gift_cost) + '원</td>' +
                            '<td style="background-color: #fafafc;">' + numberWithCommas(item.total_miss_cost) + '원</td>' +
                            '<td style="background-color: #fafafc;" name="btn"><button onclick="detail1(this)">상세</button></td>' +
                            '<td style="background-color: #fafafc;"></td>';
                    }
                    tr.innerHTML = td;
                    tbody.append(tr);
                    $(tr).attr('data-value', item.forDate);
                });
            }
        }, 'json').fail(function (error) {
            alert(error.status)
        });
        // });
    }

    // $('#tab-4').click(function () {
    // 선불권 구매 이력
    function tab_active3(keep) {
        var tbody = $('#date03').find('tbody');
        tbody.empty();
        data["type"] = 1;
        // $('#target').val("#tab-4");
        if(keep == 'noKeep') data.pageNo = 1;
        $('#target').val("tab_active3");
        $.post('selectServicesTypeHistory', data, function (json) {
            setPagingVal(json.paging);
            delete json.paging;
            var list = json.salesList;
            if (list.length == 0) tbody.append('<tr><td colspan="8">선불권 구매 내역이 없습니다</td></tr>');
            else {
                list.forEach(function (item) {
                    var resource = JSON.parse(item.sale_resource)[0];
                    var resource1 = JSON.parse(item.sale_resource);
                    if(item.status == 4){
                        var eliment_tags = "";
                        $.each(resource1, function(i, v){
                            // console.log(JSON.stringify(v));
                            var tr = "<tr style='background-color: #fafafc;'>";
                            var changeCost = "";
                            var changeDate = "";

                            if(parseInt(v.changeCost) < 0) changeCost = comma(Math.abs(v.changeCost)) + '원 차감';
                            else changeCost = comma(v.changeCost) + '원 추가';

                            if(parseInt(v.changeDate) < 0) changeDate = comma(Math.abs(v.changeDate)) + '일 감소';
                            else changeDate = comma(v.changeDate) + '일 추가';

                            // var td = '<td>' + item.forDate.split(' ')[0] + '</td>' +
                            var td = '<td>' + item.forDate + '</td>' +
                                '<td>' + changeCost + '</td>' +
                                '<td>-</td>' +
                                '<td>' + v.name + '</td>' +
                                '<td>-</td>' +
                                '<td>-</td>' +
                                '<td>' + changeDate + '</td>' +
                                '<td></td>';
                            tr += td + "</tr>";
                            eliment_tags += tr;
                        });
                        tbody.append(eliment_tags);
                    }
                    else if (item.status == 5){
                        var eliment_tags = "";
                        $.each(resource1, function(i, v){
                            var tr = "<tr style='background-color: #fafafc;'>";

                            // var td = '<td>' + item.forDate.split(' ')[0] + '</td>' +
                            var td = '<td>' + item.forDate + '</td>' +
                                '<td> 선불권 삭제 </td>' +
                                '<td>-</td>' +
                                '<td>' + v.name + '</td>' +
                                '<td>-</td>' +
                                '<td>-</td>' +
                                '<td>' + v.validity + '</td>' +
                                '<td></td>';
                            tr += td + "</tr>";
                            eliment_tags += tr;
                        });
                        tbody.append(eliment_tags);
                    } else {
                        var empl = resource.empl1_name;
                        if (resource.empl2_idx != null) empl += ', ' + resource.empl2_name;
                        var tr = document.createElement("tr");
                        if (item.refund_status == 0) $(tr).addClass('refund-data');
                        var td = '<td>' + item.forDate.split(' ')[0] + '</td>' +
                            '<td>' + comma(uk_noshow(item.total_cost)) + '원</td>' +
                            '<td>' + empl + '</td>' +
                            '<td>' + resource.services_name + '</td>' +
                            '<td>' + comma(uk_noshow(item.accumulate)) + '원</td>' +
                            '<td>' + comma(uk_noshow(item.total_miss_cost)) + '원</td>' +
                            '<td>' + item.validity + '</td>' +
                            '<td name="btn"><button data-value="' + item.idx + '"  onclick="otherDetail(this)">상세</button></td>';
                        tr.innerHTML = td;
                        tbody.append(tr);
                        $(tr).attr('data-value', item.forDate);
                    }
                });
            }
        }, 'json').fail(function (error) {
            alert(error.status)
        });
        // });
    }
    // $('#tab-3').click(function () {
    // 티켓 구매 이력
    function tab_active4(keep) {
        var tbody = $('#date04').find('tbody');
        tbody.empty();
        data["type"] = 2;
        // $('#target').val("#tab-3");
        if(keep == 'noKeep') data.pageNo = 1;
        $('#target').val("tab_active4");
        $.post('selectServicesTypeHistory', data, function (json) {
            setPagingVal(json.paging);
            delete json.paging;
            var list = json.salesList;
            if (list.length == 0) tbody.append('<tr><td colspan="8">티켓 구매 내역이 없습니다</td></tr>');
            else {
                list.forEach(function (item) {
                    var resource = JSON.parse(item.sale_resource)[0];
                    var resource1 = JSON.parse(item.sale_resource);

                    // 수정
                    if(item.status == 4){
                        var eliment_tags = "";
                        $.each(resource1, function(i, v){
                            var tr = "<tr style='background-color: #fafafc;'>";
                            var changeCost = "";
                            var changeDate = "";

                            if(parseInt(v.changeCost) < 0) changeCost = comma(Math.abs(v.changeCost)) + '회 차감';
                            else changeCost = comma(v.changeCost) + '회 추가';

                            if(parseInt(v.changeDate) < 0) changeDate = comma(Math.abs(v.changeDate)) + '일 감소';
                            else changeDate = comma(v.changeDate) + '일 추가';

                            var td = '<td>' + item.forDate.split(' ')[0] + '</td>' +
                                '<td>' + changeCost + '</td>' +
                                '<td>-</td>' +
                                '<td>' + v.name + '</td>' +
                                '<td>-</td>' +
                                '<td>-</td>' +
                                '<td>' + changeDate + '</td>' +
                                '<td></td>';
                            tr += td + "</tr>";
                            eliment_tags += tr;
                        });
                        tbody.append(eliment_tags);
                    }
                    // 삭제
                    else if(item.status == 5){
                        var eliment_tags = "";
                        $.each(resource1, function(i, v){
                            var tr = "<tr style='background-color: #fafafc;'>";

                            var td = '<td>' + item.forDate.split(' ')[0] + '</td>' +
                                '<td> 티켓 삭제 </td>' +
                                '<td>-</td>' +
                                '<td>' + v.name + '</td>' +
                                '<td>-</td>' +
                                '<td>-</td>' +
                                '<td>' + v.validity + '</td>' +
                                '<td></td>';
                            tr += td + "</tr>";
                            eliment_tags += tr;
                        });
                        tbody.append(eliment_tags);
                    }
                    else {
                        var tr = document.createElement("tr");
                        var empl = resource.empl1_name;
                        if (resource.empl2_idx != null) empl += ', ' + resource.empl2_name;
                        var td = '<td>' + item.forDate.split(' ')[0] + '</td>' +
                            '<td>' + numberWithCommas(parseInt(item.total_sales_fee)) + '원</td>' +
                            '<td>' + empl + '</td>' +
                            '<td>' + resource.services_name + '</td>' +
                            '<td>' + item.accumulate + ' 회</td>' +
                            '<td>' + numberWithCommas(item.total_miss_cost) + '원</td>' +
                            '<td>' + item.validity + '</td>' +
                            '<td name="btn"><button data-value="' + item.idx + '" onclick="otherDetail(this)">상세</button></td>';
                        tr.innerHTML = td;
                        tbody.append(tr);
                        $(tr).attr('data-value', item.forDate);
                    }
                    if (item.status == 1) $(tr).addClass("refund-data");
                });
            }
        }, 'json').fail(function (error) {
            alert(error.status)
        });
        // });
    }

    // $('#tab-5').click(function () {
    function tab_active5(keep) {
        var tbody = $('#date05').find('tbody');
        tbody.empty();
        // $('#target').val("#tab-5");
        if(keep == 'noKeep') data.pageNo = 1;
        $('#target').val("tab_active5");
        data["type"] = 3;
        $.post('selectServicesTypeHistory', data, function (json) {
            setPagingVal(json.paging);
            delete json.paging;
            var list = json.salesList;
            if (list.length == 0) tbody.append('<tr><td colspan="8">제품 구매 내역이 없습니다</td></tr>');
            else {
                list.forEach(function (item) {
                    var sales_fee = 0, prepaid_cost = 0, sales_count = JSON.parse(item.sale_resource).length;
                    JSON.parse(item.sale_resource).forEach(function (sale) {
                        sales_fee += parseInt(sale.sales_fee)
                    });
                    if (item.prepaid != null) JSON.parse(item.prepaid).forEach(function (prepaid) {
                        prepaid_cost += parseInt(prepaid.cost);
                    });
                    var tr = document.createElement("tr");
                    var td = '<td>' + item.forDate.split(' ')[0] + '</td>' +
                        '<td>' + numberWithCommas(parseInt(item.total_sales_fee)) + '원</td>' +
                        '<td>' + numberWithCommas(parseInt(item.use_ticket_count)) + ' 회</td>' +
                        '<td>' + numberWithCommas(parseInt(item.use_prepaid_cost)) + '원</td>' +
                        '<td>' + sales_count + '개</td>' +
                        '<td>' + numberWithCommas(item.total_money + item.total_card + item.total_other_cost + item.total_gift_cost) + '원</td>' +
                        '<td>' + numberWithCommas(item.total_miss_cost) + '원</td>' +
                        '<td name="btn"><button onclick="detail1(this)">상세</button></td>';
                    tr.innerHTML = td;
                    tbody.append(tr);
                    $(tr).attr('data-value', item.forDate);
                });
            }
        }, 'json').fail(function (error) {
            alert(error.status)
        });
        // });
    }

    // $('#tab-6').click(function () {
    function tab_active6 (keep) {
        var tbody = $('#date06').find('tbody');
        tbody.empty();
        // $('#target').val("#tab-6");
        if(keep == 'noKeep') data.pageNo = 1;
        $('#target').val("tab_active6");
        data["type"] = -1;
        $.post('selectServicesTypeHistory', data, function (json) {
            setPagingVal(json.paging);
            delete json.paging;
            var list = json.salesList;
            if (list.length == 0) tbody.append('<tr><td colspan="6">미수금 발생 내역이 없습니다</td></tr>');
            else {
                list.forEach(function (item) {
                    var sales_fee = 0, prepaid_cost = 0, sales_count = JSON.parse(item.sale_resource).length;
                    JSON.parse(item.sale_resource).forEach(function (sale) {
                        sales_fee += parseInt(sale.sales_fee)
                    });
                    if (item.prepaid != null) JSON.parse(item.prepaid).forEach(function (prepaid) {
                        prepaid_cost += parseInt(prepaid.cost);
                    });
                    var tr = document.createElement("tr");
                    var service = item.services_type;
                    var func = '';
                    if (service == 0) {
                        type = '시술';
                        func = 'onclick="simpleDetail(this)"';
                    }
                    else if (service == 1) {
                        type = '선불권';
                        func = 'onclick="otherDetail(this)"';
                    }
                    else if (service == 2) {
                        type = '티켓';
                        func = 'onclick="otherDetail(this)"';
                    }
                    else if (service == 3) {
                        type = '제품';
                        func = 'onclick="simpleDetail(this)"';
                    }
                    var btn = '<button data-value="' + item.idx + '" ' + func + '>상세</button>';
                    var td = '<td>' + item.forDate.split(' ')[0] + '</td>' +
                        '<td>' + type + '</td>' +
                        '<td>' + sales_count + '개</td>' +
                        '<td>' + numberWithCommas(parseInt(item.total_sales_fee)) + '원</td>' +
                        '<td>' + numberWithCommas(item.total_miss_cost) + '원</td>' +
                        '<td name="btn">' + btn + '</td>';
                    tr.innerHTML = td;
                    tbody.append(tr);
                    $(tr).attr('data-value', item.forDate);
                });
            }
        }, 'json').fail(function (error) {
            alert(error.status)
        });
        // });
    }
</script>
<%-- 시술 내역 삭제 --%>
<script>
    function historyDelete(obj){
        obj = $(obj);
        var conf = confirm("시술 내역을 삭제하시겠습니까?\n삭제된 정보는 복구가 불가능하며 사용된 선불권, 횟수권은 다시 복구됩니다.");

        if(!conf) return false;
        var sale_idx = obj.closest("tr").find("input[type=hidden][name=sale_idx]").val();

        var data = submitAjax("/Sales/cleaningSalesRow", {sale_idx : sale_idx});
        if(data.code == 200){
            alert("해당 시술이 판매 취소되었습니다. \n사용된 선불권/횟수권이 다시 복구되었습니다.");
            obj.closest("tr").css("background-color", "#fafafc");
            obj.remove();
        } else {
            alert("새로고침을 하신 후 다시 시도해 주시요.");
        }
    }
</script>

<%-- AJAX Loading--%>
<%@include file="../../include/utils/ajaxLoading.jspf" %>
</body>
</html>