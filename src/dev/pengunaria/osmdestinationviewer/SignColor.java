package dev.pengunaria.osmdestinationviewer;

import java.awt.Color;

/**
 * Class representing a color for a destination sign
 */
class SignColor {
	private final Color color;

	SignColor(String color) {
		if (color == null || color.isEmpty() || "none".equals(color)) {
			color = null;
		}
		if (color.startsWith("#")) {
			this.color = Color.decode(color);
		} else {
			try {
				this.color = (Color) Color.class.getField(color.toUpperCase()).get(null);
			} catch (Exception e) {
				throw new IllegalArgumentException("Invalid color name: " + color, e);
			}
		}
	}

	public boolean isEmpty() {
		return color == null;
	}

	private static String getHtmlColor(Color color) {
		if (color == null) {
			return "none";
		}
		return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
	}

	public String getContrastColor() {
		// Calculate a contrasting color (black or white) based on the luminance
		double luminance = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
		return luminance > 0.5 ? getHtmlColor(Color.BLACK) : getHtmlColor(Color.WHITE);
	}

	@Override
	public String toString() {
		return getHtmlColor(color);
	}
}
