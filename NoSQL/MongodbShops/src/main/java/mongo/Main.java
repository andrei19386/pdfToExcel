package mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.Document;


import java.util.*;


public class Main {

    public static void executionOfAction(ActionType action, String string,
                                         MongoCollection<Document> collectionShops,
                                         MongoCollection<Document> collectionGoods) {
        String data;
        Document document;
        String[] words;
        switch (action) {
            case ADD_SHOP:
                data = string.replaceFirst("[A][D][D][_][S][H][O][P] ", "");
                document = new Document()
                        .append("Shop_name", data);
                collectionShops.insertOne(document);
                break;
            case ADD_GOODS:
                data = string.replaceFirst("[A][D][D][_][G][O][O][D][S] ", "");
                words = data.split("[ ]");
                int price = Integer.parseInt(words[1]);
                String goodsName = words[0];
                document = new Document()
                        .append("Goods_name", goodsName)
                        .append("Goods_price",price);
                collectionGoods.insertOne(document);
                break;
            case EXHIBIT_GOODS:
                data = string.replaceFirst("[E][X][H][I][B][I][T][_][G][O][O][D][S] ", "");
                words = data.split("[ ]");
                String goods = words[0];
                String shop = words[1];
                exhibit(collectionShops, goods, shop);
                break;
            case STATISTICS:
                statistics(collectionShops);
                break;
            case SYNTAX:
                syntaxInfo();
                break;
            default:
                System.out.println("Неизвестная команда!");
                syntaxInfo();
                break;
        }
    }

    private static void syntaxInfo() {
        System.out.println("Известные форматы команд:");
        System.out.println("ADD_SHOP <shop_name>");
        System.out.println("ADD_GOODS <goods_name> <goods_price>");
        System.out.println("EXHIBIT_GOODS <goods_name> <shop_name>");
        System.out.println("STATISTICS");
        System.out.println("SYNTAX");
    }

    private static void statistics(MongoCollection<Document> collectionShops) {
        List<BsonDocument> bson = getBson(); //Формируем запрос
        AggregateIterable<Document> aggregateIterable = collectionShops
                .aggregate(bson);//Выполняем команду mongoDb
        Map<String,Shop> shopMap = new HashMap<>();
        fillingMap(aggregateIterable, shopMap); // Заполняем предварительными сведениями Map
        // по результатам запроса
        bson = getBsonLess100();//Новым запросом определяем количество товаров дешевле 100 р.
        aggregateIterable = collectionShops.aggregate(bson);
        fillingMapLessBy100(aggregateIterable, shopMap);
        for(Map.Entry<String, Shop> entry : shopMap.entrySet()) {
            findCheapName(collectionShops, entry);//Находим имя дешевого товара
            findExpensiveName(collectionShops, entry); //Находим имя дорогого товара
            printInfo(entry);
        }
    }

    private static void exhibit(MongoCollection<Document> collectionShops, String goods, String shop) {
        BsonDocument bsonDocument =  BsonDocument.parse("{ Shop_name : \"" + shop + "\"}");
        FindIterable<Document> docIterable = collectionShops.find(bsonDocument);
        for(Document doc : docIterable) {
            List<String> list = (List<String>)doc.get("Goods_names");
            if(list==null){
                list = new ArrayList<>();
            }
            list.add(goods);
            doc.put("Goods_names",list);
            collectionShops.replaceOne(bsonDocument,doc);
        }
    }

    private static void findExpensiveName(MongoCollection<Document> collectionShops,
                                          Map.Entry<String, Shop> entry) {
        List<BsonDocument> bson;
        AggregateIterable<Document> aggregateIterable;
        bson = getBsonByPrice(entry.getValue().getMaxPrice());
        aggregateIterable = collectionShops.aggregate(bson);
        entry.getValue().setNameExpensive(aggregateIterable.first()
                .get("Goods").toString());
    }

    private static void findCheapName(MongoCollection<Document> collectionShops,
                                      Map.Entry<String, Shop> entry) {
        List<BsonDocument> bson;
        AggregateIterable<Document> aggregateIterable;
        bson = getBsonByPrice(entry.getValue().getMinPrice());
        aggregateIterable = collectionShops.aggregate(bson);
        entry.getValue().setNameCheap(aggregateIterable.first()
                .get("Goods").toString());
    }

    private static void fillingMapLessBy100(AggregateIterable<Document> aggregateIterable, Map<String, Shop> shopMap) {
        for(Document doc : aggregateIterable) {
            Document newDoc = (Document) doc.get("_id");
            shopMap.get(newDoc.get("name").toString()).
                    setCountLess100(Integer.parseInt(doc.get("countAll").toString()));
        }
    }

    private static void fillingMap(AggregateIterable<Document> aggregateIterable, Map<String, Shop> shopMap) {
        for(Document doc : aggregateIterable) {
            Document newDoc = (Document) doc.get("_id");
            Shop currentShop = new Shop(newDoc.get("name").toString());
            currentShop.setCountGoods(Integer.parseInt(doc.get("countAll").toString()));
            currentShop.setAveragePrice(Double.parseDouble(doc.get("avprice").toString()));
            currentShop.setMinPrice(Integer.parseInt(doc.get("minprice").toString()));
            currentShop.setMaxPrice(Integer.parseInt(doc.get("maxprice").toString()));
            shopMap.put(currentShop.getShopName(),currentShop);
        }
    }

    private static void printInfo(Map.Entry<String, Shop> entry) {
        System.out.println("___________________________________________");
        System.out.println("Имя магазина: " + entry.getValue().getShopName());
        System.out.println("Всего товаров: " + entry.getValue().getCountGoods());
        System.out.println("Средняя цена товара: " + entry.getValue().getAveragePrice());
        System.out.println("Дешевый товар: " + entry.getValue().getNameCheap() + ", цена в рублях : " +
        entry.getValue().getMinPrice());
        System.out.println("Дорогой товар: " + entry.getValue().getNameExpensive() + ", цена в рублях: " +
                entry.getValue().getMaxPrice());
        System.out.println("Количество товаров дешевле 100 р.: " + entry.getValue().getCountLess100());
        System.out.println("___________________________________________");
    }

    private static BsonDocument getBsonLookup() {
        BsonDocument bsonDocument = BsonDocument.parse("{\n" +
                "$lookup: {\n" +
                "from: \"goods\",\n" +
                "localField: \"Goods_names\",\n" +
                "foreignField: \"Goods_name\",\n" +
                "as: \"list\"} },\n"
        );
        return bsonDocument;
    }

    private static BsonDocument getBsonUnwind() {
        BsonDocument bsonDocument = BsonDocument.parse(
                "{$unwind: {path: \"$list\"}\n" +
                        "}"
        );
        return bsonDocument;
    }


    private static List<BsonDocument> getBsonLess100() {
        BsonDocument bsonDocument = getBsonLookup();
        BsonDocument bsonDocument2 = getBsonUnwind();
        BsonDocument bsonDocument3 = BsonDocument.parse(
                "{$match: {\"list.Goods_price\": {$lt: 100}}" +
                        "}"
        );
        BsonDocument bsonDocument4 = BsonDocument.parse(
                "{\n" +
                        "$group: {\n" +
                        "_id: {name: \"$Shop_name\"},\n" +
                        "countAll: {$sum: 1} " +
                        "}\n" +
                        "}");
        List<BsonDocument> bsonDocumentList = new ArrayList<>();
        bsonDocumentList.add(bsonDocument);
        bsonDocumentList.add(bsonDocument2);
        bsonDocumentList.add(bsonDocument3);
        bsonDocumentList.add(bsonDocument4);
        return bsonDocumentList;
    }

    private static List<BsonDocument> getBsonByPrice(int findPrice) {
        BsonDocument bsonDocument = getBsonLookup();
        BsonDocument bsonDocument2 = getBsonUnwind();
        BsonDocument bsonDocument3 = BsonDocument.parse(
                "{$match: {\"list.Goods_price\": {$eq: " + findPrice + "}}" +
                        "}"
        );
        BsonDocument bsonDocument4 = BsonDocument.parse(
                "{\n" +
                        "$group: {\n" +
                        "_id: {name: \"$Shop_name\"},\n" +
                        "Goods: {$max: \"$list.Goods_name\"}" +
                        "countAll: {$sum: 1} " +
                        "}\n" +
                        "}");
        List<BsonDocument> bsonDocumentList = new ArrayList<>();
        bsonDocumentList.add(bsonDocument);
        bsonDocumentList.add(bsonDocument2);
        bsonDocumentList.add(bsonDocument3);
        bsonDocumentList.add(bsonDocument4);
        return bsonDocumentList;
    }


    private static List<BsonDocument> getBson() {
        BsonDocument bsonDocument = getBsonLookup();
        BsonDocument bsonDocument2 = getBsonUnwind();
        BsonDocument bsonDocument3 = BsonDocument.parse(
                "{\n" +
                "$group: {\n" +
                "_id: {name: \"$Shop_name\"},\n" +
                "avprice: {$avg: \"$list.Goods_price\"},\n" +
                "minprice: {$min: \"$list.Goods_price\"},\n" +
                "maxprice: {$max: \"$list.Goods_price\"},\n" +
                "countAll: {$sum: 1} " +
                "}\n" +
                "}");
        List<BsonDocument> bsonDocumentList = new ArrayList<>();
        bsonDocumentList.add(bsonDocument);
        bsonDocumentList.add(bsonDocument2);
        bsonDocumentList.add(bsonDocument3);
        return bsonDocumentList;
    }


    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient( "127.0.0.1" , 27017 );
        MongoDatabase database = mongoClient.getDatabase("local");
        MongoCollection<Document> collectionShops = database.getCollection("shops");
        MongoCollection<Document> collectionGoods = database.getCollection("goods");
       // collectionShops.drop();
       // collectionGoods.drop();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Введите команду: ADD_SHOP, ADD_GOODS, EXHIBIT_GOODS," +
                    "  STATISTICS или SYNTAX");
            String input = scanner.nextLine();
            if (input.equals("0")) {
                break;
            }

            ActionType action = input.matches("[A][D][D][_][S][H][O][P] [A-Za-z0-9_]+")
                    ? ActionType.ADD_SHOP : ActionType.NOTHING;
            action = input.matches("[A][D][D][_][G][O][O][D][S] [A-Za-z0-9_]+ [0-9]+")
                    ? ActionType.ADD_GOODS : action;
            action = input.matches("[E][X][H][I][B][I][T][_][G][O][O][D][S] [A-Za-z0-9_]+ [A-Za-z0-9_]+")
                    ? ActionType.EXHIBIT_GOODS : action;
            action = input.matches("[S][T][A][T][I][S][T][I][C][S]")
                    ? ActionType.STATISTICS : action;
            action = input.matches("[S][Y][N][T][A][X]")
                    ? ActionType.SYNTAX : action;
            executionOfAction(action, input, collectionShops, collectionGoods);
        }

        System.out.println();
    }

}
