/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package basicengine;

import basicengine.items.*;

/**
 *
 * @author JVoulg
 */
public class GameLibrary {
    
    // Clothes
    public Cloth createCloth(String name, String color, String level,
            String compatibleWith,
            boolean affectsStr, int str,
            boolean affectsDex, int dex,
            boolean affectsCon, int con,
            boolean affectsCha, int cha) {
        
        Cloth cloth = new Cloth(name, color, level, compatibleWith,
                affectsStr, str,
                affectsDex, dex,
                affectsCon, con,
                affectsCha, cha);
        
        return cloth;
    }
    
    // Regular items
    public Item createItem(String name, int size, boolean equippable, boolean storable) {
        Item item = new Item(name, size, equippable, storable, false);
        return item;
    }
    
    // Weapons
    public Item createWeapon(String name, int size, boolean equippable, boolean storable,
            boolean weapon, int dSize, int attackBonus, boolean melee, boolean ranged) {
        Item item = new Item(name, size, equippable, storable, weapon, dSize, attackBonus, melee, ranged);
        return item;
    }
    
    // Inventories
    public Inventory createInventory(String name, int size, boolean equipable) {
        Inventory inventory = new Inventory(name, size, equipable);
        return inventory;
    }
    
    // Furniture
    public Furniture createFurniture(int size, String name) {
        Furniture furniture = new Furniture(size, name);
        return furniture;
    }
}
