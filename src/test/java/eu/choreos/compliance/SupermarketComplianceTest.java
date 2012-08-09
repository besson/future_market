package eu.choreos.compliance;

import static eu.choreos.vv.assertions.RehearsalAsserts.assertRole;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.choreos.admin.ChoreographyManager;
import eu.choreos.vv.abstractor.Choreography;
import eu.choreos.vv.abstractor.Role;
import eu.choreos.vv.abstractor.Service;

public class SupermarketComplianceTest {

	private static Choreography futureMarket;
	private static Role supermarket;
	private static ChoreographyManager manager;

	@BeforeClass
	public static void setUp() throws FileNotFoundException {
		futureMarket = Choreography
				.build("./src/test/resources/futureMarket.yml");
		supermarket = new Role("supermarket", "./roles/supermarket.wsdl");

		manager = new ChoreographyManager();
		manager.enactChoreography();
	}

	@AfterClass
	public static void tearDown() {
		manager.stopChoreography();
	}

	@Test
	public void servicesMustBeCompliantWithTheSupermarketRole()
			throws Exception {
		List<Service> supermarkets = futureMarket
				.getServicesForRole("supermarket");
		for (Service service : supermarkets) {
			String wsdlURL = manager.completeURI(service.getUri());
			service.setUri(wsdlURL);
			service.addRole(supermarket);

			assertRole(supermarket, service, SMRoleTest.class);
		}
	}
}
