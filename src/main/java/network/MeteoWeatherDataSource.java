package network;

import com.google.gson.JsonObject;
import data.WeatherData;
import event.*;
import io.reactivex.netty.RxNetty;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import rx.Observable;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

public class MeteoWeatherDataSource extends WeatherDataSource {

    private static final String URL = "http://www.meteo.waw.pl/";

    @Override
    protected Observable<DataEvent> makeRequest(){
        return RxNetty.createHttpRequest(prepareHttpGETRequest(URL))
            .compose(this::unpackResponse)
            .map(this::createWeatherData)
            .map(WeatherDataEvent::new);
    }

    protected WeatherData createWeatherData(String html) {
        try {
            Document document = Jsoup.connect(URL).get();

            String temperatureString = document.getElementById("PARAM_0_TA").text();
            String pressureString = document.getElementById("PARAM_0_PR").text();
            String windSpeedString = document.getElementById("PARAM_WV").text();
            String windDegreeString = document.getElementById("PARAM_WD").text();

            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            DecimalFormat format = new DecimalFormat("0.#");
            format.setDecimalFormatSymbols(symbols);

            float temperature = format.parse(temperatureString).floatValue();
            float pressure = format.parse(pressureString).floatValue();
            float windSpeed = format.parse(windSpeedString).floatValue();
            float windDegree = format.parse(windDegreeString).floatValue();

            WeatherData weatherData = new WeatherData();

            weatherData.setTemperature(temperature);
            weatherData.setPressure(pressure);
            weatherData.setWindSpeed(windSpeed);
            weatherData.setWindDegree(windDegree);
            weatherData.setCloudiness(null);

            return weatherData;

        } catch (IOException | ParseException e) {
            Observable.just(new ErrorEvent(e)).cast(AppEvent.class)
                .mergeWith(EventStream.getInstance().getEvents());


            WeatherData weatherData = new WeatherData();

            weatherData.setTemperature(null);
            weatherData.setPressure(null);
            weatherData.setWindSpeed(null);
            weatherData.setWindDegree(null);
            weatherData.setCloudiness(null);

            return weatherData;
        }
    }

}
