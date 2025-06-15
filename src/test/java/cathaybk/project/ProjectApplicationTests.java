package cathaybk.project;

import cathaybk.project.dto.CoindeskResponseDTO;
import cathaybk.project.entity.Currency;
import cathaybk.project.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProjectApplicationTests {
	@Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CurrencyRepository currencyRepository;

    @BeforeEach
    public void setup() {
        currencyRepository.deleteAll();
        currencyRepository.save(new Currency("USD", "美元"));
        currencyRepository.save(new Currency("EUR", "歐元"));
        currencyRepository.save(new Currency("GBP", "英鎊"));
    }

    @Test
    public void testGetOriginalCoindeskJson() {
        ResponseEntity<String> response = restTemplate.getForEntity("/coindesk/original", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
            .isNotNull()
            .contains("bpi")
            .contains("time")
            .contains("updatedISO");
    }

    @Test
    public void testGetConvertedCoindeskJson() {
        ResponseEntity<CoindeskResponseDTO> response = restTemplate.getForEntity("/coindesk/converted", CoindeskResponseDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        CoindeskResponseDTO result = response.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getUpdateTime()).isNotEmpty();
        assertThat(result.getCurrencyList())
            .isNotNull()
            .isNotEmpty();

        var firstCurrency = result.getCurrencyList().get(0);
        assertThat(firstCurrency.getCode()).isNotEmpty();
        assertThat(firstCurrency.getName()).isNotEmpty();
        assertThat(firstCurrency.getRate()).isNotEmpty();
        assertThat(firstCurrency.getRateFloat()).isGreaterThan(0);
    }

    @Test
    public void testCreateCurrency() {
        Currency newCurrency = new Currency("JPY", "日圓");
        ResponseEntity<Currency> response = restTemplate.postForEntity("/currencies", newCurrency, Currency.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody())
            .isNotNull()
            .satisfies(currency -> {
                assertThat(currency.getId()).isNotNull();
                assertThat(currency.getCode()).isEqualTo("JPY");
                assertThat(currency.getName()).isEqualTo("日圓");
            });
    }

    @Test
    public void testListAllCurrencies() {
        ResponseEntity<Currency[]> response = restTemplate.getForEntity("/currencies", Currency[].class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
            .isNotNull()
            .hasSize(3)
            .extracting(Currency::getCode)
            .containsExactlyInAnyOrder("USD", "EUR", "GBP");
    }

    @Test
    public void testUpdateCurrency() {
        // 先創建一個貨幣
        Currency newCurrency = new Currency("JPY", "日圓");
        ResponseEntity<Currency> createResponse = restTemplate.postForEntity("/currencies", newCurrency, Currency.class);
        Long id = createResponse.getBody().getId();

        // 更新貨幣
        Currency update = new Currency("JPY2", "日圓2");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Currency> entity = new HttpEntity<>(update, headers);
        
        ResponseEntity<Currency> updateResponse = restTemplate.exchange(
            "/currencies/" + id, 
            HttpMethod.PUT, 
            entity, 
            Currency.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody())
            .isNotNull()
            .satisfies(currency -> {
                assertThat(currency.getId()).isEqualTo(id);
                assertThat(currency.getCode()).isEqualTo("JPY2");
                assertThat(currency.getName()).isEqualTo("日圓2");
            });
    }

    @Test
    public void testDeleteCurrency() {
        // 先創建一個貨幣
        Currency newCurrency = new Currency("JPY", "日圓");
        ResponseEntity<Currency> createResponse = restTemplate.postForEntity("/currencies", newCurrency, Currency.class);
        Long id = createResponse.getBody().getId();

        // 刪除貨幣
        restTemplate.delete("/currencies/" + id);

        // 驗證貨幣已被刪除
        assertThat(currencyRepository.findById(id)).isEmpty();
    }

    @Test
    public void testUpdateNonExistentCurrency() {
        Currency update = new Currency("JPY", "日圓");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Currency> entity = new HttpEntity<>(update, headers);
        
        ResponseEntity<Currency> response = restTemplate.exchange(
            "/currencies/999", 
            HttpMethod.PUT, 
            entity, 
            Currency.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
