package com.place.jogodecartas.controller;

import com.place.jogodecartas.model.Game;
import com.place.jogodecartas.model.Player;
import com.place.jogodecartas.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GameController {

    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/create")
    public String create(@RequestParam List<String> players) {
        Game g = service.createGame(players);
        return "redirect:/game/" + g.getId() + "?p=" + players.get(0);
    }

    @GetMapping("/game/{id}")
    public String game(@PathVariable String id,
                       @RequestParam String p,
                       @RequestParam(required = false) String msg,
                       Model model) {
        Game g = service.get(id);
        if (g == null) return "redirect:/?error=notfound";

        model.addAttribute("game", g);
        model.addAttribute("self", g.findPlayer(p));
        model.addAttribute("table", g.getTable());
        model.addAttribute("scores", g.getScores());
        if (msg != null) model.addAttribute("msg", msg);
        return "game";
    }

    @PostMapping("/game/{id}/play")
    public String play(@PathVariable String id,
                       @RequestParam String p,
                       @RequestParam int card) {
        service.play(id, p, card);

        Game g = service.get(id);
        String next = (g != null) ? g.getCurrentPlayerName() : p;

        return "redirect:/game/" + id + "?p=" + next;
    }

    @PostMapping("/game/{id}/draw")
    public String draw(@PathVariable String id,
                       @RequestParam String p) {
        service.draw(id, p);
        return "redirect:/game/" + id + "?p=" + p;
    }

    @PostMapping("/game/{id}/pass")
    public String passTurn(@PathVariable String id, @RequestParam String p) {
        Game g = service.get(id);

        // Checa se realmente pode passar a vez!
        if (g != null && !g.podePassar()) {
            // Se não pode passar, redireciona de volta e exibe a mensagem
            String mensagem = "Você deve comprar ao menos uma carta antes de passar a vez!";
            return "redirect:/game/" + id + "?p=" + p + "&msg=" + mensagem;
        }

        // Passa a vez normalmente
        service.passTurn(id);

        String next = (g != null) ? g.getCurrentPlayerName() : p;
        return "redirect:/game/" + id + "?p=" + next;
    }
    @PostMapping("/game/{id}/next")
public String nextRound(@PathVariable String id, @RequestParam String p) {
    service.nextRound(id);
    // Redireciona para o jogador da vez (o próximo a jogar)
    Game g = service.get(id);
    String next = (g != null) ? g.getCurrentPlayerName() : p;
    return "redirect:/game/" + id + "?p=" + next;
}

}
