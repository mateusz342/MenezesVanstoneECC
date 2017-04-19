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
		EllipticCurveMV points=new EllipticCurveMV();
		System.out.println(points.generateGaloisField());
		points.generatePublicKey(7);
		EllipticCurveMV algorithm=new EllipticCurveMV();
		PointOperation operations=new PointOperation();
		System.out.println("Generator "+algorithm.generator);
		String text="Hello world!";
		byte[] bytes=text.getBytes();
		long privkey=7;
		Point pubkey=operation.multiply(privkey, algorithm.generator);
		byte[] result=algorithm.encrypt(bytes,pubkey);
		byte[] plaintext=algorithm.decrypt(result, privkey);
	}

}
