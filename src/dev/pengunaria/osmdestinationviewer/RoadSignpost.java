package dev.pengunaria.osmdestinationviewer;

import java.util.Map;

/**
 * Class to extend for road signposts.
 */
abstract class RoadSignpost implements Signpost {
	protected Map<String, String> tags;
	protected String countryCode;

	RoadSignpost(Map<String, String> tags, String countryCode) throws Exception {
		if (countryCode == null || countryCode.isEmpty()) {
			countryCode = "IT"; // Default to IT if not specified
		}
		this.tags = tags;
		this.countryCode = countryCode;
	}

	protected boolean isLeftDriving() {
		switch (countryCode.toUpperCase()) {
		case "GB":
		case "IE":
		case "AU":
		case "NZ":
		case "ZA":
		case "JP":
			// TODO: Add more countries with left driving
			return true;
		default:
			return false; // Default to right driving
		}
	}
}
