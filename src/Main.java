
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import Part2.Game;
import Part2.Human;
import Part2.Minimax;
import Part2.RunGame;


public class Main {

	public static void main(String[] args) throws FileNotFoundException {

		Scanner stdin = new Scanner(new BufferedInputStream(System.in));
		
		System.out.println("meow. What part of the MP would you like to solve? \n"
				+"1) 1.1 \n"
				+"2) 2.1");
		
		int choice = stdin.nextInt();
		
		switch(choice){
		case 1:
			runPartOne();
			break;
		case 2:
			runPartTwo();
			break;
		default:
			System.out.println("Invalid choice. Try again!\n");
			break;
		}

	}

	private static void runPartOne(){
		Scanner stdin = new Scanner(new BufferedInputStream(System.in));
		System.out.println("Good choice... What puzzle would you like to solve? \n"
				+"1) one \n"
				+"2) two \n"
				+"3) three \n"
				+"4) four \n"
				+"5) five \n");
		
		
		int choice = stdin.nextInt();
		if(choice < 1 || choice > 5) {
			System.out.println("Invalid choice. Try again!\n"); return;
		}
		
		
		Puzzle puzzle = Parser.parsePuzzle(new File("data/puzzle"+choice+".txt"));
		
		List<Entry<String, Integer>> cats = puzzle.getcategoryRank();
		for(Entry<String, Integer> cat: cats){	
			System.out.println(cat.getKey()  + " " + cat.getValue());
		}

		System.out.println("What search would you like to do? \n"
				+"1) letter-based assignment \n"
				+"2) word-based assignment \n");

		
		while(true){
			choice = stdin.nextInt();
			if(choice == 1){
				puzzle.solveLetterBased();
				break;
			} else if (choice == 2){
				BacktrackSearch.solveWordBased(1,puzzle, puzzle.getCategoryLocs().size());
				break;
			} else {
				System.out.println("Invalid choice. Try again!\n");
			}
		}
		
		
	}
	
	private static void runPartTwo() throws FileNotFoundException {
		Scanner stdin = new Scanner(new BufferedInputStream(System.in));
		
		System.out.println("Do you want to play with the interface? \n"+
				" 1: Yes\n"+
				" 2: No");
		
		int interfaceChoice = stdin.nextInt();
		
		System.out.println("Good choice... What map would you like to play on? \n"
				+"1) Keren \n"
				+"2) Narvik \n"
				+"3) Sevastopol \n"
				+"4) Smolensk \n"
				+"5) Westerplatte \n");
		
		int choice = stdin.nextInt();
		System.out.println("Choose a mode \n"+
				" 1: Minimax vs. minimax\n"+
				" 2: Alpha-beta vs. alpha-beta\n"+
				" 3: Minimax vs. alpha-beta (minimax goes first)\n"+
				" 4: Alpha-beta vs. minimax (alpha-beta goes first)\n"+
				" 5: Human vs minimax\n");
		
		int mode = stdin.nextInt();
		
		Game g = null;
		switch(mode){
		case 1:
			g = new Game(new Minimax(), new Minimax(), choice);
			RunGame.main(null);
			break;
		case 2:
		//	g = new Game(new Alphabeta(), new Alphabeta(), input);
			break;
		case 3:
		//	g = new Game(new Minimax(), new Alphabeta(), input);
			break;	
		case 4:
		//	g = new Game(new Alphabeta(), new Minimax(), input);
			break;
		case 5:
			g = new Game(new Human(), new Minimax(), choice);
		default:
			break;
		}
		if(interfaceChoice != 1)
			g.playGame();
	
	}



}
