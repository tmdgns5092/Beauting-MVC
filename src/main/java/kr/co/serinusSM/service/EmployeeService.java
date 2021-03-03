package kr.co.serinusSM.service;

import kr.co.serinusSM.dao.EmployeeDAO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("employeeService")
public class EmployeeService {
    Logger log = Logger.getLogger(this.getClass());

    @Resource(name="employeeDAO")
    private EmployeeDAO employeeDAO;

    /* 직원 리스트 가져오기 */
    public List<Map<String, Object>> selectAllEmpl(Map<String, Object> map) { return employeeDAO.selectAllEmpl(map); }
    /* 직원 코드 생성 */
    public Map<String, Object> selectEmplLargestNumber(Map<String, Object> map) { return employeeDAO.selectEmplLargestNumber(map); }
    /* 직원 등록 */
    public void insertNewEmployee(Map<String, Object> map) { employeeDAO.insertNewEmployee(map); }
    /* 직원 코드 중복 확인 */
    public Map<String, Object> selectOverlapFromEmplCodeFromAjax(Map<String, Object> map) { return employeeDAO.selectOverlapFromEmplCodeFromAjax(map); }
    /* 직원 정보 수정 */
    public void updateModifiedEmplFromAjax(Map<String, Object> map) { employeeDAO.updateModifiedEmplFromAjax(map); }
    /* 직원 순서 변경 */
    public void updateEmployeeProcedure(Map<String, Object> map) { employeeDAO.updateEmployeeProcedure(map); }
    /* 직원 목표금액 가져오기 */
    public Map<String,Object> selectEmplGoalData(Map<String,Object> map) { return employeeDAO.selectEmplGoalData(map); }
    /* 직원 목표금액 등록하기 */
    public void employeeGoalInsert(Map<String,Object> map) { employeeDAO.employeeGoalInsert(map); }
    /* 직원 목표금액 수정하기 */
    public void employeeGoalUpdate(Map<String,Object> map) { employeeDAO.employeeGoalUpdate(map); }
}
