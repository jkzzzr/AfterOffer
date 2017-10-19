
public class AAA {

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
	        int end = array[0].length -1;
	        while (start <= end){
	            if ((start > (array.length -1)) ||(end <0)){
	                colum = array.length - 1;
	                break;
	            }
	            int middle = (start + end )/2;
	            if (array[middle][middle] < target){
	                start = middle + 1;
	            }else if (array[middle][middle] > target){
	                end = middle - 1;
	            }else {
	                return true;
	            }
	            colum = middle;
	        }
	        if (colum == array[0].length - 1){
	            return false;
	        }
	        
	        System.out.println(colum);
	        //检索右上角
	                start =  colum + 1;
	                end = array.length - 1;

	                int col2 = array.length - 1;
	                while (start <= end){
	                    if ((start > (array.length -1)) ||(end < colum + 1)){
	                        col2 = array.length - 1;
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
	                    col2 = middle;
	                }
	                start = 0;
	                end = array[colum].length - 1;
	                while (start <= end){
	                    int middle = (start + end )/2;
	                    if (array[middle][colum] < target){
	                        start = middle + 1;
	                    }else if (array[middle][colum] > target){
	                        end = middle - 1;
	                    }else {
	                        return true;
	                    }
	                }
	        
	        
	        //检索左下角
	        
	       		start =  0;
	            end = colum;
	        
	        
	        
	        	int col3 = 0;    
	            while (start <= end){
	                if ((start > (array.length -1)) ||(end <0)){
	                    colum = array.length - 1;
	                    break;
	                }
	                int middle = (start + end )/2;
	                if (array[colum + 1][middle] < target){
	                    start = middle + 1;
	                }else if (array[colum + 1][middle] > target){
	                    end = middle - 1;
	                }else {
	                    return true;
	                }
	                col3 = middle;
	            }
	            start = 0;
	            end = array[colum].length - 1;
	            while (start <= end){
	                int middle = (start + end )/2;
	                if (array[middle][col3] < target){
	                    start = middle + 1;
	                }else if (array[middle][col3] > target){
	                    end = middle - 1;
	                }else {
	                    return true;
	                }
	            }
	            
	            return false;
	    }
}
