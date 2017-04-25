package MVECC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat.Encoding;

public class MVECCmain {
	

	
	public static void main(String[] args) throws IOException {
		Point p1=new Point(2,7);
		//Point p3=new Point()
		PointOperation operation=new PointOperation();
		//Point p2=operation.doublePoint(p1);
		//p2.toString();
		//System.out.println(p2);
		//Point suma=operation.add(p2, p1);
		//suma.toString();
		//System.out.println(suma);
		EllipticCurveMV algorithm=new EllipticCurveMV();
		System.out.println(algorithm.generateGaloisField());
		algorithm.generatePublicKey(7);
		//EllipticCurveMV algorithm=new EllipticCurveMV();
		PointOperation operations=new PointOperation();
		System.out.println("Generator "+algorithm.generator);
		String text="My name is Mateusz! His name is Olek.";
		byte[] bytes=text.getBytes();
		//byte[] bytes=algorithm.readDocument();
		long privkey=7;
		Point pubkey=operation.multiply(privkey, algorithm.generator);
		/*byte[] result*/ArrayList<Long> result=algorithm.encrypt(bytes,pubkey);
		byte[] plaintext=algorithm.decrypt(result, privkey);
		String finalresult = new String(plaintext, StandardCharsets.UTF_8);
		System.out.println(finalresult);
	}

}
