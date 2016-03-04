package io.propty.propty;

/**
 * Created by tkelly on 3/4/16.
 * SwipeCard data class
 * Contains all of the relevant MLS data for a single listing, and get methods that convert the data into a String for display purposes
 * Constructor takes 7 arguments:
 * 1. String description - the 'headline' of the listing, or a brief description of the property (might not be provided by MLS)
 * 2. int bedrooms - the number of bedrooms ('Bedrooms' on MLS)
 * 3. int baths_full - the number of full bathrooms ('BathsFull' on MLS)
 * 4. int baths_half - the number of half bathrooms ('BathsHalf' on MLS)
 * 5. int sq_ft - the square footage ('SqFtTotal' or 'SqFtUnderRoof' on MLS)
 * 6. double list_price - the listed price of the property ('ListPrice' on MLS)
 * 7. String property_type - the type of the property ('ListingType' or 'PropertyType' on MLS)
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
