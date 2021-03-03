/*
 * 팝빌 문자 API Java SDK SpringMVC Example
 *
 * - SpringMVC SDK 연동환경 설정방법 안내 : http://blog.linkhub.co.kr/591/
 * - 업데이트 일자 : 2018-07-26
 * - 연동 기술지원 연락처 : 1600-9854 / 070-4304-2991~2
 * - 연동 기술지원 이메일 : code@linkhub.co.kr
 *
 * <테스트 연동개발 준비사항>
 * 1) src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml 파일에 선언된
 * 	  util:properties 의 링크아이디(LinkID)와 비밀키(SecretKey)를 링크허브 가입시 메일로
 *    발급받은 인증정보를 참조하여 변경합니다.
 * 2) 팝빌 개발용 사이트(test.popbill.com)에 연동회원으로 가입합니다.
 *
 * Copyright 2006-2014 linkhub.co.kr, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package kr.co.serinusSM.controller.popbillAPI;

import com.google.gson.Gson;
import com.popbill.api.ChargeInfo;
import com.popbill.api.MessageService;
import com.popbill.api.PopbillException;
import com.popbill.api.Response;
import com.popbill.api.message.*;
import kr.co.serinusSM.common.CommonUtils;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("MessageService")
public class PopbillMessageController {
    @Autowired
    private MessageService messageService;

    // 팝빌회원 사업자번호
    private String testCorpNum = "6173622290";

    // 팝빌회원 아이디
    private String testUserID = "MansOfBeauty";

    Logger log = Logger.getLogger(this.getClass());

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String home(Locale locale, Model model) {
        log.debug("checkIsMember : ");
        System.out.println("locale : "+locale);
        return "Message/index";
    }


    @RequestMapping(value = "getUnitCost", method = RequestMethod.GET)
    @ResponseBody
    public Object getUnitCost(@RequestParam Map<String, Object> paramMap) {
        /**
         *  문자메시지 전송단가를 확인합니다.
         */

        // 문자 메시지 유형, SMS-단문, LMS-장문, MMS-포토
        MessageType msgType = null;
        if("sms".equals(paramMap.get("type").toString())){
            msgType = MessageType.SMS;
        } else if("lms".equals(paramMap.get("type").toString())){
            msgType = MessageType.LMS;
        } else if("mms".equals(paramMap.get("type").toString())){
            msgType = MessageType.MMS;
        }

        JSONObject jobj = new JSONObject();

        try {

            float unitCost = messageService.getUnitCost(paramMap.get("comNum").toString(), msgType);
            jobj.put("Result",unitCost);
            jobj.put("code",200);
        } catch (PopbillException e) {
            jobj.put("Exception", e);
            jobj.put("code",900);
        }

        return jobj;
    }

    @RequestMapping(value = "getChargeInfo", method = RequestMethod.GET)
    public String chargeInfo( Model m) {

        // 문자 메시지 유형, SMS-단문, LMS-장문, MMS-포토
        MessageType msgType = MessageType.SMS;

        try {

            ChargeInfo chrgInfo = messageService.getChargeInfo(testCorpNum, msgType);
            m.addAttribute("ChargeInfo",chrgInfo);

        } catch (PopbillException e) {
            m.addAttribute("Exception", e);
            return "exception";
        }

        return "getChargeInfo";
    }

    @RequestMapping(value = "getURL", method = RequestMethod.GET)
    @ResponseBody
    public Object getURL(Model m, HttpSession session) {
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        log.debug("session : " + sMap);
        JSONObject jobj = new JSONObject();
        /**
         * 문자 서비스 관련 팝업 URL을 반환합니다.
         * - 보안정책에 따라 반환된 URL은 30초의 유효시간을 갖습니다.
         */

        // BOX - 문자 전송내역 조회 / SENDER - 발신번호 관리 팝업
        String TOGO = "SENDER";

        try {

            String url = messageService.getURL(sMap.get("com_num").toString(), TOGO);

            jobj.put("Result",url);
            jobj.put("code", 200);
        } catch (PopbillException e) {
            jobj.put("Exception", e);
            jobj.put("code", 200);
            e.printStackTrace();
        }

        return jobj;
    }

    @RequestMapping(value = "sendSMS", method = RequestMethod.GET)
    @ResponseBody
    public Object sendSMS( /*Model m*/ Map<String, Object> param, Map<String, Object> sMap) {
        JSONObject jobj = new JSONObject();

        // 발신번호
        String sender = param.get("sender_number").toString();

        // 수신번호
        String receiver = "01032025092";

        // 수신자명
        String receiverName = "수신자명";

        // 메시지 내용, 90byte 초과된 내용은 삭제되어 전송
        String content = "문자메시지 내용";

        // 예약전송일시, null 처리시 즉시전송
        Date reserveDT = null;

        // 광고문자 전송여부
        Boolean adsYN = false;

        // 전송요청번호
        // 파트너가 전송 건에 대해 관리번호를 구성하여 관리하는 경우 사용.
        // 1~36자리로 구성. 영문, 숫자, 하이픈(-), 언더바(_)를 조합하여 팝빌 회원별로 중복되지 않도록 할당.
        String requestNum = "";

        try {
            String receiptNum = messageService.sendSMS(sMap.get("com_num").toString(), sender, receiver,
                    receiverName, content, reserveDT, adsYN, sMap.get("id").toString(), requestNum);
            jobj.put("Result",receiptNum);
        } catch (PopbillException e) {
            jobj.put("Exception", e);
        }

        return jobj;
    }

    @RequestMapping(value = "sendSMS_Multi", method = RequestMethod.GET)
    public String sendSMS_Multi( Model m) {

        // [동보전송] 발신번호, 개별문자 전송정보에 발신자번호 없는 경우 적용
        String sender = "07043042991";

        // [동보전송] 메시지 내용, 개별문자 전송정보에 문자내용이 없는 경우 적용
        String content = "대량문자 메시지 내용";

        // 문자전송 예약일시
        Date reserveDT = null;

        // 광고문자 전송여부
        Boolean adsYN = false;

        // 개별문자 전송정보, 최대 1000건.
        Message msg1 = new Message();
        msg1.setSender("07043042991");
        msg1.setSenderName("발신자1");
        msg1.setReceiver("000111222");
        msg1.setReceiverName("수신자1");
        msg1.setContent("메시지 내용1");

        Message msg2 = new Message();
        msg2.setSender("07043042991");
        msg2.setSenderName("발신자2");
        msg2.setReceiver("000111222");
        msg2.setReceiverName("수신자2");
        msg2.setContent("메시지 내용2");

        Message[] messages = new Message[] {msg1,msg2};

        // 전송요청번호
        // 파트너가 전송 건에 대해 관리번호를 구성하여 관리하는 경우 사용.
        // 1~36자리로 구성. 영문, 숫자, 하이픈(-), 언더바(_)를 조합하여 팝빌 회원별로 중복되지 않도록 할당.
        String requestNum = "";

        try {
            String receiptNum = messageService.sendSMS(testCorpNum, sender, content,
                    messages, reserveDT, adsYN, testUserID, requestNum);

            m.addAttribute("Result",receiptNum);

        } catch (PopbillException e) {
            m.addAttribute("Exception", e);
            return "exception";
        }

        return "result";
    }

    @RequestMapping(value = "sendLMS", method = RequestMethod.GET)
    public String sendLMS( Model m) {

        // 발신번호
        String sender = "07043042991";

        // 수신번호
        String receiver = "000111222";

        // 수신자명
        String receiverName = "수신자";

        // 문자메시지 제목
        String subject = "장문문자 제목";

        // 장문 메시지 내용, 2000byte 초과시 길이가 조정되어 전송됨
        String content = "장문 문자메시지 내용";

        // 예약문자 전송일시
        Date reserveDT = null;

        // 광고문자 전송여부
        Boolean adsYN = false;

        // 전송요청번호
        // 파트너가 전송 건에 대해 관리번호를 구성하여 관리하는 경우 사용.
        // 1~36자리로 구성. 영문, 숫자, 하이픈(-), 언더바(_)를 조합하여 팝빌 회원별로 중복되지 않도록 할당.
        String requestNum = "";

        try {

            String receiptNum = messageService.sendLMS(testCorpNum, sender, receiver,
                    receiverName, subject, content, reserveDT, adsYN, testUserID, requestNum);

            m.addAttribute("Result",receiptNum);

        } catch (PopbillException e) {
            m.addAttribute("Exception", e);
            return "exception";
        }

        return "result";
    }

    @RequestMapping(value = "sendLMS_Multi", method = RequestMethod.GET)
    public String sendLMS_Multi( Model m) {

        // [동보전송] 발신번호, 개별 전송정보의 발신번호가 없는 경우 적용
        String sender = "07043042991";

        // [동보전송] 메시지 제목, 개별 전송정보의 메시지 제목이 없는 경우 적용
        String subject = "대량전송 제목";

        // [동보전송] 메시지 내용, 개별 전송정보의 메시지 내용이 없는 경우 적용
        String content = "대량전송 내용";
        Date reserveDT = null;
        Boolean adsYN = true;

        // 개별 전송정보, 최대 1000건.
        Message msg1 = new Message();
        msg1.setSender("07043042991");
        msg1.setSenderName("발신자1");
        msg1.setReceiver("000111222");
        msg1.setReceiverName("수신자1");
        msg1.setSubject("장문 제목");
        msg1.setContent("메시지 내용1");

        Message msg2 = new Message();
        msg2.setSender("07043042991");
        msg2.setSenderName("발신자2");
        msg2.setReceiver("000111222");
        msg2.setReceiverName("수신자2");
        msg2.setSubject("장문 제목");
        msg2.setContent("메시지 내용2");

        Message[] messages = new Message[] { msg1, msg2 };

        // 전송요청번호
        // 파트너가 전송 건에 대해 관리번호를 구성하여 관리하는 경우 사용.
        // 1~36자리로 구성. 영문, 숫자, 하이픈(-), 언더바(_)를 조합하여 팝빌 회원별로 중복되지 않도록 할당.
        String requestNum = "";

        try {

            String receiptNum = messageService.sendLMS(testCorpNum, sender, subject,
                    content, messages, reserveDT, adsYN, testUserID, requestNum);

            m.addAttribute("Result",receiptNum);

        } catch (PopbillException e) {
            m.addAttribute("Exception", e);
            return "exception";
        }

        return "result";
    }

    @RequestMapping(value = "sendMMS", method = RequestMethod.GET)
    public String sendMMS( Model m) {

        // 발신번호
        String sender = "07043042991";

        // 수신번호
        String receiver = "010111222";

        // 수신자명
        String receiverName = "수신자";

        // 포토 문자 메시지 제목
        String subject = "포토 문자 제목";

        // 포토 문자 메시지, 2000 byte 초과시 길이가 조정됭 전송됨
        String content = "멀티 문자메시지 내용";

        // 전송할 이미지 파일, 300KByte 이하 JPG 포맷전송 가능.
        File file = new File("C:/test2.jpg");

        // 예약전송일시
        Date reserveDT = null;

        // 광고문자 전송여부
        Boolean adsYN = false;

        // 전송요청번호
        // 파트너가 전송 건에 대해 관리번호를 구성하여 관리하는 경우 사용.
        // 1~36자리로 구성. 영문, 숫자, 하이픈(-), 언더바(_)를 조합하여 팝빌 회원별로 중복되지 않도록 할당.
        String requestNum = "";

        try {

            String receiptNum = messageService.sendMMS(testCorpNum, sender, receiver,
                    receiverName, subject, content, file, reserveDT, adsYN, testUserID, requestNum);

            m.addAttribute("Result",receiptNum);

        } catch (PopbillException e) {
            m.addAttribute("Exception", e);
            return "exception";
        }

        return "result";
    }

    @RequestMapping(value = "sendMMS_Multi", method = RequestMethod.GET)
    public String sendMMS_Multi( Model m) {


        // [동보전송] 발신번호, 개별 전송정보의 발신번호가 없는 경우 적용
        String sender = "07043042991";

        // [동보전송] 메시지 제목, 개별 전송정보의 메시지 제목이 없는 경우 적용
        String subject = "대량전송 제목";

        // [동보전송] 메시지 내용, 개별 전송정보의 메시지 내용이 없는 경우 적용
        String content = "대량전송 메시지 내용";

        // 개별 전송정보, 최대 1000건.
        Message msg1 = new Message();
        msg1.setSender("07043042991");
        msg1.setSenderName("발신자1");
        msg1.setReceiver("010111222");
        msg1.setReceiverName("수신자1");
        msg1.setSubject("멀티 메시지 제목");
        msg1.setContent("메시지 내용1");

        Message msg2 = new Message();
        msg2.setSender("07043042991");
        msg2.setSenderName("발신자2");
        msg2.setReceiver("010333444");
        msg2.setReceiverName("수신자2");
        msg2.setSubject("멀티 메시지 제목");
        msg2.setContent("메시지 내용2");

        // 전송할 이미지 파일, 300KByte 이하 JPG 포맷전송 가능.
        File file = new File("C:/test2.jpg");

        // 예약전송일시
        Date reserveDT = null;

        // 광고문자 전송여부
        Boolean adsYN = false;

        Message[] messages = new Message[] {msg1,msg2};

        // 전송요청번호
        // 파트너가 전송 건에 대해 관리번호를 구성하여 관리하는 경우 사용.
        // 1~36자리로 구성. 영문, 숫자, 하이픈(-), 언더바(_)를 조합하여 팝빌 회원별로 중복되지 않도록 할당.
        String requestNum = "";

        try {
            String receiptNum = messageService.sendMMS(testCorpNum, sender, subject,
                    content, messages, file, reserveDT, adsYN, testUserID, requestNum);

            m.addAttribute("Result",receiptNum);

        } catch (PopbillException e) {
            m.addAttribute("Exception", e);
            return "exception";
        }

        return "result";
    }

    @RequestMapping(value = "sendXMS", method = RequestMethod.POST)
    @ResponseBody
    public Object sendXMS( Model m, HttpSession session, @RequestParam Map<String, Object> paramMap) {
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();

        // 사업자 번호
        String corpNum = sMap.get("com_num").toString();
        // 아이디
        String userID = sMap.get("id").toString();
        // 발신번호
        String sender = sMap.get("tel").toString();

        // 수신번호
//        String receiver = paramMap.get("receiver").toString();
        String receiver = "01045405327";

        // 수신자명
//        String receiverName = paramMap.get("clientName").toString();
        String receiverName = "천태훈";

        // 문자메시지 제목
//        String subject = "[" + sMap.get("com_name").toString() + "오늘 매출]";
        String subject = "[아뷰 오늘 매출]";

        // 문자메시지 내용, 90Byte를 기준으로 단문과 장문을 자동인식하여 전송됨.
        String content = "문자메시지 내용";

        // 예약전송 일시
//        String rDT = paramMap.get("reserveDT").toString();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date reserveDT = null;
        /* try {
            reserveDT = sdf.parse(rDT);
        } catch (ParseException e) {
            e.printStackTrace();
        } */

        // 광고문자 전송여부
        Boolean adsYN = false;

        // 전송요청번호
        // 파트너가 전송 건에 대해 관리번호를 구성하여 관리하는 경우 사용.
        // 1~36자리로 구성. 영문, 숫자, 하이픈(-), 언더바(_)를 조합하여 팝빌 회원별로 중복되지 않도록 할당.
        String requestNum = "";

        try {
            System.out.println("start ... ");

            System.out.println("testCorpNum ... " + corpNum);
            System.out.println("sender ... " + sender);
            System.out.println("receiver ... " + receiver);
            System.out.println("receiverName ... " + receiverName);
            System.out.println("subject ... " + subject);
            System.out.println("content ... " + content);
            System.out.println("reserveDT ... " + reserveDT);
            System.out.println("adsYN ... " + adsYN);
            System.out.println("testUserID ... " + userID);
            System.out.println("requestNum ... " + requestNum);

            String receiptNum = messageService.sendXMS(corpNum, sender, receiver,
                    receiverName, subject, content, reserveDT, adsYN, userID, requestNum);

            jobj.put("Result", receiptNum);
            jobj.put("code", 200);
            System.out.println("end ... ");

            System.out.println("receiptNum : " + receiptNum);

        } catch (PopbillException e) {
            System.out.println("exception ... ");

            jobj.put("code", 900);
            jobj.put("Result", e);

            System.out.println("e : " + e);
        }

        return jobj;
    }

    @RequestMapping(value = "sendXMS_Multi", method = RequestMethod.GET)
    public String sendXMS_Multi( Model m) {

        // [동보전송용] 발신번호, 개별 전송정보에 발신번호가 없는 경우 적용
        String sender = "07043042991";

        // [동보전송용] 문자메시지 제목, 개별 전송정보에 메시지 제목이 없는 경우 적용
        String subject = "장문문자 제목";

        // [동보전송용] 문자메시지 내용, 90Byte를 기준으로 단문과 장문을 자동인식하여 전송됨.
        String content = "문자메시지 내용";

        // 예약전송 일시
        Date reserveDT = null;

        // 광고문자 전송여부
        Boolean adsYN = true;

        // 개별 전송정보, 최대 1000건.
        Message msg1 = new Message();
        msg1.setSender("07043042991");
        msg1.setSenderName("발신자1");
        msg1.setReceiver("000111222");
        msg1.setReceiverName("수신자1");
        msg1.setContent("메시지 내용1");

        Message msg2 = new Message();
        msg2.setSender("07043042991");
        msg2.setSenderName("발신자2");
        msg2.setReceiver("000111222");
        msg2.setReceiverName("수신자2");
        msg2.setSubject("장문 제목");
        msg2.setContent("메시지 내용2");

        Message[] messages = new Message[] {msg1,msg2};

        // 전송요청번호
        // 파트너가 전송 건에 대해 관리번호를 구성하여 관리하는 경우 사용.
        // 1~36자리로 구성. 영문, 숫자, 하이픈(-), 언더바(_)를 조합하여 팝빌 회원별로 중복되지 않도록 할당.
        String requestNum = "";

        try {

            String receiptNum = messageService.sendXMS(testCorpNum, sender, subject,
                    content, messages, reserveDT, adsYN, testUserID, requestNum);

            m.addAttribute("Result",receiptNum);

        } catch (PopbillException e) {
            m.addAttribute("Exception", e);
            return "exception";
        }

        return "result";
    }

    @RequestMapping(value = "getMessages", method = RequestMethod.GET)
    @ResponseBody
    public Object getMessages( Model m, @RequestParam Map<String, Object> paramMap, HttpSession session) {
        /**
         * 문자전송요청시 발급받은 접수번호(receiptNum)로 전송상태를 확인합니다
         * - 응답항목에 대한 자세한 사항은 "[문자 API 연동매뉴얼] >
         * 3.3.1. GetMessages (전송내역 확인)을 참조하시기 바랍니다.
         */
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        // 문자전송 접수번호
        String receiptNum = paramMap.get("number").toString();

        try {

            SentMessage[] sentMessages = messageService.getMessages(sMap.get("com_num").toString(), receiptNum);

            jobj.put("SentMessages",sentMessages);
            jobj.put("code", 200);
        } catch (PopbillException e) {
            jobj.put("Exception", e);
            jobj.put("code", 900);
        }

        return jobj;
    }

    @RequestMapping(value = "getMessagesRN", method = RequestMethod.GET)
    public String getMessagesRN( Model m) {
        /**
         * 문자전송요청시 할당한 전송요청번호(requestNum)로 전송상태를 확인합니다
         * - 응답항목에 대한 자세한 사항은 "[문자 API 연동매뉴얼] >
         * 3.3.2. GetMessagesRN (전송내역 확인 - 요청번호 할당)을 참조하시기 바랍니다.
         */

        // 문자전송 요청 시 할당한 전송요청번호(requestNum)
        String requestNum = "";

        try {

            SentMessage[] sentMessages = messageService.getMessagesRN(testCorpNum, requestNum);

            m.addAttribute("SentMessages",sentMessages);

        } catch (PopbillException e) {
            m.addAttribute("Exception", e);
            return "exception";
        }

        return "Message/SentMessage";
    }

    @RequestMapping(value = "cancelReserve", method = RequestMethod.GET)
    @ResponseBody
    public Object cancelReserve(@RequestParam Map<String, Object> paramMap) {
        /**
         * 문자전송요청시 발급받은 접수번호(receiptNum)로
         * 예약문자 전송을 취소합니다.
         * - 예약취소는 예약전송시간 10분전까지만 가능합니다.
         */

        // 예약문자전송 접수번호
        String receiptNum = paramMap.get("receipt_num").toString();
        JSONObject jobj = new JSONObject();

        try {
            Response response = messageService.cancelReserve(testCorpNum, receiptNum);
            jobj.put("code", 200);
            jobj.put("response", response);

        } catch (PopbillException e) {
            jobj.put("code", 900);
            jobj.put("response", e.getMessage());
        }

        return jobj;
    }

    @RequestMapping(value = "cancelReserveRN", method = RequestMethod.GET)
    public String cancelReserveRN( Model m) {
        /**
         * 문자전송요청시 할당한 전송요청번호(requestNum)로
         * 예약문자 전송을 취소합니다.
         * - 예약취소는 예약전송시간 10분전까지만 가능합니다.
         */

        // 예약문자전송 요청시 할당한 전송요청번호
        String requestNum = "";

        try {
            Response response = messageService.cancelReserveRN(testCorpNum, requestNum);

            m.addAttribute("Response",response);

        } catch (PopbillException e) {
            m.addAttribute("Exception", e);
            return "exception";
        }

        return "response";
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public String search(Model m) {
        /**
         * 검색조건을 사용하여 문자전송 내역을 조회합니다.
         * - 최대 검색기간 : 6개월 이내
         */

        // 시작일자, 날짜형식(yyyyMMdd)
        String SDate = "20180701";

        // 종료일자, 날짜형식(yyyyMMdd)
        String EDate = "20180720";

        // 전송상태 배열, 1-대기, 2-성공, 3-실패, 4-취소
        String[] State = {"1", "2", "3","4"};

        // 검색대상 배열, SMS-단문, LMS-장문, MMS-포토
        String[] Item = {"SMS", "LMS", "MMS"};

        // 예약여부, false-전체조회, true-예약전송건 조회
        Boolean ReserveYN = false;

        // 개인조회 여부, false-전체조회, true-개인조회
        Boolean SenderYN = false;

        // 페이지 번호
        int Page = 1;

        // 페이지당 목록개수 (최대 1000건)
        int PerPage = 20;

        // 정렬방향 D-내림차순, A-오름차순
        String Order = "D";

        // 조회 검색어.
        // 문자 전송시 입력한 발신자명 또는 수신자명 기재.
        // 조회 검색어를 포함한 발신자명 또는 수신자명을 검색합니다.
        String QString = "";

        try {

            MSGSearchResult response = messageService.search(testCorpNum, SDate,
                    EDate, State, Item, ReserveYN, SenderYN, Page, PerPage, Order, QString);

            m.addAttribute("SearchResult",response);

        } catch (PopbillException e) {
            m.addAttribute("Exception", e);
            return "exception";
        }
        return "Message/SearchResult";
    }

    @RequestMapping(value = "autoDenyList", method = RequestMethod.GET)
    public String getAutoDenyList(Model m){
        /**
         * 080 서비스 수신거부 목록을 확인합니다.
         */

        try {
            AutoDeny[] autoDenyList = messageService.getAutoDenyList(testCorpNum);
            m.addAttribute("AutoDenyList", autoDenyList);
        } catch (PopbillException e) {
            m.addAttribute("Exception", e);
            return "exception";
        }
        return "Message/AutoDeny";
    }

    @RequestMapping(value = "getSenderNumberList", method = RequestMethod.GET)
    @ResponseBody
    public Object getSenderNumberList(Model m, HttpSession session){
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        /**
         * 발신번호 목록을 확인합니다.
         */
        try {
            SenderNumber[] senderNumberList = messageService.getSenderNumberList(sMap.get("com_num").toString());
            jobj.put("SenderNumberList", senderNumberList);
            jobj.put("code", 200);
        } catch (PopbillException e) {
            jobj.put("Exception", e);
            jobj.put("code", 900);
            e.printStackTrace();
        }
        return jobj;
    }

    @RequestMapping(value = "getStates", method = RequestMethod.GET)
    public Object getStates(Model m, @RequestParam Map<String, Object> paramMap) {
        /**
         * 문자전송요청에 대한 전송결과를 확인합니다.
         */

        // 문자전송 접수번호 배열
//        String[] ReceiptNumList = new String[] {"018041717000000018"};
        String[] ReceiptNumList = new String[]{"018041717000000018"};
        JSONObject jobj = new JSONObject();

        try {

            MessageBriefInfo[] messageBriefInfos = messageService.getStates(testCorpNum, ReceiptNumList);

            m.addAttribute("MessageBriefInfos", messageBriefInfos);

        } catch (PopbillException e) {
            m.addAttribute("Exception", e);
            return "exception";
        }

        return "Message/MessageBriefInfo";
    }

    @RequestMapping("getStatesBatchCheck.do")
    @ResponseBody
    public Object getStatesBatchCheck(@RequestParam Map<String, Object> paramMap) {
        log.debug("popbill from paramMap : " + paramMap);
        JSONObject jobj = new JSONObject();
        Map<String, List<Map<String, Object>>> content = new HashMap<>();
        /**
         * 문자전송요청에 대한 전송결과를 확인합니다.
         */
        Gson gson = new Gson();
        String comNum = "";
        String id = "";
        String shopIdx = "";
        List<Map<String, List<String>>> receiptList = gson.fromJson(paramMap.get("receiptNums").toString(), List.class);

        try {
            System.out.println("receiptList size : " + receiptList.size());
            if (receiptList.size() > 0) {
                for (int i = 0; i < receiptList.size(); i++) {
                    for (String key : receiptList.get(i).keySet()) {
                        // 문자전송 접수번호 배열
                        List<String> ReceiptNumList = gson.fromJson(receiptList.get(i).get(key).toString(), List.class);
                        String[] receiptArray = ReceiptNumList.toArray(new String[ReceiptNumList.size()]);

                        String[] tmps = key.split("@");
                        comNum = tmps[0];
                        id = tmps[1];
                        shopIdx = tmps[2];
                        for (int j = 0; j < receiptArray.length; j++) {
                            SentMessage[] messageBriefInfos = messageService.getMessages(comNum, receiptArray[j].substring(0, receiptArray[j].indexOf("@")));
//                            log.debug(messageBriefInfos);
                            for (int k = 0; k < messageBriefInfos.length; k++) {
                                List<Map<String, Object>> list = new ArrayList<>();

                                Map<String, Object> map = new HashMap<>();
                                String method = String.valueOf(messageBriefInfos[k].getMessageType());
                                map.put("status", messageBriefInfos[k].getState().toString());
                                map.put("type", method);

                                if(messageBriefInfos[k].getResult() != null){
                                    map.put("result", messageBriefInfos[k].getResult().toString());
                                } else {
                                    map.put("result", "null");
                                }

                                System.out.println("================================================");
                                log.debug(receiptArray[j]);
                                System.out.println(receiptArray[j].substring(0, receiptArray[j].indexOf("@")) + " - status : " +messageBriefInfos[k].getState());
                                System.out.println(receiptArray[j].substring(0, receiptArray[j].indexOf("@")) + " - result : " +messageBriefInfos[k].getResult());
                                System.out.println(receiptArray[j].substring(0, receiptArray[j].indexOf("@")) + " - type : " +messageBriefInfos[k].getMessageType());
                                System.out.println("================================================\n");

                                String responseKey = receiptArray[j] + "@" + comNum + "@" + id + "@" + shopIdx;
                                // 중복
                                if (content.containsKey(responseKey)) {
                                    List<Map<String, Object>> tmpList = content.get(responseKey);
                                    tmpList.add(map);
                                    content.put(responseKey, tmpList);
                                }
                                // 최초
                                else {
                                    list.add(map);
                                    content.put(responseKey, list);
                                }
                            }
                        }
                        jobj.put("msgStatus", content);
                        jobj.put("code", 200);      // 조회 성공
                    }
                }
            } else {
                System.out.println("code 500");
                jobj.put("code", 500);          // receipt_num 값 없음
            }
        } catch (PopbillException e) {
            e.printStackTrace();
            jobj.put("code", 901);      // 조회 실패
            jobj.put("exception", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 900);      // 조회 실패
            jobj.put("exception", e.getMessage());
        }
        log.debug("response : " + jobj);
        return jobj;
    }

    @RequestMapping("sendAutoMessageBatchs.do")
    @ResponseBody
    public Object sendAutoMessageBatchs(@RequestParam Map<String, Object> paramMap){
        log.debug("param : " + paramMap);
        CommonUtils utils = new CommonUtils();
        Gson gson = new Gson();
        JSONObject jobj = new JSONObject();
        Map<String, List<Map<String,Object>>> paramCast = gson.fromJson(paramMap.get("paramCast").toString(), Map.class);
        Map<String, Object> tmpMap = new HashMap<>();
        List<Map<String, Object>> prepaidList = paramCast.get("prepaid");
        List<Map<String, Object>> ticketList = paramCast.get("ticket");
        List<Map<String, Object>> brithList = paramCast.get("brith");

        utils.printList(prepaidList);
        utils.printList(ticketList);
        utils.printList(brithList);


        try{
            // PREPAID SEND
            for(int i  = 0; i < prepaidList.size(); i++){
                tmpMap = new HashMap<>();
                String receiptNum = "";
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date rDate = transFormat.parse(prepaidList.get(i).get("rDate").toString());
                if("sms".equals(prepaidList.get(i).get("method").toString())){
                    receiptNum = messageService.sendSMS(Integer.toString(utils.parseObjectToInt(prepaidList.get(i).get("comNum"))), prepaidList.get(i).get("tel").toString(), prepaidList.get(i).get("clientPhone").toString(), prepaidList.get(i).get("clientName").toString(), prepaidList.get(i).get("content").toString(), rDate, prepaidList.get(i).get("shopId").toString());
                } else {
                    receiptNum = messageService.sendLMS(Integer.toString(utils.parseObjectToInt(prepaidList.get(i).get("comNum"))), prepaidList.get(i).get("tel").toString(), prepaidList.get(i).get("clientPhone").toString(), prepaidList.get(i).get("clientName").toString(), "", prepaidList.get(i).get("content").toString(), rDate, prepaidList.get(i).get("shopId").toString());
                }
                System.out.println("receitpNum : " + receiptNum);
                tmpMap = prepaidList.get(i);
                tmpMap.put("receiptNum", "" + receiptNum + "");
                prepaidList.set(i, tmpMap);
            }
            // TICKET SEND
            for(int i  = 0; i < ticketList.size(); i++){
                String receiptNum = "";
                tmpMap = new HashMap<>();
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date rDate = transFormat.parse(ticketList.get(i).get("rDate").toString());
                if("sms".equals(ticketList.get(i).get("method").toString())){
                    receiptNum = messageService.sendSMS(Integer.toString(utils.parseObjectToInt(ticketList.get(i).get("comNum"))), ticketList.get(i).get("tel").toString(), ticketList.get(i).get("clientPhone").toString(), ticketList.get(i).get("clientName").toString(), ticketList.get(i).get("content").toString(), rDate, ticketList.get(i).get("shopId").toString());
                } else {
                    receiptNum = messageService.sendLMS(Integer.toString(utils.parseObjectToInt(ticketList.get(i).get("comNum"))), ticketList.get(i).get("tel").toString(), ticketList.get(i).get("clientPhone").toString(), ticketList.get(i).get("clientName").toString(), "", ticketList.get(i).get("content").toString(), rDate, ticketList.get(i).get("shopId").toString());
                }
                System.out.println("receitpNum : " + receiptNum);
                tmpMap = ticketList.get(i);
                tmpMap.put("receiptNum", receiptNum);
                ticketList.set(i, tmpMap);
            }
            // BRITH SEND
            for(int i  = 0; i < brithList.size(); i++){
                String receiptNum = "";
                tmpMap = new HashMap<>();
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date rDate = transFormat.parse(brithList.get(i).get("rDate").toString());
                if("sms".equals(brithList.get(i).get("method").toString())){
                    receiptNum = messageService.sendSMS(Integer.toString(utils.parseObjectToInt(brithList.get(i).get("comNum"))), brithList.get(i).get("tel").toString(), brithList.get(i).get("clientPhone").toString(), brithList.get(i).get("clientName").toString(), brithList.get(i).get("content").toString(), rDate, brithList.get(i).get("shopId").toString());
                } else {
                    receiptNum = messageService.sendLMS(Integer.toString(utils.parseObjectToInt(brithList.get(i).get("comNum"))), brithList.get(i).get("tel").toString(), brithList.get(i).get("clientPhone").toString(), brithList.get(i).get("clientName").toString(), "", brithList.get(i).get("content").toString(), rDate, brithList.get(i).get("shopId").toString());
                }
                System.out.println("receitpNum : " + receiptNum);
                tmpMap = brithList.get(i);
                tmpMap.put("receiptNum", receiptNum);
                brithList.set(i, tmpMap);
            }

            jobj.put("prepaid", prepaidList);
            jobj.put("ticket", ticketList);
            jobj.put("brith", brithList);
            jobj.put("code", 200);
        } catch(Exception e){
            jobj.put("code", 900);
            e.printStackTrace();
        }

        log.debug("jobj : " + jobj);
        return jobj;
    }
}
