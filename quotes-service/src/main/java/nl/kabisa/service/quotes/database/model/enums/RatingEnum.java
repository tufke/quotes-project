package nl.kabisa.service.quotes.database.model.enums;

public enum RatingEnum {

    AGREE(10, "agree"),
    NEUTRAL(20, "neutral"),
    DISAGREE(30, "disagree");

    private final int code;
    private final String alfaCode;

    RatingEnum(int code, String alfaCode) {
        this.alfaCode = alfaCode;
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    public String getAlfaCode() { return alfaCode; }

    public static RatingEnum valueOfCode(int code) {
        for (RatingEnum rate : RatingEnum.values()) {
            if(rate.getCode() == code) {
                return rate;
            }
        }
        throw new IllegalArgumentException("Unknown code");
    }

    public static RatingEnum valueOfAlfaCode(String alfaCode) {
        for (RatingEnum rate : RatingEnum.values()) {
            if(rate.getAlfaCode().equals(alfaCode)) {
                return rate;
            }
        }
        throw new IllegalArgumentException("Unknown alfacode");
    }
}
