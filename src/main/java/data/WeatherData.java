package data;

public class WeatherData {

    private static float ABSOLUTE_ZERO = 273.15f;

    public static float convertFromKelvinToCelsius(float kelvin) {
        return kelvin - ABSOLUTE_ZERO;
    }

    private Float temperature;
    private Float pressure;
    private Float cloudiness;
    private Float windSpeed;
    private Float windDegree;
    private Float humidity;

    String iconCode = "wi-na";

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

    public Float getHumidity() {
        return humidity;
    }

    public String getHumidityString() {
        return humidity != null ? Math.round(humidity) + "%" : "--";
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    public String getIconCode() {
        return iconCode;
    }

    public void setIconCode(String iconCode) {

        switch (iconCode) {
            case "01d":
                this.iconCode = "wi-day-sunny";
                break;

            case "02d":
                this.iconCode = "wi-day-cloudy";
                break;

            case "03d":
            case "03n":
                this.iconCode = "wi-cloud";
                break;

            case "04d":
            case "04n":
                this.iconCode = "wi-cloudy";
                break;

            case "09d":
            case "09n":
                this.iconCode = "wi-rain";
                break;

            case "10d":
                this.iconCode = "wi-day-rain";
                break;

            case "11d":
            case "11n":
                this.iconCode = "wi-thunderstorm";
                break;

            case "13d":
            case "13n":
                this.iconCode = "wi-snow-wind";
                break;

            case "50d":
            case "50n":
                this.iconCode = "wi-fog";
                break;

            case "01n":
                this.iconCode = "wi-night-clear";
                break;

            case "02n":
                this.iconCode = "wi-night-cloudy";
                break;

            case "10n":
                this.iconCode = "wi-night-rain";
                break;

            default:
                this.iconCode = "wi-na";
        }

    }

    @Override
    public String toString() {
        return "Weather Data:" +
                "\n\ticon-code = " + getIconCode() +
                "\n\ttemperature = " + getTemperatureString() +
                "\n\tpressure = " + getPressureString() +
                "\n\tcloudiness = " + getCloudinessString() +
                "\n\thumidity = " + getHumidityString() +
                "\n\twindSpeed = " + getWindSpeedString() +
                "\n\twindDegree = " + getWindDegreeString();
    }
}
