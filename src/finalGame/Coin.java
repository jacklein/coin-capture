package finalGame;

import java.awt.Point;
import java.util.Random;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// MainShip collects Coin to win game
public class Coin{
	OceanMap oceanMap;
	int coords[][];
	int dimensions;
	
	Image coinImage;
	ImageView coinImageView;
	private Point currentLocation;
	int scale;
	Random rand = new Random();
	
	public Coin(int scale){
		// singleton coordinates
		oceanMap = OceanMap.getInstance();
		dimensions = oceanMap.getDimensions();
		coords = oceanMap.getMap();
		this.scale = scale;
		
		// initially assigns pirate ship random coordinate
		while(true){
			int x = rand.nextInt(dimensions);
			int y = rand.nextInt(dimensions);
			// initial point can't be an island, pirate, or mainShip
			if(coords[x][y] != 1 && coords[x][y] != 2){
				currentLocation = new Point(x,y);
				// coins are marked as '3' in 2d int array
				coords[x][y] = 3;
				break;
			}
		}
		
		// specific image for coin
		coinImage = new Image("file:images/doubloon.png",scale,scale,true,true); 
		coinImageView = new ImageView(coinImage);
		coinImageView.setX(currentLocation.x*scale);
		coinImageView.setY(currentLocation.y*scale);
	}
	
	public void addToPane(ObservableList<Node> sceneGraph){
		sceneGraph.add(coinImageView);
	}
	
	public ImageView getImage(){
		return this.coinImageView;
	}
	
	public Point getLocation(){
		return this.currentLocation;
	}
}
