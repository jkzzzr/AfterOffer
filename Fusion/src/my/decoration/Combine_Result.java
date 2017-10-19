package my.decoration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import p1.Ndeval;

public class Combine_Result {

	private int year ;
	private String Path_base = "";
//	private String Path_comb = "";
	private String qrelfile = "";
/*	private HashMap<String, Double> ERRIAMap = new HashMap<String, Double>();
	private HashMap<String, Double> aNDCGMap = new HashMap<String, Double>();*/
	private static boolean erria = true;
	public static void main(String[] args) throws Exception {
		Combine_Result combine_Result = new Combine_Result();
		//andcg和erria两次执行
		for (int xx = 0; xx < 2; xx++){
			erria =  !erria;
			for (int ss = 2009; ss <=2011;ss++){
				combine_Result.clear(ss);
				combine_Result.fun2_Write(combine_Result.func2(erria), erria);
				System.out.println("year:\t"+ss+"\t"+ (erria?"erria":"ndcg"));
			}
		}
	}
	
	public void clear(int year) throws Exception{
		this.year = year;
		this.Path_base = "F:\\Fusion\\" + year + "\\random_Experiment";
		this.qrelfile = "F:\\Fusion\\" + year + "\\qrels.final-format";
	//	preEvluateBest_ALL();
	//	this.Path_comb = 
	}
	
	/**
	 * 将某个randomno下的CombSum结果和combmnz结果评价，并将评价结果写到它的result下
	 * @param rand_no
	 * @throws Exception
	 */
	public void func1(int rand_no) throws Exception{
		String combmnz = Path_base + "\\" + rand_no + "\\file_choosen" +"\\combmnz-"+year + "-" + rand_no;
		String combsum = Path_base + "\\" + rand_no + "\\file_choosen" +"\\combsum-"+year + "-" + rand_no;
		String resultPath = Path_base + "\\" + rand_no +"\\result";
		BufferedWriter bw = new BufferedWriter(new FileWriter(resultPath, true));
		double d1[] = Ndeval.run(year, combmnz, qrelfile);
		double d2[] = Ndeval.run(year, combsum, qrelfile);
		bw.write("combsum\t"+d2[2]+"\t"+d2[11]+"\t\n");
		bw.write("combmnz\t"+d1[2]+"\t"+d1[11]+"\t\n");
		bw.close();
	}

	/**
	 * 将某一年下的所有result结果，合并到一起
	 * @param b 为真，则保存第一个数据err-ia，为假则保存第二个数据andcg
	 * @return
	 * @throws IOException
	 */
	public ArrayList<String> func2(boolean b) throws IOException{
		ArrayList<String> result = new ArrayList<String>();
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		for (int i = 3; i <=20; i++){
			String inputFile = this.Path_base + "\\" + i +"\\result";
			BufferedReader bReader =new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "utf-8"));
			String systemNameLie = bReader.readLine();
	//		double best[] = EvluateBest(systemNameLie);
			String temp = "";
			boolean b_combmnz = false;
			while ((temp = bReader.readLine())!=null){
				StringTokenizer st = new StringTokenizer(temp);
				String combineType = st.nextToken();
				if (b_combmnz){
					combineType = "combmnz";
					b_combmnz = false;
				}
				if (combineType.equals("combsum")){
					b_combmnz = true;
				}
				int linenumber =0;
				//先查找是哪一个组合
				if (!hm.containsKey(combineType)){
					hm.put(combineType, result.size());
					result.add(combineType);
					linenumber = result.size() - 1;
				}else {
					linenumber = hm.get(combineType);
				}
				double score = 0.0;
				if (b){
					score = Double.parseDouble(st.nextToken());
				}else {
					st.nextToken();//跳过erria结果
					score = Double.parseDouble(st.nextToken());
				}
				String tempString = result.get(linenumber);
				result.set(linenumber, tempString + "\t"+ score);
			}
			
			//添加best
			
			bReader.close();
		}
		return result;
	}
	
	/**
	 * 
	 * @param list
	 * @param b 为真表示，处理的是erria
	 * @throws IOException
	 */
	public void fun2_Write(ArrayList<String> list, boolean b) throws IOException{
		String summaryPath = "F:\\Fusion\\" + year +"\\summary."+year+".";
		if (b){
			summaryPath += "erria";
		}else {
			summaryPath += "andcg";
		}
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(summaryPath, true));
		for (int i = 0; i < list.size();i++){
			bw.write(list.get(i) + "\t"+ computeAvg(list.get(i))+"\n");
		}
		bw.close();
	}
	
	public double computeAvg(String line){
		String[]strings = line.split("\t");
		double sum = 0.0;
		for (int i = 1; i < strings.length;i++){
			sum += Double.parseDouble(strings[i]);
		}
		return sum/(strings.length -1);
	}
	
	
	/**
	 * func1的包装方法
	 * @throws Exception
	 */
	public static void run1() throws Exception{
		Combine_Result combine_Result = new Combine_Result();
		combine_Result.clear(2011);
		for (int i = 3; i <=20; i++){
			combine_Result.func1(i);
			System.out.println(i);
		}
	}
	
	/**
	 * 
	 * @param systemNameLine
	 * @return [0]errmax, [1] adcg
	 */
	/*public double[] EvluateBest(String systemNameLine){
		String strings[] = systemNameLine.split("\t"); 
		double errMax = 0.0;
		double adcg = 0.0;
		for (String filename:strings){
			double temp = ERRIAMap.get(filename);
			if (temp >errMax){
				temp = errMax;
			}
			temp = aNDCGMap.get(filename);
			if (temp > adcg){
				adcg = temp;
			}
		}
		double []result = {errMax, adcg};
		return result;
	}*/
/*	
	public void preEvluateBest_ALL() throws Exception{
		String filename = "F:\\Fusion\\" + this.year +"\\normalize\\";
		File file = new File(filename);
		File files[] = file.listFiles();
		for (File tempFile:files){
			double d1[] = Ndeval.run(year, filename + tempFile.getName(), qrelfile);
			double s1 = d1[2];
			double s2 = d1[11];
			ERRIAMap.put(tempFile.getName(), s1);
			aNDCGMap.put(tempFile.getName(), s2);
		}
		
	} */
}
