package finalGame;

import java.awt.Point;
import java.util.Optional;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

// Concrete class that extends the Ship component for the decorator pattern
public class MainShip extends Ship{
	OceanExplorer oceanExplorer;
	Stage oceanStage;
	Image shipImage;
	ImageView shipImageView;
	
	private Image coinImage;
	private ImageView coinImageView;
	private Label coinCountLabel;
	
	private Rectangle healthBar;
	private Label healthBarLabel;
	
	private boolean removeFromMap;
	private Point startingLocation;
	private int coinCount;
	private int moveCount;
	private int health;
	
	public MainShip(int scale, Stage oceanStage, OceanExplorer oceanExplorer){
		// singleton coordinates
		oceanMap = OceanMap.getInstance();
		dimensions = oceanMap.getDimensions();
		coords = oceanMap.getMap();
		
		// need oceanExplorer and oceanStage for restarting the game
		this.oceanStage = oceanStage;
		this.oceanExplorer = oceanExplorer;
		this.scale = scale;

		// initially assigns ship random point
		while(true){
			int x = rand.nextInt(dimensions);
			int y = rand.nextInt(dimensions);
			// initial point can't be an island
			if(coords[x][y] != 1){
				currentLocation = new Point(x,y);
				coords[x][y] = 2;
				break;
			}
		}
		// for the whirlpools
		startingLocation = new Point(currentLocation.x, currentLocation.y);
		removeFromMap = true;
		
		shipImage = new Image("file:images/ship.png",scale,scale,true,true); 
		shipImageView = new ImageView(shipImage);
		shipImageView.setX(currentLocation.x * scale);
		shipImageView.setY(currentLocation.y * scale);
		
		// main ship health, starts at 10, dies at 0
		health = 10;
		// amt of coins, needs 10 to win
		coinCount = 0;
		// counts the moves for its observers
		moveCount = 0;
		
	}
	
	// setX and setY are the coords for healthBar position
	// only called when mainShip is instantiated
	public void setHealthBar(int setX, int setY, ObservableList<Node> sceneGraph){
		healthBar = new Rectangle();
		healthBar.setWidth(this.getHealth()*10);
		healthBar.setHeight(20);
		healthBar.setFill(Color.RED);
		healthBar.setLayoutX(setX);
		healthBar.setLayoutY(setY);
		sceneGraph.add(healthBar);
		
		healthBarLabel = new Label("10/10 Health");
		healthBarLabel.setLayoutX(setX);
		healthBarLabel.setLayoutY(setY-22);
		sceneGraph.add(healthBarLabel);
	}
	
	// setX and setY are the coords for coin counter position
	// only called when mainShip is instantiated
	public void setCoinCounter(int setX, int setY, ObservableList<Node> sceneGraph){
		// specific image for coin
		coinImage = new Image("file:images/doubloon.png",scale,scale,true,true); 
		coinImageView = new ImageView(coinImage);
		coinImageView.setX(setX);
		coinImageView.setY(setY);
		sceneGraph.add(coinImageView);
		
		coinCountLabel = new Label("X " + coinCount);
		coinCountLabel.setLayoutX(setX + 30);
		coinCountLabel.setLayoutY(setY + 5);
		sceneGraph.add(coinCountLabel);
	}
	
	// updates health bar each time mainShip's health changes
	public void updateHealthBar(){
		healthBar.setWidth(health*10);
		healthBarLabel.setText(health + "/10 Health");
		
		// alert for game over due to zero health
		if(health <= 0){
			gameOver("Game Over" ,"Your health ran out.");
		}
	}
	
	// updates the coin counter each time mainShip gains or loses a coin
	public void updateCoinCounter(){
		coinCountLabel.setText("X " + coinCount);
		
		// alert for winning game due to 10 coins
		if(coinCount == 10){
			gameOver("You Won!", "You collected all 10 coins!");
		}
	}
	
	// ends the game for a specific reason
	public void gameOver(String result, String reason){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(result);
		alert.setHeaderText(null);
		// specific reason for ending game
		alert.setContentText(reason);
		
		ButtonType restartButton = new ButtonType("Restart");
		ButtonType exitButton = new ButtonType("Exit");
		
		alert.getButtonTypes().setAll(restartButton, exitButton);
		
		Optional<ButtonType> action = alert.showAndWait();
		// if restart pressed ==> play game again
		if(action.get() == restartButton){
			try {
				oceanExplorer.start(oceanStage);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// if exit pressed ==> close app
		else if(action.get() == exitButton)
			Platform.exit();
	}

	public void move(String s){
		moveCount++;
		coords[this.getX()][this.getY()] = 0;
		if(s.equals("EAST")){
			if(this.getX()<dimensions-1 && coords[this.getX()+1][this.getY()]!=1)
				currentLocation.x++;
		}
		else if(s.equals("WEST")){
			if(this.getX()>0 && coords[this.getX()-1][this.getY()]!=1 )
				currentLocation.x--;
		}
		else if(s.equals("NORTH")){
			if(this.getY()>0 && coords[this.getX()][this.getY()-1]!=1)
				currentLocation.y--;
		}
		else{
			if(this.getY()<dimensions-1 && coords[this.getX()][this.getY()+1]!=1)
				currentLocation.y++;
		}
		
		// picked up coin, removes it from map
		if(coords[this.getX()][this.getY()] == 3){
			coinCount++;
			// updates coin counter
			updateCoinCounter();
			// removes coin image from root
			oceanExplorer.removeCoin(currentLocation);
		}
		// picked up power up
		else if(coords[this.getX()][this.getY()] == 4){
			removeFromMap = oceanExplorer.useDecorator(this);
		}
		
		// keeping track of where MainShip is on grid
		// don't change coordinate if decorator doesn't leave map after used
		coords[this.getX()][this.getY()] = removeFromMap? 2: 4;
		removeFromMap = true;
		
		
		// updates image location
		shipImageView.setX(this.getX()*scale);
		shipImageView.setY(this.getY()*scale);
		
		// notify the observers (pirate ships, monsters)
		setChanged();
		notifyObservers();
		
		System.out.println("Health: " + this.health);
		System.out.println("Coins: " + this.coinCount);
	}	

	public void addToPane(ObservableList<Node> sceneGraph){
		sceneGraph.add(shipImageView);
	}
	
	public ImageView getImageView(){
		return this.shipImageView;
	}
	
	public void setCurrentLocation(Point location){
		this.currentLocation.setLocation(location.x, location.y);
	}
	
	public Point getStartingLocation(){
		return this.startingLocation;
	}
	
	public int getX(){
		return this.currentLocation.x;
	}
	
	public int getY(){
		return this.currentLocation.y;
	}
	
	public MainShip getShip(){
		return this;
	}
	
	public int getHealth(){
		return this.health;
	}
	
	public void setHealth(int amount){
		this.health += amount;
		// health can't go above 10
		if(health > 10)
			health = 10;
	}
	
	public int getCoinCount(){
		return coinCount;
	}
	
	public void setCoinCount(int amount){
		coinCount += amount;
	}
	
	// calls oceanExplorer's method to add another coin to the map
	// called when coinStripPirate takes a coin
	public void addCoinToMap(){
		oceanExplorer.addCoinToMap();
	}

	public int getMoveCount(){
		return this.moveCount;
	}

	@Override
	public void powerup() {
		// TODO Auto-generated method stub
		
	}
}
