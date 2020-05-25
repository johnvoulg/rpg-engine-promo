/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package basicengine.items;

/**
 *
 * @author JVoulg
 */
public class Item {
    // Regular items constractor
    public Item(String name, int size, boolean equippable, boolean storable, boolean weapon) {
        this.name = name;
        this.size = size;
        this.equippable = equippable;
        this.storable = storable;
        this.equipped = false;
        this.weapon = weapon;
    }
    
    // Weapons constructor
    public Item(String name, int size, boolean equippable, boolean storable, boolean weapon,
            int dSize, int attackBonus, boolean melee, boolean ranged) {
        this.name = name;
        this.size = size;
        this.equippable = equippable;
        this.storable = storable;
        this.equipped = false;
        this.weapon = weapon;
        this.dSize = dSize;
        this.attackBonus = attackBonus;
        this.melee = melee;
        this.ranged = ranged;
    }
    
    private String name;
    private int size;
    private boolean equippable;
    private boolean equipped;
    private boolean storable;
    private boolean weapon;
    private int dSize;
    private int attackBonus;
    private boolean melee;
    private boolean ranged;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the equippable
     */
    public boolean isEquippable() {
        return equippable;
    }

    /**
     * @param equippable the equippable to set
     */
    public void setEquippable(boolean equippable) {
        this.equippable = equippable;
    }

    /**
     * @return the equip
     */
    public boolean isEquipped() {
        return equipped;
    }

    /**
     * @param equip the equip to set
     */
    public void setEquipped(boolean equipped) {
        if (this.isEquippable()) {
            this.equipped = equipped;
        }
    }

    /**
     * @return the storable
     */
    public boolean isStorable() {
        return storable;
    }

    /**
     * @param storable the storable to set
     */
    public void setStorable(boolean storable) {
        this.storable = storable;
    }
    
    public int getDSize() {
        return dSize;
    }

    public void setDSize(int dSize) {
        this.dSize = dSize;
    }

    public boolean isWeapon() {
        return weapon;
    }

    public void setWeapon(boolean weapon) {
        this.weapon = weapon;
    }
    
    public int getAttackBonus() {
        return attackBonus;
    }

    public void setAttackBonus(int attackBonus) {
        this.attackBonus = attackBonus;
    }
    
    public boolean isMelee() {
        return melee;
    }

    public void setMelee(boolean melee) {
        this.melee = melee;
    }

    public boolean isRanged() {
        return ranged;
    }

    public void setRanged(boolean ranged) {
        this.ranged = ranged;
    }
}
