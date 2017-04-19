package MVECC;

public class PointOperation {
	
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
}
