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

package main.java.dev.pengunaria.osmdestinationviewer.factory;

import java.util.Map;

import main.java.dev.pengunaria.osmdestinationviewer.model.Destination;
import main.java.dev.pengunaria.osmdestinationviewer.model.Lane;
import main.java.dev.pengunaria.osmdestinationviewer.model.Signpost;
import main.java.dev.pengunaria.osmdestinationviewer.render.LanesRenderer;
import main.java.dev.pengunaria.osmdestinationviewer.render.Renderable;

/**
 * Factory for creating Lane sign renderers from OSM tags.
 *
 * References: https://wiki.openstreetmap.org/wiki/Key:destination
 * https://wiki.openstreetmap.org/wiki/User:Mueschel/DestinationTagging
 */
public class LanesFactory implements Factory {
	@Override
	public Renderable createRenderer(Map<String, String> tags, String countryCode) throws Exception {
		Lane[] lanes;
		if (tags.containsKey("destination:lanes")) {
			String[] lanesStr = tags.get("destination:lanes").split("\\|");
			if (tags.containsKey("lanes") && !Integer.toString(lanesStr.length).equals(tags.get("lanes"))) {
				throw new IllegalArgumentException(
						"Number of lanes does not match number of destinations for tag: \"destination:lanes\"");
			}
			lanes = new Lane[lanesStr.length];
			for (int i = 0; i < lanesStr.length; i++) {
				String[] destinationsStr = lanesStr[i].split(";");
				Destination[] destinations = new Destination[destinationsStr.length];
				for (int j = 0; j < destinations.length; j++) {
					destinations[j] = new Destination(destinationsStr[j]);
				}
				lanes[i] = new Lane(destinations);
			}
		} else {
			throw new IllegalArgumentException("Highway without destination");
		}
		Signpost signpost = new Signpost();
		signpost.setLanes(lanes);
		signpost.setBackgroundColor(RoadSignpostUtils.getBackgroundColor(tags, countryCode));
		return new LanesRenderer(signpost);
	}
}
