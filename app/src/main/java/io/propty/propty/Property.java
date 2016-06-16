package io.propty.propty;

/**
 * Created by hankerins on 6/14/16.
 */
public class Property extends SwipeCard {

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
     * @param image_id The id number of the image to be displayed in the background
     */

    private String address;
    private int id, zip;

    Property(){

    }

    Property(int id, String description, String listing_id, int bedrooms, int baths_full,
             int baths_3quart, int baths_half, int baths_1quart, int living_area,
             String living_area_units, double list_price, String property_type,
             String property_sub_type, int image_id, String address, int zip) {
        super(description, listing_id, bedrooms, baths_full,
                baths_3quart, baths_half, baths_1quart, living_area,
                living_area_units, list_price, property_type,
                property_sub_type, image_id);
        this.id = id;
        this.address = address;
        this.zip = zip;
    }

    Property(String description, String listing_id, int bedrooms, int baths_full,
              int baths_3quart, int baths_half, int baths_1quart, int living_area,
              String living_area_units, double list_price, String property_type,
              String property_sub_type, int image_id, String address, int zip) {
        super(description, listing_id, bedrooms, baths_full,
         baths_3quart, baths_half, baths_1quart, living_area,
         living_area_units, list_price, property_type,
                 property_sub_type, image_id);
        this.address = address;
        this.zip = zip;
    }

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



    //TODO delete old constructors (left for reference right now)
    /*

    public Property(int id, String propertyName, String address, int price,
                    float numBeds, float numBaths, int sqFootage, String structure,
                    int zip, boolean pool, boolean garage) {
        this.id = id;
        this.propertyName = propertyName;
        this.address = address;
        this.price = price;
        this.numBeds = numBeds;
        this.numBaths = numBaths;
        this.sqFootage = sqFootage;
        this.structure = structure;
        this.zip = zip;
        this.pool = pool;
        this.garage = garage;
    }

    public Property(String propertyName, String address, int price,
                    float numBeds, float numBaths, int sqFootage, String structure,
                    int zip, boolean pool, boolean garage) {
        this.propertyName = propertyName;
        this.address = address;
        this.price = price;
        this.numBeds = numBeds;
        this.numBaths = numBaths;
        this.sqFootage = sqFootage;
        this.structure = structure;
        this.zip = zip;
        this.pool = pool;
        this.garage = garage;
    }
    */


    /*
    public void setPropertyName(String propertyName) { this.propertyName = propertyName; }
    public String getPropertyName() { return this.propertyName; }

    public int getPrice() { return this.price; }

    public float getNumBeds() { return this.numBeds; }

    public float getNumBaths() { return this.numBaths; }
    public void setSqFootage(int sqFootage) { this.sqFootage = sqFootage; }
    public int getSqFootage() { return this.sqFootage; }
    public void setStructure(String structure) { this.structure = structure; }
    public String getStructure() { return this.structure; }

    public void setPool(boolean pool) { this.pool = pool; }
    public boolean getPool() { return this.pool; }
    public void setGarage(boolean garage) { this.garage = garage; }
    public boolean getGarage(){ return this.garage; }
    */
}
