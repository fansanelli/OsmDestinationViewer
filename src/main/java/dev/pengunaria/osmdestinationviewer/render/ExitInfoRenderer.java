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
import main.java.dev.pengunaria.osmdestinationviewer.model.Direction;
import main.java.dev.pengunaria.osmdestinationviewer.model.Lane;
import main.java.dev.pengunaria.osmdestinationviewer.model.Signpost;
import main.java.dev.pengunaria.osmdestinationviewer.render.api.SvgSerializer;
import main.java.dev.pengunaria.osmdestinationviewer.render.model.GraphicDocument;
import main.java.dev.pengunaria.osmdestinationviewer.render.model.RectElement;
import main.java.dev.pengunaria.osmdestinationviewer.render.model.TextElement;

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

		Lane lane = this.signpost.getLanes()[0];
		int numLines = 0;
		for (Destination dest : lane.getDestinations()) {
			numLines++; // per il name
			if (dest.getStreet() != null && !dest.getStreet().isEmpty()) {
				numLines++; // per la street
			}
		}
		int height = numLines * lineHeight + 10;

		GraphicDocument gdoc = new GraphicDocument(width, height);
		// Sfondo
		gdoc.addElement(new RectElement(0, 0, width, height, this.signpost.getBackgroundColor().toString(), null));

		int currentY = y;
		for (Destination dest : lane.getDestinations()) {
			boolean hasColor = dest.getColor() != null && !dest.getColor().isEmpty();
			boolean hasStreet = dest.getStreet() != null && !dest.getStreet().isEmpty();
			String textColor = hasColor ? dest.getColor().getContrastColor()
					: this.signpost.getBackgroundColor().getContrastColor();

			if (hasColor) {
				int rectHeight = hasStreet ? lineHeight * 2 : lineHeight;
				RectElement innerRect = new RectElement(5, currentY - lineHeight + 5, width - 10, rectHeight,
						dest.getColor().toString(), null);
				gdoc.addElement(innerRect);
			}

			String arrow = lane.getDirection() != null ? lane.getDirection().toString() : "";
			String text = dest.getName();
			String label;
			if (lane.getDirection() == Direction.LEFT) {
				label = arrow + " " + text;
			} else {
				label = text + " " + arrow;
			}
			TextElement textElem = new TextElement(label, 10, currentY, textColor, null, 16);
			gdoc.addElement(textElem);

			if (hasStreet) {
				currentY += lineHeight;
				TextElement streetElem = new TextElement(dest.getStreet(), 20, currentY, textColor, null, 14);
				gdoc.addElement(streetElem);
			}
			currentY += lineHeight;
		}

		try {
			SvgSerializer serializer = new SvgSerializer();
			return serializer.serialize(gdoc);
		} catch (Exception e) {
			throw new RuntimeException("SVG generation failed", e);
		}
	}
}
