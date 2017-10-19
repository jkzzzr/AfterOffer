import Entry.ClusterEntry;
import Entry.FuseEntry;
import Input.c1;
import OriginData.DataInput_My;
import fuse.compute.ClusterFuse;
import fuse.compute.PreResult;
import fuse.structure.ScoreBuilder;

public class Start {

	public static void main(String[] args) {
		ClusterFuse.lambda = 0.6;
		
	/*	long time1_Start = System.currentTimeMillis();
		FuseEntry.year = 2009;
		System.out.println("Step1!!");
		ClusterEntry.main(null);
		System.out.println("Step2!!");
		FuseEntry.main(null);
		long time1_End = System.currentTimeMillis();

		System.out.println("2009年运行消耗时间为：" + (time1_End - time1_Start));
		
		clear();
	*/
	   FuseEntry.year = 2010;
		long time2_Start = System.currentTimeMillis();
		System.out.println("Step1!!");
		ClusterEntry.main(null);
		System.out.println("Step2!!");
		FuseEntry.main(null);
		long time2_End = System.currentTimeMillis();
		System.out.println("2010年运行消耗时间为：" + (time2_End - time2_Start));
		
		
		clear();
		
		
	/*	
		FuseEntry.year = 2011;
		long time3_Start = System.currentTimeMillis();
		System.out.println("Step1!!");
		ClusterEntry.main(null);
		System.out.println("Step2!!");
		FuseEntry.main(null);
		long time3_End = System.currentTimeMillis();
		System.out.println("2011年运行消耗时间为：" + (time3_End - time3_Start));
		
		clear();
		
*/
//		System.out.println("2009年运行消耗时间为：" + (time1_End - time1_Start));
		System.out.println("2010年运行消耗时间为：" + (time2_End - time2_Start));
//		System.out.println("2011年运行消耗时间为：" + (time3_End - time3_Start));
	}

	private static void clear() {
		c1.Input_QD.clear();
		PreResult.Q_CentrMap.clear();
		PreResult.Q_SimMap.clear();
		ScoreBuilder.pcq.clear();
		ScoreBuilder.pdc.clear();
		DataInput_My.AllDoc.clear();
		DataInput_My.Input_D_W.clear();
		DataInput_My.Input_dS.clear();
		DataInput_My.Input_QD.clear();
		DataInput_My.Q_Ditem_Docid.clear();
	}
}
