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

import main.java.dev.pengunaria.osmdestinationviewer.model.Lane;
import main.java.dev.pengunaria.osmdestinationviewer.model.Signpost;

public class GuidepostRenderer implements Renderable {
	private final Signpost signpost;

	public GuidepostRenderer(Signpost signpost) {
		this.signpost = signpost;
	}

	@Override
	public String toSvg(boolean compact) {
		try {
			Document doc = SvgUtils.getNewDocument();

			Element svg = doc.createElementNS("http://www.w3.org/2000/svg", "svg");
			final int docWidth = 320;
			final int arrowHeight = 60;
			int y = 5; // Initial Document height
			for (Lane lane : signpost.getLanes()) {
				for (int i = 0; i < lane.getDestinations().length; i += 3) {
					Element arrow = SvgUtils.getArrow(doc, 5, y, 310, arrowHeight, false);
					for (int j = 0; j < 3; j++) {
						if (i + j >= lane.getDestinations().length)
							break;

						Element textEl = doc.createElementNS("http://www.w3.org/2000/svg", "text");
						textEl.setAttribute("x", String.valueOf(10));
						textEl.setAttribute("y", String.valueOf(20 + j * 15)); // 15 = textSpacing
						textEl.setAttribute("text-anchor", "start");
						textEl.setAttribute("alignment-baseline", "middle");
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
