/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.angel.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.angel.agent.Agent;
import com.angel.rest.InputJson;

public abstract class IEvents implements Runnable
{

	InputJson json;
	Agent agent;
	public static final Logger LOG = LoggerFactory.getLogger(IEvents.class);
}