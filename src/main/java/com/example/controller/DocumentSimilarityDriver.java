package com.example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class DocumentSimilarityDriver {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: DocumentSimilarityDriver <input path> <output path>");
            System.exit(-1);
        }

        Configuration hadoopConf = new Configuration();
        Job similarityJob = Job.getInstance(hadoopConf, "Document Similarity Analysis");

        similarityJob.setJarByClass(DocumentSimilarityDriver.class);
        similarityJob.setMapperClass(DocumentSimilarityMapper.class);
        similarityJob.setReducerClass(DocumentSimilarityReducer.class);

        similarityJob.setOutputKeyClass(Text.class);
        similarityJob.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(similarityJob, new Path(args[0]));
        FileOutputFormat.setOutputPath(similarityJob, new Path(args[1]));

        int exitCode = similarityJob.waitForCompletion(true) ? 0 : 1;
        System.exit(exitCode);
    }
}
