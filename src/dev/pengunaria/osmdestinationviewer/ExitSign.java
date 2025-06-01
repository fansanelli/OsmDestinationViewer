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
			String[] destinationsStr = tags.get("destination").split(";", -1);
			Destination[] destinations = new Destination[destinationsStr.length];
			for (int j = 0; j < destinations.length; j++) {
				destinations[j] = new Destination(destinationsStr[j]);
			}
			if (tags.containsKey("destination:ref")) {
				if (tags.get("destination:ref").contains(";")) {
					String[] refs = tags.get("destination:ref").split(";", -1);
					if (refs.length != destinations.length) {
						throw new Exception(
								"Number of references does not match number of destinations for tag: destination:ref");
					}
					for (int j = 0; j < refs.length; j++) {
						destinations[j].setRef(refs[j]);
					}
				} else {
					String ref = tags.get("destination:ref");
					for (int j = 0; j < destinations.length; j++) {
						destinations[j].setRef(ref);
					}
				}
			}
			if (tags.containsKey("destination:symbol")) {
				String[] symbols = tags.get("destination:symbol").split(";", -1);
				if (symbols.length != destinations.length) {
					throw new Exception(
							"Number of symbols does not match number of destinations for tag: destination:symbol");
				}
				for (int j = 0; j < symbols.length; j++) {
					destinations[j].setSymbol(symbols[j]);
				}
			}
			if (tags.containsKey("destination:int_ref")) {
				String[] intRefs = tags.get("destination:int_ref").split(";", -1);
				if (intRefs.length != destinations.length) {
					throw new Exception(
							"Number of internal references does not match number of destinations for tag: destination:int_ref");
				}
				for (int j = 0; j < intRefs.length; j++) {
					destinations[j].setIntRef(intRefs[j]);
				}
			}
			if (tags.containsKey("destination:street")) {
				String[] streets = tags.get("destination:street").split(";", -1);
				if (streets.length != destinations.length) {
					throw new Exception(
							"Number of streets does not match number of destinations for tag: destination:street");
				}
				for (int j = 0; j < streets.length; j++) {
					destinations[j].setStreet(streets[j]);
				}
			}
			if (tags.containsKey("destination:colour")) {
				String[] colors = tags.get("destination:colour").split(";", -1);
				if (colors.length != destinations.length) {
					throw new Exception(
							"Number of colors does not match number of destinations for tag: destination:colour");
				}
				for (int j = 0; j < colors.length; j++) {
					destinations[j].setColor(new SignColor(colors[j]));
				}
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

		// Calcola l'altezza totale tenendo conto delle street
		int numLines = 0;
		for (Destination dest : lane.getDestinations()) {
			numLines++; // per il name
			if (dest.getStreet() != null && !dest.getStreet().isEmpty()) {
				numLines++; // per la street
			}
		}
		int height = numLines * lineHeight + 10;

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

			boolean hasColor = dest.getColor() != null && !dest.getColor().isEmpty();
			boolean hasStreet = dest.getStreet() != null && !dest.getStreet().isEmpty();

			// Calcola il colore del testo
			String textColor = hasColor ? dest.getColor().getContrastColor() : getBackgroundColor().getContrastColor();

			// Rettangolo colorato per il name (e street se presente)
			if (hasColor) {
				int rectHeight = hasStreet ? lineHeight * 2 : lineHeight;
				svg.append("<rect x=\"5\" y=\"").append(y - lineHeight + 5).append("\" width=\"")
					.append(width - 10).append("\" height=\"").append(rectHeight).append("\" rx=\"4\" fill=\"")
					.append(dest.getColor()).append("\"/>");
			}

			// Testo principale (name)
			if (lane.getDirection() == Direction.LEFT) {
				svg.append("<text x=\"10\" y=\"").append(y).append("\"").append(style).append(" fill=\"").append(textColor).append("\">")
					.append(arrow).append(" ").append(text).append("</text>");
			} else {
				svg.append("<text x=\"10\" y=\"").append(y).append("\"").append(style).append(" fill=\"").append(textColor).append("\">")
					.append(text).append(" ").append(arrow).append("</text>");
			}

			// Street sotto il name, dentro lo stesso rect se presente
			if (hasStreet) {
				y += lineHeight;
				// Il colore del testo della street segue la stessa logica del name
				svg.append("<text x=\"20\" y=\"").append(y).append("\" fill=\"").append(textColor).append("\" font-size=\"14\">")
					.append(dest.getStreet()).append("</text>");
			}
			y += lineHeight;
		}
		svg.append("</g>");
		svg.append("</svg>");
		return svg.toString();
	}
}
