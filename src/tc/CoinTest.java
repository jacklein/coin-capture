package tc;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import finalGame.Coin;
import finalGame.OceanExplorer;
import finalGame.OceanMap;
import javafx.application.Application;


public class CoinTest extends OceanExplorer {
	static OceanMap oceanMap;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Initialize Java FX
		oceanMap = OceanMap.getInstance();
		oceanMap.setMap(25);
	    System.out.printf("About to launch FX App\n");
	    Thread t = new Thread("JavaFX Init Thread") {
	        public void run() {
	            Application.launch(OceanExplorer.class, new String[0]);
	        }
	    };
	    t.setDaemon(true);
	    t.start();
	    System.out.printf("FX App thread started\n");
	    Thread.sleep(500);
	}
	
	// tests if Coin is actually a coin
	@Test
	public void isCoin(){
		finalGame.Coin coin = new Coin(25);
		assertTrue(coin instanceof Coin);
	}
	
	// tests if x coordinate is in bounds
	@Test
	public void testXCoordinate() throws Exception {
		finalGame.Coin coin = new Coin(25);
		assertTrue(coin.getLocation().x >= 0);
	}
	
	// tests if y coordinate is in bounds
	@Test
	public void testYCoordinate(){
		finalGame.Coin coin = new Coin(25);
		assertTrue(coin.getLocation().y >= 0 && coin.getLocation().y <= 24);
	}
	
	// makes sure its value in the 2d int array equals 3
	// 3 is the value for all the coins on the map
	@Test
	public void testValue(){
		finalGame.Coin coin = new Coin(25);
		assertTrue(oceanMap.getMap()[coin.getLocation().x][coin.getLocation().y] == 3);
	}
}
