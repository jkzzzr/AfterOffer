package my;

import java.io.File;

import p1.CombMNZ;
import p1.CombSUM;

public class My_combSum_MNZ {
	public static void main(String args[]) throws Exception{
		combmnz_2013();
		combsum_2013();
	//	combmnz_2014();
	//	combsum_2014();
	}
	
	public static void combbb(int systemNO, int qid, String inputPath, String postfix) throws Exception{
		CombMNZ c = new CombMNZ(systemNO, 50, qid, null);
		File[]files = new File(inputPath).listFiles();
		c.Fusion(files, inputPath+"\\combmnz"+postfix, "combmnz"+postfix);
		
		CombSUM combSUM = new CombSUM(systemNO, 50, qid, null);
		File[]files2 = new File(inputPath).listFiles();
		combSUM.CombSumFusion(files2, inputPath+"\\combsum"+postfix, "combsum"+postfix);
	}
	
	public static void combbb(int systemNO, int qid, String inputPath, String postfix, File[]files) throws Exception{
		CombMNZ c = new CombMNZ(systemNO, 50, qid, null);
	//	File[]files = new File(inputPath).listFiles();
		c.Fusion(files, inputPath+"\\combmnz"+postfix, "combmnz"+postfix);
		
		CombSUM combSUM = new CombSUM(systemNO, 50, qid, null);
	//	File[]files2 = new File(inputPath).listFiles();
		combSUM.CombSumFusion(files, inputPath+"\\combsum"+postfix, "combsum"+postfix);
	}
	
	
	public static void combmnz_2014() throws Exception{
		CombMNZ c = new CombMNZ(8, 50, 251, null);
		File[]files = new File("E:\\ws\\ws-1\\Fusion\\2014_choosen").listFiles();
		c.Fusion(files, "E:\\ws\\ws-1\\Fusion\\2014_combmnz", "combmnz_2014");
	}
	public static void combsum_2014() throws Exception{
		CombSUM combSUM = new CombSUM(8, 50, 251, null);
		File[]files = new File("E:\\ws\\ws-1\\Fusion\\2014_choosen").listFiles();
		combSUM.CombSumFusion(files, "E:\\ws\\ws-1\\Fusion\\2014_combsum", "combsum_2014");
	}
	
	
	//*************************************
	public static void combmnz_2013() throws Exception{
		CombMNZ c = new CombMNZ(8, 50, 201, null);
		File[]files = new File("E:\\ws\\ws-1\\Fusion\\2013_choosen").listFiles();
		c.Fusion(files, "E:\\ws\\ws-1\\Fusion\\2013_combmnz", "combmnz_2013");
	}
	
	public static void combsum_2013() throws Exception{
		CombSUM combSUM = new CombSUM(8, 50, 201, null);
		File[]files = new File("E:\\ws\\ws-1\\Fusion\\2013_choosen").listFiles();
		combSUM.CombSumFusion(files, "E:\\ws\\ws-1\\Fusion\\2013_combsum", "combsum_2013");
	}
	
}
