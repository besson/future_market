package eu.choreos.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@SuppressWarnings("restriction")
@WebService
public class WSRegistry {
	
	private Map<String, String> endpoints;
	
	public WSRegistry() {
		endpoints = new HashMap<String, String>();
		
		for(Services service : Services.values()) {
			endpoints.put(service.name(), service.getEndpoint());
		}
	}
	
	@WebMethod
	@WebResult(name="endpoint")
	public String getEndpoint(String key){
		return endpoints.get(key);
	}

	@WebMethod
	public void setEndpoint(String key, @WebParam(name="endpoint")String value){
		endpoints.remove(key);
		endpoints.put(key, value);
	}
	
	@WebMethod
	@WebResult(name="item")
	public List<String> getAllEndpoints(){
		List<String> smEndpoints = new ArrayList<String>();
		
		Map<String, String> localEndpoints = endpoints;
		localEndpoints.remove(Services.SM1.name());
		localEndpoints.remove(Services.SM2.name());
		localEndpoints.remove(Services.SM3.name());
		localEndpoints.remove(Services.FUTURE_MARKET_PLACE.name());
		localEndpoints.remove(Services.SM_REGISTRY.name());
		localEndpoints.remove(Services.WS_REGISTRY.name());
		
		for (Entry<String, String> entry : localEndpoints.entrySet()) {
			smEndpoints.add(entry.getValue());
		}
		
		return smEndpoints;
	}
}
