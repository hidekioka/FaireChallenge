package fairechallenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.Gson;

public class Main {
	private final static String BASE_URL = "https://www.faire-stage.com";
	private final static String API_URL_PRODUCTS = "/api/v1/products";
	private final static String API_URL_ORDERS = "/api/v1/orders";
	// Will be the user's input
	// private final static String FAIRE_API_TOKEN = "HQLA9307HSLQYTC24PO2G0LITTIOHS2MJC8120PVZ83HJK4KACRZJL91QB7K01NWS2TUCFXGCHQ8HVED8WNZG0KS6XRNBFRNGY71";
	private final static String FAIRE_API_TOKEN_PARAM_NAME = "X-FAIRE-ACCESS-TOKEN";
	private final static int LIMIT_SEARCH_PRODUCTS = 100;
	private final static int LIMIT_SEARCH_ORDERS = 50;
	private final static String TEA_DROPS_BRAND = "b_d2481b88";

	public static void main(String[] args) throws IOException {
		System.out.println("Enter API token:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String apiToken = br.readLine();
		List<ProductsPage> allProducts = getAllProducts(apiToken);
		List<OrdersPage> allOrders = getAllOrders(apiToken);

		// Create a map with the available product option to make the iterations easier and faster
		HashMap<String, ProductOption> availableProductsMap = mapProducts(allProducts, TEA_DROPS_BRAND);
		List<Order> processedOrders = new ArrayList<>();
		List<Order> backorderedOrders = new ArrayList<>();
		processOrders(allOrders, availableProductsMap, processedOrders, backorderedOrders, apiToken);

		Statistics statistics = new Statistics(processedOrders, backorderedOrders);

		Entry<String, Integer> bestSelling = statistics.getBestSellingProductInQuantity();
		Order largestProcessedOrder = statistics.getLargestProcessedOrder();
		Entry<String, Integer> stateWithMostOrders = statistics.getStateWithMostOrders();
		Entry<String, Integer> mostBackorderedProduct = statistics.getMostBackOrderedProductInQuantity();
		Order largestBackordererdOrder = statistics.getLargestBackorderedOrder();
		long averagePricePerOrder = statistics.getAveragePricePerOrder();
		System.out.println("================METRICS===============");
		if (bestSelling != null) {
			System.out.println("Best selling product was: " + bestSelling.getKey() + " number of units sold: " + bestSelling.getValue());
		} else {
			System.out.println("Could not find the best selling product");
		}
		if (largestProcessedOrder != null) {
			System.out.println("Largest order by dollar amount was: " + largestProcessedOrder.getId() + " with total price: " + largestProcessedOrder.getCalculatedPrice());
		} else {
			System.out.println("Could not find the largest order");
		}
		if (stateWithMostOrders != null) {
			System.out.println("State with most orders was: " + stateWithMostOrders.getKey() + " number of orders: " + stateWithMostOrders.getValue());
		} else {
			System.out.println("Could not find the state with most orders");
		}
		if (mostBackorderedProduct != null) {
			System.out.println("Product option that was most backordered was: " + mostBackorderedProduct.getKey() + " with number of units not sold: " + mostBackorderedProduct.getValue());
		} else {
			System.out.println("Could not find the product that was most backordered");
		}
		System.out.println("Average price paid per order: " + averagePricePerOrder + " cents");
		if (largestBackordererdOrder != null) {
			System.out.println("Larges Backordered order by dollar amount: " + largestBackordererdOrder.getId() + " with total price: " + largestBackordererdOrder.getCalculatedPrice());
		} else {
			System.out.println("Could not find the backordered order that had the highest prices");
		}
	}

	private static HashMap<String, ProductOption> mapProducts(List<ProductsPage> allProducts, String brandId) {
		// Inserting all products options available in the map
		HashMap<String, ProductOption> availableProductsMap = new HashMap<String, ProductOption>();
		for (ProductsPage page : allProducts) {
			for (Product product : page.getProducts()) {
				// Check the product if it is active
				if (product.getActive() && product.getBrandId().equals(brandId)) {
					for (ProductOption productOption : product.getOptions()) {
						// If there is 0 of the option, it's not available
						if (productOption.getQuantity() > 0) {
							availableProductsMap.put(productOption.getId(), productOption);
						}
					}

				}
			}
		}
		// System.out.println(Arrays.toString(availableProductsMap.entrySet().toArray()));
		return availableProductsMap;
	}

	private static void processOrders(List<OrdersPage> allOrders, HashMap<String, ProductOption> availableProductsMap, List<Order> processedOrders, List<Order> backorderedOrders, String apiToken) {
		// Iterating all orders and choosing which ones must be processed
		for (OrdersPage page : allOrders) {
			for (Order order : page.getOrders()) {
				// Only process the orders that have NEW state, then will check if there's availability
				if (order.getState().equals("NEW")) {
					try {
						// Process the orders and populate the lists so we can use them for metrics
						processOrder(order, availableProductsMap, processedOrders, backorderedOrders, apiToken);
					} catch (IOException e) {
						// Do not throw the Exception again because we still want to process the remaining orders
						System.out.println("ORDER COULD NOT BE PROCESSED: " + order.getId());
					}
				}
			}
		}
	}

	/**
	 * Check if the order can be processed and put in the list of processed or backordered
	 * 
	 * @return true if the processing was done corretly
	 */
	private static boolean processOrder(Order order, HashMap<String, ProductOption> productOptionsMapById, List<Order> processedOrders, List<Order> backorderedOrders, String apiToken) throws IOException {
		// Check if the quantity for all items is enough for the order
		boolean canBeProcessed = true;
		for (Item item : order.getItems()) {
			ProductOption option = productOptionsMapById.get(item.getProductOptionId());
			if (option == null) {
				// This product option is not available so this order cannot be processed
				canBeProcessed = false;
				// System.out.println("\tNo product with this id: " +
				// item.getProductOptionId());
				break;
			} else if (option.getQuantity() < item.getQuantity()) {
				// This product option is not available so this order cannot be processed
				canBeProcessed = false;
				// System.out.println("\tNot enought items " + item.getProductOptionId() + " needed: " + item.getQuantity() + " available: " + option.getQuantity());
				break;
			}
		}
		if (canBeProcessed) {
			// marks the order as processed ID The ID of the order to accept -> since there is no uppercase ID parameter in the JSON, using the lowercase id one
			acceptOrder(order.getId(), apiToken);
			for (Item item : order.getItems()) {
				ProductOption option = productOptionsMapById.get(item.getProductOptionId());
				int optionQuantity = option.getQuantity();
				int newOptionQuantity = optionQuantity - item.getQuantity();

				// updates the quantity on the server
				updateProductOption(item.getProductOptionId(), newOptionQuantity, apiToken);

				// Only subtracts items after the requests are correctly processed
				option.setQuantity(newOptionQuantity);
				processedOrders.add(order);
				// System.out.println("\tSubtracting quantity for " + item.getProductOptionId() + ", before: " + optionQuantity + " after: " + newOptionQuantity);
			}
		} else {
			// Cannot be processed, mark as backordered
			backorderedOrders.add(order);
		}
		return canBeProcessed;
	}

	private static List<ProductsPage> getAllProducts(String apiToken) throws IOException {
		// To get all products, sending requests until there is a response with no items
		List<ProductsPage> allProducts = new ArrayList<>();
		ProductsPage currentPage = null;
		int pageNumber = 1; // starts at page 1
		while (currentPage == null || !currentPage.getProducts().isEmpty()) {
			currentPage = getProductsFromPage(pageNumber, apiToken);
			allProducts.add(currentPage);
			pageNumber++;
		}
		return allProducts;
	}

	private static List<OrdersPage> getAllOrders(String apiToken) throws IOException {
		// To get all orders, sending requests until there is a response with no items
		List<OrdersPage> allOrders = new ArrayList<>();
		OrdersPage currentPage = null;
		int pageNumber = 1; // starts at page 1
		while (currentPage == null || !currentPage.getOrders().isEmpty()) {
			currentPage = getOrdersFromPage(pageNumber, apiToken);
			allOrders.add(currentPage);
			pageNumber++;
		}
		return allOrders;
	}

	private static ProductsPage getProductsFromPage(int pageNumber, String apiToken) throws IOException {
		ProductsPage products = new ProductsPage();
		String urlParameters = "?limit=" + LIMIT_SEARCH_PRODUCTS + "&page=" + pageNumber;
		URL url = new URL(BASE_URL + API_URL_PRODUCTS + urlParameters);
		String requestMethod = "GET";
		String requestReturn = sendHttpRequest(url, apiToken, requestMethod);
		Gson gson = new Gson();
		products = gson.fromJson(requestReturn, products.getClass());
		System.out.println(requestReturn);
		return products;
	}

	private static OrdersPage getOrdersFromPage(int pageNumber, String apiToken) throws IOException {
		OrdersPage orders = new OrdersPage();
		String urlParameters = "?limit=" + LIMIT_SEARCH_ORDERS + "&page=" + pageNumber;
		URL url = new URL(BASE_URL + API_URL_ORDERS + urlParameters);
		String requestMethod = "GET";
		String requestReturn = sendHttpRequest(url, apiToken, requestMethod);
		Gson gson = new Gson();
		orders = gson.fromJson(requestReturn, orders.getClass());
		System.out.println(requestReturn);
		return orders;
	}

	private static void acceptOrder(String orderId, String apiToken) throws IOException {
		URL url = new URL(BASE_URL + API_URL_ORDERS + "/" + orderId + "/processing");
		String requestMethod = "PUT";
		String requestReturn = sendHttpRequest(url, apiToken, requestMethod);
		System.out.println(requestReturn);
	}

	private static void updateProductOption(String productOptionId, int availableUnits, String apiToken) throws IOException {
		String urlParameters = "?available_units=" + availableUnits;
		URL url = new URL(BASE_URL + API_URL_PRODUCTS + "/options/" + productOptionId + urlParameters);
		String requestMethod = "PATCH";
		String requestReturn = sendHttpRequest(url, apiToken, requestMethod);
		System.out.println(requestReturn);
	}

	private static String sendHttpRequest(URL url, String apiToken, String requestMethod) throws IOException {
		URLConnection con = url.openConnection();
		HttpURLConnection http = (HttpURLConnection) con;
		http.setRequestMethod(requestMethod);
		// Inserting the token so the API can be accessed
		http.setRequestProperty(FAIRE_API_TOKEN_PARAM_NAME, apiToken);
		http.setDoOutput(true);
		StringBuffer response = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (IOException e) {
			// Ideally should be using a specific LOG class but for this application it's just okay for a simple print
			System.out.println(e.getMessage());
		}
		return response.toString();

	}
}
