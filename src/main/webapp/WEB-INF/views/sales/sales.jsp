<!DOCTYPE html>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../../include/include-header.jspf" %>
    <%@include file="../../include/css/include-stylesheet.jspf" %>
    <%@include file="../../include/utils/include-bootstrapmenu.jspf" %>
    <%@include file="../../include/utils/include-datepicker.jspf" %>
</head>

<body class="on-body">
<%@include file="../../include/include-header-view.jspf" %>

<div class="page-name">
    <div style="width: 1300px;margin: 0 auto;padding: 0 15px">
        <h3>결제</h3>
        <p>시술 및 제품을 선택 후 금액을 입력후에 매출등록을 하세요.</p>
    </div>
</div>
<%--${rMap}--%>
<div class="container mb-100 mh-764" style="width: 1300px">
    <div id="dialog-background"></div>
    <input type="hidden" id="reload-resource-payment" value="${payment}">
    <input type="hidden" id="reload-resource-fordate" value="${forDate}">
    <input type="hidden" id="reload-resource-client-idx" value="${client_idx}">


    <div class="pay-day">
        <c:set var="now" value="<%=new java.util.Date()%>"/>
        <c:set var="sysDate"><fmt:formatDate value="${now}" pattern="yyyy-MM-dd"/></c:set>
        <c:set var="sysHour"><fmt:formatDate value="${now}" pattern="HH"/></c:set>
        <c:set var="sysMinute"><fmt:formatDate value="${now}" pattern="mm"/></c:set>
        <table>
            <tr>
                <td width="30%">
                    <input type="text" id="sales_date" value="${sysDate}">
                </td>
                <td width="20%">
                    <input type="text" id="hour" value="${sysHour}" min="0" max="24" maxlength="2" oninput="maxLengthCheck(this)" onkeydown="onlyNumber(this)"> <span>시</span>
                </td>
                <td width="20%">
                    <input type="text" id="minute" value="${sysMinute}" min="0" max="" maxlength="2" oninput="maxLengthCheck(this)" onkeydown="onlyNumber(this)"> <span>분</span>
                </td>
            </tr>
        </table>
    </div>
    <div>
    <div class="top-user-information">
        <div class="user-name">
            <h4>${rMap.client_name}</h4>
            <p>
                <c:if test="${rMap.client_phone ne '' && not empty rMap.client_phone}">
                    <c:if test="${fn:length(rMap.client_phone) eq 11}">
                        <c:set var="num1" value="${fn:substring(rMap.client_phone,0,3)}"/>
                        <c:set var="num2" value="${fn:substring(rMap.client_phone,3,7)}"/>
                        <c:set var="num3" value="${fn:substring(rMap.client_phone,7,11)}"/>
                        ${num1}-${num2}-${num3}
                    </c:if>
                    <c:if test="${fn:length(rMap.client_phone) ne 11}">
                        <c:set var="num1" value="${fn:substring(rMap.client_phone,0,3)}"/>
                        <c:set var="num2" value="${fn:substring(rMap.client_phone,3,6)}"/>
                        <c:set var="num3" value="${fn:substring(rMap.client_phone,6,10)}"/>
                        ${num1}-${num2}-${num3}
                    </c:if>
                </c:if>
                <c:if test="${rMap.client_phone eq '' || empty rMap.client_phone}">-</c:if>
            </p>
        </div>
        <ul>
            <li>
                <h4>등급</h4>
                <p>
                    <c:if test="${client_type eq 'member'}">
                        <c:if test="${rMap.client_rank eq ''}">-</c:if>
                        <c:if test="${rMap.client_rank ne ''}">${rMap.client_rank}</c:if>
                    </c:if>
                    <c:if test="${client_type eq 'no_member'}">미등록 회원</c:if>
                </p>
            </li>
            <li>
                <h4>보유 회원권</h4>
                <p id="has_prepaid_cost">${rMap.client_prepaid_point}</p>
                <p>
                <small>원</small>
                </p>
            </li>
            <li>
                <h4>보유 포인트</h4>
                <p id="has_point_cost"><fmt:formatNumber value="${rMap.client_point}" pattern="#,###"/>
                    <small>원</small>
                </p>
            </li>
            <li style="color: #fa5b4a;
    font-weight: 500;">
                <h4>미수금</h4>
                <p id="has_miss_cost"><fmt:formatNumber value="${rMap.client_miss_cost}" pattern="#,###"/>
                    <small>원</small>
                </p>
            </li>
        </ul>
        <c:if test="${rMap.client_idx ne 0}">
            <div>
                <textarea name="this_client_memo" id="this_client_memo">${rMap.memo}</textarea>
                <button type="button" class="memo-edite-btn" onclick="saleMemoUpdate()">메모수정</button>
            </div>
            <div>
                <button type="button" class="" data-toggle="modal" onclick="callClientPayStatInfo()" data-target="#ex01" style="background: #fa5b4a;
    color: #fff;
    border: 1px solid #fc4535;">미수 처리</button>
                <%--<button type="button" class="" data-toggle="modal" onclick="callHasPrepaidTicket()" data-target="#ex02">환불</button>--%>
            </div>
        </c:if>
    </div>
    <div class="pay-body">
        <div class="pay-service">
            <c:if test="${client_type eq 'member'}">
                <!-- 정액/선불권 모달 -->
                <button class="add-btn" id="prepayment_modal_trigger">
                    <i class="fas fa-ticket-alt"></i> 회원권 판매
                </button>
                <%@include file="../../include/modals/sales/prepaymentSalesModal.jspf" %>

                <!-- 티켓/패키지 모달 -->
                <button class="add-btn" id="ticket_modal_trigger">
                    <i class="fas fa-ticket-alt"></i> 횟수권 판매
                </button>
                <%@include file="../../include/modals/sales/ticketSalesModal.jspf" %>
            </c:if>
        </div>


        <%-- New Sale Ver2 --%>
        <div class="nav-tabs-custom">
            <ul class="nav nav-tabs">
                <li class="active"><a href="#service-tab" data-toggle="tab" aria-expanded="false">시술</a></li>
                <li class=""><a href="#product-tab" data-toggle="tab" aria-expanded="true">제품</a></li>
            </ul>
            <div class="tab-content">
                <%-- 시술 탭--%>
                <div class="tab-pane active" id="service-tab">
                    <%@include file="../../include/modals/sales/service/service-tab.jspf" %>
                </div>
                <%-- /.시술 탭--%>
                <%-- 제품 탭 --%>
                <div class="tab-pane" id="product-tab">
                    <%@include file="../../include/modals/sales/product/product-tab.jspf" %>
                </div>
                <%-- /.제품 탭 --%>
            </div>
            <button type="button" data-toggle="modal" <%--data-target="#last-pay-modal"--%> onclick="finalModalShow()">매출등록</button>
        </div>
        <%-- /.New Sale Ver2 --%>

    </div>
    </div>
</div>

<%@include file="../../include/include-menu-footer.jspf" %>

<%-- service jspf --%>
<%@include file="../../include/modals/sales/service/dc.jspf"%>                <%-- DC Modal--%>
<%@include file="../../include/modals/sales/service/prepaid-choice.jspf"%>    <%-- Prepaid Choice Modal --%>
<%@include file="../../include/modals/sales/service/ticket-choice.jspf"%>     <%-- Ticket Choice Modal --%>
<%@include file="../../include/modals/sales/service/pay-method-modal.jspf"%>  <%-- Pay Method --%>
<%-- /.service jspf --%>
<%-- prodcut jspf --%>
<%@include file="../../include/modals/sales/product/dc.jspf"%>                <%-- DC Modal--%>
<%@include file="../../include/modals/sales/product/prepaid-choice.jspf"%>    <%-- Prepaid Choice Modal --%>
<%@include file="../../include/modals/sales/product/ticket-choice.jspf"%>     <%-- Ticket Choice Modal --%>
<%@include file="../../include/modals/sales/product/pay-method-modal.jspf"%>  <%-- Pay Method --%>
<%--최종 결제 모달--%>
<%@include file="../../include/modals/sales/finalSettlement.jspf"%>
<!-- 미수금 모달 -->
<%@include file="../../include/modals/sales/missCostCalculate.jspf"%>
<!-- 환불 모달 -->
<%@include file="../../include/modals/sales/refundCostCalculate.jspf"%>

<script src="/static/dist/js/utils.js"></script>
<%-- script 전역 --%>
<script>
    var pMapReady = {"prepaid" : ""};
</script>
<%-- Cannot read property 'prepaid' of undefined Debug --%>
<script>
    $(document).ready(function (){
        client_idx = '${rMap.client_idx}';
        makeClientPrepaidList(false);
        // console.log("ready prepaid : " + pMapReady.prepaid);
    });
</script>

<%-- Ver1 --%>
<script>
    var forDate = '${forDate}';

    var ctx = "${pageContext.request.contextPath}";
    console.log("ctx :  "+ ctx);
    var point_select_flag = false;
    var status_membership;
    var salesCalculationFlag = false;
    /* 회원 정보 */
    var client_point = '${rMap.client_point}';
    var res_idx = '${rMap.res_idx}';
    /* 회원권 */
    var prepayment_cate, prepayment_detail, client_idx, client_has_prepaid;
    var sum_prepaid_falg = true;
    var prepayment_hass_client = new Array();
    var empl2_flag = false;
    var membership_flag = false;
    /* 횟수권 */
    var ticket_cate, ticket_detail;
    var sum_ticket_falg = true;
    var ticket_hass_client = new Array();

    /* 선불권/횟수권 백업 객체 */
    var pre_backup_array = [];
    var ticket_backup_array = [];

    $(window).on('load', function () {
        /* ver1 */
        status_membership = '${rMap.status_membership}';
        if('${rMap.client_idx}' != '0') client_idx = '${rMap.client_idx}';
        if('${rMap.client_point}' == '') client_point = 0;
        else client_point = '${rMap.client_point}';
        if (status_membership == '0') membership_flag = true;
        else membership_flag = false;
        /* ver 2*/
        /* service */
        serviceEmplMack();
        prepaidMake();
        ticketMake();
        emplMake();
        /* /.service */
        /* product */
        productPrepaidMake();
        productTicketMake();
        /* /.product */
    });
</script>
<%-- Ver2 --%>
<script>
    var service_cate, service_detail;                                                       // 시술
    var product_cate, product_detail;                                                       // 제품
    var pre_map = jQuery.parseJSON('${rMap.prepaid_map}');                  // 회원권
    var prepaid_btn_object, pre_backup_cost;
    var pre_backup_one = 0;
    var ticket_map = jQuery.parseJSON('${rMap.ticket_map}');                // 횟수권
    var ticket_btn_object;
    var empl_list = jQuery.parseJSON('${eList}');                           // 직원
    var product_empl_list = jQuery.parseJSON('${eList}');
    var dc_type_all = false;                                                // 할인
    var dc_object, product_dc_object;
    var product_dc_type_all = false;
    var pay_type;                                                           // 계산기
</script>

<%-- 회원권 / 횟수권 판매 --%>
<script>
    /* 정액 / 선불권 */
    $(document).on('click', '#prepayment_modal_trigger', function () {
        $('.sale-btn').hide();
        $('.card-btn').hide();
        $('.gift-btn').hide();

        $('#total_cost_by_fee').val('0');
        $('#point-cost').text('0');
        $('#prepaid_point_cost_text').text('0');
        $('#prepaid_point_text').val('0');
        $('.money-btn').hide();
        $('.other-btn').hide();
        $('.point-btn').hide();
        $('#prepayment_list_box').empty();
        $('#right_sum_cost').text('0');
        $('#right_total_pay_cost').text('0');
        $('#prepaid_employee_two_cost').hide();
        $('#miss_cost_text').text('0');
        $('#prepaid_employee_one_cost').val('0');

        var empl_idx = '${rMap.empl_idx}';
        var empl_name = '${rMap.empl_name}';

        makeClientPrepaidList(true);
        $.ajax({
            url: "/Sales/callPrepaymentList",
            type: "post",
            dataType: "json",
            async: false,
            success: function (data) {
                if (data.code == 200) {
                    makePrepaymentTable(data, empl_idx, empl_name);
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
        $('#sales-prepayment').modal();
        $('#merge-chk1').prop('checked', false);
        $('#chk-div1').hide();
        $('#prepayment_pay_empl_list_box2').closest('div').find('.nice-select').hide();
        // $('#prepaid_pay_btn-contnet1').hide();
        // $('#prepaid_pay_btn-contnet2').hide();
        $('#employeePlus').show();
        $('#employeeMinus').hide();
        // $('#prepaid_div2').hide();

    });
    /* 티켓 */
    $(document).on('click', '#ticket_modal_trigger', function () {
        var empl_idx = '${rMap.empl_idx}';
        var empl_name = '${rMap.empl_name}';
        $('#ticket_list_box').empty();
        $.ajax({
            url: "/Sales/callTicketList",
            type: "post",
            dataType: "json",
            async: false,
            success: function (data) {
                if (data.code == 200) {
                    makeTicketTableModal(data, empl_idx, empl_name);
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
        $('#sales-ticket').modal();
        $('#merge-chk').prop('checked', false);
        $('#chk-div').hide();
        $('#ticket_sale_text').val('0');
        $('.ticket-sale-btn').hide();
        $('.ticket-pay-btn').hide();
        $('.ticket-card-btn').hide();
        $('.ticket-gift-btn').hide();
        $('.ticket-other-btn').hide();
        $('.ticket-point-btn').hide();
        $('#ticket_pay_empl_list_box2').closest('div').find('.nice-select').hide();
        $('#ticket-employeePlus').show();
        $('#ticket-employeeMinus').hide();
        $('#ticket_right_total_pay_cost').text(comma(0));

        $('#ticket_point_cost_text').text('0');
        $('#ticket-sale-cost').text('0');
        $('#ticket-pay-cost').text('0');
        $('#ticket-card-cost').text('0');
        $('#ticket-gift-cost').text('0');
        $('#ticket-other-cost').text('0');
        $('#ticket-point-cost').text('0');
        $('#ticket_miss_cost_text').text('0');

    });
</script>

<%-- 미수금 --%>
<script>
    function callClientPayStatInfo(){
        $.ajax({
            url: "/Client/callPrepaidAndPointCost",
            type: "post",
            dataType: "json",
            data: {
                "client_idx" : client_idx
            },
            async : false,
            success: function (data) {
                if (data.code == 200) {
                    $('#miss-cost-modal-miss-cost').text(comma(data.clientMap.miss_cost));
                    $('#pay-the-misscost').val(comma(data.clientMap.miss_cost));
                } else {
                    location.href = document.URL;
                }
            },
            error: function () {
                alert("에러가 발생했습니다.");
                location.href = document.URL;
            }
        });
    }
</script>
<%-- 메모 업데이트 --%>
<script>
    function saleMemoUpdate(){
        var memo = $('#this_client_memo').val();
        var tmp = {
            client_idx : client_idx,
            memo : memo
        };
        var data = submitAjax("/Client/clientMemoUpdate", tmp);
        if(data.code == 200){
            alert("고객 메모가 수정되었습니다.");
        } else {
            location.href = document.URL;
        }
    }
</script>

<script src="${pageContext.request.contextPath}/static/dist/js/sales/prepaymentMakeTable.js"></script>      <%-- 회원권 판매 --%>
<script src="${pageContext.request.contextPath}/static/dist/js/sales/ticketMakeTable.js"></script>          <%-- 횟수권 판매 --%>
<%-- ver 2 --%>
<%-- service js --%>
<script src="${pageContext.request.contextPath}/static/dist/js/sales/service/salesTest-Calculation.js"></script>
<script src="${pageContext.request.contextPath}/static/dist/js/sales/service/salesTest-Employee.js"></script>
<script src="${pageContext.request.contextPath}/static/dist/js/sales/service/salesTest-Prepaid.js"></script>
<script src="${pageContext.request.contextPath}/static/dist/js/sales/service/salesTest-Service.js"></script>
<script src="${pageContext.request.contextPath}/static/dist/js/sales/service/salesTest-Ticket.js"></script>
<script src="${pageContext.request.contextPath}/static/dist/js/sales/service/subJS/salesTest-Service-dc.js"></script>
<%-- /.service js --%>
<%-- product js --%>
<script src="${pageContext.request.contextPath}/static/dist/js/sales/product/salesTest-product-Calculation.js"></script>
<script src="${pageContext.request.contextPath}/static/dist/js/sales/product/salesTest-product-Employee.js"></script>
<script src="${pageContext.request.contextPath}/static/dist/js/sales/product/salesTest-product-Prepaid.js"></script>
<script src="${pageContext.request.contextPath}/static/dist/js/sales/product/salesTest-product-Product.js"></script>
<script src="${pageContext.request.contextPath}/static/dist/js/sales/product/salesTest-product-Ticket.js"></script>
<script src="${pageContext.request.contextPath}/static/dist/js/sales/product/subJS/salesTest-Product-dc.js"></script>

<script src="${pageContext.request.contextPath}/static/dist/js/sales/finalSettlement.js"></script>
<script src="${pageContext.request.contextPath}/static/dist/js/sales/normalSalesAction.js"></script>
<%-- /.product js --%>
<%-- /.ver 2 --%>

<%-- AJAX Loading--%>
<%@include file="../../include/utils/ajaxLoading.jspf" %>
</body>
</html>