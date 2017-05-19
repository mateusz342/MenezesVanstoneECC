package MVECC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat.Encoding;

public class MVECCmain {
	public static void main(String[] args) throws IOException {
		EllipticCurveMV algorithm=new EllipticCurveMV();
		Point generator;
		BigInteger p;
		int w1=0,w2=0,w3=0;
		ArrayList<BigInteger> result = new ArrayList<BigInteger>();
		String finalresult = new String();
		BigInteger privkey = null;
		algorithm.ParameterInitialization();
		do{
		p=algorithm.generateP();
		generator=algorithm.thepoint(p);
		}while(generator.getY().equals(BigInteger.ZERO));
		System.out.println("Elliptic Curve: y^2=x^3+"+EllipticCurveMV.a+"x+"+EllipticCurveMV.b);
		System.out.println("Modulus:"+p);
		System.out.println("Point from Elliptic Curve:"+generator);
		
		do{
		System.out.println("Choose what you want to do:");
		System.out.println("1.Encrypt message");
		System.out.println("2.Decrypt ciphertext");
		System.out.println("3.Exit");
		Scanner in= new Scanner(System.in);
		int choice=in.nextInt();
		
		
		
		if(choice==1 && w1==0){
		do{
		privkey=new BigInteger(p.bitCount()-1,new SecureRandom());
		}while(privkey.equals(0));
		Point pubkey=algorithm.generatePublicKey(generator,privkey);
		//String text="To jest test";
		//byte[] tocipher=text.getBytes();
		byte[] bytes=algorithm.readDocument();
		/*ArrayList<BigInteger>*/ result=algorithm.encrypt(bytes,pubkey,generator);
		System.out.print("Ciphertext:");
		
		byte[] ciphertext=new byte[result.size()];
		for(int i=0;i<result.size();i++){
			ciphertext[i]=result.get(i).byteValue();
		}
		String finalciphertext=new String(ciphertext,StandardCharsets.UTF_8);
		System.out.println(finalciphertext);
		w1++;
		
		BufferedWriter writer=null;
		try
		{
		    writer = new BufferedWriter( new FileWriter("D:/git/MenezesVanstone/MenezesVanstoneECC/src/MVECC/Ciphertext.txt"));//change to your pathfile
		    writer.write(finalciphertext);

		}
		catch ( IOException e)
		{
		}
		finally
		{
		    try
		    {
		       if ( writer != null)
		        writer.close( );
		    }
		    catch ( IOException e)
		    {
		   }
	}
		}
		
		else if(choice==2 && w1==1){
		byte[] plaintext=algorithm.decrypt(result, privkey,generator);
		 finalresult = new String(plaintext, StandardCharsets.UTF_8);
		System.out.println("Output:"+finalresult);
		w2++;
		}
		
		else if(choice==3){
			return;
		}
		else{
			System.out.println("Try again");}
		}while(w1==0 || w2==0);
		
		//write file
		if(w1==1 && w2==1){
		BufferedWriter writer=null;
		try
		{
		    writer = new BufferedWriter( new FileWriter("D:/git/MenezesVanstone/MenezesVanstoneECC/src/MVECC/Out.txt"));//change to your pathfile
		    writer.write(finalresult);

		}
		catch ( IOException e)
		{
		}
		finally
		{
		    try
		    {
		       if ( writer != null)
		        writer.close( );
		    }
		    catch ( IOException e)
		    {
		   }
	}
	
	}
		}

}
