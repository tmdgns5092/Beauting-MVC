package kr.co.serinusSM.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("reservationDAO")
public class ReservationDAO extends AbstractDAO {
    Logger log = Logger.getLogger(this.getClass());

    /* 모든 직원 정보 가져오기 */
    public Map<String, Object> selectEmployeeAllMaxPage(Map<String, Object> map) { return (Map<String, Object>)selectOne("reservation.selectEmployeeAllMaxPage", map);}
    public List<Map<String, Object>> selectEmployeeAll(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("reservation.selectEmployeeAll", map); }
    /* 예약정보 가져오기 */
    public List<Map<String, Object>> selectToDayReservation(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("reservation.selectToDayReservation", map); }
    /* 날짜 예약정보 가져오기 */
    public List<Map<String, Object>> selectDatePickReservation(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("reservation.selectDatePickReservation", map); }
    /* 예약 업데이트 */
    public void updateReservation(Map<String, Object> map) { update("reservation.updateReservation", map); }
    /* 예약 이동 체크 */
    public Map<String, Object> selectReservationMoveCheck(Map<String, Object> map) { return (Map<String, Object>)selectOne("reservation.selectReservationMoveCheck", map); }
    /* 예약 마감시간 업데이트 */
    public void updateFormReservationEndTime(Map<String, Object> map) { update("reservation.updateFormReservationEndTime", map); }
    /* 예약 등록 고객 검색 */
    public List<Map<String, Object>> selectSortingAndPagingClients(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("reservation.selectSortingAndPagingClients", map); }
    /* 예약 등록 고객 가져오기 */
    public Map<String, Object> selectReservationClientList_Count(Map<String, Object> map) { return (Map<String, Object>)selectOne("reservation.selectReservationClientList_Count", map); }
    public List<Map<String, Object>> selectReservationClientList(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("reservation.selectReservationClientList", map); }
    /* 서비스 카테고리 가져오기 */
    public List<Map<String, Object>> selectServiceCategory(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("reservation.selectServiceCategory", map); }
    /* 서비스 디테일 가져오기 */
    public List<Map<String, Object>> serviceDetailCall(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("reservation.serviceDetailCall", map); }
    /* 예약 직원 스케쥴 확인 */
    public Map<String, Object> selectEmployeScheduleCheck(Map<String, Object> map) { return (Map<String, Object>)selectOne("reservation.selectEmployeScheduleCheck", map); }
    /* 예약 시간 중복 확인 */
    public Map<String, Object> selectReservationOverlapCheck(Map<String, Object> map) { return (Map<String, Object>)selectOne("reservation.selectReservationOverlapCheck", map); }
    /* 직원 스케줄 확인 */
    public Map<String, Object> selectEmployeeScheduleCheck(Map<String, Object> map) { return (Map<String, Object>)selectOne("reservation.selectEmployeeScheduleCheck", map); }
    /* 예약 시간 중복 확인 */
    public Map<String, Object> selectReservationOverlapCheck_ver2(Map<String, Object> map) { return (Map<String, Object>)selectOne("reservation.selectReservationOverlapCheck_ver2", map); }
    /* 예약 등록 */
    public void addReservation(Map<String, Object> map) { insert("reservation.addReservation", map); }
    /* 예약정보 가져오기 */
    public Map<String, Object> callReservationDataFromIdx(Map<String, Object> map) { return (Map<String, Object>)selectOne("reservation.callReservationDataFromIdx", map); }
    /* 예약 수정 (세부항목) */
    public void updateReservationDetail(Map<String, Object> map) { update("reservation.updateReservationDetail", map); }
    /* 예약 불이행 */
    public void updateReservationCancel1(Map<String, Object> map) { update("reservation.updateReservationCancel1", map); }
    public void updateReservationCancel2(Map<String, Object> map) { update("reservation.updateReservationCancel2", map); }
    /* 예약 삭제 전 고객 정보 가져오기 */
    public Map<String, Object> selectClientInfo(Map<String, Object> map) { return (Map<String, Object>)selectOne("reservation.selectClientInfo", map); }
    public List<Map<String, Object>> selectReceiptNum(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("reservation.selectReceiptNum", map); }
    /* 예약 삭제 */
    public void reservationRemove(Map<String, Object> map) { update("reservation.reservationRemove", map); }
    /* 예약 불이행 취소 */
    public void reservationStatusUpdateDefault(Map<String, Object> map) { update("reservation.reservationStatusUpdateDefault", map); }
    /* 불이행 횟수 차감 */
    public void updateClientNoshowCountMinus(Map<String, Object> map) { update("reservation.updateClientNoshowCountMinus", map); }
    /* 선택한 날 판매 검색 */
    public List<Map<String, Object>> selectSalesForDate(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("reservation.selectSalesForDate", map); }
    /* 매장의 휴무인지 확인 */
    public Map<String, Object> selectScheduleHoliday(Map<String, Object> map) { return (Map<String, Object>)selectOne("reservation.selectScheduleHoliday", map); }
    /* 선택한날 목표금액 검색 */
    public Map<String, Object> selectEmployeeGoalData(Map<String, Object> map) { return (Map<String, Object>)selectOne("reservation.selectEmployeeGoalData", map); }
    /* 고객 idx 가져오기 */
    public Map<String, Object> selectClientIdx(Map<String, Object> map) { return (Map<String, Object>)selectOne("reservation.selectClientIdx", map); }
    /* 프로모션 등록 */
    public void insertPromotion(Map<String,Object> map) { insert("reservation.insertPromotion", map); }
    /* 프로모션 수정 */
    public void updatePromotion(Map<String,Object> map) { update("reservation.updatePromotion", map); }
    /* 진행중인 프로모션 검색 */
    public Map<String, Object> selectOnePromotion(Map<String,Object> map) { return (Map<String, Object>)selectOne("reservation.selectOnePromotion", map); }
    /* 지난 프로모션 검색 */
    public List<Map<String, Object>> selectListPromotion(Map<String,Object> map) { return (List<Map<String, Object>>)selectList("reservation.selectListPromotion", map); }
    /* 자동 문자 설정 가져오기 */
    public List<Map<String, Object>> selectMessageAuto(Map<String,Object> map) { return (List<Map<String, Object>>)selectList("reservation.selectMessageAuto", map); }
    /* 이미 등록된 예약 문자 가져오기 */
    public List<Map<String, Object>> beforSendHistory(Map<String,Object> map) { return (List<Map<String, Object>>)selectList("reservation.beforSendHistory1", map); }
    /* 이미 등록된 예약 문자 가져오기 */
    public Map<String, Object> selectReservationFromIdx(Map<String,Object> map) { return (Map<String, Object>)selectOne("reservation.selectReservationFromIdx", map); }
    /* 예약 발송 취소 DB UPDATE */
    public void updateMsgHistroyFromReservationMsg(Map<String,Object> map) { update("reservation.updateMsgHistroyFromReservationMsg", map); }

    /* 자동 문자 설정 가져오기 */
    public List<Map<String, Object>> selectReservationAutoMsg(Map<String,Object> map) { return (List<Map<String, Object>>)selectList("reservation.selectReservationAutoMsg", map); }
    /* 예약취소 설정 가져오기 */
    public Map<String, Object> selectCancleAutoSetting(Map<String,Object> map) { return (Map<String, Object>)selectOne("reservation.selectCancleAutoSetting", map); }

    /* 자동 문자 msg_history insert */
    public void insertAutoMsgHistory(Map<String, Object> map) { insert("reservation.insertAutoMsgHistory", map);}

    public void insertAutoMsgHistorySub(Map<String, Object> map) { insert("reservation.insertAutoMsgHistorySub", map);}

    /* 자동문자 차감 */
    public void SMSPointConsumption(Map<String, Object> map) { insert("reservation.SMSPointConsumption", map);}

    public Map<String, Object> selectShopInfo(Map<String,Object> map) { return (Map<String, Object>)selectOne("reservation.selectShopInfo", map); }
}
