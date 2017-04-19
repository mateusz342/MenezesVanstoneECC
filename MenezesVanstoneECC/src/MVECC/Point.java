package MVECC;

public class Point {
	private long x;
	private long y; 
	
	public Point(){
		
	}
	public Point(long x,long y){
		this.x=x;
		this.y=y;
	}
	
	public long getX(){
		return x;
	}
	
	public long getY(){
		return y;
	}
	
	public void setX(long x){
		this.x=x;
	}
	
	public void setY(long y){
		this.y=y;
	}
	
	@Override
	public String toString() {
		String result=x+" "+y;
		return result;
	}
}
