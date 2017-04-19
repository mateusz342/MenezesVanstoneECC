package MVECC;

import java.math.BigInteger;

public class Point {
	private long x;
	private long y; 
	BigInteger x1;
	BigInteger x2;
	public Point(){
		
	}
	public Point(long x,long y){
		this.x=x;
		this.y=y;
	}
	
	public Point(BigInteger x1, BigInteger x2){
		this.x1=x1;
		this.x2=x2;
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
	public boolean isEqual(Point p) {
		return x==p.x && y==p.y;
	}
}
