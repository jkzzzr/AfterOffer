package p1;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import p1.Ndeval.rList;



public class TempUtils {
	public static void cosine(double[][] weights){
		int n=weights.length;
		int m=weights[0].length;
		String out="";
		double[][] diss=new double[n][n];
		for(int i=0;i<n;i++){
			for(int j=i+1;j<n;j++){
				double dis=0,va=0,vb=0;
				for(int s=0;s<m;s++){
					dis+=weights[i][s]*weights[j][s];
					va+=Math.pow(weights[i][s], 2);
					vb+=Math.pow(weights[j][s], 2);
				}
				diss[i][j]=dis/(Math.sqrt(va)*Math.sqrt(vb));
			}
		}
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				out+=diss[i][j]+"\t";
			}
			System.out.println(out);
			out="";
		}
	}
	public static void ndeval_mean(String run,String qrel) throws Exception{
		Ndeval n=new Ndeval();
		File f=new File(run);
		ArrayList<rList> rrl=n.processRun(f , false);
		ArrayList<rList> qrl=n.processQrels(qrel);
		n.applyQrels(qrl, rrl);
		double[] eval=n.statisticalAverage(n.statisticalAll(rrl, null));
/*//		System.out.println("run,ERR-IA@20, alpha-NDCG@20, MAP-IA, P-IA@20");
		System.out.println(f.getName()+"\t"+eval[2]+"\t"+eval[11]+"\t"+eval[14]+"\t"+eval[15]);*/
		System.out.println(f.getName()+"\t"+eval[2]+"\t"+eval[11]);
	}
	
	public static double[] MY_ndeval_mean(String run,String qrel) throws Exception{
		Ndeval n=new Ndeval();
		File f=new File(run);
		ArrayList<rList> rrl=n.processRun(f , false);
		ArrayList<rList> qrl=n.processQrels(qrel);
		n.applyQrels(qrl, rrl);
		double[] eval=n.statisticalAverage(n.statisticalAll(rrl, null));
/*//		System.out.println("run,ERR-IA@20, alpha-NDCG@20, MAP-IA, P-IA@20");
		System.out.println(f.getName()+"\t"+eval[2]+"\t"+eval[11]+"\t"+eval[14]+"\t"+eval[15]);*/
	//	System.out.println(f.getName()+"\t"+eval[2]+"\t"+eval[11]);
		return eval;
	}
	
	public static void ndevalinputs() throws Exception{
		int year=2011;
		String path="E:/TREC Data/2011webtrack/2011Nadhocruns/";
		String qrelpath="E:/kuaipan/TREC Fusion Experiment/Fusiondata_3years/"+year+"/qrels-for-ndeval."+year+".txt";
		FileWriter w=new FileWriter("E:/TREC Data/"+year+"adhoc.ndeval.csv");
		File[] runs=new File(path).listFiles();
		for(int i=0;i<runs.length;i++){
			Ndeval n=new Ndeval();
			ArrayList<rList> rrl=n.processRun(runs[i], false);
			ArrayList<rList> qrl=n.processQrels(qrelpath);
			n.applyQrels(qrl, rrl);
			double[] eval=n.statisticalAverage(n.statisticalAll(rrl, null));
			w.write(runs[i].getName()+","+eval[2]+","+eval[11]+","+eval[14]+","+eval[15]+"\n");
		}
		w.close();
	}
	public static void renameInputs() throws IOException{
		String path="E:/TREC Data/2010webtrack/adhoc/adhoc";
		String newpath="E:/TREC Data/2010webtrack/2010adhocruns/";
		File[] runsdir=new File(path).listFiles();
		for(int i=0;i<runsdir.length;i++){
			File f=runsdir[i].listFiles()[0];
			BufferedReader r=new BufferedReader(new FileReader(f));
			String sysname=r.readLine().split("[ \t]")[5];
			r.close();
			f.renameTo(new File(newpath+sysname));
			
		}
	}
	public static void normalizeInputs() throws IOException{
		String path="E:/TREC Data/2010webtrack/2010adhocruns";
		String outpath="E:/TREC Data/2010webtrack/2010Nadhocruns/";
		File[] runs=new File(path).listFiles();
		for(int i=5;i<runs.length;i++){
			System.out.println(runs[i].getName());
			LC lc=new LC(1, 50, 0, null);
			LC.normAinput(runs[i], outpath+runs[i].getName(), 3);
		}
	}
	//Combsum fuse 2009 adhoc 5/10 components
	public static void testFuse() throws Exception{
		int year=2009;
		String path="E:/TREC Data/2009webtrack/2009Nadhocruns/";
		String qrelpath="E:/kuaipan/TREC Fusion Experiment/Fusiondata_3years/"+year+"/qrels-for-ndeval."+year+".txt";
		String temprun="tempfuse5";
//		String[] runsname={"MSRAAF","MS2","uogTrdphCEwP","ICTNETADRun4","UMHOOsd"};
		String[] runsname={"UCDSIFTinter","ICTNETADRun4","NeuLMWeb600","MS2","uogTrdphCEwP","UDWAxQEWeb","udelIndDRSP",
				"UMHOOsd","MSRANORM","THUIR09TxAn"};
		File[] runs=new File[runsname.length];
		for(int i=0;i<runsname.length;i++){
			runs[i]=new File(path+runsname[i]);
		}
		Hashtable<String, Integer> sys=LC.setSystem(runs);
		CombSUM cs=new CombSUM(runs.length, 50, 1, sys);
		cs.CombSumFusion(runs, temprun, "cs");
		
		Ndeval n=new Ndeval();
		ArrayList<rList> rrl=n.processRun(new File(temprun), false);
		ArrayList<rList> qrl=n.processQrels(qrelpath);
		n.applyQrels(qrl, rrl);
		double[] eval=n.statisticalAverage(n.statisticalAll(rrl, null));
		System.out.print(eval[2]+","+eval[11]+","+eval[14]+","+eval[15]+"\n");	
	}
	public static void computeInputsSim(int year) throws IOException{
		String path="E:/TREC Data/"+year+"webtrack/"+year+"Nruns/";
		File[] runs=new File(path).listFiles();
		double[][] dis=new double[runs.length][runs.length];
		int startquery=(year-2009)*50+1;
		Hashtable<String, Integer> sys=LC.setSystem(runs);
		LCHeuristic lc=new LCHeuristic(runs.length, 50, startquery, sys);
		double[][][] diss=lc.setDissimilarity_distance(runs, 150);
		for(int i=0;i<runs.length;i++){
			for(int j=i+1;j<runs.length;j++){
				for(int q=0;q<50;q++){
					dis[i][j]+=diss[i][j][q];
				}
				dis[i][j]/=50;
				if(i!=j && dis[i][j]<0.5){
					System.out.println(runs[i].getName()+","+runs[j].getName()+","+dis[i][j]);
				}
			}
		}
	}
	public static void listName() throws IOException{
		String s="arsc09web ICTNETADRun3 ICTNETADRun4 ICTNETADRun5 IE09 irra1a irra2a irra3a MS1 MS2 MSRAAF MSRAC MSRANORM muadanchor muadibm5 muadimp NeuLMWeb600 NeuLMWebBase pkuLink pkuSewmTp pkuStruct RmitLm RmitOkapi Sab9wtBase Sab9wtBf1 Sab9wtBf2 scutrun1 scutrun2 scutrun3 SIEL09 THUIR09An THUIR09LuTA THUIR09TxAn twCSrs9N twCSrsR twJ48rsU UamsAw7an3 UamsAwebQE10 UCDSIFTinter UCDSIFTprob UCDSIFTslide udelIndDMRM udelIndDRPR udelIndDRSP UDWAxBL UDWAxQE UDWAxQEWeb UMHOObm25B UMHOObm25GS UMHOObm25IF UMHOOqlB UMHOOqlGS UMHOOqlIF UMHOOsd UMHOOsdp uogTrdphA uogTrdphCEwP uogTrdphP uvaee uvamrf uvamrftop watprf watrrfw WatSdmrm3 WatSdmrm3we WatSql watwp yhooumd09BFM yhooumd09BGC yhooumd09BGM";
		FileWriter w=new FileWriter("C:/Users/hcl/Desktop/new  1.csv");
		String[] items=s.split(" ");
		for(int i=0;i<items.length;i++){
			w.write(items[i]+",");
		}
		w.write("\n");
		for(int i=0;i<items.length;i++){
			w.write(items[i]+"\n");
		}
		w.close();
	}
	public static void checkQreladhocndeval() throws IOException{
		int startqid=101;
		String adhoc="E:/kuaipan/TREC Fusion Experiment/Fusiondata_3years/2011/qrels-for-adhoc.2011.txt";
		String ndeval="E:/kuaipan/TREC Fusion Experiment/Fusiondata_3years/2011/qrels-for-ndeval.2011.txt";
		String out="E:/kuaipan/TREC Fusion Experiment/Fusiondata_3years/2011/attach adhoc and ndeval.csv";
		ArrayList<Hashtable<String, int[]>> qrels=new ArrayList<>();
		BufferedReader r=new BufferedReader(new FileReader(adhoc));
		FileWriter w=new FileWriter(out);
		String templine="";
		String tempquery="";
		Hashtable<String, int[]> qrel=null;
		while((templine=r.readLine())!=null){
			String[] items=templine.split(" ");
			if(!items[0].equals(tempquery)){
				if(qrel!=null)
					qrels.add(qrel);
				qrel=new Hashtable<>();
				tempquery=items[0];
			}
			if(!items[3].equals("0")){
				int[] topics=new int[10];
				topics[0]=1;
				qrel.put(items[2], topics);
			}
		}
		qrels.add(qrel);
		r.close();
		r=new BufferedReader(new FileReader(ndeval));
		while((templine=r.readLine())!=null){
			String[] items=templine.split(" ");
			if(!items[3].equals("0")){
				int qid=Integer.parseInt(items[0])-startqid;
				Hashtable<String, int[]> qr=qrels.get(qid);
				int[] topics;
				if(qr.containsKey(items[2])){
					topics=qr.get(items[2]);	
				}else{
					topics=new int[10];
				}
				topics[Integer.parseInt(items[1])]=1;
				qr.put(items[2], topics);
			}		
		}
		r.close();
		int zs=0,zfs=0,fzs=0,fzfs=0;
		boolean pp=false;
		for(int i=0;i<qrels.size();i++){
			Enumeration<String> docs = qrels.get(i).keys();
			while(docs.hasMoreElements()){
				String docid=docs.nextElement();
				w.write(docid);
				int[] topics=qrels.get(i).get(docid);
				for(int k=0;k<topics.length;k++){
					w.write(","+topics[k]);
					if(k!=0 && topics[k]==1){
						pp=true;
					}
				}
				if(topics[0]==1){
					if(pp){
						zs++;
					}else{
						zfs++;
					}
				}else{
					if(pp){
						fzs++;
					}else{
						fzfs++;
					}
				}	
				pp=false;
				w.write("\n");
			}
			//for each query
			w.write("suma,"+i+","+zs+","+zfs+","+fzs+","+fzfs+"\n");
			zs=zfs=fzs=fzfs=0;
		}
		w.close();
	}
	public static ArrayList<Hashtable<String, int[]>> getQrel(String qrel,int startquery) throws IOException{
		ArrayList<Hashtable<String, int[]>> qrellist=new ArrayList<>();
	    BufferedReader reader=new BufferedReader(new FileReader(qrel));
		String templine=null;
		Hashtable<String, int[]> rel=new Hashtable<>();
		int temptopic=startquery;
		while((templine=reader.readLine())!=null){
			String[] items=templine.split(" ");
			if(Integer.parseInt(items[3])>0){
				int qid=Integer.parseInt(items[0]);
				if(qid!=temptopic){
					qrellist.add(rel);
					rel=new Hashtable<>();
					temptopic++;
					while(qid!=temptopic){
						qrellist.add(null);
						temptopic++;
					}
				}
				int[] subtopics;
				if(rel.get(items[2])==null){
					subtopics=new int[10];
				}else{
					subtopics=rel.get(items[2]);
				}
				subtopics[Integer.parseInt(items[1])]=1;
				rel.put(items[2], subtopics);
			}		
		}
		qrellist.add(rel);
		reader.close();	
		return qrellist;
	}
	
	public static void checkSubtopics(int top) throws IOException{
		int year=2009;
		String run="tempfuse5";
//		String run="E:/TREC Data/2009webtrack/2009Nadhocruns/UCDSIFTinter";
		String qrel="E:/kuaipan/TREC Fusion Experiment/Fusiondata_3years/"+year+"/qrels-for-ndeval."+year+".txt";
		int startquery=(year-2009)*50+1;
		int subqn=0,alln=0;
		ArrayList<Hashtable<String, int[]>> qrellist=getQrel(qrel, startquery);
		
		BufferedReader r=new BufferedReader(new FileReader(run));
		String templine=null;
		while((templine=r.readLine())!=null){
			String[] items=templine.split("[ \t]");
			if(Integer.parseInt(items[3])<=top){
				Hashtable<String, int[]> rel=qrellist.get(Integer.parseInt(items[0])-startquery);
				if(rel==null){
					continue;
				}else{
					int[] subtopics=rel.get(items[2]);
					int subn=0;
					if(subtopics!=null){
						for(int i=0;i<subtopics.length;i++){
							if(subtopics[i]>0){
								subn++;
							}
						}					
					}
					if(subn>0){
						subqn++;
						alln+=subn;
					}
				}
			}		
		}
		r.close();
		System.out.println("num of rel docs:"+subqn+" "+alln);
	}
	public static void attachList(Hashtable<String, Integer> doclist,File input) throws IOException{
		BufferedReader r=new BufferedReader(new FileReader(input));
		String templine="";
		while((templine=r.readLine())!=null){
			String[] items=templine.split("[ \t]");
			doclist.put(items[2], new Integer(0));
		}
		r.close();
	}
	public static void statsDocs() throws IOException{
		ArrayList<File> inputs=new ArrayList<>();
		for(int year=2009;year<=2011;year++){
			String input1="E:/TREC Data/"+year+"webtrack/"+year+"adhocruns";
			String input2="E:/TREC Data/"+year+"webtrack/"+year+"divruns";
			File[] in=new File(input1).listFiles();
			for(File f:in){
				inputs.add(f);
			}
			in=new File(input2).listFiles();
			for(File f:in){
				inputs.add(f);
			}
		}
		System.out.println(inputs.size());
		String output="E:/TREC Data/09-11docs(adhocdiv).txt";
		Hashtable<String, Integer> doclist=new Hashtable<>();
		for(int i=0;i<inputs.size();i++){
			System.out.println("====dealing with "+(i+1)+" file "+inputs.get(i).getName()+"===");
			attachList(doclist, inputs.get(i));
		}
		
		int i=0;
		FileWriter w=new FileWriter(output);
		Enumeration<String> keys=doclist.keys();
		while(keys.hasMoreElements()){
			String doc=keys.nextElement();
			w.write(doc+"\n");
			i++;
		}
		w.close();
		System.out.println("total docs:"+i);
	}
	public static double computeCoverage(int[] a,int[] b){
		int n_a=0,n_b=0,n_sim=0;
		for(int i=0;i<a.length;i++){
			for(int j=0;j<b.length;j++){
				if(a[i]==b[j]){
					n_sim++;
				}
			}
		}
		n_a=a.length-n_sim;
		n_b=b.length-n_sim;
		double score=1.0*n_a*n_b*(n_a+n_b+n_sim)/(n_sim+1)/5;
		score=n_a*n_b*(n_a+n_b+n_sim)/30.0;
		return score;
	}
	public static void subtest() throws IOException{
		ArrayList<int[]> sets=new ArrayList<>();
		BufferedReader r=new BufferedReader(new FileReader("tempsubsets"));
		String templine=null;
		while((templine=r.readLine())!=null){
			String[] items=templine.split(",");
			int[] set=new int[items.length];
			for(int i=0;i<items.length;i++){
				set[i]=Integer.parseInt(items[i]);
			}
			sets.add(set);
		}
		r.close();
		FileWriter w=new FileWriter("temp.csv");
		for(int i=0;i<sets.size();i++){
			for(int j=i+1;j<sets.size();j++){
				double score=computeCoverage(sets.get(i), sets.get(j));
				String line="{";
				for(int k:sets.get(i)){
					line+=k+" ";
				}
				line+="} &{";
				for(int k:sets.get(j)){
					line+=k+" ";
				}
				line+="},"+score+"\n";
				w.write(line);
			}
		}
		w.close();
	}
	public static void main(String[] args) throws Exception{
		//����������
//		TempUtils.testFuse();
//		TempUtils.checkSubtopics(100);
//		TempUtils.listName();
//		TempUtils.statsDocs();
//		TempUtils.subtest();
		/*
		int year=2001;
		String qrel="E:/TREC Data/"+year+"webtrack/qrels-for-adhoc.txt";
		BufferedReader r=new BufferedReader(new FileReader("E:/TREC Data/"+year+"webtrack/norminput.txt"));
    	for(int i=0;i<8;i++){
    		String templine=r.readLine();
    		File run=new File("E:/TREC Data/"+year+"webtrack/"+year+"webtrack.adhoc.n/"+templine.trim());
    		String outpath="E:/TREC Data/"+year+"webtrack/eval.component.csv";
    		Treceval eval=new Treceval();
    		eval.trec_eval(qrel, run, outpath, new String[]{"-graded"});
    	}
    	r.close();
    	*/
		int year=2009;   
		String	norminput="E:/kuaipan/TREC Fusion Experiment/Fusiondata_3years/"+year+"/divinput.n/";	
		String	qrel="E:/kuaipan/TREC Fusion Experiment/Fusiondata_3years/"+year+"/qrels-for-ndeval."+year+".txt";
		int top=100;		
//		TempUtils.ndeval_mean(norminput+"mudvimp", qrel);	
//		TempUtils.ndeval_mean(norminput+"mudvimp.nn", qrel);
		File[] inputs=new File[3];
		inputs[0]=new File(norminput+"MSRAACSF");
		inputs[1]=new File(norminput+"UamsDancTFb1");
		inputs[2]=new File(norminput+"mudvimp");
		Hashtable<String, Integer> systems=LC.setSystem(inputs);
		
		CombSUM cbsum=new CombSUM(3, 50, 1, systems);
		cbsum.setStandardlength(1000);
		cbsum.CombSumFusion(inputs, "temp", "temp");
		TempUtils.ndeval_mean("temp", qrel);
		
		CombMNZ cmbmnz=new CombMNZ(3, 50, 1, systems);
		cmbmnz.setStandardlength(1000);
		cmbmnz.Fusion(inputs, "temp", "temp");
		TempUtils.ndeval_mean("temp", qrel);
		
		LCHeuristic lch=new LCHeuristic(3, 50, (year-2009)*50+1, systems);
		lch.setPerformanceFromNdeval(inputs, qrel, "ERR-IA@20");
		lch.setDissimilarity_distance(inputs, top);//dis
		lch.setDiversity_0(inputs, qrel, "ERR-IA@20", top);//complementarity_pair-wise
		lch.cobimeFeatureWeight(1,0,0);
		lch.printWeight(lch.performance);
		lch.printWeight(lch.diversity);
		lch.printWeight(lch.dissimilarity);
		lch.LCFusion(norminput, "temp", "fusedresult"+"."+year);
		TempUtils.ndeval_mean("temp", qrel);
		
		lch.cobimeFeatureWeight(2,0,0);
		lch.LCFusion(norminput, "temp", "fusedresult"+"."+year);
		TempUtils.ndeval_mean("temp", qrel);
		
		lch.cobimeFeatureWeight(0,0,1);
		lch.LCFusion(norminput, "temp", "fusedresult"+"."+year);
		TempUtils.ndeval_mean("temp", qrel);

		lch.cobimeFeatureWeight(0,0,2);
		lch.LCFusion(norminput, "temp", "fusedresult"+"."+year);
		TempUtils.ndeval_mean("temp", qrel);

		lch.cobimeFeatureWeight(0,1,0);
		lch.LCFusion(norminput, "temp", "fusedresult"+"."+year);
		TempUtils.ndeval_mean("temp", qrel);

		lch.cobimeFeatureWeight(0,2,0);
		lch.LCFusion(norminput, "temp", "fusedresult"+"."+year);
		TempUtils.ndeval_mean("temp", qrel);

		lch.cobimeFeatureWeight(1,0,1);
		lch.LCFusion(norminput, "temp", "fusedresult"+"."+year);
		TempUtils.ndeval_mean("temp", qrel);

		lch.cobimeFeatureWeight(1,0,2);
		lch.LCFusion(norminput, "temp", "fusedresult"+"."+year);
		TempUtils.ndeval_mean("temp", qrel);
		
		lch.cobimeFeatureWeight(1,1,1);
		lch.LCFusion(norminput, "temp", "fusedresult"+"."+year);
		TempUtils.ndeval_mean("temp", qrel);
			
	}
}
