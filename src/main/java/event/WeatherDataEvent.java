package event;

import data.WeatherData;
import network.WeatherDataSource;

public class WeatherDataEvent extends DataEvent {

    private WeatherData weatherData;
    private WeatherDataSource source;

    public WeatherDataEvent(WeatherData weatherData, WeatherDataSource source) {
        this.weatherData = weatherData;
        this.source = source;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }

    public Float getTemperature() {
        return weatherData.getTemperature();
    }

    public Float getPressure() {
        return weatherData.getPressure();
    }

    public Float getCloudiness() {
        return weatherData.getCloudiness();
    }

    public Float getWindSpeed() {
        return weatherData.getWindSpeed();
    }

    public Float getWindDegree() {
        return weatherData.getWindDegree();
    }

    public WeatherDataSource getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "Fetched weather data from " + source;
    }
}
