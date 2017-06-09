package event;

import data.WeatherData;

public class WeatherDataEvent extends DataEvent {

    private WeatherData weatherData;

    public WeatherDataEvent(WeatherData weatherData) {
        this.weatherData = weatherData;
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

    // TODO
    @Override
    public String toString() {
        return "WeatherDataEvent";
    }
}
