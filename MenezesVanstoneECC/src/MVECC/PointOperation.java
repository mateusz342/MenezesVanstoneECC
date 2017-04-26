package MVECC;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import javafx.util.*;
public class PointOperation {
	BigInteger y1;
	BigInteger y2;
	Point y0;
	BigInteger two=BigInteger.valueOf(2);
	BigInteger three=BigInteger.valueOf(3);
	
	//double Point
	public Point doublePoint(Point P){
		Point result=new Point();
		
		if(P.getY().equals(BigInteger.ZERO)){
			result.setX(BigInteger.ZERO);
			result.setY(BigInteger.ZERO);
		}
		else{
			BigInteger lambda,inv,xnew,ynew;
			BigInteger p=EllipticCurveMV.p;
			BigInteger a=EllipticCurveMV.a;
			lambda=((three.multiply(P.getX().modPow(two, p)).add(a))).mod(p);
			inv=(BigInteger.valueOf(2).multiply(P.getY().mod(p))).modInverse(p);
			lambda=(lambda.multiply(inv)).mod(p);
			
			xnew=(((lambda.multiply(lambda)).mod(p)).subtract(P.getX()).subtract(P.getX())).mod(p);
			ynew=(((lambda.multiply((P.getX().subtract(xnew)).mod(p))).subtract(P.getY())).mod(p));
			
			if(ynew.compareTo(BigInteger.ZERO)<0){
				ynew=ynew.add(p);
			}
			
			result.setX(xnew);
			result.setY(ynew);
		}
		return result;
	}
	
	//addition two Points
	public Point add(Point p1, Point p2){
		Point result=new Point();
		BigInteger p=EllipticCurveMV.p;
		
		if(p1.getX().equals(BigInteger.ZERO) && p1.getY().equals(BigInteger.ZERO)){
			result.setX(p2.getX());
			result.setY(p2.getY());
		}
		else if(p2.getX().equals(BigInteger.ZERO) && p2.getY().equals(BigInteger.ZERO)){
			 result.setX(p1.getX());
	         result.setY(p1.getY());
		}
		else if(p1.getY().equals(BigInteger.ZERO.subtract(p2.getY()))){
			result.setX(BigInteger.ZERO);
			result.setY(BigInteger.ZERO);
		}
		else{
			BigInteger lambda, xnew, ynew,inv;
			
			lambda=(p2.getY().subtract(p1.getY())).mod(p);
			inv=(p2.getX().subtract(p1.getX())).modInverse(p);
			lambda=(lambda.multiply(inv)).mod(p);
			xnew=(((lambda.multiply(lambda)).mod(p)).subtract(p1.getX()).subtract(p2.getX())).mod(p);
			ynew=(((lambda.multiply((p1.getX().subtract(xnew)).mod(p))).subtract(p1.getY())).mod(p));
			
			result.setX(xnew);
			result.setY(ynew);
		}
		return result;
	}
	//the multiplication of Point
	public Point multiply(BigInteger privkey,Point base){
		Point result=new Point(BigInteger.ZERO,BigInteger.ZERO);
		Point generator=new Point(base.getX(),base.getY());
		long privkey1=6;
		 String binary1 = Long.toBinaryString(privkey1);
	       String binary=privkey.toString(2);
			for (int i=binary.length()-1; i>=0; i--) {
	            if (binary.charAt(i) == '1') {
	                if (result.getX()==BigInteger.ZERO && result.getY()==BigInteger.ZERO){
	                    result = generator;
	                } else {
	                    result = add(generator,result);
	                }
	            }
	            generator = doublePoint(generator);
	        }
	        return result;
	}
	//the computation of y0
   public Point computey0(Point generator,BigInteger k){
	BigInteger p=EllipticCurveMV.p;
   	Point y0=new Point();
   	y0=multiply(k, generator);
   	return y0;
   }
   
   //enryption
    public ArrayList<BigInteger> encrypt(ArrayList<Point> pm,Point pub,Point generator,BigInteger k){
    	BigInteger p=EllipticCurveMV.p;
    	Point beta=new Point();
    	ArrayList<BigInteger> ciphertext=new ArrayList<>();
    	beta=multiply(k,pub);
    	
    	BigInteger c1=beta.getX();
    	BigInteger c2=beta.getY();
    	for(int i=0;i<pm.size();i++){
    		Point tocipher=pm.get(i);
    		y1=(c1.multiply(tocipher.getX())).mod(p);
    		y2=(c2.multiply(tocipher.getY())).mod(p);
    		ciphertext.add(y1);
    		ciphertext.add(y2);
    	}
    	return ciphertext;
    }
    
 
    //decryption
    public Point decrypt(Point y0,BigInteger y1,BigInteger y2,BigInteger pri,Point generator){
    	BigInteger p=EllipticCurveMV.p;
    	Point decrypt=multiply(pri,y0);
    	BigInteger c1=decrypt.getX();
    	BigInteger c2=decrypt.getY();
    	
    	BigInteger invc1=c1.modInverse(p);
    	BigInteger invc2=c2.modInverse(p);
    	
    	BigInteger x1=(y1.multiply(invc1)).mod(p);
    	BigInteger x2=(y2.multiply(invc2)).mod(p);

    	Point plaintext=new Point(x1,x2);
    	return plaintext;
    }
    public BigInteger gety1(){
    	return y1;
    }
    public BigInteger gety2(){
    	return y2;
    }
    public Point gety0(){
    	return y0;
    }
}
