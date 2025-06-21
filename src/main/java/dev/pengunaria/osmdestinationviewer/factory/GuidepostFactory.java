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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.java.dev.pengunaria.osmdestinationviewer.model.Destination;
import main.java.dev.pengunaria.osmdestinationviewer.model.Direction;
import main.java.dev.pengunaria.osmdestinationviewer.model.Lane;
import main.java.dev.pengunaria.osmdestinationviewer.model.Signpost;
import main.java.dev.pengunaria.osmdestinationviewer.render.GuidepostRenderer;
import main.java.dev.pengunaria.osmdestinationviewer.render.Renderable;

/**
 * Factory for creating Guidepost renderers from OSM tags.
 *
 * References: https://wiki.openstreetmap.org/wiki/IT:CAI#Luoghi_di_posa
 * https://wiki.openstreetmap.org/wiki/Key:direction_north
 */
public class GuidepostFactory implements Factory {
	private static final String[] directionTags = { "direction_north", "direction_east", "direction_south",
			"direction_west", "direction_northeast", "direction_northwest", "direction_southeast",
			"direction_southwest" };

	@Override
	public Renderable createRenderer(Map<String, String> tags, String countryCode) throws Exception {
		Lane[] lanes;
		/**
		 * Ogni destinazione inserita all'interno di una freccia deve essere separata da
		 * punto e virgola (;), la successiva freccia deve essere separata dalla prima
		 * con un "pipe" (|).
		 * 
		 * Es. Bait dei Aiseli 1:30;Malga Bodrina 2:30;Cima Roccapiana 3:45|Strada delle
		 * Longhe;schia di Mezzocorona
		 */
		if (tags.containsKey("destination")) {
			String[] lanesStr = tags.get("destination").split("\\|");
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
			List<Lane> laneList = new ArrayList<>();
			for (String tag : directionTags) {
				if (tags.containsKey(tag)) {
					String cleaned = tags.get(tag).replaceAll("(^|;)\\s*KP \\d+\\s*(;|$)", ";");
					cleaned = cleaned.replaceAll(";+", ";").replaceAll("^;|;$", "");
					if (cleaned.isEmpty())
						continue;

					String[] destinationsStr = cleaned.split(";");
					Destination[] destinations = new Destination[destinationsStr.length];
					for (int j = 0; j < destinationsStr.length; j++) {
						destinations[j] = new Destination(destinationsStr[j]);
					}
					if (tags.containsKey(tag + ":symbol")) {
						String[] symbols = tags.get(tag + ":symbol").split(";", -1);
						if (symbols.length != destinations.length) {
							throw new IllegalArgumentException(
									"Number of symbols does not match number of destinations for tag: " + tag);
						}
						for (int j = 0; j < symbols.length; j++) {
							destinations[j].setSymbol(symbols[j]);
						}
					}
					String[] ref = (tags.containsKey(tag + ":ref")) ? tags.get(tag + ":ref").split(";") : null;
					laneList.add(new Lane(destinations, Direction.valueOf(tag.replace("direction_", "").toUpperCase()),
							ref));
				}
			}
			lanes = laneList.toArray(new Lane[0]);
		}
		if (lanes.length == 0) {
			throw new IllegalArgumentException("Guidepost without destination");
		}
		Signpost signpost = new Signpost();
		signpost.setLanes(lanes);
		return new GuidepostRenderer(signpost);
	}
}
