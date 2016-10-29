package cache;

class ExpectedCacheStatistics {
    private static int cityCount = 0;
    private static int cityHits = 0;
    private static int cityMisses = 0;

    private static int hotelCount = 0;
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

    public static String plusOntCityCount() {
        return String.valueOf(++cityCount);
    }

    public static String sameCityCount() {
        return String.valueOf(cityCount);
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