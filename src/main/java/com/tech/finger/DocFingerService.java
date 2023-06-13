//package com.tech.finger;
//
//import javax.jws.WebService;
//import java.io.IOException;
//import java.util.List;
//
//@WebService(endpointInterface = "com.xdtech.finger.DocFingerServiceI", targetNamespace = "http://www.xd-tech.com")
//public class DocFingerService implements DocFingerServiceI {
//
////	@Override
//	public ContentFinger fingerValue(String dataid, List<DocField> fieldList, String flag) {
//		SimHash finger = new ExtractFinger(fieldList).finger();
//		ContentFinger cfinger = new ContentFinger();
//		cfinger.setFinger(finger.getStrSimHash().toString());
//		cfinger.setFlag(flag);
//
//		compareAllDocs(dataid, finger, flag, cfinger);
//
//		return cfinger;
//	}
//
//	/**
//	 * 判断是否重复
//	 *
//	 * @param bigInteger
//	 * @param flag
//	 * @param cfinger
//	 */
//	private void compareAllDocs(String dataid, SimHash finger, String flag, ContentFinger cfinger) {
//		try {
//			CompareResult compareResult = FingerDBFactory.getSimHashComparator(flag).compare(finger.getStrSimHash(), dataid);
//			cfinger.setSimilarityNo(compareResult.getSimilarityNo());
//			cfinger.setRepeat(compareResult.isRepeat());
//			cfinger.setHashcode(compareResult.getHashcode());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//
//}
