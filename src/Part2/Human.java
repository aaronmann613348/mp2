package Part2;

import java.io.BufferedInputStream;
import java.util.Scanner;

public class Human extends Player {
	
	public Human(){
		super();
	}

	@Override
	public void takeTurn(Square[][] board, Game g) {
		
		Scanner stdin = new Scanner(new BufferedInputStream(System.in));
		
		System.out.println("Choose the row you want! (0-5 foolish human or press 6 for a random move)");
		Square humanSquare;
		int row = stdin.nextInt();
		if(row == 6){
			humanSquare = getUnoccupiedSquare(g.getBoard()).get(0);
		}
		else{
			System.out.println("Choose the column you want! (0-5 foolish human)");
			int col = stdin.nextInt();
			humanSquare = g.getBoard()[row][col];
		}
		
		humanSquare.setColor(this.color);
		doTheBlitz(board, humanSquare, this.color);
	}
		
}
