<%@ page import="sun.net.www.http.HttpClient" %>
<%@ page import="com.sun.deploy.net.HttpResponse" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.net.URLConnection" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStreamReader" %><%--
  Created by IntelliJ IDEA.
  User: lee
  Date: 2018-08-17
  Time: 오전 11:27
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../../include/include-header.jspf"%>
    <%@include file="../../include/css/include-stylesheet.jspf"%>
    <%--<link rel="stylesheet" href="/css/style.css">--%>
</head>


<body class="addshop_body">
<span id="id_ctimsgarea"></span>
<div class="mobWrap">
    <div class="innerBox">
        <div class="signin-container">
            <h1>고객관리의 시작 뷰팅</h1>
            code: ${vo.code}   //
            <a href="javascript:;" onclick="location.href = 'index.do'">BEAUT<span style="font-weight: 600;font-style: oblique;margin-right: 7px;color: #51abf3;">I</span>NG</a>
            <div class="signin-form">
                <div class="signin-form-inputs">
                    <div class="ui input">
                        <input placeholder="아이디" class="h-52" type="text" id="inputID">
                    </div>
                    <div class="ui input">
                        <input placeholder="비밀번호" class="h-52" type="password" id="inputPassword">
                    </div>
                </div>
                <!--자동로그인 체크박스-->
                <div style = "margin-top:10px;text-align: right">
                    <input type = "checkbox" name = "autologin" values = "1" id = "autoLogin"> <span style="color: #878787 !important;opacity: 0.8;font-size: 12px !important;font-weight: 500;">로그인기억하기</span>
                </div>
                <button class="login_btn01" onclick="loginCheck();">로그인</button>
            </div>
        </div>
    </div>
</div>
<script>
    // When the user scrolls down 80px from the top of the document, resize the navbar's padding and the logo's font size
    window.onscroll = function() {scrollFunction()};

    function scrollFunction() {
        if (document.body.scrollTop > 50 || document.documentElement.scrollTop > 50) {
            document.getElementById("navbar").style.padding = "8px 0px";
            document.getElementById("navbar").style.background = "rgba(0,0,0,0.8)";
            document.getElementById("navbar").style.height = "60px";
        } else {
            document.getElementById("navbar").style.padding = "3px 0px";
            document.getElementById("navbar").style.background = "transparent";
            document.getElementById("navbar").style.height = "50px";
        }
    }
</script>

<script>

    function loginCheck(){
        var autologin;
        /* 아이디 & 페스워드 입력 확인*/
        if($('#inputID').val() == ""){
            alert("아이디를 입력해 주세요");
            $('#inputID').focus();
            return false;
        }
        if($('#inputPassword').val() == ""){
            alert("비밀번호를 입력해 주세요");
            $('#inputPassword').focus();
            return false;
        }

        /*자동 로그인 체크 확인*/
        if($("#autoLogin").prop("checked"))
        {
            autologin = true;
        }
        else
        {
            autologin = false;
        }

        $.ajax({
            url : "/loginCheckAjax",
            type: "post",
            data : {
                "id" : $('#inputID').val(),
                "password" : $('#inputPassword').val(),
                "autologin" : autologin
            },
            dataType : "json",

            success : function(data){
                if(data.code == 200) {
                    location.href = '/Reservation/calendar';
                    return false;
                } else if(data.code == 900){
                    alert("아이디나 비밀번호가 존재하지 않습니다\n다시 시도해 주십시오");
                    $('#inputID').focus();
                    return false;
                } else if(data.code == 901){
                    alert("서버의 상태가 좋지 않습니다. 잠시 후 다시 시도해 주십시오.");
                    $('#inputID').focus();
                    return false;
                }
            },
            error : function(request,error){
                //console.log("data : "+data);
                alert("잠시 후 다시 시도해 주십시오");
                location.href = document.URL;
            }
        });
    }

    /* Enter 버튼 이벤트*/
    $(document).keypress(function(event){
        if(event.which == 13){
            loginCheck();
            return false;
        }
    });



</script>

<%-- AJAX Loading--%>
<%@include file="../../include/utils/ajaxLoading.jspf" %>
</body>
</html>