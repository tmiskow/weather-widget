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
                .map(WeatherDataEvent::new);
    }

    private WeatherData createWeatherData(JsonObject jsonObject) {

        JsonObject main = jsonObject.getAsJsonObject("main");
        JsonObject clouds = jsonObject.getAsJsonObject("clouds");
        JsonObject wind = jsonObject.getAsJsonObject("wind");

        float temperature = main.get("temp").getAsFloat();
        temperature = WeatherData.convertfromKelvinToCelsius(temperature);

        float pressure = main.get("pressure").getAsFloat();
        float cloudiness = clouds.get("all").getAsFloat();
        float windSpeed = wind.get("speed").getAsFloat();
        float windDegree = wind.get("deg").getAsFloat();

        WeatherData weatherData = new WeatherData();
        weatherData.setTemperature(temperature);
        weatherData.setPressure(pressure);
        weatherData.setCloudiness(cloudiness);
        weatherData.setWindSpeed(windSpeed);
        weatherData.setWindDegree(windDegree);

        return weatherData;
    }
}
