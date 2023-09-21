package kr.co.serinusSM.controller;


import com.google.gson.Gson;
import com.popbill.api.MessageService;
import com.popbill.api.PopbillException;
import com.popbill.api.Response;
import kr.co.serinusSM.common.CommonUtils;
import kr.co.serinusSM.common.Paging;
import kr.co.serinusSM.service.ReservationService;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("Reservation")
public class ReservationController {
    Logger log = Logger.getLogger(this.getClass());
    @Resource(name = "reservationService")
    private ReservationService reservationService;
    @Resource(name = "commonUtils")
    private CommonUtils commonUtils;
//    @Autowired
    private MessageService messageService;

    /* 직월별 달성된 월/일 매출&소진 계산 */
    private List<Map<String, Object>> setEmployeeSales(Map<String, Object> sMap, List<Map<String, Object>> rList) throws Exception{
        Gson gson = new Gson();
        List<Map<String, Object>> sList = reservationService.selectSalesForDate(sMap), sale_resource = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date toDay = sdf.parse(sMap.get("forDate").toString());
        int empl1_idx = 0, empl2_idx = 0;
        if(sList == null || sList.size() == 0) return rList;
        else {
            for(Map<String, Object> sale : sList){
                int status = Integer.parseInt(sale.get("status").toString());
                if(sale.containsKey("sale_resource")) sale_resource = gson.fromJson(sale.get("sale_resource").toString(), List.class);
                Date salesDay = sdf.parse(sale.get("date").toString());
                int compare = toDay.compareTo(salesDay);
                for(Map<String, Object> resource : sale_resource){
                    if(status == 0) {
                        if(!"".equals(resource.get("empl1_idx").toString())){
                            empl1_idx = Integer.parseInt(resource.get("empl1_idx").toString());
                            empl2_idx=0;
                        }
                        if (resource.containsKey("empl2_idx")){
                            if(!"".equals(resource.get("empl2_idx").toString())/* || !"undefined".equals(resource.get("empl2_idx").toString()) || !"null".equals(resource.get("empl2_idx").toString())*/) {
                                    empl2_idx = Integer.parseInt(resource.get("empl2_idx").toString());
                            }
                        }
                        for(int i=0; i<rList.size(); i++){
                            Map<String, Object> map = rList.get(i);
                            int idx = Integer.parseInt(map.get("idx").toString());

                            if(idx != empl1_idx && idx != empl2_idx) continue; // 해당 직원이 아니면 넘김

                            int empl_cost=0, // 비교가 2번 이라서 0으로 초기화함
                                    month_cost = Integer.parseInt(map.get("month_cost").toString()),
                                    toDay_cost = Integer.parseInt(map.get("toDay_cost").toString()),
                                    remainingDay_cost = Integer.parseInt(map.get("remainingDay_cost").toString());

                            if(idx == empl1_idx) empl_cost = Integer.parseInt(resource.get("empl1_cost").toString());
                            else if(idx == empl2_idx) empl_cost = Integer.parseInt(resource.get("empl2_cost").toString());

                            if(compare == 0) toDay_cost += empl_cost;// 오늘 판매
                            else if(compare > 0) remainingDay_cost += empl_cost;// 오늘 이전 판매
                            month_cost += empl_cost; // 이달 판매

                            rList.get(i).put("toDay_cost", toDay_cost);
                            rList.get(i).put("remainingDay_cost", remainingDay_cost);
                            rList.get(i).put("month_cost", month_cost);
                        }
                    } else if(status == 1) {
                        int empl_idx = Integer.parseInt(resource.get("empl_idx").toString());
                        for(int i=0; i<rList.size(); i++){
                            Map<String, Object> map = rList.get(i);
                            int idx = Integer.parseInt(map.get("idx").toString());

                            if(empl_idx != idx) continue; // 해당 직원이 아니면 넘김

                            int final_cost = Integer.parseInt(resource.get("final_cost").toString()),
                                    month_cost = Integer.parseInt(map.get("month_cost").toString()),
                                    toDay_cost = Integer.parseInt(map.get("toDay_cost").toString()),
                                    remainingDay_cost = Integer.parseInt(map.get("remainingDay_cost").toString());

                            int type = Integer.parseInt(resource.get("type").toString());
                            if(type == 1)final_cost *= -1;

                            if(compare == 0) toDay_cost += final_cost;// 오늘 판매금액 추가
                            else if(compare > 0) remainingDay_cost += final_cost;// 오늘 이전 판매금액 추가
                            month_cost += final_cost; // 이달 판매금액 추가

                            rList.get(i).put("toDay_cost", toDay_cost);
                            rList.get(i).put("remainingDay_cost", remainingDay_cost);
                            rList.get(i).put("month_cost", month_cost);
                        }
                    } else if(status == 2) {
                        int empl_idx = Integer.parseInt(resource.get("empl_idx").toString());
                        for(int i=0; i<rList.size(); i++){
                            Map<String, Object> map = rList.get(i);
                            int idx = Integer.parseInt(map.get("idx").toString());

                            if(empl_idx != idx) continue; // 해당 직원이 아니면 넘김

                            int cost = Integer.parseInt(resource.get("cost").toString()),
                                    month_cost = Integer.parseInt(map.get("month_cost").toString()),
                                    toDay_cost = Integer.parseInt(map.get("toDay_cost").toString()),
                                    remainingDay_cost = Integer.parseInt(map.get("remainingDay_cost").toString());

                            // log.debug("empl_idx : " + empl_idx + ", empl1_idx : " + empl1_idx + ", empl2_idx : " + empl2_idx);
                            if(compare == 0) toDay_cost += cost;// 오늘 판매금액 추가
                            else if(compare > 0) remainingDay_cost += cost;// 오늘 이전 판매금액 추가
                            month_cost += cost; // 이달 판매금액 추가

                            rList.get(i).put("toDay_cost", toDay_cost);
                            rList.get(i).put("remainingDay_cost", remainingDay_cost);
                            rList.get(i).put("month_cost", month_cost);
                        }
                    }
                }
            }
            return rList;
        }
    }
    // 페이지 예약
    @RequestMapping("calendar")
    public ModelAndView calendar(@RequestParam Map<String, Object> paramMap, HttpSession session) throws Throwable {
        ModelAndView mv = new ModelAndView("/reservation/calendar");
        Map<String, Object> sMap = (Map<String, Object>)session.getAttribute("shopInfo");
        Gson gson = new Gson();
        List<Map<String, Object>> reserList = new ArrayList<>();
        int paramEmplPage = 0;
        int maxEmplPage = 0;
        log.debug("calendar: " + paramMap);

        /* 직원 Select */
        sMap.put("forDate", commonUtils.setForDate(paramMap));
        /* 휴무일 Check */
        Map<String, Object> holidayCheck = reservationService.selectScheduleHoliday(sMap);
        /* paging */
        Map<String, Object> emplPageMap = reservationService.selectEmployeeAllMaxPage(sMap);

        if(!paramMap.containsKey("emplPage")) paramEmplPage = 0;
        else {
            paramEmplPage = Integer.parseInt(paramMap.get("emplPage").toString());
            maxEmplPage = Integer.parseInt(emplPageMap.get("count").toString()) / 7;
            if(paramEmplPage > maxEmplPage) paramEmplPage = maxEmplPage;
        }
        sMap.put("emplPage", paramEmplPage * 7);

        List<Map<String, Object>> rList = reservationService.selectEmployeeAll(sMap), empl_goal = new ArrayList<>();
        rList = setEmployeeSales(sMap, rList);
        Map<String, Object> gMap = reservationService.selectEmployeeGoalData(sMap);
        if(gMap != null) {
            if (gMap.containsKey("employee_goal")) empl_goal = gson.fromJson(gMap.get("employee_goal").toString(), List.class);
            for(int i=0; i<rList.size(); i++){
                for(Map<String, Object> map : empl_goal){
                    if(rList.get(i).get("idx").toString().equals(map.get("idx").toString())){
                        rList.get(i).put("month_goal", map.get("cost"));
                        break;
                    } else continue;
                }
            }
        }

        for(int i=0; i<rList.size(); i++) {
            Map<String, Object> map = rList.get(i);
            int toDay_goal = 0,
                    month_goal = Integer.parseInt(map.get("month_goal").toString()),
                    month_cost = Integer.parseInt(map.get("month_cost").toString()),
                    toDay_cost = Integer.parseInt(map.get("toDay_cost").toString()),
                    remainingDay_cost = Integer.parseInt(map.get("remainingDay_cost").toString()),
                    schedule_count = Integer.parseInt(map.get("schedule_count").toString());
            double month_percent = 0.0, day_percent = 0.0;
            /* 이달의 목표를 달성 하였다. */
            if(month_cost >= month_goal) {
                toDay_goal = 0;
                day_percent = 100;
            }
            /* 목표 달성을 하지 않았다. */
            else {
                /* 일 목표 */
                if(month_goal != 0) toDay_goal = (month_goal - remainingDay_cost) / schedule_count;
                /* 일 목표 달성률 */
                if(toDay_goal != 0 && toDay_cost != 0) day_percent = (double)toDay_cost / toDay_goal * 100;
            }
            /* 월 목표 달성률 */
            if(month_goal != 0 && month_cost != 0) month_percent = (double)month_cost / month_goal * 100;

            rList.get(i).put("toDay_goal", toDay_goal);
            rList.get(i).put("day_percent", day_percent);
            rList.get(i).put("month_percent", month_percent);
        }

        /* 휴무일 체크 */
        if(holidayCheck == null) {
            try {
                reserList = reservationService.selectDatePickReservation(sMap);
            } catch (Exception e) {
                reserList = null;
                e.printStackTrace();
            }
        } else {
            for(int i=0; i<rList.size(); i++){      // 휴무일 이면 모든 직원 예약판 막기
                rList.get(i).put("work_start", sMap.get("shop_close"));
                rList.get(i).put("work_end", sMap.get("shop_open"));
            }
        }

        /* 날짜 형태 케스팅 */
        String moveDate = commonUtils.setForDate(paramMap).replaceFirst("-", "년 ");
        moveDate = moveDate.replaceFirst("-", "월 ");
        moveDate +="일";
        mv.addObject("forDate", commonUtils.setForDate(paramMap));
        mv.addObject("checkDate", moveDate);
        /* List<Map> to JSONArray && resource to object */
        for(int i = 0 ; i < reserList.size(); i++){
            String s = reserList.get(i).get("resource").toString();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(s);
            reserList.get(i).remove("resource");
            reserList.get(i).put("r_time", json.get("time"));
            reserList.get(i).put("r_count", json.get("count"));
            reserList.get(i).put("r_validity", json.get("validity"));
        }
        JSONArray reservation = CommonUtils.convertListToJson(reserList);
        JSONArray employeeList = CommonUtils.convertListToJson(rList);

        /* 이달의 아트 가져오기 */
        Map<String, Object> pmMap = reservationService.selectOnePromotion(sMap);

        /* 이달의 아트 */
        mv.addObject("promotion", pmMap);
        /* 직원 */
        mv.addObject("employeeList", employeeList);
        /* 직원 수 */
        mv.addObject("employeeSize", rList.size());
        /* 직원 페이지 수 */
        mv.addObject("emplPage", paramEmplPage);
        /* 직원 최대 페이지 수 */
        mv.addObject("emplMaxPage", Integer.parseInt(emplPageMap.get("count").toString()) / 8);
        /* 예약 */
        mv.addObject("reservation", reservation);

        return mv;
    }

    @RequestMapping("testLogin")
    public ModelAndView testLogin(@RequestParam Map<String, Object> paramMap, HttpSession session) throws Throwable {
        ModelAndView mv = new ModelAndView("/reservation/calendar");
        Map<String, Object> sMap = reservationService.selectShopInfo(paramMap);
        session.setAttribute("shopInfo", sMap);
        Gson gson = new Gson();
        List<Map<String, Object>> reserList = new ArrayList<>();
        int paramEmplPage = 0;
        int maxEmplPage = 0;
        log.debug("calendar: " + paramMap);

        /* 직원 Select */
        sMap.put("forDate", commonUtils.setForDate(paramMap));
        /* 휴무일 Check */
        Map<String, Object> holidayCheck = reservationService.selectScheduleHoliday(sMap);
        /* paging */
        Map<String, Object> emplPageMap = reservationService.selectEmployeeAllMaxPage(sMap);

        if(!paramMap.containsKey("emplPage")) paramEmplPage = 0;
        else {
            paramEmplPage = Integer.parseInt(paramMap.get("emplPage").toString());
            maxEmplPage = Integer.parseInt(emplPageMap.get("count").toString()) / 7;
            if(paramEmplPage > maxEmplPage) paramEmplPage = maxEmplPage;
        }
        sMap.put("emplPage", paramEmplPage * 7);

        List<Map<String, Object>> rList = reservationService.selectEmployeeAll(sMap), empl_goal = new ArrayList<>();
        rList = setEmployeeSales(sMap, rList);
        Map<String, Object> gMap = reservationService.selectEmployeeGoalData(sMap);
        if(gMap != null) {
            if (gMap.containsKey("employee_goal")) empl_goal = gson.fromJson(gMap.get("employee_goal").toString(), List.class);
            for(int i=0; i<rList.size(); i++){
                for(Map<String, Object> map : empl_goal){
                    if(rList.get(i).get("idx").toString().equals(map.get("idx").toString())){
                        rList.get(i).put("month_goal", map.get("cost"));
                        break;
                    } else continue;
                }
            }
        }

        for(int i=0; i<rList.size(); i++) {
            Map<String, Object> map = rList.get(i);
            int toDay_goal = 0,
                    month_goal = Integer.parseInt(map.get("month_goal").toString()),
                    month_cost = Integer.parseInt(map.get("month_cost").toString()),
                    toDay_cost = Integer.parseInt(map.get("toDay_cost").toString()),
                    remainingDay_cost = Integer.parseInt(map.get("remainingDay_cost").toString()),
                    schedule_count = Integer.parseInt(map.get("schedule_count").toString());
            double month_percent = 0.0, day_percent = 0.0;
            /* 이달의 목표를 달성 하였다. */
            if(month_cost >= month_goal) {
                toDay_goal = 0;
                day_percent = 100;
            }
            /* 목표 달성을 하지 않았다. */
            else {
                /* 일 목표 */
                if(month_goal != 0) toDay_goal = (month_goal - remainingDay_cost) / schedule_count;
                /* 일 목표 달성률 */
                if(toDay_goal != 0 && toDay_cost != 0) day_percent = (double)toDay_cost / toDay_goal * 100;
            }
            /* 월 목표 달성률 */
            if(month_goal != 0 && month_cost != 0) month_percent = (double)month_cost / month_goal * 100;

            rList.get(i).put("toDay_goal", toDay_goal);
            rList.get(i).put("day_percent", day_percent);
            rList.get(i).put("month_percent", month_percent);
        }

        /* 휴무일 체크 */
        if(holidayCheck == null) {
            try {
                reserList = reservationService.selectDatePickReservation(sMap);
            } catch (Exception e) {
                reserList = null;
                e.printStackTrace();
            }
        } else {
            for(int i=0; i<rList.size(); i++){      // 휴무일 이면 모든 직원 예약판 막기
                rList.get(i).put("work_start", sMap.get("shop_close"));
                rList.get(i).put("work_end", sMap.get("shop_open"));
            }
        }

        /* 날짜 형태 케스팅 */
        String moveDate = commonUtils.setForDate(paramMap).replaceFirst("-", "년 ");
        moveDate = moveDate.replaceFirst("-", "월 ");
        moveDate +="일";
        mv.addObject("forDate", commonUtils.setForDate(paramMap));
        mv.addObject("checkDate", moveDate);
        /* List<Map> to JSONArray && resource to object */
        for(int i = 0 ; i < reserList.size(); i++){
            String s = reserList.get(i).get("resource").toString();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(s);
            reserList.get(i).remove("resource");
            reserList.get(i).put("r_time", json.get("time"));
            reserList.get(i).put("r_count", json.get("count"));
            reserList.get(i).put("r_validity", json.get("validity"));
        }
        JSONArray reservation = CommonUtils.convertListToJson(reserList);
        JSONArray employeeList = CommonUtils.convertListToJson(rList);

        /* 이달의 아트 가져오기 */
        Map<String, Object> pmMap = reservationService.selectOnePromotion(sMap);

        /* 이달의 아트 */
        mv.addObject("promotion", pmMap);
        /* 직원 */
        mv.addObject("employeeList", employeeList);
        /* 직원 수 */
        mv.addObject("employeeSize", rList.size());
        /* 직원 페이지 수 */
        mv.addObject("emplPage", paramEmplPage);
        /* 직원 최대 페이지 수 */
        mv.addObject("emplMaxPage", Integer.parseInt(emplPageMap.get("count").toString()) / 8);
        /* 예약 */
        mv.addObject("reservation", reservation);

//        CommonUtils.printList(reserList);
//        log.debug("results : " + mv);

        System.out.println("직원 ? ");
        commonUtils.printList(rList);
        return mv;
    }

//    /* 모달(예약 1) */
//    @RequestMapping(value="reservation1", method=RequestMethod.GET)
//    public ModelAndView reservation1(@RequestParam Map<String, Object> paramMap, HttpSession session) throws Exception{
//        log.debug("param : " + paramMap);
//        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
//        ModelAndView mv = new ModelAndView("/bootstrap-modals/calendar/reservation1");
//
//        /* 페이징 */
//        Paging paging = new Paging();
//
//        paramMap.put("id", sMap.get("id"));
//        if(!paramMap.containsKey("type"))paramMap.put("type", "notype");
//        if(!paramMap.containsKey("data"))paramMap.put("type", "nodata");
//        if(paramMap.get("data") == null || (((String)paramMap.get("data")).trim().length() == 0))paramMap.put("type", "nodata");
//
//        Map<String, Object> cMap = reservationService.selectReservationClientList_Count(paramMap);
//
//        int totalCount = Integer.parseInt(cMap.get("cnt").toString());
//        int pageNo = 1;
//        if (paramMap.get("pageNo") != null) {
//            String paramPageNo = paramMap.get("pageNo").toString();
//            pageNo = Integer.parseInt(paramPageNo);
//        }
//        paging.setPageNo(pageNo);
//        paging.setTotalCount(totalCount);
//
//        paramMap.put("start_idx", paging.getStartIndex());
//        paramMap.put("page_size", paging.getPageSize());
//
//
//        List<Map<String, Object>> cList = reservationService.selectReservationClientList(paramMap);
//
//        mv.addObject("totalCount", totalCount);
//        mv.addObject("cList", cList);
//        mv.addObject("paging", paging);
//        /* /.페이징 */
//
//        mv.addObject("times", paramMap.get("times"));
//        mv.addObject("res_date", paramMap.get("forDate"));
//
//
//        log.debug("cList : " + cList);
//        log.debug("paging : " + paging);
//        return mv;
//    }
    /* 모달(예약 2) */
//    @RequestMapping(value="reservation2", method=RequestMethod.GET)
//    public ModelAndView reservation2(@RequestParam Map<String, Object> paramMap, HttpSession session) {
//        ModelAndView mv = new ModelAndView("/bootstrap-modals/calendar/reservation2");
//        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
//        int type = Integer.parseInt(paramMap.get("type").toString());
//
//        if(type < 1){
//            mv.addObject("client_idx", paramMap.get("client_idx"));
//        } else{
//            mv.addObject("un_name", paramMap.get("name"));
//            mv.addObject("un_phone", paramMap.get("phone"));
//        }
//        mv.addObject("type", paramMap.get("type"));
//        mv.addObject("res_hour", paramMap.get("res_hour"));
//        mv.addObject("res_minute", paramMap.get("res_minute"));
//        mv.addObject("res_empl", paramMap.get("res_empl"));
//        mv.addObject("res_date", paramMap.get("res_date"));
//        log.debug("params : " + paramMap);
//
//        /* 서비스 가져오기 */
//        List<Map<String, Object>> cateMap = reservationService.selectServiceCategory(sMap);
//        mv.addObject("service_list", cateMap);
//
//        /* 직원 가져오기 */
//        List<Map<String, Object>> rList = reservationService.selectEmployeeAll(sMap);
//        mv.addObject("emplList", rList);
//        return mv;
//    }
    /* 예약 수정 */
    /*@RequestMapping(value="modifiedReservation", method=RequestMethod.GET)
    public ModelAndView modifiedReservation(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        ModelAndView mv = new ModelAndView("/bootstrap-modals/calendar/mf-reservation");
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        log.debug("params : " + paramMap);

        *//* 예약정보 불러오기 *//*
        paramMap.put("shopId", sMap.get("id"));
        paramMap.put("shopIdx", sMap.get("idx"));
        Map<String, Object> rMap = reservationService.callReservationDataFromIdx(paramMap);
        *//* 서비스 가져오기 *//*
        List<Map<String, Object>> cateMap = reservationService.selectServiceCategory(sMap);
        *//* 직원 가져오기 *//*
        List<Map<String, Object>> rList = reservationService.selectEmployeeAll(sMap);

        mv.addObject("resList", rMap);
        mv.addObject("service_list", cateMap);
        mv.addObject("emplList", rList);
        mv.addObject("res_idx", paramMap.get("idx"));

        System.out.println("mv return : " + mv);

        return mv;
    }*/


    /* ////////////////////////////// Return Objects Method ////////////////////////////// */

    /* 예약 드레그 이동 */
    @RequestMapping("updateReservationFromAjax")
    @ResponseBody
    public Object updateReservationFromAjax(@RequestParam Map<String, Object> paramMap, HttpSession session){
        boolean updateFlag = false;
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
//        log.debug("params : " + paramMap);

        /* rowspan에 따른 예약 시간 범위 구하기 */
        String endTime = CommonUtils.addStringHour(paramMap, sMap);

        String closeTime = sMap.get("shop_close").toString();
        String closeTimes[] = closeTime.split(":");
        int closeHour = Integer.parseInt(closeTimes[0]);
        int closeMinute = Integer.parseInt(closeTimes[1]);

        String endTimes[] = endTime.split(":");
        int endHour = Integer.parseInt(endTimes[0]);
        int endMinute = Integer.parseInt(endTimes[1]);

        if(closeHour < endHour || (closeHour <= endHour && closeMinute <= endMinute)){ paramMap.put("end_time", closeTime); }
        else { paramMap.put("end_time", endTime); }
        paramMap.put("shopId", sMap.get("id"));

        /* move-time check */
        Map<String, Object> checkMap = reservationService.selectReservationMoveCheck(paramMap);
//        CommonUtils.printMap(checkMap);
        if(checkMap == null || checkMap.isEmpty()) updateFlag = true;

        /* 예약 업데이트 */
        if(updateFlag){
            try{
                /* 예약 문자 취소 */
                /* 예약 문자 발송 */
                reservationService.updateReservation(paramMap);
                jobj.put("code", 200);
            } catch(Exception e){ jobj.put("code", 900); }
        }else jobj.put("code", 901);

        return jobj;
    }
    /* 예약 드래그 End Time Update */
    @RequestMapping("updateFormReservationEndTime")
    @ResponseBody
    public Object updateFormReservationEndTime(@RequestParam Map<String, Object> paramMap){
        JSONObject jobj = new JSONObject();
        log.debug("params : " + paramMap);

        try{ reservationService.updateFormReservationEndTime(paramMap); jobj.put("code", 200);}
        catch(Exception e){ jobj.put("code", 900);}

        return jobj;
    }
    /* 예약등록 - 고객 검색 */
    @RequestMapping("clientList")
    @ResponseBody
    public Object clientList(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        Map<String, Object> sMap = (Map<String, Object>)session.getAttribute("shopInfo");
        List<Map<String, Object>> clientsList = null;
        paramMap.put("shopId", sMap.get("id"));
        JSONObject jobj = new JSONObject();
        log.debug("params : "+paramMap);


        try{clientsList = reservationService.selectSortingAndPagingClients(paramMap); jobj.put("code", 200);}
        catch(Exception e){ jobj.put("code", 900); }

        /* 데이터 */
        jobj.put("clientList", clientsList);

        return jobj;
    }

    /* 예약 드래그 End Time Update */
    @RequestMapping("serviceDetailCall")
    @ResponseBody
    public Object serviceDetailCall(@RequestParam Map<String, Object> paramMap, HttpSession session){
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        log.debug("params : " + paramMap);
        try{
            paramMap.put("shopIdx", sMap.get("idx"));
            List<Map<String, Object>> rMap = reservationService.serviceDetailCall(paramMap);
            jobj.put("detailList", rMap);
            jobj.put("code",200);
        } catch(Exception e){
            jobj.put("code", 900);
        }


        return jobj;
    }

    /* 예약 등록 */
    @RequestMapping("addReservation")
    @ResponseBody
    public Object addReservation(@RequestParam Map<String, Object> paramMap, HttpSession session){
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        paramMap.put("shopIdx", sMap.get("idx"));
        paramMap.put("shopId", sMap.get("id"));
        log.debug("params : " + paramMap);

        /* 직원 스케쥴 확인 */
/*        Map<String, Object> sdMap = reservationService.selectEmployeScheduleCheck(paramMap);
        if(sdMap == null || sdMap.isEmpty()){
            *//* 직원이 출근하지 않은 시간 *//*
            jobj.put("code", 903);
        }*/
//        else{
            /* 직원이 출근한 시간 */
            /* 예약 시간 확인 */
            Map<String, Object> rMap = reservationService.selectReservationOverlapCheck(paramMap);


            if(rMap == null || rMap.isEmpty()) {
                /* 예약 추가 */
                /* 미등록 고객 예약 등록 */
                if("1".equals(paramMap.get("res_type").toString())){
                    paramMap.put("clientIdx", "null");
                }
                /* 회원 예약 등록 */
                else{
                    paramMap.put("un_name", "null");
                    paramMap.put("un_phone", "null");
                }
                try{
                    reservationService.addReservation(paramMap);
                    boolean insertCheck = false;
                    if(paramMap.containsKey("ID")){
                        insertCheck = true;
                    }


//                    System.out.println("sms checck : " + Integer.parseInt(paramMap.get("sms_check").toString()));
//                    System.out.println("clientHasSmsCheck : "+ paramMap.get("clientHasSmsCheck").toString());
//                    System.out.println("res type : " + Integer.parseInt(paramMap.get("res_type").toString()));
                    /* 예약 자동문자 발송 */
//                    if((Integer.parseInt(paramMap.get("sms_check").toString()) == 1 && Integer.parseInt(paramMap.get("clientHasSmsCheck").toString()) == 1) || Integer.parseInt(paramMap.get("res_type").toString()) > 0){

                    System.out.println("예약 타입 (0:회원, 1:비회원) : " + paramMap.get("res_type"));
                    System.out.println("일회성 수신 동의 (0:동의, 1:비동의) : " + paramMap.get("sms_check"));
                    System.out.println("고객 수신 동의 정보 (0:비동의, 1:동의) : " + paramMap.get("clientHasSmsCheck"));
                    if (
                        // 회원인 경우 발송 조건
                        (Integer.parseInt(paramMap.get("res_type").toString()) == 0 &&              // 회원 res type : 0
                                Integer.parseInt(paramMap.get("sms_check").toString()) == 0 &&      // 일회성 수신 동의 : 0
                                Integer.parseInt(paramMap.get("clientHasSmsCheck").toString()) == 1 // 고객 수신 동의 : 1
                        ) ||
                        // 비회원인 경우 발송 조건
                        (Integer.parseInt(paramMap.get("res_type").toString()) == 1 &&              // 비회원 res type : 1
                                Integer.parseInt(paramMap.get("sms_check").toString()) == 0         // 일회성 수신 동의 : 0
                        )
                    ) {
                        reservationService.autoMessageCheckAndSend(paramMap, sMap, messageService);
                        System.out.println("문자 발송 !!! ");
//                        System.out.println("reservation sms check :  "+ paramMap.get("sms_check"));
//                        System.out.println("client has sms check :  "+ paramMap.get("clientHasSmsCheck"));
//                        System.out.println("res type :  "+ paramMap.get("res_type"));
                    } else {
                        System.out.println("발송 조건 불충분 !!! ");
//                        System.out.println("reservation sms check :  "+ paramMap.get("sms_check"));
//                        System.out.println("client has sms check :  "+ paramMap.get("clientHasSmsCheck"));
//                        System.out.println("res type :  "+ paramMap.get("res_type"));
                    }

                    if(insertCheck){
                        if(session.getAttribute("call_result_code") != null) {
                            session.removeAttribute("call_result_code");
                            session.removeAttribute("call_res_data");
                        }
                        jobj.put("code", 200);
                    }
                    else jobj.put("code", 902);
                }catch(Exception e){
                    jobj.put("code", 900);
                    jobj.put("error", e);
                    jobj.put("message", e.printStackTrace());
                    e.printStackTrace();
                }
            }else{
                jobj.put("code", 901);
            }
//        }

        return jobj;
    }

    /* 예약 수정 */
    @RequestMapping("updateReservation")
    @ResponseBody
    public Object updateReservation(@RequestParam Map<String, Object> paramMap, HttpSession session){
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        log.debug("params : " + paramMap);

        try{
            paramMap.put("shopIdx", sMap.get("idx"));
            paramMap.put("shopId", sMap.get("id"));
            String numberEndTime = paramMap.get("end_time").toString();
            numberEndTime = numberEndTime.replaceAll(":", "");
            paramMap.put("numberEndTime", numberEndTime);

            /* 직원 스케줄 확인 */
            /*Map<String, Object> emplCheckMap = reservationService.selectEmployeeScheduleCheck(paramMap);
            if(emplCheckMap == null || emplCheckMap.isEmpty()){
                jobj.put("code", 902);
                return jobj;
            }*/
        } catch(Exception e){
            e.printStackTrace();
            jobj.put("code", 900);
            return jobj;
        }
        /* 예약 시간 확인 */
        Map<String, Object> rMap = reservationService.selectReservationOverlapCheck_ver2(paramMap);

        if(rMap == null || rMap.isEmpty()) {
            /* 예약 업데이트 (세부사항) */
            try{
                reservationService.updateReservationDetail(paramMap);
                jobj.put("code", 200);
            }catch(Exception e){
                jobj.put("code", 900);
                e.printStackTrace();
            }
        }else{
            jobj.put("code", 901);
        }

        return jobj;
    }
    /* 예약 불이행 */
    @RequestMapping("reservationCancel")
    @ResponseBody
    public Object reservationCancel(@RequestParam Map<String, Object> paramMap, HttpSession session){
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        log.debug("params : " + paramMap);

        System.out.println("reservation idx (param name : idx) : " + paramMap.get("idx"));
        /* 예약 업데이트 (세부사항) */
        try{
            paramMap.put("shopId", sMap.get("id"));
            paramMap.put("shopIdx", sMap.get("idx"));
            /* 예약 확인 */
            Map<String, Object> rMap = reservationService.callReservationDataFromIdx(paramMap);
            if("0".equals(rMap.get("type").toString())){
                reservationService.updateReservationCancel1(paramMap);
                jobj.put("code", 200);
            } else{
                /* 예약 불이행 업데이트 */
                reservationService.updateReservationCancel2(paramMap);
                jobj.put("code", 200);
            }
        }catch(Exception e){
            jobj.put("code", 900);
            e.printStackTrace();
        }

        return jobj;
    }

    /* 예약 취소 */
    @RequestMapping("reservationRemove")
    @ResponseBody
    public Object reservationRemove(@RequestParam Map<String, Object> paramMap, HttpSession session){
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        log.debug("params : " + paramMap);

        try{
            paramMap.put("shopIdx", sMap.get("idx"));
            paramMap.put("shopId", sMap.get("id"));
            Map<String, Object> clientInfoMap = reservationService.selectClientInfo(paramMap);
            reservationService.reservationRemove(paramMap);

            // 예약 취소로 인한 예약 관련 예약문자 취소
//            paramMap.put("shopId", sMap.get("id"));
            List<Map<String, Object>> receiptList = reservationService.selectReceiptNum(paramMap);
            for(int i = 0; i < receiptList.size(); i++){
                String receiptNum = receiptList.get(i).get("receipt_num").toString();
                try{
                    messageService.cancelReserve(sMap.get("com_num").toString(), receiptNum);
                } catch(PopbillException e){
                    log.error("예약 취소 문자 - 이미 취소된 문자 입니다.");
                }
            }
            try{
            if(clientInfoMap.get("idx") == null || Integer.parseInt(clientInfoMap.get("sms_check").toString()) == 1){
            /* 예약 자동문자 발송 */
                reservationService.autoCancleMessageCheckAndSend(paramMap, sMap, clientInfoMap, messageService);
            }
            } catch(Exception el){
                jobj.put("code", 200);
            }

            jobj.put("code", 200);
        }
        catch(Exception e){
            jobj.put("code", 900);
            e.printStackTrace();
        }

        return jobj;
    }

    /* 예약 불이행 취소 */
    @RequestMapping("reservationStatusUpdateDefault")
    @ResponseBody
    public Object reservationStatusUpdateDefault(@RequestParam Map<String, Object> paramMap, HttpSession session){
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        log.debug("params : " + paramMap);

        try{
            paramMap.put("shopId", sMap.get("id"));
            reservationService.reservationStatusUpdateDefault(paramMap);
            reservationService.updateClientNoshowCountMinus(paramMap);
            jobj.put("code", 200);
        }
        catch(Exception e){
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 예약 드래그 End Time Update */
    @RequestMapping("selectEmplGoalCostAjax")
    @ResponseBody
    public Object selectEmplGoalCostAjax(@RequestParam Map<String, Object> paramMap, HttpSession session){
        JSONObject jobj = new JSONObject();
        log.debug("selectEmplGoalCostAjax : " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>)session.getAttribute("shopInfo");
        int paramEmplPage = 0;
        int maxEmplPage = 0;
        /* paging */
        Map<String, Object> emplPageMap = reservationService.selectEmployeeAllMaxPage(sMap);
        if(!paramMap.containsKey("emplPage")){
            paramEmplPage = 0;
        } else {
            paramEmplPage = Integer.parseInt(paramMap.get("emplPage").toString());
            maxEmplPage = Integer.parseInt(emplPageMap.get("count").toString()) / 8;
            if(paramEmplPage > maxEmplPage){
                paramEmplPage = maxEmplPage;
            }
        }
        sMap.put("emplPage", paramEmplPage * 8);

        List<Map<String, Object>> rList = reservationService.selectEmployeeAll(sMap);

        try {
            // reservationService.selectEmplGoalCostAjax(paramMap);
            jobj.put("code", 200);
        } catch(Exception e){ jobj.put("code", 900);}

        return jobj;
    }

    /* 모달(예약 1) */
    @RequestMapping("reservation1")
    @ResponseBody
    public Object reservation1(@RequestParam Map<String, Object> paramMap, HttpSession session) throws Exception{
        log.debug("param : " + paramMap);
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");

        try{
            /* 페이징 */
            Paging paging = new Paging();
            paramMap.put("id", sMap.get("id"));
            if(!paramMap.containsKey("type"))paramMap.put("type", "notype");
            if(!paramMap.containsKey("data"))paramMap.put("type", "nodata");
            if(paramMap.get("data") == null || (((String)paramMap.get("data")).trim().length() == 0))paramMap.put("type", "nodata");
            Map<String, Object> cMap = reservationService.selectReservationClientList_Count(paramMap);
            int totalCount = Integer.parseInt(cMap.get("cnt").toString());
            int pageNo = 1;
            if (paramMap.get("pageNo") != null) {
                String paramPageNo = paramMap.get("pageNo").toString();
                pageNo = Integer.parseInt(paramPageNo);
            }
            paging.setPageNo(pageNo);
            paging.setTotalCount(totalCount);

            paramMap.put("start_idx", paging.getStartIndex());
            paramMap.put("page_size", paging.getPageSize());

            List<Map<String, Object>> cList = reservationService.selectReservationClientList(paramMap);

            for(int i = 0; i < cList.size(); i++){
                if(cList.get(i).get("phone") != null){
                    String phoneFormat = cList.get(i).get("phone").toString();
                    cList.get(i).put("phone", CommonUtils.phone(phoneFormat));
                } else {
                    cList.get(i).put("phone", "");
                }
            }
            jobj.put("totalCount", totalCount);
            jobj.put("cList", cList);
            jobj.put("paging", paging);
            /* /.페이징 */

            jobj.put("times", paramMap.get("times"));
            jobj.put("res_date", paramMap.get("forDate"));

            jobj.put("code", 200);
        } catch(Exception e){
            e.printStackTrace();
            jobj.put("code", 900);
        }

//        System.out.println("jobj : " + jobj);
        return jobj;
    }


    // 예약 모달1 Ver 2
    @RequestMapping(value="reservation1Paging")
    @ResponseBody
    public Object reservation1Paging(@RequestParam Map<String, Object> paramMap, HttpSession session){
        log.debug("param : " + paramMap);
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        Map<String, Object> cMap = null;
        List<Map<String, Object>> cList = null;


        /* 페이징 */
        Paging paging = new Paging();

        paramMap.put("id", sMap.get("id"));
        if(!paramMap.containsKey("type"))paramMap.put("type", "notype");
        if(!paramMap.containsKey("data"))paramMap.put("type", "nodata");
        if(paramMap.get("data") == null || (((String)paramMap.get("data")).trim().length() == 0))paramMap.put("type", "nodata");

        try{
            cMap = reservationService.selectReservationClientList_Count(paramMap);
        }catch(Exception e){
            jobj.put("code", 900);
        }

        int totalCount = Integer.parseInt(cMap.get("cnt").toString());
        int pageNo = 1;
        if (paramMap.get("pageNo") != null) {
            String paramPageNo = paramMap.get("pageNo").toString();
            pageNo = Integer.parseInt(paramPageNo);
        }
        paging.setPageNo(pageNo);
        paging.setTotalCount(totalCount);

        paramMap.put("start_idx", paging.getStartIndex());
        paramMap.put("page_size", paging.getPageSize());

        try{
            cList = reservationService.selectReservationClientList(paramMap);
            for(int i = 0; i < cList.size(); i++){
                String phoneFormat = cList.get(i).get("phone").toString();
                cList.get(i).put("phone", CommonUtils.phone(phoneFormat));
            }
        }catch(Exception e){
            jobj.put("code", 900);
        }

//        jobj.put("totalCount", totalCount);
        jobj.put("code", 200);
        jobj.put("cList", cList);
        jobj.put("paging", paging);
        /* /.페이징 */

        log.debug(paging);

        return jobj;
    }

    // 예약 모달 2 Ver 2
    @RequestMapping("reservation2")
    @ResponseBody
    public Object reservation2(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        log.debug("params : " + paramMap);
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");

        try{
            int type = Integer.parseInt(paramMap.get("type").toString());
            if(type < 1){
                jobj.put("client_idx", paramMap.get("client_idx"));
            } else{
                jobj.put("un_name", paramMap.get("name"));
                jobj.put("un_phone", paramMap.get("phone"));
            }
            jobj.put("type", paramMap.get("type"));
            jobj.put("clientHasSmsCheck", paramMap.get("sms_check"));
            jobj.put("res_hour", paramMap.get("res_hour"));
            jobj.put("res_minute", paramMap.get("res_minute"));
            jobj.put("res_empl", paramMap.get("res_empl"));
            jobj.put("res_date", paramMap.get("res_date"));
            jobj.put("default_minut", sMap.get("default_minute"));
            jobj.put("shop_open", sMap.get("shop_open"));
            jobj.put("shop_close", sMap.get("shop_close"));

            /* 서비스 가져오기 */
            List<Map<String, Object>> cateMap = reservationService.selectServiceCategory(sMap);
            jobj.put("service_list", cateMap);

            /* 직원 가져오기 */
            List<Map<String, Object>> rList = reservationService.selectEmployeeAll(sMap);
            jobj.put("emplList", rList);

            jobj.put("code", 200);
        } catch(Exception e){
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 예약 수정 */
    @RequestMapping("modifiedReservation")
    @ResponseBody
    public Object modifiedReservation(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        log.debug("params : " + paramMap);
        try{
            /* 예약정보 불러오기 */
            paramMap.put("shopId", sMap.get("id"));
            paramMap.put("shopIdx", sMap.get("idx"));
            Map<String, Object> rMap = reservationService.callReservationDataFromIdx(paramMap);
            /* 서비스 가져오기 */
            List<Map<String, Object>> cateMap = reservationService.selectServiceCategory(sMap);
            /* 직원 가져오기 */
            List<Map<String, Object>> rList = reservationService.selectEmployeeAll(sMap);

            jobj.put("resList", rMap);
            jobj.put("service_list", cateMap);
            jobj.put("emplList", rList);
            jobj.put("res_idx", paramMap.get("idx"));
            jobj.put("shopOpen", sMap.get("shop_open"));
            jobj.put("shopClose", sMap.get("shop_close"));

            jobj.put("code", 200);
        } catch(Exception e){
            e.printStackTrace();
            jobj.put("code", 900);
        }

//        System.out.println("mv return : " + jobj);

        return jobj;
    }

    /* 예약 idx 로 고객 idx 불러오기 */
    /* 예약 수정 */
    @RequestMapping("clientInfoIdxCall")
    @ResponseBody
    public Object clientInfoIdxCall(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        log.debug("params : " + paramMap);


        try{
            paramMap.put("shop_id", sMap.get("id"));
            Map<String, Object> cMap = reservationService.selectClientIdx(paramMap);

            jobj.put("client_idx", cMap.get("client_idx"));
            jobj.put("type", cMap.get("type"));
            jobj.put("code", 200);
        } catch(Exception e){
            e.printStackTrace();
            jobj.put("code", 900);
        }

//        System.out.println("mv return : " + jobj);

        return jobj;
    }

    /* 시술 카테고리 조회 */
    @RequestMapping("selectCategory")
    @ResponseBody
    public Object selectCategory(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        log.debug("selectCategory : " + paramMap);
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        try {
            List<Map<String, Object>> cateMap = reservationService.selectServiceCategory(sMap);
            jobj.put("cateMap", cateMap);
            jobj.put("code", 200);
        }catch (Exception e){
            jobj.put("code", 900);
            e.printStackTrace();
        }
        return jobj;
    }

    /* 진행 중인 프로모션 조회 */
    @RequestMapping("selectOnePromotion")
    @ResponseBody
    public Object selectPromotion(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        log.debug("selectOnePromotion : " + paramMap);
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        try {
            paramMap.put("shopIdx", sMap.get("idx"));
            Map<String, Object> pmMap = reservationService.selectOnePromotion(paramMap);
            jobj.put("pmMap", pmMap);
            jobj.put("code", 200);
        } catch (Exception e){
            jobj.put("code", 900);
            e.printStackTrace();
        }
        return jobj;
    }

    /* 프로모션 이력 조회 */
    @RequestMapping("selectListPromotion")
    @ResponseBody
    public Object selectListPromotion(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        log.debug("selectOnePromotion : " + paramMap);
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        try {
            paramMap.put("shopIdx", sMap.get("idx"));
            List<Map<String, Object>> pmList = reservationService.selectListPromotion(paramMap);
            log.debug(pmList);
            log.debug(pmList.size());
            if(pmList.size() > 0){
                jobj.put("pmList", pmList);
                jobj.put("code", 200);
            } else if(pmList.size() == 0) jobj.put("code", 902);
            else jobj.put("code", 900);

        } catch (Exception e){
            jobj.put("code", 900);
            e.printStackTrace();
        }
        return jobj;
    }

    /* 프로모션 등록 */
    @RequestMapping("insertPromotion")
    @ResponseBody
    public Object insertPromotion(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        log.debug("insertPromotion : " + paramMap);
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        try {
            paramMap.put("shopIdx", sMap.get("idx"));
            reservationService.insertPromotion(paramMap);
            jobj.put("code", 200);
        } catch (Exception e){
            jobj.put("code", 900);
            e.printStackTrace();
        }
        return jobj;
    }

    /* 프로모션 수정 */
    @RequestMapping("updatePromotion")
    @ResponseBody
    public Object updatePromotion(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        log.debug("updatePromotion : " + paramMap);
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        try {
            paramMap.put("shopIdx", sMap.get("idx"));
            reservationService.updatePromotion(paramMap);
            jobj.put("code", 200);
        } catch (Exception e){
            jobj.put("code", 900);
            e.printStackTrace();
        }
        return jobj;
    }

    // 예약 자동 문자 재발송 (문자 취소 & 문자 발송)
    @RequestMapping("autoReservationMsgReSend")
    @ResponseBody
    public Object autoReservationMsgReSend(@RequestParam Map<String, Object> paramMap, HttpSession session) {
//        log.debug("updatePromotion : " + paramMap);
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");

        sMap.put("resIdx", paramMap.get("resIdx"));
        sMap.put("res_idx", paramMap.get("resIdx"));
        try{
//        List<Map<String, Object>> settingList = reservationService.selectMessageAuto(sMap);
        List<Map<String, Object>> list = reservationService.beforSendHistory(sMap);
        Map<String, Object> resData = reservationService.selectReservationFromIdx(sMap);
//        System.out.println("쿼리 셀렉트");
//        commonUtils.printMap(resData);
        sMap.put("list", list);


            // 예약 문자 취소
            for(int i = 0; i < list.size(); i++){
                try{
                    Response response = messageService.cancelReserve(sMap.get("com_num").toString(), list.get(i).get("receipt_num").toString());
//                    System.out.println("문자 취소 결과 : " + response);
                } catch(PopbillException pe){
//                    System.out.println("예약 문자가 아닙니다...");
                    pe.printStackTrace();
                }
            }
            // 예약 문자 취소 업데이트 msg_history update
            if(list.size() > 0)
                reservationService.updateMsgHistroyFromReservationMsg(sMap);

            // 예약문자 재발송 resend
            String from = resData.get("date").toString();
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date resDateFormat = transFormat.parse(from);

            SimpleDateFormat hour = new SimpleDateFormat("HH");
            SimpleDateFormat minute = new SimpleDateFormat("mm");
            String hourDate = hour.format(resDateFormat);
            String minuteDate = minute.format(resDateFormat);

            Map<String, Object> map = new HashMap();

//            System.out.println("hour str : " + hourDate);
//            System.out.println("minute str : " + minuteDate);
            map.put("shopIdx", sMap.get("idx"));
            map.put("clientIdx", resData.get("client_idx"));
            map.put("un_name", resData.get("un_name"));
            map.put("un_phone", resData.get("un_phone"));
            map.put("res_date", resData.get("date"));
            map.put("res_hour", hourDate);
            map.put("res_minute", minuteDate);
            map.put("res_type", resData.get("type"));
            if(resData.get("name") == null) map.put("client_name", "name");
            else map.put("client_name", resData.get("name"));
            if(resData.get("phone") == null) map.put("client_phone", "");
            else map.put("client_phone", resData.get("phone"));

            map.put("ID", paramMap.get("resIdx"));
//            System.out.println("컨트롤러 단");
//            commonUtils.printMap(map);
            reservationService.autoMessageCheckAndSend(map, sMap, messageService);

            jobj.put("code", 200);
        } catch(Exception e){
            e.printStackTrace();
            jobj.put("code", 900);
        }

        return jobj;
    }
}
