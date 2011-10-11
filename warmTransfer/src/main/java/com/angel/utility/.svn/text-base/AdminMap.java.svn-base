/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.angel.utility;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.angel.agent.Admin;

/**
 * Admin map class. If there is no real difference between admin and agent then
 * we can getrid of this.
 * 
 * @author @author <a href="mailto:ravindra_d@spanservices.com"> Ravindra D </a>
 */
public class AdminMap
{

	private static AdminMap adminMap;
	private Map<String, Admin> adminHashMap = new ConcurrentHashMap<String, Admin>();
	private static final Logger LOG = Logger.getLogger(AdminMap.class);

	static
	{
		adminMap = new AdminMap();
	}

	/**
	 * Single ton getter for the admin map;
	 * 
	 * @return
	 */
	public static AdminMap getAdminMap()
	{
		return adminMap;
	}

	/**
	 * Sets the admin.
	 * 
	 * @param id
	 *            of the admin
	 * @param admin
	 *            the admin object
	 */
	public void setAdmin(final String id, final Admin admin)
	{
		adminHashMap.put(id, admin);
	}

	/**
	 * Gets the admin with string value admin id.
	 * 
	 * @param key
	 *            the admin id
	 * @return the admin object stored in the hash map.
	 */
	public Admin getAdmin(final String key)
	{
		if (adminHashMap.containsKey(key))
		{
			return adminHashMap.get(key);
		}
		else
		{
			return null;
		}
	}

	/**
	 * removes the admin associated with key.
	 * 
	 * @param key
	 *            the admin id.
	 */
	public void removeAdmin(final String key)
	{
		adminHashMap.remove(key);
	}

	/**
	 * Boolean handler for checking the agent exist.
	 * 
	 * @param agent
	 *            the agent id.
	 * @return boolean stating found or not.
	 */
	public boolean checkAdminExist(final String agent)
	{

		try
		{
			return adminHashMap.containsKey(agent);
		}
		catch (NullPointerException e)
		{
			LOG.warn("Admin not present in the Map", e);
		}
		return false;
	}

	/**
	 * Returns the admin.
	 * 
	 * @param id
	 *            the id of the admin
	 * @return the Admin object.
	 */
	public Admin getAdminById(final String id)
	{
		final Collection<Admin> c = adminHashMap.values();
		final Iterator<Admin> it = c.iterator();
		while (it.hasNext())
		{
			final Admin admin = (Admin) it.next();
			if (admin.getChannelId().equals(id))
			{
				return admin;
			}
		}
		return null;
	}
}
