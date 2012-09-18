package eu.choreos.contracts;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.WebResult;

import eu.choreos.model.UserInfo;

@WebService
public interface Supermarket {

	@WebMethod
	@WebResult(name = "price")
	public Double searchForProduct(String name);
	
	@WebMethod
	public String registerSupermarket(String endpoint);
	
	@WebMethod
	public String purchase(String id, UserInfo userInfo);
	
}
