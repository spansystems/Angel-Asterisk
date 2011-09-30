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

import com.angel.agent.Admin;
import com.angel.agent.Agent;

public final class ManagerServer extends IManager
{

	static AsteriskServer asteriskServer;
	static ManagerConnection managerConnection;
	Agent agent;
	Admin admin;
	private String Asterisk_IP;
	private String loginName;
	private String loginPwd;
	private ManagerEvents manager;
	private AsteriskChannelEvents asteriskChannelClass;

	/*
	 * Constructor is used to initate the connection with asterisk server with
	 * ip, username and passwd. Agent and Admin are instantiazed at this point.
	 */
	ManagerServer()
	{
		readFileForIP();
		asteriskServer = new DefaultAsteriskServer(Asterisk_IP, loginName, loginPwd);
		managerConnection = asteriskServer.getManagerConnection();
		manager = new ManagerEvents();
		asteriskChannelClass = new AsteriskChannelEvents();
	}

	/*
	 * Adding the listeners to this class for asterisk events and status events.
	 */

	public static AsteriskServer getAsteriskServer()
	{
		return asteriskServer;
	}

	public void run() throws ManagerCommunicationException, IllegalArgumentException, IllegalStateException, IOException, TimeoutException
	{
		try
		{
			asteriskChannelClass.run();
			manager.run();
			StatusAction action = new StatusAction();
			managerConnection.sendAction(action);
			Thread.sleep(300000);
		}
		catch (InterruptedException ex)
		{
			LOG.error("Interrupted exception", ex);
		}

	}

	/*
	 * Receives NewAsteriskChannel channel from Asterisk server.
	 */

	public static ManagerConnection getManagerConnection()
	{
		return managerConnection;
	}

	/*
	 * Reads the property file to login to Asterisk server
	 */
	private void readFileForIP()
	{
		try
		{
			final Properties props = ManagerServer.loadProperties(ManagerServer.class, "/resources/conf.properties");
			Asterisk_IP = props.getProperty("ip");
			loginName = props.getProperty("username");
			loginPwd = props.getProperty("pwd");
		}
		catch (IOException ex)
		{
			LOG.error("IO Exception while reading the configuration file.", ex);
		}
	}

	/*
	 * This method search for the property file in local resource
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
