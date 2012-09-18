package eu.choreos.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.choreos.abstraction.ChoreographyAbstractor;
import eu.choreos.admin.ChoreographyManager;
import eu.choreos.vv.clientgenerator.Item;
import eu.choreos.vv.clientgenerator.ItemImpl;
import eu.choreos.vv.clientgenerator.WSClient;
import eu.choreos.vv.interceptor.MessageInterceptor;
import eu.choreos.vv.servicesimulator.MockResponse;
import eu.choreos.vv.servicesimulator.WSMock;
import eu.choreos.ws.Services;

public class FutureMarketPlaceFlowTest extends ChoreographyAbstractor {

	private static ChoreographyManager manager;
	private static WSMock mock;
	private static MessageInterceptor interceptor;

	@BeforeClass
	public static void setUp() throws Exception {
		buildChoreographyAbstraction();

		enactChoreography();

		publishSupermarketsInTheRegistry();
	}

	@Before
	public void cleanRegistry() throws Exception {
		removeServiceEndpoint("smMock");
		removeServiceEndpoint("paoDoFuturoProxy");
	}

	@AfterClass
	public static void tearUp() throws Exception {
		manager.stopChoreography();
		mock.stop();
		interceptor.stop();
	}

	@Test
	public void shouldReturnTheCheapestPrice() throws Exception {
		createAndRegisterMock();
		
		Item shopList = createShopList("milk");

		WSClient client = new WSClient(futureMarketPlaceURI);
		Item cheapestPrice = client.request("getPriceOfProductList", shopList);

		assertEquals("0.5", cheapestPrice.getChild("return")
				.getContent("price"));
	}

	@Test
	public void shouldReturnTheFinalPriceAndAnID() throws Exception {
		Item shopList = createShopList("bread", "corn", "beer", "coke");

		WSClient client = new WSClient(futureMarketPlaceURI);
		Item cheapestPrice = client.request("getPriceOfProductList", shopList);

		assertTrue(cheapestPrice.getChild("return").getContentAsDouble("price") > 0);
		assertNotNull(cheapestPrice.getChild("return").getContentAsDouble(
				"orderId"));
	}

	@Test
	public void paoDoFuturoServiceMustReceiveThreeSearchForProductMessages()
			throws Exception {
		Item shopList = createShopList("bread", "coffee", "milk");

		interceptor = new MessageInterceptor("9001");
		interceptor.interceptTo(Services.PAO_DO_FUTURO.getEndpoint());

		registerServiceEndpoint("paoDoFuturoProxy", interceptor.getProxyWsdl());

		WSClient client = new WSClient(futureMarketPlaceURI);
		client.request("getPriceOfProductList", shopList);

		assertEquals(3, interceptor.getMessages().size());
	}

	private Item createShopList(String... items) {

		Item shopList = new ItemImpl("getPriceOfProductList");

		for (String item : items) {
			shopList.addChild("arg0").setContent(item);
		}

		return shopList;
	}

	private static WSMock createASupermarketMockWithTheCheapestPrice()
			throws Exception {
		String realsupermarketURI = Services.PAO_DO_FUTURO.getEndpoint();

		WSMock mock = new WSMock("smMock", "7001", realsupermarketURI);

		Item responseContent = new ItemImpl("searchForProductResponse");
		responseContent.addChild("price").setContent("0.5");
		MockResponse response = new MockResponse().whenReceive("milk")
				.replyWith(responseContent);

		mock.returnFor("searchForProduct", response);
		mock.start();

		return mock;
	}

	private static void publishSupermarketsInTheRegistry() throws Exception {
		WSClient client = new WSClient(paoDoFuturoURI);
		client.request("registerSupermarket", paoDoFuturoURI);

		client = new WSClient(futureMartURI);
		client.request("registerSupermarket", futureMartURI);
	}

	private static void createAndRegisterMock() throws Exception {
		mock = createASupermarketMockWithTheCheapestPrice();
		String smMockURI = mock.getWsdl();
		registerServiceEndpoint("smMock", smMockURI);
	}

	private static void registerServiceEndpoint(String serviceName,
			String endpoint) throws Exception {
		WSClient client;
		client = new WSClient(smRegistryURI);
		client.request("setEndpoint", serviceName, endpoint);
	}
	
	private static void removeServiceEndpoint(String serviceName) throws Exception {
		WSClient client;
		client = new WSClient(smRegistryURI);
		client.request("removeEndpoint", serviceName);
	}

	private static void enactChoreography() {
		manager = new ChoreographyManager();
		manager.enactChoreography();
	}
}
