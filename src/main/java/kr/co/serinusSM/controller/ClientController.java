package kr.co.serinusSM.controller;

import com.google.gson.Gson;
import kr.co.serinusSM.common.CommonUtils;
import kr.co.serinusSM.common.Paging;
import kr.co.serinusSM.service.ClientService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("Client")
public class ClientController {
    Logger log = Logger.getLogger(this.getClass());

    @Resource(name = "clientService")
    private ClientService clientService;

    @Resource(name = "commonUtils")
    private CommonUtils commonUtils;

    /* 페이지 직원 등록 */
    @RequestMapping("clientList")
    public ModelAndView clientList(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        ModelAndView mv = new ModelAndView("/client/clientList");
        Map<String, Object> sMap = (Map<String, Object>)session.getAttribute("shopInfo");
        Gson gson = new Gson();
        Paging paging = new Paging();
        paramMap.put("id", sMap.get("id"));
        log.debug("client list : "+paramMap);

        /* Client All count */
        if(!paramMap.containsKey("sort")) paramMap.put("sort", "");
        else mv.addObject("sort", paramMap.get("sort"));
        if(paramMap.containsKey("data")) mv.addObject("data", paramMap.get("data"));
        else mv.addObject("data", "");

        Map<String, Object> rMap = clientService.selectAllClientsList(paramMap);

        /* Count 0 */
        if(Integer.parseInt(rMap.get("count").toString()) < 1){
            mv.addObject("size", 0); }
        /* Count > 0 */
        else {
            int totalCount = Integer.parseInt(rMap.get("count").toString());            // list size
            int pageNo = 1;                                                             // pageNunber
            if(paramMap.get("pageNo") != null){                                         // param pageNumber check
                String paramPageNo = paramMap.get("pageNo").toString();                 // get pageNumber
                pageNo = Integer.parseInt(paramPageNo);
            }
            paging.setPageNo(pageNo);                                                   // sql start index set
            paging.setTotalCount(totalCount);                                           // sql end index set
            paramMap.put("start_idx", paging.getStartIndex());
            paramMap.put("end_idx", paging.getPageSize());

            List<Map<String, Object>> clientsList = clientService.selectSortingAndPagingClients(paramMap);
            for(int i=0; i<clientsList.size(); i++){ // containsKey
                if(clientsList.get(i).containsKey("prepaid")) clientsList.get(i).put("prepaid", gson.fromJson(clientsList.get(i).get("prepaid").toString(), List.class));
                if(clientsList.get(i).containsKey("ticket")) clientsList.get(i).put("ticket",  gson.fromJson(clientsList.get(i).get("ticket").toString(), List.class));
            }

            /* 데이터 */
            if(!paramMap.containsKey("sort")) mv.addObject("sort", "");
            else mv.addObject("sort", paramMap.get("sort"));
            if(!paramMap.containsKey("data")) mv.addObject("data", "");
            else mv.addObject("data", paramMap.get("data"));
            mv.addObject("size", rMap.size());
            mv.addObject("clientList", clientsList);
            /* 페이징 */
            mv.addObject("paging", paging);
        }

        return mv;
    }

    /* ////////////////////////////// Return Objects Method ////////////////////////////// */

    /* 고객 코드 자동 생성 */
    @RequestMapping("clientCodeAuto")
    @ResponseBody
    public Object clientCodeAuto(@RequestParam Map<String, Object> paramMap, HttpSession session) throws Exception {
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        log.debug("clientCodeAuto : " + paramMap);

        Map<String, Object> rMap = clientService.selectClientLargestNumber(sMap);

        String clientNumber = rMap.get("maxNumber").toString();


        if("0".equals(clientNumber)){
            jobj.put("max", 1);
        } else {
            jobj.put("max", clientNumber);
        }

        log.debug("max Number : " + jobj);

        return jobj;
    }

    /* 고객 휴대폰 번호 중복 확인 */
    @RequestMapping("clientOverCheckFromPhone")
    @ResponseBody
    public Object clientOverCheckFromPhone(@RequestParam Map<String, Object> paramMap, HttpSession session) throws Exception {
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        log.debug("clientOverCheckFromPhone : " + paramMap);

        paramMap.put("shopId", sMap.get("id"));
        Map<String, Object> rMap = clientService.selectOverCheckFromPhone(paramMap);

        if(rMap == null || rMap.get("overCheck") == null) jobj.put("code", 200);
        else if(Integer.parseInt(rMap.get("overCheck").toString()) < 1) jobj.put("code", 200);
        else jobj.put("code", 900);

        return jobj;
    }

    /* 신규 고객 등록 */
    @RequestMapping("insertNewClientFromAjax")
    @ResponseBody
    public Object insertNewClientFromAjax(@RequestParam Map<String, Object> paramMap, HttpSession session) throws Exception {
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        log.debug("insertNewClientFromAjax : " + paramMap);

        /* 신규 고객 등록 */
        paramMap.put("shopId", sMap.get("id"));
        paramMap.put("id", sMap.get("id"));
        paramMap.put("shopIdx", sMap.get("idx"));

        Map<String, Object> checkMap = clientService.selectOverCheckFromCode(paramMap);
        if(checkMap == null || !checkMap.containsKey("check_idx") || "0".equals(checkMap.get("check_idx").toString())){
            clientService.insertNewClient(paramMap);
            if(paramMap.containsKey("ID")) {
                jobj.put("code", 200);
                jobj.put("idx", paramMap.get("ID"));
            }
            else jobj.put("code", 900);
        } else {
            jobj.put("code", 902);
        }

        return jobj;
    }

    /* 고객 판매내역 */
    @RequestMapping(value = "selectClientSalesAjax", method = RequestMethod.POST)
    @ResponseBody
    public Object selectClientSalesAjax(@RequestParam Map<String, Object> paramMap, HttpSession session) throws Exception {
        JSONObject jobj = new JSONObject();
        Gson gson = new Gson();
        log.debug("selectClientSalesAjax : " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        try {
            paramMap.put("shop_id", sMap.get("id").toString());
            List<Map<String, Object>> csList = clientService.selectClientSalesAjax(paramMap);
            if(csList.size()<1 || csList == null) jobj.put("code", 900);
            else {
                for(int i=0; i<csList.size(); i++) csList.get(i).put("sale_resource", gson.fromJson(csList.get(i).get("sale_resource").toString(), List.class));
                return csList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code",902);
        }
        return jobj;
    }

    /* 고객 정보 */
    @RequestMapping(value = "selectClientInfoAjax", method = RequestMethod.POST, produces = "application/text; charset=utf8")
    @ResponseBody
    public Object selectClientInfoAjax(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        JSONObject jobj = new JSONObject();
        Gson gson = new Gson();
        log.debug("clientInfoAjax : " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        try {
            paramMap.put("shop_id", sMap.get("id").toString());
            Map<String, Object> cInfo = clientService.selectClientInfoAjax(paramMap);
            log.debug(cInfo);
            if(cInfo == null) jobj.put("code", 900);
            else return gson.toJson(cInfo);
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code",902);
        }
        return jobj;
    }

    /* 고객 정보 수정 */
    @RequestMapping(value = "updateClientInfoAjax", method = RequestMethod.POST, produces = "application/json; charset=utf8")
    @ResponseBody
    public Object updateClientInfoAjax(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        Gson gson = new Gson();
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        paramMap.put("shop_id", sMap.get("id"));
        log.debug("updateClientInfoAjax : " + paramMap);
        try {
            Map<String, Object> cMap = clientService.selectClientForIdx(paramMap);
            paramMap.put("origin_code", cMap.get("code"));
            boolean check = clientService.selectClientForCode(paramMap);
            if(check){
                jobj.put("code", 201);
                return jobj;
            }
            paramMap.put("shop_idx", sMap.get("idx"));
            clientService.selectThisClient(paramMap, gson);
            clientService.updateClientInfoAjax(paramMap);
            jobj.put("code", 200);
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 902);
        }
        return jobj;
    }


    /* 선불 잔액 잔여포인트 미수금 가져오기 */
    @RequestMapping(value = "callPrepaidAndPointCost", method = RequestMethod.POST)
    @ResponseBody
    public Object callPrepaidAndPointCost(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        Map<String ,Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        try {
            paramMap.put("shop_id", sMap.get("id"));
            Map<String, Object> clientMap = clientService.selectClientPrePointMiss(paramMap);
            jobj.put("clientMap", clientMap);
            jobj.put("code", 200);
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 902);
        }
        return jobj;
    }

    /* 고객 메모 수정 */
    @RequestMapping("clientMemoUpdate")
    @ResponseBody
    public Object clientMemoUpdate(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        log.debug("param : " + paramMap);
        Map<String ,Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        try {
            paramMap.put("shop_id", sMap.get("id"));
            clientService.updateClientMemo(paramMap);
            jobj.put("code", 200);
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 902);
        }
        return jobj;
    }

    /* 고객 코드 반자동 */
    @RequestMapping("selectClientCodeSemiAuto")
    @ResponseBody
    public Object selectClientCodeSemiAuto(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        log.debug("param : " + paramMap);
        Map<String ,Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        try {
            paramMap.put("shop_id", sMap.get("id"));
            Map<String, Object> cMap = clientService.selectClientCodeSemiAuto(paramMap);
            jobj.put("client_code", cMap.get("max_code"));
            jobj.put("code", 200);
        } catch (NullPointerException ne){
            jobj.put("code", 905);
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 900);
        }
        return jobj;
    }
}