package eu.choreos.unit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.choreos.admin.ChoreographyManager;
import eu.choreos.vv.clientgenerator.Item;
import eu.choreos.vv.clientgenerator.WSClient;
import eu.choreos.ws.Services;
import static org.junit.Assert.assertEquals;

public class SupermarketWSTest {
	
	private static ChoreographyManager manager;
	
	@BeforeClass
	public static void setUp() throws Exception{
		manager = new ChoreographyManager();
		
		manager.start(Services.SM1);
		manager.start(Services.SM2);
	}
	
	@AfterClass
	public static void tearDown() throws Exception{
		manager.stop(Services.SM1);
		manager.stop(Services.SM2);
	}
	
	@Test
	public void sm1ShouldReturnAPriceWhenSearchForMilk() throws Exception {
		WSClient client = new WSClient("http://localhost:1221/sm1?wsdl");
		Item price = client.request("checkPriceOf", "milk");
		
		assertEquals(3.50, price.getContentAsDouble("result"), 0.0001);
	}
	
	@Test
	public void sm2ShouldReturnAPriceWhenSearchForMilk() throws Exception {
		WSClient client = new WSClient("http://localhost:1222/sm2?wsdl");
		Item price = client.request("getPriceOfItem", "milk");
		
		assertEquals(2.0, price.getContentAsDouble("value"), 0.0001);
	}
}
