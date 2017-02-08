package Part2;

import java.util.ArrayList;
import java.util.List;

public class Minimax extends Player {

	public Minimax(){
		super();
	}

	public double evaluateSpot(Square[][] board, String color, Square s){
		double value = board[s.getX()][s.getY()].getValue();
		List<Square> listOfNeighbor = getNeighbors(board, s.getX(), s.getY(), color);

		boolean isBlitz = false;

		if(listOfNeighbor!=null)
			for(Square neibs: listOfNeighbor){
				if(neibs.getColor().equals(color)) isBlitz = true; //meaning this is a blitz move
			}
		if(isBlitz)
			for(Square neibs: listOfNeighbor){
				if(!neibs.getColor().equals(color)) value = value + neibs.getValue() + 0.1; //meaning this is an enemy
			}

		if(isBlitz)
			return value;
		else 
			return board[s.getX()][s.getY()].getValue();
	}


	public void takeTurn(Square[][] board, Game g) {
		//Temporary board used to see future steps. 
		Square[][] tempBoard = new Square[6][6];
		for(int i = 0 ; i < 6 ; i++){
			for(int j = 0 ; j < 6 ; j++){
				tempBoard[i][j] = new Square(i, j, board[i][j].getValue(), board[i][j].getColor());
			}
		}

		//Calculating the next best move using Minimax strategy with depth of 3
		double max = -99999999;
		int maxX = -1;
		int maxY = -1;
		for(Square spot : getUnoccupiedSquare(tempBoard))
		{
			tempBoard[spot.getX()][spot.getY()].setColor(this.color);
			List<Square> enemyBlitz = doTheBlitz(tempBoard, spot, this.color);
			double max2 = -99999;

			for(Square spot2 : getUnoccupiedSquare(tempBoard))
			{
				List<Square> enemyBlitz2 = null;
			
				if(this.getColor().equals("green")){
					tempBoard[spot2.getX()][spot2.getY()].setColor("blue");
					enemyBlitz2 = doTheBlitz(tempBoard, spot2, "blue");
					this.addNodes(1);
				}
				else {
					tempBoard[spot2.getX()][spot2.getY()].setColor("green");
					enemyBlitz2 = doTheBlitz(tempBoard, spot2, "green");
					this.addNodes(1);

				}

				double max3 = -1;

				for(Square spot3 : getUnoccupiedSquare(tempBoard))
				{
					double valueAtSpot = (double)1/3 * evaluateSpot(tempBoard, this.color, spot3);   //get evaluation for each leaf node
					if(valueAtSpot > max3)	//update max
					{
						max3 = valueAtSpot;
					}
				}
				if(max3 == -1){
					max2 = (double)1/2 * evaluateSpot(tempBoard, this.color, spot2);
				}
	
				if(max3 > max2) {
					if(this.getColor().equals("green"))
						max2 = max3 - (double)1/2 * evaluateSpot(tempBoard, "blue", spot2);
					else
						max2 = max3 - (double)1/2 * evaluateSpot(tempBoard, "green", spot2);
				}

				tempBoard[spot2.getX()][spot2.getY()].setColor("white");
				String enemy = new String("green");
				if(this.color.equals("green"))
					enemy = new String("blue");
				for(Square s : enemyBlitz2){
					tempBoard[s.getX()][s.getY()].setColor(enemy);
				}

			}
		
			if(max2 == -99999)
			{
				maxX = spot.getX();
				maxY = spot.getY();
				break;
			}

			if(max2 > max)
			{
				max = max2 + evaluateSpot(tempBoard, this.color, spot);
				maxX = spot.getX();
				maxY = spot.getY();
			}

			tempBoard[spot.getX()][spot.getY()].setColor("white");
			String enemy = new String("green");
			if(this.color.equals("green"))
				enemy = new String("blue");
			for(Square s : enemyBlitz){
				tempBoard[s.getX()][s.getY()].setColor(enemy);
			}
		}
		
		
		//Make the real move here!
		System.out.print(this.color+"'s turn");
		boolean isBlitz = false;
		boolean doesBlitz = false;
		List<Square> listOfNeighbor = getNeighbors(board, maxX, maxY, this.color);

		if(listOfNeighbor!=null)
		{
			for(Square neibs: listOfNeighbor)
			{
				if(neibs.getColor().equals(this.color)) isBlitz = true; //meaning this is a blitz move
			}
			if(isBlitz) 
			{
				for(Square neibs: listOfNeighbor)
				{
					if(!neibs.getColor().equals(this.color)){
						board[neibs.getX()][neibs.getY()].setColor(this.color); //meaning this is an enemy
						doesBlitz = true;
					}
				}
			}

		}
		if(doesBlitz)
			System.out.println(" with a blitz.");
		else
			System.out.println(" without a blitz.");

		board[maxX][maxY].setColor(this.color);
	}

	
	public void printBoard(Square[][] board){
		for( int i = 0 ; i < 6 ; i++){
			for (int j = 0 ; j < 6 ; j++){
				if(board[i][j].getColor().equals("white"))
					System.out.print("      : "+board[i][j].getValue()+"  ");
				else 
					System.out.print("  "+board[i][j].getColor()+" : "+board[i][j].getValue()+"  ");
			}
			System.out.print("\n");
		}
		System.out.println();
	}
}
