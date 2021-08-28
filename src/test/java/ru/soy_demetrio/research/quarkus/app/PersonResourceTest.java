package ru.soy_demetrio.research.quarkus.app;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.ValidatableResponse;
import ru.soy_demetrio.research.quarkus.domain.Person;

@QuarkusTest
public class PersonResourceTest {

    @Test
    public void testRetrieveSingle() {
        given()
          .get("/rest/person/3")
          .then()
             .statusCode(200)
             .body(
            		 "firstName", equalTo("Сидор"), 
            		 "familyName", equalTo("Сидоров")
            	);
    }


    @Test
    public void testInsert() {
    	Person person = Person.builder().firstName("Сергей").familyName("Сергеев").build();
    	
        given()
        	.header("Content-Type", "application/json").body(person).post("/rest/person")
        	.then().statusCode(200);
    }
    
    @Test
    public void testUpdate() {
    	Person person = Person.builder().id(1L).firstName("Иван").familyName("Иванофф").build();
    	
        given()
        	.header("Content-Type", "application/json").body(person).post("/rest/person")
        	.then().statusCode(200).body(equalTo("1"));
    }
    
    

    // TODO Перевести тесты на in-memory базу, заполнять её каждый раз.
//    @Test
//    public void testRetrieveMultiple() {
//    	ValidatableResponse response = given().get("/rest/persons").then();
//    	response.statusCode(200);
//    	assertThat(response.extract().jsonPath().getList("$").size(), true);
//             .body("size()", is(5));
//    }
//
//    @Test
//    public void testDelete() {
//    	
//        given()
//        	.delete("/rest/person/5")
//        	.then().statusCode(204);
//    }
    
    
    
}