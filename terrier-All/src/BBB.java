
public class BBB {

	public static void main(String[] args) {
	//	int [][]array = new int[][]{{1,12,33,55},{2,13,34,56},{43,44,45,57}};
		int [][]array = new int[][]{};
		int target = 2;
		System.out.println(Find(42, array));
	}

	public static boolean Find(int target, int array[][]){
		if (array.length ==0){
			System.out.println("!!!");
			return false;
		}
		int start = 0;
		int end = array.length - 1;
		int cccol = 0;
		while (start <= end){
			int middle = (start + end) /2;
			if (array[middle][0] < target){
				start = middle +1;
				cccol = middle;
			}else if (array[middle][0] > target){
				cccol = middle -1;
				end = middle -1 ;
			}else {
				return true;
			}
		}
		int col = cccol >= array.length ?array.length -1 :cccol;
		int tempEnd = array.length;
		for (int i = 0; i <= col; i++){
			int temp = findRow(array[i], 0, tempEnd, target);
			if (temp != -1){
				tempEnd = ++temp;
			}else {
				return true;
			}
		}		
		return false;
	}
	
	public static int findRow(int[] array, int start, int end, int target){
		if (end > array.length - 1){
			end = array.length - 1;
		}
		int col = -1;
		while (start <= end){
			int middle = (start + end)/2; 
			if (array[middle] < target){
				start = middle + 1;
			}else if (array[middle] > target){
				end = middle - 1;
			}else {
				return -1;
			}
			col = middle;
		}
		return col;
	}
}
