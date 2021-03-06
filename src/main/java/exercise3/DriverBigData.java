package exercise3;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class DriverBigData extends Configured implements Tool {

    /*
     *
     * The input file is a CSV file as so:
     *
     * sensorID,date\tPM10value\n
     *
     * The output would be:
     *
     * (sensorID, #days)
     *
     * where #days is the number of days above a specific threshold
     *
     */

    public static void main(String args[]) throws Exception {
        int res = ToolRunner.run(new Configuration(), new DriverBigData(), args);
        System.exit(res);
    }

    @Override
    public int run(String args[]) throws Exception {

        Path inputPath, outputDirectory;
        int numberOfReducers;
        int exitCode;

        numberOfReducers = Integer.parseInt(args[0]);
        inputPath = new Path(args[1]);
        outputDirectory = new Path(args[2]);
        // Configuration conf = this.getConf(); // not necessary
        Job job = Job.getInstance();

        job.setJobName("Exercise 3 - PM10");
        FileInputFormat.addInputPath(job, inputPath);
        FileOutputFormat.setOutputPath(job, outputDirectory);

        job.setJarByClass(DriverBigData.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setMapperClass(MapperBigData.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setReducerClass(ReducerBigData.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setNumReduceTasks(numberOfReducers);

        if (job.waitForCompletion(true)) {
            exitCode = 0;
        } else {
            exitCode = 1;
        }
        return exitCode;

    }

}
