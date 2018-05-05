package edu.brown.cs.mldm.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class Reader {
	public void readFiles(String filename, Map<String, String> map){
		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"))){	
		String str;
		 while ((str = reader.readLine()) != null) {
			 String[] wordsinLine = str.split("\\s+");
			 String name = "";
			 for (int i = 0; i < wordsinLine.length-1; i++) {
				 if(i==0){
					 name = name + wordsinLine[i]; 
				 } else{
					 name = name + " " + wordsinLine[i]; 
				 }
				
			 }
			 map.put(wordsinLine[wordsinLine.length-1], name);
		 }
		 return;
	} catch (UnsupportedEncodingException e) {
		//e.printStackTrace();
		return;
	} catch (FileNotFoundException e) {
		//e.printStackTrace();
		return;
	} catch (IOException e) {
		//e.printStackTrace();
		return;
	
	}
	
}
}
