package data;

public class AirQualityData {

    private Float pm25;
    private Float pm10;

    public AirQualityData(Float pm25, Float pm10) {
        this.pm25 = pm25;
        this.pm10 = pm10;
    }

    public Float getPM25() {
        return pm25;
    }

    public Float getPM10() {
        return pm10;
    }

    @Override
    public String toString() {
        return "Air Quality Data:" +
                "\n\tpm25 = " + pm25 +
                "\n\tpm10 = " + pm10;
    }
}
