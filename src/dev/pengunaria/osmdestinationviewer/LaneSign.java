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

	@Override
	public String toSvg(boolean compact) {
		return null;
	}
}
