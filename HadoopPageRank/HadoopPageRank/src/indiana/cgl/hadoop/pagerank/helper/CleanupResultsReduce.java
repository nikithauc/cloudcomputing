package indiana.cgl.hadoop.pagerank.helper;

import java.io.IOException;
 
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.Reducer;

public class CleanupResultsReduce extends Reducer<DoubleWritable, Text, Text, DoubleWritable>{
public void reduce(DoubleWritable key, Iterable<Text> values,
		Context context) throws IOException, InterruptedException {
System.out.println(key);
for (Text val: values)
	context.write(val, key);
}
}
