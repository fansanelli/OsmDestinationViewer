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
import main.java.dev.pengunaria.osmdestinationviewer.render.api.SvgSerializer;
import main.java.dev.pengunaria.osmdestinationviewer.render.model.GraphicDocument;
import main.java.dev.pengunaria.osmdestinationviewer.render.model.RectElement;
import main.java.dev.pengunaria.osmdestinationviewer.render.model.TextElement;

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
			if (laneLines > maxLines)
				maxLines = laneLines;
		}
		int height = maxLines * lineHeight + 10;
		int width = numLanes * laneWidth + (numLanes - 1) * laneSpacing + 10;

		GraphicDocument gdoc = new GraphicDocument(width, height);
		gdoc.addElement(new RectElement(0, 0, width, height, signpost.getBackgroundColor().toString(), null));

		for (int i = 0; i < numLanes; i++) {
			Lane lane = this.signpost.getLanes()[i];
			int xOffset = 5 + i * (laneWidth + laneSpacing);
			int y = 15;
			for (Destination dest : lane.getDestinations()) {
				boolean hasColor = dest.getColor() != null && !dest.getColor().isEmpty();
				boolean hasStreet = dest.getStreet() != null && !dest.getStreet().isEmpty();
				String textColor = hasColor ? dest.getColor().getContrastColor()
						: signpost.getBackgroundColor().getContrastColor();

				if (hasColor) {
					int rectHeight = hasStreet ? lineHeight * 2 : lineHeight;
					RectElement innerRect = new RectElement(xOffset, y - lineHeight + 5, laneWidth, rectHeight,
							dest.getColor().toString(), null);
					gdoc.addElement(innerRect);
				}

				String text = dest.getName();
				String label = (lane.getDirection() != null) ? lane.getDirection().toString() : text;
				TextElement textElem = new TextElement(label, xOffset + 10, y, textColor, null, 16);
				gdoc.addElement(textElem);

				if (hasStreet) {
					y += lineHeight;
					TextElement streetElem = new TextElement(dest.getStreet(), xOffset + 20, y, textColor, null, 14);
					gdoc.addElement(streetElem);
				}
				y += lineHeight;
			}
		}

		try {
			SvgSerializer serializer = new SvgSerializer();
			return serializer.serialize(gdoc);
		} catch (Exception e) {
			throw new RuntimeException("SVG generation failed", e);
		}
	}
}
