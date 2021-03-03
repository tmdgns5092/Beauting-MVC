package kr.co.serinusSM.service;

import kr.co.serinusSM.dao.MessagesDAO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("messagesService")
public class MessagesService {
    Logger log = Logger.getLogger(this.getClass());

    @Resource(name = "messagesDAO")
    private MessagesDAO messagesDAO;
    /* 자동 문자 설정 조회 */
    public Map<String, Object> selectAutoMessage(Map<String,Object> map) { return messagesDAO.selectAutoMessage(map); }
    /* 문자 이력 저장 */
    public void insertMessageData(Map<String,Object> map){ messagesDAO.insertMessageData(map); }
    /* 문자 포인트 차감 */
    public void updateDeductionMessagePointSubtract(Map<String,Object> map){ messagesDAO.updateDeductionMessagePointSubtract(map); }
    /* 총 고객 수 조회 */
    public Map<String, Object> selectAllClientCount(Map<String, Object> map) { return messagesDAO.selectAllClientCount(map); }
    /* 고객 페이징 조회 */
    public List<Map<String, Object>> selectAllClientPaging(Map<String, Object> map) { return messagesDAO.selectAllClientPaging(map); }
    /*  */
    public Map<String, Object> selectAllCheckClientCount(Map<String, Object> map) { return messagesDAO.selectAllCheckClientCount(map); }

    public List<Map<String, Object>> selectAllCheckClient(Map<String, Object> map) {
        return messagesDAO.selectAllCheckClient(map);
    }

    public Map<String, Object> selectNotAllCheckClientCount(Map<String, Object> map) {
        return messagesDAO.selectNotAllCheckClientCount(map);
    }

    public List<Map<String, Object>> selectNotAllCheckClient(Map<String, Object> map) {
        return messagesDAO.selectNotAllCheckClient(map);
    }

    /* 자동 문자 설정 가져오기 */
    public List<Map<String, Object>> selectAutoMSGSettings(Map<String, Object> map) {
        return messagesDAO.selectAutoMSGSettings(map);
    }

    public List<Map<String, Object>> selectServiceCategory14(Map<String, Object> map) {
        return messagesDAO.selectServiceCategory14(map);
    }

    public List<Map<String, Object>> selectServiceCategory15(Map<String, Object> map) {
        return messagesDAO.selectServiceCategory15(map);
    }

    public Map<String,Object> selectAllMessageList(Map<String,Object> map) {
        return messagesDAO.selectAllMessageList(map);
    }

    public List<Map<String,Object>> selectSortingAndPagingMessages(Map<String,Object> map) {
        return messagesDAO.selectSortingAndPagingMessages(map);
    }

    /* 자동 문자 업데이트 */
//    ================ type 1,2,3 ================
    public void updateAutoMessageType1(Map<String, Object> map) {
        Map<String, Object> checkMap = messagesDAO.autoSettingCheck(map);
        // update
        if (checkMap != null) {
            map.put("autoIDX", checkMap.get("idx"));
            messagesDAO.updateAutoMessageType1(map);
        }
        // insert
        else messagesDAO.insertAutoMessageType1(map);
    }

    //    ================ type 4,5 ================
    public void updateAutoMessageType2(Map<String, Object> map) {
        Map<String, Object> checkMap = messagesDAO.autoSettingCheck(map);
        // update
        if (checkMap != null) {
            map.put("autoIDX", checkMap.get("idx"));
            messagesDAO.updateAutoMessageType2(map);
        }
        // insert
        else messagesDAO.insertAutoMessageType2(map);
    }

    //    ================ type 6,7,8,9,11,12 ================
    public void updateAutoMessageType3(Map<String, Object> map) {
        Map<String, Object> checkMap = messagesDAO.autoSettingCheck(map);
        // update
        if (checkMap != null) {
            map.put("autoIDX", checkMap.get("idx"));
            messagesDAO.updateAutoMessageType3(map);
        }
        // insert
        else messagesDAO.insertAutoMessageType3(map);
    }

    //    ================ type 10,13 ================
    public void updateAutoMessageType4(Map<String, Object> map) {
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("array");

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> loopMap = list.get(i);
            loopMap.put("idx", map.get("idx"));
            Map<String, Object> checkMap = messagesDAO.autoSettingCheck(loopMap);
            // update
            if (checkMap != null) {
                loopMap.put("autoIDX", checkMap.get("idx"));
                messagesDAO.updateAutoMessageType4(loopMap);
            }
            // insert
            else messagesDAO.insertAutoMessageType4(loopMap);
        }
    }

    //    ================ type 14,15 ================
    public void updateAutoMessageType5(Map<String, Object> map) {
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("array");

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> loopMap = list.get(i);
            loopMap.put("idx", map.get("idx"));

            Map<String, Object> checkMap = messagesDAO.autoSettingCheck(loopMap);
            // update

            if (checkMap != null) {
                loopMap.put("autoIDX", checkMap.get("idx"));
                messagesDAO.updateAutoMessageType5(loopMap);
            }
            // insert
            else {
                messagesDAO.insertAutoMessageType5(loopMap);
            }
        }
    }
    //    ================ type 16 ================
    public void updateAutoMessageType6(Map<String, Object> map) {
        Map<String, Object> checkMap = messagesDAO.autoSettingCheck(map);
        // update
        if (checkMap != null) {
            map.put("autoIDX", checkMap.get("idx"));
            messagesDAO.updateAutoMessageType6(map);
        }
        // insert
        else messagesDAO.insertAutoMessageType6(map);
    }
}