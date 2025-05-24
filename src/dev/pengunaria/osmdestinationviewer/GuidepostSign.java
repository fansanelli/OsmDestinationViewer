package dev.pengunaria.osmdestinationviewer;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

class GuidepostSign implements Signpost {
	private static final String[] directionTags = { "direction_north", "direction_east", "direction_south",
			"direction_west", "direction_northeast", "direction_northwest", "direction_southeast",
			"direction_southwest" };
	private Lane[] lanes;

	GuidepostSign(Map<String, String> tags) throws Exception {
		/**
		 * Vedi: https://wiki.openstreetmap.org/wiki/IT:CAI#Luoghi_di_posa
		 * 
		 * Ogni destinazione inserita all'interno di una freccia deve essere separata da
		 * punto e virgola (;), la successiva freccia deve essere separata dalla prima
		 * con un "pipe" (|).
		 * 
		 * Es. Bait dei Aiseli 1:30;Malga Bodrina 2:30;Cima Roccapiana 3:45|Strada delle
		 * Longhe;schia di Mezzocorona
		 */
		if (tags.containsKey("destination")) {
			String[] lanesStr = tags.get("destination").split("\\|");
			this.lanes = new Lane[lanesStr.length];
			for (int i = 0; i < lanesStr.length; i++) {
				String[] destinationsStr = lanesStr[i].split(";");
				Destination[] destinations = new Destination[destinationsStr.length];
				for (int j = 0; j < destinations.length; j++) {
					destinations[j] = new Destination(destinationsStr[j]);
				}
				this.lanes[i] = new Lane(destinations);
			}
		} else {
			/**
			 * Vedi: https://wiki.openstreetmap.org/wiki/Key:direction_north
			 */
			List<Lane> laneList = new ArrayList<>();
			for (String tag : directionTags) {
				if (tags.containsKey(tag)) {
					// Rimuovi tutte le destinazioni "KP <numero>" dalla stringa prima di splittare
					String cleaned = tags.get(tag).replaceAll("(^|;)\\s*KP \\d+\\s*(;|$)", ";");
					// Rimuovi eventuali punti e virgola multipli o ai bordi
					cleaned = cleaned.replaceAll(";+", ";").replaceAll("^;|;$", "");
					if (cleaned.isEmpty())
						continue;

					String[] destinationsStr = cleaned.split(";");
					Destination[] destinations = new Destination[destinationsStr.length];
					for (int j = 0; j < destinationsStr.length; j++) {
						destinations[j] = new Destination(destinationsStr[j]);
					}
					if (tags.containsKey(tag + ":symbol")) {
						String[] symbols = tags.get(tag + ":symbol").split(";");
						if (symbols.length != destinations.length) {
							throw new Exception(
									"Number of symbols does not match number of destinations for tag: " + tag);
						}
						for (int j = 0; j < symbols.length; j++) {
							destinations[j].setSymbol(symbols[j]);
						}
					}
					laneList.add(
							new Lane(destinations, Direction.valueOf(tag.replace("direction_", "").toUpperCase())));
				}
			}
			this.lanes = laneList.toArray(new Lane[0]);
		}
		if (this.lanes.length == 0) {
			throw new Exception("Guidepost without destination");
		}
	}

	public String toSvg() {
		final int width = 220;
		final int lineHeight = 20;
		final int maxChars = 28; // soglia per comprimere il testo
		int y = 15;

		int numLines = 0;
		for (Lane lane : lanes)
			numLines += lane.destinations.length;
		int height = numLines * lineHeight + 10;

		StringBuilder svg = new StringBuilder();
		svg.append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"").append(width).append("\" height=\"")
				.append(height).append("\" viewBox=\"0 0 ").append(width).append(" ").append(height).append("\">");

		for (Lane lane : lanes) {
			svg.append("<g class=\"lane\">");
			if (lane.getDirection() != null) {
				svg.append("<text x=\"10\" y=\"").append(y).append("\" font-weight=\"bold\" fill=\"#555\">")
					.append(lane.getDirection()).append("</text>");
				y += lineHeight;
			}
			for (Destination dest : lane.destinations) {
				String text = dest.getName();
				String style = "";
				if (text.length() > maxChars) {
					style = " style=\"letter-spacing:-1.5px;\"";
				}
				svg.append("<text x=\"10\" y=\"").append(y).append("\"").append(style).append(">").append(text)
						.append("</text>");
				y += lineHeight;
			}
			svg.append("</g>");
		}
		svg.append("</svg>");
		return svg.toString();
	}
}
