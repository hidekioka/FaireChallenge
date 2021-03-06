package fairechallenge;

import com.google.gson.annotations.SerializedName;

public class Item {
	@SerializedName("order_id")
	private String orderId;
	private String id;
	@SerializedName("product_id")
	private String productId;
	@SerializedName("product_option_id")
	private String productOptionId;
	@SerializedName("price_cents")
	private int price = 0;
	private int quantity = 0;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductOptionId() {
		return productOptionId;
	}

	public void setProductOptionId(String productOptionId) {
		this.productOptionId = productOptionId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * Calculated price paid for this Item, considering quantity
	 * @return
	 */
	public int getCalculatedPrice() {
		return this.quantity * this.price;
	}
}
