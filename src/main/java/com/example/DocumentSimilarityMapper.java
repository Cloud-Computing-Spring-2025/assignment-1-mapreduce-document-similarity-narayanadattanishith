package com.example;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DocumentSimilarityMapper extends Mapper<Object, Text, Text, Text> {

    private static final String DOCUMENT_DELIMITER = "\\s+";
    private static final int EXPECTED_PARTS = 2;

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String[] documentParts = value.toString().split(DOCUMENT_DELIMITER, EXPECTED_PARTS);
        if (documentParts.length < EXPECTED_PARTS) {
            context.getCounter("Map Errors", "Malformed Input").increment(1);
            return;
        }

        String docId = documentParts[0];
        String content = documentParts[1];

        Set<String> uniqueWords = Arrays.stream(content.split("\\W+"))
                .map(String::toLowerCase)
                .filter(word -> !word.isEmpty())
                .collect(Collectors.toSet());

        context.write(new Text(docId), new Text(String.join(",", uniqueWords)));
    }
}
