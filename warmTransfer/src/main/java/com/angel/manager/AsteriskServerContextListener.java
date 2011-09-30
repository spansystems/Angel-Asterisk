/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.angel.manager;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.asteriskjava.live.ManagerCommunicationException;
import org.asteriskjava.manager.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author prashanth_p
 */
public class AsteriskServerContextListener implements ServletContextListener
{
	public static final Logger LOG = LoggerFactory.getLogger(AsteriskServerContextListener.class);

	/*
	 * ContextDestroyed.
	 * 
	 * @param arg0 ServletContextEvent .
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * ContextInitialized.
	 * 
	 * @param servletContextEvent
	 *            ServletContextEvent .
	 */
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent)
	{
		LOG.info("Initializing connection with asterisk server");
		connectToAseriskServer();
	}

	/**
	 * Instantiate the manager class and call run method to connect with
	 * Asterisk server
	 */
	private void connectToAseriskServer()
	{
		try
		{
			new ManagerServer().run();
		}
		catch (ManagerCommunicationException ex)
		{
			LOG.error("Manager Communication exception", ex);
		}
		catch (IllegalArgumentException ex)
		{
			LOG.error("Illegal argument exception", ex);
		}
		catch (IllegalStateException ex)
		{
			LOG.error("Illegal state exception", ex);
		}
		catch (IOException ex)
		{
			LOG.error("IO exception", ex);
		}
		catch (TimeoutException ex)
		{
			LOG.error("Time out exception", ex);
		}
	}
}
