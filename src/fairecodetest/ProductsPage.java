package fairecodetest;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to map the products item in the response JSON It's a class that represents the page with the items returned from the request
 * 
 * @author Hideki
 */
public class ProductsPage {
	private List<Product> products = new ArrayList<>();
	private int page;
	private int limit;

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public String toString() {
		return "Page: " + this.page + " | Limit: " + this.limit + " | Products number: " + this.products.size();
	}

}
