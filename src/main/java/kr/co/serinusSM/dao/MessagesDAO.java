package kr.co.serinusSM.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("messagesDAO")
public class MessagesDAO extends AbstractDAO {
    Logger log = Logger.getLogger(this.getClass());

    public Map<String, Object> selectAllClientCount(Map<String,Object> map) { return (Map<String, Object>) selectOne("messages.selectAllClientCount", map); }
    public List<Map<String, Object>> selectAllClientPaging(Map<String,Object> map) { return (List<Map<String, Object>>) selectList("messages.selectAllClientPaging", map); }
    public Map<String, Object> selectAllCheckClientCount(Map<String,Object> map) { return (Map<String, Object>) selectOne("messages.selectAllCheckClientCount", map); }
    public List<Map<String, Object>> selectAllCheckClient(Map<String,Object> map) { return (List<Map<String, Object>>) selectList("messages.selectAllCheckClient", map); }
    public Map<String, Object> selectNotAllCheckClientCount(Map<String,Object> map) { return (Map<String, Object>) selectOne("messages.selectNotAllCheckClientCount", map); }
    public List<Map<String, Object>> selectNotAllCheckClient(Map<String,Object> map) { return (List<Map<String, Object>>) selectList("messages.selectNotAllCheckClient", map); }
    /* 자동문자 설정 내용 들고오기 */
    public Map<String, Object> selectAutoMessage(Map<String,Object> map) { return (Map<String, Object>) selectOne("messages.selectAutoMessage", map); }
    /* 메세지 발송내역 적재 */
    public void insertMessageData(Map<String,Object> map) { insert("messages.insertMessageData", map); }
    /* 문자 포인트 차감 */
    public void updateDeductionMessagePointSubtract(Map<String,Object> map) { insert("messages.updateDeductionMessagePointSubtract", map); }
    /* 자동 문자 설정 가져오기 */
    public List<Map<String, Object>> selectAutoMSGSettings(Map<String,Object> map) { return (List<Map<String, Object>>) selectList("messages.selectAutoMSGSettings", map); }
    public List<Map<String, Object>> selectServiceCategory14(Map<String,Object> map) { return (List<Map<String, Object>>) selectList("messages.selectServiceCategory14", map); }
    public List<Map<String, Object>> selectServiceCategory15(Map<String,Object> map) { return (List<Map<String, Object>>) selectList("messages.selectServiceCategory15", map); }
    /* 자동 문자 업데이트 */
    // 세팅 확인
    public Map<String, Object> autoSettingCheck(Map<String,Object> map) { return  (Map<String, Object>) selectOne("messages.autoSettingCheck", map); }
    // type1 update, insert
    public void updateAutoMessageType1(Map<String,Object> map) { update("messages.updateAutoMessageType1", map); }
    public void insertAutoMessageType1(Map<String,Object> map) { insert("messages.insertAutoMessageType1", map); }
    // type2 update, insert now
    public void updateAutoMessageType2(Map<String,Object> map) { update("messages.updateAutoMessageType2", map); }
    public void insertAutoMessageType2(Map<String,Object> map) { insert("messages.insertAutoMessageType2", map); }
    // type3 update, insert now
    public void updateAutoMessageType3(Map<String,Object> map) { update("messages.updateAutoMessageType3", map); }
    public void insertAutoMessageType3(Map<String,Object> map) { insert("messages.insertAutoMessageType3", map); }
    // type4 update, insert now
    public void updateAutoMessageType4(Map<String,Object> map) { update("messages.updateAutoMessageType4", map); }
    public void insertAutoMessageType4(Map<String,Object> map) { insert("messages.insertAutoMessageType4", map); }
    // type5 update, insert now
    public void updateAutoMessageType5(Map<String,Object> map) { update("messages.updateAutoMessageType5", map); }
    public void insertAutoMessageType5(Map<String,Object> map) { insert("messages.insertAutoMessageType5", map); }
    // type6 update, insert now
    public void updateAutoMessageType6(Map<String,Object> map) { update("messages.updateAutoMessageType6", map); }
    public void insertAutoMessageType6(Map<String,Object> map) { insert("messages.insertAutoMessageType6", map); }
    // 문자 발송내역 가져오기
    public Map<String,Object> selectAllMessageList(Map<String,Object> map) { return (Map<String, Object>) selectOne("messages.selectAllMessageList", map); }
    public List<Map<String,Object>> selectSortingAndPagingMessages(Map<String,Object> map) { return (List<Map<String, Object>>) selectList("messages.selectSortingAndPagingMessages", map); }
}
