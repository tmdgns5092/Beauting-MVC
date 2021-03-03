package kr.co.serinusSM.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("scheduleDAO")
public class ScheduleDAO extends AbstractDAO {
    Logger log = Logger.getLogger(this.getClass());

    public List<Map<String, Object>> employeeSelect(Map<String, Object> map) throws Exception{ return (List<Map<String, Object>>) selectList("schedule.employeeSelect", map); }

    public List<Map<String, Object>> scheduleSelect(Map<String, Object> map) throws Exception{ return (List<Map<String, Object>>) selectList("schedule.scheduleSelect", map);}

    public void insertScheduleAjax(Map<String, Object> map) throws Exception {insert("schedule.insertScheduleAjax", map);}

    public void deleteScheduleAjax(Map<String,Object> map) throws Exception {delete("schedule.deleteScheduleAjax", map);}

    public void insertHolidayScheduleAjax(Map<String,Object> map) {insert("schedule.insertHolidayScheduleAjax", map);}

    public void updateScheduleAjax(Map<String, Object> map) throws Exception {insert("schedule.updateScheduleAjax", map);}

    public Map<String,Object> selectBlockDate(Map<String,Object> map) { return (Map<String, Object>) selectOne("schedule.selectBlockDate", map); }
}