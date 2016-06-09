package io.propty.propty;

/**
 * Created by hankerins on 6/8/16.
 */
public class User {

    private float numBedrooms, numBathrooms;
    private int minPrice, maxPrice, squareFootage, structure, within, zip;
    private boolean pool, garage;

    User(){
        numBedrooms = numBathrooms = 1;
        minPrice = maxPrice = squareFootage = structure = within = zip = 0;
        pool = garage = false;
    }

    void setNumBedrooms(float beds) { numBedrooms = beds; }
    void setNumBathrooms(float baths) { numBathrooms = baths; }
    void setMinPrice(int min) { minPrice = min; }
    void setMaxPrice(int max) { maxPrice = max; }
    void setSquareFootage(int sqFt) { squareFootage = sqFt; }
    void setStructure(int str) { structure = str; }
    void setWithin(int wit) { within = wit; }
    void setZip(int zi) { zip = zi; }
    void setPool(boolean poo) { pool = poo; }
    void setGarage(boolean gar) { garage = gar; }
    void setAll(float beds, float baths, int min, int max, int sqFt, int str, int wit, int zi,
                boolean poo, boolean gar){
        setNumBedrooms(beds);
        setNumBathrooms(baths);
        setMinPrice(min);
        setMaxPrice(max);
        setSquareFootage(sqFt);
        setStructure(str);
        setWithin(wit);
        setZip(zi);
        setPool(poo);
        setGarage(gar);
    }

}
