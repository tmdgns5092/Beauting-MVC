package kr.co.serinusSM.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("servicesDAO")
public class ServicesDAO extends AbstractDAO {
    Logger log = Logger.getLogger(this.getClass());

    /* Services
     *  Kind :: Service = 0, Prepaid = 1, Product = 2, Ticket = 3
     *  Shop_id :: 매장구분.
     * */

    // 카테고리 추가
    public void insertServicesCategory(Map<String, Object> map) throws Exception { insert("services.insertServicesCategory", map); }
    // 서비스 순서 업데이트
    public void updateServiceProcedureUpdate(Map<String, Object> map) throws Exception { update("services.updateServiceProcedureUpdate", map); }
    // Services 전체검색
    public List<Map<String, Object>> selectServicesLists(Map<String, Object> map) throws Exception{
        return selectList("services.selectServicesLists", map);
    }
    // 카테고리 검색
    public List<Map<String, Object>> selectServicesCategoryLists(Map<String, Object> map) throws Exception{
        return selectList("services.selectServicesCategoryLists", map);
    }
    // 카테고리 수정
    public void updateServicesCategoryAjax(Map<String,Object> map) throws Exception {
        update("services.updateServicesCategoryAjax", map);
    }
    // 삭제할 카테고리의 디테일 검색
    public List<Map<String, Object>> selectDeleteServicesLists(Map<String, Object> map) throws Exception{
        return selectList("services.selectDeleteServicesLists", map);
    }
    // 삭제할 카테고리의 디테일 삭제
    public void deleteAllServicesDetailAjax(Map<String,Object> map) throws Exception {
        delete("services.deleteAllServicesDetailAjax", map);
    }
    // 카테고리 삭제
    public void deleteServicesCategoryAjax(Map<String,Object> map) throws Exception {
        delete("services.deleteServicesCategoryAjax", map);
    }
    // 디테일 추가
    public void insertServicesDetail(Map<String, Object> map) throws Exception {
        insert("services.insertServicesDetail", map);
    }
    // 디테일 검색
    public List<Map<String, Object>> selectServicesDetailLists(Map<String,Object> map) throws Exception{
        return selectList("services.selectServicesDetailLists", map);
    }
    // 디테일 수정
    public void updateServicesDetailAjax(Map<String,Object> map) throws Exception {
        update("services.updateServicesDetailAjax", map);
    }
    // 디테일 삭제
    public void deleteServicesDetailAjax(Map<String,Object> map) throws Exception {
        delete("services.deleteServicesDetailAjax", map);
    }
    // 프로시더 업데이트
    public void updateServicesProcedure(Map<String, Object> map) throws Exception{
        update("services.updateServicesProcedure", map);
    }
}