//package com.tech.finger;
//
//import com.sleepycat.bind.EntryBinding;
//import com.sleepycat.bind.serial.SerialBinding;
//import com.sleepycat.bind.serial.StoredClassCatalog;
//import com.sleepycat.collections.StoredMap;
//import com.sleepycat.je.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Properties;
//
///**
// * 指纹 存储层
// *
// * @author Administrator
// *
// */
//public class BerkeleyFingerDB {
//	public static final Logger LOG = LoggerFactory.getLogger("BerkeleyFingerDB");
//	private  Database myDb = null;//数据原文
//	private  Database database = null;
//	private  Environment dbEnv = null;
//	private  String databasename = "content";
//	private  final String CLASS_CATALOG = "java_class_catalog";
//	private  StoredMap<Short[],Properties> contentDB;
//	private  StoredClassCatalog classCatalog;
//	private  Object putLock = new Object();
//	public BerkeleyFingerDB(String path) throws IOException{
//		init(path);
//	}
//	private void init(String path) throws IOException {
//		LOG.info("初始化指纹库......"+path);
//		try {
//			File file = new File(path);
//			if (!file.exists())
//				file.mkdirs();
//			EnvironmentConfig envConf = new EnvironmentConfig();
//			envConf.setAllowCreate(true);
//			envConf.setTransactional(true);
//			envConf.setConfigParam("je.log.fileMax", "1000000000");
//			dbEnv = new Environment(file, envConf);
//
//			DatabaseConfig dbConf = new DatabaseConfig();
//			dbConf.setAllowCreate(true);
//			dbConf.setTransactional(true);
//			dbConf.setSortedDuplicates(false);
//
//			myDb = dbEnv.openDatabase(null, CLASS_CATALOG, dbConf);
//			classCatalog = new StoredClassCatalog(myDb);
//
//			// 打开
//			database = dbEnv.openDatabase(null, databasename, dbConf);
//			EntryBinding<Short[]> keyBinding = new SerialBinding<Short[]>(classCatalog,Short[].class);
//	        EntryBinding<Properties> valueBinding =new SerialBinding<Properties>(classCatalog,Properties.class);
//	        contentDB = new StoredMap<Short[],Properties>(database,keyBinding, valueBinding, true);
//		} catch (Exception de) {
//			throw new IOException(de.getMessage());
//		}
//		LOG.info("初始化指纹库"+path+"完成");
//	}
//
//	public  void close(){
//		LOG.info("关闭内容指纹库......");
//		try {
//			database.close();
//			classCatalog.close();
//			if (dbEnv != null) {
//				dbEnv.cleanLog();
//				dbEnv.close();
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//
//
//	public  synchronized void saveRecord(Short[] key,Properties value){
//		synchronized (putLock) {
//			contentDB.put(key, value);
//		}
//	}
//
//
//
//	public  synchronized void update(Short[] key, Properties value){
//		synchronized (putLock) {
//			contentDB.put(key, value);
//		}
//	}
//
//	public  StoredMap<Short[],Properties> getStore(){
//		return contentDB;
//	}
//	/**
//	 * 请出数据库中的数据
//	 * @param docNo
//	 * @param object
//	 * @throws IOException
//	 * @throws DatabaseException
//	 */
//	public  synchronized void clear()throws IOException, DatabaseException {
//		synchronized (dbEnv) {
//			Transaction tra = null;
//			try{
//				database.close();
//				dbEnv.removeDatabase(null, databasename);
//			}catch(Exception e){
//				if(tra != null){
//					tra.abort();
//				}
//				e.printStackTrace();
//			}finally{
//				//再次打开db
//				DatabaseConfig dbConf = new DatabaseConfig();
//				dbConf.setAllowCreate(true);
//				dbConf.setTransactional(true);
//				dbConf.setSortedDuplicates(false);
//				database = dbEnv.openDatabase(null, databasename, dbConf);
//			}
//		}
//	}
//	public  Properties getRecord(Short[] key){
//		return contentDB.get(key);
//	}
//
//	public static void main(String[] args) throws IOException{
//		Short[] key = new Short[]{3,4,5,4};
//		Short[] key1 = new Short[]{3,4,5,4};
//		System.out.println(key.equals(key1));
//		String value = "标题";
//		Properties properties = new Properties();
//		properties.put("title", value);
//		BerkeleyFingerDB db = new BerkeleyFingerDB("");
//		db.saveRecord(key, properties);
//		System.out.println(db.getStore().get(key1));
//	}
//}
