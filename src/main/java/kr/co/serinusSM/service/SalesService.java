package kr.co.serinusSM.service;

import com.popbill.api.MessageService;
import kr.co.serinusSM.common.CommonUtils;
import kr.co.serinusSM.dao.SalesDAO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("salesService")
public class SalesService {
    Logger log = Logger.getLogger(this.getClass());

    @Resource(name = "salesDAO")
    private SalesDAO salesDAO;

    /* 예약 타입 불러오기 */
    public int selectResType(Map<String, Object> map) {
        return salesDAO.selectResType(map);
    }

    /* 예약 결제 정보 불러오기 (회원) */
    public Map<String, Object> selectCallReservationPayInfo1(Map<String, Object> map) { return salesDAO.selectCallReservationPayInfo1(map); }
    /* 예약 결제 정보 불러오기 (미등록 회원) */
    public Map<String, Object> selectCallReservationPayInfo2(Map<String, Object> map) { return salesDAO.selectCallReservationPayInfo2(map); }
    /* 미예약 판매 페이지 고객정보 불러오기 */
    public Map<String, Object> selectClientInfoFromIdx(Map<String, Object> map) { return salesDAO.selectClientInfoFromIdx(map); }
    /* 판매 직원 불러오기 */
    public List<Map<String, Object>> selectEmployeeListCall(Map<String, Object> map) { return salesDAO.selectEmployeeListCall(map); }
    /* 판매 직원 불러오기 Ver 2*/
    public List<Map<String, Object>> selectSaleEmployeeListCall(Map<String, Object> map) { return salesDAO.selectSaleEmployeeListCall(map); }
    /* 서비스 카테고리 불러오기 */
    public List<Map<String, Object>> selectServiceCategoryCall(Map<String, Object> map) { return salesDAO.selectServiceCategoryCall(map); }
    public Map<String, Object> selectServiceCategoryIdxCall(Map<String, Object> map) { return salesDAO.selectServiceCategoryIdxCall(map); }
    /* 서비스 디테일 불러오기 */
    public List<Map<String, Object>> selectServiceDetailCall(Map<String, Object> map) { return salesDAO.selectServiceDetailCall(map); }
    /* 제품 모두 불러오기 */
    public List<Map<String, Object>> salectAllProductCateCall(Map<String, Object> map) { return salesDAO.salectAllProductCateCall(map); }
    public List<Map<String, Object>> salectAllProductDetailCall(Map<String, Object> map) { return salesDAO.salectAllProductDetailCall(map); }
    /* 분류 세부 제품 가져오기 */
    public List<Map<String, Object>> salectCateSortProductDetailCall(Map<String, Object> map) { return salesDAO.salectCateSortProductDetailCall(map); }
    /* 제품 검색 */
    public List<Map<String, Object>> salectProductSreach(Map<String, Object> map) { return salesDAO.salectProductSreach(map); }
    /* 정액 설불권 모두 불러오기 */
    public List<Map<String, Object>> salectAllCatePrepaymentListCall(Map<String, Object> map) { return salesDAO.salectAllCatePrepaymentListCall(map); }
    public List<Map<String, Object>> salectAllDetailPrepaymentListCall(Map<String, Object> map) { return salesDAO.salectAllDetailPrepaymentListCall(map); }
    /* 티켓 불러오기 */
    public List<Map<String, Object>> salectAllCateTicketListCall(Map<String, Object> map) { return salesDAO.salectAllCateTicketListCall(map); }
    public List<Map<String, Object>> salectAllDetailTicketListCall(Map<String, Object> map) { return salesDAO.salectAllDetailTicketListCall(map); }
    /* 판매내역 고객 검색 */
    public Map<String, Object> selectClient(Map<String, Object> map) { return salesDAO.selectClient(map); }
    /* 고객 보유 선불권 불러오기 */
    public Map<String, Object> selectPrepaidOfClientCall(Map<String, Object> map) { return salesDAO.selectPrepaidOfClientCall(map); }
    /* 고객 보유 티켓 불러오기 */
    public Map<String, Object> selectTicketOfClientCall(Map<String, Object> map) { return salesDAO.selectTicketOfClientCall(map); }
    /* 선불권 판매 */
    public void insertPrepaidSalesData(Map<String, Object> map) { salesDAO.insertPrepaidSalesData(map); }
    /* 고객 선불권 sum update */
    public void updateSumClientPrepaidJson(Map<String, Object> map) { salesDAO.updateSumClientPrepaidJson(map); }
    /* 고객 선불권 update */
    public void updateClientPrepaidJson(Map<String, Object> map) { salesDAO.updateClientPrepaidJson(map); }
    /* 횟수권 */
    public void insertTicketSalesData(Map<String, Object> map) { salesDAO.insertTicketSalesData(map); }
    /* 고객 횟수권 sum update */
    public void updateSumClientTicketJson(Map<String, Object> map) { salesDAO.updateSumClientTicketJson(map); }
    /* 고객 횟수권 update */
    public void updateClientTicketJson(Map<String, Object> map) { salesDAO.updateClientTicketJson(map); }
    /* searchClient 페이징 카운트 */
    public Map<String, Object> searchClientPagingCount(Map<String, Object> map) { return salesDAO.searchClientPagingCount(map); }
    /* searchClient 페이징 컨텐츠 */
    public List<Map<String, Object>> searchClientPagingContent(Map<String, Object> map) { return salesDAO.searchClientPagingContent(map); }
    /* 고객의 예약 내역 모두 불러오기 */
    public Map<String, Object> selectAllReservationHistory(Map<String, Object> map) { return salesDAO.selectAllReservationHistory(map); }
    /* 고객의 예약 내역 페이징 불러오기 */
    public List<Map<String, Object>> selectReservationHistory(Map<String, Object> map) { return salesDAO.selectReservationHistory(map); }
    /* 고객 서비스 타입별 총 카운트 */
    public Map<String, Object> selectCountServicesType(Map<String, Object> map) { return salesDAO.selectCountServicesType(map); }
    /* 고객 서비스 타입별 내역 페이징 */
    public List<Map<String, Object>> selectServicesTypeHistory(Map<String, Object> map) { return salesDAO.selectServicesTypeHistory(map); }
    /* 고객 서비스 타입별 총 카운트 (안씀) */
    public Map<String, Object> selectAllServicesHistory(Map<String, Object> map) { return salesDAO.selectAllServicesHistory(map); }
    /* 고객 서비스 타입별 내역 페이징 (안씀) */
    public List<Map<String, Object>> selectServicesHistory(Map<String, Object> map) { return salesDAO.selectServicesHistory(map); }
    /* 하나의 판매내역 검색 */
    public Map<String, Object> selectSalesDateAjax(Map<String, Object> map) { return salesDAO.selectSalesDateAjax(map); }
    /* 한번의 판매내역 검색 */
    public Map<String, Object> selectSalesOneAjax(Map<String, Object> map) { return salesDAO.selectSalesOneAjax(map); }
    /* 일반 판매 - 서비스 */
    public void insertServieSales(Map<String, Object> map) { salesDAO.insertServieSales(map); }
    /* 일반 판매 - 제품 */
    public void insertProductSales(Map<String, Object> map) { salesDAO.insertProductSales(map); }
    /* 고객 선불권 차감 */
    public void updateClientUsePrepaid(Map<String, Object> map) { salesDAO.updateClientUsePrepaid(map); }
    /* 고객 횟수권 차감 */
    public void updateClientUseTicket(Map<String, Object> map) { salesDAO.updateClientUseTicket(map); }
    /* 고객 총 결제금액 미수금 업데이트 */
    public int updateClientCosts1(Map<String, Object> map) {
        int totalVist = salesDAO.selecttotalVist(map);
        int totalPay = salesDAO.selecttotalPay(map);
        return totalPay / totalVist;
    }
    public void updateClientCosts2(Map<String, Object> map) { salesDAO.updateClientCosts(map); }
    /* 예약 상태 수정 */
    public void updateReservationStatus(Map<String, Object> map) { salesDAO.updateReservationStatus(map); }
    /* 미수금 정산 */
    public void insertPayTheMissCost(Map<String, Object> map) { salesDAO.insertPayTheMissCost(map); }
    /* 고객 미수금 수정 */
    public void updateClientMissCost(Map<String, Object> map) { salesDAO.updateClientMissCost(map); }
    /* 판매 취소로 인한 회원 정보 복구 */
    public void updateClientRestorePrice(Map<String, Object> map) { salesDAO.updateClientRestorePrice(map); }
    /* 예약판 판매 취소 */
    public void updateCancelSale(Map<String, Object> map) { salesDAO.updateCancelSale(map); }
    /* 판매 금액 불러오기 */
    public Map<String, Object> amountAtTheTimeOfSaleCall(Map<String, Object> map) { return salesDAO.amountAtTheTimeOfSaleCall(map); }
    /* 환불 */
    public void refundSubmit(Map<String, Object> map) { salesDAO.refundSubmit(map); }
    /* 회원권 삭제 + 비회원 전환*/
    public void clientHasMemberShipEnd(Map<String, Object> map) { salesDAO.clientHasMemberShipEnd(map); }
    /* 회원권 삭제 */
    public void clientHasMemberShipRemove(Map<String, Object> map) { salesDAO.clientHasMemberShipRemove(map); }
    /* 티켓 삭제 */
    public void clientHasMemberShipTicketRemove(Map<String, Object> map) { salesDAO.clientHasMemberShipTicketRemove(map); }
    /* 총 결제금액 수정 */
    public void updateClientPrice(Map<String, Object> map) { salesDAO.updateClientPrice(map); }
    public void updateClientPrice2(Map<String, Object> map) { salesDAO.updateClientPrice2(map); }
    /* 프로모션 가져오기 */
    public Map<String, Object> promotionSelect(Map<String, Object> map) { return salesDAO.promotionSelect(map); }
    /* 프로모션 성과 적용하기 */
    public void updatePromotionAchievement(Map<String, Object> map) { salesDAO.updatePromotionAchievement(map); }
    /* 판매취소 적용 */
    public void updateCancelPromotionAchievement(Map<String, Object> map) { salesDAO.updateCancelPromotionAchievement(map); }
    /* 판매취소를 위한 판매 레코드 호출 */
    public List<Map<String, Object>> selectCancelSalesFromResIdx(Map<String, Object> map) { return salesDAO.selectCancelSalesFromResIdx(map); }
    /* 판매 데이터 불러오기 [key : sales_table idx] */
    public Map<String, Object> selectSalesDataFromTableIdx(Map<String, Object> map) { return salesDAO.selectSalesDataFromTableIdx(map); }
    /* 판매 취소로 인한 판매 데이터 복구 */
    public void updateReturnSaleFromClientData(Map<String, Object> map) { salesDAO.updateReturnSaleFromClientData(map); }
    // 판매 상태 업데이트
    public void updateSaleStatusChange(Map<String, Object> map) { salesDAO.updateSaleStatusChange(map); }
    /* 객단가만 업데이트 */
    public void updateClientPriceVer2(Map<String, Object> map) { salesDAO.updateClientPriceVer2(map); }
    /* 6. 포인트 적립 자동문자 */
    public void autoMessageFromAccumulatePoint(Map<String, Object> map, Map<String, Object> sMap, MessageService messageService) {
        String receiptNum = "";
        List<Integer> typeList = new ArrayList<>();
        typeList.add(6);
        map.put("typeList", typeList);
        Map<String, Object> settingMap = salesDAO.selectReservationAutoMsg(map);
        Map<String, Object> historyInsertMap = new HashMap<>();

        System.out.println("\n\n\n       client map ");
        CommonUtils utils = new CommonUtils();

        if (settingMap != null) {
            map.put("shop_id", sMap.get("id"));
            Map<String, Object> clientMap = salesDAO.selectClient(map);
            Date date = CommonUtils.stringConvertDateMinusMinute(Integer.parseInt(settingMap.get("send_time").toString()));
            utils.printMap(clientMap);
            if (date != null) {

                System.out.println("포인트 적립 map : "  + map);
                utils.printMap(map);
                try {
                    /* sms */
                    if ("sms".equals(settingMap.get("method").toString())){
                        System.out.println(utils.autoMsgReplace6(settingMap.get("content").toString(), clientMap.get("name").toString(), clientMap.get("point").toString()));
                        System.out.println("포인트 적립 문자 발송 : " + sMap.get("com_num").toString() + sMap.get("tel").toString() + clientMap.get("phone").toString() + clientMap.get("name").toString() + utils.autoMsgReplace6(settingMap.get("content").toString(), clientMap.get("name").toString(), clientMap.get("point").toString()) + date + sMap.get("id").toString());

                        if(CommonUtils.getByteSizeToComplex(utils.autoMsgReplace6(settingMap.get("content").toString(), clientMap.get("name").toString(), clientMap.get("point").toString())) <= 90){
                            historyInsertMap.put("method", 0);
                            receiptNum = messageService.sendSMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientMap.get("phone").toString(), clientMap.get("name").toString(), utils.autoMsgReplace6(settingMap.get("content").toString(), clientMap.get("name").toString(), clientMap.get("point").toString()), date, sMap.get("id").toString());
                        } else {
                            historyInsertMap.put("method", 1);
                            receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientMap.get("phone").toString(), clientMap.get("name").toString(), sMap.get("com_name").toString() + "네일 샵 [포인트 적립]", utils.autoMsgReplace6(settingMap.get("content").toString(), clientMap.get("name").toString(), clientMap.get("point").toString()), date, sMap.get("id").toString());

                        }
                    }
                    /* lms */
                    else if ("lms".equals(settingMap.get("method").toString())){
                        System.out.println(utils.autoMsgReplace6(settingMap.get("content").toString(), clientMap.get("name").toString(), clientMap.get("point").toString()));
                        System.out.println("포인트 적립 문자 발송 : " + sMap.get("com_num").toString() + sMap.get("tel").toString() + clientMap.get("phone").toString() + clientMap.get("name").toString() + "포인트 적립 문자" + utils.autoMsgReplace6(settingMap.get("content").toString(), clientMap.get("name").toString(), clientMap.get("point").toString()) + date + sMap.get("id").toString());
                        historyInsertMap.put("method", 1);
                        receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientMap.get("phone").toString(), clientMap.get("name").toString(), sMap.get("com_name").toString() + "네일 샵 [포인트 적립]", utils.autoMsgReplace6(settingMap.get("content").toString(), clientMap.get("name").toString(), clientMap.get("point").toString()), date, sMap.get("id").toString());
                    }

                    historyInsertMap.put("send_count", 1);
                    historyInsertMap.put("receipt_num", receiptNum);
                    historyInsertMap.put("send_type", 6);
                    historyInsertMap.put("client_idx", clientMap.get("idx"));
                    historyInsertMap.put("receiveDate", date);
                    historyInsertMap.put("content", settingMap.get("content").toString());
                    historyInsertMap.put("shopId", sMap.get("id"));


                    salesDAO.insertAutoMsgHistory(historyInsertMap);

                    System.out.println("\n\n포인트 적립 자동문자 - receiptNum : " + receiptNum + "\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* 7. 포인트 사용 자동문자 */
    public void autoMessageFromUsePoint(Map<String, Object> map, Map<String, Object> sMap, MessageService messageService) {
        String receiptNum = "";
        List<Integer> typeList = new ArrayList<>();
        typeList.add(7);
        map.put("typeList", typeList);
        map.put("shopIdx", sMap.get("idx"));
        Map<String, Object> settingMap = salesDAO.selectReservationAutoMsg(map);
        Map<String, Object> historyInsertMap = new HashMap<>();
        CommonUtils utils = new CommonUtils();

        if (settingMap != null) {
            map.put("shop_id", sMap.get("id"));
            Map<String, Object> clientMap = salesDAO.selectClient(map);
            Date date = CommonUtils.stringConvertDateMinusMinute(Integer.parseInt(settingMap.get("send_time").toString()));

            if (date != null) {
                try {
                    /* sms */
                    if ("sms".equals(settingMap.get("method").toString()))
                    {
                        System.out.println(utils.autoMsgReplace6(utils.autoMsgReplace6(settingMap.get("content").toString(), clientMap.get("name").toString(), clientMap.get("point").toString()), clientMap.get("name").toString(), clientMap.get("point").toString()));
                        if(CommonUtils.getByteSizeToComplex(utils.autoMsgReplace6(settingMap.get("content").toString(), clientMap.get("name").toString(), clientMap.get("point").toString())) <= 90){
                            receiptNum = messageService.sendSMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientMap.get("phone").toString(), clientMap.get("name").toString(), utils.autoMsgReplace6(settingMap.get("content").toString(), clientMap.get("name").toString(), clientMap.get("point").toString()), date, sMap.get("id").toString());
                            historyInsertMap.put("method", 0);
                        } else {
                            receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientMap.get("phone").toString(), clientMap.get("name").toString(), sMap.get("com_name").toString() + "네일 샵 [포인트 사용]", utils.autoMsgReplace6(settingMap.get("content").toString(), clientMap.get("name").toString(), clientMap.get("point").toString()), date, sMap.get("id").toString());
                            historyInsertMap.put("method", 1);
                        }
                    }
                    /* lms */
                    else if ("lms".equals(settingMap.get("method").toString()))
                    {
                        System.out.println(utils.autoMsgReplace6(utils.autoMsgReplace6(settingMap.get("content").toString(), clientMap.get("name").toString(), clientMap.get("point").toString()), clientMap.get("name").toString(), clientMap.get("point").toString()));
                        receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientMap.get("phone").toString(), clientMap.get("name").toString(), sMap.get("com_name").toString() + "네일 샵 [포인트 사용]", utils.autoMsgReplace6(settingMap.get("content").toString(), clientMap.get("name").toString(), clientMap.get("point").toString()), date, sMap.get("id").toString());
                        historyInsertMap.put("method", 1);
                    }
                    System.out.println("\n\n포인트 사용 자동문자 - receiptNum : " + receiptNum + "\n");
                    historyInsertMap.put("send_count", 1);
                    historyInsertMap.put("receipt_num", receiptNum);
                    historyInsertMap.put("send_type", 7);
                    historyInsertMap.put("client_idx", clientMap.get("idx"));
                    historyInsertMap.put("receiveDate", date);
                    historyInsertMap.put("content", settingMap.get("content").toString());
                    historyInsertMap.put("shopId", sMap.get("id"));


                    salesDAO.insertAutoMsgHistory(historyInsertMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* 8. 선불권 결제 자동문자 */
    public void autoMessageFromAccumulatePreaid(Map<String, Object> map, Map<String, Object> sMap, MessageService messageService) {
        String receiptNum = "";
        List<Integer> typeList = new ArrayList<>();
        typeList.add(8);
        map.put("typeList", typeList);
        Map<String, Object> settingMap = salesDAO.selectReservationAutoMsg(map);
        CommonUtils utils = new CommonUtils();

        if (settingMap != null) {
            map.put("shop_id", sMap.get("id"));
            Map<String, Object> clientMap = salesDAO.selectClient(map);

            System.out.println("param");
            utils.printMap(map);
            System.out.println("sMap");
            utils.printMap(sMap);
            System.out.println("clientInfo");
            utils.printMap(clientMap);


            Date date = CommonUtils.stringConvertDateMinusMinute(Integer.parseInt(settingMap.get("send_time").toString()));

            try {
                /* sms */
                if ("sms".equals(settingMap.get("method").toString())){
                    System.out.println("선불권 적립 : " + sMap.get("com_num").toString()+ sMap.get("tel").toString()+ clientMap.get("phone").toString()+ clientMap.get("name").toString()+ utils.autoMsgReplace8(settingMap.get("content").toString(), map.get("accumulate").toString(), clientMap)+ date+ sMap.get("id").toString());

                    if(CommonUtils.getByteSizeToComplex(utils.autoMsgReplace8(settingMap.get("content").toString(), map.get("accumulate").toString(), clientMap)) <= 90){
                        receiptNum = messageService.sendSMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientMap.get("phone").toString(), clientMap.get("name").toString(), utils.autoMsgReplace8(settingMap.get("content").toString(), map.get("accumulate").toString(), clientMap), date, sMap.get("id").toString());
                        map.put("method", 0);
                    } else {
                        receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientMap.get("phone").toString(), clientMap.get("name").toString(), sMap.get("com_name").toString() + "네일 샵 [선불권 구매]", utils.autoMsgReplace8(settingMap.get("content").toString(), map.get("accumulate").toString(), clientMap), date, sMap.get("id").toString());
                        map.put("method", 1);
                    }
                }
                /* lms */
                else if ("lms".equals(settingMap.get("method").toString())){
                    System.out.println("선불권 적립 : " + sMap.get("com_num").toString()+ sMap.get("tel").toString()+ clientMap.get("phone").toString()+ clientMap.get("name").toString()+ sMap.get("com_name").toString() + "네일 샵 [선불권 구매]" + utils.autoMsgReplace8(settingMap.get("content").toString(), map.get("accumulate").toString(), clientMap)+ date+ sMap.get("id").toString());
                    receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientMap.get("phone").toString(), clientMap.get("name").toString(), sMap.get("com_name").toString() + "네일 샵 [선불권 구매]", utils.autoMsgReplace8(settingMap.get("content").toString(), map.get("accumulate").toString(), clientMap), date, sMap.get("id").toString());
                    map.put("method", 1);
                }
                System.out.println("\n\n선불권 적립 자동문자 - receiptNum : " + receiptNum + "\n");

                map.put("receipt_num", receiptNum);
                map.put("send_type", 8);
                map.put("receiveDate", date);
                map.put("send_count", 1);
                map.put("content", settingMap.get("content").toString());
                map.put("client_idx", clientMap.get("idx"));
                salesDAO.insertPrepaidMsgHistory(map);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* 9. 선불권 사용 자동문자 */
    public void autoMessageFromUsePreaid(Map<String, Object> map, Map<String, Object> sMap, MessageService messageService, List<Map<String, Object>> usePrepaidList) {
        String receiptNum = "";
        List<Integer> typeList = new ArrayList<>();
        CommonUtils utils = new CommonUtils();
        typeList.add(9);
        map.put("shopIdx", sMap.get("idx"));
        map.put("typeList", typeList);
        Map<String, Object> settingMap = salesDAO.selectReservationAutoMsg(map);
        Map<String, Object> historyInsertMap = new HashMap<>();

        if (settingMap != null) {
            map.put("shop_id", sMap.get("id"));
            Map<String, Object> clientMap = salesDAO.selectClient(map);
            Date date = CommonUtils.stringConvertDateMinusMinute(Integer.parseInt(settingMap.get("send_time").toString()));

            System.out.println("map");
            utils.printMap(map);
            System.out.println("smap");
            utils.printMap(sMap);
            System.out.println("client");
            utils.printMap(clientMap);

            try {
                for(int i = 0 ; i < usePrepaidList.size(); i ++){
                    /* sms */
                    if ("sms".equals(settingMap.get("method").toString())) {
                        System.out.println(i + "차 선불권 사용 : " + sMap.get("com_num").toString() + sMap.get("tel").toString() + clientMap.get("phone").toString() + clientMap.get("name").toString() + utils.autoMsgReplace9(settingMap.get("content").toString(), usePrepaidList.get(i), clientMap.get("name").toString()) + date + sMap.get("id").toString());
                        if(CommonUtils.getByteSizeToComplex(utils.autoMsgReplace9(settingMap.get("content").toString(), usePrepaidList.get(i), clientMap.get("name").toString())) <= 90){
                            receiptNum = messageService.sendSMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientMap.get("phone").toString(), clientMap.get("name").toString(), utils.autoMsgReplace9(settingMap.get("content").toString(), usePrepaidList.get(i), clientMap.get("name").toString()), date, sMap.get("id").toString());
                            historyInsertMap.put("method", 0);
                        } else {
                            receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientMap.get("phone").toString(), clientMap.get("name").toString(), sMap.get("com_name").toString() + "네일 샵 [선불권 사용]", utils.autoMsgReplace9(settingMap.get("content").toString(), usePrepaidList.get(i), clientMap.get("name").toString()), date, sMap.get("id").toString());
                            historyInsertMap.put("method", 1);
                        }
                    }
                    /* lms */
                    else if ("lms".equals(settingMap.get("method").toString())) {
                        System.out.println(i + "차 선불권 사용 : " + sMap.get("com_num").toString()+ sMap.get("tel").toString()+ clientMap.get("phone").toString()+ clientMap.get("name").toString()+ sMap.get("com_name").toString() + "네일 샵 [선불권 사용]" + utils.autoMsgReplace9(settingMap.get("content").toString(), usePrepaidList.get(i), clientMap.get("name").toString())+ date+ sMap.get("id").toString());
                        receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientMap.get("phone").toString(), clientMap.get("name").toString(), sMap.get("com_name").toString() + "네일 샵 [선불권 사용]", utils.autoMsgReplace9(settingMap.get("content").toString(), usePrepaidList.get(i), clientMap.get("name").toString()), date, sMap.get("id").toString());
                        historyInsertMap.put("method", 1);
                    }
                    System.out.println("\n\nreceiptNum : " + receiptNum + "\n");
                    historyInsertMap.put("send_count", 1);
                    historyInsertMap.put("receipt_num", receiptNum);
                    historyInsertMap.put("send_type", 9);
                    historyInsertMap.put("client_idx", clientMap.get("idx"));
                    historyInsertMap.put("receiveDate", date);
                    historyInsertMap.put("content", settingMap.get("content").toString());
                    historyInsertMap.put("shopId", sMap.get("id"));

                    salesDAO.insertAutoMsgHistory(historyInsertMap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* 11. 티켓 결제자동문자 */
    public void autoMessageFromAccumulateTicket(Map<String, Object> map, Map<String, Object> sMap, MessageService messageService) {
        String receiptNum = "";
        List<Integer> typeList = new ArrayList<>();
        typeList.add(11);
        map.put("typeList", typeList);
        Map<String, Object> settingMap = salesDAO.selectReservationAutoMsg(map);
        CommonUtils utils = new CommonUtils();

        if (settingMap != null) {
            map.put("shop_id", sMap.get("id"));
            Map<String, Object> clientMap = salesDAO.selectClient(map);

            System.out.println("map");
            utils.printMap(map);
            System.out.println("smap");
            utils.printMap(sMap);
            System.out.println("client map");
            utils.printMap(clientMap);


            Date date = CommonUtils.stringConvertDateMinusMinute(Integer.parseInt(settingMap.get("send_time").toString()));

            try {
                /* sms */
                if ("sms".equals(settingMap.get("method").toString())) {
                    System.out.println("티켓 결제 : " + sMap.get("com_num").toString() + sMap.get("tel").toString() + clientMap.get("phone").toString() + clientMap.get("name").toString() + utils.autoMsgReplace11(settingMap.get("content").toString(), map.get("services_name").toString(), map.get("accumulate").toString(), clientMap.get("name").toString()) + date + sMap.get("id").toString());
                    if(CommonUtils.getByteSizeToComplex(utils.autoMsgReplace11(settingMap.get("content").toString(), map.get("services_name").toString(), map.get("accumulate").toString(), clientMap.get("name").toString())) <= 90){
                        receiptNum = messageService.sendSMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientMap.get("phone").toString(), clientMap.get("name").toString(), utils.autoMsgReplace11(settingMap.get("content").toString(), map.get("services_name").toString(), map.get("accumulate").toString(), clientMap.get("name").toString()), date, sMap.get("id").toString());
                        map.put("method", 0);
                    } else {
                        receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientMap.get("phone").toString(), clientMap.get("name").toString(), sMap.get("com_name").toString() + "네일 샵 [티켓 적립]", utils.autoMsgReplace11(settingMap.get("content").toString(), map.get("services_name").toString(), map.get("accumulate").toString(), clientMap.get("name").toString()), date, sMap.get("id").toString());
                        map.put("method", 1);
                    }

                }
                /* lms */
                else if ("lms".equals(settingMap.get("method").toString())){
                    System.out.println("티켓 결제 : " + sMap.get("com_num").toString() + sMap.get("tel").toString() + clientMap.get("phone").toString() + clientMap.get("name").toString() + sMap.get("com_name").toString() + "네일 샵 [티켓 적립]" + utils.autoMsgReplace11(settingMap.get("content").toString(), map.get("services_name").toString(), map.get("accumulate").toString(), clientMap.get("name").toString()) + date + sMap.get("id").toString());
                    receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientMap.get("phone").toString(), clientMap.get("name").toString(), sMap.get("com_name").toString() + "네일 샵 [티켓 적립]", utils.autoMsgReplace11(settingMap.get("content").toString(), map.get("services_name").toString(), map.get("accumulate").toString(), clientMap.get("name").toString()), date, sMap.get("id").toString());
                    map.put("method", 1);
                }
                System.out.println("\n\n티켓 결제 자동문자 receiptNum : " + receiptNum + "\n");

                map.put("receipt_num", receiptNum);
                map.put("send_type", 11);
                map.put("receiveDate", date);
                map.put("send_count", 1);
                map.put("content", settingMap.get("content").toString());
                map.put("client_idx", clientMap.get("idx"));
                salesDAO.insertPrepaidMsgHistory(map);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* 12. 티켓 사용 자동문자 */
    public void autoMessageFromUseTicket(Map<String, Object> map, Map<String, Object> sMap, MessageService messageService, List<Map<String, Object>> useTicketList) {
        String receiptNum = "";
        List<Integer> typeList = new ArrayList<>();
        typeList.add(12);
        map.put("typeList", typeList);
        map.put("shopIdx", sMap.get("idx"));
        Map<String, Object> settingMap = salesDAO.selectReservationAutoMsg(map);
        Map<String, Object> historyInsertMap = new HashMap<>();
        CommonUtils utils = new CommonUtils();

        if (settingMap != null) {
            map.put("shop_id", sMap.get("id"));
            Map<String, Object> clientMap = salesDAO.selectClient(map);
            Date date = CommonUtils.stringConvertDateMinusMinute(Integer.parseInt(settingMap.get("send_time").toString()));


            System.out.println("map");
            utils.printMap(map);
            System.out.println("smap");
            utils.printMap(sMap);
            System.out.println("client map");
            utils.printMap(clientMap);

            try {
                for(int i = 0 ; i < useTicketList.size(); i ++){
                    /* sms */
                    if ("sms".equals(settingMap.get("method").toString())) {
                        System.out.println(i + "차 테켓 사용 : " + sMap.get("com_num").toString() + sMap.get("tel").toString() + clientMap.get("phone").toString() + clientMap.get("name").toString() + utils.autoMsgReplace12(settingMap.get("content").toString(), useTicketList.get(i), clientMap.get("name").toString())+ date + sMap.get("id").toString());

                        if(CommonUtils.getByteSizeToComplex(utils.autoMsgReplace12(settingMap.get("content").toString(), useTicketList.get(i), clientMap.get("name").toString())) <= 90){
                            receiptNum = messageService.sendSMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientMap.get("phone").toString(), clientMap.get("name").toString(), utils.autoMsgReplace12(settingMap.get("content").toString(), useTicketList.get(i), clientMap.get("name").toString()), date, sMap.get("id").toString());
                            historyInsertMap.put("method", 0);
                        } else {
                            receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientMap.get("phone").toString(), clientMap.get("name").toString(), sMap.get("com_name").toString() + "네일 샵 [티켓 사용]", utils.autoMsgReplace12(settingMap.get("content").toString(), useTicketList.get(i), clientMap.get("name").toString()), date, sMap.get("id").toString());
                            historyInsertMap.put("method", 1);
                        }
                    }
                    /* lms */
                    else if ("lms".equals(settingMap.get("method").toString())) {
                        System.out.println(i + "차 티켓 사용 : " + sMap.get("com_num").toString() + sMap.get("tel").toString() + clientMap.get("phone").toString() + clientMap.get("name").toString() + sMap.get("com_name").toString() + "네일 샵 [티켓 사용]" + utils.autoMsgReplace12(settingMap.get("content").toString(), useTicketList.get(i), clientMap.get("name").toString())+ date + sMap.get("id").toString());
                        receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientMap.get("phone").toString(), clientMap.get("name").toString(), sMap.get("com_name").toString() + "네일 샵 [티켓 사용]", utils.autoMsgReplace12(settingMap.get("content").toString(), useTicketList.get(i), clientMap.get("name").toString()), date, sMap.get("id").toString());
                        historyInsertMap.put("method", 1);
                    }
                    System.out.println("\n\nreceiptNum : " + receiptNum + "\n");
                    historyInsertMap.put("send_count", 1);
                    historyInsertMap.put("receipt_num", receiptNum);
                    historyInsertMap.put("send_type", 12);
                    historyInsertMap.put("client_idx", clientMap.get("idx"));
                    historyInsertMap.put("receiveDate", date);
                    historyInsertMap.put("content", settingMap.get("content").toString());
                    historyInsertMap.put("shopId", sMap.get("id"));

                    salesDAO.insertAutoMsgHistory(historyInsertMap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* 14. 고객 방문 후 관리 (첫 방문) */
    public void autoMessageFromAfterVisitService1(Map<String, Object> map, Map<String, Object> sMap, Map<String, Object> clientMap, List<Map<String, Object>> serviceCateList, MessageService messageService) {
        String receiptNum = "";
        List<String> typeList = new ArrayList<>();
        Map<String, Object> historyInsertMap = new HashMap<>();
        for(int i = 0; i < serviceCateList.size(); i ++){
            String type = "15-" + serviceCateList.get(i).get("cate_idx").toString() + "%";
            typeList.add(type);
        }
        map.put("typeList", typeList);
        map.put("shopIdx", sMap.get("idx"));
        List<Map<String, Object>> settingList = salesDAO.selectReservationAutoMsgAsync(map);

        CommonUtils utils = new CommonUtils();


        try{
            for(int i = 0; i < settingList.size(); i++){
                Date date = null;
                String typeIndex = settingList.get(i).get("type").toString();
                typeIndex = typeIndex.substring(typeIndex.length()-1, typeIndex.length());
                date = utils.stringConvertDateMinusDay14(Integer.parseInt(settingList.get(i).get("send_time").toString()), Integer.parseInt(typeIndex));

                /* test */
                SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");

                /* sms */
                if ("sms".equals(settingList.get(i).get("method").toString())) {
                    System.out.println(i + "차 최초 방문 관리 사용 : " + sMap.get("com_num").toString() + sMap.get("tel").toString() + clientMap.get("phone").toString() + clientMap.get("name").toString() + utils.autoMsgReplace14(settingList.get(i).get("content").toString(), clientMap.get("name").toString()) + format1.format(date) + sMap.get("id").toString());
                    if(CommonUtils.getByteSizeToComplex(utils.autoMsgReplace14(settingList.get(i).get("content").toString(), clientMap.get("name").toString())) <= 90){
                        receiptNum = messageService.sendSMS(sMap.get("com_num").toString() , sMap.get("tel").toString() , clientMap.get("phone").toString() , clientMap.get("name").toString() , utils.autoMsgReplace14(settingList.get(i).get("content").toString(), clientMap.get("name").toString()) , date , sMap.get("id").toString());
                        historyInsertMap.put("method", 0);
                    } else {
                        receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), "", sMap.get("tel").toString() , clientMap.get("phone").toString() , clientMap.get("name").toString() , utils.autoMsgReplace14(settingList.get(i).get("content").toString(), clientMap.get("name").toString()) , date , sMap.get("id").toString());
                        historyInsertMap.put("method", 1);
                    }
                }
                /* lms */
                else if ("lms".equals(settingList.get(i).get("method").toString())) {
                    System.out.println(i + "차 최초 방문 관리 사용 : " + sMap.get("com_num").toString() + sMap.get("tel").toString() + clientMap.get("phone").toString() + clientMap.get("name").toString() + sMap.get("com_name").toString() + "네일 샵 [고객님 방문 요청]" + utils.autoMsgReplace14(settingList.get(i).get("content").toString(), clientMap.get("name").toString()) + format1.format(date) + sMap.get("id").toString());
                    receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), "", sMap.get("tel").toString() , clientMap.get("phone").toString() , clientMap.get("name").toString() , utils.autoMsgReplace14(settingList.get(i).get("content").toString(), clientMap.get("name").toString()) , date , sMap.get("id").toString());
                    historyInsertMap.put("method", 1);
                }
                System.out.println("\n\nreceiptNum : " + receiptNum + "\n");
                historyInsertMap.put("send_count", 1);
                historyInsertMap.put("receipt_num", receiptNum);
                historyInsertMap.put("send_type", 14);
                historyInsertMap.put("client_idx", clientMap.get("idx"));
                historyInsertMap.put("receiveDate", date);
                historyInsertMap.put("content", settingList.get(i).get("content").toString());
                historyInsertMap.put("shopId", sMap.get("id"));

                salesDAO.insertAutoMsgHistory(historyInsertMap);
            }
        } catch(Exception e){
            e.printStackTrace();
        }

    }
    /* 15. 고객 방문 후 관리 (재 방문) */
    public void autoMessageFromAfterVisitService2(Map<String, Object> map, Map<String, Object> sMap, Map<String, Object> clientMap, List<Map<String, Object>> serviceCateList, MessageService messageService) {
        String receiptNum = "";
        List<String> typeList = new ArrayList<>();
        Map<String, Object> historyInsertMap = new HashMap<>();
        for(int i = 0; i < serviceCateList.size(); i ++){
            String type = "15-" + serviceCateList.get(i).get("cate_idx").toString() + "%";
            typeList.add(type);
        }
        map.put("typeList", typeList);
        map.put("shopIdx", sMap.get("idx"));
        List<Map<String, Object>> settingList = salesDAO.selectReservationAutoMsgAsync(map);

        CommonUtils utils = new CommonUtils();
        System.out.println("settingList");
        utils.printList(settingList);

        try{
            for(int i = 0; i < settingList.size(); i++){
                Date date = null;
                String typeIndex = settingList.get(i).get("type").toString();
                typeIndex = typeIndex.substring(typeIndex.length()-1, typeIndex.length());
                System.out.println("type index : " + typeIndex);
                date = utils.stringConvertDateMinusDay14(Integer.parseInt(settingList.get(i).get("send_time").toString()), Integer.parseInt(typeIndex));

                /* test */
                SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");

                /* sms */
                if ("sms".equals(settingList.get(i).get("method").toString())) {
                    System.out.println(i + "차 재방문 관리 사용 : " + sMap.get("com_num").toString() + sMap.get("tel").toString() + clientMap.get("phone").toString() + clientMap.get("name").toString() + utils.autoMsgReplace14(settingList.get(i).get("content").toString(), clientMap.get("name").toString()) + format1.format(date) + sMap.get("id").toString());
                    if(CommonUtils.getByteSizeToComplex(utils.autoMsgReplace14(settingList.get(i).get("content").toString(), clientMap.get("name").toString())) <= 90){
                        receiptNum = messageService.sendSMS(sMap.get("com_num").toString() , sMap.get("tel").toString() , clientMap.get("phone").toString() , clientMap.get("name").toString() , utils.autoMsgReplace14(settingList.get(i).get("content").toString(), clientMap.get("name").toString()) , date , sMap.get("id").toString());
                        historyInsertMap.put("method", 0);
                    } else {
                        receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientMap.get("phone").toString(), clientMap.get("name").toString(), sMap.get("com_name").toString() + "네일 샵 [고객님 방문 요청]", utils.autoMsgReplace14(settingList.get(i).get("content").toString(), clientMap.get("name").toString()), date, sMap.get("id").toString());
                        historyInsertMap.put("method", 0);
                    }
                }
                /* lms */
                else if ("lms".equals(settingList.get(i).get("method").toString())) {
                    System.out.println(i + "차 재방문 관리 사용 : " + sMap.get("com_num").toString() + sMap.get("tel").toString() + clientMap.get("phone").toString() + clientMap.get("name").toString() + sMap.get("com_name").toString() + "네일 샵 [고객님 방문 요청]" + utils.autoMsgReplace14(settingList.get(i).get("content").toString(), clientMap.get("name").toString()) + format1.format(date) + sMap.get("id").toString());
                    receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientMap.get("phone").toString(), clientMap.get("name").toString(), sMap.get("com_name").toString() + "네일 샵 [고객님 방문 요청]", utils.autoMsgReplace14(settingList.get(i).get("content").toString(), clientMap.get("name").toString()), date, sMap.get("id").toString());
                    historyInsertMap.put("method", 0);
                }
                System.out.println("\n\nreceiptNum : " + receiptNum + "\n");
                historyInsertMap.put("send_count", 1);
                historyInsertMap.put("receipt_num", receiptNum);
                historyInsertMap.put("send_type", 15);
                historyInsertMap.put("client_idx", clientMap.get("idx"));
                historyInsertMap.put("receiveDate", date);
                historyInsertMap.put("content", settingList.get(i).get("content").toString());
                historyInsertMap.put("shopId", sMap.get("id"));

                salesDAO.insertAutoMsgHistory(historyInsertMap);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}