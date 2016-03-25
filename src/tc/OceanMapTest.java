package tc;

import static org.junit.Assert.*;

import org.junit.Test;
import finalGame.OceanMap;

public class OceanMapTest {
	static OceanMap oceanMap;

	// tests if singleton is not null
	@Test
	public void mapExists(){
		oceanMap = OceanMap.getInstance();
		assertTrue(oceanMap != null);
	}
	
	// tests if dimensions are set correctly
	@Test
	public void correctDimensions(){
		oceanMap = OceanMap.getInstance();
		oceanMap.setMap(25);
		assertTrue(oceanMap.getDimensions() == 25);
	}

	// tests if oceanMap's grid gets initialized 
	@Test
	public void correctArray(){
		oceanMap = OceanMap.getInstance();
		oceanMap.setMap(25);
		assertTrue(oceanMap.getMap()[0][0] == 0);
	}
}
