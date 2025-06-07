/*
 * Copyright (c) 2025 Pengunaria.dev
 *
 * This file is part of OsmDestinationViewer and is licensed under the MIT License.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package main.java.dev.pengunaria.osmdestinationviewer;

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
		try {
			Document doc = SvgUtils.getNewDocument();

			Element svg = doc.createElementNS("http://www.w3.org/2000/svg", "svg");
			final int docWidth = 220;
			final int arrowHeight = 60;
			int y = 5; // Initial Document height
			for (Lane lane : lanes) {
				for (int i = 0; i < lane.getDestinations().length; i += 3) {
					Element arrow = SvgUtils.getArrow(doc, 5, y, 210, arrowHeight, false);
					for (int j = 0; j < 3; j++) {
						if (i + j >= lane.getDestinations().length)
							break;

						Element textEl = doc.createElementNS("http://www.w3.org/2000/svg", "text");
						textEl.setAttribute("x", String.valueOf(10));
						textEl.setAttribute("y", String.valueOf(20 + j * 15)); // 15 = textSpacing
						textEl.setAttribute("text-anchor", "left");
						textEl.setAttribute("alignment-baseline", "left");
						textEl.setAttribute("font-size", "13");
						textEl.setTextContent(lane.getDestinations()[i + j].getName());
						arrow.appendChild(textEl);
					}
					svg.appendChild(arrow);
					y += arrowHeight + 5;
				}
			}

			svg.setAttribute("width", String.valueOf(docWidth));
			svg.setAttribute("height", String.valueOf(y + 5));
			svg.setAttribute("viewBox", "0 0 " + docWidth + " " + (y + 5));
			doc.appendChild(svg);
			return SvgUtils.serializeDocument(doc);
		} catch (Exception e) {
			throw new RuntimeException("SVG generation failed", e);
		}
	}
}
