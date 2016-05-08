package com.makao.utils;

import org.apache.log4j.Logger;


/**
 * @description: TODO
 * @author makao
 * @date 2016年5月8日
 */
public class ShellExecutor {
	private static final Logger logger = Logger.getLogger(ShellExecutor.class);
	public static void callShell(String shellString) {  
		String[] cmds = {"/bin/sh","-c",shellString};
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
}
