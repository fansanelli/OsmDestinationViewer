package dev.pengunaria.osmdestinationviewer;

import java.util.Map;

/**
 * Class representing a simple exit information sign
 */
class LaneSign extends RoadSignpost {
	private Lane[] lanes;

	LaneSign(Map<String, String> tags, String countryCode) throws Exception {
		super(tags, countryCode);

		/**
		 * Vedi https://wiki.openstreetmap.org/wiki/Key:destination
		 * https://wiki.openstreetmap.org/wiki/User:Mueschel/DestinationTagging
		 */
		if (tags.containsKey("destination")) {

		} else {
			throw new Exception("Highway without destination");
		}
	}

	protected String getBackgroundColor() {
		/**
		 * Vedi: https://wiki.openstreetmap.org/wiki/Key:destination:colour
		 */
		switch (this.countryCode.toUpperCase()) {
		case "DE":
			if (isMotorway()) {
				return "blue"; // Blue for motorways in DE
			}
			break;
		case "IT":
			if (isMotorway()) {
				return "green"; // Green for motorways in IT
			} else if (isFreeway()) {
				return "blue"; // Blue for freeways in IT
			} else {
				return "white"; // Default color for other highways in IT
			}
		case "US":
			if (isMotorway()) {
				return "green"; // Green for motorways in and US
			}
			break;
		}
		return "blue";
	}

	private boolean isMotorway() {
		return tags.containsKey("highway") && "motorway".equals(tags.get("highway"));
	}

	private boolean isFreeway() {
		return tags.containsKey("motorroad") && "yes".equals(tags.get("motorroad"));
	}

	@Override
	public String toSvg(boolean compact) {
		return null;
	}
}
