package my.decoration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

public class Test {

	public static void main(String[] args) throws Exception {
		func1();
	}
	
	public static void func1() throws Exception{
		System.err.println("12");
		String inputString = "";
		RandomAccessFile raf = new RandomAccessFile("C:\\Users\\Administrator\\Desktop\\test\\result", "rw");
		String temp = "";
		long[] positions = new long[17];
		int i = 0;
		while ((temp = raf.readLine())!=null){
			positions[i++] = raf.getFilePointer();
		}
		System.out.println(positions.length);
		for (int j = 0; j <positions.length; j++){
			System.out.println(positions[j]+"\t"+j);
		}
		
		
		
		File file = File.createTempFile("temp", null);
		file.deleteOnExit();
		FileInputStream fis = new FileInputStream(file);
		FileOutputStream fos = new FileOutputStream(file);
		raf.seek(positions[0]);
		//将插入点后面的所有内容誊写一遍，到temp中
		byte[] buff = new byte[64];
		int hasRead = 0;
		while ((hasRead = raf.read(buff))>0){
			fos.write(buff);
		}
		
		//再回要插入的点，插入东西
		raf.seek(positions[0]);
		raf.write("我母鸡啊".getBytes());
		//插完之后，将后面的东西再誊写回来
		while ((hasRead = fis.read(buff))>0){
			raf.write(buff, 0, hasRead);
		}
		raf.close();
		fis.close();
		fos.close();
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		raf.close();
	}

}
