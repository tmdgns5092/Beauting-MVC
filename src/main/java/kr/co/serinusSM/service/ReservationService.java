package kr.co.serinusSM.service;

import com.popbill.api.MessageService;
import kr.co.serinusSM.common.CommonUtils;
import kr.co.serinusSM.dao.ReservationDAO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("reservationService")
public class ReservationService {
    Logger log = Logger.getLogger(this.getClass());

    @Resource(name="reservationDAO")
    private ReservationDAO reservationDAO;

    /* 모든 직원 정보 가져오기 */
    public Map<String, Object> selectEmployeeAllMaxPage(Map<String, Object> map) { return reservationDAO.selectEmployeeAllMaxPage(map);}
    public List<Map<String, Object>> selectEmployeeAll(Map<String, Object> map) { return reservationDAO.selectEmployeeAll(map); }
    /* 예약정보 가져오기 */
    public List<Map<String, Object>> selectToDayReservation(Map<String, Object> map) { return reservationDAO.selectToDayReservation(map); }
    /* 날짜 예약정보 가져오기 */
    public List<Map<String, Object>> selectDatePickReservation(Map<String, Object> map) { return reservationDAO.selectDatePickReservation(map); }
    /* 예약 업데이트 */
    public void updateReservation(Map<String, Object> map) {
        CommonUtils.printMap(map);
        reservationDAO.updateReservation(map);
    }
    /* 예약 이동 체크 */
    public Map<String, Object> selectReservationMoveCheck(Map<String, Object> map) { return reservationDAO.selectReservationMoveCheck(map); }
    /* 예약 마감시간 업데이트 */
    public void updateFormReservationEndTime(Map<String, Object> map) { reservationDAO.updateFormReservationEndTime(map); }
    /* 예약 등록 고객 검색 */
    public List<Map<String, Object>> selectSortingAndPagingClients(Map<String, Object> map) { return reservationDAO.selectSortingAndPagingClients(map); }
    /* 예약 등록 고객 가져오기 */
    public Map<String, Object> selectReservationClientList_Count(Map<String, Object> map) { return reservationDAO.selectReservationClientList_Count(map); }
    public List<Map<String, Object>> selectReservationClientList(Map<String, Object> map) { return reservationDAO.selectReservationClientList(map); }
    /* 서비스 카테고리 가져오기 */
    public List<Map<String, Object>> selectServiceCategory(Map<String, Object> map) { return reservationDAO.selectServiceCategory(map); }
    /* 서비스 디테일 가져오기 */
    public List<Map<String, Object>> serviceDetailCall(Map<String, Object> map) { return reservationDAO.serviceDetailCall(map); }
    /* 예약 직원 스케쥴 확인 */
    public Map<String, Object> selectEmployeScheduleCheck(Map<String, Object> map) { return reservationDAO.selectEmployeScheduleCheck(map); }
    /* 예약 중복 확인 */
    public Map<String, Object> selectReservationOverlapCheck(Map<String, Object> map) { return reservationDAO.selectReservationOverlapCheck(map); }
    /* 직원 스케줄 확인 */
    public Map<String, Object> selectEmployeeScheduleCheck(Map<String, Object> map) { return reservationDAO.selectEmployeeScheduleCheck(map); }
    /* 예약 중복 확인 */
    public Map<String, Object> selectReservationOverlapCheck_ver2(Map<String, Object> map) { return reservationDAO.selectReservationOverlapCheck_ver2(map); }
    /* 예약 등록 */
    public void addReservation(Map<String, Object> map) { reservationDAO.addReservation(map); }
    /* 예약정보 불러오기 */
    public Map<String, Object> callReservationDataFromIdx(Map<String, Object> map) { return reservationDAO.callReservationDataFromIdx(map); }
    /* 예약 수정 (세부) */
    public void updateReservationDetail(Map<String, Object> map) { reservationDAO.updateReservationDetail(map); }
    /* 예약 불이행 */
    public void updateReservationCancel1(Map<String, Object> map) { reservationDAO.updateReservationCancel1(map); }
    public void updateReservationCancel2(Map<String, Object> map) { reservationDAO.updateReservationCancel2(map); }
    /* 예약 삭제 전 고객 정보 가져오기 */
    public Map<String, Object> selectClientInfo(Map<String, Object> map) { return reservationDAO.selectClientInfo(map); }
    /* 예약 취소로 인한 문자 취소 */
    public List<Map<String, Object>> selectReceiptNum(Map<String, Object> map) { return reservationDAO.selectReceiptNum(map); }
    /* 예약 삭제 */
    public void reservationRemove(Map<String, Object> map) { reservationDAO.reservationRemove(map); }
    /* 예약 불이행 취소 */
    public void reservationStatusUpdateDefault(Map<String, Object> map) { reservationDAO.reservationStatusUpdateDefault(map); }
    /* 불이행 횟수 차감 */
    public void updateClientNoshowCountMinus(Map<String, Object> map) { reservationDAO.updateClientNoshowCountMinus(map); }
    /* 선택한 날 판매 검색 */
    public List<Map<String, Object>> selectSalesForDate(Map<String, Object> map) { return reservationDAO.selectSalesForDate(map); }
    /* 휴일인지 검사  */
    public Map<String, Object> selectScheduleHoliday(Map<String, Object> map) { return reservationDAO.selectScheduleHoliday(map); }
    /* 선택한날 목표금액 검색 */
    public Map<String, Object> selectEmployeeGoalData(Map<String, Object> map) { return reservationDAO.selectEmployeeGoalData(map); }
    /* 고객 idx 가져오기 */
    public Map<String, Object> selectClientIdx(Map<String, Object> map) { return reservationDAO.selectClientIdx(map); }
    /* 프로모션 등록 */
    public void insertPromotion(Map<String,Object> map) { reservationDAO.insertPromotion(map); }
    /* 프로모션 수정 */
    public void updatePromotion(Map<String,Object> map) { reservationDAO.updatePromotion(map); }
    /* 진행중인 프로모션 검색 */
    public Map<String, Object> selectOnePromotion(Map<String,Object> map) { return reservationDAO.selectOnePromotion(map); }
    /* 지난 프로모션 검색 */
    public List<Map<String, Object>> selectListPromotion(Map<String,Object> map) { return reservationDAO.selectListPromotion(map); }
    /* 자동 문자 설정 가져오기 */
    public List<Map<String, Object>> selectMessageAuto(Map<String,Object> map) { return reservationDAO.selectMessageAuto(map); }
    /* 이미 등록된 예약 문자 가져오기 */
    public List<Map<String, Object>> beforSendHistory(Map<String,Object> map) { return reservationDAO.beforSendHistory(map); }
    /* res idx로 예약 가져오기 */
    public Map<String, Object> selectReservationFromIdx(Map<String,Object> map) { return reservationDAO.selectReservationFromIdx(map); }
    /* 예약 발송 취소 DB UPDATE */
    public void updateMsgHistroyFromReservationMsg(Map<String,Object> map) { reservationDAO.updateMsgHistroyFromReservationMsg(map); }

    /*  예약 자동문자 확인 및 발송 */
    public void autoMessageCheckAndSend(Map<String, Object> map, Map<String, Object> sMap, MessageService messageService) throws Exception{

        CommonUtils utils = new CommonUtils();

        System.out.println("서비스 단 : ");
        utils.printMap(map);
        List<Map<String, Object>> autoSettingList = reservationDAO.selectReservationAutoMsg(map);
        String receiptNum = "";
        int sendPoint = 0;

        if (autoSettingList != null) {
            for (int i = 0; i < autoSettingList.size(); i++) {
                Map<String, Object> historyInsertMap = new HashMap<>();
                String clientPhone = "";
                String clientName = "";
                try{
                    if(!"null".equals(map.get("clientIdx").toString())){
                        clientName = map.get("client_name").toString();
                        clientPhone = map.get("client_phone").toString();
                    } else {
                        clientName = map.get("un_name").toString();
                        clientPhone = map.get("un_phone").toString();
                    }
                } catch(NullPointerException e){
                    clientName = map.get("un_name").toString();
                    clientPhone = map.get("un_phone").toString();
                }
                clientPhone = clientPhone.replaceAll("-", "");
                Date date = null;
                boolean msgSendFlag = false;

                /* type 1 전날 알림 */
                if ("1".equals(autoSettingList.get(i).get("type").toString())) {
                    date = CommonUtils.stringConvertDateMinusDay(map.get("res_date").toString(), autoSettingList.get(i).get("send_time").toString(), -1);
                    historyInsertMap.put("send_type", 1);
                    if (date != null) msgSendFlag = true;
                    else msgSendFlag = false;

                }
                /* type 2 당일 알림 */
                else if ("2".equals(autoSettingList.get(i).get("type").toString())) {
                    String settingTimes = autoSettingList.get(i).get("send_time").toString();
                    String[] settingTimeArray = settingTimes.split(":");
                    String settingHour = settingTimeArray[0];
                    String settingMinute = settingTimeArray[1];

                    date = CommonUtils.stringConvertDateMinusDay2(map.get("res_date").toString(), settingHour, settingMinute);
                    historyInsertMap.put("send_type", 2);
                    if (date != null) msgSendFlag = true;
                    else msgSendFlag = false;

                }
                /* type 3 시간대별 알림 */
                else if ("3".equals(autoSettingList.get(i).get("type").toString())) {
                    date = CommonUtils.stringConvertDateMinusTime(map.get("res_date").toString(), map.get("res_hour").toString(), map.get("res_minute").toString(), autoSettingList.get(i).get("send_time").toString());
                    historyInsertMap.put("send_type", 3);
                    if (date != null) msgSendFlag = true;
                    else msgSendFlag = false;

                }
                /* type 4 예약등록 알림 */
                else if ("4".equals(autoSettingList.get(i).get("type").toString())) {
                    historyInsertMap.put("send_type", 4);
                    msgSendFlag = true;
                }

                if (msgSendFlag) {
                    try {
                        if ("sms".equals(autoSettingList.get(i).get("method").toString())) {
                            System.out.print("확인 : ");
                            System.out.println(sMap.get("com_num").toString() + sMap.get("tel").toString() + clientPhone + clientName + utils.autoMsgReplace1(autoSettingList.get(i).get("content").toString(), clientName, map.get("res_date").toString(), map.get("res_hour").toString(), map.get("res_minute").toString()) + date + sMap.get("id").toString());

                            // 90 byte 이하
                            if(CommonUtils.getByteSizeToComplex(utils.autoMsgReplace1(autoSettingList.get(i).get("content").toString(), clientName, map.get("res_date").toString(), map.get("res_hour").toString(), map.get("res_minute").toString())) <= 90){
                                receiptNum = messageService.sendSMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientPhone, clientName, utils.autoMsgReplace1(autoSettingList.get(i).get("content").toString(), clientName, map.get("res_date").toString(), map.get("res_hour").toString(), map.get("res_minute").toString()), date, sMap.get("id").toString());
                                historyInsertMap.put("method", 0);
                                historyInsertMap.put("content", autoSettingList.get(i).get("content").toString());
                                sendPoint += 14;
                            } else {
                                receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientPhone, clientName, "예약 알림", utils.autoMsgReplace1(autoSettingList.get(i).get("content").toString(), clientName, map.get("res_date").toString(), map.get("res_hour").toString(), map.get("res_minute").toString()), date, sMap.get("id").toString());
                                historyInsertMap.put("method", 1);
                                historyInsertMap.put("content", autoSettingList.get(i).get("content").toString());
                                sendPoint += 34;
                            }
                        }
                        /* lms */
                        else if ("lms".equals(autoSettingList.get(i).get("method").toString())) {
                            System.out.print("확인 : ");
                            System.out.println(sMap.get("com_num").toString() + sMap.get("tel").toString() + clientPhone + clientName + "예약 알림" + utils.autoMsgReplace1(autoSettingList.get(i).get("content").toString(), clientName, map.get("res_date").toString(), map.get("res_hour").toString(), map.get("res_minute").toString()) + date + sMap.get("id").toString());
                            receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientPhone, clientName, "예약 알림", utils.autoMsgReplace1(autoSettingList.get(i).get("content").toString(), clientName, map.get("res_date").toString(), map.get("res_hour").toString(), map.get("res_minute").toString()), date, sMap.get("id").toString());
                            historyInsertMap.put("method", 1);
                            historyInsertMap.put("content", autoSettingList.get(i).get("content").toString());
                            sendPoint += 34;
                        }
                        Date receiveDate = new Date();
                        if(date != null) receiveDate = date;

                        historyInsertMap.put("send_count", 1);
                        historyInsertMap.put("receipt_num", receiptNum);
                        historyInsertMap.put("res_type", map.get("res_type").toString());
                        historyInsertMap.put("client_idx", map.get("clientIdx"));
                        historyInsertMap.put("res_idx", map.get("ID"));
                        historyInsertMap.put("receiveDate" , receiveDate);
                        historyInsertMap.put("shopId", sMap.get("id"));
                        reservationDAO.insertAutoMsgHistory(historyInsertMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("날짜 초과");
                }

            }
        }
        sMap.put("sendPoint", sendPoint);
//        reservationDAO.SMSPointConsumption(sMap);
    }

    /* type 5 예약 취소 자동문자 확인 및 발송 */
    public void autoCancleMessageCheckAndSend(Map<String, Object> map, Map<String, Object> sMap, Map<String, Object> clientInfo, MessageService messageService) {

        // @@@@@ 예약 문자 취소
        Map<String, Object> historyInsertMap = new HashMap<>();
        Map<String, Object> autoSettingMap = reservationDAO.selectCancleAutoSetting(map);
        CommonUtils utils = new CommonUtils();
        String receiptNum = "";
        String clientName = "";
        String clientPhone = "";
        String clientIdx = "";

        /* 미등록 고객 */
        if(!"null".equals(clientInfo.get("un_name").toString())){
            clientName = clientInfo.get("un_name").toString();
            clientPhone = clientInfo.get("un_phone").toString();
            clientIdx = "0";
        }
        /* 등록 고객 */
        else {
            clientName = clientInfo.get("name").toString();
            clientPhone = clientInfo.get("phone").toString();
            clientIdx = clientInfo.get("idx").toString();
        }


        if (autoSettingMap != null) {
            try {
                if ("sms".equals(autoSettingMap.get("method").toString())) {
                    System.out.println(sMap.get("com_num").toString() + sMap.get("tel").toString() + clientName + clientPhone + utils.autoMsgReplace5(autoSettingMap.get("content").toString(), clientName, clientInfo.get("date").toString()) + null + sMap.get("id").toString());
                    // 90 byte 이하
                    if(CommonUtils.getByteSizeToComplex(utils.autoMsgReplace5(autoSettingMap.get("content").toString(), clientName, clientInfo.get("date").toString())) <= 90){
                        receiptNum = messageService.sendSMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientPhone, clientName, utils.autoMsgReplace5(autoSettingMap.get("content").toString(), clientName, clientInfo.get("date").toString()), null, sMap.get("id").toString());
                        historyInsertMap.put("method", 0);
                        historyInsertMap.put("content", autoSettingMap.get("content").toString());
                    } else {
                        System.out.println(
                                "com num : " + sMap.get("com_num").toString() +
                                        "\n 발신 번호 : " + sMap.get("tel").toString()+
                                        "\n 수신 번호 : " + clientPhone+
                                        "\n 고객 이름 : " + clientName+
                                        "\n 제목 : " + "예약 취소 알림"+
                                        "\n 본문 : " + utils.autoMsgReplace5(autoSettingMap.get("content").toString(), clientName, clientInfo.get("date").toString())+
                                        "\n 예약 전송 : " + null+
                                        "\n 매장 아이디 : " + sMap.get("id").toString()
                        );

                        receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientPhone, clientName, "예약 취소 알림", utils.autoMsgReplace5(autoSettingMap.get("content").toString(), clientName, clientInfo.get("date").toString()), null, sMap.get("id").toString());
                        historyInsertMap.put("method", 1);
                        historyInsertMap.put("content", autoSettingMap.get("content").toString());
                    }
                }
                /* lms */
                else if ("lms".equals(autoSettingMap.get("method").toString())) {
                    System.out.println(sMap.get("com_num").toString() + sMap.get("tel").toString() + clientPhone + clientName + "예약 취소 알림" + utils.autoMsgReplace5(autoSettingMap.get("content").toString(), clientName, clientInfo.get("date").toString()) + null + sMap.get("id").toString());
                    receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), sMap.get("tel").toString(), clientPhone, clientName, "예약 취소 알림", utils.autoMsgReplace5(autoSettingMap.get("content").toString(), clientName, clientInfo.get("date").toString()), null, sMap.get("id").toString());
                    historyInsertMap.put("method", 1);
                    historyInsertMap.put("content", autoSettingMap.get("content").toString());
                }


                historyInsertMap.put("send_count", 1);
                historyInsertMap.put("receipt_num", receiptNum);
                historyInsertMap.put("send_type", 5);
                historyInsertMap.put("client_idx", clientIdx);
                historyInsertMap.put("res_idx", map.get("idx"));
                if(clientInfo.get("res_type") != null && !"0".equals(clientInfo.get("res_type").toString()))
                    historyInsertMap.put("res_type", 0);
                else
                    historyInsertMap.put("res_type", 1);

                historyInsertMap.put("shopId", sMap.get("id"));
                reservationDAO.insertAutoMsgHistorySub(historyInsertMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public Map<String, Object> selectShopInfo(Map<String, Object> map) { return reservationDAO.selectShopInfo(map); }
}
