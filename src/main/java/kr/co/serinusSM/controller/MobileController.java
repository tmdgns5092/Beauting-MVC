package kr.co.serinusSM.controller;


import kr.co.serinusSM.common.CommonUtils;
import kr.co.serinusSM.service.CommonService;
import kr.co.serinusSM.service.EmployeeService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("Mobile")
public class MobileController {

    Logger log = Logger.getLogger(this.getClass());

    @Resource(name = "commonService")
    private CommonService commonService;

    @Resource(name = "employeeService")
    private EmployeeService employeeService;

    /* 매장 로그인 */
    @RequestMapping("loginCheckAjax")
//    @CrossOrigin(origins="http://localhost:8081")
//    @CrossOrigin(origins="http://m.beauting.kr/*")
    @CrossOrigin(origins="*")
    @ResponseBody
    public Object loginCheckAjax(@RequestParam Map<String, Object> paramMap) {
        log.debug("params : " + paramMap);
        JSONObject jobj = new JSONObject();
        Map<String, Object> rMap = null;
        List<Map<String, Object>> eMap = null;
        CommonUtils commonUtils = new CommonUtils();

        paramMap.put("password", commonUtils.functionTransition(paramMap.get("password").toString()));

        try {
            rMap = commonService.loginCheckAjax(paramMap);
            eMap = employeeService.selectAllEmpl(rMap);
        } catch (Exception e) {
            jobj.put("code", 900);
        }

        if (rMap == null) jobj.put("code", 900);
        else {
            jobj.put("code", 200);
            jobj.put("data", rMap);
            jobj.put("employee", eMap);
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

    /* 매장 직원 스케쥴 불러오기 */
    @RequestMapping("scheduleCall")
//    @CrossOrigin(origins="http://localhost:8081")
//    @CrossOrigin(origins="http://m.beauting.kr/*")
    @CrossOrigin(origins="*")
    @ResponseBody
    public Object scheduleCall(@RequestParam Map<String, Object> paramMap){
        log.debug("scheduleCall : " + paramMap);
        JSONObject jobj = new JSONObject();
        Map<String, Object> mobileSessionMap = new HashMap<>();

        int idx = Integer.parseInt(paramMap.get("idx").toString());
        int emplIdx = Integer.parseInt(paramMap.get("emplIdx").toString());
        String id = paramMap.get("id").toString();
        String default_minute = paramMap.get("default_minute").toString();
        String date = paramMap.get("date").toString();

        mobileSessionMap.put("idx", idx);
        mobileSessionMap.put("emplIdx", emplIdx);
        mobileSessionMap.put("id", id);
        mobileSessionMap.put("default_minute", default_minute);
        mobileSessionMap.put("date", date);

        List<Map<String, Object>> reservationList = commonService.selectReservation(mobileSessionMap);

        jobj.put("data", reservationList);
        return jobj;
    }

    /*자동 로그인 체크*/
//    @RequestMapping("autoLoginCheck")
//    @ResponseBody
//    public Object autoLoginCheck(@RequestParam Map<String, Object> paramMap, HttpSession session, HttpServletRequest request) {
//        JSONObject jobj = new JSONObject();
//        log.debug("params : " + paramMap);
//        Map<String, Object> rMap = null;
//        Map<String, Object> autoLogin = new HashMap<String, Object>();
//        Map<String, Object> cMapc = paramMap;
//        /*쿠키 존재여부*/
//        if(paramMap != null) {
//            javax.servlet.http.Cookie[] loginCookie = request.getCookies();
//
//            int loginCookie_length = loginCookie.length;
//            if (loginCookie != null) {
//                /*쿠키 갯수만큼 id,pw 체크*/
//                for (int i = 0; i < loginCookie_length; i++) {
//                    autoLogin.put("id", loginCookie[i].getName());
//                    autoLogin.put("password", loginCookie[i].getValue());
//                    rMap = commonService.autoLoginCheck(autoLogin);
//                    if (rMap != null) {
//                        /*자동로그인 ㅊ크 완료시 자동로그인 기한 재설정 90일*/
//                        loginCookie[i].setMaxAge(60 * 60 * 24 * 90);
//                        session.setAttribute("shopInfo", rMap);
//                        jobj.put("code", 200);
//                        return jobj;
//                    }
//                    else
//                    {
//                        jobj.put("code", 201);
//                        autoLogin.clear();
//                    }
//                }
//            }
//            else
//            {
//                jobj.put("code", 201);
//            }
//        }
//        else {
//            jobj.put("code", 201);
//        }
//
//        return jobj;
//    }

    /* 채팅 저장 */
//    @RequestMapping(value = "insertChat", method = RequestMethod.POST)
//    @ResponseBody
//    public Object insertChat(@RequestParam Map<String, Object> paramMap, HttpSession session) {
//        JSONObject jobj = new JSONObject();
//        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
//        log.debug("insertChat : " + paramMap);
//        try {
//            paramMap.put("idx", sMap.get("idx"));
//            commonService.insertChat(paramMap);
//            jobj.put("code", 200);
//            jobj.put("idx", paramMap.get("ID").toString());
//        } catch (Exception e) {
//            jobj.put("code", 902);
//        }
//        return jobj;
//    }

    /* 채팅 내역 */
//    @RequestMapping(value = "selectChat", method = RequestMethod.POST)
//    @ResponseBody
//    public Object selectChat(@RequestParam Map<String, Object> paramMap, HttpSession session) throws Exception {
//        JSONObject jobj = new JSONObject();
//        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
//        log.debug("selectChat : " + paramMap);
//        try {
//            paramMap.put("idx", sMap.get("idx"));
//            List<Map<String, Object>> cList = commonService.selectChat(paramMap);
//            if (cList == null || cList.size() < 1) jobj.put("code", 900);
//            else {
//                jobj.put("code", 200);
//                jobj.put("chatList", cList);
//            }
//        } catch (Exception e) {
//            jobj.put("code", 902);
//            e.printStackTrace();
//        }
//        return jobj;
//    }

    /* 채팅 삭제 */
//    @RequestMapping(value = "deleteChat", method = RequestMethod.POST)
//    @ResponseBody
//    public Object deleteChat(@RequestParam Map<String, Object> paramMap) {
//        JSONObject jobj = new JSONObject();
//        log.debug("deleteChat : " + paramMap);
//        try {
//            commonService.deleteChat(paramMap);
//            jobj.put("code", 200);
//        } catch (Exception e) {
//            jobj.put("code", 902);
//        }
//        return jobj;
//    }


}