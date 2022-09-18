import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Random;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestAssuredTest {
    public static String boardId;
    public static String listId;
    public static String cardToDelete;
    String key = "53986634fbfca4c28899d25dd22b271e";
    String token = "e5345d73bf2db5fbb5411f198d35a813ce94e1106acea228890be69b15945256";

    @Test
    @Order(1)
    public void createBoardTest() {
        JSONObject expBody = new JSONObject();
        expBody.put("name", "Test Board Final");
        expBody.put("key", key);
        expBody.put("token", token);

        Response response = given().
                contentType(ContentType.JSON).
                body(expBody.toString()).
                when().
                post("https://api.trello.com/1/boards/");

        JsonPath actBody = response.jsonPath();
        this.boardId = actBody.get("id");
    }

    @Test
    @Order(2)
    public void createList() {
        JSONObject expBody = new JSONObject();
        expBody.put("name", "Test List");
        expBody.put("idBoard", this.boardId);
        expBody.put("key", key);
        expBody.put("token", token);

        Response response = given().
                contentType(ContentType.JSON).
                body(expBody.toString()).
                when().
                post("https://api.trello.com/1/lists/");

        JsonPath actBody = response.jsonPath();
        this.listId = actBody.get("id");

    }

    @Test
    @Order(3)
    public void createCard() throws InterruptedException {
        JSONObject expBody = new JSONObject();
        expBody.put("idList", this.listId);
        expBody.put("key", key);
        expBody.put("token", token);
        expBody.put("name", "Test Card");

        JSONObject expBody2 = new JSONObject();
        expBody2.put("idList", this.listId);
        expBody2.put("key", key);
        expBody2.put("token", token);
        expBody2.put("name", "Test Card2");


        Response firstCardResponse = given().
                contentType(ContentType.JSON).
                body(expBody.toString()).
                when().
                post("https://api.trello.com/1/cards/");
        JsonPath actBody = firstCardResponse.jsonPath();
        String firstId = actBody.get("id");

        Response secondCardResponse = given().
                contentType(ContentType.JSON).
                body(expBody2.toString()).
                when().
                post("https://api.trello.com/1/cards/");
        JsonPath actBody2 = secondCardResponse.jsonPath();
        String secondId = actBody2.get("id");

        Random random = new Random();
        int cardOrder = random.nextInt(2);
        String[] cardIds = {firstId, secondId};
        this.cardToDelete = cardIds[cardOrder];


    }

    @Test
    @Order(4)
    public void deleteRandomCard() throws InterruptedException {
        Thread.sleep(3000);

        JSONObject expBody = new JSONObject();
        expBody.put("key",  key);
        expBody.put("token", token);
        given().
                contentType(ContentType.JSON).
                body(expBody.toString()).
                when().
                delete("https://api.trello.com/1/cards/"+this.cardToDelete);
    }

    @Test
    @Order(5)
    public void deleteBoard() throws InterruptedException {
        Thread.sleep(5000);

        JSONObject expBody = new JSONObject();
        expBody.put("key",  key);
        expBody.put("token", token);
        given().
                contentType(ContentType.JSON).
                body(expBody.toString()).
                when().
                delete("https://api.trello.com/1/boards/"+this.boardId);
    }
}