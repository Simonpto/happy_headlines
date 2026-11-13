package simon.articleservice.model;

import java.util.Locale;

public enum Continent {
    AFRICA,
    ANTARCTICA,
    ASIA,
    EUROPE,
    NORTH_AMERICA,
    OCEANIA,
    SOUTH_AMERICA,
    GLOBAL;

    public static Continent fromPath(String value) {
        return Continent.valueOf(value.toUpperCase(Locale.ROOT).replace('-', '_'));
    }
}
