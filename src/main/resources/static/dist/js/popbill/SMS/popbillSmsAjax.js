/*
    팝빌 SMS 가이드 라인
    [1]파트너연동회원 가입 여부 확인
    [2] 연동회원 잔여포인트 확인
    [3] 파트너 잔여포인트 확인
    [4] 파트너 충전 팝업
    [5] 팝빌 로그인 URL
    [6] 연동회원 포인트 춪언 팝업 URL
    [7] 기본 팝업 URL
    [8] 연동회원 가입 요청
    [9] 연동회원 담당자 확인
    [10] 연동회원 담당자 정보 수정
    [11] 연동회원 담당자 신규 등록
    [12] 회원 아이디 중복 확인
    [13] 연동회원 회사정보 확인
    [14] 연동회원 회사정보 수정
    [15] 발신번호 관리 팝업
    [17]자동감지 문자 발송
    [18]접수번호 상태 확인
    [19]문자 단가 확인
*/



/* [1]파트너연동회원 가입 여부 확인 */
function messageTest(num, link){
    $.ajax({
        url : "/BaseService/checkIsMember",
        type: "get",
        data : {
            "corpNum" : num,
            "linkId" : link
        },
        dataType : "json",
        success : function(data){
            //console.log(data);
            if(data.code == 200){
                alert("code : " + data.Response.code + "\nstatus : " + data.Response.message);
            }else{
                alert("code : " +data.code);
            }
        },
        error : function(){
            alert("에러가 발생했습니다.");
        }
    });
}
/* [2]연동회원 잔여포인트 확인 */
function getBalance(){
    var result = 0;
    $.ajax({
        url : "/BaseService/getBalance",
        type: "get",
        async: false,
        dataType : "json",
        success : function(data){
            result = data.Result;
            // console.log("function response : " + result);
        },
        error : function(request,status,error){
            console.log("code = "+ request.status + " \nmessage = " + request.responseText + " \nerror = " + error);
            alert("code = "+ request.status + " \nmessage = " + request.responseText + " \nerror = " + error); // 실패 시 처리
            // alert("에러가 발생했습니다.");
        }
    });
    return result;
}
/* [3]파트너 잔여포인트 확인 */
function getPartnerBalance(){
    $.ajax({
        url : "/BaseService/getPartnerBalance",
        type: "get",
        /*data : {
            "corpNum" : num,
            "linkId" : link
        },*/
        dataType : "json",
        success : function(data){
            //console.log(data);
            if(data.code == 200){
                alert("data : "+data);
            }else{
                alert("code : " +data.code);
            }
        },
        error : function(){
            alert("에러가 발생했습니다.");
        }
    });
}
/* [4]파트너 충전 팝업 */
function getPartnerURL(){
    $.ajax({
        url : "/BaseService/getPartnerURL",
        type: "get",
        /*data : {
            "corpNum" : num,
            "linkId" : link
        },*/
        dataType : "json",
        success : function(data){
            //console.log(data);
            if(data.code == 200){
                popupOpen(data.Result);
                /*$('#iframe').attr('src', data.Result);
                $("#testMyModal").modal({backdrop: 'static', keyboard: false});*/
            }else{
                alert("code : " +data.code);
            }
        },
        error : function(){
            alert("에러가 발생했습니다.");
        }
    });
}
/* [5]팝빌 로그인 URL */
function getPopbillURL_LOGIN(){
    $.ajax({
        url : "/BaseService/getPopbillURL_LOGIN",
        type: "get",
        /*data : {
            "corpNum" : num,
            "linkId" : link
        },*/
        dataType : "json",
        success : function(data){
            //console.log(data);
            if(data.code == 200){
                popupOpen(data.Result);
                /*$('#iframe').attr('src', data.Result);
                $("#testMyModal").modal({backdrop: 'static', keyboard: false});*/
            }else{
                alert("code : " +data.code);
            }
        },
        error : function(){
            alert("에러가 발생했습니다.");
        }
    });
}
/* [6]연동회원 포인트 춪언 팝업 URL */
function getPopbillURL_CHRG(){
    $.ajax({
        url : "/BaseService/getPopbillURL_CHRG",
        type: "get",
        /*data : {
            "corpNum" : num,
            "linkId" : link
        },*/
        dataType : "json",
        success : function(data){
            //console.log(data);
            if(data.code == 200){
                popupOpen(data.Result);
                /*$('#iframe').attr('src', data.Result);
                $("#testMyModal").modal({backdrop: 'static', keyboard: false});*/
            }else{
                // console.log("Exception : " + JSON.stringify(data.Exception));
                alert("code : " +data.code);
            }
        },
        error : function(){
            alert("에러가 발생했습니다.");
        }
    });
}
/* [7] 기본 팝업 URL*/
function getPopbillURL(){
    $.ajax({
        url : "/BaseService/getPopbillURL",
        type: "get",
        /*data : {
            "corpNum" : num,
            "linkId" : link
        },*/
        dataType : "json",
        success : function(data){
            // console.log(data);
            if(data.code == 200){
                popupOpen(data.Result);
                /*$('#iframe').attr('src', data.Result);
                $("#testMyModal").modal({backdrop: 'static', keyboard: false});*/
            }else{
                alert("code : " +data.code);
            }
        },
        error : function(){
            alert("에러가 발생했습니다.");
        }
    });
}
/* [8]연동회원 가입 요청*/
function joinMember(){
    $.ajax({
        url : "/BaseService/joinMember",
        type: "get",
        /*data : {
            "corpNum" : num,
            "linkId" : link
        },*/
        dataType : "json",
        success : function(data){
            //console.log(data);
            if(data.code == 200){
                alert("data : "+data);
            }else{
                alert(data.Exception);
            }
        },
        error : function(){
            alert("에러가 발생했습니다.");
        }
    });
}
/* [9]연동회원 담당자 확인 */
function listContact(){
    $.ajax({
        url : "/BaseService/listContact",
        type: "get",
        /*data : {
            "corpNum" : num,
            "linkId" : link
        },*/
        dataType : "json",
        success : function(data){
            //console.log(data);
            if(data.code == 200){
                alert("data : "+data);
            }else{
                alert("code : " +data.code);
            }
        },
        error : function(){
            alert("에러가 발생했습니다.");
        }
    });
}
/* [10]연동회원 담당자 정보 수정 */
function updateContact(){
    $.ajax({
        url : "/BaseService/updateContact",
        type: "get",
        /*data : {
            "corpNum" : num,
            "linkId" : link
        },*/
        dataType : "json",
        success : function(data){
            //console.log(data);
            if(data.code == 200){
                alert("data : "+data);
            }else{
                alert("code : " +data.code);
            }
        },
        error : function(){
            alert("에러가 발생했습니다.");
        }
    });
}
/* [11]연동회원 담당자 신규 등록 */
function registContact(){
    $.ajax({
        url : "/BaseService/registContact",
        type: "get",
        /*data : {
            "corpNum" : num,
            "linkId" : link
        },*/
        dataType : "json",
        success : function(data){
            //console.log(data);
            if(data.code == 200){
                alert("data : "+data);
            }else{
                alert(data.Exception);
            }
        },
        error : function(){
            alert("에러가 발생했습니다.");
        }
    });
}
/* [12]회원 아이디 중복 확인 */
function checkID(){
    $.ajax({
        url : "/BaseService/checkID",
        type: "get",
        /*data : {
            "corpNum" : num,
            "linkId" : link
        },*/
        dataType : "json",
        success : function(data){
            //console.log(data);
            if(data.code == 200){
                alert("data : "+data);
            }else{
                alert("code : " +data.code);
            }
        },
        error : function(){
            alert("에러가 발생했습니다.");
        }
    });
}
/* [13]연동회원 회사정보 확인*/
function getCorpInfo(){
    $.ajax({
        url : "/BaseService/getCorpInfo",
        type: "get",
        /*data : {
            "corpNum" : num,
            "linkId" : link
        },*/
        dataType : "json",
        success : function(data){
            //console.log(data);
            if(data.code == 200){
                alert("data : "+data);
            }else{
                alert("code : " +data.code);
            }
        },
        error : function(){
            alert("에러가 발생했습니다.");
        }
    });
}
/* [14]연동회원 회사정보 수정*/
function updateCorpInfo(){
    $.ajax({
        url : "/BaseService/updateCorpInfo",
        type: "get",
        /*data : {
            "corpNum" : num,
            "linkId" : link
        },*/
        dataType : "json",
        success : function(data){
            //console.log(data);
            if(data.code == 200){
                alert("data : "+data);
            }else{
                alert("code : " +data.code);
            }
        },
        error : function(){
            alert("에러가 발생했습니다.");
        }
    });
}
/* [15]발신번호 목록 확인*/
function getSenderNumberList(){
    // console.log('getSenderList');
    var return_data;
    $.ajax({
        url : "/MessageService/getSenderNumberList",
        type: "get",
        /*data : {
            "corpNum" : num,
            "linkId" : link
        },*/
        dataType : "json",
        async : false,
        success : function(data){
            // console.log(data);
            return_data = data;
        },
        error : function(){
            alert("에러가 발생했습니다.");
        }
    });
    return return_data;
}

/* [16]발신번호 관리 팝업 */
function getURL(){
    $.ajax({
        url : "/MessageService/getURL",
        type: "get",
        /*data : {
            "corpNum" : num,
            "linkId" : link
        },*/
        dataType : "json",
        success : function(data){
            if(data.code == 200){
                popupOpen(data.Result);
            }else{
                alert("code : " +data.code);
            }
        },
        error : function(){
            alert("에러가 발생했습니다.");
        }
    });
}

/* [17]자동감지 문자 발송 */
function sendXMS(){
    //console.log("test ... ");
    $.ajax({
        url : "/MessageService/sendXMS",
        type: "post",
        dataType : "json",
        success : function(data){
            //console.log(data);
            if(data.code == 200){
                alert("data : "+JSON.stringify(data));
                //console.log("data :  " + JSON.stringify(data));
            }else{
                alert("code : " +data.code);
                //console.log("data :  " + JSON.stringify(data));
            }
        },
        error : function(data){
            alert("에러가 발생했습니다. e : "+data.Result);

            //console.log("data : " + JSON.stringify(data));
        }
    });
}

/* [18] 접수번호 상태 확인*/
function getMessages(number){
    $.ajax({
        url : "/MessageService/getMessages",
        type: "get",
        dataType : "json",
        data : {"number" : number},
        success : function(data){
            //console.log(data);
            if(data.code == 200){
                // alert("data : "+data);
                return data.SentMessages;
                //console.log("data :  " + JSON.stringify(data));
            }else{
                alert("code : " +data);
            }
        },
        error : function(data){
            alert("에러가 발생했습니다. e : "+data.Result);

            //console.log("data : " + JSON.stringify(data));
        }
    });
}

/* [19] 접수번호 상태 확인*/
function getUnitCost(number, type){
    var result_data = "";
    $.ajax({
        url : "/MessageService/getUnitCost",
        type: "get",
        dataType : "json",
        data : {"comNum" : number, "type" : type},
        async : false,
        success : function(data){
            //console.log(data);
            if(data.code == 200){
                result_data = data;
                //console.log("data :  " + JSON.stringify(data));
            }else{
                result_data = data;
                //console.log("data :  " + JSON.stringify(data));
            }
        },
        error : function(data){
            alert("에러가 발생했습니다. e : "+data.Result);
            result_data = data;
            //console.log("data : " + JSON.stringify(data));
        }
    });

    return result_data;
}

// 자동 문자 값 가져오기
function selectAutoMessage(type) {
    var autoMessage = null;
    $.ajax({
        type: 'POST',
        url: '/Messages/selectAutoMessage',
        dataType: 'json',
        data: {type : type},
        async: false,
        success: function (data) {
            // console.log(data);
            if(data.code == 200 && data.autoMessage.onoff == 1){
                autoMessage = data.autoMessage;
            }
        },
        error: function (request, status, error) {
            alert('오류가 발생했습니다. 잠시후 다시 시도해주십시오. ::' + request.status);
            // console.log("request:code : " + request.status + "\nmessage:" + request.responseText + "\nerror : " + error);
        }
    });
    return autoMessage;
}

// 메세지 발송건 DB 저장
function insertMessageHistory(data) {
    $.post('/Messages/insertMessageData', data, function (data) {
        // console.log(data);
        if (data.code == 200) console.log('적재성공');
        else if (data.code == 902) console.log('적재실패');
        else console.log(data.e);
    }, 'json').fail(function (request, status, error) {
        alert('오류가 발생했습니다. 잠시후 다시 시도해주십시오. ::' + request.status);
        // console.log("request:code : " + request.status + "\nmessage:" + request.responseText + "\nerror : " + error);
    });
}

/* 팝업창 띄우기*/
function popupOpen(url){

    var popUrl = url;

    // var popOption = "width=545, height=685, resizable=no, scrollbars=no, status=no;";
    var popOption = "width=830, height=685, resizable=no, scrollbars=no, status=no;";

    window.open(popUrl,"",popOption);

}

