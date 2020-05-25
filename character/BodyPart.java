/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package basicengine.character;

import java.util.ArrayList;
import basicengine.items.Cloth;
import basicengine.items.Item;

/**
 *
 * @author JVoulg
 */
public class BodyPart {
    
    // Characteristics
    private String name;
    private int health;
    private boolean vital;
    private boolean canEquip;
    private boolean harmful;
    private int dSize; // dies size
    private int attackBonus;
    private boolean immobilized;
    
    // Items
    private Item equippedItem;
    
    // Wearables
    private ArrayList<Cloth> wearables = new ArrayList<Cloth>();
    
    BodyPart(String name, int health, boolean vital, boolean canEquip) {
        this.name = name;
        this.health = health;
        this.vital = vital;
        this.canEquip = canEquip;
    }
    
    BodyPart(String name, int maxHp, boolean vital, boolean canEquip, int dSize, int attackBonus) {
        this.name = name;
        this.health = maxHp;
        this.vital = vital;
        this.canEquip = canEquip;
        this.harmful = true;
        this.dSize = dSize;
        this.attackBonus = attackBonus;
    }
    
    public int getDSize() {
        return dSize;
    }

    public void setDSize(int dSize) {
        this.dSize = dSize;
    }

    public boolean isHarmful() {
        return harmful;
    }

    public void setHarmful(boolean harmful) {
        this.harmful = harmful;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVital() {
        return vital;
    }

    public void setVital(boolean vital) {
        this.vital = vital;
    }
    
    public int getAttackBonus() {
        return attackBonus;
    }

    public void setAttackBonus(int attackBonus) {
        this.attackBonus = attackBonus;
    }
    
    public boolean isImmobilized() {
        return immobilized;
    }

    public void setImmobilized(boolean immobilized) {
        this.immobilized = immobilized;
    }
    
    /*
     * Equip Items
     */
    public boolean canEquip() {
        return canEquip;
    }

    public void setCanEquip(boolean canEquip) {
        this.canEquip = canEquip;
    }
    
    public Item getEquippedItem() {
        return equippedItem;
    }

    public String setEquippedItem(Item item) {
        if (!this.hasEquippedItem()) {
            if (this.getHealth() > 0 && !this.isImmobilized()) {
                if (item.isEquippable()) {
                    // Let the body part to know that it has equipped an item
                    this.equippedItem = item;
                    // Let the item know that it has been equipped
                    item.setEquipped(true);
                } else {
                    return "NOT_EQUIPPABLE";
                }
            } else {
                if (this.getHealth() <= 0) {
                    return "BAD_CONDITION";
                } else if (this.isImmobilized()) {
                    return "IMMOBILIZED";
                }
            }
        } else {
            return "ALREADY_HAS_ITEM";
        }
        return "OK";
    }
    
    public boolean hasEquippedItem() {
        if (equippedItem != null) {
            return true;
        }
        return false;
    }
    
    public String removeEquippedItem() {
        if (this.hasEquippedItem()) {
            // Get the item reference
            Item item = this.getEquippedItem();
            // Set null the body's item reference
            this.equippedItem = null;
            // Let the item know that it's not equipped anymore
            item.setEquipped(false);
        } else {
            return "IT_HAS_NO_EQUIP";
        }
        
        return "OK";
    }
    
    /*
     * Cloths
     */
    public ArrayList<Cloth> getWearables() {
        return wearables;
    }

    public void setWearables(ArrayList<Cloth> wearables) {
        this.wearables = wearables;
    }
    
    public String addWearable(Cloth cloth) {
        if (cloth.getCompatibleWith().equals(this.name)) {
            for (Cloth wearable : this.wearables) {
                if (wearable.getLevel().equals(cloth.getLevel())) {
                    return "ALREADY_WEARS_ON_SAME_LEVEL";
                }
            }
            // Add the cloth to the body part's wearables
            this.wearables.add(cloth);
            // Let the cloth know that it is worn
            cloth.setWorn(true);
        }
        return "OK";
    }
    
    public void removeWearable(Cloth cloth) {
        for (Cloth wearable : this.wearables) {
            if (wearable == cloth) {
                // Remove the cloth from the wearables
                this.wearables.remove(cloth);
                // Let the cloth know that is not worn anymore
                cloth.setWorn(false);
                break;
            }
        }
    }
    
    public void removeWearable(String level) {
        for (Cloth wearable : this.wearables) {
            if (wearable.getLevel().equals(level)) {
                // Remove the cloth from the wearables
                this.wearables.remove(wearable);
                // Let the cloth know that is not worn anymore
                wearable.setWorn(false);
                break;
            }
        }
    }
    
    public Cloth getWearableAtLevel(String level) {
        Cloth returnedCloth = null;
        for (Cloth wearable : this.wearables) {
            if (wearable.getLevel().equals(level)) {
                returnedCloth = wearable;
                break;
            }
        }
        return returnedCloth;
    }
}
