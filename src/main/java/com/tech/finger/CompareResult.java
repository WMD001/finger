package com.tech.finger;

import java.math.BigInteger;

public class CompareResult {
	private int distance;
	/**
	 * 数据id，标识是不同的数据
	 */
	private String dataid;
	private String similarityNo;
	private boolean repeat;
	private String url;
	private BigInteger hashcode;
	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getUrl1() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSimilarityNo() {
		return similarityNo;
	}

	public void setSimilarityNo(String similarityNo) {
		this.similarityNo = similarityNo;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	public String getDataid() {
		return dataid;
	}

	public void setDataid(String dataid) {
		this.dataid = dataid;
	}

	public BigInteger getHashcode() {
		return hashcode;
	}

	public void setHashcode(BigInteger hashcode) {
		this.hashcode = hashcode;
	}
	
}
