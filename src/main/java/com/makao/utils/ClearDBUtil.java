package com.makao.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @description: TODO
 * @author makao
 * @date 2016年5月8日
 */
public class ClearDBUtil {
	private static Properties prop = new Properties();
	//public static Map<String, String> properties_map = readProperties(ClassLoader.getSystemResource("").toString().replaceAll("file:", "")+"jdbc.properties");
	// JDBC driver name and database URL
	public static String JDBC_DRIVER = "com.mysql.jdbc.Driver";//properties_map.get("driverClass");  
	public static String DB_URL = "jdbc:mysql://localhost:3306/wxmall";//properties_map.get("jdbcUrl"); 

	   //  Database credentials
	public static String USER = "root";//properties_map.get("username"); 
	public static String PASS = "660419";//properties_map.get("password"); 
	   
	   public static void main(String[] args) {
		   //ClearDBUtil.dropDBTables("wxmall");
		   //System.out.println(ClassLoader.getSystemResource("").toString().replaceAll("file:", "")+"jdbc.properties");
	   }
	   public static String test(){
		   return ClassLoader.getSystemResource("").toString();
	   }
	 
	  /**
	 * @param databaseName
	 * 清空databaseName数据库里的所有表，但不删除
	 */
	public static void clearDBTables(String databaseName){
		  
	  }
	 /**
	 * 删除databaseName数据库里的所有表
	 */
	public static void dropDBTables(String databaseName){
		   Connection conn = null;
		   Statement stmt = null;
		   try{
		      //STEP 2: Register JDBC driver
		      Class.forName(JDBC_DRIVER);

		      //STEP 3: Open a connection
		      System.out.println("Connecting to a selected database...");
		      conn = DriverManager.getConnection(DB_URL, USER, PASS);
		      System.out.println("Connected database successfully...");
		      
		      //STEP 4: Execute a query
		      System.out.println("Deleting table in given database...");
		      stmt = conn.createStatement();
		      
		      String sql = "SELECT concat('DROP TABLE IF EXISTS ', table_name, ';')"
		      		+ " FROM information_schema.tables"
		      		+ " WHERE table_schema = '"+databaseName+"';";
		      ResultSet rs = stmt.executeQuery(sql);
		      while(rs.next()){
		    	  System.out.println(rs.getString(1));
		    	  Statement s = conn.createStatement();
		    	  s.executeUpdate(rs.getString(1));
		    	  s.close();
		      }
		      rs.close();
		      System.out.println("Table  deleted in given database...");
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            conn.close();
		      }catch(SQLException se){
		      }// do nothing
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		   System.out.println("Goodbye!");
	   }
	   

	   
	   private static Map<String, String> readProperties(String path)
		{
			InputStream is;
			try {
				is = new FileInputStream(path);
				prop.load(is);
				is.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			Map<String,String> sinceid = new HashMap<String,String>();
			for(Object id: prop.keySet())
			{
				sinceid.put(id.toString(), prop.getProperty(id.toString()));
			}
			return sinceid;
		}
	  
}
