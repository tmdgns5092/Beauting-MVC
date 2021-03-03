package kr.co.serinusSM.controller;

import com.google.gson.Gson;
import kr.co.serinusSM.common.CommonUtils;
import kr.co.serinusSM.service.EmployeeService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("Employee")
public class EmployeeController {
    Logger log = Logger.getLogger(this.getClass());

    @Resource(name = "employeeService")
    private EmployeeService employeeService;

    @Resource(name = "commonUtils")
    private CommonUtils commonUtils;

    /* 페이지 직원 등록 */
    @RequestMapping("employeeList")
    public ModelAndView employeeList(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        ModelAndView mv = new ModelAndView("/employee/employeeList");
        log.debug("employeeList : " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        List<Map<String, Object>> rList = new ArrayList<>(), goal = new ArrayList<>();
        Map<String, Object> gMap = null;
        String gIdx = null;
        sMap.put("forDate", commonUtils.setForDate(paramMap));
        try {
            /* 직원 리스트 가져오기 */
            rList = employeeService.selectAllEmpl(sMap);
            /* 직원 목표 가져오기 */
            gMap = employeeService.selectEmplGoalData(sMap);
            if(gMap != null){
                goal = new Gson().fromJson(gMap.get("employee_goal").toString(), List.class);
                gIdx = gMap.get("idx").toString();
            }

            if(!goal.isEmpty() || goal.size() > 0){
                for(int i=0; i < rList.size(); i++){
                    if(goal == null) { rList.get(i).put("goal_cost", 0); rList.get(i).put("goal_exhaust", 0); }
                    else {
                        String idx = rList.get(i).get("idx").toString();
                        for(Map<String, Object> map : goal){
                            if(idx.equals(map.get("idx"))){
                                rList.get(i).put("goal_cost", map.get("cost"));
                                rList.get(i).put("goal_exhaust", map.get("exhaust"));
                                break;
                            } else {
                                rList.get(i).put("goal_cost", 0);
                                rList.get(i).put("goal_exhaust", 0);
                            }
                        }
                    }
                }
            } else {
                for(int i=0; i < rList.size(); i++){
                    Map<String, Object> rMap = rList.get(i);
                    rMap.put("goal_cost", 0);
                    rMap.put("goal_exhaust", 0);
                    rList.set(i, rMap);
                }
            }
        } catch(Exception e){ e.printStackTrace(); }

        System.out.println("emplList : " + rList);
        mv.addObject("gIdx", gIdx);
        mv.addObject("emplList", rList);
        return mv;
    }

    /* ////////////////////////////// Return Objects Method ////////////////////////////// */

    /* 직원 코드 생성 */
    @RequestMapping(value = "createEmpleCodeFromAjax", method = RequestMethod.POST)
    @ResponseBody
    public Object createEmpleCodeFromAjax(@RequestParam Map<String, Object> paramMap, HttpSession session){
        JSONObject jobj = new JSONObject();
        log.debug("params : " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        Map<String, Object> rMap = employeeService.selectEmplLargestNumber(sMap);

        int emplCode = Integer.parseInt(rMap.get("maxNumber").toString());

        if (Integer.parseInt(rMap.get("maxNumber").toString()) > 0) jobj.put("max", emplCode + 1);
         else jobj.put("max", 1);

        return jobj;
    }

    /* 직원 등록 */
    @RequestMapping(value = "submitAddEmplFromAjax", method = RequestMethod.POST)
    @ResponseBody
    public Object submitAddEmplFromAjax(@RequestParam Map<String, Object> paramMap, HttpSession session){
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        log.debug("params : " + paramMap);

        paramMap.put("shopId", sMap.get("idx"));
        try{
            employeeService.insertNewEmployee(paramMap);
            if(paramMap.containsKey("ID")){ jobj.put("code", 200); }
            else { jobj.put("code", 900); }
        } catch(Exception e){
            e.printStackTrace();
            jobj.put("code", 900);
        }
        return jobj;
    }

    /* 직원 수정 */
    @RequestMapping(value = "submitModifiedEmplFromAjax", method = RequestMethod.POST)
    @ResponseBody
    public Object submitModifiedEmplFromAjax(@RequestParam Map<String, Object> paramMap, HttpSession session){
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        paramMap.put("shopId", sMap.get("idx"));
        log.debug("params : " + paramMap);
        boolean overCheck = false;

        /* 직원번호 중복 확인 */
        if("true".equals(paramMap.get("over_check").toString())){
            Map<String, Object> rMap = employeeService.selectOverlapFromEmplCodeFromAjax(paramMap);
            if(0 < Integer.parseInt(rMap.get("count").toString())) overCheck = true;
        }
        /* 직원 정보 수정 */
        if(overCheck){ jobj.put("code", 800);
        }else{
            try{ employeeService.updateModifiedEmplFromAjax(paramMap); jobj.put("code" , 200);
            } catch(Exception e) {jobj.put("code", 900);}
        }
        return jobj;
    }

    /* 직원 순서 변경 */
    @RequestMapping(value = "updateEmployeeProcedure", method = RequestMethod.POST)
    @ResponseBody
    public Object updateEmployeeProcedure(@RequestParam Map<String, Object> paramMap){
        log.debug("params : " + paramMap);
        JSONObject jobj = new JSONObject();

        try{
            employeeService.updateEmployeeProcedure(paramMap);
            jobj.put("code", 200);
        } catch(Exception e){
            e.printStackTrace();
            jobj.put("code", 900);
        }
        return jobj;
    }
    /* 직원 목표 등록 */
    @RequestMapping(value = "employeeGoalInsert", method = RequestMethod.POST)
    @ResponseBody
    public Object employeeGoalInsert(@RequestParam Map<String, Object> paramMap){
        log.debug("employeeGoalInsert : " + paramMap);
        JSONObject jobj = new JSONObject();
        try {
            employeeService.employeeGoalInsert(paramMap);
            jobj.put("code", 200);
        } catch(Exception e){
            e.printStackTrace();
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 직원 목표 변경 */
    @RequestMapping(value = "employeeGoalUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Object employeeGoalUpdate(@RequestParam Map<String, Object> paramMap){
        log.debug("employeeGoalUpdate : " + paramMap);
        JSONObject jobj = new JSONObject();
        try{
            employeeService.employeeGoalUpdate(paramMap);
            jobj.put("code", 200);
        } catch(Exception e){
            e.printStackTrace();
            jobj.put("code", 900);
        }

        return jobj;
    }

}
