package fairecodetest;

import com.google.gson.annotations.SerializedName;

public class ProductOption {
	private String id;
	@SerializedName("product_id")
	private String productId;
	@SerializedName("available_quantity")
	private int quantity = 10;
	private String name;
	private boolean active;

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

	@Override
	public String toString() {
		return this.name + " | " +  this.quantity;
	}
}
