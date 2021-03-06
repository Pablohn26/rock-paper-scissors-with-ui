package com.caguilera.rockpaperscissors.service;


import com.caguilera.rockpaperscissors.core.GameStatistics;
import com.caguilera.rockpaperscissors.core.Result;
import com.caguilera.rockpaperscissors.core.RockPaperScissorsGame;
import com.caguilera.rockpaperscissors.core.Shape;
import com.caguilera.rockpaperscissors.dto.PlayRequestDto;
import com.caguilera.rockpaperscissors.dto.GameResultDto;
import com.caguilera.rockpaperscissors.dto.StatisticsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RockPaperScissorsGameService {

    private final RockPaperScissorsGame game;
    private final GameStatistics statistics;

    @Autowired
    public RockPaperScissorsGameService(RockPaperScissorsGame game, GameStatistics statistics) {
        this.game = game;
        this.statistics = statistics;
    }

    public GameResultDto playRound(PlayRequestDto playRequest) {
        int gameId = playRequest.getGameId();
        Shape player1Choice = playRequest.getPlayer1Choice();
        Shape player2Choice = playRequest.getPlayer2Choice();

        return playRound(gameId, player1Choice, player2Choice);
    }

    private GameResultDto playRound(int gameId, Shape player1Choice, Shape player2Choice) {

        Result result = game.play(player1Choice, player2Choice);

        statistics.registerRound(gameId, result);

        return GameResultDto.from(gameId, result, statistics);
    }


    public StatisticsDto getStatistics() {
        return StatisticsDto.from(statistics);
    }
}
