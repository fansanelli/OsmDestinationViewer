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
import main.java.dev.pengunaria.osmdestinationviewer.render.model.ArrowElement;
import main.java.dev.pengunaria.osmdestinationviewer.render.model.GraphicDocument;
import main.java.dev.pengunaria.osmdestinationviewer.render.model.GroupElement;
import main.java.dev.pengunaria.osmdestinationviewer.render.model.TextElement;

public class GuidepostRenderer implements Renderable {
	private final Signpost signpost;

	public GuidepostRenderer(Signpost signpost) {
		this.signpost = signpost;
	}

	@Override
	public String toSvg(boolean compact) {
		final int docWidth = 320;
		final int arrowHeight = 60;
		int y = 5;
		GraphicDocument gdoc = new GraphicDocument(docWidth, 1000); // altezza provvisoria, aggiornata dopo
		int maxY = y;

		for (Lane lane : signpost.getLanes()) {
			for (int i = 0; i < lane.getDestinations().length; i += 3) {
				GroupElement arrowGroup = new GroupElement();
				ArrowElement arrow = new ArrowElement(5, y, 310, arrowHeight, false, "#fff", "#000", 2);
				arrowGroup.addChild(arrow);
				for (int j = 0; j < 3; j++) {
					if (i + j >= lane.getDestinations().length)
						break;
					Destination dest = lane.getDestinations()[i + j];
					TextElement textEl = new TextElement(dest.getName(), 10, y + 20 + j * 15, "#000", null, 13);
					arrowGroup.addChild(textEl);
				}
				gdoc.addElement(arrowGroup);
				y += arrowHeight + 5;
				maxY = y;
			}
		}
		gdoc = new GraphicDocument(docWidth, maxY + 5);
		// Ricostruisci la scena con l'altezza corretta
		y = 5;
		for (Lane lane : signpost.getLanes()) {
			for (int i = 0; i < lane.getDestinations().length; i += 3) {
				GroupElement arrowGroup = new GroupElement();
				ArrowElement arrow = new ArrowElement(5, y, 310, arrowHeight, false, "#fff", "#000", 2);
				arrowGroup.addChild(arrow);
				for (int j = 0; j < 3; j++) {
					if (i + j >= lane.getDestinations().length)
						break;
					Destination dest = lane.getDestinations()[i + j];
					TextElement textEl = new TextElement(dest.getName(), 10, y + 20 + j * 15, "#000", null, 13);
					arrowGroup.addChild(textEl);
				}
				gdoc.addElement(arrowGroup);
				y += arrowHeight + 5;
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
