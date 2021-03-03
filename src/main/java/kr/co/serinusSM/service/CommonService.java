package kr.co.serinusSM.service;

import kr.co.serinusSM.common.CommonUtils;
import kr.co.serinusSM.dao.CommonDAO;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("commonService")
public class CommonService {

    @Resource(name = "commonDAO")
    private CommonDAO commonDAO;

    /* 매장 로그인 체크 */
    public Map<String, Object> loginCheckAjax(Map<String, Object> map) {
        return commonDAO.loginCheckAjax(map);
    }

    public Map<String, Object> autoLoginCheck(Map<String, Object> map) {
        return commonDAO.autoLoginCheck(map);
    }

    /* 원장 아이디 중복 확인 */
    public Map<String, Object> overlapCheckFromManager(Map<String, Object> map) {
        return commonDAO.overlapCheckFromManager(map);
    }

    /* 원장 회원가입 */
    public void insertManager(Map<String, Object> map) {
        commonDAO.insertManager(map);
    }

    /* 매장 아이디 중복 확인 */
    public Map<String, Object> overlapShopCheckFromIdAjax(Map<String, Object> map) {
        return commonDAO.overlapShopCheckFromIdAjax(map);
    }

    public void insertChat(Map<String, Object> map) {
        commonDAO.insertChat(map);
    }

    public List<Map<String, Object>> selectChat(Map<String, Object> map) {
        return commonDAO.selectChat(map);
    }

    public void deleteChat(Map<String, Object> map) {
        commonDAO.deleteChat(map);
    }

    /* 매장 추가 */
    public void insertShop(Map<String, Object> map) {
        /*
         * 1. shop 테이블 insert
         * 2. create OOO_client
         * 3. create OOO_msg_history
         * 4. create OOO_reservation
         * 5,.create OOO_sales
         * 6. create OOO_schedule
         * 7. create 000_call_history
         * 8. default service list insert
         * */

        /* 1 */
        commonDAO.insertShop(map);
        String shopIdx = map.get("ID").toString();
        /* 2 */
        commonDAO.createClient(map);
        /* 3 */
        commonDAO.createMSGHistory(map);
        /* 4 */
        commonDAO.createReservation(map);
        /* 5 */
        commonDAO.createSales(map);
        /* 6 */
        commonDAO.createSchedule(map);
        /* 7 */
        commonDAO.createCallHistory(map);
        map.put("shop_idx", shopIdx);

        /* 8 */
        if ("네일".equals(map.get("item_type").toString())) {
            commonDAO.insertDefaultServiceCategory1(map);
            commonDAO.insertDefaultServiceCategory2(map);
            commonDAO.insertDefaultServiceCategory3(map);

            commonDAO.insertDefaultServiceDetail1_1(map);
            commonDAO.insertDefaultServiceDetail1_2(map);
            commonDAO.insertDefaultServiceDetail1_3(map);
            commonDAO.insertDefaultServiceDetail1_4(map);
            commonDAO.insertDefaultServiceDetail1_5(map);

            commonDAO.insertDefaultServiceDetail2_1(map);
            commonDAO.insertDefaultServiceDetail2_2(map);
            commonDAO.insertDefaultServiceDetail2_3(map);
            commonDAO.insertDefaultServiceDetail2_4(map);
            commonDAO.insertDefaultServiceDetail2_5(map);

            commonDAO.insertDefaultServiceDetail3_1(map);
            commonDAO.insertDefaultServiceDetail3_2(map);
            commonDAO.insertDefaultServiceDetail3_3(map);
            commonDAO.insertDefaultServiceDetail3_4(map);
            commonDAO.insertDefaultServiceDetail3_5(map);
            commonDAO.insertDefaultServiceDetail3_6(map);
            commonDAO.insertDefaultServiceDetail3_7(map);
            commonDAO.insertDefaultServiceDetail3_8(map);
            commonDAO.insertDefaultServiceDetail3_9(map);
        } else if ("반영구, 속눈썹".equals(map.get("item_type").toString())) {
            /* 카테고리 삽입 */
            Map<String, Object> cMap = new HashMap<>();
            JSONObject jobj = new JSONObject();
            jobj.put("color", "#ffdab9");
            cMap.put("cate_category", "눈썹");
            cMap.put("shop_idx", map.get("shop_idx"));
            cMap.put("resource", jobj.toString());
            cMap.put("p_int", 1);
            System.out.println("cmap : " + cMap);
            commonDAO.insertDefaultCateCustom(cMap);
            jobj.put("color", "#96ceec");
            cMap.put("cate_category", "입술");
            cMap.put("shop_idx", map.get("shop_idx"));
            cMap.put("resource", jobj.toString());
            cMap.put("p_int", 2);
            System.out.println("cmap : " + cMap);
            commonDAO.insertDefaultCateCustom(cMap);
            jobj.put("color", "#e6e6fa");
            cMap.put("cate_category", "연장");
            cMap.put("shop_idx", map.get("shop_idx"));
            cMap.put("resource", jobj.toString());
            cMap.put("p_int", 3);
            commonDAO.insertDefaultCateCustom(cMap);
            jobj.put("color", "#b0e0e6");
            cMap.put("cate_category", "기타");
            cMap.put("shop_idx", map.get("shop_idx"));
            cMap.put("resource", jobj.toString());
            cMap.put("p_int", 4);
            commonDAO.insertDefaultCateCustom(cMap);


            /* 디테일 삽입 */
            Map<String, Object> dMap = new HashMap<>();
            /* 눈썹 디테일 */
            jobj.put("time", "01:00");
            dMap.put("detail_name", "엠보 눈썹");
            dMap.put("detail_category", "눈썹");
            dMap.put("cost", 150000);
            dMap.put("shop_idx", map.get("shop_idx"));
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 1);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "자연 눈썹");
            dMap.put("cost", 200000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 2);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "디지털 눈썹");
            dMap.put("cost", 100000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 3);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "콤보 눈썹");
            dMap.put("cost", 180000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 4);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "남자 눈썹");
            dMap.put("cost", 200000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 5);
            commonDAO.insertDefaultDetailCustom(dMap);
            /* 입술 디테일 */
            jobj.put("time", "01:30");
            dMap.put("detail_name", "입술 전체");
            dMap.put("detail_category", "입술");
            dMap.put("cost", 250000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 1);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:30");
            dMap.put("detail_name", "틴트 전체");
            dMap.put("cost", 250000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 2);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "입술 윤곽");
            dMap.put("cost", 150000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 3);
            commonDAO.insertDefaultDetailCustom(dMap);
            /* 연장 디테일 */
            jobj.put("time", "01:00");
            dMap.put("detail_name", "실크모");
            dMap.put("detail_category", "연장");
            dMap.put("cost", 40000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 1);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "밍크모");
            dMap.put("cost", 40000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 2);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "벨벳모");
            dMap.put("cost", 50000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 3);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "인모");
            dMap.put("cost", 70000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 4);
            commonDAO.insertDefaultDetailCustom(dMap);
        } else if ("메이크업".equals(map.get("item_type").toString())) {
            /* 카테고리 삽입 */
            Map<String, Object> cMap = new HashMap<>();
            JSONObject jobj = new JSONObject();
            jobj.put("color", "#ffdab9");
            cMap.put("cate_category", "기본");
            cMap.put("shop_idx", map.get("shop_idx"));
            cMap.put("resource", jobj.toString());
            cMap.put("p_int", 1);
            commonDAO.insertDefaultCateCustom(cMap);
            jobj.put("color", "#96ceec");
            cMap.put("cate_category", "결혼식");
            cMap.put("resource", jobj.toString());
            cMap.put("p_int", 2);
            commonDAO.insertDefaultCateCustom(cMap);
            jobj.put("color", "#e6e6fa");
            cMap.put("cate_category", "혼주");
            cMap.put("resource", jobj.toString());
            cMap.put("p_int", 3);
            commonDAO.insertDefaultCateCustom(cMap);
            jobj.put("color", "#ffb6c1");
            cMap.put("cate_category", "승무원");
            cMap.put("resource", jobj.toString());
            cMap.put("p_int", 4);
            commonDAO.insertDefaultCateCustom(cMap);
            jobj.put("color", "#b0e0e6");
            cMap.put("cate_category", "기타");
            cMap.put("resource", jobj.toString());
            cMap.put("p_int", 5);
            commonDAO.insertDefaultCateCustom(cMap);

            /* 디테일 삽입 */
            Map<String, Object> dMap = new HashMap<>();
            /* 기본 디테일 */
            jobj.put("time", "01:30");
            dMap.put("detail_name", "기본 여자메이크업+헤어");
            dMap.put("detail_category", "기본");
            dMap.put("cost", 120000);
            dMap.put("shop_idx", map.get("shop_idx"));
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 1);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "00:30");
            dMap.put("detail_name", "여자 헤어");
            dMap.put("cost", 70000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 2);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "00:30");
            dMap.put("detail_name", "여자 메이크업");
            dMap.put("cost", 80000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 3);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "00:30");
            dMap.put("detail_name", "남자 메이크업+헤어");
            dMap.put("cost", 50000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 4);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "00:30");
            dMap.put("detail_name", "남자 헤어");
            dMap.put("cost", 30000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 5);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "00:30");
            dMap.put("detail_name", "유아");
            dMap.put("cost", 20000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 6);
            commonDAO.insertDefaultDetailCustom(dMap);
            /* 결혼 디테일 */
            jobj.put("time", "01:30");
            dMap.put("detail_name", "신랑,신부 본식 메이크업");
            dMap.put("detail_category", "결혼식");
            dMap.put("cost", 350000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 1);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:30");
            dMap.put("detail_name", "신랑,신부 촬영 메이크업");
            dMap.put("cost", 250000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 2);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:30");
            dMap.put("detail_name", "리허설 메이크업");
            dMap.put("cost", 150000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 3);
            commonDAO.insertDefaultDetailCustom(dMap);
            /* 혼주 디테일 */
            jobj.put("time", "01:30");
            dMap.put("detail_name", "어머님");
            dMap.put("detail_category", "혼주");
            dMap.put("cost", 120000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 1);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "00:30");
            dMap.put("detail_name", "아버님");
            dMap.put("cost", 30000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 2);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "02:00");
            dMap.put("detail_name", "어머님+아버님");
            dMap.put("cost", 150000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 3);
            commonDAO.insertDefaultDetailCustom(dMap);
            /* 승무원 디테일 */
            jobj.put("time", "01:30");
            dMap.put("detail_name", "헤어+메이크업");
            dMap.put("detail_category", "승무원");
            dMap.put("cost", 80000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 1);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "헤어");
            dMap.put("cost", 40000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 2);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "메이크업");
            dMap.put("cost", 50000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 3);
            commonDAO.insertDefaultDetailCustom(dMap);
        } else if ("왁싱".equals(map.get("item_type").toString())) {
            Map<String, Object> cMap = new HashMap<>();
            JSONObject jobj = new JSONObject();
            jobj.put("color", "#ffdab9");
            cMap.put("cate_category", "왁싱");
            cMap.put("shop_idx", map.get("shop_idx"));
            cMap.put("resource", jobj.toString());
            cMap.put("p_int", 1);
            commonDAO.insertDefaultCateCustom(cMap);

            Map<String, Object> dMap = new HashMap<>();
            /* 왁싱 디테일 */
            jobj.put("time", "01:00");
            dMap.put("detail_name", "발");
            dMap.put("detail_category", "왁싱");
            dMap.put("cost", 30000);
            dMap.put("shop_idx", map.get("shop_idx"));
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 1);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "다리");
            dMap.put("cost", 40000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 2);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "겨드랑이");
            dMap.put("cost", 30000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 3);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "뒷목");
            dMap.put("cost", 20000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 4);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "눈썹");
            dMap.put("cost", 20000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 5);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "인중");
            dMap.put("cost", 20000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 6);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "비키니");
            dMap.put("cost", 50000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 7);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "브라질리언");
            dMap.put("cost", 50000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 8);
            commonDAO.insertDefaultDetailCustom(dMap);
        } else if ("마사지".equals(map.get("item_type").toString())) {
            Map<String, Object> cMap = new HashMap<>();
            JSONObject jobj = new JSONObject();
            jobj.put("color", "#ffdab9");
            cMap.put("cate_category", "건식");
            cMap.put("shop_idx", map.get("shop_idx"));
            cMap.put("resource", jobj.toString());
            cMap.put("p_int", 1);
            commonDAO.insertDefaultCateCustom(cMap);
            jobj.put("color", "#96ceec");
            cMap.put("cate_category", "오일");
            cMap.put("shop_idx", map.get("shop_idx"));
            cMap.put("resource", jobj.toString());
            cMap.put("p_int", 2);
            commonDAO.insertDefaultCateCustom(cMap);

            Map<String, Object> dMap = new HashMap<>();
            /* 건식 디테일 */
            jobj.put("time", "00:50");
            dMap.put("detail_name", "발");
            dMap.put("detail_category", "건식");
            dMap.put("cost", 40000);
            dMap.put("shop_idx", map.get("shop_idx"));
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 1);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "00:50");
            dMap.put("detail_name", "등");
            dMap.put("cost", 40000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 2);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "하체");
            dMap.put("cost", 60000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 3);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "상체");
            dMap.put("cost", 60000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 4);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:30");
            dMap.put("detail_name", "전신");
            dMap.put("cost", 100000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 6);
            commonDAO.insertDefaultDetailCustom(dMap);
            /* 오일 디테일 */
            jobj.put("time", "00:50");
            dMap.put("detail_name", "발");
            dMap.put("detail_category", "오일");
            dMap.put("cost", 50000);
            dMap.put("shop_idx", map.get("shop_idx"));
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 1);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "00:50");
            dMap.put("detail_name", "등");
            dMap.put("cost", 50000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 2);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "하체");
            dMap.put("cost", 70000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 3);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "상체");
            dMap.put("cost", 70000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 4);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:30");
            dMap.put("detail_name", "전신");
            dMap.put("cost", 110000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 6);
            commonDAO.insertDefaultDetailCustom(dMap);
        } else if ("스킨".equals(map.get("item_type").toString())) {
            Map<String, Object> cMap = new HashMap<>();
            JSONObject jobj = new JSONObject();
            jobj.put("color", "#ffdab9");
            cMap.put("cate_category", "얼굴");
            cMap.put("shop_idx", map.get("shop_idx"));
            cMap.put("resource", jobj.toString());
            cMap.put("p_int", 1);
            commonDAO.insertDefaultCateCustom(cMap);
            jobj.put("color", "#96ceec");
            cMap.put("cate_category", "바디");
            cMap.put("shop_idx", map.get("shop_idx"));
            cMap.put("resource", jobj.toString());
            cMap.put("p_int", 2);
            commonDAO.insertDefaultCateCustom(cMap);
            jobj.put("color", "#e6e6fa");
            cMap.put("cate_category", "세트");
            cMap.put("shop_idx", map.get("shop_idx"));
            cMap.put("resource", jobj.toString());
            cMap.put("p_int", 3);
            commonDAO.insertDefaultCateCustom(cMap);

            Map<String, Object> dMap = new HashMap<>();
            /* 건식 디테일 */
            jobj.put("time", "00:50");
            dMap.put("detail_name", "기본");
            dMap.put("detail_category", "얼굴");
            dMap.put("cost", 50000);
            dMap.put("shop_idx", map.get("shop_idx"));
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 1);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "00:50");
            dMap.put("detail_name", "보습");
            dMap.put("cost", 40000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 2);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "00:50");
            dMap.put("detail_name", "미백");
            dMap.put("cost", 50000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 3);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "00:50");
            dMap.put("detail_name", "트러블");
            dMap.put("cost", 60000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 4);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "00:50");
            dMap.put("detail_name", "재생");
            dMap.put("cost", 40000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 5);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "00:50");
            dMap.put("detail_name", "모공");
            dMap.put("cost", 40000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 6);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:10");
            dMap.put("detail_name", "얼굴축소");
            dMap.put("cost", 70000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 7);
            commonDAO.insertDefaultDetailCustom(dMap);
            /* 바디 */
            jobj.put("time", "00:50");
            dMap.put("detail_name", "복부");
            dMap.put("detail_category", "바디");
            dMap.put("cost", 40000);
            dMap.put("shop_idx", map.get("shop_idx"));
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 1);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "00:50");
            dMap.put("detail_name", "등");
            dMap.put("cost", 40000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 2);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:50");
            dMap.put("detail_name", "상반신");
            dMap.put("cost", 60000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 3);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:00");
            dMap.put("detail_name", "하반신");
            dMap.put("cost", 60000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 4);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:30");
            dMap.put("detail_name", "전신");
            dMap.put("cost", 100000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 5);
            commonDAO.insertDefaultDetailCustom(dMap);
            /* 전신 디테일 */
            jobj.put("time", "01:30");
            dMap.put("detail_name", "등+얼굴");
            dMap.put("detail_category", "세트");
            dMap.put("cost", 60000);
            dMap.put("shop_idx", map.get("shop_idx"));
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 1);
            commonDAO.insertDefaultDetailCustom(dMap);
            jobj.put("time", "01:30");
            dMap.put("detail_name", "복부+얼굴");
            dMap.put("cost", 60000);
            dMap.put("resource", jobj.toString());
            dMap.put("p_int", 2);
            commonDAO.insertDefaultDetailCustom(dMap);
        }
    }

    /* 사업자번호 중복 확인 */
    public List<Map<String, Object>> selectComNumberOverCheckFromManager(Map<String, Object> map) {
        return commonDAO.selectComNumberOverCheckFromManager(map);
    }

    /* 매장 오픈 */
    public void updateShopOpen(Map<String, Object> map) {
        commonDAO.updateShopOpen(map);
    }

    /* 매장 마감 */
    public void updateShopClose(Map<String, Object> map) {
        commonDAO.updateShopClose(map);
    }

    /* 매장 분단위 재설정*/
    public void updateShopDefaultMinute(Map<String, Object> map) {
        commonDAO.updateShopDefaultMinute(map);
    }

    /* 모든 스케쥴 미사용 업데이트 */
    public void updateEmployeeAllSchedule(Map<String, Object> map) {
        commonDAO.updateEmployeeAllSchedule(map);
    }

    /* 매장 운영시간 업데이트 */
    public void updateOperationgTime(Map<String, Object> map) {
        commonDAO.updateOperationgTime(map);
    }

    /* 사업자 등록번호 업데이트 */
    public void updateComNum(Map<String, Object> map) {
        commonDAO.updateComNum(map);
    }

    /* 문자 포인트 업데이트 */
    public void updateMsgPoint(Map<String, Object> map) {
        commonDAO.updateMsgPoint(map);
    }

    /* 이메일 업데이트 */
    public void shopEmailUpdate(Map<String, Object> map) {
        commonDAO.shopEmailUpdate(map);
    }

    /* 사업자번호 중복허용 체크 */
    public List<Map<String, Object>> selectOverlapComnumCheck(Map<String, Object> map) {
        return commonDAO.selectOverlapComnumCheck(map);
    }

    /* 발신자 등록으로 인한 매장 데이터 업데이트 */
    public void updateShopDataFromSender(Map<String, Object> map) {
        commonDAO.updateShopDataFromSender(map);
    }

    /* 고객정보 검색 */
    public Map<String, Object> selectClientInfo(Map<String, Object> map) {
        return commonDAO.selectClientInfo(map);
    }

    /* 전화수신 기록 */
    public void insertCallHistory(Map<String, Object> map) {
        commonDAO.insertCallHistory(map);
    }

    /* 전화수신 TotalCount */
    public Map<String, Object> selectCallHistoryCount(Map<String, Object> map) {
        return commonDAO.selectCallHistoryCount(map);
    }

    /* 전화수신 내역 Paging*/
    public List<Map<String, Object>> selectCallHistoryList(Map<String, Object> map) {
        return commonDAO.selectCallHistoryList(map);
    }

    /* 전화수신내역 고객 수정 */
    public void updateClientState(Map<String, Object> map) {
        commonDAO.updateClientState(map);
    }

    /* 아메일 인증 발송 */
//    public Object regist(Map<String, Object> sMap, Map<String, Object> pMap, JavaMailSender mailSender) {
//        JSONObject jobj = new JSONObject();
//        CommonUtils utils = new CommonUtils();
//        System.out.println("서비스레지스");
//        String key = utils.randomUUID();
////		String key = new TempKey().getKey(50,false);  // 인증키 생성
//
//        //메일 전송
//        try {
//            MailHandler sendMail = new MailHandler(mailSender);
//            sendMail.setSubject("Beauting 서비스 이메일 인증]");
//            sendMail.setText(
//                    new StringBuffer().append("<h1>메일인증</h1>").
//                            append("<p>인증번호 : </p>").
//                            append("<h3>").
//                            append(key).
//                            append("</h3>").toString()
//					/*new StringBuffer().append("<h1>메일인증</h1>").
//							append("<a href='http://localhost:8080/emailConfirm.do?userEmail=").
//							append(pMap.get("email")).
//							append("&shopId=").append(sMap.get("id")).
//							append("&memberAuthKey=").append(key).
//							append("' target='_blank'>이메일 인증 확인</a>").toString()*/
//            );
//            sendMail.setFrom("mob190501@gmail.com", "서비스센터 ");
//            sendMail.setTo(pMap.get("email").toString());
//            sendMail.send();
//
//            jobj.put("code", 200);
//            jobj.put("key", key);
//        } catch (NullPointerException ne) {
//            jobj.put("code", 500);
//        } catch (Exception e) {
//            e.printStackTrace();
//            jobj.put("code", 900);
//        }
//        return jobj;
//    }

    public void insertPayer_ID(Map<String, Object> map) {
        commonDAO.insertPayer_ID(map);
    }

    public void deletePayer_ID(Map<String, Object> map) {
        commonDAO.deletePayer_ID(map);
    }

    public void insertPayment(Map<String, Object> map) {
        commonDAO.insertPayment(map);
    }

    public Map<String, Object> selectShopInfo(Map<String, Object> map) {
        return commonDAO.selectShopInfo(map);
    }

    public List<Map<String, Object>> selectReservation(Map<String, Object> map) {
        return commonDAO.selectReservation(map);
    }
}

