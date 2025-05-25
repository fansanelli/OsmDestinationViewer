package dev.pengunaria.osmdestinationviewer;

/**
 * Interface to be implemented by classes representing signposts.
 */
interface Signpost {
	String toSvg(boolean compact);
}
