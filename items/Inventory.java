/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package basicengine.items;

import java.util.ArrayList;

/**
 *
 * @author JVoulg
 */
public class Inventory {
    public Inventory(String name, int size, boolean equipable) {
        this.name = name;
        this.size = size;
        this.equipable = equipable;
        this.equiped = false;
    }
    private String name;
    private int size;
    private boolean equipable;
    private boolean equiped;
    private ArrayList<Item> items = new ArrayList<Item>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getSize() {
        return size;
    }
    
    public int getSizeUsed() {
        int totalSize = 0;
        for (Item i : items) {
            totalSize = i.getSize() + totalSize;
        }
        return totalSize;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    public boolean isEquipable() {
        return equipable;
    }

    public void setEquipable(boolean equipable) {
        this.equipable = equipable;
    }

    public boolean isEquiped() {
        return equiped;
    }

    public void setEquiped(boolean equiped) {
        this.equiped = equiped;
    }
    
    /*
     * Items related Actions
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
    
    public String addItem(Item item) {
        if (item.isStorable()) {
            if (this.getSizeUsed() + item.getSize() <= this.size) {
                this.items.add(item);
                return "OK";
            } else {
                return "FULL_CAPACITY";
            }
        } else {
            return "NOT_STORABLE";
        }
    }
    
    public Item getItem(Item item) {
        Item selectedItem = null;
        if (this.items.contains(item)) {
            selectedItem = item;
        }
        return selectedItem;
    }
    
    public Item getItemByName(String name) {
        Item selectedItem = null;
        for (Item item : this.items) {
            if (item.getName().equals(name)) {
                selectedItem = item;
                break;
            }
        }
        return selectedItem;
    }
    
    public void removeItem(Item item) {
        this.items.remove(item);
    }
    
    public void removeItemByName(String name) {
        Item item = this.getItemByName(name);
        this.removeItem(item);
    }
}
