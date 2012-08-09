package eu.choreos.ws;

public enum Services {
	SM1("http://localhost:1221/sm1?wsdl"), SM2("http://localhost:1222/sm2?wsdl"), SM3("http://localhost:1223/sm3?wsdl"), WS_REGISTRY("http://localhost:1024/wsRegistry?wsdl"), 
	SM_REGISTRY("http://localhost:8002/registry?wsdl"), FUTURE_MARKET_PLACE("http://localhost:8003/futureMarketPlace?wsdl"), PAO_DO_FUTURO("http://localhost:8001/paoDoFuturo?wsdl"), 
	FUTURE_MART("http://localhost:8002/futureMart?wsdl"), CARREFUTUR("http://localhost:8003/carrefutur?wsdl");
	
	private String endpoint;
	
	Services(String endpoint){
		this.endpoint = endpoint;
	}
	
	public String getEndpoint(){
		return endpoint;
	}
}
