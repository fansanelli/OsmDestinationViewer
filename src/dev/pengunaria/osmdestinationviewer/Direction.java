package dev.pengunaria.osmdestinationviewer;

/**
 * Class representing a lane or path direction.
 */
enum Direction {
	THROUGH("through"),
	LEFT("left"),
	RIGHT("right"),
	NORTH("north"),
	EAST("east"),
	SOUTH("south"),
	WEST("west"),
	NORTHEAST("northeast"),
	NORTHWEST("northwest"),
	SOUTHEAST("southeast"),
	SOUTHWEST("southwest");

	private final String value;

	Direction(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		switch (this) {
		case NORTH:
		case THROUGH:
			return "⬆️";
		case EAST:
			return "➡️";
		case SOUTH:
			return "⬇️";
		case WEST:
			return "⬅️";
		case RIGHT:
		case NORTHEAST:
			return "↗️";
		case LEFT:
		case NORTHWEST:
			return "↖️";
		case SOUTHEAST:
			return "↘️";
		case SOUTHWEST:
			return "↙️";
		}
		return this.value;
	}
}
