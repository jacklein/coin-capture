package finalGame;


import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// Whirlpool decorator sends mainShip back to its starting location
public class Whirlpool extends ShipDecorator{
	private Ship ship;
	private Image poolImage;
	private ImageView poolImageView;

	public Whirlpool(int scale, Ship ship){
		super();
		this.ship = ship;
		this.scale = scale;
		removeFromMap = false;
	
		// specific image for whirlpool
		poolImage = new Image("file:images/whirlpool.png",scale,scale,true,true); 
		poolImageView = new ImageView(poolImage);
		poolImageView.setX(currentLocation.x*scale);
		poolImageView.setY(currentLocation.y*scale);
	}
	
	@Override
	public void powerup() {
		MainShip mainShip;
		if(ship instanceof MainShip){
			mainShip = ((MainShip) ship);
			// set mainShips current location to its starting location
			mainShip.setCurrentLocation(mainShip.getStartingLocation());
			mainShip.getImageView().setX(mainShip.getX()*scale);
			mainShip.getImageView().setY(mainShip.getY()*scale);
		}
		ship.powerup();
	}

	@Override
	public void addToPane(ObservableList<Node> sceneGraph) {
		sceneGraph.add(poolImageView);		
	}

	@Override
	public ImageView getImageView() {
		return poolImageView;
	}
	
	// removeFromMap = false
	public boolean removeFromMap(){
		return removeFromMap;
	}

}
