package kr.co.serinusSM.common;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@Component("commonUtils")
public class CommonUtils {
	
	private static final Logger log = Logger.getLogger(CommonUtils.class);

	public static String getRandomString(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public String getResultText(int result){
		Map<String, Object> text = new HashMap<>();
		text.put("100", "문자전송 성공");
		text.put("200", "메시지 형식 오류");
		text.put("201", "문자 길이 오류");
		text.put("202", "MIME 형식 오류");
		text.put("203", "MMS 이미지 처리중 오류");
		text.put("204", "MMS 지원되지 않는 미디어 형식");
		text.put("205", "MMS 파일 확장자 오류");
		text.put("206", "MMS 파일 사이즈 오류");
		text.put("207", "MMS 파일 미존재");
		text.put("208", "기타 메세지 형식 오류");
		text.put("209", "컨텐츠 크기 초과");
		text.put("210", "메세지 크기 초과");
		text.put("211", "첨부파일 관련 오류");
		text.put("300", "발신번호 형식 오류");
		text.put("301", "발신번호 사전 미등록");
		text.put("302", "변작 신고 접수 발신 번호");
		text.put("303", "발신번호 도용차단 서비스 가입");
		text.put("401", "MMS 미지원 단말");
		text.put("402", "메세지 저장개수 초과");
		text.put("403", "단말기 음영지역, 전원꺼짐, 통신사 Time out");
		text.put("404", "핸드폰 꺼짐");
		text.put("405", "수신 불량지역에 위치");
		text.put("406", "핸드폰 호 처리중");
		text.put("407", "수신번호 서비스 중지");
		text.put("408", "SMS 수신불가 단말");
		text.put("409", "수신자가 발신번호 거부");
		text.put("410", "기타 단말기 오류");
		text.put("411", "착신 단말기 오류");
		text.put("415", "무응답 및 통화중");
		text.put("500", "이동통신사 스팸 처리");
		text.put("502", "문자 전송시간 초과");
		text.put("503", "공정위(Nospam.go.kr) 등록 스팸번호");
		text.put("504", "기타 이동통신사 오류");
		text.put("506", "이동통신사 착신번호 스팸");
		text.put("507", "수신거부");
		text.put("510", "이동통신사 전화번호 세칙 미준수 발신번호 사용");
		text.put("511", "이동통신사 사전 미등록 발신버호 사용");
		text.put("512", "이동통신사 발신번호 변작으로 등록된 발신번호 사용");
		text.put("513", "이동통신사 번호도용문자차단서비스에 가입된 발신번호 사용");
		text.put("800", "080 수신거부 대상");
		text.put("801", "관고 문자 080 수신거부 번호 미기재");
		text.put("802", "통합 수신거부 대상");
		text.put("999", "기타");

		return text.get(result+"").toString();
	}
	/* Services Category, Detail List 생성 */
	public void setPageObject(ModelAndView mv, List<Map<String, Object>> servicesList){
		Gson gson = new Gson();
		List<Map<String, Object>> categories = new ArrayList<>();
		List<Map<String, Object>> details = new ArrayList<>();
		for(Map<String, Object> map : servicesList){
			if(map.containsKey("resource"))map.put("resource", gson.fromJson(map.get("resource").toString(), Map.class));
			if(!map.containsKey("name")) categories.add(map);
			else details.add(map);
		}
		log.debug("categories : " + categories);
		log.debug("details : " + details);

		for(int i = 0 ; i < categories.size(); i++){
			int count = 0;
			String category_name = categories.get(i).get("category").toString();
			for(Map<String, Object> dMap : details){
				if(category_name.equals(dMap.get("category").toString())) count ++;
			}
			categories.get(i).put("size", count);
		}

		// 카테고리
		Collections.sort(categories, new Comparator<Map<String, Object >>() {
			@Override
			public int compare(Map<String, Object> first, Map<String, Object> second) {
				return ((Integer) first.get("procedure_int")).compareTo((Integer) second.get("procedure_int")); //ascending order
//				return ((Integer) second.get("procedure_int")).compareTo((Integer) first.get("procedure_int")); //descending order
			}
		});

		Map<String, List<Map<String, Object>>> detailMap = new HashMap<>();
		List<Map<String, Object>> detailSubList = new ArrayList<>();
		for(Map<String, Object> map : categories){
			String categoryName = map.get("category").toString();
			detailSubList = new ArrayList<>();
			for(Map<String, Object> dMap : details){
				if(categoryName.equals(dMap.get("category").toString())){
					detailSubList.add(dMap);
				}
			}
			Collections.sort(detailSubList, new Comparator<Map<String, Object >>() {
				@Override
				public int compare(Map<String, Object> first, Map<String, Object> second) {
					return ((Integer) first.get("procedure_int")).compareTo((Integer) second.get("procedure_int")); //ascending order
//				return ((Integer) second.get("procedure_int")).compareTo((Integer) first.get("procedure_int")); //descending order
				}
			});
			detailMap.put(categoryName, detailSubList);
		}


		Collections.sort(details, new Comparator<Map<String, Object >>() {
			@Override
			public int compare(Map<String, Object> first, Map<String, Object> second) {
				return ((Integer) first.get("procedure_int")).compareTo((Integer) second.get("procedure_int")); //ascending order
//				return ((Integer) second.get("procedure_int")).compareTo((Integer) first.get("procedure_int")); //descending order
			}
		});

		for(String key : detailMap.keySet()){
			System.out.println("------" + key + "-------");
			List<Map<String, Object>> list = detailMap.get(key);
			for(Map<String, Object> map : list){
				System.out.println("idx : " + map.get("idx") + ", p_int : " + map.get("procedure_int") + ", name : " + map.get("name") + ", cate : " + map.get("category"));
			}
		}

		mv.addObject("categories", categories);
		mv.addObject("detailsMap", detailMap);
	}

    /* Services Category, Detail List 생성 */
    public void setPageObjectTicketOrProduct(ModelAndView mv, List<Map<String, Object>> servicesList){
        Gson gson = new Gson();
        List<Map<String, Object>> categories = new ArrayList<>();
        List<Map<String, Object>> details = new ArrayList<>();
        for(Map<String, Object> map : servicesList){
            if(map.containsKey("resource"))map.put("resource", gson.fromJson(map.get("resource").toString(), Map.class));
            if(!map.containsKey("name")) categories.add(map);
            else details.add(map);
        }
        log.debug("categories : " + categories);
        log.debug("details : " + details);

        mv.addObject("categories", categories);
        mv.addObject("details", details);
    }

	/* Date 생성 */
	public String setForDate(Map<String, Object> paramMap){
		String date;
		if(!paramMap.containsKey("forDate")){ //period 가 없으면 해당 년도 지정
			Date today = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.format(today); // ex) value = '2018-12-31'
		} else date = paramMap.get("forDate").toString(); // 지정한 검색일 가져오기

		return date;
	}

//	public String getLastDay(String date){
//	    date = date.replaceAll("[^0-9]", "");
//	    // ex) 2019-06-10 -> 20190610 -> 201906
//        YearMonth ym = YearMonth.parse(date.substring(0, date.length()-2));
//		return ym.atEndOfMonth().toString();
//	}

	/* Map 출력 */
	public static void printMap(Map<String,Object> map){
		Iterator<Entry<String,Object>> iterator = map.entrySet().iterator();
		Entry<String,Object> entry = null;
		log.debug("--------------------printMap--------------------\n");
		while(iterator.hasNext()){
			entry = iterator.next();
			log.debug("key : "+entry.getKey()+",\tvalue : "+entry.getValue());
		}
		log.debug("");
		log.debug("------------------------------------------------\n");
	}

	/* List 출력 */
	public static void printList(List<Map<String,Object>> list){
		Iterator<Entry<String,Object>> iterator = null;
		Entry<String,Object> entry = null;
		log.debug("--------------------printList--------------------\n");
		int listSize = list.size();
		for(int i=0; i<listSize; i++){
			log.debug("list index : "+i);
			iterator = list.get(i).entrySet().iterator();
			while(iterator.hasNext()){
				entry = iterator.next();
				log.debug("key : "+entry.getKey()+",\tvalue : "+entry.getValue());
			}
			log.debug("\n");
		}
		log.debug("------------------------------------------------\n");
	}
	
	/*public List<String> getYearList(){
    	List<String> list = new ArrayList<String>();
    	list.add("2015");
    	list.add("2016");
    	list.add("2017");

    	return list;
	}

	public Float[] geoCoding(String location) {
		if (location == null)
		return null;

		Geocoder geocoder = new Geocoder();
		// setAddress : 변환하려는 주소 (경기도 성남시 분당구 등)
		// setLanguate : 인코딩 설정
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(location).setLanguage("ko").getGeocoderRequest();
		GeocodeResponse geocoderResponse;

		try {
		geocoderResponse = geocoder.geocode(geocoderRequest);
		if (geocoderResponse.getStatus() == GeocoderStatus.OK & !geocoderResponse.getResults().isEmpty()) {

		GeocoderResult geocoderResult=geocoderResponse.getResults().iterator().next();
		LatLng latitudeLongitude = geocoderResult.getGeometry().getLocation();

		Float[] coords = new Float[2];
		coords[0] = latitudeLongitude.getLat().floatValue();
		coords[1] = latitudeLongitude.getLng().floatValue();

		log.debug("coords[0] : " +coords[0]);
		log.debug("coords[1] : " +coords[1]);
		return coords;
		}
		} catch (IOException ex) {
		ex.printStackTrace();
		}
		return null;
	}

	public int StringToInt(String str, int num){
		int rtnNum = 0;

		while (str.length() < num) {
			str = str + "0";
		}

		rtnNum = Integer.parseInt(str);

		return rtnNum;
	}

	public static String computeHash(String input) {
        String ret = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] bs = md.digest(input.getBytes("US-ASCII"));


            StringBuffer sb = new StringBuffer();
            for (byte b : bs) {
                String bt = Integer.toHexString(b & 0xff);
                if(bt.length()==1) {
                    sb.append("0");
                }
                sb.append(bt);
            }
            ret = sb.toString();
        } catch (Exception e) {
        }
        return ret;
    }*/

	public String functionTransition(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*public static String getFileSize(String size)
	{
		String gubn[] = {"Byte", "KB", "MB" } ;
		String returnSize = new String ();
		int gubnKey = 0;
		double changeSize = 0;
		long fileSize = 0;
		try{
			fileSize =  Long.parseLong(size);
			for( int x=0 ; (fileSize / (double)1024 ) >0 ; x++, fileSize/= (double) 1024 ){
				gubnKey = x;
				changeSize = fileSize;
			}
			returnSize = changeSize + gubn[gubnKey];
		}catch ( Exception ex){ returnSize = "0.0 Byte"; }

		return returnSize;
	}

	public static String functionRandomString(String s){
		StringBuffer temp = new StringBuffer();
		Random rnd = new Random();
		for (int i = 0; i < 10; i++) {
			temp.append((char) ((int) (rnd.nextInt(26)) + 97));
		}

		return temp.toString();
	}

	*//* 날짜 변환 *//*
	public static String convertDateFormat(String kDate){
		String conDate = "";
		String[] dateObj = kDate.split("\\s");
		String hour = dateObj[2].substring(0, dateObj[2].indexOf(":"));
		String otherTime = dateObj[2].substring(dateObj[2].indexOf(":"), dateObj[2].length());
		System.out.println(dateObj[2].substring(0,2));
		if("오후".equals(dateObj[1]) && !"12".equals(dateObj[2].substring(0, dateObj[2].indexOf(":")))){
			int tmp = Integer.parseInt(hour) + 12;
			hour = Integer.toString(tmp);
		}
		conDate = dateObj[0] + " " + hour + otherTime;
		return conDate;
	}
	*//* % 제거 *//*
	public static String removePercentage(String str){
		str = str.replaceAll("%","");
		return str;
	}
	*//* '원' 제거 *//*
	public static String removeStringWon(String str){
		str = str.replaceAll("원", "");
		return str;
	}*/

	/* list<map> to json */
	public static JSONArray convertListToJson(List<Map<String, Object>> bankCdList) {
		JSONArray jsonArray = new JSONArray();
		for (Map<String, Object> map : bankCdList) {
			jsonArray.add(convertMapToJson(map));
		}
		return jsonArray;
	}

	/* map to json */
	public static JSONObject convertMapToJson(Map<String, Object> map) {
		JSONObject json = new JSONObject();
		for (Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			// json.addProperty(key, value);
			json.put(key, value);
		}
		return json;
	}

	/* string to json */
	/*public static JSONArray convertStrToJson(String str){
		JSONArray jArray = new JSONArray();


		return jArray;
	}*/

	/* OO:OO 시간 더하기 */
	public static String addStringHour(Map<String, Object> map, Map<String, Object> sMap){
		int hour = Integer.parseInt(map.get("hour").toString());
		int minute = Integer.parseInt(map.get("minute").toString());
		int addSpan = Integer.parseInt(map.get("span").toString());
		int defaultMinute = Integer.parseInt(sMap.get("default_minute").toString());

		if(defaultMinute == 10) addSpan = addSpan * 10;
		else if(defaultMinute == 15) addSpan = addSpan * 15;

		if((minute + addSpan) >= 60){
			hour = hour + ((minute + addSpan) / 60);
			minute = (minute + addSpan) % 60;
		} else minute = minute + addSpan;

		if(minute == 0) return hour + ":00";
		else return hour + ":" + minute;
	}
	/**
	 * Map을 json으로 변환한다.
	 *
	 * @param map Map<String, Object>.
	 * @return JSONObject.
	 */
	public static JSONObject getJsonStringFromMap( Map<String, Object> map )
	{
		JSONObject jsonObject = new JSONObject();
		for( Entry<String, Object> entry : map.entrySet() ) {
			String key = entry.getKey();
			Object value = entry.getValue() + "";
			jsonObject.put(key, value);
		}

		return jsonObject;
	}

	/**
	 * List<Map>을 jsonArray로 변환한다.
	 *
	 * @param list List<Map<String, Object>>.
	 * @return JSONArray.
	 */
	public static JSONArray getJsonArrayFromList( List<Map<String, Object>> list )
	{
		JSONArray jsonArray = new JSONArray();
		for( Map<String, Object> map : list ) {
			jsonArray.add( getJsonStringFromMap( map ) );
		}

		return jsonArray;
	}

	/**
	 * List<Map>을 jsonString으로 변환한다.
	 *
	 * @param list List<Map<String, Object>>.
	 * @return String.
	 */
	/*public static String getJsonStringFromList( List<Map<String, Object>> list )
	{
		JSONArray jsonArray = getJsonArrayFromList( list );
		return jsonArray.toJSONString();
	}*/

	/**
	 * JsonObject를 Map<String, String>으로 변환한다.
	 *
	 * @param jsonObj JSONObject.
	 * @return Map<String, Object>.
	 */
	@SuppressWarnings("unchecked")
	/*public static Map<String, Object> getMapFromJsonObject( JSONObject jsonObj )
	{
		Map<String, Object> map = null;

		try {

			map = new ObjectMapper().readValue(jsonObj.toJSONString(), Map.class) ;

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}*/

	/* sales controller method (prepaid && ticket minus) */
	public static List<Map<String, Object>> minusPrepaidTicket(List<Map<String, Object>> paramList, List<Map<String, Object>> dbList, String type){
		log.debug("param list : " + paramList);
		log.debug("db list : " + dbList);
		String costType = "";
		String useCostType = "";
		if("prepaid".equals(type)){
			costType = "cost";
			useCostType = "use_cost";
		} else{
			costType = "count";
			useCostType = "use_count";
		}
		for (int i = 0; i < paramList.size(); i++) {
			for (int j = 0; j < dbList.size(); j++) {
				String aStr = dbList.get(j).get(costType).toString(), bStr = paramList.get(i).get(useCostType).toString();
				if(aStr.indexOf(".") > -1) aStr = aStr.substring(0, aStr.indexOf("."));
				if(bStr.indexOf(".") > -1) bStr = bStr.substring(0, bStr.indexOf("."));
				int a = Integer.parseInt(aStr), b = Integer.parseInt(bStr);

				if (paramList.get(i).get("sal_idx").toString().equals(dbList.get(j).get("sale_idx").toString()) && "prepaid".equals(type)) {
					dbList.get(j).put("cost", a - b);
				} else if(paramList.get(i).get("sal_idx").toString().equals(dbList.get(j).get("sale_idx").toString()) && "ticket".equals(type)){
					dbList.get(j).put("count", a - b);
				}
				net.sf.json.JSONObject updatePrepaid = new net.sf.json.JSONObject();
				updatePrepaid.put("idx", dbList.get(j).get("idx").toString());
				updatePrepaid.put(costType, dbList.get(j).get(costType).toString());
				updatePrepaid.put("name", dbList.get(j).get("name").toString());
				updatePrepaid.put("sale_idx", dbList.get(j).get("sale_idx").toString());
				updatePrepaid.put("validity", dbList.get(j).get("validity").toString());
				if("ticket".equals(type)){
					updatePrepaid.put("cost", dbList.get(j).get("cost").toString());
				}
				dbList.set(j, updatePrepaid);
			}
		}
		return dbList;
	}

	/* 휴대폰 번호 변환*/
	public static String phone(String src) {
		if (src == null) {
			return "";
		}
		if (src.length() == 8) {
			return src.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
		} else if (src.length() == 12) {
			return src.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
		}
		return src.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
	}

	public String randomUUID(){
		String uuid = "";
		uuid = UUID.randomUUID().toString().replaceAll("-", ""); // -를 제거해 주었다.
		uuid = uuid.substring(0, 6); //uuid를 앞에서부터 10자리 잘라줌.

		return uuid;
	}

	public static Date stringConvertDateMinusDay(String str, String times, int mDay) throws ParseException {
        if(str.indexOf(" ") > -1)
            str = str.substring(0, str.indexOf(" "));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(str + " " + times + ":00");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, mDay);

		Date rDate = calendar.getTime();
		Date now = new Date();

		if(rDate.after(now)) return rDate;
		else return null;

	}
	public static Date stringConvertDateMinusDay2(String str, String resHour, String resMinute) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(str.indexOf(" ") > -1){
			str = str.substring(0, str.indexOf(" "));
		}
		Date date = sdf.parse(str + " " + resHour + ":" + resMinute + ":00");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		Date rDate = calendar.getTime();
		Date now = new Date();

		if(rDate.after(now)) return rDate;
		else return null;
	}
	public static Date stringConvertDateMinusTime(String str, String resHour, String resMinute, String time) throws ParseException {
		String[] array_time = time.split(":");
		int hour = Integer.parseInt(array_time[0]);
		int minute = Integer.parseInt(array_time[1]);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(str + " " + resHour + ":" + resMinute + ":00");
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1);
		calendar.add(Calendar.HOUR, hour);
		calendar.add(Calendar.MINUTE, minute);

		Date rDate = calendar.getTime();
		Date now = new Date();

		if(rDate.after(now)) return rDate;
		else return null;
	}
	public static Date stringConvertDateMinusMinute(int minute) {
		Date date = new Date();

		if(minute == 0 ){
			return date;
		}
		else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MINUTE, minute);

			Date rDate = calendar.getTime();
			Date now = new Date();

			if(rDate.after(now)) return rDate;
			else return null;
		}
	}

	public static Date stringConvertDateMinusDay14(int sendTime, int loopNumber) throws ParseException {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();

		if (loopNumber == 1) {
			if(sendTime == 30) calendar.add(Calendar.MINUTE, 30);
			else if(sendTime == 60) calendar.add(Calendar.HOUR, 1);
			else if(sendTime == 120) calendar.add(Calendar.HOUR, 2);
			else if(sendTime == 180) calendar.add(Calendar.HOUR, 3);
			else if(sendTime == 240) calendar.add(Calendar.HOUR, 4);
			else if(sendTime == 300) calendar.add(Calendar.HOUR, 5);
			else if(sendTime == 10000) calendar.add(Calendar.HOUR, 1);
			else if(sendTime == 20000) calendar.add(Calendar.DATE, 2);
			else if(sendTime == 30000) calendar.add(Calendar.DATE, 3);
			else if(sendTime == 40000)calendar.add(Calendar.DATE, 4);
			else if(sendTime == 50000)calendar.add(Calendar.DATE, 5);
			else if(sendTime == 60000)calendar.add(Calendar.DATE, 6);
			else if(sendTime == 70000)calendar.add(Calendar.DATE, 7);
			else if(sendTime == 80000)calendar.add(Calendar.DATE, 8);
			else if(sendTime == 90000)calendar.add(Calendar.DATE, 9);
			else if(sendTime == 100000)calendar.add(Calendar.DATE, 10);
			else if(sendTime == 110000)calendar.add(Calendar.DATE, 11);
			else if(sendTime == 120000)calendar.add(Calendar.DATE, 12);
			else if(sendTime == 130000)calendar.add(Calendar.DATE, 13);
			else if(sendTime == 140000)calendar.add(Calendar.DATE, 14);
			else if(sendTime == 150000)calendar.add(Calendar.DATE, 15);
			return calendar.getTime();
		} else {
			calendar.add(Calendar.DATE, sendTime);
			return calendar.getTime();
		}
	}

	/* 자동문자 치환
	*  type1 전날 알림
	*  type2 당일 알림
	*  type3 시간대별 알림
	*  type4 예약등록 알림
	* */
	public String autoMsgReplace1(String content, String clientName, String resDate, String resHour, String resMinute){
		String dateStrs[] = resDate.split("-");
		String thisDate = dateStrs[2];
        thisDate = thisDate.substring(0, 2);

		System.out.println("===========================");
		System.out.println("resDate : " + resDate);
		System.out.println("thisDate : " + thisDate);
		System.out.println("resHour : " + resHour);
		System.out.println("resMinute : " + resMinute);
		System.out.println("===========================");

		content = content.replaceAll("\\(성명\\)", clientName);
		content = content.replaceAll("\\(예약일\\)", thisDate + " 일");
		content = content.replaceAll("\\(예약시각\\)", resHour+"시"+resMinute+"분");
		content = content.replaceAll("\\(예약월일\\)", resDate);

		System.out.println();
		return content;
	}

	/* 자동문자 치환
	* type5 예약취소 알림
	* */
	public String autoMsgReplace5(String content, String clientName, String date){
		String dateStrs[] = date.split(" ");
		String dates[] = dateStrs[0].split("-");
		String resDate = dates[2];
		String resTimes[] = dateStrs[1].split(":");
		String resHour = resTimes[0];
		String resMinute = resTimes[1];

		content = content.replaceAll("\\(성명\\)", clientName);
		content = content.replaceAll("\\(예약일\\)", resDate + " 일");
		content = content.replaceAll("\\(예약시각\\)", resHour+"시"+resMinute+"분");
		content = content.replaceAll("\\(예약월일\\)", dateStrs[0]);
		return content;
	}

	/* 자동문자 치환
	* type6 포인트 적립 알림
	* type7 포인트 사용 알림
	* */
	public String autoMsgReplace6(String content, String clientName, String clientPoint){
		content = content.replaceAll("\\(성명\\)", clientName);
		content = content.replaceAll("\\(누적포인트\\)", clientPoint);

		return content;
	}

	/* 자동문자 치환
	 * type8 선불권 적립 알림
	 * */
	public String autoMsgReplace8(String content, String accumulate, Map<String, Object> map){
		int total_prepaid = 0;
		List<Map<String, Object>> client = new Gson().fromJson(map.get("prepaid").toString(), List.class);
		for(int i = 0 ; i < client.size(); i ++){
			double d = Double.parseDouble(client.get(i).get("cost").toString());
			total_prepaid += (int)d;
		}

		content = content.replaceAll("\\(성명\\)", map.get("name").toString());
		content = content.replaceAll("\\(선불적립\\)", accumulate);
		content = content.replaceAll("\\(선불잔액\\)", Integer.toString(total_prepaid));

		return content;
	}

	/* 자동문자 치환
	* type 9 선불권 사용
	* */
	public String autoMsgReplace9(String content, Map<String,Object> prepaidMap, String client_name){
		int totalCost = 0, useCost = 0;
		if(prepaidMap.get("total_cost").toString().indexOf(".") > 0){
			totalCost = Integer.parseInt(prepaidMap.get("total_cost").toString().substring(0, prepaidMap.get("total_cost").toString().indexOf(".")));
		} else {
			totalCost = Integer.parseInt(prepaidMap.get("total_cost").toString());
		}

		if(prepaidMap.get("total_cost").toString().indexOf(".") > 0){
			useCost = Integer.parseInt(prepaidMap.get("use_cost").toString().substring(0, prepaidMap.get("total_cost").toString().indexOf(".")));
		} else {
			useCost = Integer.parseInt(prepaidMap.get("use_cost").toString());
		}


		content = content.replaceAll("\\(성명\\)", client_name);
		content = content.replaceAll("\\(선불명\\)", prepaidMap.get("prepaid_name").toString());
		content = content.replaceAll("\\(선불차감\\)", prepaidMap.get("use_cost").toString());
		content = content.replaceAll("\\(선불잔액\\)", Integer.toString(totalCost - useCost));

		return content;
	}

	/* 자동문자 치환
	 * type 11 티켓 구매
	 * */
	public String autoMsgReplace11(String content, String ticket_name, String accumulate, String client_name){

		content = content.replaceAll("\\(성명\\)", client_name);
		content = content.replaceAll("\\(티켓명\\)", ticket_name);
		content = content.replaceAll("\\(적립횟수\\)", accumulate);

		return content;
	}

	/* 자동문자 치환
	 * type 12 티켓 사용
	 * */
	public String autoMsgReplace12(String content, Map<String, Object> ticketMap, String client_name){

		int totalCount = Integer.parseInt(ticketMap.get("total_count").toString());
		int useCount = Integer.parseInt(ticketMap.get("use_count").toString());

		content = content.replaceAll("\\(성명\\)", client_name);
		content = content.replaceAll("\\(티켓명\\)", ticketMap.get("ticket_name").toString());
		content = content.replaceAll("\\(잔여횟수\\)", Integer.toString(totalCount - useCount) + "회");

		return content;
	}

	/* 자동문자 치환
	 * type 14 고객 방문 후 관리(첫 방문)
	 * * type 15 고객 방문 후 관리(재 방문)
	 * */
	public String autoMsgReplace14(String content,  String client_name){
		content = content.replaceAll("\\(성명\\)", client_name);
		return content;
	}

	// UTIL (OBJECT TO INT)
	public static int parseObjectToInt(Object value) {
		int val = (int)Double.parseDouble(value.toString());
		return val;
	}

	/*
	* 문자 길이 확인 (byte length check)
	* */
	public static final int getByteSizeToComplex(String str) {
		int en = 0;
		int ko = 0;
		int etc = 0;

		char[] string = str.toCharArray();
		for (int j=0; j<string.length; j++) {
			if (string[j]>='A' && string[j]<='z') {
				en++;
			}
			else if (string[j]>='\uAC00' && string[j]<='\uD7A3') {
				ko++;
				ko++;
			}
			else {
				etc++;
			}
		}
		return (en + ko + etc);
	}

	// 고객 정보 모달 - 수정된 회원권 값 찾기
	public static String listDiscrimination (List<Map<String, Object>> origin, List<Map<String, Object>> difference, String costOrCount) {
		List<String> response = new ArrayList<>();
		Map<String, Object> responseObject = new HashMap<>();
		String originCost = "";			// 이전 가격
		String originValidity = "";		// 이전 기간
		String changedValidity = "";	// 최종 기간
		String sale_idx = "";			// 보유 구분자 (sale_idx)
		String name = "";				// 타겟 이름 (회원권 이름)
		String changeType = "";			// 가격, 기간, 둘 다 (0, 1, 2)
		String changeCost = "0";		// 차이 (count)
		String changeDate = "0";		// 차이 (validity)

		if(!origin.contains(difference)){
			// 선불권 변경됨
			for(Map<String, Object> d : difference) {
				String saleIdx = d.get("sale_idx").toString();
				String validity = d.get("validity").toString();
				String cost = d.get(costOrCount).toString();

				for (Map<String, Object> o : origin) {
					String originSaleIdx = o.get("sale_idx").toString();
					originValidity = o.get("validity").toString();
					originCost = o.get(costOrCount).toString();

					if (saleIdx.equals(originSaleIdx) && (!validity.equals(originValidity) || !cost.equals(originCost))) {
//							System.out.println("변경된 선불권 Map");
//							CommonUtils.printMap(o);
//							CommonUtils.printMap(d);
						changedValidity = d.get("validity").toString();
						sale_idx = o.get("sale_idx").toString();
						name = o.get("name").toString();

						/* 금액 or 횟수 소수점 제거 */
						if(d.get(costOrCount).toString().indexOf(".") != -1)
							d.put(costOrCount, d.get(costOrCount).toString().substring(0, d.get(costOrCount).toString().indexOf(".")));
						if(o.get(costOrCount).toString().indexOf(".") != -1)
							o.put(costOrCount, o.get(costOrCount).toString().substring(0, o.get(costOrCount).toString().indexOf(".")));

						if (!cost.equals(originCost) && validity.equals(originValidity)) {
							changeType = "0";
//							changeCost = String.valueOf(Integer.parseInt(d.get(costOrCount).toString()) - Integer.parseInt(o.get(costOrCount).toString()));
							changeCost = Integer.toString(Integer.parseInt(d.get(costOrCount).toString()) - Integer.parseInt(o.get(costOrCount).toString()));
						} else if (cost.equals(originCost) && !validity.equals(originValidity)) {
							changeType = "1";
							changeDate = returnDateDifference(originValidity, changedValidity);
						} else if (!cost.equals(originCost) && !validity.equals(originValidity)) {
							changeType = "2";
//							changeCost = String.valueOf(Integer.parseInt(d.get(costOrCount).toString()) - Integer.parseInt(o.get(costOrCount).toString()));
							changeCost = Integer.toString(Integer.parseInt(d.get(costOrCount).toString()) - Integer.parseInt(o.get(costOrCount).toString()));
							changeDate = returnDateDifference(originValidity, changedValidity);
						}

						responseObject.put("originCost", originCost);			// 이전 코스트
						responseObject.put("originValidity", originValidity);	// 이전 기간
						responseObject.put("changeCost", changeCost);			// 변경된 코스트
						responseObject.put("changedValidity", changedValidity);	// 변경된 기간
						responseObject.put("sale_idx", sale_idx);				// Sale_idx
						responseObject.put("name", name);						// 상품 이름
						responseObject.put("changeType", changeType);			// 유형 (0 : 금액 변경, 1 : 기간 변경, 2: 둘다 변경)
						responseObject.put("changeDate", changeDate);			// 변경된 기간 차이 ( ㅇ 일 차이)
						response.add(JSONObject.toJSONString(responseObject));
						responseObject = new HashMap<>();
					}
				}
			}

			return response.toString();
		} else {
			return "";
		}
	}

	// 고객 정보 모달 - 삭제된 값 찾기
	public static String listDeleteSearch(List<Map<String, Object>> origin, List<Map<String, Object>> difference, String costOrCount){
		List<String> response = new ArrayList<>();
		boolean flag = false;

		for(Map<String, Object> o : origin){
			for(Map<String, Object> d : difference){
				if(o.get("sale_idx").toString().equals(d.get("sale_idx").toString())) flag = true;
			}
			if(!flag) response.add(JSONObject.toJSONString(o));
			flag = false;
		}

		return response.toString();
	}

	public static String returnDateDifference(String originDate, String differenceDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date startDate = sdf.parse(differenceDate);
			Date endDate = sdf.parse(originDate);

			//두날짜 사이의 시간 차이(ms)를 하루 동안의 ms(24시*60분*60초*1000밀리초) 로 나눈다.
			long diffDay = (startDate.getTime() - endDate.getTime()) / (24 * 60 * 60 * 1000);
			return Long.toString(diffDay);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
	}


	/*==============================PopBill Method */
//	public Object sendSMS(Map<String, Object> paramMap, String comNum, String ID) {
//		JSONObject jobj = new JSONObject();
//		//String sender = "07043042991";					// 발신번호
//		String sender = paramMap.get("sender_number").toString();					// 발신번호
//		String receiver = "01032025092";					// 수신번호
//		String receiverName = "권승훈";					// 수신자명
//		String content = "문자메시지 내용";					// 메시지 내용, 90byte 초과된 내용은 삭제되어 전송
//		Date reserveDT = null;							// 예약전송일시, null 처리시 즉시전송
//		Boolean adsYN = false;							// 광고문자 전송여부
//
//		// 전송요청번호
//		// 파트너가 전송 건에 대해 관리번호를 구성하여 관리하는 경우 사용.
//		// 1~36자리로 구성. 영문, 숫자, 하이픈(-), 언더바(_)를 조합하여 팝빌 회원별로 중복되지 않도록 할당.
//		String requestNum = "";
//
//		try {
//			String receiptNum = messageService.sendSMS(comNum, sender, receiver,
//					receiverName, content, reserveDT, adsYN, ID, requestNum);
//
//			System.out.println("receipNum : " + receiptNum);
//			jobj.put("code", 200);
//		} catch (PopbillException e) {
//			jobj.put("code", 900);
//		}
//
//		return jobj;
//	}
}
