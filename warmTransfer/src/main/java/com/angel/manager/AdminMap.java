/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.angel.manager;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.angel.agent.Admin;

// ToDo : Change this class or have the functionality as part of the agent class
// with differntiator being Role.
public class AdminMap
{

	public static void setAdmin(String customer, Admin admin)
	{
		adminMap.put(customer, admin);
	}

	public static Admin getAdmin(String key)
	{
		if (adminMap.containsKey(key))
		{
			return adminMap.get(key);
		}
		else
		{
			return null;
		}
	}

	public static void removeAdmin(String key)
	{
		adminMap.remove(key);
	}

	private static Map<String, Admin> adminMap = new ConcurrentHashMap<String, Admin>();

	static boolean checkAdminExist(String agent)
	{
		if (adminMap.containsKey(agent))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static Admin getAdminById(String id)
	{
		Collection<Admin> c = adminMap.values();
		Iterator<Admin> it = c.iterator();
		while (it.hasNext())
		{
			Admin admin = (Admin) it.next();
			if (admin.getChannelId().equals(id))
				return admin;
		}
		return null;
	}
}
