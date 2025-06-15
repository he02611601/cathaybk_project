package cathaybk.project.controller;

import cathaybk.project.entity.Currencies;
import cathaybk.project.repository.CurrenciesRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currencies")
public class CurrenciesController {
    private final CurrenciesRepository repo;

    public CurrenciesController(CurrenciesRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Currencies Controller!";
    }

    @GetMapping
    public List<Currencies> allCurrencies() {
        return repo.findAll();
    }
}
