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
	private Properties prop = new Properties();
	Map<String, String> properties_map = readProperties(ClassLoader.getSystemResource("").toString().replaceAll("file:", "").replaceAll("test-classes/", "")+"classes/jdbc.properties");
	// JDBC driver name and database URL
	public   final String JDBC_DRIVER = properties_map.get("driverClass");  
	public   final String DB_URL = properties_map.get("jdbcUrl"); 

	   //  Database credentials
	public final String USER = properties_map.get("username"); 
	public final String PASS = properties_map.get("password"); 
	   
	   public static void main(String[] args) {
		   ClearDBUtil cd = new ClearDBUtil();
		   cd.clearDB();
		   //System.out.println(ClassLoader.getSystemResource("").toString().replaceAll("file:", "").replaceAll("test-classes/", "")+"classes/jdbc.properties");
	   }
	   
	   public void clearDB(){
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
		      		+ " WHERE table_schema = 'wxmall';";
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
	   

	   
	   private Map<String, String> readProperties(String path)
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
