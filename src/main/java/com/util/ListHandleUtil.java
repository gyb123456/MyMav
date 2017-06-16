package com.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListHandleUtil {

	/**
	 * 去除List<Integer>里面重复的元素
	 * @param list
	 * @return
	 */
    public static List<Integer> removeDuplicateInt(List<Integer> list) {  
    	if(list == null){
    		return null;
    	}
    	for(int a=0;a<list.size();a++){
    		 list.remove(null);
    	}
    	if(list.size() < 2){
    		return list;
    	}
    	System.out.println("list.size()============"+list.size());
    	for(int b=0;b<list.size();b++){
   		 System.out.println("list-"+b+"============"+list.get(b));
    	}
        for ( int i = 0 ; i < list.size() - 1 ; i ++ ) {  
          for ( int j = list.size() - 1 ; j > i; j -- ) {  
            if (list.get(j).intValue() == (list.get(i).intValue())) {  
              list.remove(j);  
            }   
           }   
         }   
        return list;
     }
    
    /**
	 * 去除List<Integer>里面重复的元素
	 * @param list
	 * @return
	 */
    public static List<String> removeDuplicateStr(List<String> list) {  
    	if(list == null){
    		return null;
    	}
    	for(int a=0;a<list.size();a++){
    		 list.remove(null);
    	}
    	if(list.size() < 2){
    		return list;
    	}
    	System.out.println("list.size()============"+list.size());
    	for(int b=0;b<list.size();b++){
   		 System.out.println("list-"+b+"============"+list.get(b));
    	}
        for ( int i = 0 ; i < list.size() - 1 ; i ++ ) {  
          for ( int j = list.size() - 1 ; j > i; j -- ) {  
            if (list.get(j).equals(list.get(i) )) {  
              list.remove(j);  
            }   
           }   
         }   
        return list;
     }
    
    public static String listToString(List list, char separator) {    
    	StringBuilder sb = new StringBuilder();    
    	for (int i = 0; i < list.size(); i++) {        
    	sb.append(list.get(i)).append(separator);    
    	}   
    	return sb.toString().substring(0,sb.toString().length()-1);
    }
    
    /**
     * 把String []去重后转成String字符串返回，用逗号隔开
     * @return
     */
    public static String arrayToString(String [] array){
    	List<String> list = Arrays.asList(array);
    	List l = removeDuplicateStr(list);
		return  listToString(l, ',');
    }
    	
    
    
}
