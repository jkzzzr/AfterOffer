package p1;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.zip.GZIPInputStream;

public class NormalizationModel {
	public static double a=33.783;//29.274,a=25.92,b=0.5
	public static double b=0.5;
	/*
	 * 检查并修正runs（不对分数处理）
	 */
	public static void decompressingRuns(File[] f,String decpath) throws Exception, IOException{
		for(int i=0;i<f.length;i++){
			GZIPInputStream gzi = new GZIPInputStream(new FileInputStream(f[i]));	        
	        String fout = f[i].getName(); 
	        int end=fout.length()-3;
	        String name=fout.substring(6, end);
	        BufferedOutputStream bos = new BufferedOutputStream( 
	                                       new FileOutputStream(decpath+name) ); 

	        System.out.println("writing " + fout); 

	        int b; 
	        do { 
	            b = gzi.read(); 
	            if (b==-1) break; 
	            bos.write(b); 
	        } while (true); 
	        gzi.close(); 
	        bos.close(); 
		}
		
	}
	public static void checkError(File[] files) throws Exception{
		for(int i=0;i<files.length;i++){
			boolean tag=true,taga=true;
			BufferedReader reader=new BufferedReader(new FileReader(files[i]));
			String templine=reader.readLine();
			int temprank=Integer.parseInt(templine.split("[ \t]")[3]);
			int tempquery=Integer.parseInt(templine.split("[ \t]")[0]);
			
			if(temprank==0){
				System.err.println(files[i].getName()+" Start from 0");
			}
				
			int line=1;	
			while((templine=reader.readLine())!=null){
				line++;
				String[] items=templine.split("[ \t]");
				int rank=Integer.parseInt(items[3].trim());
				int query=Integer.parseInt(items[0].trim());
	
				if(query==tempquery){
					if(temprank==0 && rank==0){
						System.err.println(files[i].getName()+" duplicated 0");
						break;
					}
					if(rank==temprank+1){
						
					}else{
						System.err.println(files[i].getName()+" Error ranks:"+line+" "+temprank);
						break;
					}
				}else if(query==tempquery+1){
					
				}else{
					System.err.println(files[i].getName()+" Error query:"+line+" "+tempquery);
					break;
				}
				temprank=rank;
				tempquery=query;	
			}
			reader.close();
		}
	}
									
	
	/**
	 * 修正一个结果中的rank(从1开始)，系统名称与文档名不一致，\t分隔，分数不变
	 * 结果按topic排序
	 * @param f
	 * @param fileName
	 * @throws IOException 
	 * @throws IOException 
	 */
	private class result{
		int topic;
		int rank;
		String line;
		public result(int t,int r,String l){
			this.topic=t;
			this.rank=r;
			this.line=l;
		}
	}
	/**
	 * 先根据topic比较，其次根据rank比较
	 * @author hcl
	 *
	 */
	private class resultCompareByRank implements Comparator<result>{
		@Override
		public int compare(result ar, result br) {
			if (ar.topic < br.topic)
			    return -1;
			if (ar.topic > br.topic)
			    return 1;
			return ar.rank - br.rank;
		}
	}
	/**
	 * 根据有效排名排序，针对查询或排名乱序的情况
	 * @param f
	 * @param fileName
	 * @throws Exception 
	 */
	public void correctRankOrder(File f,String fileName) throws Exception{
		ArrayList<result> list=new ArrayList<>();		
		String templine=null;	
		BufferedReader reader=new BufferedReader(new FileReader(f));
		while((templine=reader.readLine())!=null){
			String[] items=templine.split("[ \t]");
			result r=new result(Integer.parseInt(items[0]), Integer.parseInt(items[3]), templine);
			list.add(r);
		}
		reader.close();
		Collections.sort(list, new resultCompareByRank());
		BufferedWriter writer=new BufferedWriter(new FileWriter(fileName));
		for (int i = 1; i < list.size(); i++)
			if (list.get(i).topic == list.get(i-1).topic && list.get(i).rank == list.get(i-1).rank)
				throw new Exception("duplicate rank "+list.get(i).rank+" for topic "+list.get(i).topic+
						" in run file "+f.getName()+" line:"+i);
		int tempquery=-1,temprank=1;
		for (int i = 0; i < list.size(); i++){
			String[] items=list.get(i).line.split("[ \t]");
			if(list.get(i).topic!=tempquery){
				temprank=1;
				tempquery=list.get(i).topic;
			}
			String ii=items[0]+"\tQ0\t"+items[2]+"\t"+temprank+"\t"+items[4]+"\t"+f.getName()+"\n";
			writer.write(ii);
			temprank++;
		}
		writer.close();
	}
	
	/**
	 * 根据出现顺序排序,针对排名全为0的情况/从0开始的列表
	 * @param f
	 * @param fileName
	 * @throws IOException
	 */
	public void correctInputs(File f,String fileName) throws IOException{		
		Hashtable<Integer, String> lists=new Hashtable<>();
		BufferedReader reader=new BufferedReader(new FileReader(f));
		String templine=null;
		String temptopic=null;	
		int temprank=1;
		BufferedWriter writer=new BufferedWriter(new FileWriter(fileName));
		while((templine=reader.readLine())!=null){
				String[] items=templine.split("[ \t]");
				if(!items[0].equals(temptopic)){
					temptopic=items[0];
					temprank=1;
					
				}
				String ii=items[0]+"\tQ0\t"+items[2]+"\t"+temprank+"\t"+items[4]+"\t"+f.getName()+"\n";
				temprank++;
				writer.write(ii);			
		}
		reader.close();
		writer.close();
	}
	public void checkErrorInput(String inputdik,String norminputdik) throws IOException{
		File f=new File(inputdik);
		if(f.isFile()){
			correctInputs(f, norminputdik);
		}else{
			File[] files=f.listFiles();
			for(int i=0;i<files.length;i++){
				correctInputs(files[i], norminputdik+files[i].getName());
			}
		}
		
	}
	/**
	 * 获取qrels文件中的所有相关评价信息
	 * @param qrels
	 * @return
	 * @throws IOException
	 */
	public Hashtable<String, Integer> getRel(String qrels) throws IOException{
		Hashtable<String, Integer> rel=new Hashtable<>();
		BufferedReader reader=new BufferedReader(new FileReader(qrels));
		String templine=null;
		while((templine=reader.readLine())!=null){
			String[] items=templine.split(" ");//一个空格、两个空格
			int relevance=Integer.parseInt(items[3].trim());
			if(relevance<0) relevance=0;
			if(relevance!=0){
				rel.put(items[0]+items[2], new Integer(relevance));
			}		
		}

		reader.close();
		return rel;
	}
	public void getDivcount(String qrels) throws IOException{
		int[] subtopics=null;
		BufferedReader reader=new BufferedReader(new FileReader(qrels));
		String templine=null,temptopic="";
		while((templine=reader.readLine())!=null){
			String[] items=templine.split(" ");
			if(!items[0].equals(temptopic)){				
				if(subtopics!=null){
					int sum=0;
					for(int i=0;i<10;i++){
						if(subtopics[i]==1){
							sum++;
						}
					}
					System.out.println(temptopic+","+sum);
				}
				temptopic=items[0];
				subtopics=new int[10];
			}
			int relevance=Integer.parseInt(items[3].trim());
			if(relevance>0){
				int subtopic=Integer.parseInt(items[1].trim());
				subtopics[subtopic]=1;
			}
		}
		if(subtopics!=null){
			int sum=0;
			for(int i=0;i<9;i++){
				if(subtopics[i]==1){
					sum++;
				}
			}
			System.out.println(temptopic+","+sum);
		}
		reader.close();
	}
	public Hashtable<String, Integer> getDivRel(String qrels) throws IOException{
		Hashtable<String, Integer> rel=new Hashtable<>();
		BufferedReader reader=new BufferedReader(new FileReader(qrels));
		String templine=null;
		while((templine=reader.readLine())!=null){
			String[] items=templine.split(" ");//一个空格、两个空格
			int relevance=Integer.parseInt(items[3].trim());
			if(relevance>0){
				relevance=1;
				String key=items[0]+items[2];
				if(rel.containsKey(key)){
					int sum=rel.get(key);
					rel.put(key, new Integer(sum+relevance));
				}else{
					rel.put(items[0]+items[2], new Integer(relevance));
				}		
			}		
		}

		reader.close();
		return rel;
	}
	/**
	 * 计算不同排名位置上的估计分值（Top 1000）for Adhoc
	 * @param qrels
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public double[] computeScores(String qrels, File[] files) throws IOException{
		int count1=0,count2=0,count3=0,count4=0,count5=0;
		Hashtable<String, Integer> rel=getRel(qrels);
		double[] scores=new double[1000];
		int[] nums=new int[1000];
		for(int i=0;i<files.length;i++){
			BufferedReader r=new BufferedReader(new FileReader(files[i]));
			String templine=null;
			while((templine=r.readLine())!=null){
				String[] items=templine.split("\t");
				int rank=Integer.parseInt(items[3])-1;
				if(rank>=1000) 
					continue;
				nums[rank]++;
				if(rel.get(items[0]+items[2])!=null){
					int s=rel.get(items[0]+items[2]);
					scores[rank]+=s;
					if(s==1) count1++;
					if(s==2) count2++;
					if(s==3) count3++;
					if(s==4) count4++;
					if(s==5) count5++;
				}
			}
			r.close();
		}
		System.out.println("count1:"+count1+",  count2:"+count2
				+",  count3:"+count3+",  count4:"+count4
				+",  count5:"+count5);
		for(int i=0;i<1000;i++){
			scores[i]=1.0*scores[i]/1000;//nums[i]
			System.out.println(nums[i]);
		}
		
		return scores;
	}
	/**
	 * 计算不同排名位置上的估计分值（Top 1000）for Ndeval
	 * @param qrels
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public double[] computeDivScores(String qrels, File[] files) throws IOException{
		int count1=0,count2=0,count3=0,count4=0,count5=0,count6=0;
		Hashtable<String, Integer> rel=getDivRel(qrels);
		double[] scores=new double[1000];
		int[] nums=new int[1000];
		for(int i=0;i<files.length;i++){
			BufferedReader r=new BufferedReader(new FileReader(files[i]));
			String templine=null;
			while((templine=r.readLine())!=null){
				String[] items=templine.split("\t");
				int rank=Integer.parseInt(items[3])-1;
				if(rank>=1000) 
					continue;
				nums[rank]++;
				if(rel.get(items[0]+items[2])!=null){
					int s=rel.get(items[0]+items[2]);
					scores[rank]+=s;
					if(s==1) count1++;
					if(s==2) count2++;
					if(s==3) count3++;
					if(s==4) count4++;
					if(s==5) {count5++;}
					if(s==6) {count6++;
//					System.err.println(items[2]);
					}
				}
			}
			r.close();
		}
		System.out.println("count1:"+count1+",  count2:"+count2
				+",  count3:"+count3+",  count4:"+count4
				+",  count5:"+count5+",  count6:"+count6);
		for(int i=0;i<1000;i++){
			scores[i]=1.0*scores[i]/1000;//
			System.out.println(nums[i]);//
		}
		
		return scores;
	}
	/**
	 * 规范化分数：多种；
	 * 1：对数模型max{1-0.2*Math.log(r+1),0}
	 * 2: 倒数模型1.0/(r+60)
	 * 3： 混合模型max{1-0.2*Math.log(r+1),1.0/(r+60)}
	 * @param rank
	 * @param type
	 * @return
	 */
	public double getscore(String rank, int type){
		if(rank.equals("0")) {System.err.println("rank=0");}
    	double score = 0;
    	int r=Integer.parseInt(rank);
    	switch (type) {
		case 1:
			//1-0.2ln
			score=1-0.2*Math.log(r);
			if(score<0) score=0;
			break;
		case 2:
			//1/(r+60)
			score=1.0/(r+60);
			break;
		case 3:
			//mixmodel 0.5*(lnrank_1_0.2+25.92*reciprank_60)
			score=1-0.2*Math.log(r+1);
			if(score<0) score=0;
			score=b*(score+a/(r+60));
//			score=b*score+a/(r+60);
//			score=Math.max(b*score, a/(r+60));
			break;
		default:
			break;
		}
    	return score;
    }
	
	/**
	 * 规范化文件（从rawinput到norminput）
	 * @param year
	 * @param type
	 * @throws IOException
	 */
	 public  void norm(File[] files,String out,int type) throws IOException{
	    for(int i=0;i<files.length;i++){
	    	BufferedReader r = new BufferedReader(new FileReader(files[i]));
	    	BufferedWriter w = new BufferedWriter(new FileWriter(out+files[i].getName()));
	        String templine=null;
	       	while((templine=r.readLine())!=null){
	        	String[] items=templine.split("[ \t]");
	        	try{        	
	        		w.write(items[0]+"\t"+items[1]+"\t"+items[2]+"\t"+items[3]+"\t"+getscore(items[3],type)+"\t"+items[5]+"\n");
	        	}catch(Exception e){
	        		System.err.println(files[i].getName());
	        	}
	        }
	        r.close();
	        w.close();
	    }   	
	 }
	 public  void computeCateScores(String qrels, File[] files) throws Exception, IOException{
		 //对查询进行分类：A类：覆盖子主题少于等于bound的查询
		 ArrayList<String> cateA=new ArrayList<>();
		 String divstat="E:/kuaipan/TREC Fusion Experiment/Score normalization/2016/Div.txt";
		 int cate_bound=3;
		 BufferedReader reader=new BufferedReader(new FileReader(divstat));
		 String templine=null;
		 while((templine=reader.readLine())!=null){
			 String[] items=templine.split("\t");
			 if(Integer.parseInt(items[1])<=cate_bound){
				 cateA.add(items[0]);
			 }
		 }
		 reader.close();
		 //计算两类分数分布
		 Hashtable<String, Integer> rel=getDivRel(qrels);
		 double[] scoresA=new double[1000];
		 double[] scoresB=new double[1000];
		 for(int i=0;i<files.length;i++){
			 BufferedReader r=new BufferedReader(new FileReader(files[i]));
			 while((templine=r.readLine())!=null){
				 String[] items=templine.split("\t");
				 int rank=Integer.parseInt(items[3])-1;
				 if(rank>=1000) 
					 continue;
				 if(rel.get(items[0]+items[2])!=null){
					int s=rel.get(items[0]+items[2]);
					if(cateA.contains(items[0])){
						scoresA[rank]+=s;
					}else{
						scoresB[rank]+=s;
					}
				 }
			 }
			 r.close();
		 }
		 BufferedWriter w=new BufferedWriter(new FileWriter("hh.csv"));
		 for(int i=0;i<1000;i++){
			 System.out.println(scoresA[i]+","+scoresB[i]);
			 w.write(scoresA[i]+","+scoresB[i]+"\n");
		 }		 
		 w.close();
	 }
	public  void statCateDiv() throws Exception, IOException{
		ArrayList<String> cateA=new ArrayList<>();
		BufferedWriter w=new BufferedWriter(new FileWriter("E:/kuaipan/TREC Fusion Experiment/Score normalization/2016/Divstatss.csv"));
		 String divstat="E:/kuaipan/TREC Fusion Experiment/Score normalization/2016/Div.txt";
		 int cate_bound=3;
		 BufferedReader reader=new BufferedReader(new FileReader(divstat));
		 String templine=null;
		 while((templine=reader.readLine())!=null){
			 String[] items=templine.split("\t");
			 if(Integer.parseInt(items[1])<=cate_bound){
				 cateA.add(items[0]);
			 }
		 }
		 reader.close();
		 reader=new BufferedReader(new FileReader("E:/TREC Data/2011webtrack/eval.Combsum.csv"));
		 while((templine=reader.readLine())!=null){
			 String[] items=templine.split(",");
			 if(cateA.contains(items[1])){
				 System.out.println(templine);
				 w.write(templine+"\n");
			 }
		 }
		 reader.close();
		 w.close();
	}
	public static void main(String[] args) throws Exception{
		int year=2001;
		int toprun=20;//取部分结果进行分数估计
		int numofsystem=8;
		
		String workpath="E:/TREC Data/"+year+"webtrack/";
		String input=workpath+year+"adhocruns/";
		String norminput=workpath+year+"Nadhocruns/";
		String temprunslist=workpath+"/tempruns.txt";
		String tempruns=workpath+"/tempruns/";
		String qrel=workpath+year+".qrels-for-ndeval.txt";
		String alleval=workpath+"/alleval.div.csv";
		String relscore=workpath+"/scores.div.csv";
		String fuseresult=workpath+"fusedrun.";
		//1.检查与改正初始结果排序（保持评价结果不变）
		NormalizationModel nm=new NormalizationModel();
		String path=workpath+"adhoc";
//		nm.decompressingRuns(new File(path).listFiles(), input);
		//先检查
		NormalizationModel.checkError(new File(input).listFiles());
		//对无效排序重排（排名全为0）
//		nm.checkErrorInput(input+"mudvimp",norminput+"mudvimp.nn");
		//对乱序的重排（根据有效排名排序，针对查询或排名乱序的情况）
//		File[] files=new File(input).listFiles();
//		for(int i=0;i<files.length;i++){
//			nm.correctRankOrder(files[i],norminput+files[i].getName());//
//			Ndeval eval=new Ndeval();
//			eval.evaluate(files[i], qrel);
//			eval=new Ndeval();
//			eval.evaluate(new File(norminput+files[i].getName()), qrel);
//		}
//		
//		Ndeval eval=new Ndeval();
//		eval.evaluate(new File(input+"wistud.runB"), workpath+"qrels.all.txt");
		
		//2.评估所有runs
		File[] runs=new File(input).listFiles();
//		for(int i=0;i<runs.length;i++){
//			Treceval eval=new Treceval();			
//			eval.trec_eval(qrel, runs[i], "E:/TREC Data/"+year+"webtrack/alleval.csv", new String[] {" "});
//		}
		//for diversity
//		System.out.println("runs,ERR-IA@20,alpha-nDCG@20,MAP-IA,P-IA@20");
//		for(int i=0;i<runs.length;i++){
//			Ndeval eval=new Ndeval();
//			eval.evaluate(runs[i], qrel);
//		}
		
		//3.选取top-20估计分数分布
//		File[] files=new File[toprun];
//		BufferedReader r=new BufferedReader(new FileReader(alleval));
//		String templine=r.readLine();
//		for(int i=0;i<toprun;i++){
//			templine=r.readLine().split(",")[0];
//			files[i]=new File(norminput+templine.trim());
//		}
//		r.close();
		
////		double[] scores=nm.computeScores(qrel, files);
//		double[] scores=nm.computeDivScores(qrel, files);
//		BufferedWriter w=new BufferedWriter(new FileWriter(relscore));
//		for(int i=0;i<scores.length;i++){
//			w.write(scores[i]+"\n");
//		}
//		w.close();
		
		//4.选取融合成员并规范化
		int type=1;
		File[] files=new File[numofsystem];	
		BufferedReader r=new BufferedReader(new FileReader(temprunslist));
		for(int i=0;i<numofsystem;i++){
			String templine=r.readLine();
			files[i]=new File(norminput+templine);
		}
		nm.norm(files, tempruns, type);
		r.close();

		//6.融合
		int startquery=1;
//		File[] inputs=new File(tempruns).listFiles();		
//		Hashtable<String, Integer> systems=LC.setSystem(inputs);
//		CombSUM cbsum=new CombSUM(numofsystem, 50, startquery, systems);
//		cbsum.setStandardlength(1000);
//		cbsum.CombSumFusion(inputs, fuseresult+"Combsum."+type, year+".Combsum."+type);
		
//		CombMNZ cmbmnz=new CombMNZ(numofsystem, 50, startquery, systems);
//		cmbmnz.setStandardlength(1000);
//		cmbmnz.Fusion(inputs, fuseresult+"CombMNZ."+type, year+".CombMNZ."+type);
//		eval=new Ndeval();
//		eval.evaluate(new File(fuseresult+"CombMNZ."+type), qrel);	
		
//		7.评价融合结果
//		Treceval eval=new Treceval();
//		String method="Combsum.";
//		File run=new File(fuseresult+method+type);
//		String outpath=workpath+"eval."+method+type+".csv";
//		eval.trec_eval(qrel, run , outpath, new String[]{"-graded"});
//		
//		eval=new Treceval();
//		method="CombMNZ.";
//		run=new File(fuseresult+method+type);
//		outpath=workpath+"eval."+method+type+".csv";
//		eval.trec_eval(qrel, run , outpath, new String[]{"-graded"});
		
		//DIV
//		NormalizationModel.getDivcount(qrel);
//		nm.computeCateScores(qrel, files);
//		Ndeval eval=new Ndeval();
//		eval.evaluateALL(new File(fuseresult+"Combsum."+type), qrel);
//		nm.statCateDiv();
	}
}

