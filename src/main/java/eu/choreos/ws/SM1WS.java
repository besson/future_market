package eu.choreos.ws;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

@SuppressWarnings("restriction")
@WebService
public class SM1WS {
	
	private static Map<String, String> items;
	
	public SM1WS(){
		items = new HashMap<String, String>();
		items.put("milk", "3.50");
		items.put("bread", "1.10");
		items.put("butter", "5.00");
		items.put("pasta", "3.00");
		items.put("peas", "1.42");
		items.put("corn", "1.90");
		items.put("beer", "2.50");
		items.put("coke", "2.00");
	}

	@WebMethod
	@WebResult(name="result")
	public String checkPriceOf(String productName)  {
		return items.get(productName);
	}

}
