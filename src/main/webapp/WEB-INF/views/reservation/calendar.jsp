<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="../../include/include-header.jspf" %>
    <%@include file="../../include/css/include-stylesheet.jspf" %>
    <%--<%@include file="../../include/utils/include-bootstrapmenu.jspf" %>--%>
    <%@include file="../../include/utils/include-datepicker.jspf" %>
    <link rel="stylesheet" href="/static/dist/js/chat/chat.css">
    <style>
        .reservation-empty-td {}
        .employee-noschedule {background:hsla(0,0%,97.3%,.85)}
        .employee-noemployee {background:hsla(0,0%,93.3%,.85)}
        .ui-resizable-s {cursor: s-resize;height: 3px;width: 100%;bottom: 0;left: 0;background: #d9e6e8;z-index: 0 !important;}
        .reservation-calculation-complete {}
        .ui-resizable-helper {border: 1.2px dashed rgba(0,0,0,0.25);border-radius: 3px;z-index: 0 !important;display: inline-block;width: 116px !important;}
        .ui-resizable-ghost {color: transparent !important;z-index: 0 !important;width: 116px !important;height: 100%}
        .reservation-td.ui-draggable-dragging {color: transparent !important; height: 25px !important; width: 25px !important;border-radius: 50%;border: 2px solid #ffffff}
        .reservation-td.ui-draggable-dragging i {display: none !important;}
        .reservation-td.ui-draggable-dragging div {display: none}
    </style>
    <%-- 직원 없을 경우 --%>
    <script>
        $(document).ready(function () {
            $(".popupLayer1").mouseleave (function() {
                $(".popupLayer1").hide();
            });
            $(".popupLayer2").mouseleave (function() {
                $(".popupLayer2").hide();
            });
            $(".popupLayer3").mouseleave (function() {
                $(".popupLayer3").hide();
            });
            var emplSize = '${employeeSize}';
            if (emplSize < 1) {
                alert("등록된 직원이 없습니다.\n직원을 등록해 주세요");
                location.href = '${pageContext.request.contextPath}/Employee/employeeList';
            }
        });
    </script>
</head>
<body style="padding: 0px" class="on-body">
<%@include file="../../include/include-header-view.jspf"%>

<c:set var="open" value="${fn:substring(shopInfo.shop_open, 0, 2)}"/>
<c:set var="close" value="${fn:substring(shopInfo.shop_close, 0, 2)}"/>
<%--${forDate}--%>
<%--<div class="back-background" style="box-shadow: none"></div>--%>
<div class="container m-container" style="width: 1300px;">

    <%--캘린더 공지사항 폼--%>
    <%--<div class="director-notice">--%>
    <%--<div class="director-notice-list">--%>
    <%--<div>--%>
    <%--<span>[공지] ${shopInfo.manager_notice}</span>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--<span>공지</span>--%>
    <%--&lt;%&ndash;<div class="all-director-notice">&ndash;%&gt;--%>
    <%--&lt;%&ndash;<a>모두보기 <i class="fas fa-chevron-right"></i></a>&ndash;%&gt;--%>
    <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
    <%--</div>--%>
    <%--캘린더 공지사항 폼 끝--%>


    <input type="hidden" id="call_result_code" value="${call_result_code}">
    <input type="hidden" id="call_res_data" value="${call_res_data}">
    <div class="calendar-content-left col-md-3">
        <p class="pc-none">달력</p>
        <%--데이트 픽커--%>
        <div id="forDate"><button type="button" class="close pc-none datapick-close" onclick="location.href = '/Reservation/calendar'" style="position: absolute;
    top: 1%;
    right: 2%;"><span aria-hidden="true">&times;</span></button></div>

        <div class="promotion-content">
            <c:choose>
                <c:when test="${not empty promotion}">
                    <div class="promotion-top" style="min-height: auto;">
                        <h4>${promotion.title}</h4>
                        <span style="display: none">
                            <c:if test="${promotion.achievement_value ge promotion.target_value}">달성</c:if>
                            <c:if test="${promotion.achievement_value lt promotion.target_value}">진행중</c:if>
                        </span>
                    </div>
                    <div class="promotion-body" style="min-height: auto;">
                        <table>
                            <thead>
                            <tr>
                                <th width="50%">목표</th>
                                <th width="50%">달성</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td><fmt:formatNumber value="${promotion.target_value}" type="number"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${promotion.value_type eq 0}">
                                            <fmt:formatNumber value="${promotion.achievement_count}" type="number"/>
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:formatNumber value="${promotion.achievement_cost}" type="number"/>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <ul>
                            <li>- ${promotion.name}</li>
                            <li>- ${promotion.memo}</li>
                        </ul>
                    </div>
                </c:when>
                <c:otherwise>
                    <div>
                        <h4 style="color: #32536a;font-size: 16px;font-weight: normal;text-align: center;margin-bottom: 5px;">프로모션</h4>
                        <div class="promotion-none-form">
                            <p>등록된 프로모션이 없습니다.</p>
                            <p>프로모션 설정 > 프로모션 등록</p>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        <button class="promotion-setting-btn" type="button" data-toggle="modal" data-target="#promotionModal">프로모션 설정</button>
    </div>

    <%-- 매장 채팅 --%>
    <aside class="calendar-content-right sidebar" data-value="${forDate}">
        <button class="chat-open-btn"><%--<i class="fas fa-bell"></i>--%><%--<span class="sidebar-btn">팅톡</span>--%></button>
        <div class="meta-items">
            <div class="right-top-content">
                <h3>${shopInfo.com_name}</h3>
                <div><p id="remainPoint">문자포인트 : </p></div>
                <c:if test="${shopInfo.state eq 1}"><button class="shop-open-close" type="button" onclick="shopOnOff(this)">오픈문자 발송</button></c:if>
                <c:if test="${shopInfo.state eq 0}"><button class="shop-open-close" type="button" onclick="shopOnOff(this)">마감문자 발송</button></c:if>
            </div>
            <div class="item">전체<b>0</b></div>
            <div class="item">완료<b>0</b></div>
            <div class="item">노쇼<b>0</b></div>
        </div>
        <div class="sidebar-content">
            <div class="chat-top-name">
                <i class="far fa-star"></i>
                <h3>공지사항</h3>
            </div>
            <div class="important_msg">
                <ul id="chat_notice"></ul>
            </div>
            <div class="chat-top-name" style="margin-top: 10px">
                <h3>TODAY 채팅</h3>
            </div>
            <div class="chat_div">
                <ul id="chat_ul" data-value="${shopInfo.idx}"></ul>
            </div>
            <div class="chat_bottom">
                <div class="chat_input">
                    <input type="text" class="mytext">
                    <button type="button" id="send_btn" class="chat_btn"><i class="fas fa-paper-plane"></i></button>
                </div>
                <div class="chat_important">
                    <%--<input type="checkbox" name="status" value="0" id="s"><label for="s"><i class="fas fa-star"></i></label>--%>
                    <div class="btn-group-toggle" data-toggle="buttons">
                        <label class="btn btn-secondary" for="s" id="s-parent-label">
                            <input type="checkbox" name="status" value="0" id="s"><i class="far fa-star"></i>
                        </label>
                    </div>
                </div>
            </div>
        </div>
    </aside>
    <%-- 예약 테이블 --%>
    <div class="col-md-9 m-calendar" style="width: 1010px;
    display: inline-block;
    padding: 96px 0 50px 30px;
    border-left: 1px solid #eee;
    margin-left: 20px;
    position: relative;">
        <button class="calendar_registration_btn" type="button" id="insertClient" onclick="add_client()"><i class="fas fa-check pc-none" style=""></i>&nbsp;</span>고객등록</button>
        <div class="calendar_right_top">
            <div class="calendar-top">
                <div class="calendar-bottom-color">
                    <ul class="m-none">
                        <c:if test="${sessionScope.shopInfo.service_type eq '네일'}">
                            <li><p class="service-hand"></p>손</li>
                            <li><p class="service-foot"></p>발</li>
                            <li><p class="service-eyebrow"></p>속눈썹</li>
                            <li><p class="service-waxing"></p>왁싱</li>
                            <li><p class="service-other"></p>기타</li>
                            <li><p class="service-end"></p>결제완료</li>
                        </c:if>
                        <c:if test="${sessionScope.shopInfo.service_type eq '반영구, 속눈썹'}">
                            <li><p class="service-hand"></p>눈썹</li>
                            <li><p class="service-foot"></p>입술</li>
                            <li><p class="service-eyebrow"></p>연장</li>
                            <li><p class="service-other"></p>기타</li>
                            <li><p class="service-end"></p>결제완료</li>
                        </c:if>
                        <c:if test="${sessionScope.shopInfo.service_type eq '메이크업'}">
                            <li><p class="service-hand"></p>기본</li>
                            <li><p class="service-foot"></p>결혼식</li>
                            <li><p class="service-eyebrow"></p>혼주</li>
                            <li><p class="service-waxing"></p>승무원</li>
                            <li><p class="service-other"></p>기타</li>
                            <li><p class="service-end"></p>결제완료</li>
                        </c:if>
                        <c:if test="${sessionScope.shopInfo.service_type eq '왁싱'}">
                            <li><p class="service-hand"></p>왁싱</li>
                            <li><p class="service-other"></p>기타</li>
                            <li><p class="service-end"></p>결제완료</li>
                        </c:if>
                        <c:if test="${sessionScope.shopInfo.service_type eq '마사지'}">
                            <li><p class="service-hand"></p>건식</li>
                            <li><p class="service-foot"></p>오일</li>
                            <li><p class="service-other"></p>기타</li>
                            <li><p class="service-end"></p>결제완료</li>
                        </c:if>
                        <c:if test="${sessionScope.shopInfo.service_type eq '스킨'}">
                            <li><p class="service-hand"></p>얼굴</li>
                            <li><p class="service-foot"></p>바디</li>
                            <li><p class="service-eyebrow"></p>세트</li>
                            <li><p class="service-other"></p>기타</li>
                            <li><p class="service-end"></p>결제완료</li>
                        </c:if>
                    </ul>
                </div>
            </div>
        </div>
        <table class="schedule-table-header">
            <thead>
            <tr>
                <td style="vertical-align: middle;width: 116px !important;">
                    <ul>
                        <li onclick="emplPageingPrev(this)" data-value="" id="empl_paging_prev"><i
                                class="fas fa-chevron-left"></i></li>
                        <li onclick="emplPageingNext(this)" data-value="" id="empl_paging_next"><i
                                class="fas fa-chevron-right"></i></li>
                    </ul>

                </td>
                <%--<c:forEach var="empl" begin="0" end="${employeeSize -1}">--%>
                <c:forEach var="empl" begin="0" end="6">
                    <c:if test="${(employeeSize-1) < empl}">
                        <td class="schedule-table-td">close</td>
                    </c:if>
                    <c:if test="${(employeeSize-1) >= empl}">
                        <c:set var="item" value="${employeeList[empl]}"/>
                        <td class="schedule-table-td">
                                ${fn:replace(item.name,"\"","")}
                            <%-----------------------잠시 제거함 start--------------------%>
                            <p><fmt:formatNumber value="${item.toDay_goal}" type="number"/>
                                (<fmt:formatNumber value="${item.day_percent}" pattern="#.##"/>%)</p>
                                    <%------------------------잠시제거됨 end----------------------%>
                            <p>Today <fmt:formatNumber value="${item.toDay_cost}" type="number"/></p>
                            <div class="schedule-table-hover">
                                <p>월 목표 <fmt:formatNumber value="${item.month_goal}" type="number"/></p>
                                <p>달성 금액 <fmt:formatNumber value="${item.month_cost}" type="number"/>
                                    (<fmt:formatNumber value="${item.month_percent}" pattern="#.##"/>%)</p>
                            </div>
                        </td>
                    </c:if>
                </c:forEach>
                <%--rList.get(i).put("toDay_goal", toDay_goal);
                rList.get(i).put("day_percent", day_percent);
                rList.get(i).put("month_percent", month_percent);--%>
            </tr>
            </thead>
        </table>
        <div style="height: 600px;overflow-y: auto;position: relative;border-bottom: 1px solid #dee2e6;">
            <table class="schedule-table" id="reservation_table" style="display: block; position: absolute">
                <tbody id="container" style="position: relative">
                <%-- 매장 시간 forEach --%>
                <c:forEach begin="${open}" end="${close -1}" step="1" var="time">
                    <%-- 00 ~ 30 --%>
                    <tr class="" style="border-top: 1px solid #bbbbbb">
                            <%-- 시간 rowspan 10/15 --%>
                        <c:if test="${time < 12}"><c:set var="AmPm" value="${time}:00 AM"/></c:if>
                        <c:if test="${time == 12}"><c:set var="AmPm" value="${time}:00 PM"/></c:if>
                        <c:if test="${time > 12}"><c:set var="AmPm" value="${time - 12}:00 PM"/></c:if>
                        <td class="first-td" width="114px" style="border-right: 1px solid #ddd">${AmPm}</td>
                            <%--<c:forEach var="empl" begin="0" end="${employeeSize -1}">--%>
                        <c:forEach var="empl" begin="0" end="6">
                            <c:if test="${(employeeSize-1) < empl}">
                                <td width="114px" class="employee-noemployee" style="border-right: 1px solid #ddd"></td>
                            </c:if>
                            <c:if test="${(employeeSize-1) >= empl}">
                                <td width="114px" class="reservation-empty-td" style="border-right: 1px solid #ddd"
                                    id="${time}:00:${employeeList[empl].idx}"></td>
                            </c:if>
                        </c:forEach>
                    </tr>
                    <tr class="" style="border-top: 1px solid #ddd">
                        <c:if test="${shopInfo.default_minute eq 10}">
                            <td style="border-right: 1px solid #dfe5eb">10</td>
                        </c:if>
                        <c:if test="${shopInfo.default_minute eq 15}">
                            <td style="border-right: 1px solid #ddd">15</td>
                        </c:if>
                        <c:forEach var="empl" begin="0" end="6">
                            <c:if test="${(employeeSize-1) < empl}">
                                <td class="employee-noemployee" style="border-right: 1px solid #ddd"></td>
                            </c:if>
                            <c:if test="${(employeeSize-1) >= empl}">
                                <c:if test="${shopInfo.default_minute eq 10}">
                                    <td class="reservation-empty-td" style="border-right: 1px solid #ddd"
                                        id="${time}:10:${employeeList[empl].idx}"></td>
                                </c:if>
                                <c:if test="${shopInfo.default_minute eq 15}">
                                    <td class="reservation-empty-td" style="border-right: 1px solid #ddd"
                                        id="${time}:15:${employeeList[empl].idx}"></td>
                                </c:if>
                            </c:if>
                        </c:forEach>
                    </tr>
                    <c:if test="${shopInfo.default_minute eq 10}">
                        <tr class="" style="border-top: 1px solid #ddd">
                            <td style="border-right: 1px solid #ddd">20</td>
                                <%--<c:forEach var="empl" begin="0" end="${employeeSize -1}">--%>
                            <c:forEach var="empl" begin="0" end="6">
                                <c:if test="${(employeeSize-1) < empl}">
                                    <td class="employee-noemployee" style="border-right: 1px solid #ddd"></td>
                                </c:if>
                                <c:if test="${(employeeSize-1) >= empl}">
                                    <td class="reservation-empty-td" style="border-right: 1px solid #ddd"
                                        id="${time}:20:${employeeList[empl].idx}"></td>
                                </c:if>
                            </c:forEach>
                        </tr>
                    </c:if>

                    <%-- 30 ~ 00 --%>
                    <tr class="" style="border-top: 1px solid #ddd">
                        <td style="border-right: 1px solid #ddd;font-weight: bold">30</td>
                            <%--<c:forEach var="empl" begin="0" end="${employeeSize -1}">--%>
                        <c:forEach var="empl" begin="0" end="6">
                            <c:if test="${(employeeSize-1) < empl}">
                                <td class="employee-noemployee" style="border-right: 1px solid #ddd"></td>
                            </c:if>
                            <c:if test="${(employeeSize-1) >= empl}">
                                <td class="reservation-empty-td"  style="border-right: 1px solid #ddd"
                                    id="${time}:30:${employeeList[empl].idx}"></td>
                            </c:if>
                        </c:forEach>
                    </tr>
                    <tr class="first level timespan " style="border-top: 1px solid #ddd">
                        <c:if test="${shopInfo.default_minute eq 10}">
                            <td style="border-right: 1px solid #ddd">40</td>
                        </c:if>
                        <c:if test="${shopInfo.default_minute eq 15}">
                            <td style="border-right: 1px solid #ddd">45</td>
                        </c:if>
                            <%--<c:forEach var="empl" begin="0" end="${employeeSize -1}">--%>
                        <c:forEach var="empl" begin="0" end="6">
                            <c:if test="${(employeeSize-1) < empl}">
                                <td class="employee-noemployee" style="border-right: 1px solid #ddd"></td>
                            </c:if>
                            <c:if test="${(employeeSize-1) >= empl}">
                                <c:if test="${shopInfo.default_minute eq 10}">
                                    <td class="reservation-empty-td" style="border-right: 1px solid #ddd"
                                        id="${time}:40:${employeeList[empl].idx}"></td>
                                </c:if>
                                <c:if test="${shopInfo.default_minute eq 15}">
                                    <td class="reservation-empty-td" style="border-right: 1px solid #ddd"
                                        id="${time}:45:${employeeList[empl].idx}"></td>
                                </c:if>
                            </c:if>
                        </c:forEach>
                    </tr>
                    <c:if test="${shopInfo.default_minute eq 10}">
                        <tr class="" style="border-top: 1px solid #ddd">
                            <td style="border-right: 1px solid #ddd">50</td>
                                <%--<c:forEach var="empl" begin="0" end="${employeeSize -1}">--%>
                            <c:forEach var="empl" begin="0" end="6">
                                <c:if test="${(employeeSize-1) < empl}">
                                    <td class="employee-noemployee" style="border-right: 1px solid #ddd"></td>
                                </c:if>
                                <c:if test="${(employeeSize-1) >= empl}">
                                    <td class="reservation-empty-td" style="border-right: 1px solid #ddd"
                                        id="${time}:50:${employeeList[empl].idx}"></td>
                                </c:if>
                            </c:forEach>
                        </tr>
                    </c:if>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<%--<button onclick="shopOpen()">오픈</button><br>--%>
<%--<button onclick="shopClose()">마감</button><br>--%>
<%--<button onclick="getURL()">발신번호 관리 팝업</button><br>--%>
<%--<button onclick="getSenderNumberList()">발신번호 목록 확인 </button><br>--%>
<%--<button onclick="sendXMS()">마감문자 발신 </button><br><br>--%>
<%--<input type="text" id="resultStatusNumber"> <button onclick="getMessages()">접수번호 상태 확인</button><br><br>--%>

<%-- 예약 클릭 팝업 --%>
<%@include file="../../include/modals/calendar/tdClickPopup.jspf"%>
<%-- 예약 모서리 팝업 --%>
<%@include file="../../include/modals/calendar/resPopupInfo.jspf"%>

<%--즐겨찾기 모달--%>

<%--즐겨찾기 모달 끝--%>

<%--프로모션 모달--%>
<div class="modal fade promotion-modal" id="promotionModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document" style="width: 900px">
        <div class="modal-content">

            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">프로모션 설정</h4>
            </div>

            <div class="modal-body">
                <div class="pro-content">
                    <div class="pro-teb">
                        <ul class="nav flex-column nav-tebs" role="tablist">
                            <li class="nav-item" role="presentation" class="active">
                                <a class="nav-link active" href="#promotion" aria-controls="promotion" role="tab" data-toggle="tab">프로모션</a>
                            </li>
                            <li class="nav-item" role="presentation">
                                <a class="nav-link" href="#promotion-list" aria-controls="promotion-list" role="tab" data-toggle="tab">프로모션 내역</a>
                            </li>
                            <%--<li class="nav-item" role="presentation">--%>
                            <%--<a class="nav-link" href="#promotion-setting" aria-controls="promotion-setting" role="tab" data-toggle="tab">설정</a>--%>
                            <%--</li>--%>
                        </ul>
                    </div>
                </div>
                <div class="pro-content">
                    <div class="tab-content">
                        <!--내 프로모션-->
                        <div role="tabpanel" class="tab-pane active my-promotion-teb" id="promotion">
                            <div class="promotion-modal-content">
                                <h2>진행중인 프로모션</h2>
                                <div class="promotion-modal-content02">
                                    <c:choose>
                                        <c:when test="${not empty promotion}">
                                            <button type="button" id="artUpdateBtn" class="promotion-add btn-move">수정</button>
                                            <div class="promotion-now-table" id="progressPromotion">
                                                <table>
                                                    <thead>
                                                    <tr>
                                                        <td width="30%">프로모션 제목</td>
                                                        <td width="30%">내용</td>
                                                        <td width="15%">목표</td>
                                                        <td width="15%">달성</td>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <tr>
                                                        <td>
                                                            <p>${promotion.title}</p>
                                                            <p class="promotion-day-text"><fmt:formatDate value="${promotion.date}" pattern="yyyy.MM.dd"/></p>
                                                        </td>
                                                        <td><p>${promotion.name}</p><p class="promotion-day-text">${promotion.memo}</p></td>
                                                        <td><p><fmt:formatNumber value="${promotion.target_value}" type="number"/></p></td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${promotion.value_type eq 0}">
                                                                    <p><fmt:formatNumber value="${promotion.achievement_count}" type="number"/></p>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <p><fmt:formatNumber value="${promotion.achievement_cost}" type="number"/></p>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <%--<button type="button" id="artInsertBtn" class="promotion-add">등록</button>--%>
                                            <div>
                                                    <%--onclick = 눌렀을떄 form 보여주는 기능, 고치시면 지우셔도 됩니당--%>
                                                <p id="artInsertBtn" onclick="$('#artForm').css('display', '');">등록하기</p>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                    <form class="promotion-add-div" id="artForm" style="display: none" method="post" onsubmit="return validate(this)">
                                        <c:if test="${not empty promotion}"><input type="hidden" name="idx" value="${promotion.idx}"></c:if>
                                        <button type="button" class="promotion-add-div-close"><i class="fas fa-times"></i></button>
                                        <h2>프로모션 제목</h2>
                                        <input type="text" name="title" id="pmTitle" placeholder="제목을 입력해주세요" value="${promotion.title}" required>
                                        <h2>프로모션 내용</h2>
                                        <table>
                                            <tbody>
                                            <tr>
                                                <td width="30%">
                                                    <select class="wide" id="artCategory">
                                                        <option value="">선택</option>
                                                    </select>
                                                </td>
                                                <td width="30%">
                                                    <select class="wide" name="services_idx" id="artDetail">
                                                        <option value="">선택</option>
                                                    </select>
                                                </td>
                                                <td width="40%">
                                                    <input type="number" name="target_value" id="pmTargetValue" value="${promotion.target_value}" placeholder="목표치를 적어주세요" required>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                        <h2>횟수 / 금액 선택</h2>
                                        <div class="form-check mr-10">
                                            <input class="form-check-input" type="radio" name="value_type" id="pmRadio1" value="0"
                                                   <c:if test="${promotion.value_type eq 0 or empty promotion}">checked</c:if>>
                                            <label class="form-check-label" for="pmRadio1">횟수</label>
                                        </div>
                                        <div class="form-check">
                                            <input class="form-check-input" type="radio" name="value_type" id="pmRadio2" value="1"
                                                   <c:if test="${promotion.value_type eq 1}">checked</c:if>>
                                            <label class="form-check-label" for="pmRadio2">금액</label>
                                        </div>
                                        <h2>목표달성시 혜택</h2>
                                        <textarea name="memo" id="pmMemo" placeholder="목표달성시 혜택을 적어주세요.">${promotion.memo}</textarea>
                                        <div class="promotion-add-btn">
                                            <button type="submit" class="btn-move">등록</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <!--프로모션 내역-->
                        <div role="tabpanel" class="tab-pane" id="promotion-list">
                            <div class="promotion-modal-content promotion-list">
                                <h2>프로모션 내역</h2>
                                <div class="promotion-list-total">
                                    <ul>
                                        <li>
                                            <p>총 내역 수</p>
                                            <div>
                                                <p id="pmFull">0</p>
                                                <p>개</p>
                                            </div>
                                        </li>
                                        <li>
                                            <p>달성된 프로모션 수</p>
                                            <div>
                                                <p id="pmSuccess">0</p>
                                                <p>개</p>
                                            </div>
                                        </li>
                                        <li>
                                            <p>실패한 프로모션 수</p>
                                            <div>
                                                <p id="pmFail">0</p>
                                                <p>개</p>
                                            </div>
                                        </li>
                                    </ul>
                                </div>
                                <div class="promotion-list-table">
                                    <table id="pastPromotion">
                                        <thead>
                                        <tr>
                                            <td width="30%">프로모션 제목</td>
                                            <td width="30%">내용</td>
                                            <td width="15%">목표</td>
                                            <td width="15%">달성</td>
                                            <td width="10%">상태</td>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <%--<tr>
                                            <td>
                                                <p>1월 프로모션</p>
                                                <p class="promotion-day-text">2019.00.00</p>
                                            </td>
                                            <td>
                                                큐티클 리무 50 판매 하기
                                                <p class="promotion-day-text">내용 보이는곳</p>
                                            </td>
                                            <td>30</td>
                                            <td>50</td>
                                            <td>
                                                <span class="promotion-success">완료</span>
                                                &lt;%&ndash;<span class="promotion-failure">실패</span>&ndash;%&gt;
                                            </td>
                                        </tr>--%>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <!--프로모션 설정-->
                        <div role="tabpanel" class="tab-pane" id="promotion-setting">
                            <div class="promotion-modal-content">
                                <h2>설정</h2>
                                <div class="promotion-modal-setting">
                                    <table>
                                        <tbody>
                                        <tr>
                                            <th width="25%">프로모션 기간</th>
                                            <td>
                                                <select name="" id="" class="wide">
                                                    <option value="">선택</option>
                                                    <option value="">1일</option>
                                                </select>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                    <button type="button">저장</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <%--<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>--%>
                <%--<button type="button" class="btn btn-primary">Save changes</button>--%>
            </div>

        </div>
    </div>
</div>
<%--프로모션 모달 끝--%>
<%-- AjaxSubmit JS --%>
<%--<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js" integrity="sha384-FzT3vTVGXqf7wRfy8k4BiyzvbNfeYjK+frTVqZeNDFl8woCbF0CYG6g2fMEFFo/i" crossorigin="anonymous"></script>--%>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.js"></script>
<%--<script src="http://malsup.github.com/jquery.form.js"></script>--%>

<%-- 예약 & 드래그 & 리사이즈 --%>
<script>
    var iModal = $('#exampleModal');
    var emplMaxPage;
    var default_minute;
    var objSpan;
    var originIdx;
    var dropHour;
    var dropMinute;
    var dropEmployee;



    /* 테이블 및 직원 출력 */
    $(document).ready(function () {
        // 문자 포인트 확인
        var point = getBalance();
        $('#remainPoint').text('문자포인트 : ' + comma(point) + ' p');

        var id_list = [];
        <%--var empl_list = ${fn:replace(fn:replace(employeeList, ':', '+\":\"+'),'=', ':')};--%>
        var empl_list = new Array();
        empl_list = ${employeeList};
        var start = '' + '${fn:split(shopInfo.shop_open,':')[0]}';
        var end = '' + '${fn:split(shopInfo.shop_close,':')[0]}';
        if(start != '')start = parseInt(start);
        if(end != '')end = parseInt(end);
        var k = 0;
        for (var i = start; i < end; i++) {// 시
            for (var j = 0; j < 60;) {// 분
                empl_list.forEach(function (v) {// 시:분:idx 만들기 위한 식
                    id_list.push(i+":"+pad(j,2)+":"+empl_list[k].idx);
                    k++;
                    if(k==empl_list.length)k=0;
                });
                j += ${shopInfo.default_minute};
            }
        }
        // //console.log(id_list);// 확인용
        var j = 0;// 해당 td의 시간을 저장하는 공간, empl_list의 순서
        id_list.forEach(function (item) {
            var time = item.split(":"); // 시와 분을 구하기 위한 용도
            var tdMins = time[0] * 60 + parseInt(time[1]);
            var a = empl_list[j]; // 직원 개인의 근무시간을 가진 객체
            var op = a.work_start.split(":");
            var cl = a.work_end.split(":");
            if((op[0]*60)+parseInt(op[1]) > tdMins || (cl[0]*60)+parseInt(cl[1]) <= tdMins){
                $('td[id="'+item+'"]').addClass("employee-noschedule");
                /*$('td[id="'+item+'"]').removeClass("reservation-empty-td");*/
            }
            j++;
            if(j==empl_list.length) j=0;// tr이 바뀌면 0으로 초기화
        });
    });

    /* 직원 페이징 및 예약 정보 출력 */
    $(document).ready(function () {
        // $(window).on('load', function(){
        /* 예약 현황판 날짜 */
        var checkDate = '${checkDate}';
        if(uk(checkDate) != ""){
            $('#forDate').datepicker("setDate", forDate);
        }
        /* 직원 페이징 value set */
        emplMaxPage = '${emplMaxPage}';
        var empl_page = '${emplPage}';
        $('#empl_paging_prev').attr('data-value', empl_page);
        $('#empl_paging_next').attr('data-value', empl_page);

        /* reservation view */
        var jsonA = new Array();
        jsonA = ${reservation};
        default_minute = '${shopInfo.default_minute}';
        view_reservations(jsonA, default_minute, '${shopInfo.shop_close}');


        /* drag start */
        $('.reservation-td').draggable({
            containment : "#container", scroll : false ,
            revert : /*"invalid"*/ false,
            helper : "clone",
            cursorAt: {top: -10, left: -10},
            // opacity: 0.8,
            minWidth: 115,
            start: function(){
                objSpan = $(this).attr("rowspan");
                originIdx = $(this).attr("data-value");
            }
        });


        /* drag end */
        $('.reservation-empty-td').droppable({
            tolerance : "pointer",
            drop: function(){
                var tmp = $(this).attr("id");
                var strSplit = tmp.split(":");
                dropHour = strSplit[0];
                dropMinute = strSplit[1];
                dropEmployee = strSplit[2];
                var forDate = '${forDate}';
                var shopId = '${shopInfo.id}';

                /* ver 1 */
                <%--var spanSize = $(this).attr("rowspan");--%>
                <%--var drop_flag = res_drop_check(dropHour, dropMinute, dropEmployee, '${shopInfo.default_minute}', spanSize, objSpan);--%>

                /* ver 2 */
                var drop_flag = res_drop_check(dropHour, dropMinute, dropEmployee, '${shopInfo.default_minute}', objSpan, originIdx);

                if(drop_flag) {
                    res_move(originIdx, dropHour, dropMinute, dropEmployee, objSpan, forDate, shopId);
                }
                else {
                    alert("예약이 중첩되었습니다.");
                    form_submit('/Reservation/calendar', {forDate : $('#forDate').val()}, 'post');
                    return false;
                }
            }
        });


        /* drag resize */
        $('.reservation-td').resizable({
            containment : ".schedule-table",
            helper:"ui-resizable-helper",
            handles: "s",
            minWidth: 115,
            ghost: true,
            stop: function(event, ui){
                var default_minute = '${shopInfo.default_minute}';
                var add_size = sizeCheck(ui.originalSize.height, ui.size.height);
                var check_target = "";
                var end_minute = "";

                /* no resize */
                if(add_size == 0){ form_submit('/Reservation/calendar', {forDate : $('#forDate').val()}, 'post'); return false;}
                /* 축소*/
                else if(add_size < 0){
                    if($(this).attr("rowspan") == 1){ form_submit('/Reservation/calendar', {forDate : $('#forDate').val()}, 'post'); return false;}
                    check_target = subtract($(this).attr("id"), add_size, default_minute);
                }
                /* 확장 */
                else {
                    for(var i = 0 ; i < Number(add_size); i++){
                        check_target = resize_check($(this).attr("id"), $(this).attr("rowspan"), i, default_minute);
                        var next_td = document.getElementById(check_target);
                        if($(next_td).hasClass("reservation-td")){
                            alert("예약이 겹쳤습니다.");
                            form_submit('/Reservation/calendar', {forDate : $('#forDate').val()}, 'post');
                            return false;
                        }
                    }
                    var tmps = check_target.split(":");
                    var minute_tmp = parseInt(tmps[1]);
                    if(add_size >= 1){
                        if(default_minute == 10) check_target = tmps[0]+ ":" + (minute_tmp + 10);
                        else check_target = tmps[0]+ ":" + (minute_tmp + 15);
                    }
                    var tmp = check_target.split(":");
                    if(tmp[1] == 60){
                        tmp[0] = Number(tmp[0]) + 1;
                        tmp[1] = "00";
                        check_target = tmp[0]+":"+tmp[1];
                    }
                }
                $.ajax({
                    url : "/Reservation/updateFormReservationEndTime",
                    type: "post",
                    data : {
                        "idx" : $(this).attr("data-value"),
                        "shopId" : '${shopInfo.id}',
                        "end_time" : check_target
                    },
                    dataType : "json",
                    success : function(data){
                        if(data.code == 200){
                            form_submit('/Reservation/calendar', {forDate : $('#forDate').val()}, 'post');
                        } else if(data.code == 900){
                            alert("잠시 후 다시 시도해 주세요.");
                            form_submit('/Reservation/calendar', {forDate : $('#forDate').val()}, 'post');
                            return false;
                        } else{
                            alert("잠시 후 다시 시도해 주세요.");
                            form_submit('/Reservation/calendar', {forDate : $('#forDate').val()}, 'post');
                            return false;
                        }
                    },
                    error : function(){
                        alert("에러가 발생했습니다."); form_submit('/Reservation/calendar', {forDate : $('#forDate').val()}, 'post');
                    }
                });
            },
            grid: [0, 25]
        });
    });
</script>
<script>
    var dminute = '${shopInfo.default_minute}';
    $(document).on('keyup', '#pmTargetValue', function () { if($(this).val() == '')$(this).val(0); });
    $(document).on('keypress', '#pmTargetValue', function () { if($(this).val() == '')onlyInt(this) });


    <%-- 내역 뿌리기 --%>
    $(document).ready(function () {
        $.post('selectListPromotion', null, function (data) {
            //console.log(data);
            var target = $('#pastPromotion > tbody');
            if(data.code == 200){
                var list = data.pmList;
                if(list.length > 0)$('#pmFull').text(list.length);
                else $('#pmFull').text(0);
                list.forEach(function (v) {
                    var tr = "<tr>";
                    // var tr = document.createElement('tr');
                    var date = new Date(v.date).format('yyyy.MM.dd');
                    var sf = '<span class="promotion-success">완료</span>';
                    var type = v.value_type;
                    var value;
                    if(type == 0) value = v.achievement_count;
                    else value = v.achievement_cost;
                    if (v.target_value > value) sf = '<span class="promotion-failure">실패</span>';
                    var str = '<td><p>' + v.title + '</p><p class="promotion-day-text">' + date + '</p></td>' +
                        '<td><p>' + v.name + '</p><p class="promotion-day-text">' + v.memo + '</p></td>' +
                        '<td>' + numberWithCommas(v.target_value) + '</td>' +
                        '<td>' + numberWithCommas(value) + '</td>' +
                        '<td>' + sf + '</td>';
                    // tr.innerHTML = str;
                    tr += str + "</tr>"
                    $(target).append(tr);
                });
                $('#pmSuccess').text($(target).find('.promotion-success').length);
                $('#pmFail').text($(target).find('.promotion-failure').length);
            } else if(data.code == 902) {
                var str = '<tr><td colspan="5">지난 이달의 아트 내역이 없습니다.</td></tr>';
                $(target).append(str);
            } else {
                alert('오류가 발생했습니다. ::' + data.code);
            }
        }, 'json').fail(function (error) {
            alert('잠시후 다시 시도해주세요. :: ' + error.status);
        });
    });

    <%-- 이달의 아트 등록버튼 --%>
    $('#artInsertBtn').click(function () {
        $('#artForm').attr('data-value', 'insertPromotion');
        $('#artCategory').empty();
        $('#artCategory').append('<option value="">선택</option>');
        $.post('selectCategory', null, function (data) {
            if(data.code == 200){
                var cateMap = data.cateMap;
                cateMap.forEach(function (v) {
                    $('#artCategory').append('<option value="' + v.category + '">' + v.category + '</option>');
                });
                $('#artCategory').niceSelect('update');
                $('#artCategory').trigger('change');
            } else alert('잠시후 다시 시도해주십시오.');
        }, 'json').fail(function (error) {
            alert('잠시후 다시 시도해주십시오. :: ' + error.status);
        });
    });

    <%-- 이달의 아트 수정버튼 --%>
    $('#artUpdateBtn').click(function () {
        $('#artForm').attr('data-value', 'updatePromotion');
        $('#artCategory').empty();
        $('#artCategory').append('<option value="">선택</option>');
        var category = '${promotion.category}';
        var services_idx = '${promotion.services_idx}';
        /*$('#artForm').find('input:radio').prop('checked', true);*/

        $.post('selectCategory', null, function (data) {
            if(data.code == 200){
                var cateMap = data.cateMap;
                cateMap.forEach(function (v) {
                    $('#artCategory').append('<option value="' + v.category + '">' + v.category + '</option>');
                });
                $('#artCategory').val(category);
            } else alert('잠시후 다시 시도해주십시오.');
            $('#artCategory').niceSelect('update');
        }, 'json').fail(function (error) {
            alert('잠시후 다시 시도해주십시오. :: ' + error.status);
        });

        $.post('serviceDetailCall', {cateName : category}, function (data) {
            $('#artDetail').empty();
            $('#artDetail').append('<option value="">선택</option>');
            if(data.code == 200){
                var detail = data.detailList;
                detail.forEach(function (v) {
                    $('#artDetail').append('<option value="' + v.idx + '">' + v.name + '</option>');
                });
                $('#artDetail').val(services_idx);
            } else alert('잠시후 다시 시도해주세요.');
            $('#artDetail').niceSelect('update');
        }).fail(function (error) {
            alert('잠시후 다시 시도해주세요. :: ' + error.status);
        });
    });

    <%-- 카테고리의 디테일 적용 --%>
    $('#artCategory').change(function () {
        $.post('serviceDetailCall', {cateName : $(this).val()}, function (data) {
            $('#artDetail').empty();
            $('#artDetail').append('<option value="">선택</option>');
            if(data.code == 200){
                var detail = data.detailList;
                detail.forEach(function (v) {
                    $('#artDetail').append('<option value="' + v.idx + '">' + v.name + '</option>');
                });
            } else alert('잠시후 다시 시도해주세요.');
            $('#artDetail').niceSelect('update');
        }).fail(function (error) {
            alert('잠시후 다시 시도해주세요. :: ' + error.status);
        });
    });

    var isAjaxing = false;

    <%-- 유효성 검사 --%>
    function validate(obj) {
        if($('#pmTitle').val() == ''){
            alert('이달의 아트 제목을 입력해주세요.'); $('#pmTitle').focus(); return false;
        } else if($('#artCategory').val() == '' || $('#artDetail').val() == '') {
            alert('이달의 아트 내용을 선택해주세요.'); return false;
        } else if($('#pmGoal').val() == '') {
            alert('목표치를 적어주세요.'); $('#pmGoal').focus(); return false;
        } else if($('#pmMemo').val() == '') {
            alert('달성시 혜택을 적어주세요.'); $('#pmMemo').focus(); return false;
        } else if($(obj).find('input:radio').is(':checked') == false) {
            alert('횟수 / 금액을 선택해주세요.'); return false;
        }

        // 중복방지
        if(isAjaxing){
            alert('처리중입니다. 잠시만 기다려주세요.');
            return false;
        }

        isAjaxing = true;
        var option = {
            url : $(obj).attr('data-value'),
            success : function (data) {
                if(data.code == 200){
                    form_submit(document.URL, {forDate : $('#forDate').val()});
                    setTimeout(function () { isAjaxing = false; }, 10);
                } else {
                    alert('잠시후 다시 시도해주세요. :: ' + data.code);
                    setTimeout(function () { isAjaxing = false; }, 10);
                }
            },
            error : function (error) {
                alert('잠시후 다시 시도해주세요 :: ' + error.status);
                setTimeout(function () { isAjaxing = false; }, 10000);
            }
        }
        $(obj).ajaxSubmit(option);
        return false;
    }
</script>

<%-- footer --%>
<%@include file="../../include/include-menu-footer.jspf" %>
<input type="hidden" id="timesplit" value="${shopInfo.default_minute}">

<!-- ============================================================== -->
<!-- Modal content -->
<!-- ============================================================== -->

<%-- 고객 정보 모달 --%>
<%@include file="../../include/modals/client/clientInfo.jspf" %>

<script type="text/javascript">
    $(function(){
        $('.promotion-add').click(function(){
            $('.promotion-add-div').show();
        });
        $('.promotion-add-div-close').click(function(){
            $('.promotion-add-div').hide();
        });
    });
</script>

<script>
    $(document).ready(function(){
        $("#demo").on("hide.bs.collapse", function(){
            $(".btn").html('등록');
        });
        $("#demo").on("show.bs.collapse", function(){
            $(".btn").html('취소');
        });
    });
</script>
<%-- (BootStrap Menu) 예약 회원 메뉴 --%>
<%--<script src="${pageContext.request.contextPath}/js/calendar/bootstrapMenu.js"></script>--%>
<script>
    var forDate = '${forDate}';
    /* 고객 모달 초기화 */
    function add_client(){
        $('#auto').trigger('click');
        $('#insertClientModal').draggable({handle : ".modal-header"});
        $("#insertClientModal").modal({backdrop: 'static', keyboard: false});
    }
</script>
<%-- 직원 등록 모달 --%>
<script src="/static/dist/js/chat/chat.js"></script>

<%-- 직원 관리 모달--%>
<%--<%@include file="../../jsp/bootstrap-modals/employee/addemployee.jspf" %>--%>

<%-- 예약 등록 모달 --%>
<%@include file="../../include/modals/calendar/reservation-modals.jspf" %>


<!-- ============================================================== -->
<!-- /. Modal content -->
<!-- ============================================================== -->

<%-- 예약 현황판 페이징 --%>
<script>
    function emplPageingPrev(obj){
        var empl_page = parseInt($(obj).attr('data-value'));
        if(empl_page == 0){
            return false;
        } else{
            var forDate = $("#forDate").val();

            var form = document.createElement("form");
            $(form).attr("action", "calendar").attr("method", "post");
            $(form).html(
                '<input type="hidden" name="forDate" value="' + forDate + '" />' +
                '<input type="hidden" name="emplPage" value="' + (empl_page - 1) + '"/>'
            );
            document.body.appendChild(form);
            $(form).submit();
            document.body.removeChild(form)
        }
    }
    function emplPageingNext(obj){
        var empl_page = parseInt($(obj).attr('data-value'));

        //console.log("empl page : " + empl_page);
        //console.log("emplMaxPage : " + emplMaxPage);

        if(empl_page >= emplMaxPage){
            return false;
        } else{
            var forDate = $("#forDate").val();

            var form = document.createElement("form");
            $(form).attr("action", "calendar").attr("method", "post");
            $(form).html(
                '<input type="hidden" name="forDate" value="' + forDate + '" />' +
                '<input type="hidden" name="emplPage" value="' + (empl_page + 1) + '"/>'
            );
            document.body.appendChild(form);
            $(form).submit();
            document.body.removeChild(form)
        }
    }
</script>

<%-- 예약 등록 --%>
<script>
    var times;
    /* 미등록 td 클릭 (reservation1 open) ver2 */
    $(document).on('click', '.reservation-empty-td', function(){
        times = $(this).attr("id");
        $('#serch_data').val("");
        callReservationStep1Content();
        $('#reservation-step1').modal('show');
    });

    /* 예약 수정 모달 오픈 */
    $(document).on('click', '#test', function(){
        $('#mf_res_modal').trigger('click');
    });
</script>


<%-- (BootStrap Menu) 예약 회원 메뉴 --%>
<%--<script src="${pageContext.request.contextPath}/js/calendar/bootstrapMenu.js"></script>--%>
<%-- 드래그 이벤트 --%>
<script src="${pageContext.request.contextPath}/static/dist/js/calendar/dragAction.js"></script>

<%--채팅 사이드바 이벤트--%>
<script>
    $(function(){
        var duration = 300;

        var $sidebar = $('.sidebar');
        var $sidebarButton = $sidebar.find('.chat-open-btn').on('click', function(){
            $sidebar.toggleClass('open');
            if($sidebar.hasClass('open')){
                $sidebar.stop(true).animate({right: '-0px'}, duration);
                $sidebarButton.find('span').text('닫기');
            }else{
                $sidebar.stop(true).animate({right: '-400px'}, duration);
                $sidebarButton.find('span').text('팅톡');
            };
        });
    });
</script>

<%-- 팝업 액션 이벤트 --%>
<script src="${pageContext.request.contextPath}/static/dist/js/calendar/popupMenuActions.js"></script>

<script src="/static/js/todayuser.js"></script>

<%-- 고객 등록 모달 --%>
<%@include file="../../include/modals/client/addclient.jspf" %>

<%-- 매장 오픈 마감 --%>
<script>
    function shopOnOff(obj){
        if($(obj).is(":checked"))
            shopOpen();
        else
            shopClose();
    }
    function shopOpen(){
        $.ajax({
            url : "/shopOpen",
            type: "post",
            dataType : "json",
            success : function(data){
                if(data.code == 200){
                    form_submit('/Reservation/calendar', {forDate : $('#forDate').val()}, 'post');
                } else if(data.code == 900){
                    alert("잠시 후 다시 시도해 주세요.");
                    form_submit('/Reservation/calendar', {forDate : $('#forDate').val()}, 'post');
                    return false;
                } else{
                    alert("잠시 후 다시 시도해 주세요.");
                    form_submit('/Reservation/calendar', {forDate : $('#forDate').val()}, 'post');
                    return false;
                }
            },
            error : function(){
                alert("에러가 발생했습니다."); form_submit('/Reservation/calendar', {forDate : $('#forDate').val()}, 'post');
            }
        });
    }
    function shopClose(){
        $.ajax({
            url : "/shopClose",
            type: "post",
            dataType : "json",
            success : function(data){
                if(data.code == 200){
                    form_submit('/Reservation/calendar', {forDate : $('#forDate').val()}, 'post');
                } else if(data.code == 900){
                    alert("잠시 후 다시 시도해 주세요.");
                    form_submit('/Reservation/calendar', {forDate : $('#forDate').val()}, 'post');
                    return false;
                } else{
                    alert("잠시 후 다시 시도해 주세요.");
                    form_submit('/Reservation/calendar', {forDate : $('#forDate').val()}, 'post');
                    return false;
                }
            },
            error : function(){
                alert("에러가 발생했습니다."); form_submit('/Reservation/calendar', {forDate : $('#forDate').val()}, 'post');
            }
        });
    }
</script>

<%-- 채팅 --%>
<script>
    $(document).ready(function (){
        $.post('/selectChat', {forDate : '${forDate}'}, function (data) {
            if(data.code == 200){
                data = data.chatList;
                data.forEach(function (item) {
                    var hour = item.date.split(" ")[1].split(":")[0];
                    var ampm = hour >= 12 ? 'AM' : 'PM';
                    if(hour < 12)hour = hour % 12;
                    var strTime = hour + ':' + item.date.split(" ")[1].split(":")[1] + ' ' + ampm;
                    var important_chat = '<li data-value="'+item.idx+'">'+ item.content + '<p>' + item.date.split(" ")[0] + " " + strTime + '</p><i class="fa fa-close"onclick="del(this)"></i></li>';
                    var default_chat = '<li data-value="'+item.idx+'">' +
                        '<div class="msj macro">' +
                        '<div class="avatar"></div>' +
                        '<div class="text text-l">' +
                        '<p>'+ item.content +'</p>' +
                        '<p><small>'+ strTime +'</small></p>' +
                        '</div>' +
                        '</div>' +
                        '<i class="fa fa-close" onclick="del(this)"></i>' +
                        '</li>';
                    if(item.status == 1) $('#chat_notice').prepend(important_chat);
                    else $('#chat_ul').append(default_chat);

                });
            }
        }, 'json').fail(function (error) {
            alert(error.status);
        });
    });
</script>
<script src="${pageContext.request.contextPath}/static/dist/js/popbill/SMS/popbillSmsAjax.js"></script>


<%--모바일 데이터픽커 버튼--%>
<script>
    $(function(){
        $('.calendar-content-left').click(function(){
            $('.hasDatepicker').show();
        });
        $('.datapick-close').click(function(){
            $('.hasDatepicker').hide();
        });
    });
</script>

<%-- AJAX Loading--%>
<%@include file="../../include/utils/ajaxLoading.jspf" %>
</body>
</html>