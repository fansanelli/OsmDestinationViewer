package dev.pengunaria.osmdestinationviewer;

/**
 * Class representing a single lane in a road or path.
 */
class Lane {
	private Destination[] destinations;
	private Direction direction;

	Lane(Destination[] destinations, Direction direction) {
		this.destinations = destinations;
		this.direction = direction;
	}

	Lane(Destination[] destinations) {
		this(destinations, null);
	}

	public Destination[] getDestinations() {
		return destinations;
	}
	public void setDestinations(Destination[] destinations) {
		this.destinations = destinations;
	}

	public Direction getDirection() {
		return direction;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
}
