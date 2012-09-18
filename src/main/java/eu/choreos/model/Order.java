package eu.choreos.model;

public class Order {

	private String orderId;
	private Double price;
	
	public Order(){
		
	}
	
	public Order(String orderId, Double price) {
		this.setOrderId(orderId);
		this.setPrice(price);
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

}
