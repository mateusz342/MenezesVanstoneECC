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

import javax.sound.sampled.AudioFormat.Encoding;

public class MVECCmain {
	public static void main(String[] args) throws IOException {
		EllipticCurveMV algorithm=new EllipticCurveMV();
		BigInteger p=algorithm.generateP();
		//Point generator=new Point(new BigInteger("257015440179535"),new BigInteger("787265208059509"));//algorithm.thebasepoint(p);
		Point generator=algorithm.thepoint(p);
		System.out.println(generator);
		BigInteger privkey;
		do{
		privkey=new BigInteger(p.bitCount()-1,new SecureRandom());
		}while(privkey.equals(0));
		Point pubkey=algorithm.generatePublicKey(generator,privkey);
		//String text="To jest test";
		//byte[] tocipher=text.getBytes();
		byte[] bytes=algorithm.readDocument();
		ArrayList<BigInteger> result=algorithm.encrypt(bytes,pubkey,generator);
		System.out.print("Ciphertext:");
		byte[] ciphertext=new byte[result.size()];
		for(int i=0;i<result.size();i++){
			ciphertext[i]=result.get(i).byteValue();
		}
		String finalciphertext=new String(ciphertext,StandardCharsets.UTF_8);
		System.out.println(finalciphertext);
		byte[] plaintext=algorithm.decrypt(result, privkey,generator);
		String finalresult = new String(plaintext, StandardCharsets.UTF_8);
		System.out.println("Output:"+finalresult);
		
		//write file
		BufferedWriter writer = null;
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
