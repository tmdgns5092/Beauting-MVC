package kr.co.serinusSM.controller;

import kr.co.serinusSM.common.CommonUtils;
import kr.co.serinusSM.service.ScheduleService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("Schedule")
public class ScheduleController {

    Logger log = Logger.getLogger(this.getClass());

    @Resource(name = "scheduleService")
    private ScheduleService scheduleService;

    @Resource(name = "commonUtils")
    private CommonUtils commonUtils;

    /* 스케쥴 페이지 */
    @RequestMapping("scheduleCalendar")
    public ModelAndView scheduleCalendar(@RequestParam Map<String, Object> paramMap, HttpSession session) throws Throwable {
        ModelAndView mv = new ModelAndView("/schedule/scheduleCalendar");
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        // 1. default 설정이다.
        // year과 month를 직접 설정해야한다. 단! month는 +1 시켜서 select 해야한다.
        // 보낼때는 Calendar.MONTH 그대로 보내야 한다.

        String forDate = commonUtils.setForDate(paramMap);

        paramMap.put("forDate", forDate);
        paramMap.put("idx", sMap.get("idx"));
        paramMap.put("id", sMap.get("id"));

        /* 직원 SELECT */
        log.debug("scheduleCalendar: " + paramMap);
        List<Map<String, Object>> eList = scheduleService.employeeSelect(sMap);
        List<Map<String, Object>> sList = scheduleService.scheduleSelect(paramMap);
        Map<String, Object> block = scheduleService.selectBlockDate(paramMap);

        /* 근무시간 SELECT */
        mv.addObject("blockDate", block);
        mv.addObject("forDate", forDate);
        mv.addObject("employeeList",eList);
        mv.addObject("schedule",sList);
        return mv;
    }

    /* AJAX 매장휴무 등록 */
    @RequestMapping("insertHolidayScheduleAjax")
    @ResponseBody
    public Object insertHolidayScheduleAjax(@RequestParam Map<String, Object> paramMap, HttpSession session){
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        paramMap.put("idx", sMap.get("idx"));
        paramMap.put("id", sMap.get("id"));
        log.debug("insertHolidayScheduleAjax : " + paramMap);
        try {
            String days = paramMap.get("days").toString();
            String[] daylist = days.split(",");
            List<Map<String, Object>> idxs = new ArrayList<Map<String, Object>>();
            for (String date : daylist) {
                paramMap.put("date", date);
                scheduleService.insertHolidayScheduleAjax(paramMap);
                Map<String, Object> idx = new HashMap<String, Object>();
                idx.put("idx", paramMap.get("ID").toString());
                idx.put("date", date);
                idxs.add(idx);
            }
            jobj.put("code", 200);
            jobj.put("idxs", idxs);
        }catch(Exception e){
            jobj.put("code", 900);
            e.printStackTrace();
        }

        return jobj;
    }

    /* AJAX 스케줄 등록 */
    @RequestMapping("insertScheduleAjax")
    @ResponseBody
    public Object insertScheduleAjax(@RequestParam Map<String, Object> paramMap, HttpSession session){
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        paramMap.put("idx", sMap.get("idx"));
        paramMap.put("id", sMap.get("id"));
        log.debug("insertScheduleAjax : " + paramMap);
        try {
            String days = paramMap.get("days").toString();
            String[] daylist = days.split(",");
            List<Map<String, Object>> idxs = new ArrayList<Map<String, Object>>();
            for (String date : daylist) {
                paramMap.put("date", date);
                scheduleService.insertScheduleAjax(paramMap);
                Map<String, Object> idx = new HashMap<String, Object>();
                idx.put("idx", paramMap.get("ID").toString());
                idx.put("date", date);
                idxs.add(idx);
            }
            jobj.put("code", 200);
            jobj.put("idxs", idxs);
        }catch(Exception e){
            jobj.put("code", 900);
            e.printStackTrace();
        }

        return jobj;
    }

    /* AJAX 스케줄 수정 */
    @RequestMapping("updateScheduleAjax")
    @ResponseBody
    public Object updateScheduleAjax(@RequestParam Map<String, Object> paramMap, HttpSession session){
        JSONObject jobj = new JSONObject();
        log.debug("updateScheduleAjax : " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        paramMap.put("id", sMap.get("id"));
        try {
            scheduleService.updateScheduleAjax(paramMap);
            jobj.put("code", 200);
        }catch(Exception e){
            e.printStackTrace();
            jobj.put("code", 900);
        }
        return jobj;
    }

    /* 선택달 스케쥴 초기화 */
    @RequestMapping("deleteScheduleAjax")
    @ResponseBody
    public Object deleteScheduleAjax(@RequestParam Map<String, Object> paramMap, HttpSession session){
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        paramMap.put("idx", sMap.get("idx"));
        paramMap.put("id", sMap.get("id"));
        log.debug("deleteScheduleAjax : " + paramMap);
        try {
            if(!paramMap.containsKey("forDate")){ jobj.put("code", 902); return jobj; }
            List<Map<String, Object>> sList = scheduleService.scheduleSelect(paramMap);
            for (Map<String, Object> map : sList) {
                map.put("id", sMap.get("id"));
                scheduleService.deleteScheduleAjax(map);
            }
            jobj.put("code", 200);
        }catch(Exception e){
            e.printStackTrace();
            jobj.put("code", 900);
        }
        return jobj;
    }

    /* 스케줄 삭제 */
    @RequestMapping("deleteListScheduleAjax")
    @ResponseBody
    public Object deleteListScheduleAjax(@RequestParam Map<String, Object> paramMap, HttpSession session){
        JSONObject jobj = new JSONObject();
        log.debug("deleteListScheduleAjax : " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        paramMap.put("id", sMap.get("id"));
        try {
            String days = paramMap.get("idxs").toString();
            String[] idxList = days.split(",");
            for (String idx : idxList) {
                paramMap.put("idx", idx);
                scheduleService.deleteScheduleAjax(paramMap);
            }
            jobj.put("code", 200);
        }catch(Exception e){
            e.printStackTrace();
            jobj.put("code", 900);
        }
        return jobj;
    }

    /* AJAX 회원권 등록 */
    @RequestMapping("deleteOneAjax")
    @ResponseBody
    public Object deleteOneAjax(@RequestParam Map<String, Object> paramMap, HttpSession session){
        JSONObject jobj = new JSONObject();
        log.debug("deleteOneAjax : " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>)session.getAttribute("shopInfo");
        try {
            paramMap.put("id", sMap.get("id"));
            scheduleService.deleteScheduleAjax(paramMap);
            jobj.put("code", 200);
        }catch(Exception e){
            e.printStackTrace();
            jobj.put("code", 900);
        }
        return jobj;
    }
}