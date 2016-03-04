package io.propty.propty;

import java.util.HashMap;

/**
 * Created by tkelly on 3/4/16.
 */
public class SwipeCard {
    private String desc, type;
    private int beds, sqFt;
    private double baths, price;

    SwipeCard(String description, int bedrooms, int baths_full, int baths_half,
              int sq_ft, double list_price, String property_type) {
        desc = description;
        beds = bedrooms;
        double baths_raw = ((double) baths_full) + (((double) baths_half) / 2d);
        baths = (double) Math.round(baths_raw * 10d) / 10d;
        sqFt = sq_ft;
        price = (double) Math.round(list_price * 100d) / 100d;
        type = property_type;
    }

    public String getDesc() { return desc; }
    public String getBeds() { return Integer.toString(beds); }
    public String getBaths() { return Double.toString(baths); }
    public String getSqFt() { return Integer.toString(sqFt); }
    public String getPrice() { return Double.toString(price); }
    public String getType() { return type; }
}
