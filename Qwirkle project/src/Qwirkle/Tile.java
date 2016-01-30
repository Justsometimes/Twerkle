package Qwirkle;

public class Tile {

	public static enum Color {
		RED('R'), ORANGE('O'), BLUE('B'), YELLOW('Y'), GREEN('G'), PURPLE('P');
		public final char c;

		Color(char ch) {
			this.c = ch;
		}
	}

	public static enum Shape {
		CIRCLE('o', '\u262f'), DIAMOND('d', '\u2615'), SQUARE('s', '\u25A0'), CLOVER('c','\u269C'), CROSS('x', '\u2693'), STAR('*', '\u2605');
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
			
			return new Tile(Color.valueOf((""+ s.toCharArray()[0])), Shape.valueOf(("" + s.toCharArray()[1]))); 
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
		return (String.valueOf(shape.u) + String.valueOf(color.c));
	}
	
	/*public static void main(String[] args){
		Shape s = Shape.CIRCLE;
		Color c = Color.RED;
		Tile t = new Tile(c, s);
		System.out.println(c.toString() + t.toString());
	}*/
	
}
