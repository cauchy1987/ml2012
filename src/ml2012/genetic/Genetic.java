package ml2012.genetic;

public class Genetic {

	  int    queenCount;//�ʺ��� 
	  int    resultCount = 0;//��ĸ��� 
	  int[]  queenSite = new int[32]; 
	    
	  public static void main(String agrs[]) 
	  { 
	     
		Genetic nqueen = new Genetic(); 
	    nqueen.queenCount = 4; 
	    nqueen.queen(0); 
	  } 
	/* 
	    * �ж��Ƿ��ͻ,�������queenSite,����ȵ�ֵ������2ֵ�Ĳ�����±��,�򷵻�true����ͻ, 
	    * ���򷵻�false.��ʾ����ͻ,��λ�ÿ��Է��ûʺ�! 
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
	    * �ڿ���̨��ʾ��� 
	    */ 
	  public void printResult() 
	  { 
	    if(queenCount <= 0) 
	      System.out.println("�޽�"); 
	    else 
	    { 
	      System.out.println("��"+resultCount+"�ֽⷨbegin:"); 
	      for(int k =0;k<queenCount;k++) 
	      { 
	        System.out.println("�� "+(k+1)+" ���ʺ�λ��:"+"��"+(k+1)+"��"+"��"+queenSite[k]+"��"); 
	      } 
	      System.out.println("end."); 
	    } 
	  } 
	  /* 
	    * ��Ҫ����,��0��ʼ,���ѵ�һ���ʺ���ڵ�һ��! 
	    *        Ȼ��Ѷ������ڵ�һ��,�������ͻ,����õ������ʺ�.��ͻ��ŵڶ��� 
	    *                ����õ������ʺ�,��ͻ����ŵڶ���,�ټ���Ƿ��ͻ 
	    *              ...... 
	    *                    �����n���ʺ�����ڵ�n��,��Ȼ��ͻ,������һ���ʺ󷵻�,������һ���ʺ���������һ�� 
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
