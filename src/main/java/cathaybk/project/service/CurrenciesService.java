package cathaybk.project.service;

import cathaybk.project.entity.Currency;
import cathaybk.project.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurrenciesService {

    private final CurrencyRepository repo;

    public CurrenciesService(CurrencyRepository repo) {
        this.repo = repo;
    }

    public List<Currency> findAll() {
        return repo.findAll();
    }

    public Optional<Currency> findById(Long id) {
        return repo.findById(id);
    }

    public Currency save(Currency currency) {
        return repo.save(currency);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}

