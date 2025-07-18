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
 * Class representing a single destination
 */
public class Destination {
	/**
	 * Name of the destination (e.g., city, locality, etc.)
	 */
	private String name;
	/**
	 * Reference (e.g., exit number, road code). Applicable only for Exit Info
	 * signposts.
	 */
	private String ref;
	/**
	 * Symbol associated with the destination (e.g., tourist symbol, icon).
	 */
	private String symbol;
	/**
	 * International reference (e.g., E45, E70).
	 */
	private String intRef;
	/**
	 * Street name associated with the destination.
	 */
	private String street;
	/**
	 * Sign color associated with the destination.
	 */
	private SignColor color;

	public Destination(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getIntRef() {
		return intRef;
	}
	public void setIntRef(String intRef) {
		this.intRef = intRef;
	}

	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}

	public SignColor getColor() {
		return color;
	}
	public void setColor(SignColor color) {
		this.color = color;
	}
}
