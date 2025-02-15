package com.example;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;
import java.util.*;

public class DocumentSimilarityReducer extends Reducer<Text, Text, Text, Text> {

    private static class Document {
        String id;
        Set<String> words;

        Document(String id, Set<String> words) {
            this.id = id;
            this.words = words;
        }
    }

    private List<Document> documents = new ArrayList<>();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        for (Text value : values) {
            Set<String> wordSet = new HashSet<>(Arrays.asList(value.toString().split(",")));
            documents.add(new Document(key.toString(), wordSet));
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        for (int i = 0; i < documents.size(); i++) {
            for (int j = i + 1; j < documents.size(); j++) {
                Document doc1 = documents.get(i);
                Document doc2 = documents.get(j);

                double similarity = calculateJaccardSimilarity(doc1.words, doc2.words);

                if (similarity > 0.5) {
                    String pair = String.format("(%s, %s)", doc1.id, doc2.id);
                    String similarityPercentage = String.format("%.2f%%", similarity * 100);
                    context.write(new Text(pair), new Text(similarityPercentage));
                }
            }
        }
    }

    private double calculateJaccardSimilarity(Set<String> set1, Set<String> set2) {
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        return (double) intersection.size() / union.size();
    }
}
