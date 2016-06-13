package io.propty.propty;

/**
 * Created by hankerins on 6/8/16.
 */
public class User {

    private String firstName, lastName, email, username, password;
    private float numBedrooms, numBathrooms;
    private int minPrice, maxPrice, squareFootage, structure, within, zip;
    private boolean pool, garage;

    User(){
        numBedrooms = numBathrooms = 1;
        minPrice = maxPrice = squareFootage = structure = within = zip = 0;
        pool = garage = false;
    }

    User(String first, String last, String em, String user, String pass){
        firstName = first;
        lastName = last;
        email = em;
        username = user;
        password = pass;
        numBedrooms = numBathrooms = 1;
        minPrice = maxPrice = squareFootage = structure = within = zip = 0;
        pool = garage = false;
    }

    void setFirstName (String first) { firstName = first; }
    void setLastName (String last) { lastName = last; }
    void setEmail (String em) { email = em; }
    void setUsername (String user) { username = user; }
    void setPassword (String pass) { password = pass; }
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
    void setSettings(float beds, float baths, int min, int max, int sqFt, int str, int wit, int zi,
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

    String getFirstName() { return  firstName; }
    String getLastName() { return  lastName; }
    String getEmail() { return email; }
    String getUsername() { return username; }
    float getNumBedrooms() { return numBedrooms; }
    float getNumBathrooms() { return  numBathrooms; }
    int getMinPrice() { return minPrice; }
    int getMaxPrice() { return  maxPrice; }
    int getSquareFootage() { return squareFootage; }
    int getStructure() { return structure; }
    int getWithin() { return within; }
    int getZip() { return  zip; }
    boolean getPool() { return pool; }
    boolean getGarage() { return garage; }
}
