/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package basicengine.character;

/**
 *
 * @author JVoulg
 */
public class Ability {
    
    private String name;
    private int originalValue;
    private int value; // With wearables
    private int modifier;
    private int[][] abilityModifiers;
    
    Ability(String name, int value) {
        this.name = name;
        this.originalValue = value;
        this.value = value;
        
        this.abilityModifiers = new int[50][3];
        int abilityLevel = 0;
        for (int i = 0, modifierValue = -5; i < 50; i++, modifierValue++, abilityLevel += 2 ) {
            this.abilityModifiers[i][0] = abilityLevel;
            this.abilityModifiers[i][1] = abilityLevel+1;
            this.abilityModifiers[i][2] = modifierValue;
        }
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getOriginalValue() {
        return originalValue;
    }
    
    public void setOriginalValue(int originalValue) {
        this.originalValue = originalValue;
    }
    
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value < 10) {
            this.value = 10;
        } else {
            this.value = value;
        }
    }
    
    public int getModifier() {
        this.modifier = -5;
        for (int i=0; i<this.abilityModifiers.length; i++) {
            if (this.value >= this.abilityModifiers[i][0]
                    && this.value <= this.abilityModifiers[i][1]) {
            
                    modifier = this.abilityModifiers[i][2];
                    break;
            }
        }
        return modifier;
    }
}
