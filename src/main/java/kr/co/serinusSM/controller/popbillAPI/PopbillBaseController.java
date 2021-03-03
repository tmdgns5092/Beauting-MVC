/*
 * 팝빌 Java SDK SpringMVC Example
 *
 * - SpringMVC SDK 연동환경 설정방법 안내 : http://blog.linkhub.co.kr/591/
 * - 업데이트 일자 : 2017-11-14
 * - 연동 기술지원 연락처 : 1600-9854 / 070-4304-2991~2
 * - 연동 기술지원 이메일 : code@linkhub.co.kr
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

import com.popbill.api.*;
import kr.co.serinusSM.common.CommonUtils;
import kr.co.serinusSM.service.CommonService;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 팝빌 BaseService API 예제.
 */
@Controller
@RequestMapping("BaseService")
public class PopbillBaseController {

    @Autowired
    private TaxinvoiceService taxinvoiceService;
    private BaseService baseService;

    @Resource(name = "commonService")
    private CommonService commonService;

    // 팝빌회원 사업자번호
    private String testCorpNum = "6173622290";
    // 팝빌회원 아이디
    private String testUserID = "MansOfBeauty";
    // 링크아이디
    private String testLinkID = "MOB";

    Logger log = Logger.getLogger(this.getClass());

    /* 회원 여부 확인 */
    @RequestMapping(value = "checkIsMember", method = RequestMethod.GET)
    @ResponseBody
    public Object checkIsMember(@RequestParam Map<String, Object> paramMap) {
        log.debug("checkIsMember : " + paramMap);
        JSONObject jobj = new JSONObject();
        printObjects();

        /*
         *  해당 사업자의 파트너 연동회원 가입여부를 확인합니다.
         * - LinkID는 인증정보로 설정되어 있는 링크아이디 값입니다.
         */

        // 조회할 사업자번호, '-' 제외 10자리
        try {
            Response response = taxinvoiceService.checkIsMember(paramMap.get("corpNum").toString(), testLinkID);
            jobj.put("Response", response);
            jobj.put("code", 200);

            System.out.println("response code : " + response.getCode());
            System.out.println("response Message : " + response.getMessage());
        } catch (PopbillException e) {
            e.printStackTrace();
            jobj.put("Exception", e.getMessage());
            jobj.put("code", 900);
        }

        System.out.println("json : " + jobj);

        return jobj;
    }

    /* 연동회원 잔여포인트 */
    @RequestMapping(value = "getBalance", method = RequestMethod.GET)
    @ResponseBody
    public Object getBalance(HttpSession session) {
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        printObjects();
        /**
         * 연동회원의 잔여포인트를 확인합니다.
         * - 과금방식이 파트너과금인 경우 파트너 잔여포인트(GetPartnerBalance API)
         *   를 통해 확인하시기 바랍니다.
         */

        try {
            String comNum = "";
            comNum = sMap.get("com_num").toString();

            if (comNum != null && comNum.length() != 0) {
                System.out.println("param is not null !!! ComNum is " + comNum);
            } else {
                System.out.println("param is null !!!");
            }
            System.out.println(taxinvoiceService.getBalance(comNum));

            double remainPoint = taxinvoiceService.getBalance(comNum);
            jobj.put("Result", remainPoint);
            jobj.put("code", 200);
        } catch (PopbillException e) {
            System.out.println("popbillExcpetion ....");
            e.printStackTrace();
            jobj.put("Exception", e.getMessage());
            jobj.put("code", 900);
        } catch (Exception je) {
            System.out.println("javaException ....");
            je.printStackTrace();
        }

        return jobj;
    }

    /* 파트너 잔여포인트 확인*/
    @RequestMapping(value = "getPartnerBalance", method = RequestMethod.GET)
    @ResponseBody
    public Object getPartnerBalance() {
        JSONObject jobj = new JSONObject();
        printObjects();
        /**
         * 파트너의 잔여포인트를 확인합니다.
         * - 과금방식이 연동과금인 경우 연동회원 잔여포인트(GetBalance API)를
         *   이용하시기 바랍니다.
         */

        try {
            double remainPoint = taxinvoiceService.getPartnerBalance(testCorpNum);

            jobj.put("Result", remainPoint);
            jobj.put("code", 200);
        } catch (PopbillException e) {
            jobj.put("Exception", e.getMessage());
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 파트너 충전 팝업 */
    @RequestMapping(value = "getPartnerURL", method = RequestMethod.GET)
    @ResponseBody
    public Object getPartnerURL() {
        JSONObject jobj = new JSONObject();
        printObjects();
        /**
         * 파트너 포인트 충전 팝업 URL을 반환합니다.
         * - 보안정책에 따라 반환된 URL은 30초의 유효시간을 갖습니다.
         */

        // CHRG : 포인트 충전
        String TOGO = "CHRG";

        try {

            String url = taxinvoiceService.getPartnerURL(testCorpNum, TOGO);

            jobj.put("Result", url);
            jobj.put("code", 200);
        } catch (PopbillException e) {
            jobj.put("Exception", e.getMessage());
            jobj.put("code", 200);
        }

        return jobj;
    }

    /* 팝빌 로그인 URL */
    @RequestMapping(value = "getPopbillURL_LOGIN", method = RequestMethod.GET)
    @ResponseBody
    public Object getPopbillURL_LOGIN() {
        JSONObject jobj = new JSONObject();
        printObjects();
        /**
         * 팝빌 로그인 URL을 반환합니다.
         * - 보안정책에 따라 반환된 URL은 30초의 유효시간을 갖습니다.
         */

        String TOGO = "LOGIN";

        try {

            String url = taxinvoiceService.getPopbillURL(testCorpNum, TOGO);

            jobj.put("Result", url);
            jobj.put("code", 200);
        } catch (PopbillException e) {
            jobj.put("Exception", e.getMessage());
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 연동회원 포인트 충전 팝업 URL */
    @RequestMapping(value = "getPopbillURL_CHRG", method = RequestMethod.GET)
    @ResponseBody
    public Object getPopbillURL_CHRG(HttpSession session) {
        Map<String, Object> sMap = (Map<String ,Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        printObjects();
        /**
         * 팝빌 연동회원 포인트충전 팝업 URL을 반환합니다.
         * - 보안정책에 따라 반환된 URL은 30초의 유효시간을 갖습니다.
         */

        String TOGO = "CHRG";

        try {
            String url = taxinvoiceService.getPopbillURL(sMap.get("com_num").toString(), TOGO);

            jobj.put("Result", url);
            jobj.put("code", 200);

//            commonService.updateMsgPoint(sMap);
        } catch (PopbillException e) {
            jobj.put("Exception", e.getMessage());
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 기본 팝업 URL */
    @RequestMapping(value = "getPopbillURL", method = RequestMethod.GET)
    @ResponseBody
    public Object getPopbillURL() {
        JSONObject jobj = new JSONObject();
        printObjects();

        /**
         * 팝빌 관련 기본 팝업 URL을 반환합니다.
         * - 보안정책에 따라 반환된 URL은 30초의 유효시간을 갖습니다.
         */

        // CHRG : 포인트 충전, LOGIN : 메인 , CERT : 공인인증서 등록
        String TOGO = "CHRG";

        try {

            String url = taxinvoiceService.getPopbillURL(testCorpNum, TOGO);

            jobj.put("Result", url);
            jobj.put("code", 200);

        } catch (PopbillException e) {
            jobj.put("Exception", e.getMessage());
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 연동회원 가입 요청 */
    @RequestMapping("joinMember.do")
    @ResponseBody
    public Object joinMember(@RequestParam Map<String, Object> paramMap) {
        log.debug("popbill joinMemeber : " + paramMap);
        JSONObject jobj = new JSONObject();

        /**
         * 파트너의 연동회원으로 회원가입을 요청합니다.
         */

        JoinForm joinInfo = new JoinForm();
        // 링크아이디
        joinInfo.setLinkID(testLinkID);
        // 사업자등록번호
        joinInfo.setCorpNum(paramMap.get("comNum").toString());
        // 대표자성명
        joinInfo.setCEOName(paramMap.get("ceoName").toString());
        // 상호
        joinInfo.setCorpName(paramMap.get("comName").toString());
        // 주소
        joinInfo.setAddr(paramMap.get("addresLg").toString() + paramMap.get("addresSm").toString() + paramMap.get("postCode").toString());
        // 업태 ex.네일아트
        joinInfo.setBizType(paramMap.get("item").toString());
        // 종목 ex.네일아트
        joinInfo.setBizClass(paramMap.get("bc").toString());
        // 팝빌회원 아이디
        joinInfo.setID(paramMap.get("id").toString());
        // 팝빌회원 비밀번호
        joinInfo.setPWD(paramMap.get("password").toString());
        // 담당자명
        joinInfo.setContactName(paramMap.get("ceoName").toString());
        // 담당자 연락처
        joinInfo.setContactTEL(paramMap.get("fullTel").toString());
        // 담당자 메일주소
        joinInfo.setContactEmail(paramMap.get("email1").toString() + "@" + paramMap.get("email2").toString());

        try {
            System.out.println("join info : " + joinInfo);
            System.out.println("join linkID : " + joinInfo.getLinkID());
            System.out.println("join comNum : " + joinInfo.getCorpNum());
            System.out.println("join ceoName : " + joinInfo.getCEOName());
            System.out.println("join comName : " + joinInfo.getCorpName());
            System.out.println("join address : " + joinInfo.getAddr());
            System.out.println("join item : " + joinInfo.getBizType());
            System.out.println("join bc : " + joinInfo.getBizClass());
            System.out.println("join id : " + joinInfo.getID());
            System.out.println("join password : " + joinInfo.getPWD());
            System.out.println("join ceoName : " + joinInfo.getCEOName());
            System.out.println("join tel : " + joinInfo.getContactTEL());
            System.out.println("join email : " + joinInfo.getContactEmail());

            Response response = taxinvoiceService.joinMember(joinInfo);

            jobj.put("Response", response);
            jobj.put("code", 200);
        } catch (PopbillException e) {
            e.printStackTrace();
            jobj.put("Exception", e.getMessage());
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 연동회원 담당자 확인 */
    @RequestMapping(value = "listContact", method = RequestMethod.GET)
    @ResponseBody
    public Object listContact() {
        JSONObject jobj = new JSONObject();
        printObjects();

        /**
         * 연동회원의 담당자 목록을 확인합니다.
         */

        try {
            ContactInfo[] response = taxinvoiceService.listContact(testCorpNum);

            jobj.put("ContactInfos", response);
            jobj.put("code", 200);
        } catch (PopbillException e) {
            jobj.put("Exception", e.getMessage());
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 연동회원 담당자 정보 수정 */
    @RequestMapping(value = "updateContact", method = RequestMethod.GET)
    @ResponseBody
    public Object updateContact() {
        JSONObject jobj = new JSONObject();
        printObjects();

        /**
         * 연동회원의 담당자 정보를 수정합니다.
         */

        ContactInfo contactInfo = new ContactInfo();

        // 담당자 아이디
        contactInfo.setId(testUserID);

        // 담당자 이메일주소
        contactInfo.setEmail("test1234@test.com");

        // 담당자 팩스번호
        contactInfo.setFax("070-4304-2991");

        // 담당자 휴대폰번호
        contactInfo.setHp("010-1234-1234");

        // 담당자명
        contactInfo.setPersonName("담당지 수정 테스트");

        // 담당자 연락처
        contactInfo.setTel("070-1234-1234");

        // 회사조회 권한여부, true-회사조회, false-개인조회
        contactInfo.setSearchAllAllowYN(true);

        try {

            Response response = taxinvoiceService.updateContact(testCorpNum,
                    contactInfo, testUserID);

            jobj.put("Response", response);
            jobj.put("code", 200);
        } catch (PopbillException e) {
            jobj.put("Exception", e.getMessage());
            jobj.put("code", 200);
        }

        return jobj;
    }

    /* 연동회원 담당자 신규 등록 */
    @RequestMapping(value = "registContact", method = RequestMethod.GET)
    @ResponseBody
    public Object registContact() {
        JSONObject jobj = new JSONObject();
        printObjects();

        /**
         * 연동회원의 담당자를 신규로 등록합니다.
         */


        ContactInfo contactInfo = new ContactInfo();

        // 담당자 아이디
        contactInfo.setId("testkorea");

        // 담당자 비밀번호
        contactInfo.setPwd("test12341234");

        // 담당자 이메일주소
        contactInfo.setEmail("test1234@test.com");

        // 담당자 팩스번호
        contactInfo.setFax("070-4304-2991");

        // 담당자 휴대폰번호
        contactInfo.setHp("010-1234-1234");

        // 담당자명
        contactInfo.setPersonName("담당지 수정 테스트");

        // 담당자 연락처
        contactInfo.setTel("070-1234-1234");

        try {

            Response response = taxinvoiceService.registContact(testCorpNum,
                    contactInfo);

            jobj.put("Response", response);
            jobj.put("code", 200);

        } catch (PopbillException e) {
            e.printStackTrace();
            jobj.put("Exception", e.getMessage());
            jobj.put("code", 900);
        }

        return jobj;
    }


    /* 회원 아이디 중복 확인 */
    @RequestMapping(value = "checkID", method = RequestMethod.GET)
    @ResponseBody
    public Object checkID() {
        JSONObject jobj = new JSONObject();
        printObjects();

        /**
         * 팝빌 회원아이디 중복여부를 확인합니다.
         */

        try {

            Response response = taxinvoiceService.checkID(testUserID);
            jobj.put("Response", response);
            jobj.put("code", 200);

        } catch (PopbillException e) {
            jobj.put("Exception", e.getMessage());
            jobj.put("code", 900);
        }
        return jobj;
    }

    /* 연동회원 회사정보 확인 */
    @RequestMapping(value = "getCorpInfo", method = RequestMethod.GET)
    @ResponseBody
    public Object getCorpInfo() {
        JSONObject jobj = new JSONObject();
        printObjects();

        /**
         * 연동회원의 회사정보를 확인합니다.
         */

        try {
            CorpInfo response = taxinvoiceService.getCorpInfo(testCorpNum);
            jobj.put("CorpInfo", response);
            jobj.put("code", 200);
        } catch (PopbillException e) {
            jobj.put("Exception", e.getMessage());
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 연동회원 회사정보 수정 */
    @RequestMapping(value = "updateCorpInfo", method = RequestMethod.GET)
    @ResponseBody
    public Object updateCorpInfo(Model m) {
        JSONObject jobj = new JSONObject();
        printObjects();

        /**
         * 연동회원의 회사정보를 수정합니다
         */

        CorpInfo corpInfo = new CorpInfo();

        // 주소, 최대 300자
        corpInfo.setAddr("주소 수정 테스트");

        // 종목, 최대 40자
        corpInfo.setBizClass("업종 수정 테스트");

        // 업태, 최대 40자
        corpInfo.setBizType("업태 수정 테스트");

        // 대표자 성명, 최대 30자
        corpInfo.setCeoname("대표자명 수정 테스트");

        // 상호, 최대 70자
        corpInfo.setCorpName("상호 수정 테스트");

        try {
            Response response = taxinvoiceService.updateCorpInfo(testCorpNum,
                    corpInfo);
            jobj.put("Response", response);
            jobj.put("code", 200);

        } catch (PopbillException e) {
            jobj.put("Exception", e.getMessage());
            jobj.put("code", 900);
        }

        return jobj;
    }

    public void printObjects() {
        System.out.println("testCorpNum : " + testCorpNum);
        System.out.println("testUserID : " + testUserID);
        System.out.println("testLinkID : " + testLinkID);
    }
}
