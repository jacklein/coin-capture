package finalGame;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// Health decorator gives +2 health to mainShip
public class Health extends ShipDecorator{
	private Ship ship;
	private Image healthImage;
	private ImageView healthImageView;

	public Health(int scale, Ship ship){
		super();
		this.ship = ship;
		this.scale = scale;
		removeFromMap = true;
	
		// specific image for health
		healthImage = new Image("file:images/health.png",scale,scale,true,true); 
		healthImageView = new ImageView(healthImage);
		healthImageView.setX(currentLocation.x*scale);
		healthImageView.setY(currentLocation.y*scale);
	}
	
	@Override
	// gives 2 health to ship
	public void powerup() {
		if(ship instanceof MainShip){
			((MainShip) ship).setHealth(5);
			((MainShip) ship).updateHealthBar();
		}
		ship.powerup();
	}

	@Override
	public void addToPane(ObservableList<Node> sceneGraph) {
		sceneGraph.add(healthImageView);
	}

	@Override
	public ImageView getImageView() {
		// TODO Auto-generated method stub
		return healthImageView;
	}
	
	// whether or not the decorator is removed from game after use
	public boolean removeFromMap(){
		return removeFromMap;
	}
	

}
