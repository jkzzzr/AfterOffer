package core.algorithm.lda;

public class App_Entry_LDA {

	public static void main(String[] args) {
		LDA lda = new LDA();
		for (int i = 251; i <=300; i++){
			String datPath = "E:\\output\\123.txt";
			String contextPath = "E:\\output";
			
			lda.qid = i;
			lda.setDirPath(contextPath);
			lda.run();
			System.out.println("qid:\t" + i);
			
			/*String datPath = "E:\\output\\123.txt";
			String contextPath = "E:\\output\\Input.dat.context";
			lda.qid = i;
			lda.setDirPath("E:\\2014query\\LDA\\input\\" + lda.qid);
			lda.run();
			System.out.println("qid:\t" + i);*/
		}

	}

}
