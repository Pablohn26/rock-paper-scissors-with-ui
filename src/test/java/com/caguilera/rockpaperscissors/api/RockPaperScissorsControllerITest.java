package com.caguilera.rockpaperscissors.api;

import com.caguilera.rockpaperscissors.core.Shape;
import com.caguilera.rockpaperscissors.util.BaseWebIntegrationTest;
import com.caguilera.rockpaperscissors.util.WebIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@WebIntegrationTest
@DisplayName("RockPaperScissorsController")
class RockPaperScissorsControllerITest extends BaseWebIntegrationTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("POST /rps/play")
    class PlayTurn {


        @Test
        @DisplayName("given valid request must return 200 with game results")
        void validRequest() throws JsonProcessingException {

            given()
                    .contentType(ContentType.JSON)
                    .body(validPlayRequest())
                    .when()
                    .post(getUrl("/play"))
                    .then()
                    .statusCode(200)
                    .body(hasSameContentAs("validPlayRequest.json"));
        }

        @Test
        @DisplayName("given invalid choice for player 1 then it should return 400")
        void badRequest_invalidPlayer1Choice() throws JsonProcessingException {

            given()
                    .contentType(ContentType.JSON)
                    .body(invalidPlayer1Choice())
                    .when()
                    .post(getUrl("/play"))
                    .then()
                    .statusCode(400)
                    .log().all()
                    .body(hasSameContentAs("invalidRequest_player1Choice.json").ignoring("timestamp"));
        }

        private String invalidPlayer1Choice() throws JsonProcessingException {
            PlayRequestDto playRequest = new PlayRequestDto();

            playRequest.setGameId(1001);
            playRequest.setPlayer1Choice(null);
            playRequest.setPlayer2Choice(Shape.ROCK);

            return objectMapper.writeValueAsString(playRequest);
        }

        private String validPlayRequest() throws JsonProcessingException {

            PlayRequestDto playRequest = new PlayRequestDto();

            playRequest.setGameId(25);
            playRequest.setPlayer1Choice(Shape.PAPER);
            playRequest.setPlayer2Choice(Shape.ROCK);

            return objectMapper.writeValueAsString(playRequest);
        }


    }

}