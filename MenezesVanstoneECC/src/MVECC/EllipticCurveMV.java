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
	public static BigInteger p;
	public static  BigInteger a;//=new BigInteger("810017366067207");
	public static  BigInteger b;//=new BigInteger("864021329774289");
	public ArrayList<BigInteger> ciphertext;
	BigInteger two=new BigInteger("2");
	public ArrayList<BigInteger> poweredByTwo=new ArrayList<BigInteger>();
	BigInteger y1;
	BigInteger y2;
	Point y0;
	byte[] message;
	Point G;
	boolean found=false;
	Point H;
	Point GonCurve;
	
	//elliptic curve paramter initialization
	public void ParameterInitialization() throws IOException{
		String aparameter=readParameters(1, 0);
		a=new BigInteger(aparameter);
		String bparameter=readParameters(0, 1);
		b=new BigInteger(bparameter);
	}
	//generation of modulus p(in this case 192bit)
	public BigInteger generateP(){
		int exp=0;
		do{
		do{
		p=new BigInteger(192,new SecureRandom());
		}while(p.isProbablePrime(100)==false);
		BigInteger pminus1=p.subtract(BigInteger.ONE);
		ArrayList<Integer> table1=new ArrayList<Integer>();
		do{
			if(pminus1.mod(two.pow(exp)).equals(BigInteger.ZERO)){
				table1.add(exp);
			}
			exp++;
		}while(!(two.pow(exp).compareTo(p)>0));
		exp=table1.get(table1.size()-1);
		}while(exp==1);
		//p=new BigInteger("1125899906842679");
		return p;
	}
	
	//searching point on Elliptic Curve
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
	
	
	//Tonelli and Shanks square root computation algorithm
	private BigInteger SquareRootComputation(BigInteger y, BigInteger p){
		BigInteger pminus1;
		int l=0;
		BigInteger n;
		BigInteger z;
		BigInteger r;
		BigInteger y1;
		BigInteger ysqrt;
		int exponent=0;
		//BigInteger b;
		pminus1=p.subtract(BigInteger.ONE);
		ArrayList<Integer> table=new ArrayList<Integer>();
		int e=0;
		do{
			if(pminus1.mod(two.pow(e)).equals(BigInteger.ZERO)){
				table.add(e);
			}
			e++;
		}while(!(two.pow(e).compareTo(p)>0));
		
		e=table.get(table.size()-1);
		
		r=pminus1.divide(two.pow(e));
		
		/*do{
		n=new BigInteger(p.bitCount()-1,new Random());
		l=LegandreSymbol(n, p);
		}while(l!=-1);*/
		
		//finding smallest q
		BigInteger q=BigInteger.valueOf(2);
		for(int i=0;;i++){
			if(q.modPow((p.subtract(BigInteger.ONE)).divide(two), p).equals(p.subtract(BigInteger.ONE))){
				break;
			}
			q=q.add(BigInteger.ONE);
		}
		ysqrt=y.modPow((r.add(BigInteger.ONE)).divide(two), p);
		BigInteger b=y.modPow(r, p);
		BigInteger g=q.modPow(r, p);
		
		int exp=e;
		int straznik=0;
		
		//keep looping until b become 1 or m becomes 0
		while(straznik==0){
			int m;
			for(m=0;m<exp;m++){
				
				if(gcd(p,b).equals(BigInteger.ONE)==false){
					System.out.println("Modulus p and b are not co-prime. Try again!");
					return BigInteger.valueOf(-1);
				}
				
				if(b.modPow(two.pow(m), p).equals(BigInteger.ONE)){
					break;
				}
			}
			if(m==0){
				return ysqrt;
			}
			//update the values
			exponent=exp-m-1;
			if(exponent<0){
				return BigInteger.ZERO;
			}
			ysqrt=(ysqrt.multiply(g.modPow(two.pow(exp-m-1),p)).mod(p));
			g=g.modPow(two.pow(exp-m), p);
			b=(b.multiply(g)).mod(p);
			
			if(b.mod(p).equals(BigInteger.ONE)){
				return ysqrt;
			}
			exp=m;
		}
	return ysqrt;
	}
	
	//computing gcd
	public BigInteger gcd(BigInteger a, BigInteger b)
	{
	    if (b.equals(BigInteger.ZERO))
	        return a;
	    else
	        return gcd(b, a.mod(b));
	}
	
/*
 * Methods below gives sensible result only for long or int values(because uses brute force searching)
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
	*/
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
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		  everything = sb.toString();
		} finally {
		    br.close();
		}
		message=everything.getBytes();
		return message; 
	}
	
	//read parameters from file
	public String readParameters(int a,int b) throws IOException{
		//BufferedReader brA = new BufferedReader(new FileReader("D:/git/MenezesVanstone/MenezesVanstoneECC/src/MVECC/ParameterA.txt"));//enter your path file
		//BufferedReader brB = new BufferedReader(new FileReader("D:/git/MenezesVanstone/MenezesVanstoneECC/src/MVECC/ParameterB.txt"));
		String everything;
		File fileA=new File("D:/git/MenezesVanstone/MenezesVanstoneECC/src/MVECC/ParameterA.txt");
		File fileB=new File("D:/git/MenezesVanstone/MenezesVanstoneECC/src/MVECC/ParameterB.txt");
		int sizeA=(int) fileA.length();
		int sizeB=(int) fileB.length();
		//byte[] messageA=new byte[sizeA];
		//byte[] messageB=new byte[sizeB];
		
		if(a==1){
			BufferedReader brA = new BufferedReader(new FileReader("D:/git/MenezesVanstone/MenezesVanstoneECC/src/MVECC/ParameterA.txt"));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = brA.readLine();

		    while (line != null) {
		        sb.append(line);
		        //sb.append(System.lineSeparator());
		        line = brA.readLine();
		    }
		  everything = sb.toString();
		} finally {
		    brA.close();
		}
		return everything;
		}
		
		else{
			BufferedReader brB = new BufferedReader(new FileReader("D:/git/MenezesVanstone/MenezesVanstoneECC/src/MVECC/ParameterB.txt"));
			try {
			    StringBuilder sb = new StringBuilder();
			    String line = brB.readLine();

			    while (line != null) {
			        sb.append(line);
			        //sb.append(System.lineSeparator());
			        line = brB.readLine();
			    }
			  everything = sb.toString();
			} finally {
			    brB.close();
			}
			return everything;
		}
	}
	
	
	public BigInteger getp(){
		return p;
	}
}
