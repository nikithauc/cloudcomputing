package indiana.cgl.hadoop.pagerank.helper;


import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class Sorter extends WritableComparator {

	protected Sorter() {
		super(DoubleWritable.class, true);
	}

	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		DoubleWritable key1 = (DoubleWritable) a;
		DoubleWritable key2 = (DoubleWritable) b;
		//int result = key1.get() < key2.get() ? 1 : key1.get() == key2.get() ? 0 : -1;
		return -1 * key1.compareTo(key2);
	}
}
