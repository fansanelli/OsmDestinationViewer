package dev.pengunaria.osmdestinationviewer;

import java.util.Map;

public class OsmDestinationViewer {

	public static String getSignpost(Map<String, String> tags, String countryCode, boolean compact) throws Exception {
		if (tags == null || tags.size() == 0)
			throw new Exception("Tags is null or empty");

		Signpost signpost = null;
		if (isGuidepost(tags)) {
			signpost = new GuidepostSign(tags);
		} else if (isDirectionOrLane(tags)) {
			signpost = new LaneSign(tags, countryCode);
		} else {
			signpost = new ExitSign(tags, countryCode);
		}
		return signpost.toSvg(compact);
	}

	private static boolean isGuidepost(Map<String, String> tags) {
		return tags.containsKey("tourism") && "information".equals(tags.get("tourism"))
				&& tags.containsKey("information") && "guidepost".equals(tags.get("information"));
	}

	private static boolean isDirectionOrLane(Map<String, String> tags) {
		return tags.containsKey("destination:forward") || tags.containsKey("destination:backwards")
				|| tags.containsKey("destination:lanes") || tags.containsKey("destination:lanes:forward")
				|| tags.containsKey("destination:lanes:backward");
	}

	public static String getSignpost(Map<String, String> tags, String countryCode) throws Exception {
		return getSignpost(tags, countryCode, false);
	}

	public static String getSignpost(Map<String, String> tags) throws Exception {
		return getSignpost(tags, null);
	}
}
