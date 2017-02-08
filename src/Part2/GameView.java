package Part2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class GameView extends JFrame{

	GridLayout boardLayout = new GridLayout(6, 6);
	Game game;
	Square[][] board;
	Label playerLabel;
	Label statusLabel;
	Label scoreLabel;
	static Font font = new Font("MS Sans Serif", Font.PLAIN, 20);
	static HashMap<JButton, Square> squares = new HashMap<JButton, Square>();
	JButton[][] jButtonArray = new JButton[6][6];
	JPanel gameBoard;
	int blueScore = 0;
	int greenScore = 0;
	String p1,p2;
	boolean isBluesTurn;
	boolean isHumansTurn;
	Player player1,player2;


	public GameView(String name) {
		super(name);
		playerLabel = new Label("Blue's turn");
		statusLabel = new Label("");
		p1 = "blue";
		p2 = "green";
		scoreLabel = new Label(p1+" "+blueScore+" to "+p2+" "+greenScore);
		setResizable(false);
		isBluesTurn = true;
		isHumansTurn = false;
		this.createAndShowGUI();
	}

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method is invoked from the
	 * event dispatch thread.
	 */
	public void createAndShowGUI() {
		//Create and set up the window.
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		/* Turn off metal's use of bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Set up the content pane.
		this.addComponentsToPane(this.getContentPane());
		//Display the window.
		this.pack();
		this.setVisible(true);

	}

	public void addComponentsToPane(final Container pane) {

		gameBoard = new JPanel();
		gameBoard.setLayout(boardLayout);
		JPanel controls = new JPanel();
		controls.setLayout(new GridLayout(2, 2));

		Dimension buttonSize = new Dimension(275, 175);
		gameBoard.setPreferredSize(new Dimension((int) (buttonSize.getWidth() * 2.5),
				(int) (buttonSize.getHeight() * 3.5)));

		for (int i = 0; i < 6 ; i++) {
			for(int j = 0; j< 6 ; j++){
				JButton square = new JButton();
				square.setContentAreaFilled(false);
				square.setFont(font);
				square.setForeground(Color.WHITE);
				gameBoard.add( square );
				square.setOpaque(true);
				square.setBackground( Color.BLACK );
				jButtonArray[i][j] = square;
			}
		}

		controls.add(playerLabel);
		controls.add(statusLabel);
		controls.add(scoreLabel);

		JButton simulateButton = new JButton("Simulate Turn");
		simulateButton.addActionListener(newGame());
		controls.add(simulateButton);

		JButton humanVsHumanButton = new JButton("Human vs Human :D");
		humanVsHumanButton.addActionListener(restart());
		controls.add(humanVsHumanButton);

		JButton minimaxVsMinimaxButton = new JButton("Minimax vs Minimax");
		minimaxVsMinimaxButton.addActionListener(undo());
		controls.add(minimaxVsMinimaxButton);

		pane.add(gameBoard, BorderLayout.NORTH);
		pane.add(new JSeparator(), BorderLayout.CENTER);
		pane.add(controls, BorderLayout.SOUTH);
	}

	public void makeMove(JButton square){
		if(!isHumansTurn){
			statusLabel.setText("It is not a human's turn.");
			return;
		}
		
		if(!squares.get(square).getColor().equals("white")){
			statusLabel.setText("This sqaure is occupied. Pick again");
			return;
		}
		
		if(isBluesTurn){
			squares.get(square).setColor("blue");
			game.getP1().doTheBlitz(board, squares.get(square), "blue");
			updateBoard();
			isBluesTurn = false;
			blueScore = game.getP2().evaluateBoard(game, game.getP2().getColor());
			scoreLabel.setText(p1+" "+blueScore+" to "+p2+" "+greenScore);
			setTurnText();
		}
		else{
			squares.get(square).setColor("green");
			game.getP2().doTheBlitz(board, squares.get(square), "green");
			updateBoard();
			isBluesTurn = true;
			greenScore = game.getP2().evaluateBoard(game, game.getP2().getColor());
			scoreLabel.setText(p1+" "+blueScore+" to "+p2+" "+greenScore);
			setTurnText();
		}
			
		
		
	}
	
	private void drawNewGame(){


		ActionListener a = new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				JButton square = (JButton) event.getSource();
				makeMove(square);

			}
		};

		for (int i = 0; i < 6 ; i++) {
			for(int j = 0; j< 6 ; j++){
				System.out.println("heeer");
				JButton square = jButtonArray[i][j];
				square.setContentAreaFilled(false);
				square.setFont(font);
				square.setText(game.getBoard()[i][j].getValue()+"");
				square.setForeground(Color.WHITE);
				square.addActionListener(a);
				square.setOpaque(true);
				square.setBackground( Color.BLACK );
				squares.put(square, board[i][j]);
			}
		}


	}

	private ActionListener restart() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				System.out.println("Human vs human");
				JFrame frame = new JFrame();
				int mapChoice = new Integer(JOptionPane.showInputDialog(frame, "Enter a number 1-5 for the map you want to play!"));
				game = new Game(new Human(), new Human(), mapChoice);
				board = game.getBoard();
				isHumansTurn = true;
				drawNewGame();
			}

		};
	}

	private ActionListener newGame() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				System.out.println("Starting game");

				if(isBluesTurn){
					game.takeTurn(game.getP1());
					updateBoard();
					blueScore = game.getP1().evaluateBoard(game, game.getP1().getColor());
					isBluesTurn = false;
					scoreLabel.setText(p1+" "+blueScore+" to "+p2+" "+greenScore);
					setTurnText();
				}
				else{
					game.takeTurn(game.getP2());
					updateBoard();
					greenScore = game.getP2().evaluateBoard(game, game.getP2().getColor());
					isBluesTurn = true;
					scoreLabel.setText(p1+" "+blueScore+" to "+p2+" "+greenScore);
					setTurnText();
				}


			}



		};
	}

	public static void updateBoard(){
		for(Entry<JButton, Square> e : squares.entrySet()){
			if(e.getValue().getColor().equals("white"))
				e.getKey().setBackground(Color.BLACK);
			else if(e.getValue().getColor().equals("blue"))
				e.getKey().setBackground(Color.BLUE);
			else
				e.getKey().setBackground(Color.GREEN);
			e.getKey().setVisible(true);
		}

	}

	private ActionListener undo() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				JFrame frame = new JFrame();
				int mapChoice = new Integer(JOptionPane.showInputDialog(frame, "Enter a number 1-5 for the map you want to play!"));
				game = new Game(new Minimax(), new Minimax(), mapChoice);
				board = game.getBoard();
				drawNewGame();
			}
		};
	}

	private String getPlayerLabel() {
		if (!isBluesTurn) {
			return "Green";
		} else {
			return "Blue ";
		}
	}



	public void setTurnText() {
		playerLabel.setText(getPlayerLabel()+"'s turn");
	}

	public void setScoreLabel(int blackScore, int whiteScore) {
		scoreLabel.setText(p1+" "+whiteScore+" to "+p2+" "+blackScore);
	}

}
