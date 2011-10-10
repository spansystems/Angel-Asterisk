/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.angel.rest;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RestResource extends Application {

    private Set<Object> singleton = new HashSet<Object>();
    private Set<Class<?>> empty = new HashSet<Class<?>>();
     private static final Logger LOG = LoggerFactory.getLogger(RestResource.class);

    public RestResource() {
        LOG.info("Adding rest resource");
        singleton.add(new AsteriskRest());
        LOG.info("Resouce added successfully");
    }

    @Override
    public Set<Class<?>> getClasses() {
        return empty;
    }

    @Override
    public Set<Object> getSingletons() {
        return singleton;
    }
}
