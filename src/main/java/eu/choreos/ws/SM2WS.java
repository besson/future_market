package eu.choreos.ws;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public class SM2WS {
	
	private static Map<String, String> items;
	
	public SM2WS(){
		items = new HashMap<String, String>();
		items.put("milk", "2.00");
		items.put("bread", "1.20");
		items.put("butter", "5.20");
		items.put("pasta", "2.50");
		items.put("peas", "2.42");
		items.put("corn", "1.70");
		items.put("beer", "3.50");
		items.put("coke", "1.99");
	}

	@WebMethod
	@WebResult(name="value")
	public String getPriceOfItem(String productName)  {
		return items.get(productName);
	}

}
