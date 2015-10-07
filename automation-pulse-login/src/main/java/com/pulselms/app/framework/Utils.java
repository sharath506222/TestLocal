package com.pulselms.app.framework;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.testng.Reporter;


/**
 * Utils class will have some common utility methods required
 * @author Cognizant
 *
 */

public abstract class Utils {
	
	public static String twoStringsWithNewLine(String one, String two) {
		
		String separator = System.getProperty( "line.separator" );
		StringBuilder lines = new StringBuilder( one );
		lines.append( separator );
		lines.append( two );
		return lines.toString( );
		
	}
	
	/**
	 * Windows Only method
	 * 
	 * @param serviceName
	 * @return true if process status is running, false otherwise
	 */
	public static boolean isProcessRuning(String serviceName) {
		if (EnvParameters.getOSname() == EnvParameters.OSType.windows) {
			Process p;
			try {
				p = Runtime.getRuntime().exec("tasklist");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(p.getInputStream()));
				String line;

				while ((line = reader.readLine()) != null) {
					if (line.contains(serviceName)) {
						Reporter.log(serviceName + " is running", true);
						return true;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		} else {
			LoggerUtil.log(
					"Client OS is not window, cannot check running process: "
							+ serviceName);
			return false;
		}
	}

	/**
	 * Kills the specified process (Windows Only method)
	 * 
	 * @param serviceName
	 */
	public static void killProcess(String serviceName) {
		Reporter.log("Trying to kill " + serviceName, true);

		String KILL = "taskkill /F /T /IM ";
		if (EnvParameters.getOSname() == EnvParameters.OSType.windows) {
			try {
				Runtime.getRuntime().exec(KILL + serviceName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			Reporter.log(
					"Client OS is not window, can not kill " + serviceName,
					true);
	}
	


}
