package eu.choreos.ws;

public enum Services {
	SM1("http://localhost:1221/sm1"), 
	SM2("http://localhost:1222/sm2"), 
	SM3("http://localhost:1223/sm3"), 
	WS_REGISTRY("http://localhost:1024/wsRegistry"), 
	SM_REGISTRY("http://localhost:8002/registry"), 
	FUTURE_MARKET_PLACE("http://localhost:8003/futureMarketPlace"), 
	PAO_DO_FUTURO("http://localhost:8001/paoDoFuturo"), 
	FUTURE_MART("http://localhost:8002/futureMart"), 
	CARREFUTUR("http://localhost:8003/carrefutur");
	
	private String endpoint;
	
	Services(String endpoint){
		this.endpoint = endpoint;
	}
	
	public String getEndpoint(){
		return endpoint + "?wsdl";
	}
}
