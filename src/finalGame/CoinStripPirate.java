package finalGame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// CoinStripPirate strips a coin from mainPirate if hit

public class CoinStripPirate extends PirateShip{
	
	public CoinStripPirate(int scale) {
		super(scale);
		
		// specific image for CoinStripPirate
		pirateShipImage = new Image("file:images/coinStripPirate.png",scale,scale,true,true); 
		pirateShipImageView = new ImageView(pirateShipImage);
		pirateShipImageView.setX(currentLocation.x * scale);
		pirateShipImageView.setY(currentLocation.y * scale);
	}

	@Override
	public void move(MainShip mainShip) {

		// prevents MainShip from going on top of pirate
		coords[this.getX()][this.getY()] = 1;
		
		// moves every 2nd MainShip move
		if (mainShip.getMoveCount() % moveAmount == 0){
			// resets old spot to 0
			coords[this.getX()][this.getY()] = 0;
			
			// x movement
			if(this.getX() - mainShip.getX()  == 0){}
			else if(this.getX() - mainShip.getX() < 0){
				// checks for boundaries, islands, and coins (can't go through any)
				if(this.getX()<dimensions-1 && oceanMap.pointNotEquals(this.getX()+1,this.getY(), offLimits))
					currentLocation.x++;
			}
			else if(this.getX()>0 && oceanMap.pointNotEquals(this.getX()-1,this.getY(), offLimits))
				currentLocation.x--;
			
			// y movement
			if(this.getY() - mainShip.getY()  == 0){}
			else if(this.getY() - mainShip.getY() < 0){
				// checks for boundaries, islands, and coins (can't go through any)
				if(this.getY()<dimensions-1 && oceanMap.pointNotEquals(this.getX(),this.getY()+1, offLimits))
					currentLocation.y++;
			}
			else if(this.getY()>0 && oceanMap.pointNotEquals(this.getX(),this.getY()-1, offLimits))
				currentLocation.y--;
			
			// movement is over, now to check status of grid
			
			// if pirate moves onto mainShip, hitShip = true
			if(coords[this.getX()][this.getY()] == 2){
				hitShip = true;
			}
			
			// keeping track of where coinStripPirate is on grid
			coords[this.getX()][this.getY()] = 1;
			
			// update image
			pirateShipImageView.setX(this.getX()*scale);
			pirateShipImageView.setY(this.getY()*scale);
			
			// updates mainShip info after pirate ship moves
			if(hitShip && mainShip.getCoinCount() > 0){
				System.out.println("Hello");
				mainShip.setCoinCount(-1);
				mainShip.updateCoinCounter();
				// adds another coin to map when it takes one from mainShip
				mainShip.addCoinToMap();
			}
			// reset
			hitShip = false;
		}
	}

}
