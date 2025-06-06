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

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

class SvgUtils {
	public static Document getNewDocument() throws ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.newDocument();
		return doc;
	}

	public static String serializeDocument(Document doc) throws TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		return writer.getBuffer().toString();
	}

	/**
	 * Restituisce un gruppo SVG <g> a forma di freccia (senza testo).
	 * 
	 * @param doc       Document SVG
	 * @param x         posizione x
	 * @param y         posizione y
	 * @param width     Arrow width
	 * @param height    Arrow height
	 * @param leftwards true per freccia verso sinistra, false per destra
	 */
	public static Element getArrow(Document doc, int x, int y, int width, int height, boolean leftwards) {
		int arrowHead = height / 2;
		Element g = doc.createElementNS("http://www.w3.org/2000/svg", "g");
		g.setAttribute("class", "guidepost-arrow");
		g.setAttribute("transform", "translate(" + x + "," + y + ")");
		String points;
		if (!leftwards) {
			// Freccia verso destra (pentagono)
			points = (0) + "," + (0) + " " + (width - arrowHead) + "," + (0) + " " + (width) + "," + (height / 2) + " "
					+ (width - arrowHead) + "," + (height) + " " + (0) + "," + (height);
		} else {
			// Freccia verso sinistra (pentagono)
			points = (width) + "," + (0) + " " + (arrowHead) + "," + (0) + " " + (0) + "," + (height / 2) + " "
					+ (arrowHead) + "," + (height) + " " + (width) + "," + (height);
		}
		Element polygon = doc.createElementNS("http://www.w3.org/2000/svg", "polygon");
		polygon.setAttribute("points", points);
		polygon.setAttribute("fill", "#ffffff");
		polygon.setAttribute("stroke", "#000000");
		polygon.setAttribute("stroke-width", "2");
		g.appendChild(polygon);
		return g;
	}
}
