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

import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

		try {
			Document doc = SvgUtils.getNewDocument();

			Element svg = doc.createElement("svg");
			svg.setAttribute("xmlns", "http://www.w3.org/2000/svg");
			svg.setAttribute("width", String.valueOf(width));
			svg.setAttribute("height", String.valueOf(height));
			svg.setAttribute("viewBox", "0 0 " + width + " " + height);
			doc.appendChild(svg);

			Element rect = doc.createElement("rect");
			rect.setAttribute("x", "0");
			rect.setAttribute("y", "0");
			rect.setAttribute("width", String.valueOf(width));
			rect.setAttribute("height", String.valueOf(height));
			rect.setAttribute("fill", getBackgroundColor().toString());
			svg.appendChild(rect);

			int currentY = y;
			for (Destination dest : lane.getDestinations()) {
				boolean hasColor = dest.getColor() != null && !dest.getColor().isEmpty();
				boolean hasStreet = dest.getStreet() != null && !dest.getStreet().isEmpty();
				String textColor = hasColor ? dest.getColor().getContrastColor()
						: getBackgroundColor().getContrastColor();

				if (hasColor) {
					int rectHeight = hasStreet ? lineHeight * 2 : lineHeight;
					Element innerRect = doc.createElement("rect");
					innerRect.setAttribute("x", "5");
					innerRect.setAttribute("y", String.valueOf(currentY - lineHeight + 5));
					innerRect.setAttribute("width", String.valueOf(width - 10));
					innerRect.setAttribute("height", String.valueOf(rectHeight));
					innerRect.setAttribute("rx", "4");
					innerRect.setAttribute("fill", dest.getColor().toString());
					svg.appendChild(innerRect);
				}

				Element textElem = doc.createElement("text");
				textElem.setAttribute("x", "10");
				textElem.setAttribute("y", String.valueOf(currentY));
				textElem.setAttribute("fill", textColor);
				String arrow = lane.getDirection() != null ? lane.getDirection().toString() : "";
				String text = dest.getName();
				if (lane.getDirection() == Direction.LEFT) {
					textElem.setTextContent(arrow + " " + text);
				} else {
					textElem.setTextContent(text + " " + arrow);
				}
				svg.appendChild(textElem);

				if (hasStreet) {
					currentY += lineHeight;
					Element streetElem = doc.createElement("text");
					streetElem.setAttribute("x", "20");
					streetElem.setAttribute("y", String.valueOf(currentY));
					streetElem.setAttribute("fill", textColor);
					streetElem.setAttribute("font-size", "14");
					streetElem.setTextContent(dest.getStreet());
					svg.appendChild(streetElem);
				}
				currentY += lineHeight;
			}

			return SvgUtils.serializeDocument(doc);
		} catch (Exception e) {
			throw new RuntimeException("SVG generation failed", e);
		}
	}
}
