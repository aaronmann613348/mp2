package Part2;

public class Square {

	private int x;
	private int y;
	private String color;
	private int value;
	
	public Square(int x, int y, int value){
		this.color = "white";
		this.x = x;
		this.y = y;
		this.value = value;
	}
	
	public Square(int x, int y, int value, String color){
		this.color = color;
		this.x = x;
		this.y = y;
		this.value = value;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public String getColor(){
		return this.color;
	}
	
}
