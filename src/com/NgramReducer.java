package com;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class NgramReducer extends Reducer<Text,Text,Text,Text> {
	  
  	private Text result=new Text();
  
    public void reduce(Text key, Iterable<Text> values,Context context) throws IOException, InterruptedException {
    	
        Set<String> vols = new TreeSet<String>();		    
    	int matches=0;

    	for(Text val : values) {
    		matches = matches+1;
    		String valueStr = val.toString();
    		String[] valueStrs = valueStr.split(",");
    		
    		if(valueStrs.length>0)
    			vols.add(valueStrs[0]);
    	}
    	
    	String temp="\t"+Integer.toString(matches)+"\t"+Integer.toString(vols.size());
    	result.set(temp);
    	
    	System.out.println(key.toString()+result.toString());
    	context.write(key, result);
    }
}
