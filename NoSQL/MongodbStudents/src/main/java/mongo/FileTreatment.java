package mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;

public class FileTreatment {

public static final String PATH = "src/main/resources/mongo.csv";

    public static void main(String[] args) {

        //Инициализация работы с Mongodb
        MongoClient mongoClient = new MongoClient( "127.0.0.1" , 27017 );
        MongoDatabase database = mongoClient.getDatabase("local");
        MongoCollection<Document> collection = database.getCollection("studentsAll");
        collection.drop();

        readFileIntoCollection(collection);

        //Запрос на общее количество студентов в базе данных
       /* AtomicInteger count = new AtomicInteger(0);
        collection.find().forEach((Consumer<Document>) document -> {
            count.getAndIncrement();
        });*/
        System.out.println("Количество студентов в базе данных: " + collection.countDocuments());

        //Запрос на количество студентов старше 40 лет

        BsonDocument querySec = BsonDocument.parse("{Age: {$gt : 40} }");
        System.out.println("Количество студентов в базе данных старше 40 лет: " + collection.countDocuments(querySec));
       /*AtomicInteger countSec = new AtomicInteger(0);

        collection.find(querySec).forEach((Consumer<Document>) document -> {
            countSec.getAndIncrement();
        });*/
        
        //Запрос на имя самого молодого студента
       FindIterable<Document> docList = collection.find().sort(ascending("Age")).limit(1);
        String young = "";
        for(Document doc : docList) {
            young  = String.valueOf(doc.get("Name"));
        }
        System.out.println("Имя самого молодого студента: " + young);

        //Запрос на список курсов самого старого студента
        FindIterable<Document> docListSecond = collection.find().sort(descending("Age")).limit(1);
        String old = "";
        for(Document doc : docListSecond) {
            old = String.valueOf(doc.get("Courses"));
            System.out.println("Список курсов самого старого студента: " + old);
        }

    }

    private static void readFileIntoCollection(MongoCollection<Document> collection) {
        List<String> lines = new ArrayList<>();
        try {
          lines   = Files.readAllLines(Path.of(PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Запись содержимого файла в базу данных
        for(String line : lines){
            String[] words = line.split("[,]",3);
            String name = words[0];
            int age = Integer.parseInt(words[1]);
            String courseString = words[2].substring(1,words[2].length()-1);
            String[] courses = courseString.split("[,]");

            Document document = new Document()
                    .append("Name", name)
                    .append("Age", age)
                    .append("Courses", Arrays.stream(courses).collect(Collectors.toList()));
            collection.insertOne(document);
        }
    }

}
