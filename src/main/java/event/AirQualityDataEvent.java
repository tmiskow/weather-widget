package event;

import data.AirQualityData;

public class AirQualityDataEvent extends DataEvent {

    private AirQualityData airQualityData;

    public AirQualityDataEvent(AirQualityData airQualityData) {
        this.airQualityData = airQualityData;
    }

    public AirQualityData getAirQualityData() {
        return airQualityData;
    }

    public Float getPM10() {
        return airQualityData.getPM10();
    }

    public Float getPM25() {
        return airQualityData.getPM25();
    }

    @Override
    public String toString() {
        return "Fetched air quality data.";
    }
}
