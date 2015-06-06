package ml2012.genetic;

public class Genetic {

	  int    queenCount;//皇后数 
	  int    resultCount = 0;//解的个数 
	  int[]  queenSite = new int[32]; 
	    
	  public static void main(String agrs[]) 
	  { 
	     
		Genetic nqueen = new Genetic(); 
	    nqueen.queenCount = 4; 
	    nqueen.queen(0); 
	  } 
	/* 
	    * 判断是否冲突,如果数组queenSite,有相等的值或者有2值的差等于下标差,则返回true即冲突, 
	    * 否则返回false.表示不冲突,该位置可以放置皇后! 
	    * 
	*/ 
	  public boolean isControl(int n) {    
	    for(int index=0;index<n;index++) 
	    { 
	      if ((Math.abs(queenSite[n]-queenSite[index]) == Math.abs(n-index)) 
	          ||( queenSite[n]== queenSite[index])) 
	        return true; 
	    } 
	         return    false; 
	  } 
	  public void printResult1() 
	  { 
	    for(int j=0 ;j<queenCount;++j) 
	    { 
	      System.out.println("queen["+(j+1)+"]"+"="+queenSite[j]); 
	    } 
	  } 
	  /** 
	    * 在控制台显示结果 
	    */ 
	  public void printResult() 
	  { 
	    if(queenCount <= 0) 
	      System.out.println("无解"); 
	    else 
	    { 
	      System.out.println("第"+resultCount+"种解法begin:"); 
	      for(int k =0;k<queenCount;k++) 
	      { 
	        System.out.println("第 "+(k+1)+" 个皇后位置:"+"第"+(k+1)+"行"+"第"+queenSite[k]+"列"); 
	      } 
	      System.out.println("end."); 
	    } 
	  } 
	  /* 
	    * 主要函数,从0开始,即把第一个皇后放在第一列! 
	    *        然后把二个放在第一列,如果不冲突,则放置第三个皇后.冲突则放第二列 
	    *                则放置第三个皇后,冲突则放着第二列,再检查是否冲突 
	    *              ...... 
	    *                    如果第n个皇后放置在第n列,仍然冲突,则向上一个皇后返回,放着上一个皇后在它的下一列 
	    */ 
	  public void queen(int nowIndex) 
	  { 
	    if(nowIndex == queenCount) 
	    { 
	      resultCount ++ ;
	      printResult(); 
	    } 
	    for(int index = 1;index<=queenCount;index++) 
	    { 
	      queenSite[nowIndex] = index; 
	      if(!isControl(nowIndex)) 
	              queen(nowIndex+1); 
	    }    
	     
	  } 
}
