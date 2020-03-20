package com.keyroy.gdx.tools;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Logcat {

	public Logcat(String path){
		  try {    
              
	            PrintStream print=new PrintStream(path);  //写好输出位置文件；  
	            System.setOut(print);
	            System.setErr(print);
	        } catch (FileNotFoundException e) {    
	            e.printStackTrace();    
	        }    
	}
}
