package eu.choreos.roles;

import java.util.List;

import eu.choreos.contracts.Customer;
import eu.choreos.model.AccountType;
import eu.choreos.model.Order;
import eu.choreos.vv.clientgenerator.Item;
import eu.choreos.vv.clientgenerator.WSClient;
import eu.choreos.ws.Services;
import javax.jws.*;
import javax.swing.JOptionPane;

@SuppressWarnings("restriction")
@WebService
public class FutureMarketPlace implements Customer {
	
	private String SERVICE_REGISTRY;

	public FutureMarketPlace(){
		SERVICE_REGISTRY = Services.WS_REGISTRY.getEndpoint();
	}
	
	@WebMethod
	public Order getPriceOfProductList(List<String> products) {
		WSClient client;
		String orderId = String.valueOf(System.currentTimeMillis());
		Double price = Double.MAX_VALUE;
		
		try {
			client = new WSClient(SERVICE_REGISTRY);
			String registryURI = client.request("getEndpoint", "SM_REGISTRY").getContent("endpoint");
			
			client = new WSClient(registryURI);
			List<Item> smEndpoints = client.request("getAllEndpoints").getChildAsList("item");
			
			for(String product : products){
				for (Item item : smEndpoints) {
					String endpointURI = item.getContent();
					client = new WSClient(endpointURI);
					Double currentPrice = client.request("searchForProduct", product).getContentAsDouble("price");
					
					if (currentPrice <= price) {
						price = currentPrice;
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
				
		return new Order(orderId, price);
	}

	public String purchase(String id, AccountType account) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDeliveryData(String shipper, String orderID) {
		// TODO Auto-generated method stub
		return null;
	}

}
