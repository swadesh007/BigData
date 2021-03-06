package wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Q3JobControl {
    
private static final String OUT_PATH = "temp";

public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, " word count ");
    job.setJarByClass(Q3JobControl.class);
    job.setMapperClass(Q3Mapper1.class);
    job.setCombinerClass(Q3Reducer1.class);
    job.setReducerClass(Q3Reducer1.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args [0]));
    FileOutputFormat.setOutputPath(job, new Path(OUT_PATH));
    boolean success = job.waitForCompletion(true);
    if(success)
    {
    Job job1 = Job.getInstance(conf, " word count1 ");
    job1.setJarByClass(Q3JobControl.class);
    job1.setPartitionerClass(Q3NaturalKeyPartitioner.class);
    job1.setGroupingComparatorClass(Q3NaturalKeyGroupingComparator.class);
    job1.setGroupingComparatorClass(Q3CompositeKeyComparator.class);
    job1.setSortComparatorClass(Q3CompositeKeyComparator.class);
    job1.setMapperClass(Q3Mapper2.class);
    job1.setReducerClass(Q3Reducer2.class);
    job1.setOutputKeyClass(BiGram.class);
    job1.setOutputValueClass(NullWritable.class);
    try{
    FileInputFormat.addInputPath(job1, new Path(OUT_PATH));
    }
    catch(Exception e)
    {System.out.println("Failed to create out dir"+ e);
    }
    FileOutputFormat.setOutputPath(job1, new Path(args [1]));
    System.exit(job1.waitForCompletion(true) ? 0 : 1);
    }
}

}

