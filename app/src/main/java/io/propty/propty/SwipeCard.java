package io.propty.propty;

/**
 * Created by tkelly on 3/4/16.
 * Swipe Card Data Class
 * Contains all of the relevant MLS data for a single listing, standard get[Data] methods,
 *  and get[Data]Formatted methods that convert the data into a String for display purposes
 * Constructor takes 7 arguments:
 * 1. String description - the 'headline' of the listing, or a brief description of the property (might not be provided by MLS)
 * 2. int bedrooms - the number of bedrooms ('Bedrooms' on MLS)
 * 3. int baths_full - the number of full bathrooms ('BathsFull' on MLS)
 * 4. int baths_half - the number of half bathrooms ('BathsHalf' on MLS)
 * 5. int sq_ft - the square footage ('SqFtTotal' or 'SqFtUnderRoof' on MLS)
 * 6. double list_price - the listed price of the property ('ListPrice' on MLS)
 * 7. String property_type - the type of the property ('ListingType' or 'PropertyType' on MLS)
 **/
public class SwipeCard {
    private String desc, beds_str, baths_str, sqFt_str, price_str, type;
    private int beds, sqFt;
    private double baths, price;

    SwipeCard(String description, int bedrooms, int baths_full, int baths_half,
              int sq_ft, double list_price, String property_type) {
        desc = description;
        type = property_type;
        beds = bedrooms;
        beds_str = Integer.toString(bedrooms);
        sqFt = sq_ft;
        sqFt_str = Integer.toString(sq_ft);

        baths = (double) Math.round((((double) baths_full) + (((double) baths_half) / 2d)) * 10d);
        if (baths % 10d == 0d) {
            baths /= 10d;
            baths_str = Integer.toString((int) baths);
        }
        else {
            baths /= 10d;
            baths_str = Double.toString(baths);
        }

        price = (double) Math.round(list_price * 100d);
        if (price % 10d == 0d) {
            price /= 100d;
            price_str = Double.toString(price) + "0";
        }
        else {
            price /= 100d;
            price_str = Double.toString(price);
        }
    }

    String getDesc() { return desc; }
    int getBeds() { return beds; }
    double getBaths() { return baths; }
    int getSqFt() { return sqFt; }
    double getPrice() { return price; }
    String getType() { return type; }

    String getDescFormatted() {
        return desc + "\n\n";
    }

    String getBedsFormatted() {
        if (beds == 1) {
            return beds_str + " bedroom\n";
        }
        else {
            return beds_str + " bedrooms\n";
        }
    }

    String getBathsFormatted() {
        if (baths == 1d) {
            return baths_str + " bathroom\n";
        }
        else {
            return baths_str + " bathrooms\n";
        }
    }

    String getSqFtFormatted() {
        return sqFt_str + " sq. ft.\n";
    }

    String getPriceFormatted() {
        return "$" + price_str + "\n";
    }

    String getTypeFormatted() {
        return "Type: " + type;
    }
}
