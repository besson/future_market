package eu.choreos.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.choreos.admin.ChoreographyManager;
import eu.choreos.vv.abstractor.Choreography;
import eu.choreos.vv.abstractor.Service;
import eu.choreos.vv.clientgenerator.Item;
import eu.choreos.vv.clientgenerator.ItemImpl;
import eu.choreos.vv.clientgenerator.WSClient;
import eu.choreos.vv.servicesimulator.MockResponse;
import eu.choreos.vv.servicesimulator.WSMock;
import eu.choreos.ws.Services;

public class FutureMarketPlaceFlowTest {
	
	private static String paoDoFuturoURI;
	private static String smRegistryURI;
	private static String futureMarketPlaceURI;
	private static ChoreographyManager manager;
	private static String futureMartURI;
	
	@BeforeClass
	public static void setUp() throws Exception{
		buildChoreographyAbstraction();
		
		manager = new ChoreographyManager();
		manager.enactChoreography();
		
		String smMockURI = createASupermarketMockWithTheCheapestPrice().getWsdl();
		
		WSClient client = new WSClient(paoDoFuturoURI);
		client.request("registerSupermarket", paoDoFuturoURI);
		
		client = new WSClient(paoDoFuturoURI);
		client.request("registerSupermarket", futureMartURI);
		
		client = new WSClient(smRegistryURI);
		client.request("setEndpoint", "SmMock", smMockURI);
	}
	
	@AfterClass
	public static void tearUp() throws Exception{
		manager.stopChoreography();
	}
	
	@Test
	public void shouldReturnTheCheapestPrice() throws Exception {
		List<String> itemList = new ArrayList<String>();
		itemList.add("milk");
		Item requestContent = convertListToItem(itemList);
		
		WSClient client = new WSClient(futureMarketPlaceURI);
		Item cheapestPrice = client.request("getPriceOfProductList", requestContent);
		
		assertEquals("0.5", cheapestPrice.getChild("return").getContent("price"));
	}
	
	@Test
	public void shouldReturnTheFinalPriceAndAnID() throws Exception {
		List<String> itemList = new ArrayList<String>();
		itemList.add("milk");
		itemList.add("bread");
		itemList.add("corn");
		itemList.add("beer");
		itemList.add("coke");
		Item requestContent = convertListToItem(itemList);

		WSClient client = new WSClient(futureMarketPlaceURI);
		Item cheapestPrice = client.request("getPriceOfProductList", requestContent);
		
		assertTrue(cheapestPrice.getChild("return").getContentAsDouble("price") > 0);
		assertNotNull(cheapestPrice.getChild("return").getContentAsDouble("orderId"));
	}
	
	private static WSMock createASupermarketMockWithTheCheapestPrice() throws Exception {
		String realsupermarketURI = Services.PAO_DO_FUTURO.getEndpoint();
		
		WSMock mock = new WSMock("smMock", "7001", realsupermarketURI);
		
		Item responseContent = new ItemImpl("searchForProductResponse");
		responseContent.addChild("price").setContent("0.5");
		MockResponse response = new MockResponse().whenReceive("milk").replyWith(responseContent);
		
		mock.returnFor("searchForProduct", response);
		mock.start();
		
		return mock;
	}

	private static void buildChoreographyAbstraction() throws FileNotFoundException {
		Choreography futureMarket = Choreography.build("./src/test/resources/futureMarket.yml");
		Service paoDoFuturo = futureMarket.getServicesForRole("supermarket").get(0);
		Service futureMart = futureMarket.getServicesForRole("supermarket").get(1);
		Service futureMarketPlace = futureMarket.getServicesForRole("customer").get(0);
		List<Service> participants = futureMarketPlace.getParticipants();
		
		paoDoFuturoURI = ChoreographyManager.completeURI(paoDoFuturo.getUri());
		futureMartURI = ChoreographyManager.completeURI(futureMart.getUri());
		futureMarketPlaceURI = ChoreographyManager.completeURI(futureMarketPlace.getUri());
		smRegistryURI = ChoreographyManager.completeURI(participants.get(0).getUri());
	}
	
	private Item convertListToItem(List<String> itemList) {
		Item purchaseList = new ItemImpl("getPriceOfProductList");
		
		for(String item : itemList) {
			purchaseList.addChild("arg0").setContent(item);
		}
		return purchaseList;
	}
}
