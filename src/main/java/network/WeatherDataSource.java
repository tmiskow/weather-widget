package network;

import com.google.gson.JsonObject;
import data.WeatherData;

public abstract class WeatherDataSource extends DataSource {

    protected abstract WeatherData createWeatherData(JsonObject jsonObject);
}
