package my.add;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * 踏马的，AddCombSum-MNZ那个方法，有个bug，它把combmnz写文件时候，也写成了CombSum
 *   现在把每个rusult文件最后一行的combsum换成combmnz
 * @author Administrator
 *
 */
public class DeleteCombsum {

	public static void main(String[] args) throws Exception {
		new DeleteCombsum().fun("F:\\Fusion\\2009\\random_Experiment2\\3\\result");
	}
	
	public void fun(String from) throws Exception{
		RandomAccessFile raf = new RandomAccessFile(new File(from), "rw");
		for (int i = 0; i <17; i++){
			raf.readLine();
		}
		long index = raf.getFilePointer();
		String templine = raf.readLine();
		templine = templine.replace("combsum", "combmnz");
		raf.seek(index );
	/*	byte[]b= new byte[64];
		raf.read(b);
		System.out.println(new String(b));*/
		System.out.println(templine);
		raf.writeUTF("\n"+templine);
		raf.close();
	}

}
