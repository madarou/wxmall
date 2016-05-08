package com.makao.test.utils;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;


/**
 * @description: TODO
 * @author makao
 * @date 2016年5月8日
 */
public class ShellExecutor {
	private static final Logger logger = Logger.getLogger(ShellExecutor.class);
	public static String location = ShellExecutor.class.getResource("").toString().replaceAll("file:", "");
	/**
	 * @param shellFile
	 * @param shellCmd
	 * @param resultFile
	 * ShellExecutor.callShell("shell脚本.sh", "shell命令，会传入到shell脚本.sh", "结果输出文件名，会传入到shell脚本.sh");
	 */
	public static void callShell(String shellFile, String shellCmd, String resultFile) {  
		File file=new File(location+resultFile);    
		if(!file.exists())    
		{    
		    try {    
		        file.createNewFile();    
		    } catch (IOException e) {    
		        // TODO Auto-generated catch block    
		        e.printStackTrace();    
		    }    
		}    
		String[] cmds = {"/bin/sh",location+shellFile,shellCmd,location+resultFile};
	    try {  
	        Process process = Runtime.getRuntime().exec(cmds);  
	        int exitValue = process.waitFor();  
	        if (0 != exitValue) {  
	        	logger.error("call shell failed. error code is :" + exitValue);  
	        }  
	    } catch (Throwable e) {  
	    	logger.error("call shell failed. " + e);  
	    }  
	}
	public static void main(String[] args){
		System.out.println(ShellExecutor.location);
	}
}
