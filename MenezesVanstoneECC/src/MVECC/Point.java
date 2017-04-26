package MVECC;

import java.math.BigInteger;
import java.security.spec.ECPoint;

public class Point implements java.io.Serializable{
	//private long x;
	//private long y; 
	private  BigInteger x;
	private BigInteger y;
	public Point(){
		
	}
	/*public Point(long x,long y){
		this.x=x;
		this.y=y;
	}
	*/
	public Point(BigInteger x, BigInteger y){
		this.x=x;
		this.y=y;
	}
	public BigInteger getX(){
		return x;
	}
	
	public BigInteger getY(){
		return y;
	}
	
	public void setX(BigInteger x){
		this.x=x;
	}
	
	public void setY(BigInteger y){
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
