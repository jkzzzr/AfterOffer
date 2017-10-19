package p1;


import datastruct.*;
import datastruct.QrelsList.QrelsNote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.StringTokenizer;



/**
 * @author hcl
 *
 */
public class LC {
	public static int numofsystem;//number of component systems
	public static int numofquery;//number of query
	public int startquery;//start num of query
	public int de_gid;//groupid for DE train-test
	public static Hashtable<String,Integer> systems=new Hashtable<>();
	public static int standardlength=10000;
	public static String[] bufferline;
	public String bufferqrel;
	public double[][] weights;//5 groups of weights
	public double[] weight;//1 group of weight
	public boolean qrel_adhoc_2009=false;
	
	//-------------------------------------构造函数&成员get/set-------------------------------------------
	public LC(){
		LC.bufferline=new String[numofsystem];
	}
	public LC(int numofsystem,int numofquery,int startquery,Hashtable<String, Integer> sys){
		LC.numofsystem=numofsystem;
		LC.numofquery=numofquery;
		this.startquery=startquery;
		LC.systems=sys;
		this.weights=new double[5][numofsystem];
		LC.bufferline=new String[numofsystem];
		this.bufferqrel=new String();
	}
	//for DE
	public LC(int numofsystem,int numofquery,int startquery,int groupid,Hashtable<String, Integer> sys){
		LC.numofsystem=numofsystem;
		LC.numofquery=numofquery;
		this.startquery=startquery;
		this.de_gid=groupid;
		LC.systems=sys;
		this.weight=new double[numofsystem];
		LC.bufferline=new String[numofsystem];
		this.bufferqrel=new String();
	}
	public int getStandardlength() {
		return standardlength;
	}
	public void setStandardlength(int standardlength) {
		LC.standardlength = standardlength;
	}
	public void setQrelFlag(boolean isSpecial){
		this.qrel_adhoc_2009=isSpecial;
	}
	/**
	 * set system:从rawinput目录下获取所有input代表的系统，给予哈希值，为方便后面融合索引
	 * <p>每个系统一个整数编号：1-numofsystem。哈希表格式（system,sid）(另外对于该数据集对应的相关性评价文件,system=“qrels”,sid=-1)</p>
	 * @param rawinput
	 * @return
	 * @throws IOException
	 */
	public static Hashtable<String, Integer> setSystem(String rawinput) throws IOException{
		File fpath=new File(rawinput);
		File[] files=fpath.listFiles();
		Hashtable<String, Integer> systems=new Hashtable<>();
		systems.put("qrels", -1);
		BufferedReader reader=null;
		for(int i=0;i<files.length;i++){
			reader=new BufferedReader(new FileReader(files[i]));
			StringTokenizer tokenizer=new StringTokenizer(reader.readLine());
			for(int item=1;item<=5;item++) tokenizer.nextToken();
			String sysname=tokenizer.nextToken();
			systems.put(sysname, i);
			System.err.print(sysname+" ");
			reader.close();
		}
		System.out.println(" ");
		return systems;
	}
    /**
     * set system:从rawinput目录下获取<b>部分</b>input代表的系统，给予哈希值，为方便后面融合索引
     * <p>每个系统一个整数编号：1-numofsystem。哈希表格式（system,sid）(另外对于该数据集对应的相关性评价文件,system=“qrels”,sid=-1)</p>
     * @param files 部分input对应的File对象数组
     * @return
     * @throws IOException
     */
	public static Hashtable<String, Integer> setSystem(File[] files) throws IOException{
		Hashtable<String, Integer> systems=new Hashtable<>();
		systems.put("qrels", -1);
		BufferedReader reader=null;
		for(int i=0;i<files.length;i++){
			reader=new BufferedReader(new FileReader(files[i]));
			StringTokenizer tokenizer=new StringTokenizer(reader.readLine());
			for(int item=1;item<=5;item++) tokenizer.nextToken();
			String sysname=tokenizer.nextToken();
			systems.put(sysname, i);
			System.err.print(sysname+" ");
			reader.close();
		}
		System.out.println(" ");
		return systems;
	}
	
	
	//-------------------------------------读取/处理输入文件的一个query部分（input,qrelsinput）--------------------------------------
	/**
	 * 从TREC input文件中获取一个query下的文档排名，存入一个ResultList对象（包含topic，system，文档排名列表（docid,rank,score））
	 * <p>*使用前必须对文件的第一行缓存（缓存到bufferline[sid]）</p>
	 * @param reader 指向input文件的BufferedReader对象
	 * @param sid 当前input文件对应系统的sid,表明缓存行数组的序号（不同sid的缓存行放入一元缓存数组的不同位置）
	 * @return
	 * @throws IOException
	 */
	/**
	 * 从TREC input文件中获取一个query下的文档排名，存入一个ResultList对象（包含topic，system，文档排名列表（docid,rank,score））
	 * <p>*使用前必须对文件的第一行缓存（缓存到bufferline[sid]）</p>
	 * @param reader 指向input文件的BufferedReader对象
	 * @param sid 当前input文件对应系统的sid,表明缓存行数组的序号（不同sid的缓存行放入一元缓存数组的不同位置）
	 * @param isrecord 指明是否需要存入ResultList对象，true则是，否则reader指针指向下一个query的文档，缓存第一个，并不存储
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	public static ResultList getResultList(BufferedReader reader,int sid,boolean isrecord) throws IOException{
		StringTokenizer tokenizer=new StringTokenizer(bufferline[sid]);
		//考察topic
		int topic=Integer.parseInt(tokenizer.nextToken());
		int currenttopic=topic;
		
		if(isrecord){			
			tokenizer.nextToken();
			String docid=tokenizer.nextToken();
			int rank=0;
			try {
				rank=Integer.parseInt(tokenizer.nextToken());
			} catch (Exception e) {
			}
			
			double score=Double.parseDouble(tokenizer.nextToken());
			String system=tokenizer.nextToken();
						
			//construct the result list first item
			ResultList rList=new ResultList(topic,system);
			rList.list.add(new result(docid, rank,score));
			//read more line of the topic until it is different from current topic,and the readed line which is not with current topic have to buffer 
			String templine=null;
			while((templine=reader.readLine())!=null){
				tokenizer=new StringTokenizer(templine);
//				System.out.println(templine);
				topic=Integer.parseInt(tokenizer.nextToken());
				if(topic!=currenttopic){
					bufferline[sid]=new String(templine);
					break;
				}else{
					tokenizer.nextToken();
					docid=tokenizer.nextToken();
					try {
						rank=Integer.parseInt(tokenizer.nextToken());
					} catch (Exception e) {
						System.out.println(sid+" "+topic+" "+rank);
					}
					
					score=Double.parseDouble(tokenizer.nextToken());
					rList.list.add(new result(docid,rank,score));
				}
			}
			return rList;
		}else{
			String templine=null;
			while((templine=reader.readLine())!=null){
				tokenizer=new StringTokenizer(templine);
				topic=Integer.parseInt(tokenizer.nextToken());
				if(topic!=currenttopic){
					bufferline[sid]=new String(templine);
					return null;
				}
			}
		}
		return null;
	}	
	/**
	 * 根据topic获取排名结果（不需要BufferedReader）
	 * @param input
	 * @param topic
	 * @return
	 * @throws IOException
	 */
	public static ResultList getResultList(File input,int topic) throws IOException{
		ResultList rlist=new ResultList();
		BufferedReader reader=new BufferedReader(new FileReader(input));
		String templine=null;
		String sysname = null;
		while((templine=reader.readLine())!=null){
			String[] items=templine.split("[ \t]");
			if(Integer.parseInt(items[0])==topic){
				sysname=items[5];
				rlist.list.add(new result(items[2], Integer.parseInt(items[3]), Double.parseDouble(items[4])));
			}else if(Integer.parseInt(items[0])>topic){
				break;
			}
		}
		rlist.system=sysname;
		rlist.topic=topic;
		reader.close();
		return rlist;
	}
	
	/**
	 * 从TREC 相关性评价文件qrels中获取一个query下的相关文档的信息，存入一个QrelsList对象
	 * <p>*使用前必须对文件的第一行缓存(缓存到bufferqrel)</p>
	 * <p>*不相关的不保存（rel<=0不相关）</p>
	 * @param reader
	 * @param isrecord 为false时表示reader指针指导下一个query处，当前query的结果不保存
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	public QrelsList getQrelsList(BufferedReader reader,boolean isrecord) throws IOException{
		StringTokenizer tokenizer=new StringTokenizer(bufferqrel);
		int topic=Integer.parseInt(tokenizer.nextToken());//qrel
		int currenttopic=topic;
		int subtopic=0;
		if(isrecord){
			QrelsList qList=new QrelsList(topic);
			if(!qrel_adhoc_2009){
				subtopic=Integer.parseInt(tokenizer.nextToken());//subtopic
			}
			String docid=tokenizer.nextToken();//docid
			int relevance=Integer.parseInt(tokenizer.nextToken());//relevance
			if(relevance>0){
				qList.getList().add(new QrelsNote(subtopic, docid, relevance));
			}	
			String templine=null;
			while((templine=reader.readLine())!=null){
				tokenizer=new StringTokenizer(templine);
				topic=Integer.parseInt(tokenizer.nextToken());
				if(topic!=currenttopic){
					bufferqrel=new String(templine);
					break;
				}else{
					if(!qrel_adhoc_2009){
						subtopic=Integer.parseInt(tokenizer.nextToken());//subtopic
					}
					docid=tokenizer.nextToken();
					relevance=Integer.parseInt(tokenizer.nextToken());
					if(relevance>0){
						qList.getList().add(new QrelsNote(subtopic, docid, relevance));
					}	
				}
			}
			return qList;
		}else{
			String templine=null;
			while((templine=reader.readLine())!=null){
				tokenizer=new StringTokenizer(templine);
				topic=Integer.parseInt(tokenizer.nextToken());
				if(topic!=currenttopic){
					bufferqrel=new String(templine);
					return null;
				}
			}
		}
		return null;
		
	}	
	/**
	 * 根据topic获取相关性评价
	 * <p>*不相关的不保存（rel<=0不相关）</p>
	 * @param qrel
	 * @param topic
	 * @return
	 * @throws IOException 
	 */
	public static QrelsList getQrelsList(File qrel,int topic) throws IOException{
		QrelsList qlist=new QrelsList(topic);
		BufferedReader reader=new BufferedReader(new FileReader(qrel));
		String templine=null;
		while((templine=reader.readLine())!=null){
			String[] items=templine.split(" ");
			if(Integer.parseInt(items[0])==topic){
				int rel=Integer.parseInt(items[3]);
				if(rel>0){				
					qlist.getList().add(new QrelsNote(Integer.parseInt(items[1]), items[2], rel));
				}	
			}
		}
		reader.close();
		if(qlist.getList().size()==0) return null;
		return qlist;
	}
	//dealing with
	/**
	 * 相关性等级处理：binary/graded
	 * @param require 处理要求：“binary”或者“graded”
	 * @param qrels 相关性等级
	 * @return
	 */
	public double dealQrels(String require,double qrels){
		if(require.equals("binary"))
			return (qrels>0)? 1.0:0.0;//binary
		else if(require.equals("graded"))
			return (qrels>0)? qrels:0.0;//graded	
		return qrels;
	}
	/**
	 * 将ResultList中的所有文档信息加入二叉树，保存的格式style
	 * <li>style=0:[sid,score,sid,score,...]</li>
	 * @param list
	 * @param bt
	 * @param sid
	 * @param style
	 */
	public void addResultlistInTree(ArrayList<result> list, BinaryTree bt,int sid,int style) {
		// TODO Auto-generated method stub
		// 不舍弃
		for(int i=0;i<list.size();i++){
			String name=list.get(i).docid;
			if(style==0){
				String partscore=sid+","+list.get(i).score+",";
				bt.search_add(name, partscore);
			}
		}
	}
	/**
	 * 将QrelList中文档信息加入二叉树(若该文档树中不存在，则舍弃？？？)，保存的格式style
	 * <li>style=0:adhoc [qrel,rel]</li>
	 * <li>style=1:diversity [qrel,subtopic,rel,qrel,subtopic,rel,...]</li>
	 * @param list
	 * @param bt
	 * @param style
	 */
	public void addQrellistInTree(ArrayList<QrelsNote> list,BinaryTree bt,int style){
		for(int i=0;i<list.size();i++){
			String name=list.get(i).getDocid();
			if(style==0){
				String partscore=numofsystem+","+list.get(i).getRelevance()+",";
				bt.search_add_qrels(name, partscore);
			}
			if(style==1){
				String partscore=numofsystem+","+list.get(i).getSubtopic()+","+list.get(i).getRelevance()+",";
				bt.search_add_qrels(name, partscore);
			}
		}
	}
	//-------------------------------------统计输入文件的相关信息（input,qrelsinput）----------------------------------
	/**
	 * check the a result: the query Length of a result
	 * @param input
	 * @throws IOException
	 */
	public void checkQueryLength(String input) throws IOException{
		File path=new File(input);
		File[] files=path.listFiles();
		BufferedReader[] readers=new BufferedReader[numofsystem];
		for(int j=0;j<numofsystem;j++){
			readers[j]=new BufferedReader(new FileReader(files[j]));
			bufferline[j]=new String(readers[j].readLine());
			System.out.print(","+files[j].getName());
		}
		System.out.print("\n");
		int[] total=new int[numofsystem];
		for(int i=0;i<numofquery;i++){
			System.out.print((i+startquery));
			for(int j=0;j<numofsystem;j++){
				ResultList list=getResultList(readers[j], j,true);
				total[j]+=list.list.size();
				System.out.print(","+list.list.size());
			}
			System.out.print("\n");
		}
		System.out.print("total");
		for(int j=0;j<numofsystem;j++){
			readers[j].close();
			System.out.print(","+total[j]);
		}
		
	}
	public void checkRealQueryNum(File input) throws IOException{
		BufferedReader reader=new BufferedReader(new FileReader(input));
		int tempqueryid=this.startquery;
		String templine;
		while((templine=reader.readLine())!=null){
			StringTokenizer tokenizer=new StringTokenizer(templine);
			int queryid=Integer.parseInt(tokenizer.nextToken());
			if(queryid!=tempqueryid){
				tempqueryid++;
				if(queryid!=tempqueryid){
					System.err.println("the file: "+input.getName()+"miss the query "+tempqueryid);
				}
			}
		}
		if(tempqueryid<this.startquery+numofquery-1)
			for(;tempqueryid<this.startquery+numofquery-1;){
				tempqueryid++;
				System.err.println("the file: "+input.getName()+"miss the query "+tempqueryid);
			}
		reader.close();
	}
	
	
	//-------------------------------------规范化--------------------------------------------------------
	/**
	 * 规范化score的公式（基于rank）</br>*可以换公式
	 * @param rank 文档的rank信息
	 * @param type 
	 * @param score 
	 * @param max 
	 * @param min 
	 * @return
	 */
	public static double normscore(int rank, double score, double min, double max, int type){
		if(type==0){//reciprocal rank
			return 1.0/(rank+60);
//			return (rank<100)?1.0/(rank+60):0;
		}
		if(type==1){//0-1
			if(min==max){
				min=0;
				max=standardlength;
				score=max-rank;
			}
			return (score-min)/(max-min);
		}
		if(type==2){//[a,b]
			double a=0.024517327;
			double b=0.669482323;
			if(min==max){
				min=0;
				max=standardlength;
				score=max-rank;
			}
			return (b-a)*(score-min)/(max-min)+a;
		}
		if(type==3){//log rank
//			return Math.log(rank+2);
			double k=1-0.2*Math.log(rank);
			return (k>0)?k:0;
//			return k;
		}
		return 0;

	}
	/**
	 * 规范化一个ResultList的处理步骤：</br>
	 * 1.多个query按大小排序</br>
	 *|sheqi|2.每个query内的ResultList内所有rank根据文档出现的次序从0开始编号（大部分不变，少数乱序，少数rank始终为0）</br>
	 * 3.score根据公式规范化（公式基于rank）</br>
	 * @param input  原始input的File对象
	 * @param output  规范化后盖文件的输出路径
	 * @throws IOException
	 */
	public static void normAinput(File input,String output,int type) throws IOException{
		BufferedReader reader=new BufferedReader(new FileReader(input));
		BufferedWriter writer=new BufferedWriter(new FileWriter(output));
		//缓存input文件第一行
		bufferline[0]=new String(reader.readLine());
		//获取input中文件所有query下的ResultList
		ArrayList<ResultList> arraylists=new ArrayList<>();
		for(int i=0;i<numofquery;i++){
			ResultList list=getResultList(reader, 0,true);//获取一个query的ResultList
			arraylists.add(list);
		}
		//多个ResultList先按query大小排序
		Comparator<ResultList> comp=new Comparator<ResultList>() {
				
				@Override
				public int compare(ResultList o1, ResultList o2) {
					return o1.topic-o2.topic;
				}
		};			
		Collections.sort(arraylists, comp);	
		//每个query下的ResultList内部的list按原始出现顺序，重新编号rank(大部分rank和出现次序一致，少数不一致)，规范score,将
		//每一项组装成原本格式输出到规范化后input路径
		for(int i=0;i<numofquery;i++){
			ArrayList<result> list=arraylists.get(i).list;
			double min=10000,max=-100000;
			for(int j=0;j<list.size();j++){
				if(list.get(j).score>max){
					max=list.get(j).score;
				}
				if(list.get(j).score<min){
					min=list.get(j).score;
				}
			}
			for(int j=1;j<=list.size();j++){
				double normscore=normscore(j,list.get(j-1).score,min,max,type);
				if(normscore>0){
					writer.write(arraylists.get(i).topic+"\tQ0\t"+list.get(j-1).docid+
							"\t"+(j)+"\t"+normscore+
							"\t"+arraylists.get(i).system+"\n");
				}			
			}
		}
		reader.close();
		writer.close();
	}	
	/**
	 * 规范化（指定原始input文件所在目录，规范化目录下所有系统的input）
	 * <p>分值的规范化公式见：normscore</p>
	 * @param rawinput 原始文件目录
	 * @param norminput 规范后文件目录
	 * @throws IOException
	 */
	@Deprecated
	public static void normalization(String rawinput,String norminput) throws IOException{
		File fpath=new File(rawinput);
		File[] files=fpath.listFiles();
		for(int i=0;i<files.length;i++){
			normAinput(files[i], norminput+"/"+files[i].getName(),0);
		}	
	}
	/**
	 * 规范化（指定原始input文件所在目录，规范化目录下所有系统的input）
	 * <p>分值的规范化公式见：normscore</p>
	 * @param rawinput 原始文件目录
	 * @param norminput 规范后文件目录
	 * @param type 0:1/(r+60) 1:min-max 2:[a,b] 3:ln(rank)
	 * @throws IOException
	 */
	@Deprecated
	public static void normalization(String rawinput,String norminput,int type) throws IOException{
		File fpath=new File(rawinput);
		File[] files=fpath.listFiles();
		for(int i=0;i<files.length;i++){
			normAinput(files[i], norminput+"/"+files[i].getName(),type);
		}	
	}
	public BinaryTree[] normAccordtoCover(String qrelinput) throws IOException{
		BufferedReader reader=new BufferedReader(new FileReader(qrelinput));
		bufferqrel=reader.readLine();
		String docid,partscore;
		BinaryTree[] bts=new BinaryTree[numofquery];
		for(int qid=0;qid<numofquery;qid++){
			//get a query's qrellist
			QrelsList qrelslist=getQrelsList(reader,true);
			//if the query in qrelsfile is missing...
			while(qrelslist.getTopic()>this.startquery+qid){
				qid++;
			}
			if(qrelslist.getTopic()==this.startquery+qid){
				//create document pool/tree
				BinaryTree bt = new BinaryTree();
				ArrayList<QrelsNote> list=qrelslist.getList();
				for(int i=0;i<list.size();i++){		
					if(list.get(i).getRelevance()>0){
						docid=list.get(i).getDocid();
						partscore=list.get(i).getSubtopic()+","+list.get(i).getRelevance()+",";
						bt.search_add(docid, partscore);
					}
				}
				bts[qid]=bt;
			}	
		}
		return bts;
	}
	public int getSimofSubtopics(String[] a,String[] b){
		int num=10;
		int[] asubt=new int[num];
		int[] bsubt=new int[num];
		for(int i=0;i<a.length;i++){
			int p=Integer.parseInt(a[i]);
			asubt[p]=Integer.parseInt(a[++i]);
		}
		for(int i=0;i<b.length;i++){
			int p=Integer.parseInt(b[i]);
			bsubt[p]=Integer.parseInt(b[++i]);
		}
		int sim=0;
		for(int i=0;i<num;i++){
			if(asubt[i]==bsubt[i]&&asubt[i]>0)
				sim++;
		}
		if(sim==a.length/2){
			return -1;//have to discount
		}
		if(sim<a.length/2){
			return (a.length/2-sim)/(a.length/2+b.length/2-sim);
		}
		return 0;
	}
	public double getNormedScore(result cur,result after,int r,BinaryTree bt){
		double afs=1.0/(r+61);
		BinaryTreeNode curnode=bt.search(cur.docid);
		BinaryTreeNode afnode=bt.search(after.docid);
		String[] cursubts = null, afsubts = null;
		if(curnode!=null && afnode!=null){
			cursubts=bt.search(cur.docid).getPartscore().split(",");
			afsubts=bt.search(after.docid).getPartscore().split(",");
			if(getSimofSubtopics(cursubts, afsubts)<0){
				afs-=(afs-1.0/(r+62))*(1-1.0/(r+60));
			}else{
				
			}
		}else if(curnode!=null && afnode==null){//have to award
			//need to be changed with the method "getSimofSubtopics"	
		}else if(curnode==null && afnode!=null){//have to discount
			afs-=(afs-1.0/(r+62))*(1-1.0/(r+60));
		}
		//只惩罚拍前面的文档得分
		
		
		return afs;
	}
	public void normalization(String rawinput,String norminput,String qrelinput) throws IOException{
		BinaryTree[] bts=normAccordtoCover(qrelinput);
		File fpath=new File(rawinput);
		File[] files=fpath.listFiles();
		for(int i=0;i<files.length;i++){
			BufferedReader reader=new BufferedReader(new FileReader(files[i]));
			BufferedWriter writer=new BufferedWriter(new FileWriter(norminput+"/"+files[i].getName()));
			bufferline[0]=new String(reader.readLine());
			ArrayList<ResultList> arraylists=new ArrayList<>();
			for(int qi=0;qi<numofquery;qi++){
				ResultList list=getResultList(reader, 0,true);
				arraylists.add(list);
			}
			Comparator<ResultList> comp=new Comparator<ResultList>() {
					@Override
					public int compare(ResultList o1, ResultList o2) {
						return o1.topic-o2.topic;
					}
			};			
			Collections.sort(arraylists, comp);	
			//compute norm score
			for(int qi=0;qi<numofquery;qi++){
				ArrayList<result> list=arraylists.get(qi).list;
				BinaryTree bt=bts[qi];
				writer.write(arraylists.get(qi).topic+"\tQ0\t"+list.get(0).docid+
						"\t0\t"+Double.toString(1.0/(60))+
						"\t"+arraylists.get(qi).system+"\n");
				for(int j=1;j<(list.size()<10000?list.size():10000);j++){
					double afs=1.0/(j+60);
					if(bt!=null){
						afs=getNormedScore(list.get(j-1), list.get(j), j-1, bt);
					}
					writer.write(arraylists.get(qi).topic+"\tQ0\t"+list.get(j).docid+
							"\t"+j+"\t"+Double.toString(afs)+
							"\t"+arraylists.get(qi).system+"\n");
				}
			}
			reader.close();
			writer.close();
		}
		
	}
	
	
	//-------------------------------------权重-------------------------------------------------
	/**
	 * 设定权重：从文件获取（文件格式：一行为一组权重，以,分隔）
	 * @param weightfile
	 * @throws IOException 
	 */
	public void setWeights(String weightfile) throws IOException{
		int group=0;
		String templine=null;

		BufferedReader reader=new BufferedReader(new FileReader(weightfile));		
		while((templine=reader.readLine())!=null){		
			group++;
		}
		reader.close();
		
		this.weights=new double[group][numofsystem];
		reader=new BufferedReader(new FileReader(weightfile));	
		int i=0;
		while((templine=reader.readLine())!=null){
			for(int j=0;j<numofsystem;j++){
				weights[i][j]=new Double(templine.split("[\t, ]")[j]);
			}
			i++;
		}
		reader.close();
		
	}
	public void dissInWeights(){
		int n=this.weights.length;
		String out="";
		double[][] diss=new double[n][n];
		for(int i=0;i<n;i++){
			for(int j=i+1;j<n;j++){
				double dis=0,va=0,vb=0;
				for(int s=0;s<numofsystem;s++){
					dis+=weights[i][s]*this.weights[j][s];
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
	
	public void setWeight(double[] weight){
		this.weight=weight;
	}
	public void setWeights(double[][] weights) throws IOException{
		this.weights=weights;
	}
	/**
	 * 控制台输出权重
	 */
	public void printWeights(){
		System.out.println("");
		for(int i=0;i<5;i++){
			String s="";
			for(int j=0;j<numofsystem;j++){
				s+=weights[i][j]+",";
			}
			System.out.println(s);
		}
	}
	
	
	//-------------------------------------融合-------------------------------------------------
	/**
	 * 包含一个query下的文档二叉树转换成按分值排序,输出到融合结果文件
	 * @param topic
	 * @param bt 包含文档信息的二叉树，按docid排序
	 * @param combsysname 融合后结果的系统名称
	 * @param writer
	 * @throws IOException
	 */
	public void combineGroupSystem(int topic,BinaryTree bt,String combsysname,FileWriter writer) throws IOException{
		String temp ="";
		BinaryTreeNode btn = null;
		TreeByAttribute tba = new TreeByAttribute();
		btn=bt.getFirstNodeInLexicalOrder();
		while (btn!=null)
		{
			tba.search_add(btn.getName(),btn.getScore());
			btn=btn.getSuccesor();
		}
		btn=tba.getFirstNodeInOrder();
		int i=1;
		while (btn!=null&&i<standardlength)
		{
			temp=""; temp=new Integer(topic).toString();
			temp=temp.concat("\tQ0\t");
			temp=temp.concat(btn.getName());
			temp=temp.concat("\t"+new Integer(i).toString()+"\t");
			temp=temp.concat(new Double (btn.getScore()).toString());
			temp=temp.concat("\t"+combsysname+"\n");
			writer.write(temp);
			btn=btn.getSuccesor(); 
			i++;
		}
	}
	
	/**
	 * linear combination(5-fold 交叉验证，支持rawinput目录中部分文件的融合)
	 * <p>组合所有query下的所有input</p>
	 * <p>For validation, the query is divided into 5 groups</p>
	 * <p>先用一颗按docid排序的二叉树存放一个query下的所有文档分数信息（分值经过规范化，乘以所在系统权重）
	 * ，然后将这颗树转换成一颗以score排序的树，遍历该树，将结果输出到指定路径文件</p>
	 * @param files  若干系统对应的input文件对象数组
	 * @param output
	 * @param combsysname
	 * @throws Exception 
	 */
	public void LCFusion(File[] files,String output,String combsysname) throws Exception{
		ResultList[] grouplists=new ResultList[numofsystem];
		BufferedReader[] readers=new BufferedReader[numofsystem];
		FileWriter writer=new FileWriter(output);
		//open file reader
		for(int i=0;i<numofsystem;i++){
			readers[i]=new BufferedReader(new FileReader(files[i]));
			bufferline[i]=new String(readers[i].readLine());	
		}
		//for all query
		for(int qid=0;qid<numofquery;qid++){
			//:get the lists
			for(int i=0;i<numofsystem;i++){		
				grouplists[i]=getResultList(readers[i],i,true);
				if( i>0 && grouplists[i].topic!=grouplists[i-1].topic)
					throw new Exception("qid不一致，存在input的某个query缺失");			
			}
			//combine systems in a query
			BinaryTree bt = new BinaryTree();//LN
			for (int sid=0; sid<numofsystem; sid++){
				for (int i=0; i<grouplists[sid].list.size(); i++){
					int group=qid/(numofquery/weights.length);
					String docid=grouplists[sid].list.get(i).docid;
					double iscore=grouplists[sid].list.get(i).score;
					if(iscore==0){
						continue;
					}else{
						double score=grouplists[sid].list.get(i).score*weights[group][sid];
						if(score<=0) continue;
						bt.search_add(docid, score);
					}		
				}
			}
			combineGroupSystem(qid+startquery, bt, combsysname,writer);
		}
				
		//close the files
		for(int i=0;i<numofsystem;i++){
			readers[i].close();
		}
		writer.close();
	}
	/**
	 * linear combination(5-fold cross validation):组合所有query下的所有input	
	 * <p>适用于给定input目录，对目录下所有系统的input文件进行融合</p>
	 * @param norminput 经过规范化后的input目录
	 * @param output
	 * @param combsysname
	 * @throws Exception 
	 */
	public void LCFusion(String norminput,String output,String combsysname) throws Exception{
	//	System.out.println("!!!\t"+norminput+"\t"+output+"\t"+combsysname);
		File fpath=new File(norminput);
		File[] files=fpath.listFiles();
		LCFusion(files, output, combsysname);
	}
	/**
	 * linear combination(非交叉验证，只用一组权重)
	 * @param norminput
	 * @param output
	 * @param combsysname
	 * @throws Exception 
	 */
	public void LCFusion(String norminput,String output,String combsysname,double[] givenweight) throws Exception{
		ResultList[] grouplists=new ResultList[numofsystem];
		File fpath=new File(norminput);
		File[] files=fpath.listFiles();
		BufferedReader[] readers=new BufferedReader[numofsystem];
		FileWriter writer=new FileWriter(output);
		//open file reader
		for(int i=0;i<numofsystem;i++){
			readers[i]=new BufferedReader(new FileReader(files[i]));
			bufferline[i]=new String(readers[i].readLine());	
		}
		//for all query
		for(int qid=0;qid<numofquery;qid++){
			//:get the lists
			for(int i=0;i<numofsystem;i++){				
				grouplists[i]=getResultList(readers[i],i,true);
				if(i>0 && grouplists[i].topic!=grouplists[i-1].topic)
					throw new Exception("qid不一致，存在input的某个query缺失");	
			}
			//combine systems in a query
			BinaryTree bt = new BinaryTree();//LN
			for (int sid=0; sid<numofsystem; sid++){
				for (int i=0; i<grouplists[sid].list.size(); i++){
					String docid=grouplists[sid].list.get(i).docid;
					double score=grouplists[sid].list.get(i).score*givenweight[sid];
					if(score==0)continue;
					bt.search_add(docid, score);
					}
				}
			combineGroupSystem(qid+startquery, bt, combsysname,writer);
		}
				
		//close the files
		for(int i=0;i<numofsystem;i++){
			readers[i].close();
		}
		writer.close();
		System.err.println("fusion is end~");
	}
	/**
	 * 为DE学习设置：startquery,endquery之间的查询用于测试，此外的训练
	 * @param runs
	 * @param output
	 * @throws IOException 
	 */
	public void LCFusion_DeTrain(ArrayList<ResultList[]> runs, String output,int top) throws IOException {	
		FileWriter writer=new FileWriter(output);
		for(int qid=0;qid<runs.size();qid++){		
			if(runs.get(qid)!=null){//训练
				//combine systems in a query
				BinaryTree bt = new BinaryTree();//LN
				for (int sid=0; sid<numofsystem; sid++){
					int l=runs.get(qid)[sid].list.size();
					for (int i=0; i<(l>top?top:l); i++){//部分融合
						String docid=runs.get(qid)[sid].list.get(i).docid;
						double score=runs.get(qid)[sid].list.get(i).score*weight[sid];
						bt.search_add(docid, score);
						}
					}	
				combineGroupSystem(runs.get(qid)[0].topic, bt, "tempedrun",writer);
			}
		}
		writer.close();
	}
	public void LCFusion(File[] files,String output,String combsysname,int[][]group) throws Exception{
		FileWriter writer=new FileWriter(output);
		for(int i=0;i<group.length;i++){
			for(int j=0;j<group[i].length;j++){
				ResultList[] grouplists=new ResultList[numofsystem];
				BinaryTree bt = new BinaryTree();//store ranklist
				for(int k=0;k<numofsystem;k++){						
					grouplists[k]=LC.getResultList(files[k],group[i][j]);
					for (int l=0; l<grouplists[k].list.size(); l++){
						String docid=grouplists[k].list.get(l).docid;
						double iscore=grouplists[k].list.get(l).score;
						if(iscore==0){
							break;
						}else{
							double score=grouplists[k].list.get(l).score*weights[i][k];
							bt.search_add(docid, score);
						}		
					}
					
				}
				if(grouplists[0].topic==16){
					System.out.println("");
				}
				combineGroupSystem(grouplists[0].topic, bt, combsysname,writer);
				
			}
		}
		writer.close();
	}

	public void LCFusion(File[] files, String output, String combsysname,
			double[] givenweight) throws Exception {
		ResultList[] grouplists=new ResultList[numofsystem];
		BufferedReader[] readers=new BufferedReader[numofsystem];
		FileWriter writer=new FileWriter(output);
		//open file reader
		for(int i=0;i<numofsystem;i++){
			readers[i]=new BufferedReader(new FileReader(files[i]));
			bufferline[i]=new String(readers[i].readLine());	
		}
		//for all query
		for(int qid=0;qid<numofquery;qid++){
			//:get the lists
			for(int i=0;i<numofsystem;i++){				
				grouplists[i]=getResultList(readers[i],i,true);
				if(i>0 && grouplists[i].topic!=grouplists[i-1].topic)
					throw new Exception("qid不一致，存在input的某个query缺失");	
			}
			//combine systems in a query
			BinaryTree bt = new BinaryTree();//LN
			for (int sid=0; sid<numofsystem; sid++){
				for (int i=0; i<grouplists[sid].list.size(); i++){
					String docid=grouplists[sid].list.get(i).docid;
					double score=grouplists[sid].list.get(i).score*givenweight[sid];
					bt.search_add(docid, score);
					}
				}
			combineGroupSystem(qid+startquery, bt, combsysname,writer);
		}
				
		//close the files
		for(int i=0;i<numofsystem;i++){
			readers[i].close();
		}
		writer.close();
		System.err.println("fusion is end~");
		
	}
	
}