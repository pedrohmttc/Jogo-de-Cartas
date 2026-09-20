package com.place.jogodecartas.service;

import com.place.jogodecartas.model.Game;
import com.place.jogodecartas.model.Player;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Camada de serviço: mantém as partidas na memória
 * e delega todas as ações à classe Game.
 */
@Service
public class GameService {

    /** partidas ativas – chave = id da partida */
    private final Map<String, Game> games = new ConcurrentHashMap<>();

    /* ---------- CRUD da partida ---------- */

    public Game createGame(List<String> playerNames) {
        Game g = new Game(playerNames);
        games.put(g.getId(), g);
        return g;
    }

    public Game get(String id) {
        return games.get(id);
    }

    /* ---------- Ações de jogo ---------- */

    public void play(String gameId, String playerId, int cardIdx) {
        Game g = get(gameId);
        if (g == null) return;
        Player p = g.findPlayer(playerId);
        if (p != null) g.playCard(p, cardIdx);
    }

    public void draw(String gameId, String playerId) {
        Game g = get(gameId);
        if (g == null) return;
        Player p = g.findPlayer(playerId);
        if (p != null) g.drawCard(p);
    }

    /** passar a vez manualmente (se as regras permitirem) */
    public void passTurn(String id) {
        Game g = get(id);
        if (g != null) g.passTurn();
    }

    /** inicia a próxima rodada quando a mesa foi limpa (21 ou estouro) */
    public void nextRound(String id) {
        Game g = get(id);
        if (g != null) g.startNextRound();
    }

    /* ---------- consulta de vencedores ---------- */

    public List<Player> winners(String id) {
        Game g = get(id);
        return (g == null || !g.isFinished()) ? List.of() : g.getWinners();
    }
}
