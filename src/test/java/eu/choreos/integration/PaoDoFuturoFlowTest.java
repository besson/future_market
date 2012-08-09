package eu.choreos.integration;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

import org.junit.*;

import eu.choreos.admin.ChoreographyManager;
import eu.choreos.vv.abstractor.Choreography;
import eu.choreos.vv.abstractor.Service;
import eu.choreos.vv.clientgenerator.Item;
import eu.choreos.vv.clientgenerator.WSClient;
import eu.choreos.vv.interceptor.MessageInterceptor;
import eu.choreos.ws.Services;

public class PaoDoFuturoFlowTest {
	
	private static Service paoDoFuturo;
	private static String paoDoFuturoURI;
	private static String sm1URI;
	private static String registryURI;
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
	
	private static void buildChoreographyAbstraction()
			throws FileNotFoundException {
		Choreography futureMarket = Choreography.build("./src/test/resources/futureMarket.yml");
		paoDoFuturo = futureMarket.getServicesForRole("supermarket").get(0);
		paoDoFuturoURI = ChoreographyManager.completeURI(paoDoFuturo.getUri());
		sm1URI = ChoreographyManager.completeURI(paoDoFuturo.getParticipants().get(0).getUri());
		registryURI = ChoreographyManager.completeURI(paoDoFuturo.getParticipants().get(1).getUri());
	}

	private static void introduceProxy(String endpoint) throws Exception {
		new WSClient(Services.WS_REGISTRY.getEndpoint()).request("setEndpoint", "SM1", endpoint);
		new WSClient(Services.WS_REGISTRY.getEndpoint()).request("setEndpoint", "SM_REGISTRY", endpoint);
	}	
}
