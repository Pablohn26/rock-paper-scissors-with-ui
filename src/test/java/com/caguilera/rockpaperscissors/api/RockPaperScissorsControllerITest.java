package com.caguilera.rockpaperscissors.api;

import com.caguilera.rockpaperscissors.core.GameStatistics;
import com.caguilera.rockpaperscissors.core.Shape;
import com.caguilera.rockpaperscissors.dto.PlayRequestDto;
import com.caguilera.rockpaperscissors.util.BaseWebIntegrationTest;
import com.caguilera.rockpaperscissors.util.WebIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;

@WebIntegrationTest
@DisplayName("RockPaperScissorsController")
class RockPaperScissorsControllerITest extends BaseWebIntegrationTest {

    @Autowired
    private GameStatistics gameStatistics;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        gameStatistics.resetStats();
    }

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
        @DisplayName("given invalid choice for player 1 then it must return 400")
        void badRequestWithInvalidPlayer1Choice() throws JsonProcessingException {

            given()
                    .contentType(ContentType.JSON)
                    .body(invalidPlayer1Choice())
                    .when()
                    .post(getUrl("/play"))
                    .then()
                    .statusCode(400)
                    .body(hasSameContentAs("invalidRequest_player1Choice.json").ignoring("timestamp"));
        }

        @Test
        @DisplayName("given invalid choice for player 2 then it must return 400")
        void badRequestWithInvalidPlayer2Choice() throws JsonProcessingException {

            given()
                    .contentType(ContentType.JSON)
                    .body(invalidPlayer2Choice())
                    .when()
                    .post(getUrl("/play"))
                    .then()
                    .statusCode(400)
                    .body(hasSameContentAs("invalidRequest_player2Choice.json").ignoring("timestamp"));
        }

        @Test
        @DisplayName("given invalid choices then it must return 400")
        void badRequestWithInvalidChoices() throws JsonProcessingException {

            given()
                    .contentType(ContentType.JSON)
                    .body(invalidPlayerChoices())
                    .when()
                    .post(getUrl("/play"))
                    .then()
                    .statusCode(400);
        }

        private String invalidPlayerChoices() throws JsonProcessingException {
            PlayRequestDto playRequest = new PlayRequestDto();

            playRequest.setGameId(1001);
            playRequest.setPlayer1Choice(null);
            playRequest.setPlayer2Choice(null);

            return objectMapper.writeValueAsString(playRequest);
        }

        private String invalidPlayer2Choice() throws JsonProcessingException {
            PlayRequestDto playRequest = new PlayRequestDto();

            playRequest.setGameId(25);
            playRequest.setPlayer1Choice(Shape.PAPER);
            playRequest.setPlayer2Choice(null);

            return objectMapper.writeValueAsString(playRequest);
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


    @Nested
    @DisplayName("GET /rps/statistics")
    class GetStatistics {


        @Test
        @DisplayName("getStatistics returns 200 along with StatisticsDto")
        public void retrievesStats() throws JsonProcessingException {

            given()
                    .contentType(ContentType.JSON)
                    .body(validPlayRequest())
                    .when()
                    .post(getUrl("/play"))
                    .then()
                    .statusCode(200);

            given()
                    .contentType(ContentType.JSON)
                    .when()
                    .get(getUrl("/statistics"))
                    .then()
                    .statusCode(200)
                    .body(hasSameContentAs("GetStatistics.json"));
        }

        private String validPlayRequest() throws JsonProcessingException {

            PlayRequestDto playRequest = new PlayRequestDto();

            playRequest.setGameId(10);
            playRequest.setPlayer1Choice(Shape.PAPER);
            playRequest.setPlayer2Choice(Shape.ROCK);

            return objectMapper.writeValueAsString(playRequest);
        }

    }

}