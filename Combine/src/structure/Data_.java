package structure;

public class Data_ {

	private int qid;
	private int docid;
	//文档排名
	private int order;
	private double score;
	
	public Data_(String line){
		String []strings = line.split("[\t|\n|\r| ]");
		this.qid = Integer.parseInt(strings[0]);
		this.docid = Integer.parseInt(strings[2]);
		this.order = Integer.parseInt(strings[3]);
		this.score = Double.parseDouble(strings[4]);
	}
	
	public Data_(int qid, int docid, int order, double score) {
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
	public int getDocid() {
		return docid;
	}
	public void setDocid(int docid) {
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



}
