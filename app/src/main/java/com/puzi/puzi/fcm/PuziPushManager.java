package com.puzi.puzi.fcm;

import android.util.Log;

import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

/**
 * Created by JangwonPark on 2018. 1. 19..
 */

public class PuziPushManager {

	private static final Map<PuziPushType, Set<Integer>> receivedMap = newHashMap();
	private static PuziPushMessageVO finalMessage = null;

	static {
		for(PuziPushType type : PuziPushType.values()) {
			Set<Integer> typeSet = newHashSet();
			receivedMap.put(type, typeSet);
		}
	}

	public static boolean addId(PuziPushType type, int id) {
		return receivedMap.get(type).add(id);
	}

	public synchronized static boolean isEmptyFianlMessage(){
		return finalMessage == null;
	}

	public synchronized static PuziPushMessageVO getFinalMessage() {
		Log.d("PUSH", "+++ getFinalMessage : " + finalMessage);
		return finalMessage;
	}

	public synchronized static void refreshFinalMessage() {
		finalMessage = null;
	}

	public synchronized static void setFinalMessage(PuziPushMessageVO finalMessage) {
		PuziPushManager.finalMessage = finalMessage;
	}

}
