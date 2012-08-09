package eu.choreos.ws;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

@SuppressWarnings("restriction")
@WebService
public class SM3WS {
	
	private static Map<String, String> items;
	
	public SM3WS(){
		items = new HashMap<String, String>();
		items.put("milk", "1.00");
		items.put("bread", "1.40");
		items.put("butter", "1.20");
		items.put("pasta", "3.50");
		items.put("peas", "1.30");
		items.put("corn", "2.70");
		items.put("beer", "2.50");
		items.put("coke", "2.99");
	}

	@WebMethod
	@WebResult(name="value")
	public String searchPriceOfItem(String productName)  {
		return items.get(productName);
	}

}
