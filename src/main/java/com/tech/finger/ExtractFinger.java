package com.tech.finger;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;




/**
 * 页面指纹信息提取，按照每句话每段话提取
 * 
 * @author Administrator
 */
public class ExtractFinger {
	private List<DocField> fieldList;

	/**
	 * 构造函数
	 * 
	 * @param content
	 *            页面内容
	 */
	public ExtractFinger(List<DocField> fieldList) {
		this.fieldList = fieldList;
	}
	
	public SimHash finger(){
		Map<String,Double> result = getTokenList();
		List<String> words = new ArrayList<String>();
		List<Double> doubleList = new ArrayList<Double>();
		for (Map.Entry<String, Double> entry : result.entrySet()) {
			words.add(entry.getKey());
			doubleList.add(entry.getValue());
		}
		SimHash simhash = new SimHash(words.toArray(new String[words.size()]), doubleList.toArray(new Double[doubleList.size()]),64);
		return simhash;
	}
	
	public Map<String,Double> getTokenList() {
		Map<String,Double> wordCountMap = new HashMap<String,Double>();
		Map<String, Double> result = new TreeMap<String, Double>();
		int length = 0;
		for(DocField docField : fieldList){
			length += getTokenList(docField.getContent(), docField.getScore(), wordCountMap);
		}
		for (Map.Entry<String, Double> entry : wordCountMap.entrySet()) {
			result.put(entry.getKey().toString(), entry.getValue()/length);
		}
		return result;
	}

	/**
	 * 返回词的总数
	 * @param content
	 * @param facotr
	 * @param wordCountMap
	 * @return
	 */
	private int getTokenList(String content,int facotr,Map<String,Double> wordCountMap) {
		int length = 0;
		String regEx = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(regEx);
		try {
			List<Term> termList = HanLP.segment(content);
			for(Term term : termList){
				String value = term.word.trim();
				if(value.length()>1 && pat.matcher(value).find()){
					Double count = wordCountMap.get(value);
					if(count==null){
						count = Double.valueOf(1);
					}else{
						count = count+facotr;
					}
					wordCountMap.put(value, count);
					length++;
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return length;
	}
	
	public static void main(String[] args) throws IOException {
		String s = FileUtils.readFileToString(new File("F:\\xdproject2017\\10-民政部\\05-数据\\1.txt"),"GB2312");
		List<DocField> fieldList1 = new ArrayList<DocField>();
		fieldList1.add(new DocField("1","国家计委、财政部、民政部关于下达１９９９年地方承建移交政府安置的第四批军队离退休干部建房追加投资及财政专项基建支出预算的通知",3));
		fieldList1.add(new DocField("2",s,1));
		SimHash hash1 = new ExtractFinger(fieldList1).finger();

		
		String s2 = FileUtils.readFileToString(new File("F:\\xdproject2017\\10-民政部\\05-数据\\2.txt"),"GB2312");
		List<DocField> fieldList2 = new ArrayList<DocField>();
		fieldList1.add(new DocField("1","国家计委、财政部、民政部关于下达１９９９年地方承建移交政府安置的军队离退休干部建房投资及财政专项基建支出预算的通知",3));
		fieldList1.add(new DocField("2",s2,1));
		
		SimHash hash2 =  new ExtractFinger(fieldList2).finger();
		System.out.println(hash1.hammingDistance(hash2));
	}
	
}
