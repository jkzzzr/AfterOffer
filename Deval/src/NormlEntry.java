
public class NormlEntry {

	public static void main(String[] args) throws Exception {
		int no =10;
		System.out.println("确保重新编译啊");
		String input = "H:\\学术内容\\实验\\实验结果保存\\DDF\\2011.PM2";
	//	String input = "E:\\2014query\\result---final\\DPH_1.res.index.100.mmr.string";
		String qrelfile = "F:\\Fusion\\2011\\qrels.final-format";
		String output = "H:\\学术内容\\实验\\实验结果保存\\DDF\\2011.eval";
	//	String output = "E:\\2014query\\result---final\\DPH_1.res.index.100.mmr.string";
		Ndeval.generateSummary(input, qrelfile, output);

	}

}
