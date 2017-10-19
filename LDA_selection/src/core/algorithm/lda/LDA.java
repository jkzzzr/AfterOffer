package core.algorithm.lda;


public class LDA implements Runnable{

	
	@Override
	public void run() {
		LDAOption option = new LDAOption();
		//输出路径，文件夹
		option.dir = dirPath;//"E:/LLDA/model.dir";
		option.dfile = "doc.dat";
		option.est = true;  /////
		///option.estc = true;
		option.inf = false;
		option.modelName = "model-final";
		option.niters = 100;
		Estimator estimator = new Estimator();
		estimator.init(option);
		estimator.estimate();
	}
	
	public int qid;
	public String dirPath = "";
	public int getQid() {
		return qid;
	}

	public void setQid(int qid) {
		this.qid = qid;
	}

	public String getDirPath() {
		return dirPath;
	}

	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}


	public static void main(String[] args) {
		
		new LDA().run();
	}
	
}
