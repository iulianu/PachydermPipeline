package com.example.pipeline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class MapStage {
    MapStage(File file, File outputRoot) {
        this.file = file;
        this.outputRoot = outputRoot;
    }

    private File file;
    private File outputRoot;

    void execute() {
        try {
            System.out.println("Counting words in file " + file.getAbsolutePath());
            var wordMap = new HashMap<String, Integer>();
            var input = new Scanner(file);
            input.useDelimiter("\\W+");
            while(input.hasNext()) {
                var word = input.next();
                if(wordMap.containsKey(word)) {
                    wordMap.put(word, wordMap.get(word) + 1);
                } else {
                    wordMap.put(word, 1);
                }
            }
            for(var word : wordMap.keySet()) {
                try(var outpw = new PrintWriter(new FileOutputStream(new File(outputRoot, word), true))) {
                    outpw.print(wordMap.get(word));
                }
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        var inputRoot = new File(args[0]);
        var outputRoot = new File(args[1]);
        for(var file : inputRoot.listFiles()) {
            new MapStage(file, outputRoot).execute();
        }
    }
}
