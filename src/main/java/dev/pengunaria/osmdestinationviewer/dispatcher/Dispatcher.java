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

package main.java.dev.pengunaria.osmdestinationviewer.dispatcher;

import java.util.Map;

import main.java.dev.pengunaria.osmdestinationviewer.factory.ExitInfoFactory;
import main.java.dev.pengunaria.osmdestinationviewer.factory.Factory;
import main.java.dev.pengunaria.osmdestinationviewer.factory.GuidepostFactory;
import main.java.dev.pengunaria.osmdestinationviewer.factory.LanesFactory;
import main.java.dev.pengunaria.osmdestinationviewer.render.Renderable;

public class Dispatcher {
	public Renderable dispatch(Map<String, String> tags, String countryCode) throws Exception {
		if (tags == null || tags.size() == 0)
			throw new IllegalArgumentException("Tags is null or empty");

		Factory f;
		if (isGuidepost(tags)) {
			f = new GuidepostFactory();
		} else if (isDirectionOrLane(tags)) {
			f = new LanesFactory();
		} else {
			f = new ExitInfoFactory();
		}
		return f.createRenderer(tags, countryCode);
	}

	private static boolean isGuidepost(Map<String, String> tags) {
		return tags.containsKey("tourism") && "information".equals(tags.get("tourism"))
				&& tags.containsKey("information") && "guidepost".equals(tags.get("information"));
	}

	private static boolean isDirectionOrLane(Map<String, String> tags) {
		return tags.containsKey("destination:forward") || tags.containsKey("destination:backwards")
				|| tags.containsKey("destination:lanes") || tags.containsKey("destination:lanes:forward")
				|| tags.containsKey("destination:lanes:backward");
	}
}
