package kr.co.serinusSM.controller;

import com.google.gson.Gson;
import kr.co.serinusSM.common.CommonUtils;
import kr.co.serinusSM.service.ServicesService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("Services")
public class ServicesController {
    Logger log = Logger.getLogger(this.getClass());

    @Resource(name = "servicesService")
    private ServicesService servicesService;

    @Resource(name = "commonUtils")
    private CommonUtils commonUtils;

    // 시술
    @RequestMapping("serviceRegistration")
    public ModelAndView serviceRegistration(@RequestParam Map<String, Object> paramMap, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("/services/serviceRegistration");
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        sMap.put("type", 0);

        /* selectAll Services */
        List<Map<String, Object>> servicesList = servicesService.selectServicesLists(sMap);
//        log.debug("servicesList: " + servicesList);
        commonUtils.setPageObject(mv, servicesList);

        if(paramMap.containsKey("focuse_idx")) mv.addObject("focuse_idx", paramMap.get("focuse_idx"));
        else mv.addObject("focuse_idx", "null");

        return mv;

    }

    // 선불권
    @RequestMapping("prepaid")
    public ModelAndView prepaid(@RequestParam Map<String, Object> paramMap, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("/services/prepaid");
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        Gson gson = new Gson();
        log.debug("prepaid: " + paramMap);
        sMap.put("type", 1);

        List<Map<String, Object>> servicesList = servicesService.selectServicesLists(sMap);
        log.debug("servicesList: " + servicesList);
        for(int i = 0; i < servicesList.size(); i++){
            Map<String, Object> map = servicesList.get(i);
            if(map.containsKey("resource"))servicesList.get(i).put("resource", gson.fromJson(map.get("resource").toString(), Map.class));
        }
        mv.addObject("services", servicesList);
        return mv;
    }

    // 제품
    @RequestMapping("productMg")
    public ModelAndView productMg(@RequestParam Map<String, Object> paramMap, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("/services/productMg");
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        log.debug("productMg: " + paramMap);
        sMap.put("type", 3);

        List<Map<String, Object>> servicesList = servicesService.selectServicesLists(sMap);
        log.debug("servicesList: " + servicesList);
        commonUtils.setPageObjectTicketOrProduct(mv, servicesList);

        return mv;
    }

    // 티켓
    @RequestMapping("ticket")
    public ModelAndView ticket(@RequestParam Map<String, Object> paramMap, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("/services/ticket");
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        sMap.put("type", 2);
        log.debug("ticket: " + paramMap);

        List<Map<String, Object>> servicesList = servicesService.selectServicesLists(sMap);
        log.debug("servicesList: " + servicesList);
        commonUtils.setPageObjectTicketOrProduct(mv, servicesList);

        return mv;
    }
    /* ////////////////////////////// Return Objects Method ////////////////////////////// */

    /* 서비스 그룹 추가 */
    @RequestMapping("insertServicesCategory")
    @ResponseBody
    public Object insertServicesCategory(@RequestParam Map<String, Object> paramMap, HttpSession session){
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>)session.getAttribute("shopInfo");
        paramMap.put("shop_idx", sMap.get("idx"));
        log.debug("insertServicesCategory : " + paramMap);
        try {
            servicesService.insertServicesCategory(paramMap);
            servicesService.updateServiceProcedureUpdate(paramMap);
            jobj.put("code", 200);
            jobj.put("idx", paramMap.get("ID"));
        }catch(Exception e){
            jobj.put("code", 900);
        }
        log.debug("later paramMap Value : "+paramMap);

        return jobj;
    }

    /* 서비스 그룹 수정*/
    @RequestMapping("updateServicesCategoryAjax")
    @ResponseBody
    public Object updateServicesCategoryAjax(@RequestParam Map<String, Object> paramMap, HttpSession session){
        JSONObject jobj = new JSONObject();
        log.debug("updateServicesCategoryAjax : " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        try {
            paramMap.put("shop_idx", sMap.get("idx"));
            List<Map<String, Object>> details = servicesService.selectDeleteServicesLists(paramMap);
            servicesService.updateServicesCategoryAjax(paramMap);
            for(Map<String, Object>detail : details){
                detail.put("type",paramMap.get("type"));
                detail.put("category",paramMap.get("category"));
                servicesService.updateServicesCategoryAjax(detail);
            }
            jobj.put("code", 200);
        }catch(Exception e){
            jobj.put("code", 900);
            e.printStackTrace();
        }
        return jobj;
    }

    /* 서비스 그룹 삭제 */
    @RequestMapping("deleteServicesCategoryAjax")
    @ResponseBody
    public Object deleteServicesCategoryAjax(@RequestParam Map<String, Object> paramMap, HttpSession session){
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>)session.getAttribute("shopInfo");
        paramMap.put("shop_idx", sMap.get("idx"));
        log.debug("deleteServicesCategoryAjax : " + paramMap);

        Boolean flag = new Boolean(paramMap.get("flag").toString());

        try {
            // 하위 요소를 포함한 그룹일 경우
            if(flag){
                // 하위 요소 id, name 가져오기
                List<Map<String, Object>> details = servicesService.selectDeleteServicesLists(paramMap);
                // 모든 하위 요소 삭제
                for(Map<String, Object> detail : details) servicesService.deleteAllServicesDetailAjax(detail);
            }
            // 그룹 삭제
            servicesService.deleteServicesCategoryAjax(paramMap);
            jobj.put("code", 200);
        }catch(Exception e){
            jobj.put("code", 900);
        }
        return jobj;
    }

    /* 서비스 하위 추가 */
    @RequestMapping("insertServicesDetail")
    @ResponseBody
    public Object insertServicesDetail(@RequestParam Map<String, Object> paramMap, HttpSession session){
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>)session.getAttribute("shopInfo");
        paramMap.put("shop_idx",sMap.get("idx"));
        log.debug("insertServicesDetail : " + paramMap);
        try {
            servicesService.insertServicesDetail(paramMap);
            servicesService.updateServiceProcedureUpdate(paramMap);
            jobj.put("code", 200);
            jobj.put("idx", paramMap.get("ID"));
        } catch(Exception e) {
            jobj.put("code", 900);
        }
        return jobj;
    }

    /* 서비스 하위 수정 */
    @RequestMapping("updateServicesDetailAjax")
    @ResponseBody
    public Object updateServicesDetailAjax(@RequestParam Map<String, Object> paramMap){
        JSONObject jobj = new JSONObject();
        log.debug("updateServicesDetailAjax : " + paramMap);
        try {
            servicesService.updateServicesDetailAjax(paramMap);
            jobj.put("code", 200);
        }catch(Exception e){
            jobj.put("code", 900);
        }
        return jobj;
    }

    /* 서비스 하위 삭제*/
    @RequestMapping("deleteServicesDetailAjax")
    @ResponseBody
    public Object deleteServicesDetailAjax(@RequestParam Map<String, Object> paramMap){
        JSONObject jobj = new JSONObject();
        log.debug("deleteServicesDetailAjax : " + paramMap);
        try {
            servicesService.deleteServicesDetailAjax(paramMap);
            jobj.put("code", 200);
        }catch(Exception e){
            jobj.put("code", 900);
        }
        return jobj;
    }

    @RequestMapping("moveServices")
    @ResponseBody
    public Object moveServices(@RequestParam Map<String, Object> paramMap) throws Exception{
        JSONObject jobj = new JSONObject();

        try{
            servicesService.updateServicesProcedure(paramMap);
            jobj.put("code", 200);
        } catch(Exception e){
            jobj.put("code", 900);
        }

        return jobj;
    }
}