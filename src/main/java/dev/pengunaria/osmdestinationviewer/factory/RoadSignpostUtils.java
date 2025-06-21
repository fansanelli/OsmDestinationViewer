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

import main.java.dev.pengunaria.osmdestinationviewer.model.SignColor;

/**
 * Utilities for road signposts.
 */
class RoadSignpostUtils {

	public static boolean isLeftDriving(String countryCode) {
		if (countryCode != null) {
			switch (countryCode.toUpperCase()) {
			case "GB": // United Kingdom
			case "IE": // Ireland
			case "AU": // Australia
			case "NZ": // New Zealand
			case "ZA": // South Africa
			case "JP": // Japan
			case "IN": // India
			case "SG": // Singapore
			case "MY": // Malaysia
			case "TH": // Thailand
			case "ID": // Indonesia
			case "PH": // Philippines
			case "HK": // Hong Kong
			case "LK": // Sri Lanka
			case "KE": // Kenya
			case "PK": // Pakistan
			case "BD": // Bangladesh
			case "NG": // Nigeria
			case "GH": // Ghana
			case "TZ": // Tanzania
			case "UG": // Uganda
			case "ZM": // Zambia
			case "ZW": // Zimbabwe
			case "BW": // Botswana
			case "MW": // Malawi
			case "LS": // Lesotho
			case "NA": // Namibia
			case "MM": // Myanmar
			case "MV": // Maldives
			case "NP": // Nepal
			case "BN": // Brunei
			case "KH": // Cambodia
			case "LA": // Laos
			case "VN": // Vietnam
			case "TL": // Timor-Leste
			case "FJ": // Fiji
			case "PG": // Papua New Guinea
			case "WS": // Samoa
			case "SB": // Solomon Islands
			case "VU": // Vanuatu
			case "KI": // Kiribati
			case "TO": // Tonga
			case "TV": // Tuvalu
			case "MH": // Marshall Islands
			case "FM": // Federated States of Micronesia
			case "PW": // Palau
			case "SC": // Seychelles
			case "BB": // Barbados
			case "BS": // Bahamas
			case "JM": // Jamaica
			case "TT": // Trinidad and Tobago
			case "LC": // Saint Lucia
			case "GD": // Grenada
			case "VC": // Saint Vincent and the Grenadines
			case "KN": // Saint Kitts and Nevis
			case "DM": // Dominica
			case "AG": // Antigua and Barbuda
			case "AI": // Anguilla
			case "MS": // Montserrat
			case "VG": // British Virgin Islands
			case "KY": // Cayman Islands
			case "BM": // Bermuda
			case "TC": // Turks and Caicos Islands
			case "PR": // Puerto Rico
			case "GU": // Guam
			case "MP": // Northern Mariana Islands
			case "AS": // American Samoa
			case "UM": // United States Minor Outlying Islands
			case "VI": // U.S. Virgin Islands
				// TODO: Check if the list is correct and complete
				return true;
			}
		}
		return false; // Default to right driving
	}

	public static SignColor getBackgroundColor(Map<String, String> tags, String countryCode) {
		/**
		 * Vedi: https://wiki.openstreetmap.org/wiki/Key:destination:colour
		 */
		String backgroundColor = "white";
		if (countryCode != null) {
			switch (countryCode.toUpperCase()) {
			case "DE": // Germany
				if (isMotorway(tags)) {
					backgroundColor = "blue";
				}
				break;
			case "IT": // Italy
				if (isMotorway(tags)) {
					backgroundColor = "green";
				} else if (isFreeway(tags)) {
					backgroundColor = "blue";
				}
			case "CA": // Canada
			case "US": // United States
				if (isMotorway(tags)) {
					backgroundColor = "green";
				}
				break;
			}
		}
		try {
			return new SignColor(backgroundColor);
		} catch (Exception e) {
			return null;
		}
	}

	private static boolean isMotorway(Map<String, String> tags) {
		return tags.containsKey("highway")
				&& ("motorway".equals(tags.get("highway")) || "motorway_link".equals(tags.get("highway")));
	}

	private static boolean isFreeway(Map<String, String> tags) {
		return tags.containsKey("motorroad") && "yes".equals(tags.get("motorroad"));
	}
}
