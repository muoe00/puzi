package com.puzi.puzi.biz.user;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

/**
 * Created by JangwonPark on 2018. 1. 17..
 */

public class AddressInfo {

	public static final Map<String, List<String>> CITY_MAP = newHashMap();

	public static final List<String> REGION_LIST = newArrayList(
		"서울특별시",
		"강원도",
		"경기도",
		"경상남도",
		"경상북도",
		"광주광역시",
		"대구광역시",
		"대전광역시",
		"부산광역시",
		"세종특별자치시",
		"울산광역시",
		"인천광역시",
		"전라남도",
		"전라북도",
		"제주특별자치도",
		"충청남도",
		"충청북도"
	);

	public static final List<String> SEOUL_LIST = newArrayList(
		"강남구",
		"강동구",
		"강북구",
		"강서구",
		"관악구",
		"광진구",
		"구로구",
		"금천구",
		"노원구",
		"도봉구",
		"동대문구",
		"동작구",
		"마포구",
		"서대문구",
		"서초구",
		"성동구",
		"성북구",
		"송파구",
		"양천구",
		"영등포구",
		"용산구",
		"은평구",
		"종로구",
		"중구",
		"중랑구"
	);

	public static final List<String> GYEONGGI_LIST = newArrayList(
		"가평군",
		"고양시 덕양구",
		"고양시 일산동구",
		"고양시 일산서구",
		"과천시",
		"광명시",
		"광주시",
		"구리시",
		"군포시",
		"김포시",
		"남양주시",
		"동두천시",
		"부천시",
		"성남시 분당구",
		"성남시 수정구",
		"성남시 중원구",
		"수원시 권선구",
		"수원시 영통구",
		"수원시 장안구",
		"수원시 팔달구",
		"시흥시",
		"안산시 단원구",
		"안산시 상록구",
		"안성시",
		"안양시 동안구",
		"안양시 만안구",
		"양주시",
		"양평군",
		"여주시",
		"연천군",
		"오산시",
		"용인시 기흥구",
		"용인시 수지구",
		"용인시 처인구",
		"의왕시",
		"의정부시",
		"이천시",
		"파주시",
		"평택시",
		"포천시",
		"하남시",
		"화성시"
	);

	public static final List<String> GANGWON_LIST = newArrayList(
		"강릉시",
		"고성군",
		"동해시",
		"삼척시",
		"속초시",
		"양구군",
		"양양군",
		"영월군",
		"원주시",
		"인제군",
		"정선군",
		"철원군",
		"춘천시",
		"태백시",
		"평창군",
		"홍천군",
		"화천군",
		"횡성군"
	);

	public static final List<String> GYEONGSANGNAMDO_LIST = newArrayList(
		"거제시",
		"거창군",
		"고성군",
		"김해시",
		"남해군",
		"밀양시",
		"사천시",
		"산청군",
		"양산시",
		"의령군",
		"진주시",
		"창녕군",
		"창원시 마산합포구",
		"창원시 마산회원구",
		"창원시 성산구",
		"창원시 의창구",
		"창원시 진해구",
		"통영시",
		"하동군",
		"함안군",
		"함양군",
		"합천군"
	);

	public static final List<String> GYEONGSANGBUKDO_LIST = newArrayList(
		"경산시",
		"경주시",
		"고령군",
		"구미시",
		"군위군",
		"김천시",
		"문경시",
		"봉화군",
		"상주시",
		"성주군",
		"안동시",
		"영덕군",
		"영양군",
		"영주시",
		"영천시",
		"예천군",
		"울릉군",
		"울진군",
		"의성군",
		"청도군",
		"청송군",
		"칠곡군",
		"포항시 남구",
		"포항시 북구"
	);

	public static final List<String> GWANGJOO_LIST = newArrayList(
		"광산구",
		"남구",
		"동구",
		"북구",
		"서구"
	);

	public static final List<String> DAEGOO_LIST = newArrayList(
		"남구",
		"달서구",
		"달성군",
		"동구",
		"북구",
		"서구",
		"수성구",
		"중구"
	);

	public static final List<String> DAEJEON_LIST = newArrayList(
		"대덕구",
		"동구",
		"서구",
		"유성구",
		"중구"
	);

	public static final List<String> BUSAN_LIST = newArrayList(
		"강서구",
		"금정구",
		"기장군",
		"남구",
		"동구",
		"동래구",
		"부산진구",
		"북구",
		"사상구",
		"사하구",
		"서구",
		"수영구",
		"연제구",
		"영도구",
		"중구",
		"해운대구"
	);

	public static final List<String> SEJONG_LIST = newArrayList(
		"가람동",
		"금남면",
		"나성동",
		"다정동",
		"대평동",
		"도담동",
		"반람동",
		"금남면",
		"나성동",
		"다정동",
		"대평동",
		"도담동",
		"반곡동",
		"보람동",
		"부강면",
		"새롬동",
		"소담동",
		"소정면",
		"아름동",
		"어진동",
		"연기면"
	);

	public static final List<String> WOOLSAN_LIST = newArrayList(
		"남구",
		"동구",
		"북구",
		"울주군",
		"중구"
	);

	public static final List<String> INCHEON_LIST = newArrayList(
		"강화군",
		"계양구",
		"남구",
		"남동구",
		"동구",
		"부평구",
		"서구",
		"연수구",
		"옹진군",
		"중구"
	);

	public static final List<String> JEONRANAMDO_LIST = newArrayList(
		"강진군",
		"고흥군",
		"곡성군",
		"광양시",
		"구례군",
		"나주시",
		"담양군",
		"목포시",
		"무안군",
		"보성군",
		"순천시",
		"신안군",
		"여수시",
		"영광군",
		"영암군",
		"완도군",
		"장성군",
		"장흥군",
		"진도군",
		"함평군",
		"해남군",
		"화순군"
	);

	public static final List<String> JEONRABUKDO_LIST = newArrayList(
		"고창군",
		"군산시",
		"김제시",
		"남원시",
		"무주군",
		"부안군",
		"순창군",
		"완주군",
		"익산시",
		"임실군",
		"장수군",
		"전주시 덕진구",
		"전주시 완산구",
		"정읍시",
		"진안군"
	);

	public static final List<String> JEJU_LIST = newArrayList(
		"서귀포시",
		"제주시"
	);

	public static final List<String> CHOONGNAM_LIST = newArrayList(
		"계룡시",
		"공주시",
		"금산군",
		"논산시",
		"당진시",
		"보령시",
		"부여군",
		"서산시",
		"서천군",
		"아산시",
		"예산군",
		"천안시 동남구",
		"천안시 서북구",
		"청양군",
		"태안군",
		"홍성군"
	);

	public static final List<String> CHOONGBUK_LIST = newArrayList(
		"괴산군",
		"단양군",
		"보은군",
		"영동군",
		"옥천군",
		"음성군",
		"제천시",
		"증평군",
		"진천군",
		"청주시 상당구",
		"청주시 서원구",
		"청주시 청원구",
		"청주시 흥덕구",
		"충주시"
	);

	static {
		for(String region : REGION_LIST){
			switch (region) {
				case "서울특별시": CITY_MAP.put(region, SEOUL_LIST); break;
				case "강원도": CITY_MAP.put(region, GANGWON_LIST); break;
				case "경기도": CITY_MAP.put(region, GYEONGGI_LIST); break;
				case "경상남도": CITY_MAP.put(region, GYEONGSANGNAMDO_LIST); break;
				case "경상북도": CITY_MAP.put(region, GYEONGSANGBUKDO_LIST); break;
				case "광주광역시": CITY_MAP.put(region, GWANGJOO_LIST); break;
				case "대구광역시": CITY_MAP.put(region, DAEGOO_LIST); break;
				case "대전광역시": CITY_MAP.put(region, DAEJEON_LIST); break;
				case "부산광역시": CITY_MAP.put(region, BUSAN_LIST); break;
				case "세종특별자치시": CITY_MAP.put(region, SEJONG_LIST); break;
				case "울산광역시": CITY_MAP.put(region, WOOLSAN_LIST); break;
				case "인천광역시": CITY_MAP.put(region, INCHEON_LIST); break;
				case "전라남도": CITY_MAP.put(region, JEONRANAMDO_LIST); break;
				case "전라북도": CITY_MAP.put(region, JEONRABUKDO_LIST); break;
				case "제주특별자치도": CITY_MAP.put(region, JEJU_LIST); break;
				case "충청남도": CITY_MAP.put(region, CHOONGNAM_LIST); break;
				case "충청북도": CITY_MAP.put(region, CHOONGBUK_LIST); break;
			}
		}
	}

}
