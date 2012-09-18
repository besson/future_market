package eu.choreos.contracts;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import eu.choreos.model.AccountType;
import eu.choreos.model.Order;

@WebService
public interface Customer {

	@WebMethod
	public Order getPriceOfProductList(List<String> products);
	
	@WebMethod
	public String purchase(String id, AccountType account);
	
	@WebMethod
	public String getDeliveryData(String shipper, String orderID);
}
