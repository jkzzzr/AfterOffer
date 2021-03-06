package my;

import java.io.File;
import java.util.Hashtable;

import p1.LC;
import p1.LCHeuristic;
import p1.TempUtils;

public class test_2014 {
	public static void main(String[] args) throws Exception {
		int year=2014;
		int startquery=251;
		int top=100;
		String norminput="E:\\ws\\ws-1\\Fusion\\2014_choosen";
		String qrelpath="E:\\ws\\ws-1\\Fusion\\2014.qrels-for-ndeval.txt";
		String output="tempfuse";
		Hashtable<String, Integer> system=LC.setSystem(norminput);
		
		LCHeuristic lch=new LCHeuristic(8, 50, startquery, system);
//      NormalizationModel.java
		File[] inputs=new File(norminput).listFiles();
		long start=System.currentTimeMillis();
		lch.setPerformanceFromNdeval(inputs, qrelpath, "ERR-IA@20");
		lch.setDiversity_0(inputs, qrelpath, "ERR-IA@20", top);
		lch.setDissimilarity_distance(inputs, top);
		lch.printWeight(lch.performance);
		lch.printWeight(lch.dissimilarity);
		lch.printWeight(lch.diversity);

		//pvc
		//100, 200,001,002,10,20,101,201,102,110,210,120,211
		int wwweight[] = new int[]{100, 200,001,002,10,20,101,201,102,110,210,120,211,112,111};
		for (int i = 0;i < wwweight.length; i++){
			int a = wwweight[i] /100;
			int b = (wwweight[i]%100)/10;
			int c = (wwweight[i]%10); 
			lch.cobimeFeatureWeight(a,b,c);
	//		lch.printWeights();
			System.err.print(wwweight[i]+"\t");
			lch.LCFusion(norminput, output+"."+year, "fusedresult"+"."+year);
			TempUtils.ndeval_mean(output+"."+year, qrelpath);
			Thread.sleep(10);
		}
		
	

	}
}
