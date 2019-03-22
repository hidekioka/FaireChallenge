package fairecodetest;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Product {
	@SerializedName("brand_id")
	private String brandId;
	private List<ProductOption> options = new ArrayList<>();
	private String name;
	private String id;
	private boolean active;

	public List<ProductOption> getOptions() {
		return options;
	}

	public void setOptions(List<ProductOption> options) {
		this.options = options;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
