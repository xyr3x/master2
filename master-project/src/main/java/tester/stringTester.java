package tester;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class stringTester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Charset encoding = UTF-8;
		
	}
	
	public static int frequency( String source, String part )
	{
	  if ( source == null || source.isEmpty() || part == null || part.isEmpty() )
	    return 0;

	  int count = 0;

	  for ( int pos = 0; (pos = source.indexOf( part, pos )) != -1; count++ )
	    pos += part.length();

	  return count;
	}
	
	public static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}

}
