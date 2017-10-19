package my.add;

import java.io.BufferedWriter;
import java.io.FileWriter;

import p1.Ndeval;

public class AddCombSum_MNZ {

	private int year = 0;
	private String inputPath = "F:\\Fusion";
	private String path_qid_filechoosen="";
	private String qrelfile = "";
	public static void main(String[] args) throws Exception {
		new AddCombSum_MNZ().run();
	}
	
	
	public void run() throws Exception{
		for (int yea = 2009; yea <= 2011; yea++){
			clear(yea);
			for (int ran = 3; ran <=20;ran++){
				fun(ran);
			}
			
			
		}
		
		
	}
	
	public void clear(int year){
		this.year = year;
		this.path_qid_filechoosen = this.inputPath +"\\" +year +"\\random_Experiment";
		this.qrelfile = this.inputPath +"\\" +year +"\\qrels.final-format";
	}
	
	public void fun(int randNo) throws Exception{
		String resultPath = this.path_qid_filechoosen + "\\"+randNo + "\\result";
		BufferedWriter bw = new BufferedWriter(new FileWriter(resultPath, true));
		
		String filePath = this.path_qid_filechoosen + "\\"+randNo 
				+"\\file_choosen";
		double d[] = Ndeval.run(this.year, filePath + "\\combsum-" + year +"-" +randNo, this.qrelfile);
		bw.write("\ncombsum\t" + d[2]+"\t"+d[11] +"\n");
		
		filePath = this.path_qid_filechoosen + "\\"+randNo 
				+"\\file_choosen";
		d = Ndeval.run(this.year, filePath + "\\combmnz-" + year +"-" +randNo, this.qrelfile);
		bw.write("combmnz\t" + d[2]+"\t"+d[11] +"\n");
		bw.close();
		
	}

}
