package io.propty.propty;

import android.content.SharedPreferences;

import java.text.NumberFormat;

/*Property data class.  Contains MLS data for individual properties.  Used to make swipecards.
* Old description is below
* *
 * Swipe Card Data Class
 *
 * Contains all of the relevant MLS data for a single listing, standard get[Data] methods,
 * and get[Data]Formatted methods that convert the data into a String for display purposes
 */
public class Property {

    protected String desc, listId, areaUnits, type, address;
    protected int id, zip, beds, area, image;
    protected double baths, price;

    /**
     * Property Constructor
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
     * @param image_id The id number of the image to be displayed in the background
     */


    Property(){

    }

    Property(int id, String description, String listing_id, int bedrooms, int baths_full,
             int baths_3quart, int baths_half, int baths_1quart, int living_area,
             String living_area_units, double list_price, String property_type,
             String property_sub_type, int image_id, String address, int zip) {
        desc = description;
        listId = listing_id;
        beds = bedrooms;
        area = living_area;
        areaUnits = living_area_units;
        price = list_price;
        image = image_id;
        type = property_type +
                (property_sub_type.isEmpty() ? "" : " - " + property_sub_type);
        baths = ((double) baths_full) +
                (((double) baths_3quart) * 0.75) +
                (((double) baths_half) * 0.5) +
                (((double) baths_1quart) * 0.25);
        this.id = id;
        this.address = address;
        this.zip = zip;
    }

    Property(String description, String listing_id, int bedrooms, int baths_full,
             int baths_3quart, int baths_half, int baths_1quart, int living_area,
             String living_area_units, double list_price, String property_type,
             String property_sub_type, int image_id, String address, int zip) {
        desc = description;
        listId = listing_id;
        beds = bedrooms;
        area = living_area;
        areaUnits = living_area_units;
        price = list_price;
        image = image_id;
        type = property_type +
                (property_sub_type.isEmpty() ? "" : " - " + property_sub_type);
        baths = ((double) baths_full) +
                (((double) baths_3quart) * 0.75) +
                (((double) baths_half) * 0.5) +
                (((double) baths_1quart) * 0.25);
        this.address = address;
        this.zip = zip;
    }

    Property(String description, String listing_id, int bedrooms, int baths_full,
             int baths_3quart, int baths_half, int baths_1quart, int living_area,
             String living_area_units, double list_price, String property_type,
             String property_sub_type, int image_id) {
        desc = description;
        listId = listing_id;
        beds = bedrooms;
        area = living_area;
        areaUnits = living_area_units;
        price = list_price;
        image = image_id;
        type = property_type +
                (property_sub_type.isEmpty() ? "" : " - " + property_sub_type);
        baths = ((double) baths_full) +
                (((double) baths_3quart) * 0.75) +
                (((double) baths_half) * 0.5) +
                (((double) baths_1quart) * 0.25);
        address = "";
        zip = 12345;
    }

    String getDesc() { return desc; }
    String getListId() { return listId; }
    int getBeds() { return beds; }
    double getBaths() { return baths; }
    int getArea() { return area; }
    String getAreaUnits() { return areaUnits; }
    double getPrice() { return price; }
    String getType() { return type; }
    int getImage() { return image; }
    public int getId() { return this.id; }
    public String getAddress() { return this.address; }
    public int getZip() { return this.zip; }

    public void setId(int id) { this.id = id; }
    public void setDesc(String desc) { this.desc = desc; }
    public void setListId(String listId) { this.listId = listId; }
    public void setAreaUnits(String areaUnits) { this.areaUnits = areaUnits; }
    public void setType(String type) { this.type = type; }
    public void setBeds(int beds) { this.beds = beds; }
    public void setArea(int area) { this.area = area; }
    public void setImage(int image) { this.image = image; }
    public void setBaths(double baths) { this.baths = baths; }
    public void setPrice(double price) { this.price = price; }
    public void setAddress(String address) { this.address = address; }
    public void setZip(int zip) { this.zip = zip; }

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
