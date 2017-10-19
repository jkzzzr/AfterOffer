package diversity;


public class Entry_MMR_LDA {

	public static void main(String[] args) throws Exception {
		MMR_LDA mmr_LDA = new MMR_LDA("E:\\2014query\\result-原始，还没多样化的\\样本文档-检索结果","E:\\2014query\\result-原始，还没多样化的\\全局-自定义");
		for (int i = 251; i <= 300; i++){
			mmr_LDA.clear(i);
			mmr_LDA.div();
			mmr_LDA.write2("E:\\2014query\\LDA类别相关性计算!!!");
			System.out.println("qid:\t" + i);
		}
	}

}
