package Part2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Game {

	private int q = 0;
	private double m = 0;
	private Node root; //this is root for the whole game tree when each player takes turn. It's cleared when player finishes their current turn.
	private Square[][] board;
	private long StartTime;
	private long EndTime;
	private long TotalTime;
	private Player p1;
	private Player p2;
	private int spacesLeft;
    private boolean isBluesTurn;



	public Game(Player p1, Player p2, int input){
		this.isBluesTurn = true;
		this.p1 = p1;
		this.p2 = p2;
		p1.setColor("blue");
		p2.setColor("green");
		this.spacesLeft = 36;
		Square[][] board = new Square[6][6];
		
		String choice = new String();
		switch(input){
		case 1:
			choice = "Keren";
			break;
		case 2:
			choice = "Narvik";
			break;
		case 3:
			choice = "Sevastopol";
			break;	
		case 4:
			choice = "Smolensk";
			break;
		case 5:
			choice = "Westerplatte";
			break;
		default:
			break;
		}
		
		File f = new File("data/" + choice + ".txt");
		Scanner scanner = null;
		try {
			scanner = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for( int i = 0 ; i < 6 ; i++){
			for (int j = 0 ; j < 6 ; j++){
				int value = new Integer(scanner.nextInt());
				board[i][j] = new Square(i,j,value);
			}
		}
		
		this.board = board;
		printBoard();
	}
	
	
	public void playGame(){
		while(spacesLeft > 0){
			StartTime = System.currentTimeMillis();
			q = q + 1;
			p1.takeTurn(this.board, this);
			EndTime = System.currentTimeMillis();
			spacesLeft--;
			printBoard();
			
			StartTime = System.currentTimeMillis();

			q = q + 1;
			p2.takeTurn(this.board, this);
			EndTime = System.currentTimeMillis();

			spacesLeft--;
			printBoard();
			

			
			
		}
		
		System.out.println(p1.getColor()+"'s score is " +p1.evaluateBoard(this, p1.getColor()));
		System.out.println(p2.getColor()+"'s score is " +p2.evaluateBoard(this, p2.getColor()));
	}
	
	public boolean takeTurn(Player p){
		
		if (this.spacesLeft == 0)
			return false;
		
		p.takeTurn(this.board, this);
		this.spacesLeft--;
//		try {
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return true;
	}
	
	public List<Square> getNeighbors(int x, int y, String color){
		List<Square> neighbs = new ArrayList<Square>();
		
		//check up
		if((x-1) >= 0 && !board[x-1][y].getColor().equals("white"))
			neighbs.add(board[x-1][y]);
		
		//check down
		if((x+1) < 6 && !board[x+1][y].getColor().equals("white"))
			neighbs.add(board[x+1][y]);
		
		//check left
		if((y-1) >= 0 && !board[x][y-1].getColor().equals("white"))
			neighbs.add(board[x][y-1]);
		
		//check right
		if((y+1) < 6 && !board[x][y+1].getColor().equals("white"))
			neighbs.add(board[x][y+1]);
		

		return neighbs;
	}
	
	public void printBoard(){
		for( int i = 0 ; i < 6 ; i++){
			for (int j = 0 ; j < 6 ; j++){
				if(this.board[i][j].getColor().equals("white"))
					System.out.print("    : "+this.board[i][j].getValue()+"  ");
				else 
					System.out.print("  "+this.board[i][j].getColor().charAt(0)+" : "+this.board[i][j].getValue()+"  ");
			}
			System.out.print("\n");
		}
		System.out.println();
		System.out.println("blue: " + p1.getNodes());
		System.out.println("green: " + p2.getNodes());
		System.out.println("Number of Turns: " + q);
		System.out.println("Length of Time of Turn: " + (EndTime - StartTime));
		TotalTime = TotalTime + (EndTime - StartTime);
		System.out.println("Total Time so Far: " + TotalTime);
		if(q!=0)
		System.out.println("Average Time: " + (TotalTime/q));
		
		
		m = p1.getNodes() + p2.getNodes();
		m = m/q;
		System.out.println("Average Nodes Expanded Per Turn: " + m);
	}

	public Square[][] getBoard() {
		return board;
	}

	public void setBoard(Square[][] board) {
		this.board = board;
	}
	
	
	public boolean isBluesTurn() {
		return isBluesTurn;
	}


	public void setBluesTurn(boolean isBluesTurn) {
		this.isBluesTurn = isBluesTurn;
	}
	
	public Player getP1() {
		return p1;
	}


	public void setP1(Player p1) {
		this.p1 = p1;
	}


	public Player getP2() {
		return p2;
	}


	public void setP2(Player p2) {
		this.p2 = p2;
	}


	
}
