package eu.choreos.roles;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

import eu.choreos.contracts.Supermarket;
import eu.choreos.model.UserInfo;
import eu.choreos.vv.clientgenerator.Item;
import eu.choreos.vv.clientgenerator.WSClient;
import eu.choreos.ws.Services;

@SuppressWarnings("restriction")
@WebService
public class Carrefutur implements Supermarket{
	
	private String SERVICE_REGISTRY;

	public Carrefutur(){
		SERVICE_REGISTRY = Services.WS_REGISTRY.getEndpoint();
	}
	
	@WebMethod
	@WebResult(name = "price")
	public Double searchForProduct(String name) {
		Double price = 0.0;
		String sm1URI = "";
				
		try {
			WSClient client = new WSClient(SERVICE_REGISTRY);
			sm1URI = client.request("getEndpoint", "SM3").getContent("endpoint");
			
			client = new WSClient(sm1URI);
			Item response = client.request("searchPriceOfItem", name);
			price = response.getContentAsDouble("value");	
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return price;
	}

	public String registerSupermarket(String endpoint) {
		try {
			WSClient client = new WSClient(SERVICE_REGISTRY);
			String registryURI = client.request("getEndpoint", "SM_REGISTRY").getContent("endpoint");
			
			client = new WSClient(registryURI);
			client.request("setEndpoint", "Carrefutur", Services.CARREFUTUR.getEndpoint());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "OK";
	}

	public String purchase(String id, UserInfo userInfo) {
		return null;
	}

}
