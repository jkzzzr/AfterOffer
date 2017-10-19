package my;

import java.io.File;
import java.util.ArrayList;

import p1.Ndeval;
import p1.Ndeval.rList;

public class Choose {

	public static void main(String[] args) throws Exception {
		int year=2009;
		String input="F:\\Fusion\\final\\2011dealinput";//Combsum2010 2010.PM2
		String qrelfile="F:\\Fusion\\2011\\qrels.final-format";
	
		String files[] = new File(input).list();
		for (String name:files){
			Ndeval n=new Ndeval();
			
			File f=new File(input+"\\"+name);
			
			ArrayList<rList> rrl=n.processRun(f, false);
			ArrayList<rList> qrl=n.processQrels(qrelfile);
			n.applyQrels(qrl, rrl);	
			n.evaluateALL(new File(input+"\\"+name), qrelfile);
		}
	}

}
