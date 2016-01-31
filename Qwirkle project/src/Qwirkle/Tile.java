package Qwirkle;

import java.util.HashSet;
import java.util.Set;

public class Tile {

	public static enum Color {
		RED('R'), ORANGE('O'), BLUE('B'), YELLOW('Y'), GREEN('G'), PURPLE('P');
		public final char c;

		Color(char ch) {
			this.c = ch;
		}
	}
// the second char (u) used to be a UTF-8 character, but it turned out that the UTF-8 caused several errors.
	public static enum Shape {
		CIRCLE('o', 'o'), DIAMOND('d', 'd'), SQUARE('s', 's'), CLOVER('c','c'), CROSS('x', 'x'), STAR('*', '*');
		public final char c;
		public final char u;
		
		Shape(char c, char u){
			this.c = c;
			this.u = u;
		}
	}
	
	private final Shape shape;
	private final Color color;
	
	public Tile(Color color, Shape shape){
		this.shape = shape;
		this.color = color;
	}
	public static Tile buildTile(String s){
		if(s.matches("^[ROBYGP][odscx*]$")){
			Color c = null;
			switch (s.toCharArray()[0]){
			case 'R': 
				c = Color.RED;
				break;
			case 'O':
				c = Color.ORANGE;
				break;
			case 'B':
				c = Color.BLUE;
				break;
			case 'Y':
				c = Color.YELLOW;
				break;
			case 'G':
				c = Color.GREEN;
				break;
			case 'P':
				c = Color.PURPLE;
				break;
			}
			Shape sh = null;
			switch (s.toCharArray()[1]){
			case 'o':
				sh = Shape.CIRCLE;
				break;
			case 'd':
				sh = Shape.DIAMOND;
				break;
			case 's':
				sh = Shape.SQUARE;
				break;
			case 'c':
				sh = Shape.CLOVER;
				break;
			case 'x':
				sh = Shape.CROSS;
				break;
			case '*':
				sh = Shape.STAR;
				break;
			}
			
			
			return new Tile(c, sh); 
		} else {
			return null;
		}
	}
	

	public Color getColor(){
		 return color;
	}
	
	public Shape getShape(){
		return shape;
	}
	
	public String toString(){
		return (String.valueOf(color.c) + String.valueOf(shape.c));
	}
	
	public boolean tileInHand(Set<Tile> hand){
		boolean result = false;
		for(Tile inHand :hand){
			if(inHand.toString().equals(this.toString())){
				result = true;
				break;
			}
		}
		return result;
	}
	
	public static void main(String[] args){
		Tile t = Tile.buildTile("Bs");
		System.out.println(t.toString());
		Set<Tile> hand = new HashSet<Tile>();
		Tile f = new Tile(Color.BLUE, Shape.SQUARE);
		System.out.println(f.toString());
		hand.add(f);
		System.out.println(hand.contains(t));
		System.out.println(t.tileInHand(hand));
	}
	
}
