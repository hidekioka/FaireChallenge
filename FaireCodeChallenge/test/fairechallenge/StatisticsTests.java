package fairechallenge;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class StatisticsTests {
	private Statistics statistics;

	@Test
	public void testLargestOrderInPrice() {
		assertEquals("The highest price should be 1300", 1300, this.statistics.getLargestProcessedOrder().getCalculatedPrice());
		assertEquals("The highest price should be from order 2", "order 2", this.statistics.getLargestProcessedOrder().getId());
	}

	@Test
	public void testAveragePrice() {
		assertEquals("The average price should be 1200", 1200, this.statistics.getAveragePricePerOrder());
	}

	@Test
	public void testBestSelling() {
		assertEquals("The best selling product should be 'option 1'", "option 1", this.statistics.getBestSellingProductInQuantity().getKey());
		assertEquals("The best selling product quantity should be 24", Integer.valueOf(24), this.statistics.getBestSellingProductInQuantity().getValue());
	}

	@Test
	public void testStateWithMostOrders() {
		assertEquals("The state with most orders should be 'São Paulo'", "São Paulo", this.statistics.getStateWithMostOrders().getKey());
	}

	/**
	 * Creating some orders to calculate the statistics
	 */
	@Before
	public void createScenario() {
		List<Order> processedOrders = new ArrayList<>();
		List<Order> backorderedOrders = new ArrayList<>();
		Order order1 = new Order();
		order1.setId("order 1");
		Item item1_1 = new Item();
		item1_1.setPrice(50);
		item1_1.setQuantity(10);
		item1_1.setProductOptionId("option 1");
		Item item1_2 = new Item();
		item1_2.setPrice(120);
		item1_2.setQuantity(5);
		item1_2.setProductOptionId("option 2");
		order1.getItems().add(item1_1);
		order1.getItems().add(item1_2);
		Address address1 = new Address();
		address1.setState("São Paulo");
		order1.setAddress(address1);
		processedOrders.add(order1);

		Order order2 = new Order();
		order2.setId("order 2");
		Item item2_1 = new Item();
		item2_1.setPrice(40);
		item2_1.setQuantity(15);
		item2_1.setProductOptionId("option 3");
		Item item2_2 = new Item();
		item2_2.setPrice(50);
		item2_2.setQuantity(14);
		item2_2.setProductOptionId("option 1");
		order2.getItems().add(item2_1);
		order2.getItems().add(item2_2);
		Address address2 = new Address();
		address2.setState("São Paulo");
		order1.setAddress(address2);
		processedOrders.add(order2);
		
		this.statistics = new Statistics(processedOrders, backorderedOrders);
	}

}
