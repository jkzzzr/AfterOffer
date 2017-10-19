package diversity;

import java.io.IOException;

/**
 * 计算各个类别的MMR得分
 * @author Lee
 *
 */
public class Entry_MMR_totalType {

	public static void main(String[] args) throws IOException {
		MMR_totalType mmr = new MMR_totalType("E:\\2014query\\result\\样本文档-检索结果","E:\\2014query\\result\\全局-自定义");
		for (int i = 251; i <= 300; i++){
			mmr.clear(i);
			mmr.div();
			mmr.write("E:\\2014query\\大文档MMR-类别结果列表");
			System.out.println("qid\t" + i);
		}
	}

}
