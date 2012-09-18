package eu.choreos.abstraction;

import java.io.FileNotFoundException;
import java.util.List;

import eu.choreos.admin.ChoreographyManager;
import eu.choreos.vv.abstractor.Choreography;
import eu.choreos.vv.abstractor.Service;

public class ChoreographyAbstractor {
	
	protected static String paoDoFuturoURI;
	protected static String futureMartURI;
	protected static String sm1URI;
	protected static String registryURI;
	protected static String smRegistryURI;
	protected static String futureMarketPlaceURI;
	
	protected static void buildChoreographyAbstraction() throws FileNotFoundException {
		Choreography futureMarket = Choreography.build("./src/test/resources/futureMarket.yml");
		
		Service paoDoFuturo = futureMarket.getServicesForRole("supermarket").get(0);
		paoDoFuturoURI = ChoreographyManager.completeURI(paoDoFuturo.getUri());
		
		Service futureMart = futureMarket.getServicesForRole("supermarket").get(1);
		futureMartURI = ChoreographyManager.completeURI(futureMart.getUri());
		
		Service futureMarketPlace = futureMarket.getServicesForRole("customer").get(0);
		futureMarketPlaceURI = ChoreographyManager.completeURI(futureMarketPlace.getUri());
		
		sm1URI = ChoreographyManager.completeURI(paoDoFuturo.getParticipants().get(0).getUri());
		registryURI = ChoreographyManager.completeURI(paoDoFuturo.getParticipants().get(1).getUri());
		
		List<Service> participants = futureMarketPlace.getParticipants();
		smRegistryURI = ChoreographyManager.completeURI(participants.get(0).getUri());
	}

}
