package cache;

class ExpectedCacheStatistics {
    private static int cityEvictions = 0;
    private static int cityPuts = 0;
    private static int cityHits = 0;
    private static int cityMisses = 0;

    private static int hotelHits = 0;
    private static int hotelMisses = 0;

    public static String plusOneCityHits() {
        return String.valueOf(++cityHits);
    }

    public static String plusOneCityMisses() {
        return String.valueOf(++cityMisses);
    }

    public static String sameCityHits() {
        return String.valueOf(cityHits);
    }

    public static String sameCityMisses() {
        return String.valueOf(cityMisses);
    }

    public static String plusOntCityEvictions() {
        return String.valueOf(++cityEvictions);
    }

    public static String sameCityEvictions() {
        return String.valueOf(cityEvictions);
    }

    public static String plusOntCityPuts() {
        return String.valueOf(++cityPuts);
    }

    public static String sameCityPuts() {
        return String.valueOf(cityPuts);
    }

    public static String plusOneHotelHits() {
        return String.valueOf(++hotelHits);
    }

    public static String plusOneHotelMisses() {
        return String.valueOf(++hotelMisses);
    }

    public static String sameHotelHits() {
        return String.valueOf(hotelHits);
    }

    public static String sameHotelMisses() {
        return String.valueOf(hotelMisses);
    }
}