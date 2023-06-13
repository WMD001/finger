//package com.tech.finger;
//
//import com.xdtech.util.PropertiesReader;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class FingerDBFactory {
//	private static String basePath = PropertiesReader.berkeleyBasePath;
//	private static Map<String, BerkeleyFingerDB> dbMap = new HashMap<String, BerkeleyFingerDB>();
//	private static Map<String, SimHashComparator> simhashComparatorMap = new HashMap<String, SimHashComparator>();
//
//	public static synchronized BerkeleyFingerDB getBerkeleyDB(String flag) throws IOException {
//		if (dbMap.get(flag) == null) {
//			String path = basePath + flag;
//			dbMap.put(flag, new BerkeleyFingerDB(path));
//		}
//		return dbMap.get(flag);
//	}
//
//	public static synchronized SimHashComparator getSimHashComparator(String flag) throws IOException {
//		if (simhashComparatorMap.get(flag) == null) {
//			SimHashComparator simhashComparator = new SimHashComparator(getBerkeleyDB(flag));
//			simhashComparatorMap.put(flag, simhashComparator);
//		}
//		return simhashComparatorMap.get(flag);
//	}
//
//}
