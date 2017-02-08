package Part2;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
	
	private int score;
	
	public int nodes;
	
	public int getNodes() {
		return nodes;
	}

	public void setNodes(int nodes) {
		this.nodes = nodes;
	}
	
	public void addNodes(int value){
		this.nodes = this.nodes + value;
	}

	protected String color;
	
	public Player(){
		
	}

	public abstract void takeTurn(Square[][] board, Game g);
	
	public int evaluateBoard(Game g, String color){

		Square[][] board = g.getBoard();

		int value = 0;

		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 6; j++){
				if(board[i][j].getColor().equals(color))
					value = value + board[i][j].getValue();
			}
		}

		return value;	
	}
	
	public List<Square> getUnoccupiedSquare(Square[][] board){
		
		List<Square> list = new ArrayList<Square>();
		for(int i=0;i<6;i++)
			for(int j=0;j<6;j++)
				if(board[i][j].getColor().equals("white"))
					list.add(board[i][j]);
		
		return list;
	}
	
	public List<Square> getNeighbors(Square[][] board, int x, int y, String color){
		List<Square> neighbs = new ArrayList<Square>();

		//check up
		if(y >= 0 && (x-1) >= 0 && !board[x-1][y].getColor().equals("white"))
			neighbs.add(board[x-1][y]);

		//check down
		if(y < 6 && (x+1) < 6 && !board[x+1][y].getColor().equals("white"))
			neighbs.add(board[x+1][y]);

		//check left
		if(x >= 0 && (y-1) >= 0 && !board[x][y-1].getColor().equals("white"))
			neighbs.add(board[x][y-1]);

		//check right
		if(x <6 && (y+1) < 6 && !board[x][y+1].getColor().equals("white"))
			neighbs.add(board[x][y+1]);

		if(neighbs.size() == 0)
			return null;
		else
			return neighbs;
	}
	
	public List<Square> doTheBlitz(Square[][] tmp, Square spot, String color){
		boolean isBlitz = false;
		List<Square> listOfNeighbor = getNeighbors(tmp, spot.getX(), spot.getY(), color);
		List<Square> blitzNeighbs = new ArrayList<Square>();

		if(listOfNeighbor != null){
			for(Square neibs: listOfNeighbor)
			{
				if(neibs.getColor().equals(color)) 
					isBlitz = true; //meaning this is a blitz move
			}
			if(isBlitz) 
			{
				for(Square neibs: listOfNeighbor)
				{
					if(!neibs.getColor().equals(color)){
						tmp[neibs.getX()][neibs.getY()].setColor(color); //meaning this is an enemy
						blitzNeighbs.add(neibs);
					}
				}
			}
		}
		return blitzNeighbs;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	
}
