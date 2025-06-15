package cathaybk.project.controller;

import cathaybk.project.entity.Currency;
import cathaybk.project.service.CurrenciesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/currencies")
public class CurrenciesController {

    private final CurrenciesService service;

    public CurrenciesController(CurrenciesService service) {
        this.service = service;
    }

    @GetMapping
    public List<Currency> listAll() {
        return service.findAll();
    }

    @PostMapping
    public Currency create(@RequestBody Currency currency) {
        return service.save(currency);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Currency> update(@PathVariable Long id, @RequestBody Currency updated) {
        return service.findById(id)
            .map(existing -> {
                existing.setCode(updated.getCode());
                existing.setName(updated.getName());
                return ResponseEntity.ok(service.save(existing));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
