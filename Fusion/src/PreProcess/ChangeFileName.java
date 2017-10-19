package PreProcess;

import java.io.File;

/**
 * 下载的文件，都事input.系统.gz名字的，解压后，只有一个input了，不行，要把input。去掉
 * @author Administrator
 *
 */
public class ChangeFileName {

	public static void main(String[] args) {
		fun("F:\\Fusion\\2009\\download");

	}
	public static void fun(String path){
		File f = new File(path);
		File files[] = f.listFiles();
		for (File file: files){
			String filenmaeString = file.getName();
			filenmaeString = path + "\\" + filenmaeString;
			filenmaeString = filenmaeString.replace("input.", "");
			File fff = new File(filenmaeString);
			file.renameTo(fff);
			System.out.println(filenmaeString);
		}
	}

}
