package kr.co.serinusSM.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("salesDAO")
public class SalesDAO extends AbstractDAO {
    Logger log = Logger.getLogger(this.getClass());

    /* 예약 타입 불러오기 */
    public int selectResType(Map<String,Object> map) { return (Integer)selectOne("sales.selectResType", map); }
    /* 예약 결제 정보 불러오기 (회원) */
    public Map<String, Object> selectCallReservationPayInfo1(Map<String,Object> map) { return (Map<String, Object>)selectOne("sales.selectCallReservationPayInfo1", map); }
    /* 예약 결제 정보 불러오기 (미등록 회원) */
    public Map<String, Object> selectCallReservationPayInfo2(Map<String,Object> map) { return (Map<String, Object>)selectOne("sales.selectCallReservationPayInfo2", map); }
    /* 미예약 판매 페이지 고객정보 불러오기 */
    public Map<String, Object> selectClientInfoFromIdx(Map<String,Object> map) { return (Map<String, Object>)selectOne("sales.selectClientInfoFromIdx", map); }
    /* 판매 직원 불러오기 */
    public List<Map<String, Object>> selectEmployeeListCall(Map<String,Object> map) { return (List<Map<String, Object>>)selectList("sales.selectEmployeeListCall", map); }
    /* 판매 직원 불러오기 Ver2 */
    public List<Map<String, Object>> selectSaleEmployeeListCall(Map<String,Object> map) { return (List<Map<String, Object>>)selectList("sales.selectSaleEmployeeListCall", map); }
    /* 서비스 카테고리 불러오기 */
    public List<Map<String, Object>> selectServiceCategoryCall(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("sales.selectServiceCategoryCall", map); }
    public Map<String, Object> selectServiceCategoryIdxCall(Map<String, Object> map) { return (Map<String, Object>)selectOne("sales.selectServiceCategoryIdxCall", map); }
    /* 서비스 디테일 불러오기 */
    public List<Map<String, Object>> selectServiceDetailCall(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("sales.selectServiceDetailCall", map); }
    /* 제품 모두 불러오기 */
    public List<Map<String, Object>> salectAllProductCateCall(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("sales.salectAllProductCateCall", map); }
    public List<Map<String, Object>> salectAllProductDetailCall(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("sales.salectAllProductDetailCall", map); }
    /* 분류 세부 제품 가져오기 */
    public List<Map<String, Object>> salectCateSortProductDetailCall(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("sales.salectCateSortProductDetailCall", map); }
    /* 제품 검색 */
    public List<Map<String, Object>> salectProductSreach(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("sales.salectProductSreach", map); }
    /* 정액 선불권 모두 불러오기 */
    public List<Map<String, Object>> salectAllCatePrepaymentListCall(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("sales.salectAllCatePrepaymentListCall", map); }
    public List<Map<String, Object>> salectAllDetailPrepaymentListCall(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("sales.salectAllDetailPrepaymentListCall", map); }
    /* 티켓 불러오기 */
    public List<Map<String, Object>> salectAllCateTicketListCall(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("sales.salectAllCateTicketListCall", map); }
    public List<Map<String, Object>> salectAllDetailTicketListCall(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("sales.salectAllDetailTicketListCall", map); }
    /* 판매내역 고객 검색 */
    public Map<String, Object> selectClient(Map<String,Object> map) { return(Map<String,Object>)selectOne("sales.selectClient", map); }
    /* 고객 보유 선불권 불러오기 */
    public Map<String, Object> selectPrepaidOfClientCall(Map<String,Object> map) { return(Map<String,Object>)selectOne("sales.selectPrepaidOfClientCall", map); }
    /* 고객 보유 티켓 불러오기 */
    public Map<String, Object> selectTicketOfClientCall(Map<String,Object> map) { return(Map<String,Object>)selectOne("sales.selectTicketOfClientCall", map); }
    /* 선불권 판매 */
    public void insertPrepaidSalesData(Map<String,Object> map) { insert("sales.insertPrepaidSalesData", map); }
    /* 고객 선불권 sum update */
    public void updateSumClientPrepaidJson(Map<String, Object> map){ update("sales.updateSumClientPrepaidJson", map);}
    /* 고객 선불권 업데이트 */
    public void updateClientPrepaidJson(Map<String, Object> map){ update("sales.updateClientPrepaidJson", map);}
    /* 횟수권 판매 */
    public void insertTicketSalesData(Map<String,Object> map) { insert("sales.insertTicketSalesData", map); }
    /* 고객 횟수권 sum update */
    public void updateSumClientTicketJson(Map<String, Object> map){ update("sales.updateSumClientTicketJson", map);}
    /* 고객 횟수권 update */
    public void updateClientTicketJson(Map<String, Object> map){ update("sales.updateClientTicketJson", map);}


    /* searchClient 페이징 카운트 */
    public Map<String, Object> searchClientPagingCount(Map<String,Object> map) { return(Map<String,Object>)selectOne("sales.searchClientPagingCount", map); }
    /* searchClient 페이징 컨텐츠 */
    public List<Map<String, Object>> searchClientPagingContent(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("sales.searchClientPagingContent", map); }




    /* 고객 예약 전체 내역 카운트 */
    public Map<String,Object> selectAllReservationHistory(Map<String,Object> map) { return(Map<String,Object>)selectOne("sales.selectAllReservationHistory", map); }
    /* 고객 예약 내역 페이징 */
    public List<Map<String,Object>> selectReservationHistory(Map<String,Object> map) { return(List<Map<String,Object>>)selectList("sales.selectReservationHistory", map); }
    /* 고객 서비스 타입별 총 카운트(구) */
    public Map<String,Object> selectAllServicesHistory(Map<String,Object> map) {return(Map<String,Object>)selectOne("sales.selectAllServicesHistory", map);}
    /* 고객 서비스 타입별 내역 페이징(구) */
    public List<Map<String,Object>> selectServicesHistory(Map<String,Object> map) {return(List<Map<String,Object>>)selectList("sales.selectServicesHistory", map);}
    /* 고객 서비스 타입별 총 카운트 */
    public Map<String,Object> selectCountServicesType(Map<String,Object> map) {return(Map<String,Object>)selectOne("sales.selectCountServicesType", map);}
    /* 고객 서비스 타입별 내역 페이징 */
    public List<Map<String,Object>> selectServicesTypeHistory(Map<String,Object> map) {return(List<Map<String,Object>>)selectList("sales.selectServicesTypeHistory", map);}
    /* 하나의 판매내역 검색 */
    public Map<String,Object> selectSalesDateAjax(Map<String,Object> map) { return(Map<String, Object>)selectOne("sales.selectSalesDateAjax", map); }
    /* 하나의 판매내역 검색 */
    public Map<String,Object> selectSalesOneAjax(Map<String,Object> map) { return(Map<String, Object>)selectOne("sales.selectSalesOneAjax", map); }

    /* 일반판매 - 서비스 */
    public void insertServieSales(Map<String, Object> map) { insert("sales.insertServieSales", map); }
    /* 일반판매 - 제품 */
    public void insertProductSales(Map<String, Object> map) { insert("sales.insertProductSales", map); }
    /* 고객 선불권 차감 */
    public void updateClientUsePrepaid(Map<String, Object> map) { insert("sales.updateClientUsePrepaid", map); }
    /* 고객 횟수권 차감 */
    public void updateClientUseTicket(Map<String, Object> map) { insert("sales.updateClientUseTicket", map); }
    /* 고객 총 결제금액 미수금 업데이트 */
    public int selecttotalVist(Map<String, Object> map){ Map<String, Object> rMap = (Map<String, Object>)selectOne("sales.selecttotalVist", map); return Integer.parseInt(rMap.get("totalVisit").toString());}
    public int selecttotalPay(Map<String, Object> map){ Map<String, Object> rMap = (Map<String, Object>)selectOne("sales.selecttotalPay", map); return Integer.parseInt(rMap.get("totalPay").toString());}
    public void updateClientCosts(Map<String, Object> map) { update("sales.updateClientCosts", map); }
    /* 예약 상태 수정 */
    public void updateReservationStatus(Map<String, Object> map) { insert("sales.updateReservationStatus", map); }
    /* 미수금 정산 */
    public void insertPayTheMissCost(Map<String, Object> map) { insert("sales.insertPayTheMissCost", map); }
    /* 고객 미수금 수정 */
    public void updateClientMissCost(Map<String, Object> map) { update("sales.updateClientMissCost", map); }
    /* 판매 취소로 인한 회원 정보 복구 */
    public void updateClientRestorePrice(Map<String, Object> map) { update("sales.updateClientRestorePrice", map); }
    /* 예약판 판매 취소 */
    public void updateCancelSale(Map<String, Object> map) { update("sales.updateCancelSale", map); }

    /* 판매 금액 불러오기 */
    public Map<String,Object> amountAtTheTimeOfSaleCall(Map<String,Object> map) { return(Map<String, Object>)selectOne("sales.amountAtTheTimeOfSaleCall", map); }
    /* 환불 */
    public void refundSubmit(Map<String, Object> map) { update("sales.refundSubmit", map); }
    /* 회원권 삭제 + 비회원 전환 */
    public void clientHasMemberShipEnd(Map<String, Object> map) { update("sales.clientHasMemberShipEnd", map); }
    /* 회원권 삭제 */
    public void clientHasMemberShipRemove(Map<String, Object> map) { update("sales.clientHasMemberShipRemove", map); }
    /* 티켓 삭제 */
    public void clientHasMemberShipTicketRemove(Map<String, Object> map) { update("sales.clientHasMemberShipTicketRemove", map); }
    /* 총 결제금액 수정 */
    public void updateClientPrice(Map<String, Object> map) { update("sales.updateClientPrice", map); }
    public void updateClientPrice2(Map<String, Object> map) { update("sales.updateClientPrice2", map); }

    /* 프로모션 가져오기 */
    public Map<String,Object> promotionSelect(Map<String,Object> map) { return(Map<String, Object>)selectOne("sales.promotionSelect", map); }
    /* 프로모션 성과치 적용시키기 */
    public void updatePromotionAchievement(Map<String,Object> map) {  update("sales.updatePromotionAchievement", map); }
    /* 판매취소 성과치 - 시키기 */
    public void updateCancelPromotionAchievement(Map<String,Object> map) {  update("sales.updateCancelPromotionAchievement", map); }
    /* 판매 취소를 위한 판매 레코드 호출 */
    public List<Map<String,Object>> selectCancelSalesFromResIdx(Map<String,Object> map) { return(List<Map<String, Object>>)selectList("sales.selectCancelSalesFromResIdx", map); }
    /* 판매 데이터 불러오기 [key : sales_table idx] */
    public Map<String,Object> selectSalesDataFromTableIdx(Map<String,Object> map) { return(Map<String, Object>)selectOne("sales.selectSalesDataFromTableIdx", map); }
    /* 판매 취소로 인한 판매 데이터 복구 */
    public void updateReturnSaleFromClientData(Map<String,Object> map) {  update("sales.updateReturnSaleFromClientData", map); }
    /* 판매 상태 업데이트 */
    public void updateSaleStatusChange(Map<String,Object> map) {  update("sales.updateSaleStatusChange", map); }
    /* 객단가만 업데이트 */
    public void updateClientPriceVer2(Map<String,Object> map) {  update("sales.updateClientPriceVer2", map); }
    /* 자동문자 설정 가져오기 */
    public Map<String ,Object> selectReservationAutoMsg(Map<String, Object> map) { return (Map<String, Object>)selectOne("sales.selectReservationAutoMsg", map);}
    public List<Map<String ,Object>> selectReservationAutoMsgAsync(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("sales.selectReservationAutoMsgAsync", map);}

    /* MSG history Insert */
    public void insertAutoMsgHistory(Map<String,Object> map) {  insert("sales.insertAutoMsgHistory", map); }
    /* Prepaid History Insert */
    public void insertPrepaidMsgHistory(Map<String, Object> map) { insert("sales.insertPrepaidMsgHistory", map); }
}
