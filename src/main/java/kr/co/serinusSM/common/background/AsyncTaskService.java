package kr.co.serinusSM.common.background;

import com.popbill.api.MessageService;
import com.popbill.api.message.Message;
import kr.co.serinusSM.common.CommonUtils;
import kr.co.serinusSM.service.MessagesService;
import org.json.simple.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("asyncTaskService")
public class AsyncTaskService {

    @Resource(name = "messagesService")
    private MessagesService messagesService;

    /*
    * Thread Process
    * */
    @Async
    public void jobRunningInBackground(Map<String, Object> param, Map<String, Object> sMap, MessageService messageService, File mmlFile, String fileName, String path, HttpSession session) {
        System.out.println("Thread Start ... ");
        int methodInt = 0;
        List<String> receiptNumList = new ArrayList<>();
        JSONObject jobj = new JSONObject();
        List<Map<String, Object>> clientList = new ArrayList<>();
        String reserveDT_String = null;


        // ********** 문자 발송을 위한 고객 리스트 만들기 **********
        /* 전체 발송 - (json array 제외) */

        // 확인용

        System.out.println("확인용 param print");
        CommonUtils.printMap(param);
        if("true".equals(param.get("all_check").toString())){
            Map<String, Object> countMap = messagesService.selectAllCheckClientCount(param);
            int clientCount = Integer.parseInt(countMap.get("clientCount").toString());
            if(clientCount > 1000){
                int loopCount = clientCount / 1000;
                int loopStep = 999;
                for(int i = 0 ; i < loopCount; i++){
                    param.put("loopStart", i * 1000);
                    param.put("loopEnd", loopStep);
                    clientList.addAll(messagesService.selectAllCheckClient(param));
                    loopStep += 1000;
                }
            } else {
                param.put("loopStart", 0);
                param.put("loopEnd", 1000);
                clientList = messagesService.selectAllCheckClient(param);
            }
        }
        /* 선택된 고객만 발송 - (json array 만 발송) */
        else {
            Map<String, Object> countMap = messagesService.selectNotAllCheckClientCount(param);
            int clientCount = Integer.parseInt(countMap.get("clientCount").toString());
            if(clientCount > 1000){
                int loopCount = Math.round(clientCount / 1000);
                int loopStep = 999;
                for(int i = 0 ; i < loopCount; i++){
                    param.put("loopStart", i * 1000);
                    param.put("loopEnd", loopStep);
                    clientList.addAll(messagesService.selectNotAllCheckClient(param));
                    loopStep += 1000;
                }
            } else {
                param.put("loopStart", 0);
                param.put("loopEnd", 1000);
                clientList = messagesService.selectNotAllCheckClient(param);
            }
        }

        Boolean adsYN = true;                            // 광고문자 전송여부
        Date reserveDT = null;                            // 예약전송일시, null 처리시 즉시전송
        Date cal = null;
        String receiptNum = "";

        // ********** 만들어진 고객 리스트로 문자 보내기 **********
        try {
            try{
                reserveDT_String = param.get("reserveDT").toString();
            } catch(Exception e){

            }
//            if("null".equals(param.get("reserveDT").toString()) || "".equals(param.get("reserveDT").toString()) || param.get("reserveDT") == null){
            if("null".equals(reserveDT_String) || "".equals(reserveDT_String) || reserveDT_String == null){
                reserveDT = null;
            } else {
                String from = param.get("reserveDT").toString();
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                cal = transFormat.parse(from);

                if(cal.compareTo(new Date()) > 0){
                    reserveDT = cal;
                }
            }

            try {
                int getListIndex = 0;
                int sendLoopCount = 1;
                int arrayLenth = 1000;
                // clientList 크기가 1000을 넘었을 경우
                if(clientList.size() > 1000) sendLoopCount = Math.round(clientList.size() / 1000);
                else {
                    arrayLenth = clientList.size();
                    sendLoopCount = 0;
                }


                System.out.println("array lenth : " + arrayLenth);
                for(int j = 0; j <= sendLoopCount; j++) {
                    int count = 0;
                    Message[] messages = new Message[arrayLenth];


                    List<Map<String, Object>> testList = new ArrayList<>();
                    // 발송할 문자 구성
                    for(int i = 0; i < arrayLenth; i++){
                        Message message = new Message();
                        String msgContent = param.get("content").toString();

                        message.setSender(param.get("sender_number").toString());
                        message.setReceiver(clientList.get(i + getListIndex).get("phone").toString());
                        message.setReceiverName(clientList.get(i + getListIndex).get("name").toString());
                        message.setContent(msgContent.replaceAll("\\(이름\\)", clientList.get(i + getListIndex).get("name").toString()));
                        messages[i] = message;
                        count ++;

                        Map<String, Object> testMap = new HashMap<>();
                        testMap.put("sender", param.get("sender_number").toString());
                        testMap.put("receiver", clientList.get(i + getListIndex).get("phone").toString());
                        testMap.put("receiverName", clientList.get(i + getListIndex).get("name").toString());
                        testMap.put("content", msgContent.replaceAll("\\(이름\\)", clientList.get(i + getListIndex).get("name").toString()));
                        testList.add(testMap);
                    }

                    System.out.println("==============================");
                    for(int a = 0; a < testList.size(); a++){
                        System.out.println("--------------------------------");
                        for(String key : testList.get(a).keySet()){
                            System.out.println(key + " : " + testList.get(a).get(key));
                        }
                        System.out.println("--------------------------------");
                    }
                    System.out.println("==============================");


                    // 문자 발송
                    if("sms".equals(param.get("send_method").toString())){
                        methodInt = 0;
                        receiptNum = messageService.sendSMS(sMap.get("com_num").toString(), messages, reserveDT, sMap.get("id").toString(), null);
//                        receiptNum = messageService.sendSMS(sMap.get("com_num").toString(), messages, reserveDT, "MansOfBeauty", null);
                    } else if("lms".equals(param.get("send_method").toString())) {
                        methodInt = 1;
                        receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), messages, reserveDT, sMap.get("id").toString(), null);
//                        receiptNum = messageService.sendLMS(sMap.get("com_num").toString(), messages, reserveDT, "MansOfBeauty", null);
                    } else if("mms".equals(param.get("send_method").toString())) {
                        methodInt = 2;
                        System.out.println("이미지 문자 전송 ... ");
                        receiptNum = messageService.sendMMS(sMap.get("com_num").toString(), messages, mmlFile, reserveDT, sMap.get("id").toString(), null);

                        /* 이미지 전송 후 파일 삭제 */
                        File file = new File(path + fileName);
                        if(file.exists() ){
                            if(file.delete()){
                                System.out.println("파일삭제 성공");
                            }else{
                                System.out.println("파일삭제 실패");
                            }
                        }else{
                            System.out.println("파일이 존재하지 않습니다.");
                        }
                    }
                    receiptNumList.add(receiptNum);

                    /* 문자 이력 저장 */
                    Map<String, Object> sqlParam = new HashMap<>();
                    sqlParam.put("id", sMap.get("id"));
                    sqlParam.put("method", methodInt);
                    sqlParam.put("send_count", count);
                    sqlParam.put("status", 0);
                    sqlParam.put("receipt_num", receiptNum);
                    messagesService.insertMessageData(sqlParam);

                    /* 문자 포인트 차감 */
                    sqlParam = new HashMap<>();
                    sqlParam.put("shopId", sMap.get("id"));
                    sqlParam.put("shopIdx", sMap.get("idx"));
                    if("sms".equals(param.get("send_method").toString())) sqlParam.put("subtractPoint", count * 14);
                    else if("lms".equals(param.get("send_method").toString())) sqlParam.put("subtractPoint", count * 34);
                    else if("mms".equals(param.get("send_method").toString())) sqlParam.put("subtractPoint", count * 150);
                    messagesService.updateDeductionMessagePointSubtract(sqlParam);

                    /* shopInfo 세션 업데이트 */
                    int shopOriginPoint = Integer.parseInt(sMap.get("msg_point").toString());
                    if("sms".equals(param.get("send_method").toString())) shopOriginPoint = shopOriginPoint - (count * 14);
                    else if("lms".equals(param.get("send_method").toString())) shopOriginPoint = shopOriginPoint - (count * 34);
                    else if("mms".equals(param.get("send_method").toString())) shopOriginPoint = shopOriginPoint - (count * 150);

                    sMap.put("msg_point", shopOriginPoint);
                    session.setAttribute("shopInfo", sMap);

                    getListIndex += 1000;
                    if(clientList.size() - getListIndex < 1000) arrayLenth = clientList.size() - getListIndex;
                    else arrayLenth = 1000;
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }

            jobj.put("Result",receiptNumList);
            jobj.put("code", 200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Thread End ...");
    }
}