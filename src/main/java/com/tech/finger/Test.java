//package com.tech.finger;
//
//import org.apache.commons.io.FileUtils;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Test {
//
//	public static void main(String[] args) throws IOException {
//		// TODO Auto-generated method stub
//		String id = "992";
//		List<DocField> fieldList = new ArrayList<DocField>();
//		DocField titleField = new DocField();
//		titleField.setField("title");
//		titleField.setScore(3);
//		titleField.setContent("中华人民共和国外商投资企业和外国企业所得税法实施细则");
//
//		DocField contentField = new DocField();
//		contentField.setField("content");
//		contentField.setScore(1);
//		contentField.setContent(FileUtils.readFileToString(new File("F:\\xdproject2017\\10-民政部\\05-数据\\3.txt"),"GB2312"));
//		//fieldList.add(titleField);
//		fieldList.add(contentField);
//
//		ContentFinger contentFinger = new DocFingerService().fingerValue(id, fieldList, "test");
//		System.out.println(contentFinger.isRepeat());
//		System.out.println(contentFinger.getSimilarityNo());
//		System.out.println(contentFinger.getHashcode().toString());
//	}
//	public static void main2(String[] args) throws IOException {
//		// TODO Auto-generated method stub
//		String id = "991";
//		List<DocField> fieldList = new ArrayList<DocField>();
//		DocField titleField = new DocField();
//		titleField.setField("title");
//		titleField.setScore(3);
//		titleField.setContent("中华人民共和国国家赔偿法");
//
//		DocField contentField = new DocField();
//		contentField.setField("content");
//		contentField.setScore(1);
//		contentField.setContent(FileUtils.readFileToString(new File("F:\\xdproject2017\\10-民政部\\05-数据\\2.txt"),"GB2312"));
//		//System.out.println(contentField.getContent());
//		//fieldList.add(titleField);
//		fieldList.add(contentField);
//
//		ContentFinger contentFinger = new DocFingerService().fingerValue(id, fieldList, "test");
//		System.out.println(contentFinger.isRepeat());
//		System.out.println(contentFinger.getSimilarityNo());
//		System.out.println(contentFinger.getHashcode().toString());
//	}
//}
