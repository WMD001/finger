package com.tech.finger;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.StringTokenizer;

/**
 * 使用64bit
 * 
 * @author Administrator
 * 
 */
public class SimHash {
	private BigInteger strSimHash;
	private int hashbits = 64;
	private String tokens;
	
	public void setStrSimHash(BigInteger strSimHash) {
		this.strSimHash = strSimHash;
	}

	/**
	 * 使用string tokenizer分词，计算hash值
	 * @param tokens
	 * @param hashbits
	 */
	public SimHash(String tokens, int hashbits) {
		this.tokens = tokens;
		this.hashbits = hashbits;
		this.strSimHash = this.simHash();
	}

	/**
	 * 已经计算了词向量以及各个词向量的权重
	 * @param tokens
	 * @param scores
	 */
	public SimHash(String[] tokens, Double[] scores,int hashbits) {
		double[] v = new double[this.hashbits];
		for (int k = 0; k < tokens.length; k++) {
			String token = tokens[k];
			BigInteger t = this.hash(token);
			for (int i = 0; i < this.hashbits; i++) {
				BigInteger bitmask = new BigInteger("1").shiftLeft(i);
				if (t.and(bitmask).signum() != 0) {
					v[i] += scores[k];
				} else {
					v[i] -= scores[k];
				}
			}
		}
		BigInteger fingerprint = new BigInteger("0");
		for (int i = 0; i < this.hashbits; i++) {
			if (v[i] >= 0) {
				fingerprint = fingerprint.add(new BigInteger("1").shiftLeft(i));
			}
		}
		this.strSimHash = fingerprint;
	}

	private BigInteger hash(String source) {
		if (source == null || source.length() == 0) {
			return new BigInteger("0");
		} else {
			char[] sourceArray = source.toCharArray();
			BigInteger x = BigInteger.valueOf(((long) sourceArray[0]) << 7);
			BigInteger m = new BigInteger("1000003");
			BigInteger mask = new BigInteger("2").pow(this.hashbits).subtract(new BigInteger("1"));
			for (char item : sourceArray) {
				BigInteger temp = BigInteger.valueOf((long) item);
				x = x.multiply(m).xor(temp).and(mask);
			}
			x = x.xor(new BigInteger(String.valueOf(source.length())));
			if (x.equals(new BigInteger("-1"))) {
				x = new BigInteger("-2");
			}
			return x;
		}
	}

	public int hammingDistance(SimHash other) {
		BigInteger m = new BigInteger("1").shiftLeft(this.hashbits).subtract(new BigInteger("1"));
		BigInteger x = this.strSimHash.xor(other.strSimHash).and(m);
		int tot = 0;
		while (x.signum() != 0) {
			tot += 1;
			x = x.and(x.subtract(new BigInteger("1")));
		}
		return tot;
	}

	public BigInteger getStrSimHash() {
		return strSimHash;
	}
	
	public BigInteger simHash() {
        int[] v = new int[this.hashbits];
        StringTokenizer stringTokens = new StringTokenizer(this.tokens);
        while (stringTokens.hasMoreTokens()) {
            String temp = stringTokens.nextToken();
            BigInteger t = this.hash(temp);
            for (int i = 0; i < this.hashbits; i++) {
                BigInteger bitmask = new BigInteger("1").shiftLeft(i);
                 if (t.and(bitmask).signum() != 0) {
                    v[i] += 1;
                } else {
                    v[i] -= 1;
                }
            }
        }
        BigInteger fingerprint = new BigInteger("0");
        for (int i = 0; i < this.hashbits; i++) {
            if (v[i] >= 0) {
                fingerprint = fingerprint.add(new BigInteger("1").shiftLeft(i));
            }
        }
        return fingerprint;
    }

	public static void main(String[] args) throws IOException {
		String s = FileUtils.readFileToString(new File("F:\\xdproject2017\\10-民政部\\05-数据\\1.txt"),"GB2312");
		SimHash hash1 = new SimHash(s, 64);
		System.out.println(hash1.strSimHash + "  " + hash1.strSimHash.bitLength());

		String s2 = FileUtils.readFileToString(new File("F:\\xdproject2017\\10-民政部\\05-数据\\2.txt"),"GB2312");
		SimHash hash2 = new SimHash(s2, 64);
		System.out.println(hash2.strSimHash + "  " + hash2.strSimHash.bitLength());
		System.out.println(hash1.hammingDistance(hash2));
		
		hash1.strSimHash = new BigInteger("3173449683295930053", 10);
		hash2.strSimHash = new BigInteger("2848903618783740159", 10);
		System.out.println(hash1.hammingDistance(hash2));
	}
}
