package network;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import data.AirQualityData;
import event.AirQualityDataEvent;
import event.DataEvent;
import event.WeatherDataEvent;
import io.reactivex.netty.RxNetty;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;

public class AirQualitySource extends DataSource {

    private static final String URL = "http://powietrze.gios.gov.pl/pjp/current/getAQIDetailsList?param=AQI";

    private static final String CITY = "Warszawa";

    @Override
    protected Observable<DataEvent> makeRequest() {
        return RxNetty.createHttpRequest(JsonHelper.withJsonHeader(prepareHttpGETRequest(URL)))
                .compose(this::unpackResponse)
                .map(JsonHelper::asJsonArray)
                .map(this::createAirQualityData)
                .map(AirQualityDataEvent::new);
    }

    private AirQualityData createAirQualityData(JsonArray jsonArray) {

        List<Float> pm10List = new ArrayList<>();
        List<Float> pm25List = new ArrayList<>();

        for (JsonElement jsonElement : jsonArray) {

            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject.get("stationName").getAsString().startsWith(CITY)) {
                JsonObject values = jsonObject.get("values").getAsJsonObject();

                JsonElement pm10 = values.get("PM10");
                JsonElement pm25 = values.get("PM2.5");

                if (pm10 != null) {
                    pm10List.add(pm10.getAsFloat());
                }

                if (pm25 != null) {
                    pm25List.add(pm25.getAsFloat());
                }
            }
        }

        Float pm10Average = 0f;
        Float pm25Average = 0f;

        for (Float element : pm10List) {
            pm10Average += element;
        }

        for (Float element : pm25List) {
            pm25Average += element;
        }

        pm10Average /= pm10List.size();
        pm25Average /= pm25List.size();

        return new AirQualityData(pm25Average, pm10Average);
    }
}
