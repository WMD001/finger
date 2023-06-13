//package com.tech.finger;
//
//import java.math.BigInteger;
//import java.util.*;
//import java.util.concurrent.*;
///**
// * simhash 比较器
// *
// * @author Administrator
// *
// */
//public class SimHashComparator {
//	private static int size = 16;
//	private static int length = 4;
//	private List<Short[]> dataList;
//	private Map<String, Short[]> dataIdMap = new HashMap<>();
//
//	/**
//	 * 比较
//	 *
//	 * @param simHashCode simHashCode
//	 * @return CompareResult
//	 */
//	public synchronized CompareResult compare(BigInteger simHashCode, String dataid) {
//		CompareResult compareResult = new CompareResult();
//		compareResult.setDataid(dataid);
//
//		Short[] hashSplit = splitFour(simHashCode);
//		Set<Integer> possibleSet = findPossibleSet(hashSplit);
//		// 找到了可能的集合
//		CompareResult similarityBean = findSimlarity(possibleSet, hashSplit);
//		if (similarityBean != null) {
//			compareResult.setRepeat(true);
//			compareResult.setSimilarityNo(similarityBean.getSimilarityNo());
//		} else {
//			compareResult.setRepeat(false);
//			compareResult.setSimilarityNo(UUID.randomUUID().toString().replace("-", ""));
//		}
//		compareResult.setHashcode(simHashCode);
//		save(hashSplit, dataid, compareResult);
//		return compareResult;
//	}
//
//	/**
//	 * 查找相似性
//	 *
//	 * @param posibleSet
//	 * @param hashSplit
//	 * @return
//	 */
//	private CompareResult findSimlarity(Set<Integer> posibleSet, Short[] hashSplit) {
//		CompareResult result = null;
//		int minDistance = -1;
//		for (Integer dataIndex : posibleSet) {
//			// 计算两个hash值的海明距离
//			int distance = hamingDistance(hashSplit, dataList.get(dataIndex));
//			if (distance < 100) {
//				if(minDistance==-1 || distance<minDistance){
//					minDistance = distance;
//					result = propertyToBean();
//					result.setDistance(distance);
//				}
//			}
//		}
//		return result;
//	}
//
//	/**
//	 * 查找可能相似的simhashcode集合
//	 *
//	 * @param hashSplit
//	 * @return
//	 */
//	private Set<Integer> findPossibleSet(Short[] hashSplit) {
//		Set<Integer> posibleSet = new HashSet<>();// 这里是可能重复的hash值在dataList中的下表
//
//		int datalength = dataList.size();
//
//		int processNumber = 30;
//		int taskLength = (datalength % processNumber == 0 ? datalength / processNumber : datalength / processNumber + 1);
//		ExecutorService executor = Executors.newScheduledThreadPool(processNumber);
//		List<Callable<Set<Integer>>> list = new ArrayList<Callable<Set<Integer>>>();
//		int base = 0;
//		for (int i = 0; i < processNumber; i++) {
//			base = i * taskLength;
//			list.add(new FindPossibleSetTask(base, taskLength, hashSplit));
//		}
//		try {
//			List<Future<Set<Integer>>> result = executor.invokeAll(list);
//			for (int i = 0; i < result.size(); i++) {
//				Future<Set<Integer>> f = result.get(i);
//				try {
//					Set<Integer> set = f.get();
//					if (set != null && !set.isEmpty()) {
//						posibleSet.addAll(set);
//					}
//				} catch (ExecutionException e) {
//					e.printStackTrace();
//				}
//			}
//			executor.shutdown();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return posibleSet;
//	}
//
//	private void save(Short[] hashSplit, String dataid, CompareResult result) {
//		dataList.add(hashSplit);
//		dataIdMap.put(dataid, hashSplit);
//	}
//
//	/**
//	 * 判断 a 与 b 的二进制是 否完全匹配
//	 *
//	 * @param a
//	 * @param b
//	 * @return
//	 */
//	private static boolean completeMath(int a, int b) {
//		return a == b;
//	}
//
//	public static int hamingDistance(BigInteger value, BigInteger value2) {
//		return hamingDistance(splitFour(value), splitFour(value2));
//	}
//
//	/**
//	 * 计算被分为四个short类型的数 的 海明距离
//	 *
//	 * @param value1
//	 * @param value2
//	 * @return
//	 */
//	private static int hamingDistance(Short[] value1, Short[] value2) {
//		int distance = 0;
//		for (int i = 0; i < value1.length; i++) {
//			short a = value1[i];
//			short b = value2[i];
//			int c = a ^ b;// 两个值异或的值
//			for (int index = 0; index < size; index++) {
//				if (isBitSet(c, index)) {
//					distance++;
//				}
//			}
//		}
//		return distance;
//	}
//
//	private static boolean isBitSet(int b, int pos) {
//		return (b & (1 << pos)) != 0;
//	}
//
//	private static int getPart(final BigInteger data, int part) {
//		int size = 16;
//		BigInteger mask = new BigInteger("1").shiftLeft(size).subtract(new BigInteger("1"));
//		BigInteger value = data.shiftRight(size * (part - 1));
//		BigInteger result = value.and(mask);
//		return result.intValue();
//	}
//
//	private static BigInteger mask = new BigInteger("1").shiftLeft(size).subtract(new BigInteger("1"));
//
//	/**
//	 * 从高位到低位的4个整数,因为是16位所以选择 short类型
//	 *
//	 * @param data
//	 * @return
//	 */
//	private static Short[] splitFour(final BigInteger data) {
//		BigInteger value = data;
//		Short[] result = new Short[length];
//		for (int i = 0; i < length; i++) {
//			result[length - 1 - i] = value.and(mask).shortValue();
//			value = value.shiftRight(size);
//		}
//		return result;
//	}
//
//	private static CompareResult propertyToBean() {
//		CompareResult bean = new CompareResult();
//		bean.setRepeat(false);
//		return bean;
//	}
//
//	private static Properties beanToProperties(CompareResult bean) {
//		Properties properties = new Properties();
//		properties.setProperty("similarityNo", bean.getSimilarityNo());
//		properties.setProperty("dataid", bean.getDataid());
//		properties.setProperty("repeat", String.valueOf(bean.isRepeat()));
//		properties.setProperty("hashcode", bean.getHashcode().toString(10));
//		return properties;
//	}
//
//	public static void main(String[] args) {
//		String hash1 = "2812154493899963526";
//		String hash2 = "2666912306405768326";
//		Short[] value1 = splitFour(new BigInteger(hash1, 10));
//		Short[] value2 = splitFour(new BigInteger(hash2, 10));
//		System.out.println(hamingDistance(value1, value2));
//	}
//
//	/**
//	 * 查找可能集合的线程
//	 *
//	 * @author Administrator
//	 *
//	 */
//	class FindPossibleSetTask implements Callable<Set<Integer>> {
//		private int start = 0;
//		private int length = 0;
//		private Short[] data;
//
//		FindPossibleSetTask(int start, int length, Short[] data) {
//			this.start = start;
//			this.length = length;
//			this.data = data;
//		}
//
//		public Set<Integer> call() throws Exception {
//			int end = start + length < dataList.size() ? start + length : dataList.size();
//			int index = 0;
//			Set<Integer> posibleSet = new HashSet<Integer>();
//			while (index < SimHashComparator.length) {
//				for (int i = start; i < end; i++) {
//					if (!posibleSet.contains(i) && completeMath(dataList.get(i)[index], data[index])) {
//						posibleSet.add(i);
//					}
//				}
//				index++;
//			}
//			return posibleSet;
//		}
//	}
//
//	/**
//	 * 查找海明距离
//	 *
//	 * @author Administrator
//	 *
//	 */
//	class FindSimlarityTask implements Callable<CompareResult> {
//		private int start = 0;
//		private int length = 0;
//		private Short[] data;
//		private Set<Integer> posibleSet;
//
//		public FindSimlarityTask(int start, int taskLength, Set<Integer> posibleSet, Short[] data) {
//			this.data = data;
//			this.length = taskLength;
//			this.start = start;
//			this.posibleSet = posibleSet;
//		}
//
//		public CompareResult call() throws Exception {
//			CompareResult result = null;
////			int end = start + length < posibleSet.size() ? start + length : posibleSet.size();
////			Iterator<Integer> it = posibleSet.iterator();
////			int index = 0;
////			Integer dataIndex = null;
////			while (it.hasNext()) {
////				if (index >= start && index < end) {
////					dataIndex = it.next();
////					// 计算两个hash值的海明距离
////					int distance = hamingDistance(data, dataList.get(dataIndex));
////					if (distance < 4) {
////						Properties properties = bdb.getStore().get(dataList.get(dataIndex));
////						result = propertyToBean(properties);
////						result.setDistance(distance);
////						break;
////					}
////				}
////				index++;
////			}
//			return result;
//		}
//
//	}
//}
