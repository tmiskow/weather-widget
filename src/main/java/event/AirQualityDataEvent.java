package event;

import data.AirQualityData;
import network.AirQualityDataSource;

public class AirQualityDataEvent extends DataEvent {

    private AirQualityData airQualityData;
    private AirQualityDataSource source;

    public AirQualityDataEvent(AirQualityData airQualityData, AirQualityDataSource source) {
        this.airQualityData = airQualityData;
        this.source = source;
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
        return "Fetched air quality data from " + source;
    }
}
