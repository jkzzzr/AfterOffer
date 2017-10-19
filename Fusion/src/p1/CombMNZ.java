package p1;

import datastruct.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;


public class CombMNZ extends LC{

	public CombMNZ(int numofsystem, int numofquery, int startquery,
			Hashtable<String, Integer> sys) {
		super(numofsystem, numofquery, startquery, sys);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("deprecation")
	public void Fusion(File[] files,String output,String combsysname) throws Exception{
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
					if(score==0) continue;
					bt.search_add(docid, score);
				}
			}
			combineSystem(qid+startquery, bt, combsysname,writer);
		}
				
		//close the files
		for(int i=0;i<numofsystem;i++){
			readers[i].close();
		}
		writer.close();
		System.err.println("fusion is end~");
	}

	/**
	 * �����Ѿ���������ϵͳ�ĵ������������ĵ��ڸ���ϵͳ�еķ�ֵ�ͣ���count�л�ȡ�ĵ����ֵ�ϵͳ����
	 * @param bt
	 */
	public void combineSystem(int topic,BinaryTree bt,String combsysname,FileWriter writer) throws IOException{
		String temp ="";
		BinaryTreeNode btn = null;
		TreeByAttribute tba = new TreeByAttribute();
		btn=bt.getFirstNodeInLexicalOrder();
		while (btn!=null){
			tba.search_add(btn.getName(),btn.getScore()*btn.getCount());//score���ĵ�������ϵͳ�ķ�ֵ�ͣ�count���ĵ����ֵĴ���
			btn=btn.getSuccesor();
		}
		btn=tba.getFirstNodeInOrder();
		int i=0;
		while (btn!=null&&i<standardlength)
		{
			temp=""; temp=new Integer(topic).toString();
			temp=temp.concat("\tQ0\t");
			temp=temp.concat(btn.getName());
			temp=temp.concat("\t"+new Integer(i+1).toString()+"\t");
			temp=temp.concat(new Double (btn.getScore()).toString());
			temp=temp.concat("\t"+combsysname+"\n");
			writer.write(temp);
			btn=btn.getSuccesor(); 
			i++;
		}
	}
	
}
