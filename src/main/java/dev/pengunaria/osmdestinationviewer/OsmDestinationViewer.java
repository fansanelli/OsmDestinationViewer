package main.java.dev.pengunaria.osmdestinationviewer;

import java.util.Map;

/**
 * This class is used to create a signpost from OSM destination tags.
 */
public class OsmDestinationViewer {
	private final Signpost signpost;
	private boolean compact = false;

	/**
	 * Constructor for OsmDestinationViewer.
	 * 
	 * @param tags
	 * @param countryCode ex. DE for Germany, FR for France, etc.
	 * @throws Exception
	 */
	public OsmDestinationViewer(Map<String, String> tags, String countryCode) throws Exception {
		if (tags == null || tags.size() == 0)
			throw new Exception("Tags is null or empty");

		if (isGuidepost(tags)) {
			signpost = new GuidepostSign(tags);
		} else if (isDirectionOrLane(tags)) {
			signpost = new LaneSign(tags, countryCode);
		} else {
			signpost = new ExitSign(tags, countryCode);
		}
	}

	/**
	 * Sets the compact mode for the signpost (mobile).
	 * 
	 * @param compact
	 * @return the OsmDestinationViewer instance
	 */
	public OsmDestinationViewer setCompact(boolean compact) {
		this.compact = compact;
		return this;
	}

	/**
	 * Transforms the signpost into an image.
	 * 
	 * @return a SVG string
	 */
	public String getSvg() {
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
}
