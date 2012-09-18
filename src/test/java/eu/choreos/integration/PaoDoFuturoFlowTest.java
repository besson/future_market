package eu.choreos.integration;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.choreos.abstraction.ChoreographyAbstractor;
import eu.choreos.admin.ChoreographyManager;
import eu.choreos.vv.clientgenerator.Item;
import eu.choreos.vv.clientgenerator.WSClient;
import eu.choreos.vv.interceptor.MessageInterceptor;
import eu.choreos.ws.Services;

public class PaoDoFuturoFlowTest extends ChoreographyAbstractor {
	
	private MessageInterceptor interceptor;
	private static ChoreographyManager manager;
	
	@BeforeClass
	public static void setUp() throws Exception {
		manager = new ChoreographyManager();
		manager.enactChoreography();
		
		buildChoreographyAbstraction();
	}
	
	@Before
	public void startProxy() {
		interceptor =  new MessageInterceptor("5002");
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
		manager.stopChoreography();
	}
	
	@After
	public void stopProxy() {
		interceptor.stop();
	}
	
	@Test
	public void shouldRegisterEndpointInTheRegistry() throws Exception {
		interceptor.interceptTo(registryURI);
		introduceProxy(interceptor.getProxyWsdl());

		WSClient client = new WSClient(paoDoFuturoURI);
		client.request("registerSupermarket", "http://localhost:8001/paoDoFuturo");
		
		Item message = interceptor.getMessages().get(0);
		
		assertEquals("setEndpoint", message.getName());
		assertEquals("http://localhost:8001/paoDoFuturo?wsdl", message.getContent("endpoint"));
	}

	@Test
	public void shouldCheckPriceInSM1() throws Exception {
		interceptor.interceptTo(sm1URI);
		introduceProxy(interceptor.getProxyWsdl());
		
		WSClient client = new WSClient(paoDoFuturoURI);
		client.request("searchForProduct", "milk");
		
		Item message = interceptor.getMessages().get(0);
		
		assertEquals("checkPriceOf", message.getName());
		assertEquals("milk", message.getContent("arg0"));
	}
	
	private static void introduceProxy(String endpoint) throws Exception {
		new WSClient(Services.WS_REGISTRY.getEndpoint()).request("setEndpoint", "SM1", endpoint);
		new WSClient(Services.WS_REGISTRY.getEndpoint()).request("setEndpoint", "SM_REGISTRY", endpoint);
	}	
}
