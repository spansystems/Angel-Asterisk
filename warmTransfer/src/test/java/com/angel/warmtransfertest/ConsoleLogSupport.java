package com.angel.warmtransfertest;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.logging.LogManager;

import org.junit.Assert;

/**
 * By default SL4J-JCL uses JDK1.4 logging as no lo4j api is packaged are is
 * available in classpath as JBoss already provides binding.
 * 
 * <p>
 *   This class existing to only help us to debug details while running JUnit test cases.
 * 
 * @author thanujkumar_sc
 * 
 */
public class ConsoleLogSupport  extends Assert
{

	static
	{
		//SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST  (ALL, OFF).
		final String logLevel = "handlers= java.util.logging.ConsoleHandler \n " + ".level= ALL \n"
				+ "  java.util.logging.ConsoleHandler.level = FINE \n "
				+ " java.util.logging.ConsoleHandler.formatter = java.util.logging.SimpleFormatter ";
		try
		{
			LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(logLevel.getBytes()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Generates random integer and returns as string.
	 *
	 * @return String - Randomly generated id.
	 */
	public static String getId()
	{
		return new BigInteger(20, new SecureRandom()).toString();
	}
	
}
