package p1;


import datastruct.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Hashtable;
 
public class CombSUM extends LC{
	
	public CombSUM(int numofsystem, int numofquery, int startquery,
			Hashtable<String, Integer> sys) {
		super(numofsystem, numofquery, startquery, sys);
		// TODO Auto-generated constructor stub
	}
	public void CombSumFusion(File[] files,String output,String combsysname) throws Exception{
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
					throw new Exception("qid��һ�£�����input��ĳ��queryȱʧ");			
			}
			//combine systems in a query
			BinaryTree bt = new BinaryTree();//LN
			for (int sid=0; sid<numofsystem; sid++){
				for (int i=0; i<Math.min(grouplists[sid].list.size(),10000); i++){
					String docid=grouplists[sid].list.get(i).docid;		
					double score=grouplists[sid].list.get(i).score;
					if(score<=0) continue;
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
	//test LC.getResultList(File file ,int topic)����
	public void CombSum(File[] files,String output,String combsysname) throws Exception{
		ResultList[] grouplists=new ResultList[numofsystem];
		FileWriter writer=new FileWriter(output);
		for(int qid=0;qid<numofquery;qid++){
			for(int i=0;i<numofsystem;i++){		
				grouplists[i]=getResultList(files[i],startquery+qid);
				if(grouplists[i] == null){
					throw new Exception("component"+files[i].getName()+"�в�����Query:"+(startquery+qid)+"�Ľ��");
				}
				
			}
			BinaryTree bt = new BinaryTree();//LN
			for (int sid=0; sid<numofsystem; sid++){
				for (int i=0; i<grouplists[sid].list.size(); i++){
					String docid=grouplists[sid].list.get(i).docid;
					double score=grouplists[sid].list.get(i).score;
					bt.search_add(docid, score);
				}
			}
			combineGroupSystem(qid+startquery, bt, combsysname,writer);
		}
		writer.close();
		
	}
}
