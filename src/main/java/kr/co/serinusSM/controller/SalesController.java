package kr.co.serinusSM.controller;

import com.google.gson.Gson;
import com.popbill.api.MessageService;
import kr.co.serinusSM.common.CommonUtils;
import kr.co.serinusSM.common.Paging;
import kr.co.serinusSM.service.SalesService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
@RequestMapping("Sales")
public class SalesController {
    Logger log = Logger.getLogger(this.getClass());

    @Resource(name = "salesService")
    private SalesService salesService;

    @Resource(name = "commonUtils")
    private CommonUtils commonUtils;

//    @Autowired
    private MessageService messageService;

    /* 페이지 판매 */
    @RequestMapping("sales")
    public ModelAndView sales(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        ModelAndView mv = new ModelAndView("/sales/sales");
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        Map<String, Object> rMap = new HashMap<String, Object>();
        log.debug("sales : " + paramMap);

        // 예약을 통한 판매
        if(paramMap.containsKey("payment")){
            paramMap.put("shopId", sMap.get("id"));
            paramMap.put("shopIdx", sMap.get("idx"));

            /* 예약 타입 불러오기 */
            int res_type = salesService.selectResType(paramMap);
            if(res_type == 0){
                /* 예약 결제 정보 불러오기 (회원)*/
                rMap = salesService.selectCallReservationPayInfo1(paramMap);
                log.debug("회원 : " + rMap);
                mv.addObject("client_type", "member");
                System.out.println(mv);
            } else {
                /* 예약 결제 정보 불러오기 (미등록 회원)*/
                rMap = salesService.selectCallReservationPayInfo2(paramMap);
                log.debug("미등록 회원 : " + rMap);
                mv.addObject("client_type", "no_member");
                System.out.println(mv);
            }

            // 카테고리 idx 가져오기
            paramMap.put("category_name", rMap.get("category"));
            Map<String, Object> cateMap = salesService.selectServiceCategoryIdxCall(paramMap);

            mv.addObject("cateMap", cateMap);
            mv.addObject("serviceFlag", "true");
            mv.addObject("forDate", paramMap.get("forDate"));

            mv.addObject("payment", paramMap.get("payment"));
            mv.addObject("forDate", paramMap.get("forDate"));
        }
        // 해더를 통한 판매
        else{
            paramMap.put("shopId", sMap.get("id"));
            rMap = salesService.selectClientInfoFromIdx(paramMap);
            mv.addObject("serviceFlag", "false");
            mv.addObject("client_type", "member");
            mv.addObject("forDate", "-1");

            mv.addObject("client_idx", paramMap.get("client_idx"));
        }


        if (rMap == null || !rMap.containsKey("prepaid_map") || rMap.get("prepaid_map") == null || "0".equals(rMap.get("prepaid_map").toString())){
            rMap.put("prepaid_map", "-1");
        } else {
//            JSONArray preMap = commonUtils.convertStrToJson(rMap.get("prepaid_map").toString());
//            rMap.put("prepaid_map", preMap);
        }


        if (rMap == null || !rMap.containsKey("ticket_map") || rMap.get("ticket_map") == null || "0".equals(rMap.get("ticket_map").toString())){
            rMap.put("ticket_map", "-1");
        } else {
//            org.json.simple.JSONObject ticMap = commonUtils.convertStrToJson(rMap.get("ticket_map").toString());
//            rMap.put("ticket_map", ticMap);
        }
        paramMap.put("shopId", sMap.get("idx"));
        List<Map<String, Object>> emplList = salesService.selectSaleEmployeeListCall(paramMap);
        JSONArray jsonArray = commonUtils.getJsonArrayFromList(emplList);

        mv.addObject("eList", jsonArray);
        mv.addObject("rMap", rMap);
        return mv;
    }

    /* 페이지 판매 내역 */
    @RequestMapping("historyOfClients")
    public ModelAndView historyOfClients(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        ModelAndView mv = new ModelAndView("/sales/historyOfClients");
        log.debug("historyOfClients : " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        paramMap.put("shop_id", sMap.get("id"));
        Map<String, Object> eMap = salesService.selectClient(paramMap);
        mv.addObject("client", eMap);
        return mv;
    }

    /* 페이지 판매 고객 검색 */
    @RequestMapping("searchClient")
    public ModelAndView searchClient(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        ModelAndView mv = new ModelAndView("/sales/salesClientSelect");
        Paging paging = new Paging();
        log.debug("salesClientSelect : " + paramMap);

        paramMap.put("id", sMap.get("id"));
        if(!paramMap.containsKey("type")) paramMap.put("type", "Empty");
        else mv.addObject("type", paramMap.get("type"));
        if(paramMap.containsKey("data")) mv.addObject("data", paramMap.get("data"));
        else mv.addObject("data", "");

        Map<String, Object> cMap = salesService.searchClientPagingCount(paramMap);

        /* Paging */
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

        log.debug("param : " + paramMap);
        List<Map<String, Object>> cList = salesService.searchClientPagingContent(paramMap);

        mv.addObject("clientList", cList);
        mv.addObject("totalCount", cMap.get("cnt"));
        mv.addObject("paging", paging);


        return mv;
    }


    /* ////////////////////////////// Return Objects Method ////////////////////////////// */

    /* 고객 예약내역 불러오기 */
    @RequestMapping("selectReservationHistory")
    @ResponseBody
    public Object selectReservationHistory(@RequestParam Map<String, Object> paramMap, HttpSession session){
        JSONObject jobj = new JSONObject();
        log.debug("selectReservationHistory : " + paramMap);
        Map<String, Object> s = (Map<String, Object>) session.getAttribute("shopInfo");
        try {
            Paging paging = new Paging();
            int pageNo = 1;
            paramMap.put("id", s.get("id"));
            Map<String, Object> rMap = salesService.selectAllReservationHistory(paramMap);
            int totalCount = Integer.parseInt(rMap.get("count").toString());            // pageNumber
            if (paramMap.get("pageNo") != null) {                                         // param pageNumber check
                String paramPageNo = paramMap.get("pageNo").toString();                 // get pageNumber
                pageNo = Integer.parseInt(paramPageNo);
            }
            paging.setPageNo(pageNo);                                                   // sql start index set
            paging.setTotalCount(totalCount);                                           // sql end index set
            paramMap.put("start_idx", paging.getStartIndex());
            paramMap.put("end_idx", paging.getPageSize());

            List<Map<String, Object>> shList = salesService.selectReservationHistory(paramMap);
            jobj.put("salesList", shList);
            jobj.put("paging", paging);
        }catch (Exception e){
            e.printStackTrace();
        }

        return jobj;
    }

    /* 고객 Services 판매내역 불러오기 */
    @RequestMapping(value = "selectServicesTypeHistory", method = RequestMethod.POST, produces = "application/json; charset=utf8")
    @ResponseBody
    public Object selectServicesTypeHistory(@RequestParam Map<String, Object> paramMap, HttpSession session){
        Gson gson = new Gson();
        JSONObject jobj = new JSONObject();
        jobj.put("code", 900);
        log.debug("selectServicesTypeHistory : " + paramMap);
        Map<String, Object> s = (Map<String, Object>) session.getAttribute("shopInfo");
        Paging paging = new Paging();
        int pageNo = 1;
        try {
            paramMap.put("id", s.get("id"));
            Map<String, Object> cMap = salesService.selectCountServicesType(paramMap);
            int totalCount = Integer.parseInt(cMap.get("count").toString());            // pageNumber
            if (paramMap.get("pageNo") != null) {                                         // param pageNumber check
                String paramPageNo = paramMap.get("pageNo").toString();                 // get pageNumber
                pageNo = Integer.parseInt(paramPageNo);
            }
            paging.setPageNo(pageNo);                                                   // sql start index set
            paging.setTotalCount(totalCount);                                           // sql end index set
            paramMap.put("start_idx", paging.getStartIndex());
            paramMap.put("end_idx", paging.getPageSize());

            List<Map<String, Object>> shList = salesService.selectServicesTypeHistory(paramMap);
            jobj.put("salesList", shList);
            jobj.put("paging", paging);
        } catch (Exception e) {
            jobj.put("code", 902);
            e.printStackTrace();
        }
        log.debug(jobj);
        return gson.toJson(jobj);
    }

    /* 한번의 판매내역(시술+제품) 불러오기 */
    @RequestMapping(value = "selectSalesDetailAjax", method = RequestMethod.POST, produces = "application/json; charset=utf8")
    @ResponseBody
    public Object selectSalesDetailAjax(@RequestParam Map<String, Object> paramMap, HttpSession session){
        JSONObject jobj = new JSONObject();
        jobj.put("code", 200);
        Gson gson = new Gson();
        Map<String, Object> s = (Map<String, Object>)session.getAttribute("shopInfo");
        paramMap.put("id", s.get("id"));
        log.debug("selectSalesDetailAjax : " + paramMap);
        try {
            paramMap.put("type", 0);
            Map<String, Object> sMap = salesService.selectSalesDateAjax(paramMap);
            if(sMap != null) jobj.put("surgery", sMap);
            paramMap.put("type", 3);
            Map<String, Object> pMap = salesService.selectSalesDateAjax(paramMap);
            if(pMap != null) jobj.put("product", pMap);
        } catch (Exception e) {
            jobj.put("code", 902);
            e.printStackTrace();
        }
        log.debug(jobj);
        return gson.toJson(jobj);
    }

    /* 하나의 판매 불러오기(select idx) */
    @RequestMapping(value = "selectSalesOneAjax", method = RequestMethod.POST, produces = "application/json; charset=utf8")
    @ResponseBody
    public Object selectSalesOneAjax(@RequestParam Map<String, Object> paramMap, HttpSession session){
        JSONObject jobj = new JSONObject();
        Map<String, Object> sMap = (Map<String, Object>)session.getAttribute("shopInfo");
        jobj.put("code", 200);
        Gson gson = new Gson();
        paramMap.put("id", sMap.get("id"));
        log.debug("selectSalesOneAjax : " + paramMap);
        try {
            Map<String, Object> saleMap = salesService.selectSalesOneAjax(paramMap);
            if(saleMap != null) jobj.put("salesOne", saleMap);
        } catch (Exception e) {
            jobj.put("code", 902);
            e.printStackTrace();
        }
        log.debug(jobj);
        return gson.toJson(jobj);
    }

    /* 판매직원 불러오기 */
    @RequestMapping("callEmplList")
    @ResponseBody
    public Object callEmplList(@RequestParam Map<String, Object> paramMap){
        JSONObject jobj = new JSONObject();
        log.debug("callEmplList : " + paramMap);

        try{
            List<Map<String, Object>> emplList = salesService.selectEmployeeListCall(paramMap);

            jobj.put("emplList", emplList);
            jobj.put("code", 200);
        } catch(Exception e){
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 판매 서비스 모달 불러오기 */
    @RequestMapping("serviceModalCall")
    @ResponseBody
    public Object serviceModalCall(HttpSession session){
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();

        try{
            List<Map<String, Object>> cateList = salesService.selectServiceCategoryCall(sMap);
            List<Map<String, Object>> detailList = salesService.selectServiceDetailCall(sMap);

            jobj.put("cateList", cateList);
            jobj.put("detailList", detailList);
            jobj.put("code", 200);
        } catch(Exception e){
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 제품 리스트 가져오기 */
    @RequestMapping("productAllCall")
    @ResponseBody
    public Object productAllCall(HttpSession session){
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();

        try{
            List<Map<String, Object>> pCateList = salesService.salectAllProductCateCall(sMap);
            List<Map<String, Object>> pDetailList = salesService.salectAllProductDetailCall(sMap);

            jobj.put("cateList", pCateList);
            jobj.put("detailList", pDetailList);

            jobj.put("code", 200);
        } catch(Exception e){
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 제품 분류 정렬 */
    @RequestMapping("productCateSortCall")
    @ResponseBody
    public Object productCateSortCall(@RequestParam Map<String, Object> paramMap, HttpSession session){
        log.debug("productCateSortCall : " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();

        try{
            paramMap.put("idx", sMap.get("idx"));
            List<Map<String, Object>> pDetailList = salesService.salectCateSortProductDetailCall(paramMap);

            jobj.put("detailList", pDetailList);

            jobj.put("code", 200);
        } catch(Exception e){
            jobj.put("code", 900);
            e.printStackTrace();
        }

        return jobj;
    }

    /* 제품 검색 */
    @RequestMapping("productSearch")
    @ResponseBody
    public Object productSearch(@RequestParam Map<String, Object> paramMap, HttpSession session){
        log.debug("productSearch: " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();

        try{
            paramMap.put("idx", sMap.get("idx"));
            List<Map<String, Object>> pDetailList = salesService.salectProductSreach(paramMap);

            jobj.put("detailList", pDetailList);

            jobj.put("code", 200);
        } catch(Exception e){
            jobj.put("code", 900);
            e.printStackTrace();
        }

        return jobj;
    }

    /* 정액 / 선불권 불러오기 */
    @RequestMapping("callPrepaymentList")
    @ResponseBody
    public Object callPrepaymentList(@RequestParam Map<String, Object> paramMap, HttpSession session){
        log.debug("productSearch: " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();

        try{
            paramMap.put("idx", sMap.get("idx"));
            paramMap.put("shopId", sMap.get("idx"));
            /* salect 오타 ㅡㅡ 바꾸기 귀찮*/
            List<Map<String, Object>> cateList = salesService.salectAllCatePrepaymentListCall(paramMap);
            List<Map<String, Object>> detailList = salesService.salectAllDetailPrepaymentListCall(paramMap);
            List<Map<String, Object>> emplList = salesService.selectEmployeeListCall(paramMap);

            System.out.println("cate list : " + cateList);
            System.out.println("detail list : " + detailList);
            System.out.println("empl list : " + emplList);

            jobj.put("cateList", cateList);
            jobj.put("detailList", detailList);
            jobj.put("emplList", emplList);

            jobj.put("code", 200);
        } catch(Exception e){
            jobj.put("code", 900);
            e.printStackTrace();
        }

        return jobj;
    }

    /* 티켓 불러오기 */
    @RequestMapping("callTicketList")
    @ResponseBody
    public Object callTicketList(@RequestParam Map<String, Object> paramMap, HttpSession session){
        log.debug("productSearch: " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();

        try{
            paramMap.put("idx", sMap.get("idx"));
            paramMap.put("shopId", sMap.get("idx"));
            List<Map<String, Object>> cateList = salesService.salectAllCateTicketListCall(paramMap);
            List<Map<String, Object>> detailList = salesService.salectAllDetailTicketListCall(paramMap);
            List<Map<String, Object>> emplList = salesService.selectEmployeeListCall(paramMap);

            System.out.println("cate list : " + cateList);
            System.out.println("detail list : " + detailList);

            jobj.put("cateList", cateList);
            jobj.put("detailList", detailList);
            jobj.put("emplList", emplList);

            jobj.put("code", 200);
        } catch(Exception e){
            jobj.put("code", 900);
            e.printStackTrace();
        }

        return jobj;
    }

    /* 고객 보유 선불권 불러오기 */
    @RequestMapping("callClientPrepaidList")
    @ResponseBody
    public Object callClientPrepaidList(@RequestParam Map<String, Object> paramMap, HttpSession session){
        log.debug("callClientPrepaidList: " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        try{
            sMap.put("client_idx", paramMap.get("client_idx"));
            Map<String, Object> pMap = salesService.selectPrepaidOfClientCall(sMap);

            System.out.println("pMap : " + pMap);
            jobj.put("pMap", pMap);
            jobj.put("code", 200);
        }catch(Exception e){
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 고객 보유 티켓 불러오기 */
    @RequestMapping("callClientTicketList")
    @ResponseBody
    public Object callClientTicketList(@RequestParam Map<String, Object> paramMap, HttpSession session){
        log.debug("callClientTicketList: " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();

        try{
            sMap.put("client_idx", paramMap.get("client_idx"));
            Map<String, Object> pMap = salesService.selectTicketOfClientCall(sMap);

            System.out.println("pMap : " + pMap);
            jobj.put("pMap", pMap);
            jobj.put("code", 200);
        }catch(Exception e){
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 선불권 구매 */
    @RequestMapping("prepaidSalesInsert")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object prepaidSalesInsert(@RequestParam Map<String, Object> paramMap, HttpSession session){
        log.debug("prepaidSalesInsert: " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj =new JSONObject();
        Map<String, Object> cMap = new HashMap<>();
        String sale_idx = "NULL";
        try {
            paramMap.put("shop_id", sMap.get("id"));
            paramMap.put("shopIdx", sMap.get("idx"));

            salesService.insertPrepaidSalesData(paramMap);
            sale_idx = paramMap.get("ID").toString();

            cMap = salesService.selectClient(paramMap);

            /* 합치기 */
            if ("true".equals(paramMap.get("prepaid_sum_flag").toString())) {
                List<Map<String, Object>> pList = new Gson().fromJson(cMap.get("prepaid").toString(), List.class);
                for(int i = 0 ; i < pList.size(); i ++){
                    if (paramMap.get("sum_hss_prepaid_idx").toString().equals(pList.get(i).get("idx").toString()) && paramMap.get("sum_hss_prepaid_cost").toString().equals(pList.get(i).get("cost").toString()) && paramMap.get("sum_hss_prepaid_name").toString().equals(pList.get(i).get("name").toString()) && paramMap.get("sum_hss_prepaid_sales_idx").toString().equals(pList.get(i).get("sale_idx").toString())  && paramMap.get("sum_hss_prepaid_validity").toString().equals(pList.get(i).get("validity").toString())) {
                        JSONObject updatePrepaid = new JSONObject();
                        updatePrepaid.put("idx", paramMap.get("services_idx"));
                        updatePrepaid.put("cost", Integer.parseInt(paramMap.get("sum_hss_prepaid_cost").toString()) + Integer.parseInt(paramMap.get("accumulate").toString()) + "");
                        updatePrepaid.put("name", paramMap.get("services_name"));
                        updatePrepaid.put("sale_idx", paramMap.get("ID") + "");
                        updatePrepaid.put("validity", paramMap.get("validity"));

                        pList.set(i, updatePrepaid);
                    }else{
                        JSONObject updatePrepaid = new JSONObject();
                        updatePrepaid.put("idx", pList.get(i).get("idx").toString());
                        updatePrepaid.put("cost", pList.get(i).get("cost").toString());
                        updatePrepaid.put("name", pList.get(i).get("name").toString());
                        updatePrepaid.put("sale_idx", pList.get(i).get("sale_idx").toString());
                        updatePrepaid.put("validity", pList.get(i).get("validity").toString());
                        pList.set(i, updatePrepaid);
                    }
                }
                paramMap.put("pList", pList);
                salesService.updateSumClientPrepaidJson(paramMap);
            }
            /* 안합치기 */
            else {
                salesService.updateClientPrepaidJson(paramMap);
            }

            if(Integer.parseInt(cMap.get("sms_check").toString()) == 1){
                System.out.println("문자 발송 !!! ");
                paramMap.put("sale_idx", sale_idx);
                // 포인트 적립 시 자동문자 발송
                if(Integer.parseInt(paramMap.get("append_point").toString()) > 0) salesService.autoMessageFromAccumulatePoint(paramMap, sMap, messageService);
                // 선불권 적립시 자동문자
                salesService.autoMessageFromAccumulatePreaid(paramMap, sMap, messageService);
            } else {
                System.out.println("문자 미발송 !!! ");
            }
            jobj.put("code", 200);
        }
        catch(Exception e){
            e.printStackTrace();
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 횟수권 구매 */
    @RequestMapping("ticketSalesInsert")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object ticketSalesInsert(@RequestParam Map<String, Object> paramMap, HttpSession session){
        log.debug("param : " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        Map<String, Object> cMap = new HashMap<>();
        JSONObject jobj =new JSONObject();
        String sale_idx = "NULL";

        try {
            paramMap.put("shop_id", sMap.get("id"));
            paramMap.put("shopIdx", sMap.get("idx"));

            cMap = salesService.selectClient(paramMap);
            salesService.insertTicketSalesData(paramMap);
            sale_idx = paramMap.get("ID").toString();

            /* 합치기 */
            if ("true".equals(paramMap.get("prepaid_sum_flag").toString())) {
                System.out.println("param : " + paramMap.get("ID"));
                List<Map<String, Object>> pList = new Gson().fromJson(cMap.get("ticket").toString(), List.class);
                for(int i = 0 ; i < pList.size(); i ++){
                    if (paramMap.get("sum_hss_ticket_idx").toString().equals(pList.get(i).get("idx").toString()) && paramMap.get("sum_hss_ticket_count").toString().equals(pList.get(i).get("count").toString()) && paramMap.get("sum_hss_ticket_name").toString().equals(pList.get(i).get("name").toString()) && paramMap.get("sum_hss_ticket_sales_idx").toString().equals(pList.get(i).get("sale_idx").toString())  && paramMap.get("sum_hss_ticket_validity").toString().equals(pList.get(i).get("validity").toString())) {
                        JSONObject updatePrepaid = new JSONObject();


                        String cost = (Integer.parseInt(pList.get(i).get("count").toString()) * Integer.parseInt(pList.get(i).get("cost").toString())  +  Integer.parseInt(paramMap.get("total_cost").toString()))
                                / (Integer.parseInt(paramMap.get("accumulate").toString()) + Integer.parseInt(pList.get(i).get("count").toString())) + "";

                        updatePrepaid.put("idx", paramMap.get("services_idx"));
                        updatePrepaid.put("count", Integer.parseInt(paramMap.get("sum_hss_ticket_count").toString()) + Integer.parseInt(paramMap.get("accumulate").toString()) + "");
                        updatePrepaid.put("cost", cost);
                        updatePrepaid.put("name", paramMap.get("services_name"));
                        updatePrepaid.put("sale_idx", paramMap.get("ID") + "");
                        updatePrepaid.put("validity", paramMap.get("validity"));

                        pList.set(i, updatePrepaid);
                    }else{
                        JSONObject updatePrepaid = new JSONObject();
                        updatePrepaid.put("idx", pList.get(i).get("idx").toString());
                        updatePrepaid.put("count", pList.get(i).get("count").toString());
                        updatePrepaid.put("cost", pList.get(i).get("cost").toString());
                        updatePrepaid.put("name", pList.get(i).get("name").toString());
                        updatePrepaid.put("sale_idx", pList.get(i).get("sale_idx").toString());
                        updatePrepaid.put("validity", pList.get(i).get("validity").toString());
                        pList.set(i, updatePrepaid);
                    }
                }
                paramMap.put("pList", pList);
                salesService.updateSumClientTicketJson(paramMap);
            }
            /* 안합치기 */
            else {
                System.out.println("param : " + paramMap.get("ID"));
                String cost = Integer.parseInt(paramMap.get("total_cost").toString()) / Integer.parseInt(paramMap.get("accumulate").toString()) + "";
                paramMap.put("controller_ticket_cost", cost);
                salesService.updateClientTicketJson(paramMap);
            }

            if(Integer.parseInt(cMap.get("sms_check").toString()) == 1){
                System.out.println("문자 발송 !!! ");
                paramMap.put("sale_idx", sale_idx);
                // 포인트 적립 시 자동문자 발송
                if(Integer.parseInt(paramMap.get("append_point").toString()) > 0) salesService.autoMessageFromAccumulatePoint(paramMap, sMap, messageService);
                // 적립시 자동문자
                salesService.autoMessageFromAccumulateTicket(paramMap, sMap, messageService);
            } else {
                System.out.println("문자 미발송 !!! ");
            }

            jobj.put("code", 200);
        }
        catch(Exception e){
            e.printStackTrace();
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 시술 카테고리 & 하위 목록 조회 */
    @RequestMapping("serviceSelectCall")
    @ResponseBody
    public Object serviceSelectCall(HttpSession session){
        Map<String, Object> sMap = (Map<String,Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();

        try{
            /* 서비스 */
            List<Map<String, Object>> cateList = salesService.selectServiceCategoryCall(sMap);
            List<Map<String, Object>> detailList = salesService.selectServiceDetailCall(sMap);

            jobj.put("cateList", cateList);
            jobj.put("detailList", detailList);

            jobj.put("code", 200);
        } catch(Exception e){
            jobj.put("code", 900);
        }
        return jobj;
    }

    /* 제품 카테고리 & 하위 목록 조회 */
    @RequestMapping("productSelectCall")
    @ResponseBody
    public Object productSelectCall(HttpSession session){
        Map<String, Object> sMap = (Map<String,Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();

        try{
            /* 제품 */
            List<Map<String, Object>> cateList = salesService.salectAllProductCateCall(sMap);
            List<Map<String, Object>> detailList = salesService.salectAllProductDetailCall(sMap);

            jobj.put("cateList", cateList);
            jobj.put("detailList", detailList);

            jobj.put("code", 200);
        } catch(Exception e){
            jobj.put("code", 900);
        }
        return jobj;
    }

    /* 매장 프로모션 불러오기 */
    @RequestMapping("promotionSelect")
    @ResponseBody
    public Object promotionSelect(@RequestParam Map<String, Object> paramMap, HttpSession session){
        Map<String, Object> sMap = (Map<String,Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        paramMap.put("forDate", commonUtils.setForDate(paramMap));
        paramMap.put("shop_idx", sMap.get("idx"));
        log.debug("promotionSelect : " + paramMap);
        try{
            Map<String, Object> pMap = salesService.promotionSelect(paramMap);
            if(pMap != null) {
                jobj.put("code", 200);
                jobj.put("promotion", pMap);
            }
            else jobj.put("code", 902);
        } catch (Exception e){
            jobj.put("code", 900);
            e.printStackTrace();
        }

        return jobj;
    }

    /* 매장 프로모션 성과 적용 */
    @RequestMapping("updatePromotionAchievement")
    @ResponseBody
    public Object updatePromotionAchievement(@RequestParam Map<String, Object> paramMap, HttpSession session){
        JSONObject jobj = new JSONObject();
        log.debug("updatePromotionAchievement : " + paramMap);
        try{
            salesService.updatePromotionAchievement(paramMap);
            jobj.put("code", 200);
        } catch (Exception e){
            jobj.put("code", 900);
            e.printStackTrace();
        }
        return jobj;
    }

    /* 일반 판매 */
    @RequestMapping("normalSalesInsert")
    @ResponseBody
    public Object normalSalesInsert(HttpSession session, @RequestParam Map<String,Object> paramMap){
        log.debug("normal sales param : " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        paramMap.put("shop_id", sMap.get("id"));
        JSONObject jobj = new JSONObject();
        Map<String, Object> clientInfo = salesService.selectClient(paramMap);


        List<Map<String, Object>> servicePrepaid = new Gson().fromJson(paramMap.get("service_use_prepaid").toString(), List.class);
        List<Map<String, Object>> productPrepaid = new Gson().fromJson(paramMap.get("product_use_prepaid").toString(), List.class);
        for(int i = 0; i < servicePrepaid.size(); i++){
            for(String key : servicePrepaid.get(i).keySet()){
                System.out.println("service prepaid ["+i+" - " + key + " : " + servicePrepaid.get(i).get(key));
            }
        }
        for(int i = 0; i < productPrepaid.size(); i++){
            for(String key : productPrepaid.get(i).keySet()){
                System.out.println("product prepaid ["+i+" - " + key + " : " + productPrepaid.get(i).get(key));
            }
        }

        /* 판매 데이터 삽입 */
        try {
            paramMap.put("shop_id", sMap.get("id"));
            paramMap.put("shop_idx", sMap.get("idx"));
            if ("".equals(paramMap.get("res_idx").toString())) {
                paramMap.put("res_idx", "Null");
            }
            if("".equals(paramMap.get("client_idx").toString())){
                paramMap.put("client_idx", "0");
            }
            System.out.println(paramMap.get("res_idx"));



            if(Integer.parseInt(paramMap.get("service_size").toString()) > 0) {
                salesService.insertServieSales(paramMap);
            }
            if(Integer.parseInt(paramMap.get("product_size").toString()) > 0){
                salesService.insertProductSales(paramMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 900);      // 판매 데이터 삽입 에러
            return jobj;
        }
        try {
            // 고객 정보 업데이트
            if (!"0".equals(paramMap.get("client_idx").toString())) {
                // params
                List<Map<String, Object>> clientUsePrepaidList = new Gson().fromJson(paramMap.get("client_use_prepaid").toString(), List.class);
                List<Map<String, Object>> clientUseTicketList = new Gson().fromJson(paramMap.get("client_use_ticket").toString(), List.class);
                // DB
                List<Map<String, Object>> clientPrepaidList = new Gson().fromJson(clientInfo.get("prepaid").toString(), List.class);
                List<Map<String, Object>> clientTicketList = new Gson().fromJson(clientInfo.get("ticket").toString(), List.class);

                // 고객 차감 선불권
                if (clientUsePrepaidList.size() > 0) {
                    clientPrepaidList = CommonUtils.minusPrepaidTicket(clientUsePrepaidList, clientPrepaidList, "prepaid");
                    paramMap.put("prepaidUpdate", clientPrepaidList);
                    salesService.updateClientUsePrepaid(paramMap);
                }
                // 고객 차감 횟수권
                if (clientUseTicketList.size() > 0) {
                    clientTicketList = CommonUtils.minusPrepaidTicket(clientUseTicketList, clientTicketList, "ticket");
                    paramMap.put("ticketUpdate", clientTicketList);
                    salesService.updateClientUseTicket(paramMap);
                }
                int clientTotalCost = Integer.parseInt(paramMap.get("service_total_cost").toString()) + Integer.parseInt(paramMap.get("product_total_cost").toString());
                int clientTotalMiss = Integer.parseInt(paramMap.get("service_total_miss_cost").toString()) + Integer.parseInt(paramMap.get("product_total_miss_cost").toString());


                paramMap.put("controllerTotalPay", clientTotalCost);
                paramMap.put("controllerTotalMiss", clientTotalMiss);

                int totalPrice = salesService.updateClientCosts1(paramMap);
                paramMap.put("clientPrice", totalPrice);

                CommonUtils.printMap(paramMap);
                salesService.updateClientCosts2(paramMap);

            }
            paramMap.put("res_status", 5);
            salesService.updateReservationStatus(paramMap);


            List<Map<String, Object>> usePrepaidList = new Gson().fromJson(paramMap.get("client_use_prepaid").toString(), List.class);
            List<Map<String, Object>> useTicketList = new Gson().fromJson(paramMap.get("client_use_ticket").toString(), List.class);

            if(!"0".equals(paramMap.get("client_idx").toString())){
                if(Integer.parseInt(clientInfo.get("sms_check").toString()) == 1){
                    System.out.println("문자 발송 !!! ");
                    // 포인트 적립시 자동문자 발송
                    if(Integer.parseInt(paramMap.get("store_point").toString())>0){
                        paramMap.put("shopId", sMap.get("id"));
                        paramMap.put("shopIdx", sMap.get("idx"));

                        System.out.println("포인트 적립 문자 발송");
                        salesService.autoMessageFromAccumulatePoint(paramMap, sMap, messageService);
                    }
                    // 포인트 사용시 자동문자 발송
                    if((Integer.parseInt(paramMap.get("service_total_point").toString()) + Integer.parseInt(paramMap.get("product_total_point").toString())) > 0)
                        salesService.autoMessageFromUsePoint(paramMap, sMap, messageService);
                    // 선불권 사용시 자동문자 발송
                    if(usePrepaidList.size() > 0){
                        salesService.autoMessageFromUsePreaid(paramMap, sMap, messageService, usePrepaidList);
                    }
                    // 티켓 사용시 자동문자 발송
                    if(useTicketList.size() > 0){
                        salesService.autoMessageFromUseTicket(paramMap, sMap, messageService, useTicketList);
                    }

                    List<Map<String, Object>> serviceCateList = new Gson().fromJson(paramMap.get("serviceCateIdxArray").toString(), List.class);
                    if(serviceCateList != null && serviceCateList.size() > 0){
                        // 14 첫방문 고객
                        if(Integer.parseInt(clientInfo.get("visit_count_").toString()) == 0)
                            salesService.autoMessageFromAfterVisitService1(paramMap, sMap, clientInfo, serviceCateList, messageService);
                        // 15 재방문 고객
                        else
                            salesService.autoMessageFromAfterVisitService2(paramMap, sMap, clientInfo, serviceCateList, messageService);
                    }
                } else {
                    System.out.println("문자 미발송 !!! ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 901);      // 고객 정보 업데이트 에러
        }

        if("Null".equals(paramMap.get("res_idx").toString())){
            jobj.put("code", 201);
        } else{
            jobj.put("code", 200);
        }

        return jobj;
    }

    /* 미수금 처리 */
    @RequestMapping("payTheMissCost")
    @ResponseBody
    public Object payTheMissCost(@RequestParam Map<String, Object> paramMap, HttpSession session){
        log.debug("payTheMissCost : " + paramMap);
        Map<String, Object> sMap = (Map<String,Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();

        try{
            paramMap.put("shop_id", sMap.get("id"));
            paramMap.put("shop_idx", sMap.get("idx"));
            salesService.insertPayTheMissCost(paramMap);        // 판매 테이블 미수금 정산
            salesService.updateClientMissCost(paramMap);
            jobj.put("code", 200);
        } catch(Exception e){
            jobj.put("code", 900);
        }
        return jobj;
    }

    /* calendar > 판매 취소 */
    @RequestMapping("cancelSales")
    @ResponseBody
    public Object cancelSales(@RequestParam Map<String, Object> paramMap, HttpSession session){
        log.debug("cancelSales : " + paramMap);
        Map<String, Object> sMap = (Map<String,Object>) session.getAttribute("shopInfo");
        Map<String, Object> clientCancleParam = new HashMap<>();
        JSONObject jobj = new JSONObject();
        try{
            paramMap.put("shop_id", sMap.get("id"));
            paramMap.put("res_status", 1);
            paramMap.put("shop_idx", sMap.get("idx"));
            paramMap.put("forDate", commonUtils.setForDate(paramMap));
            Map<String, Object> pMap = salesService.promotionSelect(paramMap);
            if(pMap != null){ // 해당달 프로모션이 있으면
                pMap.put("shop_id", sMap.get("id"));
                pMap.put("res_idx", paramMap.get("res_idx"));
                // 프로모션 값 적용, 판매 status update 전에 처리
                salesService.updateCancelPromotionAchievement(pMap);
            }

            paramMap.put("shopId", sMap.get("id"));
            List<Map<String, Object>> saleMap = salesService.selectCancelSalesFromResIdx(paramMap);

            System.out.println("취소할 판매 건 수 :  " + saleMap.size());
            CommonUtils.printList(saleMap);

            System.out.println("취소 할 소진 건 정보");
            for(int i = 0 ; i < saleMap.size(); i ++){
                /*for(String key : saleMap.get(i).keySet()){
                    System.out.println("[" + i + " ] : " + key + " : " + saleMap.get(i).get(key));*/


                    List<Map<String, Object>> usePrepaidList = new Gson().fromJson(saleMap.get(i).get("use_prepaid").toString(), List.class);
                    List<Map<String, Object>> useTicketList  = new Gson().fromJson(saleMap.get(i).get("use_ticket").toString(), List.class);

                    int usePoint = Integer.parseInt(saleMap.get(i).get("total_point").toString());
                    int useMoney = Integer.parseInt(saleMap.get(i).get("total_money").toString());
                    int useCard = Integer.parseInt(saleMap.get(i).get("total_card").toString());
                    int useGift = Integer.parseInt(saleMap.get(i).get("total_gift_cost").toString());
                    int useOther = Integer.parseInt(saleMap.get(i).get("total_other_cost").toString());

                    clientCancleParam.put("shop_id", sMap.get("id"));
                    clientCancleParam.put("restorePoint", usePoint);
                    clientCancleParam.put("restoreTotalPay", useMoney + useCard + useGift + useOther);
                    clientCancleParam.put("restorePrepaid", usePrepaidList);
                    clientCancleParam.put("restoreTicket", useTicketList);
                    clientCancleParam.put("shopId", sMap.get("id"));
                    clientCancleParam.put("client_idx", saleMap.get(i).get("client_idx"));
//                }
                salesService.updateClientRestorePrice(clientCancleParam);
            }



            salesService.updateCancelSale(paramMap);        // 판매 status update
            salesService.updateReservationStatus(paramMap); // 예약상태 수
            jobj.put("code", 200);
        } catch(Exception e){
            e.printStackTrace();
            jobj.put("code", 900);
        }
        return jobj;
    }

    /* 환불 판매금액 불러오기 */
    @RequestMapping("amountAtTheTimeOfSale")
    @ResponseBody
    public Object amountAtTheTimeOfSale(@RequestParam Map<String, Object> paramMap, HttpSession session){
        log.debug("amountAtTheTimeOfSale : " + paramMap);
        Map<String, Object> sMap = (Map<String,Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();

        try{
            paramMap.put("shop_id", sMap.get("id"));
            Map<String, Object> rMap = salesService.amountAtTheTimeOfSaleCall(paramMap);

            jobj.put("total_cost", rMap.get("total_cost"));
            jobj.put("code", 200);
        } catch(Exception e){
            jobj.put("code", 900);
        }
        return jobj;
    }

    /* 환불 */
    @RequestMapping("refundSubmit")
    @ResponseBody
    public Object refundSubmit(@RequestParam Map<String, Object> paramMap, HttpSession session){
        log.debug("refundSubmit : " + paramMap);
        CommonUtils.printMap(paramMap);
        Map<String, Object> sMap = (Map<String,Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
//        String removeKey = paramMap.get("client_has_sale_idx").toString();

        try{
            paramMap.put("shop_idx", sMap.get("idx"));
            paramMap.put("shop_id", sMap.get("id"));
            paramMap.put("shopId", sMap.get("id"));

            salesService.refundSubmit(paramMap);                                            // 판매 데이터 insert
            // 선불권
            if("1".equals(paramMap.get("type").toString())) salesService.clientHasMemberShipRemove(paramMap);                               // 환불한 선불권 초기화
            // 티켓
            else salesService.clientHasMemberShipTicketRemove(paramMap);                               // 환불한 횟수권 초기화

            // 총 결제금액 수정
            Map<String, Object> cMap = salesService.selectClientInfoFromIdx(paramMap);      // 고객 선불 잔액 확인
            System.out.println("visit count : " + cMap.get("visit_count"));
            if(Integer.parseInt(cMap.get("visit_count").toString()) > 0) {
                System.out.println("update 1");
                salesService.updateClientPrice(paramMap);
            }
            else{
                System.out.println("update 2");
                salesService.updateClientPrice2(paramMap);
            }
            if("0".equals(cMap.get("client_prepaid_point").toString()) && "0".equals(cMap.get("client_ticket_point").toString())){
                salesService.clientHasMemberShipEnd(paramMap);                              // 고객 비회원 전환
                jobj.put("code", 201);
            } else {
                jobj.put("code", 200);
            }
        } catch(Exception e){
            jobj.put("code", 900);
        }
        return jobj;
    }

    /* [시술 내역 삭제] 고객 - 내역 - 시술 내역 삭제 버튼  */
    @RequestMapping("cleaningSalesRow")
    @ResponseBody
    public Object cleaningSalesRow(@RequestParam Map<String, Object> paramMap, HttpSession session){
        log.debug("param : " + paramMap);
        JSONObject jobj = new JSONObject();
        int restorePoint, restoreTotalPay, storePoint = 0;
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");

        try{
            paramMap.put("shop_id", sMap.get("id"));
            Map<String, Object> saleMap = salesService.selectSalesDataFromTableIdx(paramMap);
            CommonUtils.printMap(saleMap);

            restoreTotalPay = Integer.parseInt(saleMap.get("total_cost").toString());
            restorePoint = Integer.parseInt(saleMap.get("total_point").toString());
            storePoint = Integer.parseInt(saleMap.get("store_point").toString());

            paramMap.put("client_idx", saleMap.get("client_idx").toString());
            paramMap.put("restore_total_pay", restoreTotalPay);
            paramMap.put("restore_point", restorePoint);
            paramMap.put("store_point", storePoint);

            // 사용된 선불권이 있다
            if(saleMap.get("use_prepaid") != null && !"[]".equals(saleMap.get("use_prepaid").toString())){
                List<Map<String, Object>> resotrePrepaidList = new ArrayList<>();
                List<Map<String, Object>> usePrepaidList = new Gson().fromJson(saleMap.get("use_prepaid").toString(), List.class);
                List<Map<String, Object>> havePrepaidList = new Gson().fromJson(saleMap.get("client_prepaid").toString(), List.class);

                for(Map<String, Object> usePrepaidMap : usePrepaidList){
                    String idx = usePrepaidMap.get("idx").toString();
                    int cost = Integer.parseInt(usePrepaidMap.get("cost").toString());
                    String name = usePrepaidMap.get("name").toString();
                    String saleIdx = usePrepaidMap.get("sale_idx").toString();

                    for(Map<String, Object> havePrepaidMap : havePrepaidList){
                        if(saleIdx.equals(havePrepaidMap.get("sale_idx").toString())){
                            JSONObject resotrePrepaidMap = new JSONObject();
//                            Map<String, Object> resotrePrepaidMap = new HashMap<>();
                            int originCost = Integer.parseInt(havePrepaidMap.get("cost").toString());
                            originCost += cost;
                            resotrePrepaidMap.put("idx", havePrepaidMap.get("idx").toString());
                            resotrePrepaidMap.put("cost", Integer.toString(originCost));
                            resotrePrepaidMap.put("name", havePrepaidMap.get("name").toString());
                            resotrePrepaidMap.put("sale_idx", havePrepaidMap.get("sale_idx").toString());
                            resotrePrepaidMap.put("validity", havePrepaidMap.get("validity").toString());

                            resotrePrepaidList.add(resotrePrepaidMap);
                        } else {
                            JSONObject resotrePrepaidMap = new Gson().fromJson(havePrepaidMap.toString(), JSONObject.class);
                            resotrePrepaidList.add(resotrePrepaidMap);
                        }
                    }
                }
                paramMap.put("restore_prepaid", resotrePrepaidList.toString());
            } else {
                paramMap.put("restore_prepaid", "");
            }
            // 사용된 티켓이 있다
            if(saleMap.get("use_ticket") != null && !"[]".equals(saleMap.get("use_ticket").toString())){
                List<Map<String, Object>> resotreTicketList = new ArrayList<>();
                List<Map<String, Object>> useTicketList = new Gson().fromJson(saleMap.get("use_ticket").toString(), List.class);
                List<Map<String, Object>> haveTicketList = new Gson().fromJson(saleMap.get("client_ticket").toString(), List.class);

                for(Map<String, Object> useTicketMap : useTicketList){
                    int count = Integer.parseInt(useTicketMap.get("count").toString());
                    String saleIdx = useTicketMap.get("sale_idx").toString();

                    for(Map<String, Object> haveTicketMap : haveTicketList){
                        if(saleIdx.equals(haveTicketMap.get("sale_idx").toString())){
                            JSONObject resotreTicketMap = new JSONObject();
//                            Map<String, Object> resotreTicketMap = new HashMap<>();
                            int originCount = Integer.parseInt(haveTicketMap.get("count").toString());
                            originCount += count;
                            resotreTicketMap.put("idx", haveTicketMap.get("idx").toString());
                            resotreTicketMap.put("cost", haveTicketMap.get("cost").toString());
                            resotreTicketMap.put("name", haveTicketMap.get("name").toString());
                            resotreTicketMap.put("count", Integer.toString(originCount));
                            resotreTicketMap.put("sale_idx", haveTicketMap.get("sale_idx").toString());
                            resotreTicketMap.put("validity", haveTicketMap.get("validity").toString());

                            resotreTicketList.add(resotreTicketMap);
                        } else {
                            JSONObject resotreTicketMap = new Gson().fromJson(haveTicketMap.toString(), JSONObject.class);
                            resotreTicketList.add(resotreTicketMap);
                        }
                    }
                }
                paramMap.put("restore_ticket", resotreTicketList.toString());
            } else {
                paramMap.put("restore_ticket", "");
            }

            // 고객 선불권, 티켓, 전체 매출, 포인트 복구
            salesService.updateReturnSaleFromClientData(paramMap);
            // 판매 상태 업데이트
            salesService.updateSaleStatusChange(paramMap);

            // 객단가 갱신
            int totalPrice = salesService.updateClientCosts1(paramMap);
            paramMap.put("clientPrice", totalPrice);
            /* 객단가만 갱신*/
            salesService.updateClientPriceVer2(paramMap);

            jobj.put("code", 200);
        } catch(Exception e){
            e.printStackTrace();
            jobj.put("code", 900);
        }
        return jobj;
    }
}
