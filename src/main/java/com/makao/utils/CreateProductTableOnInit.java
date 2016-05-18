package com.makao.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.InitializingBean;

import com.makao.utils.ClearDBUtil;

/**
 * @description: 测试时使用，在spring启动时清空数据库，保证测试干净
 * @author makao
 * @date 2016年5月8日
 */
public class CreateProductTableOnInit implements ServletContextListener{
	@Override
	public void contextInitialized(ServletContextEvent context){
		System.out.println("***********Create Product Table **************");
		String path = context.getServletContext().getRealPath("/")+"WEB-INF/classes/jdbc.properties";
		InputStream is;
		Properties prop = new Properties();
		try {
			is = new FileInputStream(path);
			prop.load(is);
			is.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Map<String,String> properties_map = new HashMap<String,String>();
		for(Object id: prop.keySet())
		{
			properties_map.put(id.toString(), prop.getProperty(id.toString()));
		}
		String JDBC_DRIVER = properties_map.get("driverClass");  
		String DB_URL = properties_map.get("jdbcUrl"); 

		String USER = properties_map.get("username"); 
		String PASS = properties_map.get("password"); 
		creatProductTable("wxmall",JDBC_DRIVER,DB_URL,USER,PASS);
	}
	
	
	/**
	 * @param databaseName
	 * 服务器第一次启动时，如果没有Product总表，则建一个
	 */
	public static void creatProductTable(String databaseName,String JDBC_DRIVER,String DB_URL,String USER,String PASS){
		   Connection conn = null;
		   PreparedStatement ps = null;
		   try{
		      //STEP 2: Register JDBC driver
		      Class.forName(JDBC_DRIVER);

		      //STEP 3: Open a connection
		      System.out.println("Connecting to a selected database...");
		      conn = DriverManager.getConnection(DB_URL, USER, PASS);
		      System.out.println("Connected database successfully...");
		      
		      //STEP 4: Execute a query
		      System.out.println("Creating table Product in given database...");
		      
		      String sql = "CREATE TABLE IF NOT EXISTS `"
						+ "Product"
						+ "` (`id` int(11) NOT NULL AUTO_INCREMENT,"
						+ "`productName` varchar(30) NOT NULL,"
						+ "`catalog` varchar(30),"
						+ "`showWay` varchar(2) DEFAULT 's',"
						+ "`price` varchar(10),"
						+ "`standard` varchar(50),"
						+ "`marketPrice` varchar(10),"
						+ "`label` varchar(16),"
						+ "`coverSUrl` varchar(50),"
						+ "`coverBUrl` varchar(50),"
						+ "`inventory` int(11),"
						+ "`sequence` int(11),"
						+ "`status` varchar(30),"
						+ "`description` varchar(100),"
						+ "`origin` varchar(30),"
						+ "`salesVolume` int(11),"
						+ "`likes` int(11),"
						+ "`subdetailUrl` varchar(50),"
						+ "`detailUrl` varchar(50),"
						+ "`isShow` varchar(5) DEFAULT 'yes',"
						+ "`areaId` int(11),"
						+ "`cityId` int(11),"
						+ "PRIMARY KEY (`id`))";
		      
		      ps = conn.prepareStatement(sql);
		      ps.execute();      
		      System.out.println("Table Product created in given database...");
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(ps!=null)
		        	 ps.close();
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

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
