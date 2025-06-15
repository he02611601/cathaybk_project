package cathaybk.project.service;

import cathaybk.project.dto.CoindeskResponseDTO;
import cathaybk.project.dto.CoindeskResponseDTO.CurrencyInfo;
import cathaybk.project.entity.Currency;
import cathaybk.project.repository.CurrencyRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CoindeskService {

    private final String COINDESK_API = "https://kengp3.github.io/blog/coindesk.json";
    private final CurrencyRepository currencyRepository;

    public CoindeskService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public String getOriginalCoindeskJson() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(COINDESK_API, String.class);
    }

    public CoindeskResponseDTO getConvertedData() {
        RestTemplate restTemplate = new RestTemplate();
        String rawJson = restTemplate.getForObject(COINDESK_API, String.class);

        JSONObject json = new JSONObject(rawJson);
        String isoTime = json.getJSONObject("time").getString("updatedISO");
        String formattedTime = ZonedDateTime.parse(isoTime)
                .format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));

        JSONObject bpi = json.getJSONObject("bpi");
        List<CurrencyInfo> currencyInfoList = new ArrayList<>();

        Iterator<String> keys = bpi.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            JSONObject currencyJson = bpi.getJSONObject(key);
            String code = currencyJson.getString("code");
            String rate = currencyJson.getString("rate");
            float rateFloat = (float) currencyJson.getDouble("rate_float");

            Optional<Currency> found = currencyRepository.findByCode(code);
            String name = found.map(Currency::getName).orElse("（未知）");

            currencyInfoList.add(new CurrencyInfo(code, name, rate, rateFloat));
        }    

        CoindeskResponseDTO result = new CoindeskResponseDTO();
        result.setUpdateTime(formattedTime);
        result.setCurrencyList(currencyInfoList);
        return result;
    }
}
