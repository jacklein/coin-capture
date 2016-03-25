package finalGame;

import java.util.Random;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.ImageView;

// Composite class for composite pattern 
public class MonsterPool implements MonsterComponent, Runnable {
	OceanMap oceanMap;
	int[][] coords;
	int dimensions;
	
	Boolean running = true;
	int radius;
	int scale;
	int speed;
	Random random = new Random();
	MonsterComponent[] monsterSprites;
	
	public MonsterPool(int scale, int speed, int amount, MainShip mainShip){
		// singleton coordinates
		oceanMap = OceanMap.getInstance();
		dimensions = oceanMap.getDimensions();
		coords = oceanMap.getMap();
		
		monsterSprites = new MonsterComponent[amount];
		for(int j = 0; j < amount; j++){
			while(true){
				int x = random.nextInt(dimensions);
				int y = random.nextInt(dimensions);	
				// initial point can't be an island, coin, or decorator
				if(coords[x][y] != 1 && coords[x][y] != 3 && coords[x][y] != 4){
					// creates new sprite
					monsterSprites[j] = new MonsterSprite(x,y,scale, mainShip);
					break;
				}
			}
		}
		this.radius = 10;
		this.scale = scale;
		this.speed = speed;
	}

	public void addToPane(ObservableList<Node> sceneGraph){
		for(MonsterComponent monsterSprite: monsterSprites){
			if(monsterSprite instanceof MonsterSprite){
				ImageView monsterImageView = ((MonsterSprite) monsterSprite).getImage();
				//System.out.println("Adding circle to pane: " + circle.getCenterX() + " " + circle.getCenterY() + " " + radius);
				sceneGraph.add(monsterImageView);
			}
			else{
				((MonsterPool) monsterSprite).addToPane(sceneGraph);
			}
		}
	}

	// for every sprite in the monsterSprites array, sprites.run()
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// COMPOSITE
			for(MonsterComponent monsterSprite: monsterSprites){	
				monsterSprite.run();
			}
		}

	}	
}



