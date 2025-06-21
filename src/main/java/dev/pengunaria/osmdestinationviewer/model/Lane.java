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

package main.java.dev.pengunaria.osmdestinationviewer.model;

/**
 * Class representing a single lane in a road or path.
 */
public class Lane {
	private Destination[] destinations;
	private Direction direction;
	/**
	 * Applicable for Guideposts and Lane signs.
	 */
	private String[] ref;

	public Lane(Destination[] destinations, Direction direction, String[] ref) {
		this.destinations = destinations;
		this.direction = direction;
		this.ref = ref;
	}

	public Lane(Destination[] destinations) {
		this(destinations, null, null);
	}

	public Destination[] getDestinations() {
		return destinations;
	}
	public void setDestinations(Destination[] destinations) {
		this.destinations = destinations;
	}

	public Direction getDirection() {
		return direction;
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public String[] getRef() {
		return ref;
	}
	public void setRef(String[] ref) {
		this.ref = ref;
	}
}
