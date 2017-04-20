package MVECC;

import java.math.BigInteger;
import java.util.ArrayList;

public class EllipticCurveMV {
	public static final long p=67003;
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
	private Point map(byte[] b){
		byte[] x1=new byte[b.length/2];
		byte[] x2=new byte[b.length-x1.length];
		System.arraycopy(b, 0, x1, 0, b.length/2);
		System.arraycopy(b, b.length/2, x2, 0, b.length-x1.length);
		BigInteger x11=new BigInteger(x1);
		BigInteger x22=new BigInteger(x2);
		long x111=x11.longValue();
		long x222=x22.longValue();
		Point result=new Point(x111,x222);
		return result;
	}
	
	
	//from Point to byte
	private byte[] map(Point p){
		BigInteger x1=BigInteger.valueOf(p.getX());
		BigInteger x2=BigInteger.valueOf(p.getY());
		byte[] array1=x1.toByteArray();
		byte[] array2=x2.toByteArray();
		byte[] result=new byte[array1.length+array2.length];
		System.arraycopy(array1, 0, result, 0, array1.length);
		System.arraycopy(array2, 0, result, array1.length,array2.length);
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
	    //Point plaintext=new Point(9,1);
	    Point plaintext=map(bytes);
		y0=operation.computep0(pub, generator);
		operation.encrypt(plaintext, pub, generator);
		y1=operation.gety1();
		y2=operation.gety2();
		/*for(int i=0;i<bytes.length;i++){
			p[i]=map(bytes[i]);
			operation.encrypt(p[i], pub, generator);
			y1=operation.gety1();
			y2=operation.gety2();
			result[j]=map(y0);
			j++;
			
		}*/
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
		//byte[] result=new byte[bytes.length/2];
		Point plaintext=operation.decrypt(y0,y1,y2, pri, generator);
	byte[] result=map(plaintext);
	       return result;
	}
}
