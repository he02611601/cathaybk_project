package cathaybk.project.repository;

import cathaybk.project.entity.Currencies;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrenciesRepository extends JpaRepository<Currencies, Long> {
}
