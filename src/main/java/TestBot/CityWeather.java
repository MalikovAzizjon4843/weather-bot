package TestBot;

public class CityWeather {
    public final Coord coord;
    public final Weather weather[];
    public final String base;
    public final Main main;
    public final long visibility;
    public final Wind wind;
    public final Clouds clouds;
    public final long dt;
    public final Sys sys;
    public final long id;
    public final String name;
    public final long cod;

    public CityWeather(Coord coord, Weather[] weather, String base, Main main, long visibility, Wind wind, Clouds clouds, long dt, Sys sys, long id, String name, long cod){
        this.coord = coord;
        this.weather = weather;
        this.base = base;
        this.main = main;
        this.visibility = visibility;
        this.wind = wind;
        this.clouds = clouds;
        this.dt = dt;
        this.sys = sys;
        this.id = id;
        this.name = name;
        this.cod = cod;
    }

    public static final class Coord {
        public final double lon;
        public final double lat;

        public Coord(double lon, double lat){
            this.lon = lon;
            this.lat = lat;
        }
    }

    public static final class Weather {
        public final long id;
        public final String main;
        public final String description;
        public final String icon;

        public Weather(long id, String main, String description, String icon){
            this.id = id;
            this.main = main;
            this.description = description;
            this.icon = icon;
        }
    }

    public static final class Main {
        public final double temp;
        public final long pressure;
        public final long humidity;
        public final double temp_min;
        public final double temp_max;

        public Main(double temp, long pressure, long humidity, double temp_min, double temp_max){
            this.temp = temp;
            this.pressure = pressure;
            this.humidity = humidity;
            this.temp_min = temp_min;
            this.temp_max = temp_max;
        }

        public double getTemp() {
            return temp;
        }
    }

    public static final class Wind {
        public final double speed;
        public final double deg;

        public Wind(double speed, double deg){
            this.speed = speed;
            this.deg = deg;
        }
    }

    public static final class Clouds {
        public final long all;

        public Clouds(long all){
            this.all = all;
        }
    }

    public static final class Sys {
        public final long type;
        public final long id;
        public final double message;
        public final String country;
        public final long sunrise;
        public final long sunset;

        public Sys(long type, long id, double message, String country, long sunrise, long sunset){
            this.type = type;
            this.id = id;
            this.message = message;
            this.country = country;
            this.sunrise = sunrise;
            this.sunset = sunset;
        }
    }

    @Override
    public String toString() {
        return "The city temperature is : " + this.main.getTemp() + " °C";
    }
}
