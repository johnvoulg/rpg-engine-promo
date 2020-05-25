/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicengine.items;

/**
 *
 * @author JVoulg
 */
public class Furniture {
    
    public Furniture(int size, String name) {
        this.size = size;
        this.name = name;
        
        switch (size) {
            case 1:
                this.coverValue = 0;
                break;
            case 2:
                this.coverValue = 40;
                break;
            case 3:
                this.coverValue = 60;
                break;
        }
    }
    
    /*
    1 = small
    2 = medium
    3 = big
    */
    private int size;
    private int coverValue;
    private String name;

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @return the cover
     */
    public int getCoverValue() {
        return coverValue;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
