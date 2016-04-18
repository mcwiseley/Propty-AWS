package io.propty.propty;

import java.text.NumberFormat;

/**
 * Swipe Card Data Class
 *
 * Contains all of the relevant MLS data for a single listing, standard get[Data] methods,
 * and get[Data]Formatted methods that convert the data into a String for display purposes
 */
public class SwipeCard {

    private String desc, listId, areaUnits, type;
    private int beds, area;
    private double baths, price;

    /**
     * SwipeCard Constructor
     *
     * @param description The 'headline' of the listing, or a brief description of the property ('Remarks' on MLS?)
     * @param listing_id The unique id of this property listing ('ListingId' in MLS)
     * @param bedrooms The total number of bedrooms ('BedroomsTotal' in MLS)
     * @param baths_full The total number of full bathrooms ('BathroomsFull' in MLS)
     * @param baths_3quart The total number of 3-quarter bathrooms ('BathroomsThreeQuarter' in MLS)
     * @param baths_half The total number of half bathrooms ('BathroomsHalf' in MLS)
     * @param baths_1quart The total number of 1-quarter bathrooms ('BathroomsOneQuarter' in MLS)
     * @param living_area The area of the actual living space ('LivingArea' in MLS)
     * @param living_area_units The type of units used to measure area ('LivingAreaUnits' in MLS)
     * @param list_price The listed price of the property ('ListPrice' in MLS)
     * @param property_type The type of the property ('PropertyType' in MLS)
     * @param property_sub_type The sub-type of the property ('PropertySubType' in MLS)
     */
    SwipeCard(String description, String listing_id, int bedrooms, int baths_full,
              int baths_3quart, int baths_half, int baths_1quart, int living_area,
              String living_area_units, double list_price, String property_type,
              String property_sub_type) {
        desc = description;
        listId = listing_id;
        beds = bedrooms;
        area = living_area;
        areaUnits = living_area_units;
        price = list_price;
        type = property_type +
                (property_sub_type.isEmpty() ? "" : " - " + property_sub_type);
        baths = ((double) baths_full) +
                (((double) baths_3quart) * 0.75) +
                (((double) baths_half) * 0.5) +
                (((double) baths_1quart) * 0.25);
    }

    String getDesc() { return desc; }
    String getListId() { return listId; }
    int getBeds() { return beds; }
    double getBaths() { return baths; }
    int getArea() { return area; }
    String getAreaUnits() { return areaUnits; }
    double getPrice() { return price; }
    String getType() { return type; }

    String getDescFormatted() {
        return desc + "\n\n";
    }

    String getBedsFormatted() {
        return beds + (beds == 1 ? " bedroom\n" : " bedrooms\n");
    }

    String getBathsFormatted() {
        if ((baths * 100d) % 100d == 0d) {
            return ((int) baths) + (baths == 1d ? " bathroom\n" : " bathrooms\n");
        }
        else {
            return baths + " bathrooms\n";
        }
    }

    String getAreaFormatted() { return area + " " + areaUnits + "\n"; }

    String getPriceFormatted() {
        return NumberFormat.getCurrencyInstance().format(price) + "\n";
    }

    String getTypeFormatted() { return type + "\n"; }

}
