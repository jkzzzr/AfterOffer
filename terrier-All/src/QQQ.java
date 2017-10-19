
public class QQQ {

	public static void main(String[] args) {
		int [][]array = new int[][]{{1,12,33,55},{2,13,34,56},{43,44,45,57}};
		int target = 2;
		System.out.println(Find(43, array));
	}

	public static boolean Find(int target, int [][] array) {
        if (array.length <=0){
            return false;
        }
        if (array[0][0] > target || array[array.length - 1][array.length - 1] < target){
            return false;
        }
            int colum = array.length - 1;
          /*  if (array[array.length - 1] < target){
                
            } */
            int start = 0; 
            int end = array.length -1;
            while (start <= end){
                if ((start > (array.length -1)) ||(end <0)){
                    colum = array.length - 1;
                    break;
                }
                int middle = (start + end )/2;
                if (array[0][middle] < target){
                    start = middle + 1;
                }else if (array[0][middle] > target){
                    end = middle - 1;
                }else {
                    return true;
                }
                colum = middle;
            }
            System.out.println(colum);
            start = 0;
            end = array[colum].length - 1;
            while (start <= end){
                int middle = (start + end )/2;
                if (array[middle][colum] < target){
                    start = middle + 1;
                }else if (array[middle][colum]> target){
                    end = middle - 1;
                }else {
                    return true;
                }
            }
            
            return false;
    }
}
