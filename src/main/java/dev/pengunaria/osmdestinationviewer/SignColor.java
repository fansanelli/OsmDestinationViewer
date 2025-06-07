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

/**
 * Class representing a color for a destination sign
 */
class SignColor {
	private final String rgb; // #RRGGBB or "none"

	SignColor(String color) throws Exception {
		if (color == null || color.isEmpty() || "none".equalsIgnoreCase(color)) {
			this.rgb = "none";
			return;
		}

		if (color.startsWith("#") && color.length() == 7) {
			this.rgb = color.toLowerCase();
		} else {
			switch (color.toLowerCase()) {
			case "white":
				this.rgb = "#ffffff";
				break;
			case "black":
				this.rgb = "#000000";
				break;
			case "green":
				this.rgb = "#007229";
				break;
			case "blue":
				this.rgb = "#004682";
				break;
			case "brown":
				this.rgb = "#7b3f00";
				break;
			case "red":
				this.rgb = "#d71920";
				break;
			case "yellow":
				this.rgb = "#f7d100";
				break;
			case "orange":
				this.rgb = "#f7a100";
				break;
			default:
				throw new Exception("Invalid color name: " + color);
			}
		}
	}

	public boolean isEmpty() {
		return "none".equals(rgb);
	}

	public String getContrastColor() {
		if (isEmpty())
			return "#000000";

		// Calcola la luminanza per scegliere bianco o nero
		int r = Integer.parseInt(rgb.substring(1, 3), 16);
		int g = Integer.parseInt(rgb.substring(3, 5), 16);
		int b = Integer.parseInt(rgb.substring(5, 7), 16);
		double luminance = (0.299 * r + 0.587 * g + 0.114 * b) / 255;
		return luminance > 0.5 ? "#000000" : "#FFFFFF";
	}

	@Override
	public String toString() {
		return rgb;
	}
}
