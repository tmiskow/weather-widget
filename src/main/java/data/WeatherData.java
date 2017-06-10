package data;

public class WeatherData {

    private static float ABSOLUTE_ZERO = 273.15f;

    public static float convertfromKelvinToCelsius(float kelvin) {
        return kelvin - ABSOLUTE_ZERO;
    }

    private Float temperature;
    private Float pressure;
    private Float cloudiness;
    private Float windSpeed;
    private Float windDegree;

    public Float getTemperature() {
        return temperature;
    }

    public String getTemperatureString() {
        return temperature != null ? Math.round(temperature) + "°C" : "--";
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Float getPressure() {
        return pressure;
    }

    public String getPressureString() {
        return pressure != null ? Math.round(pressure) + " hPa" : "--";
    }

    public void setPressure(Float pressure) {
        this.pressure = pressure;
    }

    public Float getCloudiness() {
        return cloudiness;
    }

    public String getCloudinessString() {
        return cloudiness != null ? Math.round(cloudiness) + "%" : "--";
    }

    public void setCloudiness(Float cloudiness) {
        this.cloudiness = cloudiness;
    }

    public Float getWindSpeed() {
        return windSpeed;
    }

    public String getWindSpeedString() {
        return windSpeed != null ? Math.round(windSpeed) + " m/s" : "--";
    }

    public void setWindSpeed(Float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Float getWindDegree() {
        return windDegree;
    }

    public String getWindDegreeString() {
        return windDegree != null ? Math.round(windDegree) + "°" : "--";
    }

    public void setWindDegree(Float windDegree) {
        this.windDegree = windDegree;
    }

    @Override
    public String toString() {
        return "Weather Data:" +
                "\n\ttemperature = " + getTemperatureString() +
                "\n\tpressure = " + getPressureString() +
                "\n\tcloudiness = " + getCloudinessString() +
                "\n\twindSpeed = " + getWindSpeedString() +
                "\n\twindDegree = " + getWindDegreeString();
    }
}
