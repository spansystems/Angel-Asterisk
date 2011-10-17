/*
 * This class handles the connection with Asterisk server and also manages
 * Agents and Admin logins and their functionalities. It also contains event
 * listner implementation and hence is the core class for event handling.
 */
package com.angel.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.StatusAction;

/**
 * Manager server class.
 * 
 */
public final class ManagerServer extends IManager
{

	private static AsteriskServer asteriskServer;
	private static ManagerConnection managerConnection;
	private static String outboundproxy;
	private static String asteriskIP;
	private static String loginName;
	private static String loginPwd;
	private static int asteriskPort;
	private ManagerEvents manager;
	private AsteriskChannelEvents asteriskChannelEventHandler;

	/**
	 * Constructor is used to initate the connection with asterisk server with
	 * ip, username and passwd. Agent and Admin are instantiazed at this point.
	 */
	static
	{
		loadConfig();
		asteriskServer = new DefaultAsteriskServer(asteriskIP, asteriskPort, loginName, loginPwd);
		managerConnection = asteriskServer.getManagerConnection();
	}

	public ManagerServer()
	{
		manager = new ManagerEvents();
		asteriskChannelEventHandler = new AsteriskChannelEvents();
	}

	/**
	 * Adding the listeners to this class for asterisk events and status events.
	 * 
	 * @return AsteriskServer the asterisk server.
	 */
	public static AsteriskServer getAsteriskServer()
	{
		return asteriskServer;
	}

	public static String getOutboundproxy()
	{
		return outboundproxy;
	}

	public static ManagerConnection getManagerConnection()
	{
		return managerConnection;
	}

	public void run() throws ManagerCommunicationException, IllegalArgumentException, IllegalStateException, IOException, TimeoutException
	{
		asteriskChannelEventHandler.initialize();
		manager.initialize();
		final StatusAction action = new StatusAction();
		managerConnection.sendAction(action);
	}

	/*
	 * Receives NewAsteriskChannel channel from Asterisk server.
	 */

	/*
	 * Reads the property file to login to Asterisk server
	 */
	private static void loadConfig()
	{
		try
		{
			final Properties props = ManagerServer.loadProperties(ManagerServer.class, "/resources/conf.properties");
			asteriskIP = props.getProperty("asteriskIP");
			loginName = props.getProperty("userName");
			loginPwd = props.getProperty("password");
			outboundproxy = props.getProperty("outBoundProxy");
			asteriskPort = Integer.parseInt(props.getProperty("asteriskPort"));
		}
		catch (IOException ex)
		{
			LOG.error("IO Exception while reading the configuration file.", ex);
		}
	}

	/**
	 * This method search for the property file in local resource.
	 * 
	 * @param clazz
	 *            name of the class.
	 * @param resourceName
	 *            configuration file name
	 */
	public static Properties loadProperties(final Class<?> clazz, final String resourceName) throws IOException
	{
		final Properties props = new Properties();
		InputStream inStream = null;

		try
		{
			inStream = clazz.getResourceAsStream(resourceName);
			if (inStream == null)
			{
				throw new IOException("Error while loading " + resourceName + ": resource not found in the JAR file");
			}
			if (resourceName.endsWith(".properties"))
			{
				props.load(inStream);
			}
			else
			{
				throw new IOException("Property file " + resourceName + " must end with .xml or .properties");
			}
			return props;
		}
		finally
		{
			if (inStream != null)
			{
				inStream.close();
			}
		}
	}

}
