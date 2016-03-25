package finalGame;

import java.awt.Point;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// PirateShip uses the factory pattern to create different pirates
// which affect mainShip uniquely
public abstract class PirateShip implements Observer{
	OceanMap oceanMap;
	int coords[][];
	int dimensions;
	
	boolean hitShip;
	Image pirateShipImage;
	ImageView pirateShipImageView;
	Point currentLocation;
	int scale;
	int count;
	int moveAmount;
	// list of map values the pirates can't go over
	int[] offLimits = {1,3,4};
	Random rand = new Random();
	
	public PirateShip(int scale){
		// singleton coordinates
		oceanMap = OceanMap.getInstance();
		dimensions = oceanMap.getDimensions();
		coords = oceanMap.getMap();
		this.scale = scale;
		
		// initially assigns pirate ship random coordinate
		while(true){
			int x = rand.nextInt(dimensions);
			int y = rand.nextInt(dimensions);
			// initial point can't be an island, mainShip, or other pirate
			if(coords[x][y] != 1 && coords[x][y] != 2){
				currentLocation = new Point(x,y);
				// pirates are marked as '1' in 2d int array
				coords[x][y] = 1;
				break;
			}
		}
		// chooses a movement between 2 and 4
		moveAmount = 2 + (int)(Math.random()*((4-2)+1));
		System.out.println(moveAmount);
		// true if pirate hits mainShip
		hitShip = false;
		// tracks mainShip move count
		count = 0;
	}
	
	public void addToPane(ObservableList<Node> sceneGraph){
		sceneGraph.add(pirateShipImageView);
	}
	
	@Override
	// each pirate ship moves based off the observable ship's current location 
	public void update(Observable o, Object arg) {
		if(o instanceof MainShip){
			//each type of pirate has their own movePirate method
			move((MainShip)o);
		}
	}
	
	public ImageView getImageView(){
		return this.pirateShipImageView;
	}
	
	public Point getPirateShipLocation(){
		return this.currentLocation;
	}
	
	public int getX(){
		return this.currentLocation.x;
	}
	
	public int getY(){
		return this.currentLocation.y;
	}
	
	public abstract void move(MainShip mainShip);
	
}
