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

package main.java.dev.pengunaria.osmdestinationviewer.render.api;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import main.java.dev.pengunaria.osmdestinationviewer.render.model.ArrowElement;
import main.java.dev.pengunaria.osmdestinationviewer.render.model.GraphicDocument;
import main.java.dev.pengunaria.osmdestinationviewer.render.model.GraphicElement;
import main.java.dev.pengunaria.osmdestinationviewer.render.model.GroupElement;
import main.java.dev.pengunaria.osmdestinationviewer.render.model.RectElement;
import main.java.dev.pengunaria.osmdestinationviewer.render.model.TextElement;

public class SvgSerializer implements DocumentSerializer {
	@Override
	public String serialize(GraphicDocument gdoc) throws Exception {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.newDocument();

		Element svg = doc.createElementNS("http://www.w3.org/2000/svg", "svg");
		svg.setAttribute("xmlns", "http://www.w3.org/2000/svg");
		svg.setAttribute("width", String.valueOf(gdoc.getWidth()));
		svg.setAttribute("height", String.valueOf(gdoc.getHeight()));
		svg.setAttribute("viewBox", String.format("0 0 %d %d", gdoc.getWidth(), gdoc.getHeight()));
		doc.appendChild(svg);

		for (GraphicElement el : gdoc.getElements()) {
			appendElement(doc, svg, el);
		}

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		return writer.getBuffer().toString();
	}

	private void appendElement(Document doc, Element parent, GraphicElement el) {
		if (el instanceof GroupElement) {
			Element g = doc.createElementNS("http://www.w3.org/2000/svg", "g");
			parent.appendChild(g);
			for (GraphicElement child : ((GroupElement) el).getChildren()) {
				appendElement(doc, g, child);
			}
		} else if (el instanceof RectElement) {
			RectElement r = (RectElement) el;
			Element rect = doc.createElementNS("http://www.w3.org/2000/svg", "rect");
			rect.setAttribute("x", String.valueOf(r.getX()));
			rect.setAttribute("y", String.valueOf(r.getY()));
			rect.setAttribute("width", String.valueOf(r.getWidth()));
			rect.setAttribute("height", String.valueOf(r.getHeight()));
			rect.setAttribute("fill", r.getFill() != null ? r.getFill() : "none");
			rect.setAttribute("stroke", r.getStroke() != null ? r.getStroke() : "none");
			parent.appendChild(rect);
		} else if (el instanceof TextElement) {
			TextElement t = (TextElement) el;
			Element text = doc.createElementNS("http://www.w3.org/2000/svg", "text");
			text.setTextContent(t.getText());
			text.setAttribute("x", String.valueOf(t.getX()));
			text.setAttribute("y", String.valueOf(t.getY()));
			text.setAttribute("fill", t.getFill() != null ? t.getFill() : "black");
			text.setAttribute("font-family", t.getFontFamily() != null ? t.getFontFamily() : "sans-serif");
			text.setAttribute("font-size", String.valueOf(t.getFontSize()));
			parent.appendChild(text);
		} else if (el instanceof ArrowElement) {
			ArrowElement a = (ArrowElement) el;
			int arrowHead = a.getHeight() / 2;
			String points;
			if (!a.isLeftwards()) {
				// Right arrow (pentagon)
				points = String.format("0,0 %d,0 %d,%d %d,%d 0,%d", a.getWidth() - arrowHead, a.getWidth(),
						a.getHeight() / 2, a.getWidth() - arrowHead, a.getHeight(), a.getHeight());
			} else {
				// Left arrow (pentagon)
				points = String.format("%d,0 %d,0 0,%d %d,%d %d,%d", a.getWidth(), arrowHead, a.getHeight() / 2,
						arrowHead, a.getHeight(), a.getWidth(), a.getHeight());
			}
			Element g = doc.createElementNS("http://www.w3.org/2000/svg", "g");
			g.setAttribute("transform", String.format("translate(%d,%d)", a.getX(), a.getY()));
			Element polygon = doc.createElementNS("http://www.w3.org/2000/svg", "polygon");
			polygon.setAttribute("points", points);
			polygon.setAttribute("fill", a.getFill() != null ? a.getFill() : "#ffffff");
			polygon.setAttribute("stroke", a.getStroke() != null ? a.getStroke() : "#000000");
			polygon.setAttribute("stroke-width", String.valueOf(a.getStrokeWidth()));
			g.appendChild(polygon);
			parent.appendChild(g);
		}
	}
}
