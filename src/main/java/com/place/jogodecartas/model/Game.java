package com.place.jogodecartas.model;

import java.util.*;

public class Game {

    private final String id              = UUID.randomUUID().toString();
    private final Deck   deck            = new Deck();
    private final List<Player> players   = new ArrayList<>();

    private int  currentPlayerIndex = 0;
    private boolean finished        = false;

    private final Table table                 = new Table();
    private final Map<Player,Integer> scores  = new HashMap<>();

    private String  currentPlayerName;
    private String  ultimaMensagem   = "";
    private boolean comprouCartaNoTurno = false;

    /** quando true a rodada está pausada – exibe botão “Próxima rodada” */
    private boolean roundPaused = false;
    private int roundNumber = 1;

    public int getRoundNumber() { return roundNumber; }


    /* ---------- construtor ---------- */
    public Game(List<String> playerNames) {
        deck.shuffle();
        for (String n: playerNames) {
            Player p = new Player(n.trim());
            players.add(p);
            scores.put(p,0);
        }
        // cinco cartas iniciais
        for (int i=0;i<5;i++)
            for (Player p:players)
                Optional.ofNullable(deck.draw()).ifPresent(p::addCard);

        currentPlayerName = players.get(0).getName();
    }

    /* ---------- ações ---------- */

    /** jogar carta */
  public void playCard(Player p, int idx) {
    if (finished || roundPaused || !isTurn(p)) return;

    Card c = p.getHand().remove(idx);
    p.setLastCard(c);
    table.play(c);
    comprouCartaNoTurno = false; // jogou => ainda não comprou

    int sum = table.getSum();
    if (sum == 21) {
        scores.merge(p, 1, Integer::sum);  // soma 1 ponto ao jogador
        ultimaMensagem = "Parabéns! " + p.getName() + " somou 21 e fez um ponto!";
        endRound();
    } else if (sum > 21) {
        ultimaMensagem = "Estourou 21! Mesa limpa e próxima rodada.";
        endRound();
    } else {
        passarVezAuto(); // passa a vez normalmente
    }

    checkGameEnd(p);
}


    /** comprar carta */
    public void drawCard(Player p){
        if (finished || roundPaused || !isTurn(p)) return;

        Optional.ofNullable(deck.draw()).ifPresent(p.getHand()::add);
        comprouCartaNoTurno = true;
        ultimaMensagem = "";
        checkGameEnd(p);
    }

    /** passa vez explicitamente (só se permitido) */
    public void passTurn(){
        if (finished || roundPaused || !podePassar()) return;
        passarVezAuto();
        ultimaMensagem = "";
    }

    /** botão “próxima rodada” chama aqui */
    public void startNextRound(){
    if (!roundPaused || finished) return;
    roundPaused = false;
    comprouCartaNoTurno = false;
    ultimaMensagem = "";
    roundNumber++; // incrementa rodada
}

    /* ---------- regras auxiliares ---------- */

    private void endRound(){
        table.clear();
        roundPaused = true;              // exibe botão no front
        comprouCartaNoTurno = false;
        passarVezAuto();                 // já define próximo a começar
    }

    private void passarVezAuto(){
        currentPlayerIndex = (currentPlayerIndex+1)%players.size();
        currentPlayerName  = players.get(currentPlayerIndex).getName();
        comprouCartaNoTurno = false;
    }

    private boolean isTurn(Player p){
        return p.getName().equalsIgnoreCase(currentPlayerName);
    }

    /** só pode passar se NÃO tem jogada válida e já comprou carta */
    public boolean podePassar(){
        Player p = players.get(currentPlayerIndex);
        boolean temJogada = p.getHand().stream()
                             .anyMatch(c -> table.getSum()+c.getValue() <=21);
        return !temJogada && comprouCartaNoTurno;
    }

    /** verifica se acaba por falta de cartas ou mão vazia */
    private void checkGameEnd(Player p){
        if (p.getHand().isEmpty() || deck.remaining()<players.size()){
            finished        = true;
            roundPaused     = false;
            ultimaMensagem  = "Jogo finalizado!";
        }
    }

    /* ---------- getters usados na view ---------- */
    public String getId(){return id;}
    public List<Player> getPlayers(){return players;}
    public Table getTable(){return table;}
    public String getCurrentPlayerName(){return currentPlayerName;}
    public boolean isFinished(){return finished;}
    public boolean isRoundPaused(){return roundPaused;}
    public boolean getPodePassar(){return podePassar();}
    public String getUltimaMensagem(){return ultimaMensagem;}
    public int getRemainingCards(){return deck.remaining();}
    public int getScore(Player p){return scores.getOrDefault(p,0);}   // <-- facil p/ Thymeleaf

    public List<Player> getWinners(){
        int max = scores.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        final int m = max;
        return scores.entrySet().stream()
                     .filter(e -> e.getValue()==m)
                     .map(Map.Entry::getKey)
                     .toList();
    }
        // ... resto da classe ...

    public Player findPlayer(String name) {
        return players.stream()
                .filter(pl -> pl.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Map<Player, Integer> getScores() { 
    return scores; }
   


}


