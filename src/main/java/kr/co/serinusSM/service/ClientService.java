package kr.co.serinusSM.service;

import com.google.gson.Gson;
import kr.co.serinusSM.common.CommonUtils;
import kr.co.serinusSM.dao.ClientDAO;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("clientService")
public class ClientService {
    Logger log = Logger.getLogger(this.getClass());

    @Resource(name="clientDAO")
    private ClientDAO clientDAO;

    /* Client All Count */
    public Map<String, Object> selectAllClientsList(Map<String, Object> map) { return clientDAO.selectAllClientsList(map); }
    /* Paging & Sorting Clients */
    public List<Map<String, Object>> selectSortingAndPagingClients(Map<String, Object> map) { return clientDAO.selectSortingAndPagingClients(map); }
    /* client code Auto create */
    public Map<String, Object> selectClientLargestNumber(Map<String, Object> map) { return clientDAO.selectClientLargestNumber(map); }
    /* Phone Number Overlap Check */
    public Map<String, Object> selectOverCheckFromPhone(Map<String, Object> map){ return clientDAO.selectOverCheckFromPhone(map); }
    /* 고객 코드 중복 확인 */
    public Map<String, Object> selectOverCheckFromCode(Map<String, Object> map){ return clientDAO.selectOverCheckFromCode(map); }
    /* 신규 고객 등록 */
    public void insertNewClient(Map<String, Object> map) { clientDAO.insertNewClient(map); }
    /* 고객 판매내역 */
    public List<Map<String,Object>> selectClientSalesAjax(Map<String,Object> map) { return clientDAO.selectClientSalesAjax(map); }
    /* 고객 정보 */
    public Map<String,Object> selectClientInfoAjax(Map<String,Object> map) {return clientDAO.selectClientInfoAjax(map);}
    /* 고객정보 수정 */
    public void updateClientInfoAjax(Map<String,Object> map) { clientDAO.updateClientInfoAjax(map); }
    /* 회원권 디테일 */
    public List<Map<String,Object>> selectPrepaidDetail(Map<String,Object> map) { return clientDAO.selectPrepaidDetail(map); }
    /* 티켓 카테고리 */
    public List<Map<String,Object>> selectTicketCategory(Map<String,Object> map) { return clientDAO.selectTicketCategory(map); }
    /* 티켓 디테일 */
    public List<Map<String,Object>> selectTicketDetail(Map<String,Object> map) { return clientDAO.selectTicketDetail(map); }
    /* 고객 정보 (선불잔액 잔여 포인트 미수금) 불러오기 */
    public Map<String,Object> selectClientPrePointMiss(Map<String,Object> map) {return clientDAO.selectClientPrePointMiss(map);}
    /* 고객 메모 업데이트 */
    public void updateClientMemo(Map<String,Object> map) { clientDAO.updateClientMemo(map); }
    /* 고객 코드 반자동 */
    public Map<String,Object> selectClientCodeSemiAuto(Map<String,Object> map) {return clientDAO.selectClientCodeSemiAuto(map);}
    /* 고객 조회 for idx */
    public Map<String,Object> selectClientForIdx(Map<String,Object> map) {return clientDAO.selectClientForIdx(map);}
    /* 고객 조회 for code */
    public boolean selectClientForCode(Map<String, Object> map){
        Map<String, Object> cMap = clientDAO.selectClientForCode(map);
        System.out.println("맵 확인 : " + !MapUtils.isEmpty(cMap));
        System.out.println("맵 확인 : " + cMap);
        return !MapUtils.isEmpty(cMap);
    }
    /* 고객 정보 불러오기 */
    public void selectThisClient(Map<String, Object> map, Gson gson) {
        Map<String, Object> clientMap = clientDAO.selectThisClient(map);
        String pPrepaid = map.get("prepaid").toString();
        String pTicket = map.get("ticket").toString();
        String prepaid = clientMap.get("prepaid").toString();
        String ticket = clientMap.get("ticket").toString();

        /* 변경된 회원권 */
        List<Map<String, Object>> pPrepaidList = gson.fromJson(pPrepaid, List.class);
        List<Map<String, Object>> pTicketList = gson.fromJson(pTicket, List.class);

        /* 기존 회원권 */
        List<Map<String, Object>> prepaidList = gson.fromJson(prepaid, List.class);
        List<Map<String, Object>> ticketList = gson.fromJson(ticket, List.class);


        // 수정된 값 찾기
        String prepaidSalesData = CommonUtils.listDiscrimination(prepaidList, pPrepaidList, "cost");
        String ticketSalesData = CommonUtils.listDiscrimination(ticketList, pTicketList, "count");
        // 삭제된 값 찾기
        String prepaidDeleteData = CommonUtils.listDeleteSearch(prepaidList, pPrepaidList, "cost");
        String ticketDeleteData = CommonUtils.listDeleteSearch(ticketList, pTicketList, "count");

        map.put("discriminationPrepaid", prepaidSalesData);
        map.put("discriminationTicket", ticketSalesData);
        map.put("deletePrepaid", prepaidDeleteData);
        map.put("deleteTicket", ticketDeleteData);


        System.out.println("선불권 수정 : " + prepaidSalesData);
        System.out.println("선불권 삭제 : " + prepaidDeleteData);
        System.out.println("티켓 수정 : " + ticketSalesData);
        System.out.println("티켓 삭제: " + ticketDeleteData);
        clientDAO.insertdiscriminationPrepaid(map);
    }
}
