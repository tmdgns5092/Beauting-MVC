package kr.co.serinusSM.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("commonDAO")
public class CommonDAO extends AbstractDAO {

	Logger log = Logger.getLogger(this.getClass());

	/* 매장 로그인 체크 */
	public Map<String, Object> loginCheckAjax(Map<String, Object> map) { return (Map<String, Object>)selectOne("common.loginCheckAjax", map); }
	public Map<String, Object> autoLoginCheck(Map<String, Object> map) { return (Map<String, Object>)selectOne("common.loginCheckAjax", map); }
	/* 원장 아이디 중복 확인 */
	public Map<String, Object> overlapCheckFromManager(Map<String, Object> map) { return (Map<String, Object>)selectOne("common.overlapCheckFromManager", map); }
	/* 원장 회원가입 */
	public void insertManager(Map<String, Object> map) { insert("common.insertManager", map); }
	/* 매장 아이디 중복 확인 */
	public Map<String, Object> overlapShopCheckFromIdAjax(Map<String, Object> map) { return (Map<String, Object>)selectOne("common.overlapShopCheckFromIdAjax", map); }
	/* 매장 추가 */
	public void insertShop(Map<String, Object> map ){ insert("common.insertShop", map); }
	/* create OOO_client */
	public void createClient (Map<String, Object> map){ insert("common.createClient", map); }
	/* create OOO_MSGHistory */
	public void createMSGHistory(Map<String, Object> map){ insert("common.createMSGHistory", map); }
	/* create OOO_reservation */
	public void createReservation(Map<String, Object> map){ insert("common.createReservation", map); }
	/* create OOO_sales*/
	public void createSales(Map<String, Object> map){ insert("common.createSales", map); }
	/* create OOO_schedule*/
	public void createSchedule(Map<String, Object> map){ insert("common.createSchedule", map); }
	/* create OOO_call_history*/
	public void createCallHistory(Map<String,Object> map) { insert("common.createCallHistory", map); }
	/* default services list insert */
	public void insertDefaultServiceCategory1(Map<String, Object> map) { insert("common.insertDefaultServiceCategory1", map);	} // 카테고리 손 추가
	public void insertDefaultServiceCategory2(Map<String, Object> map) { insert("common.insertDefaultServiceCategory2", map);	} // 카테고리 발 추가
	public void insertDefaultServiceCategory3(Map<String, Object> map) { insert("common.insertDefaultServiceCategory3", map);	} // 카테고리 추가 추가

	public void insertDefaultServiceDetail1_1(Map<String, Object> map) { insert("common.insertDefaultServiceDetail1-1", map);	} // 디테일 손 추가
	public void insertDefaultServiceDetail1_2(Map<String, Object> map) { insert("common.insertDefaultServiceDetail1-2", map);	}
	public void insertDefaultServiceDetail1_3(Map<String, Object> map) { insert("common.insertDefaultServiceDetail1-3", map);	}
	public void insertDefaultServiceDetail1_4(Map<String, Object> map) { insert("common.insertDefaultServiceDetail1-4", map);	}
	public void insertDefaultServiceDetail1_5(Map<String, Object> map) { insert("common.insertDefaultServiceDetail1-5", map);	}

	public void insertDefaultServiceDetail2_1(Map<String, Object> map) { insert("common.insertDefaultServiceDetail2-1", map);	} // 디테일 발 추가
	public void insertDefaultServiceDetail2_2(Map<String, Object> map) { insert("common.insertDefaultServiceDetail2-2", map);	}
	public void insertDefaultServiceDetail2_3(Map<String, Object> map) { insert("common.insertDefaultServiceDetail2-3", map);	}
	public void insertDefaultServiceDetail2_4(Map<String, Object> map) { insert("common.insertDefaultServiceDetail2-4", map);	}
	public void insertDefaultServiceDetail2_5(Map<String, Object> map) { insert("common.insertDefaultServiceDetail2-5", map);	}

	public void insertDefaultServiceDetail3_1(Map<String, Object> map) { insert("common.insertDefaultServiceDetail3-1", map);	} // 디테일 추가 추가
	public void insertDefaultServiceDetail3_2(Map<String, Object> map) { insert("common.insertDefaultServiceDetail3-2", map);	}
	public void insertDefaultServiceDetail3_3(Map<String, Object> map) { insert("common.insertDefaultServiceDetail3-3", map);	}
	public void insertDefaultServiceDetail3_4(Map<String, Object> map) { insert("common.insertDefaultServiceDetail3-4", map);	}
	public void insertDefaultServiceDetail3_5(Map<String, Object> map) { insert("common.insertDefaultServiceDetail3-5", map);	}
	public void insertDefaultServiceDetail3_6(Map<String, Object> map) { insert("common.insertDefaultServiceDetail3-6", map);	}
	public void insertDefaultServiceDetail3_7(Map<String, Object> map) { insert("common.insertDefaultServiceDetail3-7", map);	}
	public void insertDefaultServiceDetail3_8(Map<String, Object> map) { insert("common.insertDefaultServiceDetail3-8", map);	}
	public void insertDefaultServiceDetail3_9(Map<String, Object> map) { insert("common.insertDefaultServiceDetail3-9", map);	}

	public void insertDefaultCateCustom(Map<String, Object> map) { insert("common.insertDefaultCateCustom", map);	}
	public void insertDefaultDetailCustom(Map<String, Object> map) { insert("common.insertDefaultDetailCustom", map);	}

	public void updateServiceProcedureUpdate(Map<String, Object> map) { update("services.updateServiceProcedureUpdate", map);	} // procedure int update
	/* -------------------------------------------------------------------------------------------------------------- */

	/* insert chat */
    public void insertChat(Map<String,Object> map) {
		insert("common.insertChat", map);
    }
	/* select chat */
	public List<Map<String, Object>> selectChat(Map<String,Object> map) { return (List<Map<String, Object>>) selectList("common.selectChat", map); }
	/* delete chat */
	public void deleteChat(Map<String,Object> map) {
		insert("common.deleteChat", map);
	}
	/* 사업자번호 중복 확인 */
	public List<Map<String, Object>> selectComNumberOverCheckFromManager(Map<String, Object> map){ return (List<Map<String, Object>>) selectList("common.selectComNumberOverCheckFromManager", map); }
	/* 매장 오픈 */
	public void updateShopOpen(Map<String, Object> map){ update("common.updateShopOpen", map);}
	/* 매장 마감 */
	public void updateShopClose(Map<String, Object> map){ update("common.updateShopClose", map);}
	/* 매장 분단위 재설정 */
	public void updateShopDefaultMinute(Map<String, Object> map){ update("common.updateShopDefaultMinute", map);}
	/* 모든 스케쥴 미사용 업데이트 */
	public void updateEmployeeAllSchedule(Map<String, Object> map){
		update("common.updateEmployeeAllSchedule", map);
		update("common.deleteEmployeeAllSchedule", map);
	}
	/* 매장 운영시간 업데이트 */
	public void updateOperationgTime(Map<String, Object> map){ update("common.updateOperationgTime", map);}
	/* 사업자 등록번호 업데이트 */
	public void updateComNum(Map<String, Object> map){ update("common.updateComNum", map);}
	/* 문자 포인트 업데이트 */
	public void updateMsgPoint(Map<String, Object> map){ update("common.updateMsgPoint", map);}
	/* 이메일 업데이트 */
	public void shopEmailUpdate(Map<String, Object> map){ update("common.shopEmailUpdate", map);}
	/* 사업자번호 중복허용 체크 */
	public List<Map<String, Object>> selectOverlapComnumCheck(Map<String, Object> map) { return (List<Map<String, Object>>)selectList("common.selectOverlapComnumCheck", map);}
	/* 발신자 등록으로 인한 매장 데이터 업데이트 */
	public void updateShopDataFromSender(Map<String, Object> map){ update("common.updateShopDataFromSender", map);}
	/* 고객 정보 조회 */
    public Map<String, Object> selectClientInfo(Map<String, Object> map) { return (Map<String, Object>)selectOne("common.selectClientInfo", map); }

    public void insertCallHistory(Map<String,Object> map) { insert("common.insertCallHistory", map); }
    /* 전화 수신내역 토탈 카운트 */
    public Map<String,Object> selectCallHistoryCount(Map<String,Object> map) { return (Map<String, Object>)selectOne("common.selectCallHistoryCount", map); }
	/* 전화 수신내역 리스트 페이징 */
	public List<Map<String,Object>> selectCallHistoryList(Map<String,Object> map) { return (List<Map<String,Object>>)selectList("common.selectCallHistoryList", map); }
	/* 전화수신내역 고객 수정 */
	public void updateClientState(Map<String,Object> map) { update("common.updateClientState", map); }

    public void insertPayer_ID(Map<String,Object> map) { update("common.insertPayer_ID", map); }

	public void deletePayer_ID(Map<String,Object> map) { update("common.deletePayer_ID", map); }

	public void insertPayment(Map<String,Object> map) { insert("common.insertPayment", map); }

	public Map<String, Object> selectShopInfo(Map<String, Object> map){ return (Map<String, Object>) selectOne("common.selectShopInfo", map);}

	public List<Map<String, Object>> selectReservation(Map<String, Object> map){ return (List<Map<String, Object>>) selectList("common.selectReservation", map);}
}
