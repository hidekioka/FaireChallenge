package fairecodetest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * This class will be used to calculate some statistics from all processed orders
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
					mapOfSoldProducts.put(item.getProductOptionId(), mapOfSoldProducts.get(item.getProductOptionId()) + item.getQuantity());
				}
			}
		}

		// Checks which is quantity is bigger and returns the product
		Entry<String, Integer> bestSellingOption = null;
		for (Entry<String, Integer> entry : mapOfSoldProducts.entrySet()) {
			if (bestSellingOption == null) {
				bestSellingOption = entry;
			} else {
				if (entry.getValue() > bestSellingOption.getValue()) {
					bestSellingOption = entry;
				}
			}
		}

		return bestSellingOption;
	}

	/**
	 * Returns the order with the largest dollar paid in it, chooses first in case of more than one
	 * 
	 * @return Order
	 */
	public Order getLargestOrder() {
		Order largestOrder = null;
		int highestPaidInOrder = -1;
		for (Order order : this.processedOrders) {
			int totalPaidInOrder = 0;
			for (Item item : order.getItems()) {
				totalPaidInOrder = totalPaidInOrder + item.getPrice() * item.getQuantity();
			}
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
		// Checks which state had more orders
		Entry<String, Integer> stateWithMostOrders = null;
		for (Entry<String, Integer> entry : mapOfStates.entrySet()) {
			if (stateWithMostOrders == null) {
				stateWithMostOrders = entry;
			} else {
				if (entry.getValue() > stateWithMostOrders.getValue()) {
					stateWithMostOrders = entry;
				}
			}
		}
		return stateWithMostOrders;
	}

	/**
	 * Very similar to getBestSellingProductInQuantity but it must be processed in the orders that were backordered
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
					mapOfBackOrderedProducts.put(item.getProductOptionId(), mapOfBackOrderedProducts.get(item.getProductOptionId()) + item.getQuantity());
				}
			}
		}

		// Checks which is quantity is bigger and returns the product
		Entry<String, Integer> mostBackOrderedProduct = null;
		for (Entry<String, Integer> entry : mapOfBackOrderedProducts.entrySet()) {
			if (mostBackOrderedProduct == null) {
				mostBackOrderedProduct = entry;
			} else {
				if (entry.getValue() > mostBackOrderedProduct.getValue()) {
					mostBackOrderedProduct = entry;
				}
			}
		}

		return mostBackOrderedProduct;
	}

	/**
	 * Return the average price in cents for processed orders
	 * 
	 * @return long so we don't lose too much precision
	 */
	public long getAveragePricePerOrder() {
		int totalPricePaid = 0;
		for (Order order : this.processedOrders) {
			for (Item item : order.getItems()) {
				totalPricePaid = totalPricePaid + item.getPrice() * item.getQuantity();
			}
		}
		long averagePricePerOrder = this.processedOrders.size() != 0L ? Long.valueOf(totalPricePaid) / this.processedOrders.size() : 0L;
		return averagePricePerOrder;
	}
}
