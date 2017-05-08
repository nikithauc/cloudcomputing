// Based on the code from WordCount.java
// Archana Molasi (molasia)
// Nikitha Chettiar (nikchett)
// This program performs basic statistic operations of min, max, average and standard deviation on the input data.

import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class Statistics {
	public static class Map extends Mapper<LongWritable, Text, Text, DoubleWritable>{
		private Text word = new Text("Values");   // type of output key
                public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			Double decimal = Double.parseDouble(value.toString());
			DoubleWritable decimalValue = new DoubleWritable(decimal);
			context.write(word, decimalValue);     // create a pair <keyword, decimal value> 
		}
	}

	public static class Reduce
	extends Reducer<Text, DoubleWritable,Text, DoubleWritable> {
		double sum = 0.0, mini = Double.MAX_VALUE, maxi = Double.MIN_VALUE, sumSquare  = 0.0, standardDev = 0.0, avg = 0.0 ; 
		private DoubleWritable result = new DoubleWritable();
		private DoubleWritable resultMini = new DoubleWritable();
		private DoubleWritable resultMaxi = new DoubleWritable();
		private DoubleWritable resultSD = new DoubleWritable();
		private DoubleWritable md = new DoubleWritable();
		private Text minimum = new Text("Minimum");   
		private Text maximum = new Text("Maximum");  
		private Text average = new Text("Average");  
		private Text standardDeviation = new Text("Standard Deviation");
		private int count = 0; 
		public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
			for (DoubleWritable val : values) {
				count += 1;
				sum += val.get();  
				sumSquare += val.get() * val.get();
				// checking for minimum value
				if (val.get() < mini)
					mini = val.get();
				// checking for maximum value
				if (val.get() > maxi)
					maxi = val.get();
			}

			//calculation of average
			avg = sum/count;
			//calculation of standard deviation
			standardDev = Math.sqrt((sumSquare+(avg*avg*count)-(2*avg*sum))/count);
			result.set(avg);
			resultMini.set(mini);	
			resultMaxi.set(maxi);	
			resultSD.set(standardDev);
                        context.write(minimum, resultMini);
			context.write(maximum, resultMaxi);
			context.write(average, result);
			context.write(standardDeviation, resultSD);

		}
	}
	// Driver program
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration(); 
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs(); // get all args
		if (otherArgs.length != 2) {
			System.err.println("Usage: Statistics <in> <out>");
			System.exit(2);
		}
		// create a job with name "statistics"
		Job job = new Job(conf, "statistics");
		job.setJarByClass(Statistics.class);
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		// set output key type   
		job.setOutputKeyClass(Text.class);
		// set output value type
		job.setOutputValueClass(DoubleWritable.class);
		//set the HDFS path of the input data
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		// set the HDFS path for the output
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		//Wait till job completion
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
