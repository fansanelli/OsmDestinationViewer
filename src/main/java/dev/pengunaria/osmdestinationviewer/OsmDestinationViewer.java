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

import main.java.dev.pengunaria.osmdestinationviewer.dispatcher.Dispatcher;
import main.java.dev.pengunaria.osmdestinationviewer.render.Renderable;

/**
 * This class is used to create a signpost from OSM destination tags.
 */
public class OsmDestinationViewer {
	private final Renderable r;
	private boolean compact = false;

	/**
	 * Constructor for OsmDestinationViewer.
	 * 
	 * @param tags
	 * @param countryCode ex. DE for Germany, FR for France, etc.
	 * @throws Exception
	 */
	public OsmDestinationViewer(Map<String, String> tags, String countryCode) throws Exception {
		r = new Dispatcher().dispatch(tags, countryCode);
	}

	/**
	 * Sets the compact mode for the signpost (mobile).
	 * 
	 * @param compact
	 * @return the OsmDestinationViewer instance
	 */
	public OsmDestinationViewer setCompact(boolean compact) {
		this.compact = compact;
		return this;
	}

	/**
	 * Transforms the signpost into an image.
	 * 
	 * @return a SVG string
	 */
	public String getSvg() {
		return r.toSvg(compact);
	}
}
