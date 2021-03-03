package kr.co.serinusSM.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("employeeDAO")
public class EmployeeDAO extends AbstractDAO {
    Logger log = Logger.getLogger(this.getClass());

    /* 직원 리스트 가져오기 */
    public List<Map<String, Object>> selectAllEmpl(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("employee.selectAllEmpl", map); }
    /* 직원 코드 생성 */
    public Map<String, Object> selectEmplLargestNumber(Map<String, Object> map) { return (Map<String, Object>) selectOne("employee.selectEmplLargestNumber", map); }
    /* 직원 등록 */
    public void insertNewEmployee(Map<String, Object> map) { insert("employee.insertNewEmployee", map); }
    /* 직원 번호 중복 확인 */
    public Map<String, Object> selectOverlapFromEmplCodeFromAjax(Map<String, Object> map) { return (Map<String, Object>) selectOne("employee.selectOverlapFromEmplCodeFromAjax", map); }
    /* 직원 정보 수정 */
    public void updateModifiedEmplFromAjax(Map<String, Object> map) { update("employee.updateModifiedEmplFromAjax", map); }
    /* 직원 순서 변경 */
    public void updateEmployeeProcedure(Map<String, Object> map) { update("employee.updateEmployeeProcedureThis", map); update("employee.updateEmployeeProcedureTarget", map); }

    public Map<String,Object> selectEmplGoalData(Map<String,Object> map) { return (Map<String, Object>) selectOne("employee.selectEmplGoalData", map); }
    /* 직원 목표금액 등록하기 */
    public void employeeGoalInsert(Map<String,Object> map) { update("employee.employeeGoalInsert", map); }
    /* 직원 목표금액 수정하기 */
    public void employeeGoalUpdate(Map<String,Object> map) { update("employee.employeeGoalUpdate", map); }
}
