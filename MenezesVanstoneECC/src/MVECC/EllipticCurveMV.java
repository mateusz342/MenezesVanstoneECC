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
	String hexa="";
	static String hexb="64210519 e59c80e7 0fa7e9ab 72243049 feb8deec c146b9b1";
	public static final BigInteger a=new BigInteger("556675194");
	public static final BigInteger b=new BigInteger(/*hexb,16*/"3547273738");
	private static final BigInteger IOUtils = null;
	//public final Point generator;
	private final long NULL_VALUE = -1;
	private int e=0;
	private ArrayList<Point> field;
	public ArrayList<BigInteger> ciphertext;
	public ArrayList<Integer> table=new ArrayList<Integer>();
	BigInteger two=new BigInteger("2");
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
		do{
		p=new BigInteger(192,new SecureRandom());
		}while(p.isProbablePrime(100)==false);
		//p=new BigInteger("1125899906842679");
		return p;
	}
	
	//searching point on Elliptic Curve(working in sensible time for small coefficient)
	public Point thepoint(BigInteger p){
		BigInteger x;
		BigInteger y;
		BigInteger ysqrt;
		int l;
		do{
		do{
			x=new BigInteger(193,new SecureRandom());
		}while(p.compareTo(x)<0);
		y=(x.modPow(BigInteger.valueOf(3), p).add(a.multiply(x)).add(b)).mod(p);
		l=LegandreSymbol(y, p);
		}while(!(y.equals(BigInteger.ZERO)) && l!=1);
		ysqrt=SquareRootComputation(y, p);
		G=new Point(x,ysqrt);
		return G;
	}
	
	//Legendre symbol computation
	private int LegandreSymbol(BigInteger x,BigInteger p){
		int k=1;
		while(!(p.equals(BigInteger.ONE))){
			if(x.equals(BigInteger.ZERO)){
				return 0;
			}
			int v=0;
			while((x.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO))){
				v+=1;
				x=x.divide(BigInteger.valueOf(2));
			}
			if(v%2==1 && (p.mod(BigInteger.valueOf(8)).equals(BigInteger.valueOf(3)) || p.mod(BigInteger.valueOf(8)).equals(BigInteger.valueOf(-3)))){
				k=-k;
			}
			if(x.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)) && p.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))){
				k=-k;
			}
			BigInteger r;
			r=x;
			x=p.mod(r);
			p=r;
		}
		return k;
	}
	
	
	//Tonelli and Shanks square root computation
	private BigInteger SquareRootComputation(BigInteger y, BigInteger p){
		BigInteger pminus1=p.subtract(BigInteger.ONE);
		int l=0;
		BigInteger n;
		BigInteger z;
		BigInteger r;
		BigInteger y1;
		BigInteger ysqrt=BigInteger.ZERO;
		BigInteger b;
		int s;
		int exponent;
		do{
			if(pminus1.mod(two.pow(e)).equals(BigInteger.ZERO)){
				table.add(e);
			}
			e++;
		}while(!(two.pow(e).compareTo(p)>0));
		
		e=table.get(table.size()-1);
		r=pminus1.divide(two.pow(e));
		
		do{
		n=new BigInteger(p.bitCount()-1,new Random());
		l=LegandreSymbol(n, p);
		}while(l!=-1);
		if(e==1){
			BigInteger exponent2=(p.add(BigInteger.ONE)).divide(BigInteger.valueOf(4));
			ysqrt=(ysqrt.add(y.modPow(exponent2, p)));
			return ysqrt;
			}
		z=n.modPow(r, p);
		y1=z;
		s=e;
		BigInteger exponent1=(r.subtract(BigInteger.ONE)).divide(two);
		ysqrt=y.modPow(((r.subtract(BigInteger.ONE)).divide(two)), p);
		b=(y.multiply(ysqrt.pow(2))).mod(p);
		ysqrt=(y.multiply(ysqrt)).mod(p);
		int m;
		BigInteger t;
		while((b.mod(p).equals(BigInteger.ONE))==false){
			m=1;
			while((b.modPow((two.pow(m)), p).equals(BigInteger.ONE)==false&& m<s-1)){
				m+=1;
			}
			if(s==m){
				m-=1;
			}
			exponent=s-m-1;
			BigInteger twoinverse=new BigInteger("2");
			/*if(exponent<0){
				twoinverse=twoinverse.modInverse(p);
				exponent=-exponent;
				twoinverse=twoinverse.pow(exponent);
			}*/
			//t=y1.modPow((twoinverse.pow(exponent)), p);
			t=y1.modPow((two.pow(exponent)), p);
			y1=t.modPow(two, p);
			s=m;
			ysqrt=(t.multiply(ysqrt)).mod(p);
			b=(y1.multiply(b)).mod(p);
			}
		
	return ysqrt;
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
