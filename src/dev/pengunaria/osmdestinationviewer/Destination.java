package dev.pengunaria.osmdestinationviewer;

/**
 * Class representing a single destination
 */
class Destination {
	private String name;
	private String ref;
	private String symbol;
	private String intRef;
	private String street;

	Destination(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getIntRef() {
		return intRef;
	}
	public void setIntRef(String intRef) {
		this.intRef = intRef;
	}

	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
}
