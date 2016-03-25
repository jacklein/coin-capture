package finalGame;

import java.awt.Point;
import java.util.Random;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// Node class for composite pattern
class MonsterSprite implements MonsterComponent{
	OceanMap oceanMap;
	int coords[][];
	int dimensions;
	
	boolean hitShip;
	MainShip mainShip;
	Point currentLocation;
	Image monsterImage;
	ImageView monsterImageView;
	int scale;
	Random random = new Random();
	// list of map values the pirates can't go over
	int[] offLimits = {1,3,4};
	
	MonsterSprite(int x, int y, int scale, MainShip mainShip){
		//singleton coordinates
		oceanMap = OceanMap.getInstance();
		dimensions = oceanMap.getDimensions();
		coords = oceanMap.getMap();
		
		currentLocation = new Point(x,y);
		this.scale = scale;
		// monsters are marked as '1' in 2d int array
		coords[x][y] = 1;
		
		monsterImage = new Image("file:images/monster.png",scale,scale,true,true); 
		monsterImageView = new ImageView(monsterImage);
		monsterImageView.setX(currentLocation.x * scale);
		monsterImageView.setY(currentLocation.y * scale);
		
		// able to inflict damage on mainShip
		this.mainShip = mainShip;
		hitShip = false;
	}
	
	ImageView getImage(){
		return monsterImageView;
	}
	
	void setX(int x){
		currentLocation.x = x;
	}
	
	void setY(int y){
		currentLocation.y = y;
	}
	
	void setPosition(int x, int y){
		setPositionX(x);
		setPositionY(y);
	}
	
	int getX(){
		return currentLocation.x;
	}
	
	int getY(){
		return currentLocation.y;
	}
	
	public void setPositionX(int x){
		monsterImageView.setX(currentLocation.x * scale);
	}
	
	public void setPositionY(int y){
		monsterImageView.setY(currentLocation.y * scale);
	}

	@Override
	// monster sprite movement
	public void run() {
		// resets old spot to 0
		coords[this.getX()][this.getY()] = 0;
		
		// set X
		int xMove = this.getX() + random.nextInt(3)-1;
		if (xMove >=0 && xMove <= dimensions-1 && oceanMap.pointNotEquals(xMove, this.getY(), offLimits))
			this.setX(xMove);
		
		// set Y
		int yMove = this.getY() + random.nextInt(3)-1;
		if (yMove >=0 && yMove <= dimensions-1  && oceanMap.pointNotEquals(this.getX(), yMove, offLimits))
			this.setY(yMove);
		
		// if monster sprite moves onto mainShip, inflict 1 damage
		if(coords[this.getX()][this.getY()] == 2){
			hitShip = true;
		}
		
		// move sprite
		coords[this.getX()][this.getY()] = 1;
		this.setPosition(this.getX(), this.getY());
		
		// updates mainShip info after monster sprite moves
		if(hitShip){
			// have to nest the funcs in Runnable in order to work with threads
			Platform.runLater(new Runnable(){
				@Override
				public void run(){
					mainShip.setHealth(-1);
					mainShip.updateHealthBar();
					hitShip = false;
				}
			});
		}
		
	}
}