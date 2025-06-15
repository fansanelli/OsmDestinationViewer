package test.java.dev.pengunaria.osmdestinationviewer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import main.java.dev.pengunaria.osmdestinationviewer.OsmDestinationViewer;

public class OsmDestinationViewerTest {

	@Test
	void testGuidepostSignWithPipeAndSemicolon() throws Exception {
		Map<String, String> tags = new HashMap<>();
		tags.put("tourism", "information");
		tags.put("information", "guidepost");
		tags.put("destination", "Bait dei Aiseli 1:30;Malga Bodrina 2:30|Cima Roccapiana 3:45");

		String svg = new OsmDestinationViewer(tags, null).setCompact(false).getSvg();

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
		Exception exception = assertThrows(Exception.class,
				() -> new OsmDestinationViewer(tags, null).setCompact(false).getSvg());
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

		String svg = new OsmDestinationViewer(tags, null).setCompact(false).getSvg();

		assertTrue(svg.startsWith("<svg"));
		assertTrue(svg.contains("(Bf) Stadtmitte 10 km"));
		assertTrue(svg.contains("Holthausen 2.0 km"));
		assertFalse(svg.contains("KP 11"));
		assertTrue(svg.endsWith("</svg>"));
	}

	@Test
	void testSimpleExit() throws Exception {
		Map<String, String> tags = new HashMap<>();
		tags.put("highway", "motorway_link");
		tags.put("oneway", "yes");
		tags.put("destination", "Saint-Valérien-de-Milton");
		tags.put("destination:ref", "211 Sud");

		String svg = new OsmDestinationViewer(tags, "CA").setCompact(false).getSvg();

		assertTrue(svg.startsWith("<svg"));
		assertTrue(svg.contains("Saint-Valérien-de-Milton"));
		assertTrue(svg.endsWith("</svg>"));
	}

	@Test
	void testTwoDestinationsOneRef() throws Exception {
		Map<String, String> tags = new HashMap<>();
		tags.put("highway", "motorway_link");
		tags.put("oneway", "yes");
		tags.put("destination", "Hazleton;Allentown");
		tags.put("destination:ref", "I 81 North");
		tags.put("destination:ref:to", "I 78");

		String svg = new OsmDestinationViewer(tags, "US").setCompact(false).getSvg();

		assertTrue(svg.startsWith("<svg"));
		assertTrue(svg.contains("Hazleton"));
		assertTrue(svg.contains("Allentown"));
		assertTrue(svg.endsWith("</svg>"));
	}
	
	@Test
	void testAutostrade() throws Exception {
		Map<String, String> tags = new HashMap<>();
		tags.put("highway", "motorway_link");
		tags.put("oneway", "yes");
		tags.put("destination", "MESTRE;ospedale;CASTELFRANCO V");
		tags.put("destination:street", "Via Castellana;;");
		tags.put("destination:colour", "white;white;blue");
		tags.put("destination:symbol", "centre;hospital;");

		String svg = new OsmDestinationViewer(tags, "IT").setCompact(false).getSvg();

		assertTrue(svg.startsWith("<svg"));
		assertTrue(svg.contains("MESTRE"));
		assertTrue(svg.contains("ospedale"));
		assertTrue(svg.contains("CASTELFRANCO V"));
		assertTrue(svg.endsWith("</svg>"));
	}

	@Test
	void testHexColor() throws Exception {
		Map<String, String> tags = new HashMap<>();
		tags.put("highway", "motorway_link");
		tags.put("oneway", "yes");
		tags.put("destination", "HEX;NOWHERE");
		tags.put("destination:colour", "#F0E060;none");

		String svg = new OsmDestinationViewer(tags, "IT").setCompact(false).getSvg();

		assertTrue(svg.startsWith("<svg"));
		assertTrue(svg.contains("#f0e060"));
		assertTrue(svg.endsWith("</svg>"));
	}

	@Test
	void testXSS() throws Exception {
		Map<String, String> tags = new HashMap<>();
		tags.put("highway", "motorway_link");
		tags.put("oneway", "yes");
		tags.put("destination", "<NOWHERE>");

		String svg = new OsmDestinationViewer(tags, "IT").setCompact(false).getSvg();

		assertTrue(svg.startsWith("<svg"));
		assertFalse(svg.contains("<NOWHERE>")); // non deve essere presente
		assertTrue(svg.contains("&lt;NOWHERE&gt;")); // deve essere sanificato
		assertTrue(svg.endsWith("</svg>"));
	}

	
	@Test
	void testLanes() throws Exception {
		Map<String, String> tags = new HashMap<>();
		tags.put("highway", "motorway");
		tags.put("lanes", "5");
		tags.put("destination:lanes", "Oberhausen;Düsseldorf;Köln-Nord|Oberhausen;Düsseldorf;Köln-Nord|Oberhausen;Düsseldorf;Köln-Nord|K-Zentrum|Olpe;Gummersbach");
		tags.put("destination:ref:lanes", "A 3|A 3|A 3||A 4");

		String svg = new OsmDestinationViewer(tags, "DE").setCompact(false).getSvg();

		assertTrue(svg.startsWith("<svg"));
		assertTrue(svg.endsWith("</svg>"));

		// Verifica che tutte le destinazioni siano presenti
		assertTrue(svg.contains("Oberhausen"));
		assertTrue(svg.contains("Düsseldorf"));
		assertTrue(svg.contains("Köln-Nord"));
		assertTrue(svg.contains("K-Zentrum"));
		assertTrue(svg.contains("Olpe"));
		assertTrue(svg.contains("Gummersbach"));
	}	
}