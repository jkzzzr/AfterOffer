package change;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import Structure.Create_Doc2;

public class Int_Str {

	public static String inputPath = "";
	public static String outputPath = "";
	
	public static void main(String[] args) {

	}
	/**
	 * 根据原始的0-499个记录了各个集合中文档号的文件，
	 * 写出对应的String类型的docid
	 * @throws Exception
	 */
	public static void run () throws Exception{
		Create_Doc2 create_Doc2 = new Create_Doc2();
		int AllDocNum = create_Doc2.alldocnumber;
		for (int i = 0; i < 500; i++){
			String input = inputPath + "/" + i;
			BufferedReader br = new BufferedReader(new FileReader(input));
			
			String output = outputPath +"/" + i;
			File file = new File(output);
			if (!file.exists()){
				file.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(output, true));
			
			String line = "";
			while ((line = br.readLine())!=null){
				int docNo = Integer.parseInt(line);
				String docid = create_Doc2.getdocString(docNo);
				bw.write(docid);
			}
			br.close();
			bw.flush();
			bw.close();
		}
		
	}
}
