package kr.co.serinusSM.service;

import kr.co.serinusSM.dao.ScheduleDAO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("scheduleService")
public class ScheduleService {
    Logger log = Logger.getLogger(this.getClass());

    @Resource(name="scheduleDAO")
    private ScheduleDAO scheduleDAO;

    public List<Map<String, Object>> employeeSelect(Map<String, Object> map) throws Exception{ return scheduleDAO.employeeSelect(map); }

    public List<Map<String, Object>> scheduleSelect(Map<String, Object> map) throws Exception{ return scheduleDAO.scheduleSelect(map); }

    public void insertScheduleAjax(Map<String, Object> map) throws Exception{ scheduleDAO.insertScheduleAjax(map); }

    public void updateScheduleAjax(Map<String, Object> map) throws Exception{ scheduleDAO.updateScheduleAjax(map); }

    public void deleteScheduleAjax(Map<String,Object> map) throws Exception { scheduleDAO.deleteScheduleAjax(map); }

    public void insertHolidayScheduleAjax(Map<String,Object> map) { scheduleDAO.insertHolidayScheduleAjax(map); }

    public Map<String,Object> selectBlockDate(Map<String,Object> map) { return scheduleDAO.selectBlockDate(map); }
}