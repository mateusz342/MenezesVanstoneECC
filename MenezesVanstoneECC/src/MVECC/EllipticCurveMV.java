package MVECC;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.spec.EllipticCurve;
import java.util.ArrayList;
import java.util.Random;

public class EllipticCurveMV {
	private EllipticCurve c;
	public static BigInteger p;
	public static final BigInteger a=new BigInteger("556675194");
	public static final BigInteger b=new BigInteger("3547273738");
	private static final BigInteger IOUtils = null;
	//public final Point generator;
	private final long NULL_VALUE = -1;

	private ArrayList<Point> field;
	public ArrayList<BigInteger> ciphertext;
	//private BigInteger[] poweredByTwo;
	public ArrayList<BigInteger> poweredByTwo=new ArrayList<BigInteger>();
	BigInteger y1;
	BigInteger y2;
	Point y0;
	byte[] message;
	Point G;
	boolean found=false;
	Point H;
	Point GonCurve;
	//Elliptic Curve y^2=x^3+Ax+Bx
	//public EllipticCurveMV(){
		//field = generateGaloisField();
		//Point point=new Point(BigInteger.valueOf(2),BigInteger.valueOf(4));
	    //generator =thebasepoint();// point; // Creating generator point
	//}
	public BigInteger generateP(){
		//do{
		//p=new BigInteger(160,new SecureRandom());
		//}while(p.isProbablePrime(100)==false);
		p=new BigInteger("1125899906842679");
		return p;
	}
	
	//searching point on Elliptic Curve(working in sensible time for small coefficient)
	public Point thebasepoint(BigInteger p){
		BigInteger x;
		BigInteger y;
		BigInteger max=new BigInteger("130000000");
		while(found==false){
		do{
		x=new BigInteger(7,new SecureRandom());
		y=(x.modPow(BigInteger.valueOf(3), p).add(a.multiply(x)).add(b)).mod(p);
		}while(y.equals(0) /*|| y.compareTo(max)>0*/);
		G=isresidue(y,p);
		GonCurve=new Point(x,G.getY());
		if(GonCurve.getY().equals(BigInteger.valueOf(0))==false){
			found=true;
		}
		}
		return GonCurve;
	}
	//checking if y^2=x^3+ax+b
	public Point isresidue( BigInteger a,BigInteger p){
		Point epoint;
		BigInteger i=BigInteger.valueOf(1);
		BigInteger sum = BigInteger.valueOf(0);
		BigInteger y=BigInteger.valueOf(1);
		do{
			//sum=(sum+i)%p;
			sum=(sum.add(i)).mod(p);
			if(a.equals(sum)){
				epoint=new Point(a,y);
				return epoint;
			}
			else 
			{
				y=y.add(BigInteger.valueOf(1));
				i=i.add(BigInteger.valueOf(2));
			}
			}while(i.compareTo(p)<0 && y.compareTo(a)<0);
		epoint=new Point(BigInteger.valueOf(0),BigInteger.valueOf(0));
		return epoint;
		}

	//generate an array with elements which powered by two   
	private void generatePoweredByTwo(BigInteger p) {
	      // poweredByTwo = new BigInteger[];
	    BigInteger i=new BigInteger("0");   
	    BigInteger result;
		do{
				result=(i.multiply(i)).mod(p);
	    	   poweredByTwo.add(result);
	    	   i=i.add(BigInteger.valueOf(1));
	       }while(p.compareTo(i)>0);
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
		 x111=b[i];
		if(straznik==1 && i==b.length-1){
			x222=0;
		}
		else{
			x222=b[i+1];}
		result=new Point(BigInteger.valueOf(x111),BigInteger.valueOf(x222));
		messagepoints.add(result);
		}
		return messagepoints;
	}
	
	
	//from Point to byte
	private byte[] map(ArrayList<Point> points){
		byte[] result=new byte[points.size()*2];
		byte[] result1=new byte[points.size()*2];
		int j=0;
		for(int i=0;i<points.size();i++){
			result[j]= points.get(i).getX().byteValue();
			result[j+1]=(byte) points.get(i).getY().byteValue();
			j+=2;
		}
		return result;
	}
	
	//generation of public key
	public Point generatePublicKey(Point generator,BigInteger privkey){
		PointOperation operation=new PointOperation();
		Point result=operation.multiply(privkey, generator);
		return result;
	}
	
	//encryption method
	public ArrayList<BigInteger> encrypt(byte[] bytes, Point pub,Point generator){
		PointOperation operation=new PointOperation();
		p=EllipticCurveMV.p;
		int j=0;
	    ArrayList<Point> plaintext=map(bytes);
	    BigInteger k=new BigInteger(p.bitCount()-1,new SecureRandom());
		y0=operation.computey0(generator,k);
		ciphertext=operation.encrypt(plaintext, pub, generator,k);
		byte[] result=new byte[ciphertext.size()];
		for(int i=0;i<ciphertext.size();i++){
			result[i]=ciphertext.get(i).byteValue();
		}
		y1=operation.gety1();
		y2=operation.gety2();
		this.y1=y1;
		this.y2=y2;
		return ciphertext;
	}
	//decryption method
	public byte[] decrypt(ArrayList<BigInteger> in,BigInteger pri,Point generator){
		PointOperation operation=new PointOperation();
		ArrayList<Point> points=new ArrayList<>();
		for(int i=0;i<in.size();i+=2){
		Point plaintext=operation.decrypt(y0,in.get(i),in.get(i+1), pri, generator);
		points.add(plaintext);
		}
		byte[] result=map(points);
	       return result;
	}
	
	//read document from file
	public byte[] readDocument() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("D:/git/MenezesVanstone/MenezesVanstoneECC/src/MVECC/In.txt"));//enter your path file
		String everything;
		File file=new File("D:/git/MenezesVanstone/MenezesVanstoneECC/src/MVECC/In.txt");
		int size=(int) file.length();
		byte[] message=new byte[size];
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        //sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		  everything = sb.toString();
		} finally {
		    br.close();
		}
		message=everything.getBytes();
		return message; 
	}
	public BigInteger getp(){
		return p;
	}
}
