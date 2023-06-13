package com.tech.finger;

import java.math.BigInteger;

/**
 * 内容的指纹信息
 * @author Administrator
 *
 */
public class ContentFinger {
	/**指纹*/
	private String finger;
	/**是否重复*/
	private boolean repeat;
	/**重复性的比较范围**/
	private String flag;
	/**内容编号*/
	private String similarityNo;
	
	/**内容指纹*/
	private BigInteger hashcode;
	public String getFinger() {
		return finger;
	}

	public void setFinger(String finger) {
		this.finger = finger;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	public String getSimilarityNo() {
		return similarityNo;
	}

	public void setSimilarityNo(String similarityNo) {
		this.similarityNo = similarityNo;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public BigInteger getHashcode() {
		return hashcode;
	}

	public void setHashcode(BigInteger hashcode) {
		this.hashcode = hashcode;
	}

}
