package PreProcess;

import java.io.File;

/**
 * ChangeFileName完了以后呢，
 * 每个文件解压到了自己的文件夹下面，文件名呢，还都是input，所以要全部提取出来
 * @author Administrator
 *
 */
public class GetFile {

	public static void main(String[] args) {
		func2("F:\\Fusion\\2011\\download", "F:\\Fusion\\2011\\original");
	}
	/**
	 * 2009年的数据，每个文件夹的名字是input
	 * @param input
	 * @param output
	 */
	public static void  func2(String input, String output){
		File f = new File(input);
		File files[] = f.listFiles();
		for (File file: files){
			String filename = file.getName();
			File f_from = new File(input+"\\"+filename+"\\input");
			File f_to = new File(output+"\\"+filename);
			f_from.renameTo(f_to);
			System.out.println(filename);
		}
	}
	
	public static void  func3(String input, String output){
		File f = new File(input);
		File files[] = f.listFiles();
		for (File file: files){
			String filename = file.getName();
			File f_from = new File(input+"\\"+filename+"\\input");
			File f_to = new File(output+"\\"+filename);
			f_from.renameTo(f_to);
			System.out.println(filename);
		}
	}

}
