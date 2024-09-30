package nz.ac.wgtn.swen301.a3.client;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class Client {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java nz.ac.wgtn.swen301.a3.client.Client <csv|excel> <fileName>");
            System.exit(1);
        }

        String type = args[0].toLowerCase();
        String fileName = args[1];

        if (!type.equals("csv") && !type.equals("excel")) {
            System.err.println("Invalid type. Expected 'csv' or 'excel'.");
            System.exit(1);
        }

        String urlString = "http://localhost:8080/logstore/stats/" + type;

        try {
            URL url = URI.create(urlString).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = connection.getInputStream();
                     FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                }
            } else {
                System.err.println(responseCode + ": Server is not available.");
                System.exit(1);
            }
            System.out.println("File created successfully.");
        } catch (IOException e) {
            System.err.println("Error writing data to a file: " + e.getMessage());
            System.exit(1);
        }
    }
}
