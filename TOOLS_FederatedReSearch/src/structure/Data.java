package structure;

import java.util.StringTokenizer;

public class Data {

	private int qid;
	private String docid;
	//文档排名
	private int order;
	private double score;
	
	public Data(String line){
		StringTokenizer st = new StringTokenizer(line);
		
		
		
		this.qid = Integer.parseInt(st.nextToken());
		st.nextToken();
		this.docid = st.nextToken();
		this.order = Integer.parseInt(st.nextToken());
		this.score = Double.parseDouble(st.nextToken());
	}
	/**
	 * 不同的行类型，这个行的类型呢，就是评价文件：201 	1 	clueweb12-0000tw-05-12114	 1
	 * @param line
	 * @param type
	 */
	public Data(String line, int type){
		StringTokenizer st = new StringTokenizer(line);
		
		
		
		this.qid = Integer.parseInt(st.nextToken());
		st.nextToken();
		this.docid = st.nextToken();
		this.score = Double.parseDouble(st.nextToken());
	}
	
	public Data(int qid, String docid, int order, double score) {
		super();
		this.qid = qid;
		this.docid = docid;
		this.order = order;
		this.score = score;
	}

	public int getQid() {
		return qid;
	}
	public void setQid(int qid) {
		this.qid = qid;
	}
	public String getDocid() {
		return docid;
	}
	public void setDocid(String docid) {
		this.docid = docid;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return String.format("%-8s",this.qid) + "\t "
				+ String.format("%-8s","Q0") + "\t "
				+ String.format("%-8s", this.docid) + "\t "
				+ String.format("%-8s", this.order) + "\t "
				+ String.format("%-8s", this.score) + "\t "
				+ String.format("%-8s", "FR---MMR") + "\t ";
	}

	public String toString2(String docid2) {
		return String.format("%-8s",this.qid) + "\t "
				+ String.format("%-8s","Q0") + "\t "
				+ String.format("%-8s", docid2) + "\t "
				+ String.format("%-8s", this.order) + "\t "
				+ String.format("%-8s", this.score) + "\t "
				+ String.format("%-8s", "FR---MMR") + "\t ";
	}



}
