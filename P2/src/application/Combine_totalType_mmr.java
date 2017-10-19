package application;

import java.io.IOException;

import diversity.MMR_totalType;

/**
 * 对原始文档结果，按照类别合并，之后可以进行MMR重拍
 * @author Administrator
 *
 */
public class Combine_totalType_mmr {

	public static void main(String[] args) throws Exception {
		/*MMR_totalType mmr = new MMR_totalType("原始文档结果列表输入(docid)","自定义文件（最好是全文的）");
		for (int i = 251; i < 300; i++){
			mmr.clear(i);
			mmr.div();
			mmr.write("输出路径");
		}*/

		MMR_totalType mmr = new MMR_totalType("E:\\2014query\\DPH_1.res.index.1000.adhoc","E:\\2014query\\result\\全局-自定义");
		for (int i = 299; i <= 300; i++){
			mmr.clear(i);
			mmr.div();
			mmr.write("E:\\2014query\\result\\类别合并");
		}
	}

}
