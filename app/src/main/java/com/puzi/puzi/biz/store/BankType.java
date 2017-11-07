package com.puzi.puzi.biz.store;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by JangwonPark on 2017. 11. 5..
 */
@AllArgsConstructor
@Getter
public enum BankType {
	IBKOKRSE("003", "기업은행"),
	CZNBKRSE("004", "국민은행"),
	KOEXKRSE("005", "외환은행"),
	NFFCKRSE("007", "수협"),
	NACFKRSE("011", "농협"),
	HVBKKRSEXXX("020", "우리은행"),
	SCBLKRSE("023", "제일은행"),
	CITIKRSX("027", "씨티은행"),
	DAEBKR22("031", "대구은행"),
	PUSBKR2P("032", "부산은행"),
	KWABKRSE("034", "광주은행"),
	JJBKKR22("035", "제주은행"),
	JEONKRSE("037", "전북은행"),
	KYNAKR22XXX("039", "경남은행"),
	SHBKKRSEPO("071", "우체국"),
	HNBNKRSE("081", "하나은행"),
	SHBKKRSE("088", "신한은행");

	private String code;
	private String name;

	public static List<String> getBankNameList() {
		List<String> list = newArrayList();
		for(BankType bankType : values()) {
			list.add(bankType.getName());
		}
		return list;
	}

	public static BankType findByName(String name) {
		for(BankType bankType : values()) {
			if(bankType.getName().equals(name)){
				return bankType;
			}
		}
		return null;
	}
}
