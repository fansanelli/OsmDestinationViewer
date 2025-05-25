package dev.pengunaria.osmdestinationviewer;

import java.util.Map;

/**
 * Class representing a simple exit information sign
 */
class ExitSign extends RoadSignpost {
	private Lane lane;

	ExitSign(Map<String, String> tags, String countryCode) throws Exception {
		super(tags, countryCode);
		/**
		 * Vedi https://wiki.openstreetmap.org/wiki/Key:destination
		 * https://wiki.openstreetmap.org/wiki/Exit_Info
		 */
		if (tags.containsKey("destination")) {
			String[] destinationsStr = tags.get("destination").split(";");
			Destination[] destinations = new Destination[destinationsStr.length];
			for (int j = 0; j < destinations.length; j++) {
				destinations[j] = new Destination(destinationsStr[j]);
			}
			this.lane = new Lane(destinations, this.isLeftDriving() ? Direction.LEFT : Direction.RIGHT);
		} else {
			throw new Exception("Highway without destination");
		}
	}

	@Override
	public String toSvg(boolean compact) {
		final int width = 220;
		final int lineHeight = 20;
		final int maxChars = 28; // soglia per comprimere il testo
		int y = 15;
		int height = lane.getDestinations().length * lineHeight + 10;

		StringBuilder svg = new StringBuilder();
		svg.append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"").append(width).append("\" height=\"")
				.append(height).append("\" viewBox=\"0 0 ").append(width).append(" ").append(height).append("\">");

		// Sfondo rettangolare colorato
		svg.append("<rect x=\"0\" y=\"0\" width=\"").append(width).append("\" height=\"").append(height)
			.append("\" fill=\"").append(getBackgroundColor()).append("\"/>");

		svg.append("<g class=\"lane\">\n");
		String arrow = lane.getDirection() != null ? lane.getDirection().toString() : "";
		for (Destination dest : lane.getDestinations()) {
			String text = dest.getName();
			String style = "";
			if (text.length() > maxChars) {
				style = " style=\"letter-spacing:-1.5px;\"";
			}
			if (lane.getDirection() == Direction.LEFT) {
				svg.append("<text x=\"10\" y=\"").append(y).append("\"").append(style).append(" fill=\"white\">")
					.append(arrow).append(" ").append(text).append("</text>");
			} else {
				svg.append("<text x=\"10\" y=\"").append(y).append("\"").append(style).append(" fill=\"white\">")
					.append(text).append(" ").append(arrow).append("</text>");
			}
			y += lineHeight;
		}
		svg.append("</g>");
		svg.append("</svg>");
		return svg.toString();
	}
}
