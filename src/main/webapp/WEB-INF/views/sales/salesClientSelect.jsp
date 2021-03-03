<%--
  Created by IntelliJ IDEA.
  User: gwonseunghun
  Date: 07/02/2019
  Time: 2:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../../include/include-header.jspf" %>
    <%@include file="../../include/css/include-stylesheet.jspf" %>
    <title>Title</title>
</head>
<body class="calendar-body on-body">
<%@include file="../../include/include-header-view.jspf"%>
<div class="page-name">
    <div style="width: 1300px;margin: 0 auto;padding: 0 15px">
        <h3>결제고객 검색</h3>
        <p>결제를 원하시는 고객님을 검색후 선택하시면 고객님 결제 페이지로 이동합니다.</p>
    </div>
</div>
<div class="container searchClient-container mb-100 mh-764" style="width: 1300px">
    <div class="top-search">
        <table>
            <tr>
                <td width="28%" style="padding-right: 3px">
                    <select class="nice-select wide" id="search_type">
                        <option value="all_search">전체검색</option>
                        <option value="code_search">고객번호</option>
                        <option value="name_search">고객이름</option>
                        <option value="phone_search">휴대폰</option>
                    </select>
                </td>
                <td width="45%" style="width: 212px">
                    <input type="text" id="search_text">
                </td>
                <td width="25%">
                    <button onclick="searchClient()">검색</button>
                </td>
            </tr>
        </table>
    </div>
    <table class="table-hover">
        <thead>
        <tr>
            <th width="10%">고객번호</th>
            <th width="18%">이름</th>
            <th width="18%">연락처</th>
            <th width="18%">잔액</th>
            <th width="18%">객단가</th>
            <th width="18%"><p>불이행 횟수</p></th>
        </tr>
        </thead>
        <tbody id="clientSearchBody">
        <c:forEach items="${clientList}" var="client">
        <tr data-value="${client.idx}">
            <td>${client.code}</td>
            <td class="font-weight-bold">${client.name}</td>
            <td>
                <c:if test="${client.phone ne '' && not empty client.phone}">
                    <c:if test="${fn:length(client.phone) eq 11}">
                        <c:set var="num1" value="${fn:substring(client.phone,0,3)}"/>
                        <c:set var="num2" value="${fn:substring(client.phone,3,7)}"/>
                        <c:set var="num3" value="${fn:substring(client.phone,7,11)}"/>
                        ${num1}-${num2}-${num3}
                    </c:if>
                    <c:if test="${fn:length(client.phone) ne 11}">
                        <c:set var="num1" value="${fn:substring(client.phone,0,3)}"/>
                        <c:set var="num2" value="${fn:substring(client.phone,3,6)}"/>
                        <c:set var="num3" value="${fn:substring(client.phone,6,fn:length(client.phone))}"/>
                        ${num1}-${num2}-${num3}
                    </c:if>
                </c:if>
                <c:if test="${client.phone eq '' || empty client.phone}">-</c:if>
            </td>
            <td>
                ${client.prepaid} 원
            </td>
            <td>
                <c:if test="${client.client_price ne '' && not empty client.client_price}">${client.client_price} 원</c:if>
                <c:if test="${client.client_price eq '' || empty client.client_price}">0 원</c:if>
            </td>
            <td>
                <c:if test="${client.noshow ne '' && not empty client.noshow}">${client.noshow} 회</c:if>
                <c:if test="${client.noshow eq '' || empty client.noshow}">0 회</c:if>
            </td>
        </tr>
        </c:forEach>
    </table>

    <div class="page-nav" style="text-align: center;">
        <form action="searchClient" method="post">
            <nav aria-label="Page navigation">
                <ul class="pagination" id="paging_ul">
                    <li>
                        <a href="javascript:void(0);" aria-label="Previous" data-value="${paging.prevPageNo}" class="paging_btn">
                            <span aria-hidden="true">≪</span>
                        </a>
                    </li>
                    <c:forEach var="i" begin="${paging.startPageNo }" end="${paging.endPageNo }" step="1">
                        <c:choose>
                            <c:when test="${i eq paging.pageNo }">
                                <li class="active">
                                    <a href="javascript:void(0);" data-value="${i}" class="paging_btn">${i}
                                        <span class="sr-only">(current)</span>
                                    </a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li>
                                    <a href="javascript:void(0);" data-value="${i}" class="paging_btn">${i}</a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <li>
                        <a href="javascript:void(0);" aria-label="Next" data-value="${paging.nextPageNo}" class="paging_btn">
                            <span aria-hidden="true">≫</span>
                        </a>
                    </li>
                </ul>
            </nav>
        </form>
    </div>
</div>
<%@include file="../../include/include-menu-footer.jspf" %>

<%-- load --%>
<script>
    $(document).ready(function () {
        var type = '${type}';
        var data = '${data}';

        if(uk(type) != '') {$('#search_type').val(type); $('#search_type').niceSelect('update');}
        if(uk(data) != '') $('#search_text').val(data);
    });
    $('#search_text').keydown(function(key){
        if (key.keyCode == 13) {
            searchClient();
        }
    });
</script>
<%-- 고객 검색 --%>
<script>
    function searchClient() {
        var data = $('#search_text').val();
        var type = $('#search_type option:selected').val();
        var form = document.createElement("form");

        $(form).attr("action", "/Sales/searchClient").attr("method", "post");
        $(form).html(
            '<input type="hidden" name="data" value="'+data+'" />' +
            '<input type="hidden" name="type" value="'+type+'" />' +
            '<input type="hidden" name="pageNo" value="'+0+'" />'
        );
        document.body.appendChild(form);
        $(form).submit();
        document.body.removeChild(form);
    }
</script>
<%-- 페이징 버튼 --%>
<script>
    $('.paging_btn').click(function(){
        var form = document.createElement("form");
        $(form).attr("action", "/Sales/searchClient").attr("method", "post");
        $(form).html('<input type="hidden" name="pageNo" value="'+$(this).attr('data-value')+'" />');
        document.body.appendChild(form);
        $(form).submit();
        document.body.removeChild(form);
    });
</script>
<%-- tr 클릭 페이지 이동 --%>
<script>
    $('#clientSearchBody tr').click(function(){
        var value = $(this).attr('data-value');
        form_submit("/Sales/sales", {client_idx : value});
    });
</script>

<script src="/static/dist/js/utils.js"></script>

<%-- AJAX Loading--%>
<%@include file="../../include/utils/ajaxLoading.jspf" %>
</body>
</html>
