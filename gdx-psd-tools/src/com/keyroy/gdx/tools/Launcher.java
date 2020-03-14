package com.keyroy.gdx.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

public class Launcher {
		
	public static void main(String[] args) {

//		File f=new File("log.txt");  
//        try {
//			f.createNewFile();
//			FileOutputStream fileOutputStream = new FileOutputStream(f);  
//	        PrintStream printStream = new PrintStream(fileOutputStream);  
//	        System.setOut(printStream);  
//	        System.out.println("默认输出到控制台的这一句，输出到了文件 out.txt");  
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
      
        
	    HashMap<String, String> cmds = new HashMap<String, String>();
	    String[] arrayOfString1 = args;
	    int j = args.length;
	    for (int i = 0; i < j; i++) {
			String cmd = arrayOfString1[i];
			try {
			   String[] sp = cmd.split("=");
			   String key = sp[0].trim();
			   String val = sp[1].trim();
			   cmds.put(key, val);
			}
			catch (Exception localException)
			{
			} 
	    }
	    
	    if (cmds.containsKey("xlsx2json")) {
	    	XlsxTools.main(args);
	    }
	    if (cmds.containsKey("i18n")) {
	    	I18nXlsxTools.main(args);
	    }
	    
    	XlsxTools.main(args);
    	
	}
}
