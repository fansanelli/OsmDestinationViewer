package dev.pengunaria.osmdestinationviewer;

import java.util.Map;

public class OsmDestinationViewer {

	public static String getSignpost(Map<String, String> tags, boolean leftDriving, boolean compact) throws Exception {
		if (tags == null || tags.size() == 0) throw new Exception("Tags is null or empty");
		
		Signpost signpost = null;
		if (isGuidepost(tags)) {
			signpost = new GuidepostSign(tags);
			return signpost.toSvg();
		} else if (isOneWay(tags)) {
			if (!tags.containsKey("destination") && !tags.containsKey("destination:lanes"))
				throw new Exception("One-way roads without destination");
		} else {
			if (!tags.containsKey("destination:forward") && !tags.containsKey("destination:backward")
					&& !tags.containsKey("destination:lanes:forward") && !tags.containsKey("destination:lanes:backward"))
				throw new Exception("One-way roads without destination");			
		}
		return null;
	}
	
	private static boolean isGuidepost(Map<String, String> tags) {
		return tags.containsKey("tourism") && "information".equals(tags.get("tourism"))
				&& tags.containsKey("information") && "guidepost".equals(tags.get("information"));
	}
	
	private static boolean isOneWay(Map<String, String> tags) {
		if (tags.containsKey("highway") && "motorway".equals(tags.get("highway"))) return true;
		if (tags.containsKey("oneway") && "yes".equals(tags.get("oneway"))) return true;
		if (tags.containsKey("motorroad") && "yes".equals(tags.get("motorroad"))) return true;
		return false;
	}

	public static String getSignpost(Map<String, String> tags, boolean leftDriving) throws Exception {
		return getSignpost(tags, leftDriving, false);
	}

	public static String getSignpost(Map<String, String> tags) throws Exception {
		return getSignpost(tags, false);
	}
}
