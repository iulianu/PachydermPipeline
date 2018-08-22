package com.example.pipeline;

import jdk.incubator.http.HttpClient;
import jdk.incubator.http.HttpRequest;
import jdk.incubator.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Scrape {
    private URI url;
    private Path destination;

    private static String readContents(File file) {
        try {
            byte[] encoded = Files.readAllBytes(file.toPath());
            return new String(encoded).trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Scrape(File urlFile, Path outputRoot) {
        this.url = URI.create(readContents(urlFile));
        this.destination = new File(outputRoot, urlFile.getName());
    }

    private void execute() {
        System.out.println("Scraping URL " + url + " into file " + destination);

        var request = HttpRequest.newBuilder()
                .uri(url).GET().build();
        try {
            HttpClient.newBuilder().build()
                .send(request,
                        HttpResponse.BodyHandler.asFile(destination.toPath()));
        } catch(IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        var inputRoot = Paths.get(args[0]);
        var outputRoot = Paths.get(args[1]);
        for(var file : inputRoot.toFile().listFiles()) {
            new Scrape(file, outputRoot).execute();
        }
    }
}
