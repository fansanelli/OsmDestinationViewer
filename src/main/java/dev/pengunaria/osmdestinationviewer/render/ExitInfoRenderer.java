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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import main.java.dev.pengunaria.osmdestinationviewer.model.Destination;
import main.java.dev.pengunaria.osmdestinationviewer.model.Direction;
import main.java.dev.pengunaria.osmdestinationviewer.model.Lane;
import main.java.dev.pengunaria.osmdestinationviewer.model.Signpost;

/**
 * Class to render a simple exit information sign
 */
public class ExitInfoRenderer implements Renderable {
	private final Signpost signpost;

	public ExitInfoRenderer(Signpost signpost) {
		this.signpost = signpost;
	}

	@Override
	public String toSvg(boolean compact) {
		final int width = 220;
		final int lineHeight = 20;
		int y = 15;

		// Calcola l'altezza totale tenendo conto delle street
		Lane lane = this.signpost.getLanes()[0];
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
			rect.setAttribute("fill", this.signpost.getBackgroundColor().toString());
			svg.appendChild(rect);

			int currentY = y;
			for (Destination dest : lane.getDestinations()) {
				boolean hasColor = dest.getColor() != null && !dest.getColor().isEmpty();
				boolean hasStreet = dest.getStreet() != null && !dest.getStreet().isEmpty();
				String textColor = hasColor ? dest.getColor().getContrastColor()
						: this.signpost.getBackgroundColor().getContrastColor();

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
