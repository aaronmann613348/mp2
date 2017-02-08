package Part2;
import java.util.List;

public class Node {
	public Square[][] board;
	public String Color; //which player is MAX)
	public double utility;		
	public List<Node> children;
	public Node parent;
	public int depth;
	public int tempValue;
	public int x;
	public int y;
	
	public Node(){
		board = null;
		String Color = null; //which color is playing)
		double utility = 0;
		List<Node> children = null;
		Node parent = null;
		int depth = 0;
		int tempValue = 0;
	}

}
