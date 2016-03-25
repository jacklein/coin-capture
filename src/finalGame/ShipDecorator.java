package finalGame;

import java.awt.Point;

// Decorator abstract class for Ship
public abstract class ShipDecorator extends Ship{
	boolean removeFromMap;
	
	public ShipDecorator(){
		// singleton coordinates
		oceanMap = OceanMap.getInstance();
		dimensions = oceanMap.getDimensions();
		coords = oceanMap.getMap();
		
		// initially assigns decorator random coordinate
		while(true){
			int x = rand.nextInt(dimensions);
			int y = rand.nextInt(dimensions);
			// initial point can't be an island, pirate, mainShip, a coin, or other decorator
			if(coords[x][y] != 1 && coords[x][y] != 2 && coords[x][y] != 3 && coords[x][y] != 4){
				currentLocation = new Point(x,y);
				// decorators are marked as '4' in 2d int array
				coords[x][y] = 4;
				break;
			}
		}
	}
	
	// all decorators use this function
	public abstract boolean removeFromMap();
}
