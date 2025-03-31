package com.microservices.saliiov.song.song_service.component.stepdefinitions;

import com.microservices.saliiov.song.song_service.dto.SongDto;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = "eureka.client.enabled=false")
@ActiveProfiles("test")
public class CreateSongStepDefinition {

    private SongDto songDto;
    private Response response;

    @Given("the user provides a valid song creation request with:")
    public void theUserProvidesAValidSongCreationRequestWith(DataTable dataTable) {
        songDto = getSongDto(dataTable);
    }


    @When("the user submits the registration request to the API endpoint {string}")
    public void theUserSubmitsTheRegistrationRequestToTheAPIEndpoint(String endpoint) {
        String fullUrl = "http://localhost:8080" + endpoint;
        response = RestAssured.given()
                .contentType("application/json")
                .body(songDto)
                .when()
                .post(fullUrl);
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int status) {
        response.then().statusCode(status);
    }

    @And("the response body should contain:")
    public void theResponseBodyShouldContain(DataTable dataTable) {
        Map<String, Integer> expectedResponse = dataTable.asMap(String.class, Integer.class);
        expectedResponse.forEach((key, value) -> response.then().body(key, CoreMatchers.is(value)));
    }

    private static SongDto getSongDto(DataTable dataTable) {
        Map<String, String> params = dataTable.asMap(String.class, String.class);
        return SongDto.builder()
                .name(params.get("name"))
                .artist(params.get("artist"))
                .album(params.get("album"))
                .length(params.get("length"))
                .year(params.get("year"))
                .resourceId(Long.parseLong(params.get("resourceId")))
                .build();
    }
}