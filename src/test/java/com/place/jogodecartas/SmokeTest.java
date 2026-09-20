package com.place.jogodecartas;

import com.place.jogodecartas.model.Game;
import com.place.jogodecartas.service.GameService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SmokeTest {

    @Test
    void fluxoBasicoFunciona() {
        GameService svc = new GameService();
        Game jogo = svc.createGame(List.of("a", "b"));

        // cada jogador recebeu 5 cartas
        jogo.getPlayers().forEach(p -> assertEquals(5, p.getHand().size()));

        // jogador 'a' compra uma carta
        svc.draw(jogo.getId(), "a");
        assertEquals(6, jogo.findPlayer("a").getHand().size());

        // forçar término consumindo baralho
        while (!jogo.isFinished()) {
            svc.draw(jogo.getId(), "a");
            svc.draw(jogo.getId(), "b");
        }
        assertTrue(jogo.isFinished());
    }
}


