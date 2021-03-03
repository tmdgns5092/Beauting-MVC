package kr.co.serinusSM.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("clientDAO")
public class ClientDAO extends AbstractDAO {
    Logger log = Logger.getLogger(this.getClass());

    /* Client All Count */
    public Map<String, Object> selectAllClientsList(Map<String, Object> map) { return (Map<String, Object>)selectOne("client.selectAllClientsList", map); }
    /* Paging & Sorting Clients */
    public List<Map<String, Object>> selectSortingAndPagingClients(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("client.selectSortingAndPagingClients", map); }
    /* Client Code Auto Create */
    public Map<String, Object> selectClientLargestNumber(Map<String, Object> map) { return (Map<String, Object>) selectOne("client.selectClientLargestNumber", map); }
    /* Phone Number Overlap Check */
    public Map<String, Object> selectOverCheckFromPhone(Map<String, Object> map) { return (Map<String, Object>) selectOne("client.selectOverCheckFromPhone", map); }
    /* 고객 코드 중복 확인 */
    public Map<String, Object> selectOverCheckFromCode(Map<String, Object> map) { return (Map<String, Object>) selectOne("client.selectOverCheckFromCode", map); }
    /* 신규 고객 등록 */
    public void insertNewClient(Map<String, Object> map) { insert("client.insertNewClient", map);}
    /* 고객 판매내역 */
    public List<Map<String,Object>> selectClientSalesAjax(Map<String,Object> map) { return (List<Map<String, Object>>)selectList("client.selectClientSalesAjax", map); }
    /* 고객 정보 */
    public Map<String,Object> selectClientInfoAjax(Map<String,Object> map) { return (Map<String, Object>)selectOne("client.selectClientInfoAjax", map); }
    /* 고객정보 수정 */
    public void updateClientInfoAjax(Map<String,Object> map) { update("client.updateClientInfoAjax", map); }
    /* 고객정보 수정 이력 */
    public void insertdiscriminationPrepaid(Map<String,Object> map) {
        // 선불권 수정 이력 저장
        if(!"[]".equals(map.get("discriminationPrepaid").toString())) insert("client.discriminationPrepaid", map);
        // 횟수권 수정 이력 저장
        if(!"[]".equals(map.get("discriminationTicket").toString())) insert("client.discriminationTicket", map);
        // 선불권 삭제 이력 저장
        if(!"[]".equals(map.get("deletePrepaid").toString())) insert("client.deletePrepaid", map);
        // 횟수권 삭제 이력 저장
        if(!"[]".equals(map.get("deleteTicket").toString())) insert("client.deleteTicket", map);

    }

    /* (선불권 수정) 고객정보 수정에 따른 판매 데이터 넣기 */
    public void changePrepaidInsertSalesTable(Map<String,Object> map) { update("client.changePrepaidInsertSalesTable", map); }
    /* (횟수권 수정) 고객정보 수정에 따른 판매 데이터 넣기 */
    public void changeTicketInsertSalesTable(Map<String,Object> map) { update("client.changeTicketInsertSalesTable", map); }


    /* 회원권 디테일 */
    public List<Map<String,Object>> selectPrepaidDetail(Map<String,Object> map) { return (List<Map<String, Object>>)selectList("client.selectPrepaidDetail", map); }
    /* 티켓 카테고리 */
    public List<Map<String,Object>> selectTicketCategory(Map<String,Object> map) { return (List<Map<String, Object>>)selectList("client.selectTicketCategory", map); }
    /* 티켓 디테일 */
    public List<Map<String,Object>> selectTicketDetail(Map<String,Object> map) { return (List<Map<String, Object>>)selectList("client.selectTicketDetail", map); }
    /* 고객 정보 (선불잔액 잔여 포인트 미수금) 불러오기 */
    public Map<String,Object> selectClientPrePointMiss(Map<String,Object> map) { return (Map<String, Object>)selectOne("client.selectClientPrePointMiss", map); }
    /* 고객 메모 업데이트 */
    public void updateClientMemo(Map<String,Object> map) { update("client.updateClientMemo", map); }
    /* 고객 조회 for idx */
    public Map<String,Object> selectClientForIdx(Map<String,Object> map) { return (Map<String, Object>)selectOne("client.selectClientForIdx", map); }
    /* 고객 조회 for code */
    public Map<String,Object> selectClientForCode(Map<String,Object> map) { return (Map<String, Object>)selectOne("client.selectClientForCode", map); }
    /* 고객 코드 반자동 */
    public Map<String,Object> selectClientCodeSemiAuto(Map<String,Object> map) { return (Map<String, Object>)selectOne("client.selectClientCodeSemiAuto", map); }
    /* 고객 정보 가져오기 */
    public Map<String,Object> selectThisClient(Map<String,Object> map) { return (Map<String, Object>)selectOne("client.selectThisClient", map); }
}
