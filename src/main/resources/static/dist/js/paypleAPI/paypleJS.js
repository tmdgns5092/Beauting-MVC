function authOption(url, data) {
    return {
        "async": false,
        "url": url,
        "method": "POST",
        "content-type": "application/json",
        "cache-control": "no-cache",
        "data": JSON.stringify(data)
    };
}

$('#puserdel').click(function () {
    if(!confirm('등록된 계좌를 해지하시겠습니까?')) return false;
    var option1 = authOption(cfg.auth_url, {cst_id : cfg.cst_id, custKey : cfg.custKey, PCD_PAY_WORK : 'PUSERDEL'});
    var puserdel = null;
    $.ajax(option1).done(function (res) {
        console.log(res);
        puserdel = res;
    });
    var option2 = authOption(puserdel.return_url, {PCD_CST_ID : puserdel.cst_id, PCD_CUST_KEY : puserdel.custKey, PCD_AUTH_KEY : puserdel.AuthKey, PCD_PAYER_ID : payple_payer_id});
    $.ajax(option2).done(function (res) {
        console.log(res);
        if(res.PCD_PAY_RST == 'success'){
            $.post('/Payple/accountTermination', null, function (data) {
                if(data.code == 200){
                    alert('계좌해지 성공');
                }
            },'json').fail(function (request, status, error) {
                alert('오류가 발생했습니다. 잠시후 다시 시도해주십시오. ::' + request.status);
                console.log("request:code : " + request.status + "\nmessage:" + request.responseText + "\nerror : " + error);
            });
        }
    });
});

/* 금액 확인 */
function checkBuyTotal(buy_total){
    var buyList = [0, 10000, 20000, 30000, 50000, 100000, 150000, 200000];
    return buyList.indexOf(buy_total) != -1 ? true : false;
}

$('#payAction').on('click', function (event) {
    var buy_total = $('#buy_total').val();
    if(checkBuyTotal(buy_total)){
        console.log('aa');
        alert('잘못된 금액입니다. 다시 시도해주십시오.');
        location.reload();
        return false;
    }
    var date = new Date();
    var pay_work = 'PAY';
    var order_num = id + Math.round(date.getTime()/1000 | 0);
    var buyer_no = no;
    var is_taxsave = $('#is_taxsave').prop('checked') ? "Y" : "N";
    var is_reguler = $('input:radio[name=is_reguler]:checked').val();
    var buy_goods = is_reguler == 'N' ? '문자 충전(단건)' : '문자 충전(정기결제)';
    console.log(buy_goods);
    var pay_year = date.getFullYear();
    var pay_month = date.getMonth() + 1;
    var obj = new Object();
    /*
     * DEFAULT SET 1
     */
    obj.PCD_CST_ID = cfg.cst_id;
    obj.PCD_CUST_KEY = cfg.custKey;
    obj.PCD_AUTH_URL = cfg.auth_url;

    //#########################################################################################################################################################################

    obj.PCD_CPAY_VER = "1.0.1";							// (필수) 결제창 버전 (Default : 1.0.0)
    obj.PCD_PAY_TYPE = 'transfer';						// (필수) 결제 방법 (transfer | card)

    //#########################################################################################################################################################################

    obj.PCD_PAY_WORK = pay_work;							// (필수) 결제요청 업무구분 (AUTH : 본인인증+계좌등록, CERT: 본인인증+계좌등록+결제요청등록(최종 결제승인요청 필요), PAY: 본인인증+계좌등록+결제완료)

    /*
     * 1. 결제자 인증
     * PCD_PAY_WORK : AUTH
     */
    if (pay_work == 'AUTH') { // 안쓸거다
        console.log('auth');
        /*
        obj.PCD_PAYER_NO = buyer_no;						// (선택) 가맹점 회원 고유번호 (결과전송 시 입력값 그대로 RETURN)
        obj.PCD_PAYER_NAME = buyer_name;					// (선택) 결제자 이름
        obj.PCD_PAYER_HP = buyer_hp;						// (선택) 결제자 휴대폰 번호
        obj.PCD_PAYER_EMAIL = buyer_email;					// (선택) 결제자 Email
        obj.PCD_REGULER_FLAG = is_reguler;					// (선택) 정기결제 여부 (Y|N)
        obj.PCD_SIMPLE_FLAG = simple_flag;					// (선택) 간편결제 여부 (Y|N)
        */
    }
    /*
     * 2. 결제자 인증 및 결제
     * PCD_PAY_WORK : CERT | PAY
     */
    //## 2.1 최초결제 및 단건(일반,비회원)결제
    if (pay_work != 'AUTH') {
        if (simple_flag != 'Y' || payple_payer_id == '') {
            console.log('simple_flag : ' + simple_flag);
            console.log('payple_payer_id : ' + payple_payer_id);
            obj.PCD_PAY_GOODS = buy_goods;						// (필수) 결제 상품
            obj.PCD_PAY_TOTAL = buy_total;						// (필수) 결제 금액
            obj.PCD_PAYER_NO = buyer_no;

            obj.PCD_PAY_OID = order_num;						// 주문번호 (미입력 시 임의 생성)
            obj.PCD_REGULER_FLAG = is_reguler;					// (선택) 정기결제 여부 (Y|N)
            obj.PCD_PAY_YEAR = pay_year;						// (PCD_REGULER_FLAG = Y 일때 필수) [정기결제] 결제 구분 년도 (PCD_REGULER_FLAG : 'Y' 일때 필수)
            obj.PCD_PAY_MONTH = pay_month;						// (PCD_REGULER_FLAG = Y 일때 필수) [정기결제] 결제 구분 월 (PCD_REGULER_FLAG : 'Y' 일때 필수)

            obj.PCD_TAXSAVE_FLAG = is_taxsave;					// (선택) 현금영수증 발행 여부 (Y|N)
        }
        //## 2.2 간편결제 (재결제)
        if (simple_flag == 'Y' && payple_payer_id != '') {
            console.log('simple_flag : ' + simple_flag);
            console.log('payple_payer_id : ' + payple_payer_id);
            obj.PCD_SIMPLE_FLAG = 'Y';							// 간편결제 여부 (Y|N)
            //-- PCD_PAYER_ID 는 소스상에 표시하지 마시고 반드시 Server Side Script 를 이용하여 불러오시기 바랍니다. --//
            obj.PCD_PAYER_ID = payple_payer_id;					// 결제자 고유ID (본인인증 된 결제회원 고유 KEY)
            //-------------------------------------------------------------------------------------//
            obj.PCD_PAYER_NO = buyer_no;						// (선택) 가맹점 회원 고유번호 (결과전송 시 입력값 그대로 RETURN)
            obj.PCD_PAY_GOODS = buy_goods;						// (필수) 결제 상품
            obj.PCD_PAY_TOTAL = buy_total;						// (필수) 결제 금액

            obj.PCD_PAY_OID = order_num;						// 주문번호 (미입력 시 임의 생성)

            obj.PCD_REGULER_FLAG = is_reguler;					// (선택) 정기결제 여부 (Y|N)
            obj.PCD_PAY_YEAR = pay_year;						// (PCD_REGULER_FLAG = Y 일때 필수) [정기결제] 결제 구분 년도 (PCD_REGULER_FLAG : 'Y' 일때 필수)
            obj.PCD_PAY_MONTH = pay_month;						// (PCD_REGULER_FLAG = Y 일때 필수) [정기결제] 결제 구분 월 (PCD_REGULER_FLAG : 'Y' 일때 필수)

            obj.PCD_TAXSAVE_FLAG = is_taxsave;					// (선택) 현금영수증 발행 여부 (Y|N)
        }
    }
    //#########################################################################################################################################################################
    /*
     * DEFAULT SET 2
     */

    //-- order_result.html => [app.js] app.post('/result', ...) --//
    obj.PCD_PAYER_AUTHTYPE = $('input:radio[name=authType]:checked').val();				                            // (선택) [간편결제/정기결제] 본인인증 방식 (sms : 문자인증 | pwd : 패스워드 인증)
    obj.PCD_RST_URL = '/Payple/result';				                    // (필수) 결제(요청)결과 RETURN URL

    obj.payple_dir_path = '';					                            // (선택) cPayPayple 폴더 경로 (ex: /shop/cPayPayple 은 /shop 로 지정)

    //-- auth => [app.js] app.post('/cPayPayple/auth', ...) --//
    obj.payple_auth_file = '';		                                // (선택) cPayPayple 폴더 의 payple_payAuth.html 대체파일 명
    console.log(obj);
    if(obj.PCD_PAY_GOODS != '문자 충전(단건)' && obj.PCD_PAY_GOODS != '문자 충전(정기결제)'){
        alert('잘못된 접근입니다. 다시 시도해주십시오.');
        location.reload();
        return false;
    }
    PaypleCpayAuthCheck(obj);
    event.preventDefault();
});