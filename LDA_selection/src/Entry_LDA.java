import core.algorithm.lda.LDA;


public class Entry_LDA {

	public static void main(String[] args) {
		func1("E:\\2014query\\LDA-Single\\input");

	}
	public static void func1(String input){
		int start=251;
		int end = 300;
		LDA lda = new LDA();
		for (int i = start; i <=end; i++){
			String path = input +"\\"+i+"\\doc.dat";
			String contextPath = input+"\\"+i;
			
			lda.qid = i;
			lda.setDirPath(contextPath);
			lda.run();
			System.out.println("qid:\t" + i);
		}
	}

}
