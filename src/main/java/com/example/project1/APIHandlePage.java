package com.example.project1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class APIHandlePage {
    public static void main(String[] args) {
        try {
            for (int i = 1; i <= 1; i++) {
                URL url = new URL("https://adhamkhaled74.github.io/host_api/temp.json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    System.out.println(response.toString());

                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(response.toString());



//
                    // Write JSON to a file
                    String fileName =  "temp.json";
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(  "C:\\Users\\Ahmed\\IdeaProjects\\project1\\src\\main\\"+File.separator + fileName))) {
                        writer.write(jsonNode.toPrettyString());
                        System.out.println("JSON data saved to: "+ File.separator + fileName);
                    }

                    File file =new File("C:\\Users\\Ahmed\\IdeaProjects\\project1\\src\\main\\temp.json");
                    APIResponse temp= objectMapper.readValue(file, APIResponse.class);
                    List<APIModel[]> yy=temp.getData();

                    System.out.println(temp.getStatus());
                    for(APIModel[] data:temp.getData()){
                        for (int j=0;j<data.length;j++){
                            System.out.println(data[j].getGrade());
                            System.out.println(data[j].getSubject());
                        }
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}