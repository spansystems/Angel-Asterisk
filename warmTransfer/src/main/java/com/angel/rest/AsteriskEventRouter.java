package com.angel.rest;

import com.angel.events.IEvents;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsteriskEventRouter
{

	private static final Logger LOG = LoggerFactory.getLogger(AsteriskEventRouter.class);
	/**
	 * Thread pool executor, executes for connecting caller.
	 */
	private ThreadPoolExecutor executor;
	/**
	 * Singleton Object
	 */
	private static AsteriskEventRouter router;
	/**
	 * Boolean flag to stop the Event Router
	 */
	private boolean isStopped;

	/**
	 * Private Constuctor to avoid duplicate objects
	 */
	private AsteriskEventRouter()
	{
		executor = new ThreadPoolExecutor(5, 7, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new AsteriskThreadFactory());
	}

	/**
	 * Retrieves the AsteriskEventRouter singleton object
	 * 
	 * @return - Returns the AsteriskEventRouter object
	 */
	public static AsteriskEventRouter getInstance()
	{
		if (router == null)
		{
			router = new AsteriskEventRouter();
		}
		return router;
	}

	/**
	 * Executes the given event
	 * 
	 * @param event
	 *            - Asterisk Event to execute
	 * @return - Returns TRUE if event successfully executed, FALSE otherwise
	 */
	public boolean route(IEvents event)
	{
		boolean isRouted = false;
		try
		{
			if (isStopped)
			{
				LOG.info("AsteriskEventRouter is not running.");
			}
			else
			{
				executor.execute(event);
				isRouted = true;
			}
		}
		catch (Exception e)
		{
			LOG.info("Exception while executing the AsteriskEvent ." + e.getStackTrace());
		}
		return isRouted;
	}

	/**
	 * Method to shutdown the AsteriskEventRouter thread pool
	 */
	public void shutdown()
	{
		isStopped = true;
		executor.shutdown();
	}

	/**
	 * Method to stop the event router from executing events
	 */
	public void stopEventRouter()
	{
		isStopped = true;
	}

	private static class AsteriskThreadFactory implements ThreadFactory
	{

		/**
		 * Constructs a new Thread. Implementations may also initialize
		 * priority, name, daemon status, ThreadGroup, etc.
		 * 
		 * Specified by: newThread(...) in ThreadFactory
		 * 
		 * @param r
		 *            a runnable to be executed by new thread instance
		 * @return constructed thread, or null if the request to create a thread
		 *         is rejected
		 */
		@Override
		public Thread newThread(Runnable r)
		{
			/**
			 * Constructs a new Thread.
			 */
			final Thread thread = new Thread(r);

			return thread;
		}
	}
}
