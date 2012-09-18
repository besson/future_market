package eu.choreos.admin;

import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.Endpoint;

import eu.choreos.roles.Carrefutur;
import eu.choreos.roles.FutureMarketPlace;
import eu.choreos.roles.FutureMart;
import eu.choreos.roles.PaoDoFuturo;
import eu.choreos.ws.SM1WS;
import eu.choreos.ws.SM2WS;
import eu.choreos.ws.SM3WS;
import eu.choreos.ws.Services;
import eu.choreos.ws.WSRegistry;

public class ChoreographyManager {

	private Map<Services, Endpoint> services;
	
	public ChoreographyManager() {
		services = new HashMap<Services, Endpoint>();
		
		addServices();
		addRoles();
	}

	private void addServices() {
		services.put(Services.SM1, Endpoint.create(new SM1WS()));
		services.put(Services.SM2, Endpoint.create(new SM2WS()));
		services.put(Services.SM3, Endpoint.create(new SM3WS()));
		services.put(Services.WS_REGISTRY, Endpoint.create(new WSRegistry()));
		services.put(Services.SM_REGISTRY, Endpoint.create(new WSRegistry()));
	}
	
	private void addRoles() {
		services.put(Services.PAO_DO_FUTURO, Endpoint.create(new PaoDoFuturo()));
		services.put(Services.FUTURE_MARKET_PLACE, Endpoint.create(new FutureMarketPlace()));
		services.put(Services.FUTURE_MART, Endpoint.create(new FutureMart()));
		services.put(Services.CARREFUTUR, Endpoint.create(new Carrefutur()));
	}

	public void start(Services service) {
		Endpoint endpoint = services.get(service);
		String uri = service.getEndpoint();
		uri = uri.substring(0, uri.length() - 5);
		endpoint.publish(uri);
	}
	
	public void stop(Services service) {
		Endpoint endpoint = services.get(service);
		endpoint.stop();
	}

	public void enactChoreography() {
		for(Services service : Services.values()) {
			start(service);
		}
	}
	
	public void stopChoreography() {
		for(Services service : Services.values()) {
			stop(service);
		}
	}

	public static String completeURI(String uri) {
		return uri + "?wsdl";
	}
}
