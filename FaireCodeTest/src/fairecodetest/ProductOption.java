package fairecodetest;

import com.google.gson.annotations.SerializedName;

public class ProductOption {
	private String id;
	@SerializedName("product_id")
	private String productId;
	@SerializedName("available_quantity")
	private int quantity = 100;
	private String name;
	private boolean active;
	private int wholesale_price_cents;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getName() {
		return name;	
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getWholesale_price_cents() {
		return wholesale_price_cents;
	}

	public void setWholesale_price_cents(int wholesale_price_cents) {
		this.wholesale_price_cents = wholesale_price_cents;
	}

	@Override
	public String toString() {
		return this.name + " | " +  this.quantity;
	}
}
