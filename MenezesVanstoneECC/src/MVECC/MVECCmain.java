package MVECC;

public class MVECCmain {

	public static void main(String[] args) {
		Point p1=new Point(2,7);
		//Point p3=new Point()
		PointOperation operation=new PointOperation();
		Point p2=operation.doublePoint(p1);
		//p2.toString();
		System.out.println(p2);
		Point suma=operation.add(p2, p1);
		//suma.toString();
		System.out.println(suma);
	}

}
