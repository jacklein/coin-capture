package finalGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OceanExplorer extends Application {
	final int dimensions = 25;

	
	final int scale = 25;
	Scene scene;
	Pane root;
	OceanMap oceanMap;
	MainShip mainShip;
	ArrayList<Thread> monsterArray;
	HashMap<Point, Coin> coinMap = new HashMap<Point, Coin>();
	HashMap<Point, ShipDecorator> decoratorMap = new HashMap<Point, ShipDecorator>();
	
	@Override
	public void start(Stage oceanStage) throws Exception {
		// first call to singleton oceanMap sets the dimensions for its 2d array
		oceanMap = OceanMap.getInstance();
		oceanMap.setMap(dimensions);
		root = new AnchorPane();
		
		drawMap();
		generateIslands(40);
		
		// creates all the players, oceanStage parameter is for mainShip
		createPlayers(oceanStage);

		// setting the scene
		scene = new Scene(root, 625, 700);
		oceanStage.setTitle("Coin Capture!");
		oceanStage.setScene(scene);
		oceanStage.show();
		
		// shows rules dialog 
		showRules(oceanStage);
		
		startSailing();
		
	}

	// draws grid
	public void drawMap(){
		for(int x = 0; x < dimensions; x++){
			for(int y = 0; y < dimensions; y++){
			Rectangle rect = new Rectangle(x*scale,y*scale,scale,scale);
			//rect.setStroke(Color.BLACK); 
			rect.setFill(Color.PALETURQUOISE); 
			root.getChildren().add(rect); 
			}
		}
	}
	
	// generates islands
	public void generateIslands(int i){
		int count = 0;
		Random rand = new Random();
		while(count<=i){
			int x;
			int y;
			// makes sure all islands are in diff spots
			while(true){
				x = rand.nextInt(dimensions);
				y = rand.nextInt(dimensions);
				if(oceanMap.getMap()[x][y]!=1)
					break;
			}
			Image islandImage = new Image("file:images/island.png",scale,scale,true,true); 
			ImageView islandImageView = new ImageView(islandImage);
			islandImageView.setX(x*scale);
			islandImageView.setY(y*scale);
			// islands are marked as '1' in 2d int array
			oceanMap.getMap()[x][y] = 1;
			root.getChildren().add(islandImageView);
			count++;
		}
	}
	
	// creates an alert to show rules for the game
	public void showRules(Stage oceanStage) throws IOException {
		if(oceanStage.isShowing()){
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Coin Capture!");
			alert.setHeaderText(null);
			String rules = createRules();
			alert.setContentText(rules);
			alert.showAndWait();
		}
		// monsters start moving after rules are dismissed
		for(Thread thread : monsterArray){
			thread.start();
		}
	}
	
	// reads rules in from a txt file
	public String createRules() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("src/rules.txt"));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		       // sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    String everything = sb.toString();
		    return everything;
		} finally {
		    br.close();
		}
	}
	
	// removes coin from map when mainShip picks it up, method called from mainShip
	public void removeCoin(Point mainShipLocation){
		// remove coin image from scene
		root.getChildren().remove(coinMap.get(mainShipLocation).getImage());
		// remove KV pair from map
		coinMap.remove(mainShipLocation);
	}
	
	// adds another coin to map after coinStripPirate takes a coin
	public void addCoinToMap(){
		Coin coin = new Coin(scale);
		coin.addToPane(root.getChildren());
		coinMap.put(coin.getLocation(), coin);
	}
	
	// adds decorator's powerup to mainShip
	public boolean useDecorator(Ship mainShip){
		ShipDecorator decorator = decoratorMap.get(mainShip.getLocation());
		decorator.powerup();
		// if decorator is supposed to be removed from map
		if(decorator.removeFromMap){
			// removes decorator from scene
			root.getChildren().remove(decorator.getImageView());
			// remove KV pair from map
			decoratorMap.remove(mainShip.getLocation());
		}
		return decorator.removeFromMap();
	}
	
	// responds to key events
	public void startSailing(){
		scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
	public void handle(KeyEvent ke) {
		//monstersThread.resume();
		switch(ke.getCode()){
			case RIGHT:
			mainShip.move("EAST");
			break;
			case LEFT:
			mainShip.move("WEST");
			break;
			case UP:
			mainShip.move("NORTH");
			break;
			case DOWN:
			mainShip.move("SOUTH");
			break;
			default:
			break;
		}
		//if(mainShip.getCount()%10==0)
			//monstersThread.suspend();
		}});
		 }
	
	// instantiates all the different players
	public void createPlayers(Stage oceanStage){
		
		// MAIN SHIP
		mainShip = new MainShip(scale, oceanStage, this);
		mainShip.addToPane(root.getChildren());
		mainShip.setHealthBar(10, 670, root.getChildren());
		mainShip.setCoinCounter(200, 645, root.getChildren());
		
		// PIRATE SHIPS
		
		// damage pirate, creates 3
		for(int i=0; i<=2; i++){
			PirateShip damagePirate = new DamagePirate(scale);
			mainShip.addObserver(damagePirate);
			damagePirate.addToPane(root.getChildren());
		}
		
		// coin strip pirate, creates 2
		for(int i=0; i<=1; i++){
			PirateShip coinStripPirate = new CoinStripPirate(scale);
			mainShip.addObserver(coinStripPirate);
			coinStripPirate.addToPane(root.getChildren());
		}
		
		// COINS
		for(int i=0; i<=9; i++){ //10 coins
			Coin coin = new Coin(scale);
			coin.addToPane(root.getChildren());
			coinMap.put(coin.getLocation(), coin);
		}
		
		// DECORATORS
		
		// health, creates 6
		for(int i=0; i<=5; i++){
			ShipDecorator health = new Health(scale, mainShip);
			health.addToPane(root.getChildren());
			decoratorMap.put(health.getLocation(), health);
		}
		
		// whirlpools, creates 10
		for(int i=0; i<=9; i++){
			ShipDecorator whirlPool = new Whirlpool(scale, mainShip);
			whirlPool.addToPane(root.getChildren());
			decoratorMap.put(whirlPool.getLocation(), whirlPool);
		}	
		
		// MONSTER THREADS	
		monsterArray = new ArrayList<Thread>();
		
		MonsterPool monsterPool = new MonsterPool(scale, 700, 10, mainShip);
		monsterPool.addToPane(root.getChildren());
		Thread monstersThread1 = new Thread(monsterPool);
		monsterArray.add(monstersThread1);
		
		MonsterPool monsterPool2 = new MonsterPool(scale, 500, 10, mainShip);
		monsterPool2.addToPane(root.getChildren());
		Thread monstersThread2 = new Thread(monsterPool2);
		monsterArray.add(monstersThread2);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
