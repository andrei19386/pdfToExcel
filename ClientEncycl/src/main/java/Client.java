import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class Client {
    private static String login;
    private static String password;


    public static void main(String[] args)
            throws IOException {
        String baseUrl = null;
        String sourcePath = null;

        Map<String, String> envVariables = System.getenv();


        try (
                InputStream input = new FileInputStream(
                        "src/main/resources/config.properties"
                )) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);


            baseUrl = prop.getProperty("baseUrl");
            sourcePath = prop.getProperty("sourcePath");
            login = System.getenv("login");
            password = System.getenv("password");



        } catch (IOException ex) {
            ex.printStackTrace();
        }




        String[] words = sourcePath.split(File.separator);
        String sourceShort = words[words.length - 1];
        int len = sourceShort.length();


        List<String> pathList = new ArrayList<>();

        Files.walk(Paths.get(sourcePath))
                .forEach(source -> {
                    pathList.add(source.toString());
                });
        Long modelId = addNewModelToService(baseUrl, sourceShort);


        for(String path : pathList){
            CloseableHttpClient client = getCloseableHttpClient(baseUrl);
            Path destination = Paths.get("IOS/3DModels",
            path.substring(sourcePath.length() - len));
            File  file = new File(path);
            CloseableHttpResponse response1 = null;
            if(!file.isDirectory()){
                response1 = getCloseableHttpResponseForFile(baseUrl, client, destination, file, response1,modelId);
            } else {
                response1 = getHttpResponseForDirectory(baseUrl, client, destination, file, response1);
            }
            System.out.println(response1.getStatusLine().getStatusCode());
            client.close();
        }

    }

    private static Long addNewModelToService(String baseUrl, String sourceShort) throws IOException {
        CloseableHttpClient client0 = getCloseableHttpClient(baseUrl);
        HttpPost httpPost = new HttpPost(baseUrl + "/api/model/createModel");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("modelName", sourceShort);

        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);
        CloseableHttpResponse response0 = null;
        Long modelId=null;
        try {
            response0 = client0.execute(httpPost);
            String response = EntityUtils.toString(response0.getEntity(), "UTF-8");
            modelId = Long.parseLong(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client0.close();
        return modelId;
    }

    private static CloseableHttpResponse getHttpResponseForDirectory(String baseUrl,
                                                                     CloseableHttpClient client,
                                                                     Path destination, File file,
                                                                     CloseableHttpResponse response1) {
        HttpPost httpPost = new HttpPost(baseUrl + "/createFolder");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("path", destination.getParent().toString());
        builder.addTextBody("folderName",
                file.getName());
        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);
        try {
            response1 = client.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response1;
    }

    private static CloseableHttpResponse getCloseableHttpResponseForFile(String baseUrl,
                                                                         CloseableHttpClient client,
                                                                         Path destination, File file,
                                                                         CloseableHttpResponse response1,
                                                                         Long modelId) {
        HttpPost httpPost = new HttpPost(baseUrl + "/uploadToPath");

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("path", destination.getParent().toString());
        builder.addTextBody("model_id",Long.toString(modelId));
        builder.addBinaryBody(
        "file", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);

        try {
        response1 = client.execute(httpPost);
            System.out.println(destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response1;
    }

    private static CloseableHttpClient getCloseableHttpClient(String baseUrl) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        HttpPost httpPost1 = new HttpPost(baseUrl + "/api/login");
        StringEntity params =
                new StringEntity("{\"email\":\"" + login + "\",\"password\": \"" + password + "\"}");
        httpPost1.addHeader("content-type", "application/json");
        httpPost1.setEntity(params);
        CloseableHttpResponse response = client.execute(httpPost1);
        System.out.println(response.getStatusLine().getStatusCode());
        return client;
    }
}