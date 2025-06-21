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

package main.java.dev.pengunaria.osmdestinationviewer.render;

import main.java.dev.pengunaria.osmdestinationviewer.model.Destination;
import main.java.dev.pengunaria.osmdestinationviewer.model.Lane;
import main.java.dev.pengunaria.osmdestinationviewer.model.Signpost;

/**
 * Class to render lanes signposts
 */
public class LanesRenderer implements Renderable {
	private final Signpost signpost;

	public LanesRenderer(Signpost signpost) {
		this.signpost = signpost;
	}

	@Override
	public String toSvg(boolean compact) {
		final int laneWidth = 120;
		final int lineHeight = 20;
		final int laneSpacing = 10;
		int numLanes = this.signpost.getLanes().length;

		// Calcola l'altezza massima tra tutte le corsie
		int maxLines = 0;
		for (Lane lane : this.signpost.getLanes()) {
			int laneLines = 0;
			for (Destination dest : lane.getDestinations()) {
				laneLines++; // name
				if (dest.getStreet() != null && !dest.getStreet().isEmpty()) {
					laneLines++; // street
				}
			}
			if (laneLines > maxLines) maxLines = laneLines;
		}
		int height = maxLines * lineHeight + 10;
		int width = numLanes * laneWidth + (numLanes - 1) * laneSpacing + 10;

		try {
			org.w3c.dom.Document doc = SvgUtils.getNewDocument();

			org.w3c.dom.Element svg = doc.createElement("svg");
			svg.setAttribute("xmlns", "http://www.w3.org/2000/svg");
			svg.setAttribute("width", String.valueOf(width));
			svg.setAttribute("height", String.valueOf(height));
			svg.setAttribute("viewBox", "0 0 " + width + " " + height);
			doc.appendChild(svg);

			org.w3c.dom.Element rect = doc.createElement("rect");
			rect.setAttribute("x", "0");
			rect.setAttribute("y", "0");
			rect.setAttribute("width", String.valueOf(width));
			rect.setAttribute("height", String.valueOf(height));
			rect.setAttribute("fill", signpost.getBackgroundColor().toString());
			svg.appendChild(rect);

			for (int i = 0; i < numLanes; i++) {
				Lane lane = this.signpost.getLanes()[i];
				int xOffset = 5 + i * (laneWidth + laneSpacing);
				int y = 15;
				for (Destination dest : lane.getDestinations()) {
					boolean hasColor = dest.getColor() != null && !dest.getColor().isEmpty();
					boolean hasStreet = dest.getStreet() != null && !dest.getStreet().isEmpty();
					String textColor = hasColor ? dest.getColor().getContrastColor() : signpost.getBackgroundColor().getContrastColor();

					if (hasColor) {
						int rectHeight = hasStreet ? lineHeight * 2 : lineHeight;
						org.w3c.dom.Element innerRect = doc.createElement("rect");
						innerRect.setAttribute("x", String.valueOf(xOffset));
						innerRect.setAttribute("y", String.valueOf(y - lineHeight + 5));
						innerRect.setAttribute("width", String.valueOf(laneWidth));
						innerRect.setAttribute("height", String.valueOf(rectHeight));
						innerRect.setAttribute("rx", "4");
						innerRect.setAttribute("fill", dest.getColor().toString());
						svg.appendChild(innerRect);
					}

					org.w3c.dom.Element textElem = doc.createElement("text");
					textElem.setAttribute("x", String.valueOf(xOffset + 10));
					textElem.setAttribute("y", String.valueOf(y));
					textElem.setAttribute("fill", textColor);
					String text = dest.getName();
					if (lane.getDirection() != null) {
						textElem.setTextContent(lane.getDirection().toString());
					} else {
						textElem.setTextContent(text);
					}
					svg.appendChild(textElem);

					if (hasStreet) {
						y += lineHeight;
						org.w3c.dom.Element streetElem = doc.createElement("text");
						streetElem.setAttribute("x", String.valueOf(xOffset + 20));
						streetElem.setAttribute("y", String.valueOf(y));
						streetElem.setAttribute("fill", textColor);
						streetElem.setAttribute("font-size", "14");
						streetElem.setTextContent(dest.getStreet());
						svg.appendChild(streetElem);
					}
					y += lineHeight;
				}
			}

			return SvgUtils.serializeDocument(doc);
		} catch (Exception e) {
			throw new RuntimeException("SVG generation failed", e);
		}
	}
}
