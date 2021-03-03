package kr.co.serinusSM.controller;

import com.google.gson.Gson;
import com.popbill.api.MessageService;
import com.popbill.api.message.MessageBriefInfo;
import com.popbill.api.message.SentMessage;
import kr.co.serinusSM.common.CommonUtils;
import kr.co.serinusSM.common.Paging;
import kr.co.serinusSM.common.background.AsyncConfig;
import kr.co.serinusSM.common.background.AsyncTaskService;
import kr.co.serinusSM.service.MessagesService;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("Messages")
public class MessagesController {
    Logger log = Logger.getLogger(this.getClass());

    @Resource(name = "messagesService")
    private MessagesService messagesService;
    @Resource(name = "commonUtils")
    private CommonUtils commonUtils;
    @Resource(name = "asyncTaskService")
    private AsyncTaskService asyncTaskService;
    @Resource(name = "asyncConfig")
    private AsyncConfig asyncConfig;
//    @Autowired
    private MessageService messageService;

    public MessageBriefInfo[] getStates(List<Map<String, Object>> mList, String corpNum) {
        log.debug("getState()");
        MessageBriefInfo[] messageBriefInfos = null;
        try {
            String[] receiptNums = new String[mList.size()];
            for (int i = 0; i < mList.size(); i++) receiptNums[i] = mList.get(i).get("receipt_num").toString();
            messageBriefInfos = messageService.getStates(corpNum, receiptNums);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageBriefInfos;
    }

    public SentMessage[] getMessages(String receiptNum, String corpNum) {
        SentMessage[] sentMessages = null;
        try {
            sentMessages = messageService.getMessages(corpNum, receiptNum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sentMessages;
    }

    public List<Map<String, Object>> readMsgSample(String url, Gson gson) {
        List<Map<String, Object>> sample = null;
        try {
            FileInputStream fis = new FileInputStream(new File(url));
            byte[] buffer = new byte[fis.available()];
            while (fis.read(buffer) > 0) {
            }
            String txt = new String(buffer);
            if (txt.length() > 0) sample = gson.fromJson(txt, List.class);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                FileOutputStream op = new FileOutputStream(url);
                op.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return sample;
    }

    /* 문자 페이지 */
    @RequestMapping("message")
    public ModelAndView message(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        ModelAndView mv = new ModelAndView("message/message");
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        Gson gson = new Gson();
        log.debug("message: " + paramMap);
        String url = "/var/lib/tomcat8/webapps/msgSample/";
//        String url = "d:\\";
//         /var/lib/tomcat8/webapps/msgSample/(id)+_msg(type)Sample.txt
        List<Map<String, Object>> sms_sample = readMsgSample(url + sMap.get("id") + "_smsSample.txt", gson);
        if (sms_sample != null) mv.addObject("my_sms", sms_sample);
        List<Map<String, Object>> lms_sample = readMsgSample(url + sMap.get("id") + "_lmsSample.txt", gson);
        if (lms_sample != null) mv.addObject("my_lms", lms_sample);



        try {
            paramMap.put("shop_id", sMap.get("id"));

            Map<String, Object> cMap = messagesService.selectAllClientCount(paramMap);
            log.debug("prepaid: " + cMap.get("prepaid"));
            /* paging */
            Paging paging = new Paging();
            int totalCount = Integer.parseInt(cMap.get("count").toString());            // list size
            int pageNo = 1;                                                             // pageNunber
            if (paramMap.get("pageNo") != null) {                                         // param pageNumber check
                String paramPageNo = paramMap.get("pageNo").toString();                 // get pageNumber
                pageNo = Integer.parseInt(paramPageNo);
            }
            paging.setPageNo(pageNo);                                                   // sql start index set
            paging.setTotalCount(totalCount);                                           // sql end index set
            /* /. paging */

            paramMap.put("start_idx", paging.getStartIndex());
            paramMap.put("end_idx", paging.getPageSize());

            List<Map<String, Object>> cList = messagesService.selectAllClientPaging(paramMap);
            mv.addObject("totalClient", totalCount);
            mv.addObject("cList", cList);
            mv.addObject("paging", paging);
            mv.addObject("filterValues", paramMap);
            if (paramMap.containsKey("individual-check-array")) {
                List<Map<String, Object>> iList = gson.fromJson(paramMap.get("individual-check-array").toString(), List.class);
                JSONArray iArray = CommonUtils.convertListToJson(iList);
                mv.addObject("individualArray", iArray);
            } else
                mv.addObject("individualArray", new ArrayList<Object>());
            if (paramMap.containsKey("exception-check-array")) {
                List<Map<String, Object>> eList = gson.fromJson(paramMap.get("exception-check-array").toString(), List.class);
                JSONArray eArray = CommonUtils.convertListToJson(eList);
                mv.addObject("exceptionArray", eArray);
            } else
                mv.addObject("exceptionArray", new ArrayList<Object>());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("mv : " + mv);
        log.debug("mv : " + mv);

        return mv;
    }

    /* 문자 발송내역 페이지 */
    @RequestMapping("messageList")
    public ModelAndView messageList(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        ModelAndView mv = new ModelAndView("message/messageList");
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        Paging paging = new Paging();
        paramMap.put("id", sMap.get("id"));

        String toDay = commonUtils.setForDate(paramMap);
        /* ParamMap Setting */
        if (!paramMap.containsKey("sort")) paramMap.put("sort", "");
        if (!paramMap.containsKey("method")) paramMap.put("method", "all");
        if (!paramMap.containsKey("target_count")) paramMap.put("target_count", "0");
        if (!paramMap.containsKey("startDate"))
            paramMap.put("startDate", toDay.substring(0, toDay.lastIndexOf('-')) + "-01");
        if (!paramMap.containsKey("endDate")) paramMap.put("endDate", toDay);

        log.debug("messageList : " + paramMap);

        Map<String, Object> mMap;
        try {
            mMap = messagesService.selectAllMessageList(paramMap);
        } catch (Exception e) {
            mMap = new HashMap<>();
            mMap.put("count", 0);
            e.printStackTrace();
        }

        /* Count 0 */
        if (Integer.parseInt(mMap.get("count").toString()) < 1 || !sMap.containsKey("com_num")) {
            mv.addObject("size", 0);
        }
        /* Count > 0 */
        else {
            int totalCount = Integer.parseInt(mMap.get("count").toString());            // list size
            int pageNo = 1;                                                             // pageNunber
            if (paramMap.containsKey("pageNo")) {                                       // param pageNumber check
                String paramPageNo = paramMap.get("pageNo").toString();                 // get pageNumber
                pageNo = Integer.parseInt(paramPageNo);
            }
            paging.setPageNo(pageNo);
            paging.setTotalCount(totalCount);
            paramMap.put("start_idx", paging.getStartIndex());                          // sql start index set
            paramMap.put("end_idx", paging.getPageSize());                              // sql end index set

            List<Map<String, Object>> mList = null;
            try {
                mList = messagesService.selectSortingAndPagingMessages(paramMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*MessageBriefInfo[] messageBriefInfos;
            if(mList != null && mList.size() > 0) messageBriefInfos = getStates(mList, sMap.get("com_num").toString());
            else messageBriefInfos = new MessageBriefInfo[0];

            int j = 0;
            String sendTime = "";
            *//* select List 가 없으면 실행안되서 while 신경안써도 됨 *//*
            for (int i = 0; i < mList.size(); i++) {
                int sendCount = 0, success = 0, fail = 0, wait = 0;
                String receiptNum = mList.get(i).get("receipt_num").toString();
                log.debug("receiptNum : " + receiptNum);
                while (j < messageBriefInfos.length) {
                    MessageBriefInfo result = messageBriefInfos[j];
                    log.debug("result.getrNum : " + result.getrNum());
                    if (receiptNum.equals(result.getrNum())) {
                        sendTime = result.getsDT();
                        sendCount++;
                        if (result.getStat().equals("3")) {
                            if (result.getRlt().equals("100")) success++;
                            else fail++;
                        } else wait++;
                        j++;
                    } else break;
                }
                mList.get(i).put("sendTime", sendTime);
                mList.get(i).put("sendCount", sendCount);
                mList.get(i).put("success", success);
                mList.get(i).put("fail", fail);
                mList.get(i).put("wait", wait);
            }*/
            JSONArray jsonMSG = CommonUtils.convertListToJson(mList);
            mv.addObject("size", mMap.get("count"));
            mv.addObject("messageList", mList);
            mv.addObject("jsonMSG", jsonMSG);

            /* 페이징 */
            mv.addObject("paging", paging);
        }
        mv.addObject("map", paramMap);
        return mv;
    }

    /* 문자 상세 */
    @RequestMapping("messageDetail")
    public ModelAndView messageDetail(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        ModelAndView mv = new ModelAndView("message/messageDetail");
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        Paging paging = new Paging();
        Gson gson = new Gson();
        log.debug("messageDetail: " + paramMap);
        try {
            SentMessage[] sentMessages = getMessages(paramMap.get("receipt_num").toString(), sMap.get("com_num").toString());
            List<Map<String, Object>> sm;
            log.debug(gson.toJson(sentMessages));
            boolean fail; // 실패건만 확인
            if (!paramMap.containsKey("fail")) fail = false;
            else fail = Boolean.parseBoolean(paramMap.get("fail").toString());
            if (fail) {
                sm = new ArrayList<>();
                for (SentMessage item : sentMessages) {
                    if (item.getResult() != 100) {
                        Map<String, Object> map = gson.fromJson(gson.toJson(item), Map.class);
                        sm.add(map);
                    }
                }
            } else sm = gson.fromJson(gson.toJson(sentMessages), List.class);
            int totalCount = sm.size();
            int pageNo = 1;
            if (paramMap.containsKey("pageNo")) pageNo = Integer.parseInt(paramMap.get("pageNo").toString());

            paging.setPageNo(pageNo);
            paging.setTotalCount(totalCount);
            mv.addObject("sentMessages", sm);
            mv.addObject("paging", paging);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    /* 문자 자동발송 페이지 */
    @RequestMapping("messageAuto")
    public ModelAndView messageAuto(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        ModelAndView mv = new ModelAndView("message/messageAuto");
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        log.debug("messageAuto: " + paramMap);

        List<Map<String, Object>> autoList = messagesService.selectAutoMSGSettings(sMap);
        List<Map<String, Object>> serviceList14 = messagesService.selectServiceCategory14(sMap);
        List<Map<String, Object>> serviceList15 = messagesService.selectServiceCategory15(sMap);

        mv.addObject("autoList", autoList);
        mv.addObject("cateList14", serviceList14);
        mv.addObject("cateList15", serviceList15);
        return mv;
    }

    /* ////////////////////////////// Return Objects Method ////////////////////////////// */

    /* 문자 발송시 이력 DB에 저장 */
    @RequestMapping("insertMessageData")
    @ResponseBody
    public Object insertMessageData(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        log.debug("insertMessageData: " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        paramMap.put("id", sMap.get("id"));
        try {
            messagesService.insertMessageData(paramMap);
            if (paramMap.containsKey("ID")) {
                jobj.put("code", 200);
            } else {
                jobj.put("code", 902);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 900);
            jobj.put("e", e);
        }
        return jobj;
    }

    /* 저장된 문자 자동발송 정보 조회 */
    @RequestMapping("selectAutoMessage")
    @ResponseBody
    public Object selectAutoMessage(@RequestParam Map<String, Object> paramMap, HttpSession session) {
//        log.debug("selectAutoMessage: " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        paramMap.put("shop_idx", sMap.get("idx"));
        try {
            Map<String, Object> aMap = messagesService.selectAutoMessage(paramMap);
            if (aMap != null) {
                jobj.put("code", 200);
                jobj.put("autoMessage", aMap);
            } else {
                jobj.put("code", 902);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 900);
            jobj.put("e", e);
        }
        return jobj;
    }

    /* 문자 발송 */
    @RequestMapping("messageSendTask")
    @ResponseBody
    public Object taskTest(/*@RequestParam Map<String, Object> paramMap,*/ HttpSession session, MultipartHttpServletRequest mtfRequest) {
//        log.debug("taskTest: " + paramMap);
//        String path = "C:/dev/";
        String path = "/var/lib/tomcat8/webapps/imgRepos/";

        log.debug("files :  " + mtfRequest);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        Gson gson = new Gson();
        MultipartFile report = null;
        String fileName = "";
        File mmlFile = null;

        Map<String, Object> paramMap = gson.fromJson(mtfRequest.getParameter("data"), Map.class);

        if("mms".equals(paramMap.get("send_method").toString())){
            report = mtfRequest.getFile("mmsImg");

            try{
                fileName = report.getOriginalFilename();

                mmlFile = new File(path + report.getOriginalFilename());
                mmlFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(mmlFile);
                fos.write(report.getBytes());
                fos.close();
            } catch (Exception e){
                e.printStackTrace();
                return jobj.put("code", "905");
            }
        }

        try {
            // 스레드 빈자리 확인 (max 200)
            if (asyncConfig.checkSampleTaskExecute()) {
                List<Map<String, Object>> senderList = gson.fromJson(paramMap.get("type_client_array").toString(), List.class);
                paramMap.put("id", sMap.get("id"));
                paramMap.put("senderList", senderList);
                if(senderList.size() > 0){
                    paramMap.put("senderListFlag", "true");
                }
                else {
                    paramMap.put("senderListFlag", "false");
                }

                asyncTaskService.jobRunningInBackground(paramMap, sMap, messageService, mmlFile, fileName, path, session);
                jobj.put("code", 200);
            } else {
                System.out.println("Thread 개수 초과");
                jobj.put("code", 413);
            }
        } catch (Exception e) {
            System.out.print("message controller exception :: ");
            e.printStackTrace();
            System.out.println("Thread Err :: " + e.getMessage());
            jobj.put("code", 900);
        }

        return jobj;
    }

    /* 자동문자 설정 저장 */
    @RequestMapping("autoMessageSave")
    @ResponseBody
    public Object autoMessageSave(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        log.debug("autoMessageSave: " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        JSONObject jobj = new JSONObject();
        Gson gson = new Gson();
        paramMap.put("idx", sMap.get("idx"));
        paramMap.put("id", sMap.get("id"));

        try {
            // 시, 분
            if ("1".equals(paramMap.get("type").toString()) ||
                    "2".equals(paramMap.get("type").toString()) ||
                    "3".equals(paramMap.get("type").toString())) {

                System.out.println("================ type 1,2,3 ================");
                messagesService.updateAutoMessageType1(paramMap);
            }
            // 즉시
            else if ("4".equals(paramMap.get("type").toString()) ||
                    "5".equals(paramMap.get("type").toString())) {
                System.out.println("================ type 4,5 ================");
                messagesService.updateAutoMessageType2(paramMap);
            }
            // 분
            else if ("6".equals(paramMap.get("type").toString()) ||
                    "7".equals(paramMap.get("type").toString()) ||
                    "8".equals(paramMap.get("type").toString()) ||
                    "9".equals(paramMap.get("type").toString()) ||
                    "11".equals(paramMap.get("type").toString()) ||
                    "12".equals(paramMap.get("type").toString())) {
                System.out.println("================ type 6,7,8,9,11,12 ================");
                messagesService.updateAutoMessageType3(paramMap);
            }
            /* 하위 */
            else if ("10".equals(paramMap.get("type").toString()) ||
                    "13".equals(paramMap.get("type").toString())) {
                System.out.println("================ type 10,13 ================");
                List<Map<String, Object>> list = gson.fromJson(paramMap.get("array").toString(), List.class);
                CommonUtils.printList(list);
                paramMap.put("array", list);
                messagesService.updateAutoMessageType4(paramMap);
            } else if ("14".equals(paramMap.get("type").toString()) || "15".equals(paramMap.get("type").toString())) {
                System.out.println("================ type 14,15 ================");
                List<Map<String, Object>> list = gson.fromJson(paramMap.get("array").toString(), List.class);
                CommonUtils.printList(list);
                paramMap.put("array", list);
                messagesService.updateAutoMessageType5(paramMap);
            } else if ("16".equals(paramMap.get("type").toString())) {
                System.out.println("================ type 16 ================");
                messagesService.updateAutoMessageType6(paramMap);
            }
            jobj.put("code", 200);
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 900);
        }
        return jobj;
    }

    /* 매장별 문자 템플릿 저장 */
    @RequestMapping("myMessageSave")
    @ResponseBody
    public Object myMessageSave(@RequestParam Map<String, Object> paramMap, HttpSession session) {
        JSONObject jobj = new JSONObject();
        log.debug("autoMessageSave: " + paramMap);
        Map<String, Object> sMap = (Map<String, Object>) session.getAttribute("shopInfo");
        try {
            String type = paramMap.get("type").toString();
            if (type.indexOf("sms") == -1 && type.indexOf("lms") == -1) {
                jobj.put("code", 902);
                jobj.put("error", "type is fail");
                return jobj;
            }
            String url = "/var/lib/tomcat8/webapps/msgSample/" + sMap.get("id") + "_" + type + "Sample.txt";
            List<Map<String, Object>> conf = new Gson().fromJson(paramMap.get("txt").toString(), List.class);
            if (conf.size() > 0) {
                for (Map<String, Object> item : conf) {
                    for (String msgType : item.keySet()) {
                        if (msgType.equals("sms")) {
                            continue;
                        } else if (msgType.equals("lms")) {
                            continue;
                        } else {
                            jobj.put("code", 902);
                            jobj.put("error", "Key is not [sms, lms]");
                            return jobj;
                        }
                    }
                }
            }
            FileOutputStream op = new FileOutputStream(url);
            String txt = paramMap.get("txt").toString();
            op.write(txt.getBytes());
            op.close();
            jobj.put("code", 200);
        } catch (Exception e) {
            e.printStackTrace();
            jobj.put("code", 900);
            jobj.put("error", e);
        }
        return jobj;
    }
}
