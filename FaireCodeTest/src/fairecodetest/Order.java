package fairecodetest;

import java.util.ArrayList;
import java.util.List;

public class Order {
	private String ID;
	private String state;
	private String id;
	private List<Item> items = new ArrayList<>();

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
}
