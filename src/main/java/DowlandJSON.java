import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class DowlandJSON {

        public static final String POSTS_URI = "https://jsonplaceholder.typicode.com/posts";

        public static void main(String[] args) throws IOException, InterruptedException {

            try {

                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(POSTS_URI))
                        .build();
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .join();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                ObjectMapper mapper = new ObjectMapper();
                List<Post> posts = mapper.readValue(response.body(), new TypeReference<List<Post>>() {});

                //create own path
                String path = CreateSavePath();
                posts.forEach(post -> {
                    SaveToFile(post, path);
                });
            } catch(IOException e){
                e.printStackTrace();
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        // class to create own path and return it
        public static void SaveToFile (Post post, String dir) {

            try {

                String content = post.toString();
                String filename = String.valueOf(post.id);
                String path = dir + "\\"  + filename + ".json";
                File file = new File(path);

                if (!file.exists()) {
                    if(file.createNewFile()){
                        System.out.println("JSON file has been successfully created");
                    }
                    else {
                        System.out.println("File failed to be created");
                    }
                }
                else{
                    System.out.println("File already exist");
                }

                FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());

                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(content);
                bufferedWriter.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }


        // this class is for save our JSON Files to our path
        public static String CreateSavePath() {

            String currentPath = System.getProperty("user.dir");
            File theDir = new File(currentPath + "/JSON/");
            if(!theDir.exists()){
                if(theDir.mkdirs()){
                    System.out.println("The directory has been created successfully");
                }else{
                    System.out.println("Path failed to be created");
                }
            }else{
                System.out.println("Path already exist");
            }
            System.out.println(currentPath);
            System.out.println(theDir.getPath());
            return theDir.getPath();

        }
    }

