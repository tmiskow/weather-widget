package event;

import network.WeatherDataSource;

public class WeatherDataSourceChangeEvent extends AppEvent {

    private WeatherDataSource newSource;

    public WeatherDataSourceChangeEvent(WeatherDataSource newSource) {
        this.newSource = newSource;
    }

    public WeatherDataSource getNewSource() {
        return newSource;
    }

    @Override
    public String toString() {
        return "WeatherDataSourceChangeEvent [" + newSource + "]";
    }
}
