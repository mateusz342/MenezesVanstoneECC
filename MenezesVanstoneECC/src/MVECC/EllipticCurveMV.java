package MVECC;

import java.math.BigInteger;
import java.util.ArrayList;

public class EllipticCurveMV {
	public static final long p=11;
	public static final long a=1;
	public static final long b=6;
	public final Point generator;
	private final long NULL_VALUE = -1;

	private ArrayList<Point> field;
	private long[] poweredByTwo;
	long y1,y2;
	Point y0;
	//Elliptic Curve y^2=x^3+Ax+Bx
	public EllipticCurveMV(){
		field = generateGaloisField();
	    generator = field.get(1); // Creating generator point
	}
	
	//Generate Galois Field
	
	public ArrayList<Point> generateGaloisField(){
		ArrayList<Point> points = new ArrayList<>();
	    ArrayList<Long> temp;
	    generatePoweredByTwo();
	    
	    for(int i=0; i<p; i++) {
	           temp = function(i);
	           for(int j=0; j<temp.size(); j++) {
	               Point p = new Point(i,temp.get(j));
	               points.add(p);
	           }
	       }
	       return points;
	}
	
	//function y^2=x^3+Ax+B mod p
	 private ArrayList<Long> function(long x) {
	       ArrayList<Long> result = new ArrayList<>();
	       double y = NULL_VALUE;
	       y = x*x*x + a*x + b;
	       y = y % p;
	       for(int i=0; i<poweredByTwo.length; i++) {
	           if((poweredByTwo[i] % p) == y) {
	               result.add((long) i);
	           }
	       }
	       return result;
	   }
	
	//generate an array with elements which powered by two   
	private void generatePoweredByTwo() {
	       poweredByTwo = new long[(int)p];
	       for(int i=0; i<p; i++) {
	           poweredByTwo[i] = i * i;
	       }
	   }
	
	//from byte to Point
	private Point map(byte b){
		int index=(int)b&(0xFF);
		Point result=new Point(field.get(index).getX(),field.get(index).getY());
		return result;
	}
	
	
	//from Point to byte
	private byte map(Point p){
		byte result=0;
		for(int i=0;i<field.size();i++){
			if(field.get(i).isEqual(p)){
				result=(byte) i;
				break;
			}
		}
		return result;
	}
	
	public Point generatePublicKey(long privkey){
		PointOperation operation=new PointOperation();
		Point result=operation.multiply(privkey, generator);
		System.out.println(result);
		return result;
	}
	
	public byte[] encrypt(byte[] bytes, Point pub){
		PointOperation operation=new PointOperation();
		byte[] result=new byte[bytes.length*2];
		Point[] p=new Point[bytes.length];
		int j=0;
	    Point plaintext=new Point(9,1);
	    y0=operation.computep0(pub, generator);
		for(int i=0;i<bytes.length;i++){
			p[i]=map(bytes[i]);
			operation.encrypt(p[i], pub, generator);
			y1=operation.gety1();
			y2=operation.gety2();
			result[j]=map(y0);
			j++;
			
		}
	 /*BigInteger*/ y1=operation.gety1();
	 /*BigInteger*/ y2=operation.gety2();
		//byte[] r1=y1.toByteArray();
		//byte[] r2=y2.toByteArray();
		//byte[] result1=new byte[r1.length+r2.length];
		//System.arraycopy(r1, 0, result1, 0, r1.length);
		//System.arraycopy(r2, 0, result1, r1.length, r2.length);
	 this.y1=y1;
	 this.y2=y2;
		return result;//result1;
	}
	public byte[] decrypt(byte[] bytes,long pri){
		PointOperation operation=new PointOperation();
		byte[] result=new byte[bytes.length/2];
		operation.decrypt(y0,y1,y2, pri, generator);
	       return result;
	}
}
