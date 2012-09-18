package eu.choreos.compliance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import eu.choreos.vv.abstractor.ComplianceTestCase;
import eu.choreos.vv.clientgenerator.Item;
import eu.choreos.vv.clientgenerator.WSClient;

public class SMRoleTest extends ComplianceTestCase{
	
	 String endpoint;
	private static WSClient smClient;
	
	public SMRoleTest(String endpoint) {
		super(endpoint);
		this.endpoint = endpoint;
	}
	
	@Before
	public  void setUp() throws Exception{
		smClient = new WSClient(endpoint);
	}

	@Test
	public void shouldHaveASearchForAProductOperation() throws Exception {
		assertTrue(smClient.getOperations().contains("searchForProduct"));
	}
	
	@Test
	public void shouldRegisterSupermarketIntoSMRegistryWS() throws Exception {
		assertTrue(smClient.getOperations().contains("registerSupermarket"));
	}
	
	@Test
	public void shouldHaveAPurchaseOperation() throws Exception {
		assertTrue(smClient.getOperations().contains("purchase"));
	}
	
	@Test
	public void searchForProductShouldReturnAProduct() throws Exception {
		Item response = smClient.request("searchForProduct", "milk");
		Item price = response.getChild("price");
		
		assertEquals("searchForProductResponse", response.getName());
		assertEquals("price", price.getName());
		assertNotNull(price.getContent());
	}
	
}