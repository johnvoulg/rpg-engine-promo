/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package basicengine.items;

import java.util.EmptyStackException;

/**
 *
 * @author JVoulg
 */

enum ClothLevel {
    inner, middle, outer;
}

public class Cloth {
    
    // Characteristics
    private String name;
    private String color;
    private String compatibleWith;
    private String level;
    
    // Affections
    private boolean affectingStrength;
    private boolean affectingDexterity;
    private boolean affectingConstitution;
    private boolean affectingCharisma;
    private int strengthAffect;
    private int dexterityAffect;
    private int constitutionAffect;
    private int charismaAffect;
    
    // Condition
    private boolean worn;
    
    public Cloth(String name, String color, String level, String compatibleWith,
            boolean affectingStr, int strengthAffect,
            boolean affectingDex, int dexterityAffect,
            boolean affectingCon, int constitutionAffect,
            boolean affectingCha, int charismaAffect) {
        this.name = name;
        this.color = color;
        this.setLevel(level);
        this.compatibleWith = compatibleWith;
        this.affectingStrength = affectingStr;
        this.affectingDexterity = affectingDex;
        this.affectingConstitution = affectingCon;
        this.affectingCharisma = affectingCha;
        this.strengthAffect = strengthAffect;
        this.dexterityAffect = dexterityAffect;
        this.constitutionAffect = constitutionAffect;
        this.charismaAffect = charismaAffect;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }

    public String getCompatibleWith() {
        return compatibleWith;
    }

    public void setCompatibleWith(String compatibleWith) {
        this.compatibleWith = compatibleWith;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        for (ClothLevel cl : ClothLevel.values()) {
            if (cl.toString().equals(level)) {
                this.level = level;
                break;
            }
        }
        if (this.level.isEmpty()) {
            throw new EmptyStackException();
        }
    }
    
    public void setLevel(int clothLevel) {
        for (ClothLevel cl : ClothLevel.values()) {
            if (clothLevel == cl.ordinal()) {
                this.level = cl.toString();
                break;
            }
        }
        if (this.level.isEmpty()) {
            throw new EmptyStackException();
        }
    }
    
    /*
     * Abilities
     */
    public boolean isAffectingCharisma() {
        return affectingCharisma;
    }

    public void setAffectingCharisma(boolean affectingCharisma) {
        this.affectingCharisma = affectingCharisma;
    }

    public boolean isAffectingConstitution() {
        return affectingConstitution;
    }

    public void setAffectingConstitution(boolean affectingConstitution) {
        this.affectingConstitution = affectingConstitution;
    }

    public boolean isAffectingDexterity() {
        return affectingDexterity;
    }

    public void setAffectingDexterity(boolean affectingDexterity) {
        this.affectingDexterity = affectingDexterity;
    }

    public boolean isAffectingStrength() {
        return affectingStrength;
    }

    public void setAffectingStrength(boolean affectingStrength) {
        this.affectingStrength = affectingStrength;
    }

    public int getCharismaAffect() {
        return charismaAffect;
    }

    public void setCharismaAffect(int charismaAffect) {
        this.charismaAffect = charismaAffect;
    }

    public int getConstitutionAffect() {
        return constitutionAffect;
    }

    public void setConstitutionAffect(int constitutionAffect) {
        this.constitutionAffect = constitutionAffect;
    }

    public int getDexterityAffect() {
        return dexterityAffect;
    }

    public void setDexterityAffect(int dexterityAffect) {
        this.dexterityAffect = dexterityAffect;
    }

    public int getStrengthAffect() {
        return strengthAffect;
    }

    public void setStrengthAffect(int strengthAffect) {
        this.strengthAffect = strengthAffect;
    }
    
    /*
     * Condition
     */

    public boolean isWorn() {
        return worn;
    }

    public void setWorn(boolean worn) {
        this.worn = worn;
    }
    
}
