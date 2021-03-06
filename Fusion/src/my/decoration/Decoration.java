package my.decoration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

import my.My_combSum_MNZ;
import p1.LC;
import p1.LCHeuristic;
import p1.Ndeval;
import p1.TempUtils;

public class Decoration {

	public static int year = -1;
	public static int rand_NO = -1;
	
	public static String basePath = "F:\\Fusion";
	private static HashMap<String, Double> ERRIAMap = new HashMap<String, Double>();
	private static HashMap<String, Double> aNDCGMap = new HashMap<String, Double>();
	
	public static String qrelpath = "F:\\Fusion\\"+ year +"\\qrels.final-format";
	
	public static String file_from_String = basePath +"\\"+year+"\\normalize"; 
	public static String file_choosen = basePath + "\\"+year + "\\random_Experiment\\"+ rand_NO +"\\file_choosen";
	
	public static String weightDirectory = basePath + "\\"+year + "\\random_Experiment\\"+ rand_NO + "\\weight";
	public static String resultPath = basePath + "\\"+year + "\\random_Experiment\\"+ rand_NO + "\\result";
	/**
	 * 原来的位置
	 */
	public static HashSet<File> fileFrom = new HashSet<File>();
	/**
	 * 写到的文件夹中
	 */
//	public static HashSet<File> fileTo = new HashSet<File>();
	

	public static void main(String[] args) throws Exception {
		Decoration.year = 2009;
		initYear();
		preEvluateBest_ALL();
		
		for (int i = 3; i <=20;i++){
			System.out.println(year+"\t"+i);
			Decoration.rand_NO = i;
			init();
			preProcess(year);
			func1();
	//		My_combSum_MNZ.combbb(i, (Decoration.year - 2009) * 50 +1, Decoration.file_choosen, "-"+Decoration.year+"-"+Decoration.rand_NO);
			My_combSum_MNZ.combbb(i, (Decoration.year - 2009) * 50 +1, Decoration.file_choosen, "-"+Decoration.year+"-"+Decoration.rand_NO
					, SetToArray(Decoration.fileFrom));
				
			EvluateBest();
	//		writeBack();
		}
	}
	public static void writeEval() throws Exception{
		double[] d = Ndeval.run(Decoration.year, "-"+Decoration.year+"-"+Decoration.rand_NO, Decoration.qrelpath);
	}
	public static void initYear(){
		qrelpath = "F:\\Fusion\\"+ year +"\\qrels.final-format";
		file_from_String = basePath +"\\"+year+"\\normalize"; 
	}
	public static void init(){
		
		file_choosen = basePath + "\\"+year + "\\random_Experiment\\"+ rand_NO +"\\file_choosen";
		
		weightDirectory = basePath + "\\"+year + "\\random_Experiment\\"+ rand_NO + "\\weight";
		resultPath = basePath + "\\"+year + "\\random_Experiment\\"+ rand_NO + "\\result";
	}
	
	/**
	 * 将选中的文件，写入到文件夹下。fileFrom,fileTo
	 * 
	 * 
	 * 在结果文件中，先写下系统名字
	 * @throws Exception 
	 */
	public static void preProcess(int year) throws Exception{
		clear(year);
		
		File file_from = new File(file_from_String);
		File[] files = file_from.listFiles();
		ArrayList<File> fileList = new ArrayList<File>(Arrays.asList(files));
		
		Decoration.fileFrom = new HashSet<File>();
		while (Decoration.fileFrom.size() < Decoration.rand_NO){
			int index = (int)Math.floor(Math.random() * fileList.size());
			Decoration.fileFrom.add(fileList.get(index));
			fileList.remove(index);
		}
		
		//写文件  *****************

/*		for (File tempFileFrom :Decoration.fileFrom){
			String fileNameTOString = file_choosen+"\\"+ tempFileFrom.getName() ;
			File tempFileTO = new File(fileNameTOString);
			tempFileFrom.renameTo(tempFileTO);
			Decoration.fileTo.add(tempFileTO);
		}*/
		//****************************
		//写下系统名字，也就是文件名字
		BufferedWriter bw = new BufferedWriter(new FileWriter(resultPath, true));
		for (File tempFileGetNameFile : Decoration.fileFrom){
			bw.write(tempFileGetNameFile.getName()+"\t");
		}
		bw.write("\n");
		bw.close();
		
	}
	
	private static void clear(int year) throws Exception {
		Decoration.year = year;
		Decoration.fileFrom.clear();
	//	Decoration.fileTo.clear();
		File f0 = new File(basePath + "\\"+year + "\\random_Experiment\\"+ rand_NO);
		if (!f0.exists() || !f0.isDirectory()){
			f0.mkdir();
		}
		File f1 = new File(Decoration.file_choosen);
		if (!f1.exists() || !f1.isDirectory()){
			f1.mkdir();
		}
		File f2 = new File(Decoration.weightDirectory);
		if (!f2.exists() || !f2.isDirectory()){
			f2.mkdir();
		}
		File f3 = new File(Decoration.resultPath);
		if (!f3.exists() || !f3.isDirectory()){
			f3.createNewFile();
		}
		
	}

/*	public static void writeBack(){
		for (File tempFileTo :Decoration.fileTo){
			String fileNamefromString = file_from_String+"\\"+ tempFileTo.getName() ;
			File tempFileFrom = new File(fileNamefromString);
			tempFileTo.renameTo(tempFileFrom);
		}
	}*/

	public static void func1() throws Exception{
		int year = Decoration.year;
		int startquery = (year - 2009) * 50 + 1;
		
		int top=100;
		String output="tempfuse";
		Hashtable<String, Integer> system=LC.setSystem(Decoration.file_choosen);
		
		LCHeuristic lch=new LCHeuristic(Decoration.rand_NO, 50, startquery, system);
//      NormalizationModel.java
//		File[] inputs=new File(Decoration.file_choosen).listFiles();
		File[] inputs= SetToArray(Decoration.fileFrom);
		long start=System.currentTimeMillis();
		lch.setPerformanceFromNdeval(inputs, qrelpath, "ERR-IA@20");
		lch.setDiversity_0(inputs, qrelpath, "ERR-IA@20", top);
		lch.setDissimilarity_distance(inputs, top);
		lch.WriteWeight(lch.performance, weightDirectory+"\\perf");
		lch.WriteWeight(lch.dissimilarity, weightDirectory+"\\dissim");
		lch.WriteWeight(lch.diversity, weightDirectory+"\\diver");
		//pvc
		int wwweight[] = new int[]{100, 200,001,002,10,20,101,201,102,110,210,120,211,112,111};
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(Decoration.resultPath,true));
		
		for (int i = 0;i < wwweight.length; i++){
			int a = wwweight[i] /100;
			int b = (wwweight[i]%100)/10;
			int c = (wwweight[i]%10); 
			lch.cobimeFeatureWeight(a,b,c);
		//	lch.printWeights();
		//	System.err.println(wwweight[i]);
	//		lch.LCFusion(Decoration.file_choosen, output+"."+year, "fusedresult"+"."+year);
			lch.LCFusion(SetToArray(Decoration.fileFrom), output+"."+year, "fusedresult"+"."+year);
			double ddd[] = TempUtils.MY_ndeval_mean(output+"."+year, qrelpath);
			bw.write(wwweight[i]+"\t"+ddd[2]+"\t"+ddd[11]+"\n");
		}
		bw.close();
	
	}

	public static File[] SetToArray(HashSet<File> hashset){
		File[] result = new File[hashset.size()];
		int i =0;
		for (File file :hashset){
			result[i++] = file;
		}
		return result;
	}
	

	public static void preEvluateBest_ALL() throws Exception{
		String filename = Decoration.file_from_String;
		String qrefile = qrelpath;
		File file = new File(filename);
		File files[] = file.listFiles();
		for (File tempFile:files){
			double d1[] = Ndeval.run(year, filename + "\\"+tempFile.getName(), qrefile);
			double s1 = d1[2];
			double s2 = d1[11];
			ERRIAMap.put(tempFile.getName(), s1);
			aNDCGMap.put(tempFile.getName(), s2);
		}
		System.out.println("所有文件评价结束");
	} 
	
	
	public static void EvluateBest() throws IOException{
	//	File files[] = new File(file_choosen).listFiles();
		File files[] = SetToArray(Decoration.fileFrom);
		double errMax = 0.0;
		double adcg = 0.0;
		for (File file:files){
			String filename = file.getName();
			double temp = ERRIAMap.get(filename);
			if (temp >errMax){
				errMax = temp;
			}
			temp = aNDCGMap.get(filename);
			if (temp > adcg){
				adcg = temp;
			}
		}
	
		BufferedWriter bw = new BufferedWriter(new FileWriter(Decoration.resultPath, true));
		bw.write("Best\t"+errMax+"\t"+adcg);
		bw.close();
		return;
	}
	
	
	
}
