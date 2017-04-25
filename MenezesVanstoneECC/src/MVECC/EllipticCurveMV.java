package MVECC;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.spec.EllipticCurve;
import java.util.ArrayList;

public class EllipticCurveMV {
	private EllipticCurve c;
	public static final long p=67003;
	public static final long a=1;
	public static final long b=6;
	public final Point generator;
	private final long NULL_VALUE = -1;

	private ArrayList<Point> field;
	public ArrayList<Long> ciphertext;
	private long[] poweredByTwo;
	long y1,y2;
	Point y0;
	byte[] message;
	
	
	//Elliptic Curve y^2=x^3+Ax+Bx
	public EllipticCurveMV(){
		field = generateGaloisField();
	    generator = field.get(1); // Creating generator point
	}
	
	//Generate Galois Field
	public ArrayList<Point> generateGaloisField(){
		BigInteger x= new BigInteger(160,new SecureRandom() );
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
	private ArrayList<Point> map(byte[] b){
		Point result = null;
		long x222,x111;
		ArrayList<Point> messagepoints = new ArrayList<>();
		byte[] x1=new byte[b.length/2];
		byte[] x2=new byte[b.length-x1.length];
		System.arraycopy(b, 0, x1, 0, b.length/2);
		System.arraycopy(b, b.length/2, x2, 0, b.length-x1.length);
		BigInteger x11=new BigInteger(x1);
		BigInteger x22=new BigInteger(x2);
		int straznik = 0;
		if(b.length%2 != 0){
			straznik=1;
		}
		for(int i=0;i<b.length;i+=2){
		//long x111=x11.longValue();
		//long x222=x22.longValue();
		 x111=b[i];
		if(straznik==1 && i==b.length-1){
			x222=0;
		}
		else{
			x222=b[i+1];}
		result=new Point(x111,x222);
		messagepoints.add(result);
		}
		return messagepoints;
	}
	
	
	//from Point to byte
	private byte[] map(ArrayList<Point> points){
		byte[] result=new byte[points.size()*2];
		int j=0;
		for(int i=0;i<points.size();i++){
			result[j]=(byte) points.get(i).getX();
			result[j+1]=(byte) points.get(i).getY();
			j+=2;
		}
		
		
		//BigInteger x1=BigInteger.valueOf(p.getX());
		//BigInteger x2=BigInteger.valueOf(p.getY());
		//byte[] array1=x1.toByteArray();
		//byte[] array2=x2.toByteArray();
		//byte[] result=new byte[array1.length+array2.length];
		//System.arraycopy(array1, 0, result, 0, array1.length);
		//System.arraycopy(array2, 0, result, array1.length,array2.length);
		return result;
	}
	
	public Point generatePublicKey(long privkey){
		PointOperation operation=new PointOperation();
		Point result=operation.multiply(privkey, generator);
		System.out.println(result);
		return result;
	}
	
	public ArrayList<Long> encrypt(byte[] bytes, Point pub){
		PointOperation operation=new PointOperation();
		
		Point[] p=new Point[bytes.length];
		int j=0;
	    //Point plaintext=new Point(9,1);
	    ArrayList<Point> plaintext=map(bytes);
		y0=operation.computep0(pub, generator);
		ciphertext=operation.encrypt(plaintext, pub, generator);
		byte[] result=new byte[ciphertext.size()];
		for(int i=0;i<ciphertext.size();i++){
			result[i]=ciphertext.get(i).byteValue();
		}
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
		return ciphertext;//result1;
	}
	public byte[] decrypt(/*byte[]*/ArrayList<Long> in,long pri){
		PointOperation operation=new PointOperation();
		//byte[] result=new byte[2];
		ArrayList<Point> points=new ArrayList<>();
		for(int i=0;i<in.size();i+=2){
		Point plaintext=operation.decrypt(y0,in.get(i),in.get(i+1), pri, generator);
		points.add(plaintext);
		}
		byte[] result=map(points);
	//byte[] result=map(plaintext);
	       return result;
	}
	
	public byte[] readDocument() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("D:/git/MenezesVanstone/MenezesVanstoneECC/src/MVECC/In.txt"));
		String everything;
		File file=new File("D:/git/MenezesVanstone/MenezesVanstoneECC/src/MVECC/In.txt");
		int size=(int) file.length();
		byte[] message=new byte[size];
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		  everything = sb.toString();
		} finally {
		    br.close();
		}
		message=everything.getBytes();
		return message; //message;
	}
}
