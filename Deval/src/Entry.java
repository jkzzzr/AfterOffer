
public class Entry {
	public static void main(String [] argsStrings) throws Exception{
		int no =10;
		System.out.println("确保重新编译啊");
		String input = "E:\\2014query\\result\\final-individual\\individual."+ no+ ".mmr.string";
	//	String input = "E:\\2014query\\result---final\\DPH_1.res.index.100.mmr.string";
		String qrelfile = "E:\\2014query\\ndeval.txt";
		String output = "E:\\2014query\\ndeval\\final-individual\\individual."+ no + ".mmr.string.eval";
	//	String output = "E:\\2014query\\result---final\\DPH_1.res.index.100.mmr.string";
		Ndeval.generateSummary(input, qrelfile, output);
	}
}
