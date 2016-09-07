package com;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Ngram {
	
	public static void main(String[] args) throws Exception {
		 Configuration config = new Configuration();
		 Job job = Job.getInstance(config, "Ngram");

		 job.setJarByClass(Ngram.class);
		 
		 job.setInputFormatClass(WholeFileInputFormat.class);
	     job.setOutputKeyClass(Text.class);
	     job.setOutputValueClass(Text.class);
	     
	     job.setMapperClass(NgramMapper.class);
	     //job.setCombinerClass(NgramReducer.class);
	     job.setReducerClass(NgramReducer.class);
	     
	     job.setNumReduceTasks(1000);
	     
	     WholeFileInputFormat.addInputPath(job, new Path(args[0]));
	     FileOutputFormat.setOutputPath(job, new Path(args[1]));
	     
	     System.exit(job.waitForCompletion(true)?0:1);
	   }
}
