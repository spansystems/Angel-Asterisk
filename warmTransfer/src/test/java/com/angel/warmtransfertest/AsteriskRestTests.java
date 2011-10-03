package com.angel.warmtransfertest;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockDispatcherFactory;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.plugins.server.resourcefactory.SingletonResource;
import org.junit.Before;
import org.junit.Test;

import com.angel.rest.AsteriskRest;
import com.angel.rest.IAsteriskRest;

public class AsteriskRestTests extends ConsoleLogSupport
{

	private SingletonResource singleton;

	private Dispatcher dispatcher;

	@Before
	public void init()
	{
		IAsteriskRest rest = new AsteriskRest();
		singleton = new SingletonResource(rest);
		dispatcher = MockDispatcherFactory.createDispatcher();
		dispatcher.getRegistry().addResourceFactory(singleton);
		System.out.println("Initialized");
	}

	@Test
	public void test() throws URISyntaxException, IOException
	{
		String url = IAsteriskRest.basePath.replace("{account-id}", "200").replace("{page-id}", "100")
				+ IAsteriskRest.agentPath.replace("{agent-id}", "200");
		MockHttpRequest req = MockHttpRequest.get(url);
		MockHttpResponse resp = new MockHttpResponse();

		String json = "{\"event\":\"PARK_CALL\"},\"data\":{\"agent\":200,\"caller\":50},";
//		String json = "{\"event\":\"PARK_CALL\"}";
		req.contentType(MediaType.APPLICATION_JSON);
		req.content(json.getBytes());
		System.out.println("Invoking - " + req.getUri().getPath());
		dispatcher.invoke(req, resp);

		System.out.println("Response : " + resp.getStatus());		
		System.out.println("Response : " + resp.getContentAsString());

	}

}
