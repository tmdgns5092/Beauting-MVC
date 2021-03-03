package kr.co.serinusSM.controller;

import com.google.gson.Gson;
import kr.co.serinusSM.common.CommonUtils;
import kr.co.serinusSM.service.CommonService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("Payple")
public class PaypleController {
    Logger log = Logger.getLogger(this.getClass());


    @Resource(name = "commonService")
    private CommonService commonService;

    @Resource(name = "commonUtils")
    private CommonUtils commonUtils;

    /* 포인트 충전 페이지 */
    @RequestMapping("pointCharging")
    public ModelAndView poingCarging(@RequestParam Map<String, Object> paramMap) {
        ModelAndView mv = new ModelAndView("/cPayPayple/pointCharging");
        log.debug("pointCharging: " + paramMap);

        return mv;
    }

    /* 포인트 충전 페이지 */
    @RequestMapping("chargingList")
    public ModelAndView chargingList(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        ModelAndView mv = new ModelAndView("/cPayPayple/chargingList");
        log.debug("chargingList: " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        try{

        }catch (Exception e){
            e.printStackTrace();
        }
        return mv;
    }

    /* 결과 페이지 */
    @RequestMapping("result")
    @ResponseBody
    public ModelAndView result(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        ModelAndView mv = new ModelAndView("redirect:/Payple/pointCharging");
        log.debug("result : " + paramMap);
        Gson gson = new Gson();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
            if("success".equalsIgnoreCase(paramMap.get("PCD_PAY_RST").toString())){
                log.debug("결제 완료");
                try {
                    sMap.put("PCD_PAY_TOTAL", paramMap.get("PCD_PAY_TOTAL"));
                    sMap.put("msg_point", Integer.parseInt(sMap.get("msg_point").toString()) + Integer.parseInt(paramMap.get("PCD_PAY_TOTAL").toString()));
                    commonService.insertPayment(paramMap);
//                    commonService.updateMsgPoint(sMap);

                    String addPoint = paramMap.get("PCD_PAY_TOTAL").toString();
                    if (!sMap.containsKey("PCD_PAYER_ID")) {
                        log.debug("첫 결제 ID 저장");
                        sMap.put("PCD_PAYER_ID", paramMap.get("PCD_PAYER_ID"));
                        commonService.insertPayer_ID(sMap);
                        sMap.remove("PCD_PAY_TOTAL");
                    }
                    session.setAttribute("shopInfo", sMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /* 잔디 송신 */
                String url = "https://wh.jandi.com/connect-api/webhook/17410829/627359899dabb1e4518c7f86da909e2c";
                RestTemplate restTemplate = new RestTemplate();
                try {
                    Map<String, Object> jandiSubObj = new HashMap<>();
                    List<Map<String, Object>> requestList = new ArrayList<>();

                    jandiSubObj.put("title", "요청 정보");
                    jandiSubObj.put("description",
                            "아이디 - " + sMap.get("id") +
                            "\n 회사 명 - " + sMap.get("com_name") +
                            "\n 원장 이름 - " + sMap.get("ceo_name") +
                            "\n 연락처 - " + sMap.get("tel") +
                            "\n 주소 - " + sMap.get("lg_address ") + " " + sMap.get("sm_address") + " " + sMap.get("post") +
                            "\n 원장 아이디 - " + sMap.get("manager_idx") +
                            "\n 충전 요청 금액 - " + paramMap.get("PCD_PAY_TOTAL") + "원"
                    );
                    requestList.add(jandiSubObj);

                    JSONObject requestBody = new JSONObject();
                    requestBody.put("body", "[뷰팅네일 문자 포인트 충전 요청] - " + paramMap.get("PCD_PAY_TOTAL") + "원");
                    requestBody.put("connectColor", "#FAC11B");

                    CommonUtils.printList(requestList);
                    requestBody.put("connectInfo", requestList);

                    ResponseEntity<String> responseEntiry = restTemplate.postForEntity(url, requestBody, String.class);
                    System.out.println(responseEntiry);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else log.debug("결제 오류");
        return mv;
    }

    /* 매장별 등록된 계좌 삭제 */
    @RequestMapping("accountTermination")
    @ResponseBody
    public Object accountTermination(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        log.debug("accountTermination : " + paramMap);
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        try{
            commonService.deletePayer_ID(sMap);
            sMap.remove("PCD_PAYER_ID");
            session.setAttribute("shopInfo", sMap);
            jobj.put("code", 200);
        } catch (Exception e){
            e.printStackTrace();
            jobj.put("code", 900);
        }
        return jobj;
    }
    /* cst_id, custKey 가져오는 Ajax */
    @RequestMapping("getPaypleUser")
    @ResponseBody
    public Object getPaypleUser(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        log.debug("getPaypleUser : " + paramMap);
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        if(sMap.containsKey("PCD_PAYER_ID")) jobj.put("payple_payer_id", sMap.get("PCD_PAYER_ID"));
        String cst_id = "GANONG01";
        String custKey = "3572124a09786084406ffb30d73655131b099780da73ec73ed9b0373b7615ab6";
        jobj.put("cst_id", cst_id);
        jobj.put("custKey", custKey);
        jobj.put("auth_url", "https://cpay.payple.kr/php/auth.php");

        return jobj;
    }
}
