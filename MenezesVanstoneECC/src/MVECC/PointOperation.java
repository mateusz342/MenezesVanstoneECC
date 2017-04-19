package MVECC;
import java.math.BigInteger;
import java.util.*;
import javafx.util.*;
public class PointOperation {
	//BigInteger y1,y2;
	long y1,y2;
	public Point doublePoint(Point P){
		Point result=new Point();
		
		if(P.getY()==0){
			result.setX(0);
			result.setY(0);
		}
		else{
			long lambda,inv,xnew,ynew;
			long p=EllipticCurveMV.p;
			long a=EllipticCurveMV.a;
			lambda=((((3*P.getX()*P.getX())%p)+a)%p);
			inv=getInverse(2*P.getY(),p);
			lambda*=inv;
			lambda%=p;
			
			xnew=(((lambda*lambda)%p)-(2*P.getX()%p)+p)%p;
			ynew=(((lambda*(P.getX()-xnew))%p)-P.getY()+p)%p;
			
			if(ynew<0){
				ynew+=p;
			}
			
			result.setX(xnew);
			result.setY(ynew);
		}
		return result;
	}
	
	public long getInverse(long n,long m){
		while(n>m){
			n-=m;
		}
		while(n<0){
			n+=m;
		}
        long gq = m, gy = 0;
        long lq = n, ly = 1;
        long tq = lq, ty = ly;
        while (lq != 1) {
            long d = gq/lq;
            lq = gq - d*lq; ly = gy - d*ly;
            gq = tq; gy = ty;
            tq = lq; ty = ly;
        }
        if (ly < 0) {
            ly += m;
        }
        return ly;
	}
	
	public Point add(Point p1, Point p2){
		Point result=new Point();
		long p=EllipticCurveMV.p;
		
		if(p1.getX()==0 && p1.getY()==0){
			result.setX(p2.getX());
			result.setY(p2.getY());
		}
		else if(p2.getX()==0 && p2.getY()==0){
			 result.setX(p1.getX());
	         result.setY(p1.getY());
		}
		else if(p1.getY() == -p2.getY()){
			result.setX(0);
			result.setY(0);
		}
		else if(p1.getX()-p2.getY()==0){
			result.setX(Long.MAX_VALUE);
			result.setY(Long.MAX_VALUE);
		}
		else{
			long lambda, xnew, ynew,inv;
			
			lambda=(p2.getY()-p1.getY())%p;
			inv=getInverse(p2.getX()-p1.getX(),p);
			lambda*=inv;
			lambda%=p;
			
			xnew=(((lambda*lambda)%p)-p1.getX()-p2.getX()+2*p)%p;
			ynew=(((lambda*(p1.getX()-xnew))%p)-p1.getY()+2*p)%p;
			
			result.setX(xnew);
			result.setY(ynew);
		}
		return result;
	}
	public Point multiply(long privkey,Point base){
		Point result=new Point();
		Point generator=new Point(base.getX(),base.getY());
		
		 String binary = Long.toBinaryString(privkey);
	        for (int i=binary.length()-1; i>=0; i--) {
	            if (binary.charAt(i) == '1') {
	                if (i == binary.length()-1){
	                    result = generator;
	                } else {
	                    result = add(generator,result);
	                }
	            }
	            generator = doublePoint(generator);
	        }
	        return result;
	}
    public Point minus(Point p1, Point p2){
        Point temp = new Point();
        Point res = new Point();
        
        temp.setX(p2.getX());
        temp.setY(-p2.getY());
        
        res = add(p1, temp);
        
        return res;
    }
    
   public Point computep0(Point pub,Point generator){
	long p=EllipticCurveMV.p;
   	long k=6;
   	Point y0=new Point();
   	y0=multiply(k, generator);
   	return y0;
   }
    public Point encrypt(Point pm,Point pub,Point generator){
    	long p=EllipticCurveMV.p;
    	long k=6;
    	Point y0=new Point();
    	Point beta=new Point();
    	Point plaintext=new Point(9,1);
    	y0=multiply(k, generator);
    	beta=multiply(k,pub);
    	
    	long c1=beta.getX();
    	long c2=beta.getY();
    	
    	y1=(c1*pm.getX())%p;
    	y2=(c2*pm.getY())%p;
    	//y1=BigInteger.valueOf(c1).multiply(BigInteger.valueOf(pm.getX())).mod(BigInteger.valueOf(p));
    	//y2=BigInteger.valueOf(c2).multiply(BigInteger.valueOf(pm.getY())).mod(BigInteger.valueOf(p));
    	
    	this.y1=y1;
    	this.y2=y2;
    	return y0;
    }
    
 

    public Point decrypt(Point y0,long y1,long y2,long pri,Point generator){
    	EllipticCurveMV algorithm=new EllipticCurveMV();
    	Point decrypt=multiply(pri,y0);
    	//Point plaintext=new Point();
    	long c1=decrypt.getX();
    	long c2=decrypt.getY();
    	
    	BigInteger y1big=BigInteger.valueOf(y1);//y1;
    	BigInteger y2big=BigInteger.valueOf(y2);//y2;
    	BigInteger c1big=BigInteger.valueOf(c1);
    	BigInteger c2big=BigInteger.valueOf(c2);
    	
    	BigInteger p=BigInteger.valueOf(algorithm.p);
    	BigInteger invc1=c1big.modInverse(p);
    	BigInteger invc2=c2big.modInverse(p);
    	
    	BigInteger x1=(y1big.multiply(invc1)).mod(p);
    	BigInteger x2=(y2big.multiply(invc2)).mod(p);
    	
    	Point plaintext=new Point(x1,x2);
    	return plaintext;
    }
   /* public BigInteger gety1(){
    	return y1;
    }
    public BigInteger gety2(){
    	return y2;
    }
    */
    public long gety1(){
    	return y1;
    }
    public long gety2(){
    	return y2;
    }
}
