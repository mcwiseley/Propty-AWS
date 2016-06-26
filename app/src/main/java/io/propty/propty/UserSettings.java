package io.propty.propty;

/**
 * Created by hankerins on 6/8/16.
 */
public class UserSettings {

    private String uid, keywords;
    private float numBedrooms, numBathrooms;
    private int minPrice, maxPrice, squareFootage, structure, within, zip;

    UserSettings(){
        numBedrooms = numBathrooms = 1;
        minPrice = maxPrice = squareFootage = structure = within = zip = 0;
        uid = keywords = "";
    }

    UserSettings(String uid){
        this.uid = uid;
        keywords = "";
        numBedrooms = numBathrooms = 1;
        minPrice = maxPrice = squareFootage = structure = within = zip = 0;
    }


    void setUid (String uid) { this.uid = uid; }
    void setNumBedrooms(float beds) { numBedrooms = beds; }
    void setNumBathrooms(float baths) { numBathrooms = baths; }
    void setMinPrice(int min) { minPrice = min; }
    void setMaxPrice(int max) { maxPrice = max; }
    void setSquareFootage(int sqFt) { squareFootage = sqFt; }
    void setStructure(int str) { structure = str; }
    void setWithin(int wit) { within = wit; }
    void setZip(int zi) { zip = zi; }
    void setKeywords(String keywords) { this.keywords = keywords; }
    void setAll(String uid, float beds, float baths, int min, int max, int sqFt, int str, int wit,
                int zi, String keywords){
        setUid(uid);
        setNumBedrooms(beds);
        setNumBathrooms(baths);
        setMinPrice(min);
        setMaxPrice(max);
        setSquareFootage(sqFt);
        setStructure(str);
        setWithin(wit);
        setZip(zi);
        setKeywords(keywords);
    }

    String getUid() { return uid; }
    float getNumBedrooms() { return numBedrooms; }
    float getNumBathrooms() { return  numBathrooms; }
    int getMinPrice() { return minPrice; }
    int getMaxPrice() { return  maxPrice; }
    int getSquareFootage() { return squareFootage; }
    int getStructure() { return structure; }
    int getWithin() { return within; }
    int getZip() { return  zip; }
    String getKeywords() { return keywords; }
}
