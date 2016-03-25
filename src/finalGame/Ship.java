package finalGame;

import java.awt.Point;
import java.util.Observable;
import java.util.Random;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

// Component class for Decorator design pattern
// Has to extend Observable for MainShip
public abstract class Ship extends Observable{
	OceanMap oceanMap;
	int coords[][];
	int dimensions;
	int scale;
	Random rand = new Random();
	public Point currentLocation;
	
	public Point getLocation(){
		return this.currentLocation;
	}
	
	// methods that all classes that extend Ship use
	public abstract void powerup();
	public abstract void addToPane(ObservableList<Node> sceneGraph);
	public abstract ImageView getImageView();
}
