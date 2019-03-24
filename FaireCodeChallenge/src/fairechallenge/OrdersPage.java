package fairechallenge;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to map the order item in the response JSON 
 * It's a class that represents the page with the items returned from the request
 * @author Hideki
 */
public class OrdersPage {
	private List<Order> orders = new ArrayList<>();
	private int page;
	private int limit;

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
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
		return "Page: " + this.page + " | Limit: " + this.limit + " | Products number: " + this.orders.size();
	}

}
