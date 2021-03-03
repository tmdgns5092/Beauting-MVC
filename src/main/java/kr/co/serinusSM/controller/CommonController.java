package kr.co.serinusSM.controller;

import com.google.gson.Gson;
import kr.co.serinusSM.common.CommonUtils;
import kr.co.serinusSM.common.Paging;
import kr.co.serinusSM.service.CommonService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CommonController {

    Logger log = Logger.getLogger(this.getClass());
    @Resource(name = "commonService")
    private CommonService commonService;
    @Resource(name = "commonUtils")
    private CommonUtils commonUtils;


    /* 페이지 인덱스 */
    @RequestMapping("index")
    public ModelAndView index(@RequestParam Map<String, Object> paramMap) {
        ModelAndView mv = new ModelAndView("index");
        log.debug("index: " + paramMap);

        return mv;
    }
    /* 서비스 소개 */
    @RequestMapping("introduce")
    public ModelAndView introduce(@RequestParam Map<String, Object> paramMap) {
        ModelAndView mv = new ModelAndView("/common/introduce");
        log.debug("introduce: " + paramMap);

        return mv;
    }

    /* 고객 센터 */
    @RequestMapping("servicecenter")
    public ModelAndView servicecenter(@RequestParam Map<String, Object> paramMap) {
        ModelAndView mv = new ModelAndView("/common/servicecenter");
        log.debug("servicecenter: " + paramMap);

        return mv;
    }

    /* 이벤트 */
    @RequestMapping("event")
    public ModelAndView event(@RequestParam Map<String, Object> paramMap) {
        ModelAndView mv = new ModelAndView("/common/event");
        log.debug("event: " + paramMap);

        return mv;
    }

    /* 이용 안내 */
    @RequestMapping("manual")
    public ModelAndView manual(@RequestParam Map<String, Object> paramMap) {
        ModelAndView mv = new ModelAndView("/common/manual");
        log.debug("manual: " + paramMap);

        return mv;
    }

    /* 페이지 회원가입02 */
    @RequestMapping("loginChoice")
    public ModelAndView loginChoice(@RequestParam Map<String, Object> paramMap) {
        ModelAndView mv = new ModelAndView("/common/loginChoice");
        log.debug("loginChoice: " + paramMap);

        return mv;
    }

    /* 페이지 로그인 */
    @RequestMapping("login")
    public ModelAndView login(@RequestParam Map<String, Object> paramMap, HttpSession session, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/common/login");

        System.out.println(paramMap);
        Map<String,? > flashMap = RequestContextUtils.getInputFlashMap(request);
        System.out.println(flashMap);


        return mv;
    }

    /* 페이지 회원가입01 이용약관 */
    @RequestMapping("joinTerms")
    public ModelAndView joinTerms(@RequestParam Map<String, Object> paramMap) {
        ModelAndView mv = new ModelAndView("/common/joinTerms");
        log.debug("joinTerms: " + paramMap);

        return mv;
    }

    /* 페이지 회원가입02 */
    @RequestMapping("joinManager")
    public ModelAndView joinManager(@RequestParam Map<String, Object> paramMap) {
        ModelAndView mv = new ModelAndView("/common/joinManager");
        log.debug("joinManager: " + paramMap);

        return mv;
    }

    /* 페이지 매장 추가 */
//    @RequestMapping(value = "addShop", method = RequestMethod.POST)
    @RequestMapping("addShop")
    public ModelAndView addShop(@RequestParam Map<String, Object> paramMap) {
        ModelAndView mv = new ModelAndView("/common/addShop");
        log.debug("joinManager: " + paramMap);
        mv.addObject("id", paramMap.get("id"));

        return mv;
    }

    /* 매장 설정 페이지 */
    @RequestMapping("setting")
    public ModelAndView setting(@RequestParam Map<String, Object> paramMap) {
        ModelAndView mv = new ModelAndView("/common/setting");
        log.debug("setting: " + paramMap);
        return mv;
    }

    /* 발신자 표시 페이지 팝업 */
    @RequestMapping("getCall")
    public ModelAndView getCall(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        ModelAndView mv = new ModelAndView("/common/getCall");
        log.debug("getCall: " + paramMap);
        Map<String, Object> cMap = null;
        Gson gson = new Gson();


        try {
            if (paramMap.containsKey("phoneNum") && !"P".equals(paramMap.get("phoneNum").toString())) {
                Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
                paramMap.put("id", sMap.get("id"));
                cMap = commonService.selectClientInfo(paramMap);
                // 등록 고객
                if (cMap != null) {
                    if (cMap.containsKey("prepaid"))
                        cMap.put("prepaid", gson.fromJson(cMap.get("prepaid").toString(), List.class));
                    if (cMap.containsKey("ticket"))
                        cMap.put("ticket", gson.fromJson(cMap.get("ticket").toString(), List.class));
                    if (cMap.containsKey("product"))
                        cMap.put("product", gson.fromJson(cMap.get("product").toString(), List.class));

                    paramMap.put("client_code", cMap.get("code"));
                    paramMap.put("client_idx", cMap.get("idx"));
                    paramMap.put("status", 2);
                    mv.addObject("code", 200);
                }
                // 미등록 고객
                else {
                    cMap = new HashMap<>();
                    cMap.put("name", "미등록 고객");
                    paramMap.put("client_code", "미등록 고객");
                    paramMap.put("client_idx", null);
                    paramMap.put("status", 3);
                    mv.addObject("code", 902);
                }

                //
                paramMap.put("phone", paramMap.get("phoneNum"));
                paramMap.put("shop_id", sMap.get("id"));
                commonService.insertCallHistory(paramMap);
            } else {
                cMap = new HashMap<>();
                cMap.put("name", "발신정보 표시불가(서비스 신청안됨)");
                mv.addObject("code", 900);
            }
        } catch (Exception e) {
            mv.addObject("code", 900);
            e.printStackTrace();
        }
        log.debug(cMap);
        mv.addObject("clientInfo", cMap);
        return mv;
    }

    /* 전화걸기 팝업 */
    @RequestMapping("makeCall")
    public ModelAndView makeCall() { return new ModelAndView("/common/makeCall"); }

    /* 전화 수신 내역 */
    @RequestMapping("selectCallHistory")
    public ModelAndView selectCallHistory(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        ModelAndView mv = new ModelAndView("/common/callHistoryList");
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        paramMap.put("id", sMap.get("id"));
        String toDay = commonUtils.setForDate(paramMap);
        if(!paramMap.containsKey("startDate")) paramMap.put("startDate", toDay.substring(0, toDay.lastIndexOf('-')) + "-01");
        if(!paramMap.containsKey("endDate")) paramMap.put("endDate", toDay);
        if(!paramMap.containsKey("search_type")) paramMap.put("search_type", "");
        if(!paramMap.containsKey("search_value")) paramMap.put("search_value", "");
        if(!paramMap.containsKey("status")) paramMap.put("status", "2");
        log.debug("selectCallHistory: " + paramMap);
        try {
            int totalCount = Integer.parseInt(commonService.selectCallHistoryCount(paramMap).get("count").toString());
            /*int totalCount = 0;*/
            if (totalCount < 1) mv.addObject("size", 0);
            else {
                Paging paging = new Paging();
                int pageNo = 1;
                if (paramMap.containsKey("pageNo")) pageNo = Integer.parseInt(paramMap.get("pageNo").toString());
                paging.setPageNo(pageNo);
                paging.setTotalCount(totalCount);
                paramMap.put("start_idx", paging.getStartIndex());
                paramMap.put("end_idx", paging.getPageSize());

                List<Map<String, Object>> cList = commonService.selectCallHistoryList(paramMap);
                mv.addObject("size", totalCount);
                mv.addObject("callList", cList);
                mv.addObject("paging", paging);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mv.addObject("map", paramMap);
        log.debug("mv : " + mv);
        return mv;
    }


    /* ////////////////////////////// Return Objects Method ////////////////////////////// */

    /* 원장 회원가입 */
    @RequestMapping("singUpManagerAjax")
    @ResponseBody
    public Object singUpManagerAjax(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        JSONObject jobj = new JSONObject();
        log.debug("params : " + paramMap);

        paramMap.put("password", commonUtils.functionTransition(paramMap.get("password").toString()));
        paramMap.put("securityPassword", commonUtils.functionTransition(paramMap.get("id").toString()));
        try {
            commonService.insertManager(paramMap);
            if (paramMap.containsKey("ID")) {
                jobj.put("id", paramMap.get("ID"));
                jobj.put("code", 200);
            } else jobj.put("code", 900);
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 매장 추가 */
    @RequestMapping("createShop")
    @Transactional()
    @ResponseBody
    public Object createShop(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        JSONObject jobj = new JSONObject();
        log.debug("params : " + paramMap);

        // 매장 회원가입
        paramMap.put("password", commonUtils.functionTransition(paramMap.get("password").toString()));
        try {
            commonService.insertShop(paramMap);
            if (paramMap.containsKey("ID")) jobj.put("code", 200);
            else jobj.put("code", 900);
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 전화수신내역 고객 수정 */
    @RequestMapping("updateClientState")
    @Transactional()
    @ResponseBody
    public Object updateClientState(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        try {
            paramMap.put("id", sMap.get("id"));
            commonService.updateClientState(paramMap);
            jobj.put("code", 200);
        } catch (Exception e) {
            jobj.put("code", 900);
        }
        return jobj;
    }

    /* 매장 로그인 */
    @RequestMapping("loginCheckAjax")
    @ResponseBody
    public Object loginCheckAjax(@RequestParam Map<String, Object> paramMap, HttpSession session, HttpServletResponse response) {
        JSONObject jobj = new JSONObject();
        log.debug("loginCheckAjax params : " + paramMap);
        Map<String, Object> rMap = null;

        paramMap.put("password", commonUtils.functionTransition(paramMap.get("password").toString()));

        /*자동로그인이 체크되어있는지 확인*/
        if(paramMap.get("autologin").equals("true")) {
            /*자동로그인이 체크되어있다면 쿠키에 id=password형식으로 저장*/
            javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(paramMap.get("id").toString(), paramMap.get("password").toString());
            /*만료기한 3개원 (60초 * 60분 * 24시간 * 60일)*/
            cookie.setMaxAge(60 * 60 * 24 * 90);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        try {
            rMap = commonService.loginCheckAjax(paramMap);
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 900);
        }

        if (rMap == null) jobj.put("code", 900);
        else jobj.put("code", 200);

        session.setAttribute("shopInfo", rMap);

        return jobj;
    }

    /*자동 로그인 체크*/
    @RequestMapping("autoLoginCheck")
    @ResponseBody
    public Object autoLoginCheck(@RequestParam Map<String, Object> paramMap, HttpSession session, HttpServletRequest request) {
        JSONObject jobj = new JSONObject();
        log.debug("params : " + paramMap);
        Map<String, Object> rMap = null;
        Map<String, Object> autoLogin = new HashMap<String, Object>();
        Map<String, Object> cMapc = paramMap;
        /*쿠키 존재여부*/
        if(paramMap != null) {
            javax.servlet.http.Cookie[] loginCookie = request.getCookies();

            int loginCookie_length = loginCookie.length;
            if (loginCookie != null) {
                /*쿠키 갯수만큼 id,pw 체크*/
                for (int i = 0; i < loginCookie_length; i++) {
                    autoLogin.put("id", loginCookie[i].getName());
                    autoLogin.put("password", loginCookie[i].getValue());
                    rMap = commonService.autoLoginCheck(autoLogin);
                    if (rMap != null) {
                        /*자동로그인 ㅊ크 완료시 자동로그인 기한 재설정 90일*/
                        loginCookie[i].setMaxAge(60 * 60 * 24 * 90);
                        session.setAttribute("shopInfo", rMap);
                        jobj.put("code", 200);
                        return jobj;
                    }
                    else
                    {
                        jobj.put("code", 201);
                        autoLogin.clear();
                    }
                }
            }
            else
            {
                jobj.put("code", 201);
            }
        }
        else {
            jobj.put("code", 201);
        }

        return jobj;
    }

    /* 매장 로그인 */
    @RequestMapping("requestLogin")
    @ResponseBody
    public Object requestLogin(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        JSONObject jobj = new JSONObject();
        log.debug("requestLogin : " + paramMap);
        Map<String, Object> rMap = null;



        try {
            rMap = commonService.loginCheckAjax(paramMap);
            if (rMap == null) jobj.put("code", 900);
            else jobj.put("code", 200);
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 900);
        }
        session.setAttribute("shopInfo", rMap);

        return jobj;
    }

    /* 원장 아이디 중복 확인 */
    @RequestMapping("overlapCheckFromIdAjax")
    @ResponseBody
    public Object overlapCheckFromIdAjax(@RequestParam Map<String, Object> paramMap) {
        JSONObject jobj = new JSONObject();
        log.debug("params : " + paramMap);
        Map<String, Object> rMap = null;

        try {
            rMap = commonService.overlapCheckFromManager(paramMap);
            log.debug("rMap : " + rMap);
        } catch (Exception e) {
            jobj.put("code", 900);
        }
        if (rMap == null) jobj.put("code", 200);
        else if (rMap.containsKey("idx")) jobj.put("code", 900);
        else jobj.put("code", 900);

        return jobj;
    }

    /* 매장 아이디 중복 확인 */
    @RequestMapping("overlapShopCheckFromIdAjax")
    @ResponseBody
    public Object overlapShopCheckFromIdAjax(@RequestParam Map<String, Object> paramMap) {
        JSONObject jobj = new JSONObject();
        log.debug("params : " + paramMap);
        Map<String, Object> rMap = null;

        try {
            rMap = commonService.overlapShopCheckFromIdAjax(paramMap);
        } catch (Exception e) {
            jobj.put("code", 900);
        }
        if (rMap == null) jobj.put("code", 200);
        else if (rMap.containsKey("idx")) jobj.put("code", 900);
        else jobj.put("code", 900);

        return jobj;
    }

    /* 전화수신 기록하기 */
    @RequestMapping("insertCallHistory")
    @ResponseBody
    public Object insertCallHistory(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        log.debug("insertCallHistory : " + paramMap);
        JSONObject jobj = new JSONObject();
        try {
            paramMap.put("shop_id", sMap.get("id"));
            commonService.insertCallHistory(paramMap);
            if (paramMap.containsKey("ID")) jobj.put("code", 200);
            else jobj.put("code", 902);
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 900);
        }
        return jobj;
    }

    /* 채팅 저장 */
    @RequestMapping(value = "insertChat", method = RequestMethod.POST)
    @ResponseBody
    public Object insertChat(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        log.debug("insertChat : " + paramMap);
        try {
            paramMap.put("idx", sMap.get("idx"));
            commonService.insertChat(paramMap);
            jobj.put("code", 200);
            jobj.put("idx", paramMap.get("ID").toString());
        } catch (Exception e) {
            jobj.put("code", 902);
        }
        return jobj;
    }

    /* 채팅 내역 */
    @RequestMapping(value = "selectChat", method = RequestMethod.POST)
    @ResponseBody
    public Object selectChat(@RequestParam Map<String, Object> paramMap, HttpSession session) throws Exception {
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        log.debug("selectChat : " + paramMap);
        try {
            paramMap.put("idx", sMap.get("idx"));
            List<Map<String, Object>> cList = commonService.selectChat(paramMap);
            if (cList == null || cList.size() < 1) jobj.put("code", 900);
            else {
                jobj.put("code", 200);
                jobj.put("chatList", cList);
            }
        } catch (Exception e) {
            jobj.put("code", 902);
            e.printStackTrace();
        }
        return jobj;
    }

    /* 채팅 삭제 */
    @RequestMapping(value = "deleteChat", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteChat(@RequestParam Map<String, Object> paramMap) {
        JSONObject jobj = new JSONObject();
        log.debug("deleteChat : " + paramMap);
        try {
            commonService.deleteChat(paramMap);
            jobj.put("code", 200);
        } catch (Exception e) {
            jobj.put("code", 902);
        }
        return jobj;
    }

    /*중복된 사업자번호 확인*/
    @RequestMapping("managerComNUmberCheck")
    @ResponseBody
    public Object managerComNUmberCheck(@RequestParam Map<String, Object> paramMap) {
        JSONObject jobj = new JSONObject();

        try {
            List<Map<String, Object>> shopList = commonService.selectComNumberOverCheckFromManager(paramMap);
            System.out.println("list size : " + shopList.size());
            if (shopList.size() > 0) {
                jobj.put("code", 200);
            } else {
                jobj.put("code", 901);
            }
        } catch (Exception e) {
            jobj.put("code", 900);
        }

        log.debug("jboj : " + jobj);

        return jobj;
    }

    /* 매장 오픈 */
    @RequestMapping("shopOpen")
    @ResponseBody
    public Object shopOpen(HttpSession session) {
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();

        try {
            commonService.updateShopOpen(sMap);

            sMap.put("state", 0);
            session.setAttribute("shopInfo", sMap);

            jobj.put("code", 200);
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 매장 마감 */
    @RequestMapping("shopClose")
    @ResponseBody
    public Object shopClose(HttpSession session) {
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();

        try {
            commonService.updateShopClose(sMap);

            sMap.put("state", 1);
            session.setAttribute("shopInfo", sMap);

            jobj.put("code", 200);
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 매장 분단위 설정 */
    @RequestMapping("changeShopCalendarMinute")
    @ResponseBody
    public Object changeShopCalendarMinute(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();

        try {
            paramMap.put("idx", sMap.get("idx"));
            commonService.updateShopDefaultMinute(paramMap);
            sMap.put("default_minute", paramMap.get("minute"));
            session.setAttribute("shopInfo", sMap);

            jobj.put("code", 200);
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 매장 운영시간 업데이트 */
    @RequestMapping("updateOperationgTime")
    @ResponseBody
    public Object updateOperationgTime(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        try {
            paramMap.put("idx", sMap.get("idx"));
            paramMap.put("id", sMap.get("id"));
            commonService.updateEmployeeAllSchedule(paramMap);  // 스케쥴 업데이트 및 삭제
            commonService.updateOperationgTime(paramMap);       // 운영시간 업데이트

            sMap.put("shop_open", paramMap.get("start"));
            sMap.put("shop_close", paramMap.get("close"));
            session.setAttribute("shopInfo", sMap);
            jobj.put("code", 200);
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 매장 전화 수신 팝업에서 예약을 위한 Session Append */
    @RequestMapping("clientReservationSet")
    @ResponseBody
    public Object clientReservationSet(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        JSONObject jobj = new JSONObject();
        log.debug("clientReservationSet : " + paramMap);
        try {
            session.setAttribute("call_res_data", paramMap.get("r_data"));
            session.setAttribute("call_result_code", paramMap.get("r_code"));

            jobj.put("code", 200);
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 900);
        }
        return jobj;
    }

    /* 로그아웃 */
    @RequestMapping("logout")
    @ResponseBody
    public void logout(HttpSession session, HttpServletResponse response) {
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");

        javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(sMap.get("id").toString(), null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        session.removeAttribute("shopInfo");
    }

    /* 크롤링 허용 */
    @RequestMapping("robots.txt")
    @ResponseBody
    public String robots() {
        System.out.println("robots text call...");
        return "User-agent: Yeti\nUser-agent: Googlebot\nAllow: /\n";
    }

    /* 인증 이메일 발송 */
    /*@RequestMapping("emailSender")
    @ResponseBody
    public Object emailConfrim(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        CommonUtils utils = new CommonUtils();
        JSONObject jobj = new JSONObject();
        log.debug("emailSender param : " + paramMap);

        try {
            // 이메일 발송
            JSONObject data = (JSONObject) commonService.regist(sMap, paramMap, mailSender);

            if ("200".equals(data.get("code").toString())) {
                jobj.put("key", utils.functionTransition(data.get("key").toString()));
                jobj.put("code", 200);
            } else if ("500".equals(data.get("code").toString())) {
                jobj.put("code", 500);
            } else if ("900".equals(data.get("code").toString())) {
                jobj.put("code", 900);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 900);
        }

        return jobj;
    }*/

    /* 이메일 검증 */
    /*@RequestMapping("emailConfirm")
    @ResponseBody
    public Object emailConfirm(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        CommonUtils utils = new CommonUtils();
        log.debug("emailConfirm session : " + sMap);

        try {
            String userKey = utils.functionTransition(paramMap.get("userKey").toString());
            String emailKey = paramMap.get("hiddenKey").toString();
            if (userKey.equals(emailKey)) {
                jobj.put("code", 200);
                paramMap.put("idx", sMap.get("idx"));
            } else {
                jobj.put("code", 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 900);
        }
        return jobj;
    }*/

    /* 사업자 등록번호 중복허용 확인 */
    @RequestMapping("checkComNumOverlap")
    @ResponseBody
    public Object checkComNumOverlap(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        log.debug("checkComNumOverlap param : " + paramMap);

        try {
            paramMap.put("manager_idx", sMap.get("manager_idx"));
            paramMap.put("idx", sMap.get("idx"));
            List<Map<String, Object>> rList = commonService.selectOverlapComnumCheck(paramMap);

            CommonUtils.printList(rList);

            if (rList.size() > 0) {
                JSONArray jArray = CommonUtils.convertListToJson(rList);
                jobj.put("overList", jArray);
                jobj.put("code", 200);
            } else {
                jobj.put("code", 500);
            }
        } catch (Exception e) {
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 발신자 등록으로 인한 매장 데이터 업데이트 */
    @RequestMapping("updateShopDataFromSender")
    @ResponseBody
    public Object updateShopDataFromSender(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        JSONObject jobj = new JSONObject();
        log.debug("updateShopDataFromSender param : " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");

        try {
            paramMap.put("idx", sMap.get("idx"));
            commonService.updateShopDataFromSender(paramMap);

            String email = paramMap.get("cortactEmail1").toString() + "@" + paramMap.get("cortactEmail2").toString();
            sMap.put("com_num", paramMap.get("comNum"));
            sMap.put("email", email);
            session.setAttribute("shopInfo", sMap);

            jobj.put("code", 200);
        } catch (Exception e) {
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 회원가입시 잔디 메세지 */
    @RequestMapping(value = "jandi", method = RequestMethod.POST)
    @ResponseBody
    public Object jandiPost(@RequestParam Map<String, Object> paramMap) {
        Gson gson = new Gson();
        log.debug("jandiPost: " + paramMap);
        JSONObject jobj = new JSONObject();

        String url = "https://wh.jandi.com/connect-api/webhook/17410829/627359899dabb1e4518c7f86da909e2c";
        RestTemplate restTemplate = new RestTemplate();
        try {

            JSONObject requestBody = new JSONObject();
            requestBody.put("body", paramMap.get("body"));
            requestBody.put("connectColor", paramMap.get("connectColor"));

            List<Map<String, Object>> requestList = gson.fromJson(paramMap.get("array").toString(), List.class);

            CommonUtils.printList(requestList);
            requestBody.put("connectInfo", requestList);

//            CommonUtils.printMap((Map<String, Object>)paramMap.get("connectInfo[0][description]"));
            ResponseEntity<String> responseEntiry = restTemplate.postForEntity(url, requestBody, String.class);
            System.out.println(responseEntiry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobj;
    }

    /* 잔디 문의 메시지 */
    @RequestMapping(value = "questionSend", method = RequestMethod.POST)
    @ResponseBody
    public Object questionSend(@RequestParam Map<String, Object> paramMap) {
        Gson gson = new Gson();
        log.debug("questionSend: " + paramMap);
        JSONObject jobj = new JSONObject();
        Map<String, Object> requestMap = new HashMap<>();
        List<Map<String, Object>> requestList = new ArrayList<>();

        String url = "";
        if("question-radio-1".equals(paramMap.get("type").toString())){
            url = "https://wh.jandi.com/connect-api/webhook/17410829/ceabf3b5f80c5192a1823be262d4d6b5";
            requestMap.put("title", "신규 가입 문의");
        } else if ("question-radio-2".equals(paramMap.get("type").toString())){
            url = "https://wh.jandi.com/connect-api/webhook/17410829/0ad02e09d6e4f65513a17d6b4bcdce24";
            requestMap.put("title", "신규 가입 혜택 문의");
        } else if ("question-radio-3".equals(paramMap.get("type").toString())){
            url = "https://wh.jandi.com/connect-api/webhook/17410829/8e63e22d1ffb4a8bba5a6c24a84e1a93";
            requestMap.put("title", "데이터 이전 문의");
        } else if ("question-radio-4".equals(paramMap.get("type").toString())){
            url = "https://wh.jandi.com/connect-api/webhook/17410829/23d07d074bf91bf0cdce25372227d4b4";
            requestMap.put("title", "문제 신고");
        } else if ("question-radio-5".equals(paramMap.get("type").toString())){
            url = "https://wh.jandi.com/connect-api/webhook/17410829/5a5ff9cf4161695de353da7c047e604d";
            requestMap.put("title", "기타");
        }
        requestMap.put("description", paramMap.get("questionText"));
        requestList.add(requestMap);

        RestTemplate restTemplate = new RestTemplate();
        try {

            JSONObject requestBody = new JSONObject();
            requestBody.put("body", requestMap.get("title").toString());
            requestBody.put("connectColor", paramMap.get("connectColor"));
//            CommonUtils.printList(requestList);
            requestBody.put("connectInfo", requestList);

//            CommonUtils.printMap((Map<String, Object>)paramMap.get("connectInfo[0][description]"));
            ResponseEntity<String> responseEntiry = restTemplate.postForEntity(url, requestBody, String.class);
            System.out.println(responseEntiry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobj;
    }
}
