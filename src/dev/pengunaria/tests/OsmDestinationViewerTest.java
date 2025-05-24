package dev.pengunaria.tests;

import dev.pengunaria.osmdestinationviewer.*;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OsmDestinationViewerTest {

	@Test
	void testGuidepostSignWithPipeAndSemicolon() throws Exception {
		Map<String, String> tags = new HashMap<>();
		tags.put("tourism", "information");
		tags.put("information", "guidepost");
		tags.put("destination", "Bait dei Aiseli 1:30;Malga Bodrina 2:30|Cima Roccapiana 3:45");

		String svg = OsmDestinationViewer.getSignpost(tags);

		assertTrue(svg.startsWith("<svg"));
		assertTrue(svg.contains("Bait dei Aiseli 1:30"));
		assertTrue(svg.contains("Malga Bodrina 2:30"));
		assertTrue(svg.contains("Cima Roccapiana 3:45"));
		assertTrue(svg.endsWith("</svg>"));
	}

	@Test
	void testGuidepostSignWithoutDestinationThrows() {
		Map<String, String> tags = new HashMap<>();
		tags.put("tourism", "information");
		tags.put("information", "guidepost");
		Exception exception = assertThrows(Exception.class, () -> OsmDestinationViewer.getSignpost(tags));
		assertEquals("Guidepost without destination", exception.getMessage());
	}

	@Test
	void testGuidepostSignWithDirectionNorth() throws Exception {
		Map<String, String> tags = new HashMap<>();
		tags.put("tourism", "information");
		tags.put("information", "guidepost");
		tags.put("direction_north", "(Bf) Stadtmitte 10 km;Holthausen 2.0 km;KP 11");
		tags.put("direction_north:ref", "Eu;ERS;D8;D7;EV15");
		tags.put("direction_north:symbol", "train_station;none");

		String svg = OsmDestinationViewer.getSignpost(tags);

		assertTrue(svg.startsWith("<svg"));
		assertTrue(svg.contains("(Bf) Stadtmitte 10 km"));
		assertTrue(svg.contains("Holthausen 2.0 km"));
		assertFalse(svg.contains("KP 11"));
		assertTrue(svg.endsWith("</svg>"));
	}
}