package finalGame;

// singleton map
public class OceanMap {
	private static OceanMap oceanMap;
	private static int[][] map;
	private static int dimensions;
	
	private OceanMap(){}
	
	public synchronized static OceanMap getInstance(){
		if(oceanMap == null){
			oceanMap = new OceanMap();
		}
		return oceanMap;
	}
	
	public int[][] getMap(){
		return OceanMap.map;
	}
	
	public int getDimensions(){
		return OceanMap.dimensions;
	}
	
	public void setMap(int dimensions){
		OceanMap.dimensions = dimensions;
		OceanMap.map = new int[dimensions][dimensions];
	}
	
	// checks the value of a map coordinate
	public boolean pointNotEquals(int x, int y, int[] values){
		for(int i=0; i<values.length; i++){
			if(map[x][y] == values[i]){
				return false;
			}
		}
		return true;
	}
}
