package dev.pengunaria.osmdestinationviewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
						String[] symbols = tags.get(tag + ":symbol").split(";", -1);
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

	@Override
	public String toSvg(boolean compact) {
		final int width = 220;
		final int arrowHeight = 60;
		final int arrowSpacing = 10;
		java.util.List<Destination> allDest = new java.util.ArrayList<>();
		for (Lane lane : lanes) {
			for (Destination dest : lane.getDestinations()) {
				allDest.add(dest);
			}
		}
		int numArrows = (int) Math.ceil(allDest.size() / 3.0);
		int height = numArrows * arrowHeight + (numArrows - 1) * arrowSpacing + 10;
		try {
			Document doc = SvgUtils.getNewDocument();

			Element svg = doc.createElementNS("http://www.w3.org/2000/svg", "svg");
			svg.setAttribute("width", String.valueOf(width));
			svg.setAttribute("height", String.valueOf(height));
			svg.setAttribute("viewBox", "0 0 " + width + " " + height);
			doc.appendChild(svg);

			int y = 5;
			for (int i = 0; i < allDest.size(); i += 3) {
				String[] lines = new String[3];
				for (int j = 0; j < 3; j++) {
					if (i + j < allDest.size()) {
						lines[j] = allDest.get(i + j).getName();
					} else {
						lines[j] = "";
					}
				}
				Element arrow = SvgUtils.getArrow(doc, 10, y, false);
				// Aggiungi i <text> direttamente qui
				int textSpacing = 15;
				int arrowWidth = 200;
				for (int t = 0; t < 3; t++) {
					Element textEl = doc.createElementNS("http://www.w3.org/2000/svg", "text");
					textEl.setAttribute("x", String.valueOf(arrowWidth / 2));
					textEl.setAttribute("y", String.valueOf(20 + t * textSpacing));
					textEl.setAttribute("text-anchor", "middle");
					textEl.setAttribute("alignment-baseline", "middle");
					textEl.setAttribute("font-size", "13");
					textEl.setTextContent(lines[t]);
					arrow.appendChild(textEl);
				}
				svg.appendChild(arrow);
				y += arrowHeight + arrowSpacing;
			}

			return SvgUtils.serializeDocument(doc);
		} catch (Exception e) {
			throw new RuntimeException("SVG generation failed", e);
		}
	}
}
