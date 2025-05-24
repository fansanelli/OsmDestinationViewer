package dev.pengunaria.osmdestinationviewer;

/**
 * Class representing a single destination
 */
class Destination {
	private String name;
	private String ref;
	private String symbol;

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
}
