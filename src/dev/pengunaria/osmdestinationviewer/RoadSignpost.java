package dev.pengunaria.osmdestinationviewer;

import java.util.Map;

/**
 * Class to extend for road signposts.
 */
abstract class RoadSignpost implements Signpost {
	protected Map<String, String> tags;
	protected String countryCode;

	RoadSignpost(Map<String, String> tags, String countryCode) throws Exception {
		this.tags = tags;
		this.countryCode = countryCode;
	}

	protected boolean isLeftDriving() {
		if (this.countryCode != null) {
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

	protected String getBackgroundColor() {
		/**
		 * Vedi: https://wiki.openstreetmap.org/wiki/Key:destination:colour
		 */
		if (this.countryCode != null) {
			switch (this.countryCode.toUpperCase()) {
			case "DE": // Germany
				if (isMotorway()) {
					return "blue";
				}
				break;
			case "IT": // Italy
				if (isMotorway()) {
					return "green";
				} else if (isFreeway()) {
					return "blue";
				} else {
					return "white";
				}
			case "CA": // Canada
			case "US": // United States
				if (isMotorway()) {
					return "green";
				}
				break;
			}
		}
		return "white";
	}

	protected boolean isMotorway() {
		return tags.containsKey("highway")
				&& ("motorway".equals(tags.get("highway")) || "motorway_link".equals(tags.get("highway")));
	}

	protected boolean isFreeway() {
		return tags.containsKey("motorroad") && "yes".equals(tags.get("motorroad"));
	}
}
