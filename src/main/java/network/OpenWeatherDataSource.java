package network;

import com.google.gson.JsonObject;
import data.WeatherData;
import event.DataEvent;
import event.WeatherDataEvent;
import io.reactivex.netty.RxNetty;
import rx.Observable;

public class OpenWeatherDataSource extends WeatherDataSource {

    private static final String CITY_ID = "6695624";
    private static final String API_KEY = "951a5fc3b598beb67cb24c163ef666f3";
    private static final String URL = "http://api.openweathermap.org/data/2.5/weather?id="
                                           + CITY_ID
                                           + "&APPID="
                                           + API_KEY;

    @Override
    protected Observable<DataEvent> makeRequest() {
        return RxNetty.createHttpRequest(JsonHelper.withJsonHeader(prepareHttpGETRequest(URL)))
                .compose(this::unpackResponse)
                .map(JsonHelper::asJsonObject)
                .map(this::createWeatherData)
                .map(weatherData -> new WeatherDataEvent(weatherData, this));
    }

    private WeatherData createWeatherData(JsonObject jsonObject) {

        JsonObject weather = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject();
        JsonObject main = jsonObject.getAsJsonObject("main");
        JsonObject clouds = jsonObject.getAsJsonObject("clouds");
        JsonObject wind = jsonObject.getAsJsonObject("wind");

        Float temperature, pressure, humidity, cloudiness, windSpeed, windDegree;
        String iconCode;

        if (main.has("temp")) {
            temperature = main.get("temp").getAsFloat();
            temperature = WeatherData.convertFromKelvinToCelsius(temperature);
        }
        else {
            temperature = null;
        }

        pressure = main.has("pressure") ? main.get("pressure").getAsFloat() : null;
        humidity = main.has("humidity") ? main.get("humidity").getAsFloat() : null;
        cloudiness = clouds.has("all") ? clouds.get("all").getAsFloat() : null;
        windSpeed = wind.has("speed") ? wind.get("speed").getAsFloat() : null;
        windDegree = wind.has("deg") ? wind.get("deg").getAsFloat() : null;

        iconCode = weather.has("icon") ? weather.get("icon").getAsString() : "";

        WeatherData weatherData = new WeatherData();
        weatherData.setTemperature(temperature);
        weatherData.setPressure(pressure);
        weatherData.setCloudiness(cloudiness);
        weatherData.setHumidity(humidity);
        weatherData.setWindSpeed(windSpeed);
        weatherData.setWindDegree(windDegree);
        weatherData.setIconCode(iconCode);

        return weatherData;
    }

    @Override
    public String toString() {
        return "http://openweathermap.org/";
    }
}
