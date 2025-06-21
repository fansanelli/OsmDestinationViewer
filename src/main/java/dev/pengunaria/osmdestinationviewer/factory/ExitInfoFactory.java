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
import main.java.dev.pengunaria.osmdestinationviewer.model.Direction;
import main.java.dev.pengunaria.osmdestinationviewer.model.Lane;
import main.java.dev.pengunaria.osmdestinationviewer.model.SignColor;
import main.java.dev.pengunaria.osmdestinationviewer.model.Signpost;
import main.java.dev.pengunaria.osmdestinationviewer.render.ExitInfoRenderer;
import main.java.dev.pengunaria.osmdestinationviewer.render.Renderable;

/**
 * Class representing a simple exit information sign
 */
public class ExitInfoFactory implements Factory {
	@Override
	public Renderable createRenderer(Map<String, String> tags, String countryCode) throws Exception {
		/**
		 * Vedi https://wiki.openstreetmap.org/wiki/Key:destination
		 * https://wiki.openstreetmap.org/wiki/Exit_Info
		 */
		if (tags.containsKey("destination")) {
			String[] destinationsStr = tags.get("destination").split(";", -1);
			Destination[] destinations = new Destination[destinationsStr.length];
			for (int j = 0; j < destinations.length; j++) {
				destinations[j] = new Destination(destinationsStr[j]);
			}
			if (tags.containsKey("destination:ref")) {
				if (tags.get("destination:ref").contains(";")) {
					String[] refs = tags.get("destination:ref").split(";", -1);
					if (refs.length != destinations.length) {
						throw new Exception(
								"Number of references does not match number of destinations for tag: destination:ref");
					}
					for (int j = 0; j < refs.length; j++) {
						destinations[j].setRef(refs[j]);
					}
				} else {
					String ref = tags.get("destination:ref");
					for (int j = 0; j < destinations.length; j++) {
						destinations[j].setRef(ref);
					}
				}
			}
			if (tags.containsKey("destination:symbol")) {
				String[] symbols = tags.get("destination:symbol").split(";", -1);
				if (symbols.length != destinations.length) {
					throw new Exception(
							"Number of symbols does not match number of destinations for tag: destination:symbol");
				}
				for (int j = 0; j < symbols.length; j++) {
					destinations[j].setSymbol(symbols[j]);
				}
			}
			if (tags.containsKey("destination:int_ref")) {
				String[] intRefs = tags.get("destination:int_ref").split(";", -1);
				if (intRefs.length != destinations.length) {
					throw new Exception(
							"Number of internal references does not match number of destinations for tag: destination:int_ref");
				}
				for (int j = 0; j < intRefs.length; j++) {
					destinations[j].setIntRef(intRefs[j]);
				}
			}
			if (tags.containsKey("destination:street")) {
				String[] streets = tags.get("destination:street").split(";", -1);
				if (streets.length != destinations.length) {
					throw new Exception(
							"Number of streets does not match number of destinations for tag: destination:street");
				}
				for (int j = 0; j < streets.length; j++) {
					destinations[j].setStreet(streets[j]);
				}
			}
			if (tags.containsKey("destination:colour")) {
				String[] colors = tags.get("destination:colour").split(";", -1);
				if (colors.length != destinations.length) {
					throw new Exception(
							"Number of colors does not match number of destinations for tag: destination:colour");
				}
				for (int j = 0; j < colors.length; j++) {
					destinations[j].setColor(new SignColor(colors[j]));
				}
			}

			Signpost signpost = new Signpost();
			signpost.setLanes(new Lane[] { new Lane(destinations,
					RoadSignpostUtils.isLeftDriving(countryCode) ? Direction.LEFT : Direction.RIGHT, null) });
			signpost.setBackgroundColor(RoadSignpostUtils.getBackgroundColor(tags, countryCode));
			return new ExitInfoRenderer(signpost);
		} else {
			throw new Exception("Highway without destination");
		}
	}
}
