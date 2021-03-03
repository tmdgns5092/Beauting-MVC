package kr.co.serinusSM.service;

import kr.co.serinusSM.dao.ServicesDAO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("servicesService")
public class ServicesService {
    Logger log = Logger.getLogger(this.getClass());

    @Resource(name="servicesDAO")
    private ServicesDAO servicesDAO;

    /*  Service(서비스) Service
     *   Prepaid(선불권) Service
     *   Product(제품) Service
     *   Ticket(티켓) Service   */

    /* Services
     *  Kind :: Service = 0, Prepaid = 1, Product = 2, Ticket = 3
     *  Shop_id :: 매장구분.
     * */

    // 카테고리 추가
    public void insertServicesCategory(Map<String, Object> map) throws Exception { servicesDAO.insertServicesCategory(map); }
    // 서비스 순서 업데이트
    public void updateServiceProcedureUpdate(Map<String, Object> map) throws Exception { servicesDAO.updateServiceProcedureUpdate(map); }
    // 카테고리 수정
    public void updateServicesCategoryAjax(Map<String,Object> map) throws Exception {
        servicesDAO.updateServicesCategoryAjax(map);
    }
    // 삭제할 카테고리의 디테일 검색
    public List<Map<String, Object>> selectDeleteServicesLists(Map<String, Object> map) throws Exception{
        return servicesDAO.selectDeleteServicesLists(map);
    }
    // 삭제할 카테고리의 디테일 삭제
    public void deleteAllServicesDetailAjax(Map<String,Object> map) throws Exception {
        servicesDAO.deleteAllServicesDetailAjax(map);
    }
    // 카테고리 삭제
    public void deleteServicesCategoryAjax(Map<String,Object> map) throws Exception {
        servicesDAO.deleteServicesCategoryAjax(map);
    }
    // Services 전체검색
    public List<Map<String, Object>> selectServicesLists(Map<String, Object> map) throws Exception{
        return servicesDAO.selectServicesLists(map);
    }
    // 카테고리 검색
    public List<Map<String, Object>> selectServicesCategoryLists(Map<String, Object> map) throws Exception{
        return servicesDAO.selectServicesCategoryLists(map);
    }
    // 디테일 추가
    public void insertServicesDetail(Map<String, Object> map) throws Exception {
        servicesDAO.insertServicesDetail(map);
    }
    // 디테일 검색
    public List<Map<String, Object>> selectServicesDetailLists(Map<String, Object> map) throws Exception{
        return servicesDAO.selectServicesDetailLists(map);
    }
    // 디테일 수정
    public void updateServicesDetailAjax(Map<String,Object> map) throws Exception {
        servicesDAO.updateServicesDetailAjax(map);
    }
    // 디테일 삭제
    public void deleteServicesDetailAjax(Map<String,Object> map) throws Exception {
        servicesDAO.deleteServicesDetailAjax(map);
    }
    // 프로시더 업데이트
    public void updateServicesProcedure(Map<String, Object> map) throws Exception{
        servicesDAO.updateServicesProcedure(map);
    }

}