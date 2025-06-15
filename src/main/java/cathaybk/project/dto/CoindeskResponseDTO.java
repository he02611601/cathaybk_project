package cathaybk.project.dto;

import java.util.List;

public class CoindeskResponseDTO {
    private String updateTime;
    private List<CurrencyInfo> currencyList;

    public static class CurrencyInfo {
        private String code;
        private String name;
        private String rate;        // 保留原始 rate (含逗號)
        private float rateFloat;   // 額外加的純 float 值

        public CurrencyInfo(String code, String name, String rate, float rateFloat) {
            this.code = code;
            this.name = name;
            this.rate = rate;
            this.rateFloat = rateFloat;
        }

        public String getCode() { return code; }
        public String getName() { return name; }
        public String getRate() { return rate; }
        public float getRateFloat() { return rateFloat; }
    }

    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }

    public List<CurrencyInfo> getCurrencyList() { return currencyList; }
    public void setCurrencyList(List<CurrencyInfo> currencyList) { this.currencyList = currencyList; }
}