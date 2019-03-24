package fairechallenge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * This class will be used to calculate some statistics from all processed
 * orders
 * 
 * @author Hideki
 *
 */
public class Statistics {
	private List<Order> processedOrders = new ArrayList<>();
	private List<Order> backorderedOrders = new ArrayList<>();

	public Statistics(List<Order> processedOrders, List<Order> backorderedOrders) {
		super();
		this.backorderedOrders = backorderedOrders;
		this.processedOrders = processedOrders;
	}

	/**
	 * Returns the product option id for the one that was sold
	 * 
	 * @return Entry<String, Integer> with product option id and quantity
	 */
	public Entry<String, Integer> getBestSellingProductInQuantity() {
		HashMap<String, Integer> mapOfSoldProducts = new HashMap<>();

		// Populates the maps with the product options and how many was sold of it
		for (Order order : this.processedOrders) {
			for (Item item : order.getItems()) {
				if (mapOfSoldProducts.get(item.getProductOptionId()) == null) {
					mapOfSoldProducts.put(item.getProductOptionId(), item.getQuantity());
				} else {
					mapOfSoldProducts.put(item.getProductOptionId(),
							mapOfSoldProducts.get(item.getProductOptionId()) + item.getQuantity());
				}
			}
		}

		// Checks which is quantity is bigger and returns the product
		return getHighestValueFromMap(mapOfSoldProducts);
	}

	/**
	 * Returns the order with the largest dollar paid in it, chooses first in case
	 * of more than one
	 * 
	 * @return Order
	 */
	public Order getLargestProcessedOrder() {
		return this.getLargestOrder(this.processedOrders);
	}

	/**
	 * Returns the order that was not processed and had the highest total price
	 * 
	 * @return
	 */
	public Order getLargestBackorderedOrder() {
		return this.getLargestOrder(this.backorderedOrders);
	}
	
	private Order getLargestOrder(List<Order> orders) {
		Order largestOrder = null;
		int highestPaidInOrder = 0;
		for (Order order : orders) {
			int totalPaidInOrder = order.getCalculatedPrice();
			if (totalPaidInOrder > highestPaidInOrder) {
				highestPaidInOrder = totalPaidInOrder;
				largestOrder = order;
			}

		}
		return largestOrder;
	}

	/**
	 * Returns which was the state with the most number of orders
	 * 
	 * @return Entry<String, Integer> with state name and quantity of orders
	 */
	public Entry<String, Integer> getStateWithMostOrders() {
		HashMap<String, Integer> mapOfStates = new HashMap<>();
		for (Order order : this.processedOrders) {
			if (order.getAddress() != null && order.getAddress().getState() != null) {
				if (mapOfStates.get(order.getAddress().getState()) == null) {
					mapOfStates.put(order.getAddress().getState(), 1);
				} else {
					mapOfStates.put(order.getAddress().getState(), mapOfStates.get(order.getAddress().getState()) + 1);
				}
			}
		}
		// Checks which is quantity is bigger and returns the product
		return getHighestValueFromMap(mapOfStates);
	}

	/**
	 * The product option that was most backordered
	 * 
	 * @return Entry<String, Integer> with product option id and quantity
	 */
	public Entry<String, Integer> getMostBackOrderedProductInQuantity() {
		HashMap<String, Integer> mapOfBackOrderedProducts = new HashMap<>();

		// Populates the maps with the product options and how many was sold of it
		for (Order order : this.backorderedOrders) {
			for (Item item : order.getItems()) {
				if (mapOfBackOrderedProducts.get(item.getProductOptionId()) == null) {
					mapOfBackOrderedProducts.put(item.getProductOptionId(), item.getQuantity());
				} else {
					mapOfBackOrderedProducts.put(item.getProductOptionId(),
							mapOfBackOrderedProducts.get(item.getProductOptionId()) + item.getQuantity());
				}
			}
		}

		// Checks which is quantity is bigger and returns the product
		return getHighestValueFromMap(mapOfBackOrderedProducts);
	}

	/**
	 * Return the average price in cents for processed orders
	 * 
	 * @return long so we don't lose too much precision
	 */
	public long getAveragePricePerOrder() {
		int totalPricePaid = 0;
		for (Order order : this.processedOrders) {
			totalPricePaid = totalPricePaid + order.getCalculatedPrice();
		}
		long averagePricePerOrder = this.processedOrders.size() != 0L
				? Long.valueOf(totalPricePaid) / this.processedOrders.size()
				: 0L;
		return averagePricePerOrder;
	}

	/**
	 * Returns the entry that has the highest number in the map value
	 * 
	 * @return Entry<String, Integer>
	 */
	private Entry<String, Integer> getHighestValueFromMap(HashMap<String, Integer> map) {
		Entry<String, Integer> highestValueEntry = null;
		for (Entry<String, Integer> entry : map.entrySet()) {
			if (highestValueEntry == null) {
				highestValueEntry = entry;
			} else {
				if (entry.getValue() > highestValueEntry.getValue()) {
					highestValueEntry = entry;
				}
			}
		}
		return highestValueEntry;
	}
}
