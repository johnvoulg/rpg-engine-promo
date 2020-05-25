/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package basicengine.character;

import basicengine.Constants;
import basicengine.items.Cloth;
import basicengine.items.Inventory;
import basicengine.items.Item;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author JVoulg
 */
public class Actions {
    
    private Random random = new Random();
    
    // Character
    private Character character;
    
    // Containers
    private Inventory inventory;
    private ArrayList<Cloth> clothes = new ArrayList<Cloth>();
    
    public Actions(Character character) {
        this.character = character;
    }
    
    /*
     * ATTACK RULES:
     * Attack Roll:
     * An attack roll represents your attempt to strike your opponent on your
     * turn in a round. When you make an attack roll, you roll a d20 and add
     * your attack bonus. (Other modifiers may also apply to this roll.) If your
     * result equals or beats the target’s Armor Class, you hit and deal damage.
     * 
     * Attack Bonus:
     * Your attack bonus with a melee weapon is:
     * Dexterity modifier
     * With a ranged weapon, your attack bonus is:
     * Constitution modifier
     * 
     * Damage Roll:
     * d* + Bonus
     * 
     * DEFENCE RULES:
     * Your Defence Class (DC) represents how hard it is for opponents to land a
     * solid, damaging blow on you. It’s the attack roll result that an opponent
     * needs to achieve to hit you. Your DC is equal to the following:
     * d20 + Dexterity modifier
     */
    public boolean attackRoll(int attackModifier, int defenceModifier) {
        
        // Calculate attack roll
        int d20Attack = Constants.attackModifierHandicap + this.random.nextInt(Constants.attackModifierRest);
        int attRoll = d20Attack + attackModifier;
        
        // Calculate defender's defence class
        int d20Def = this.random.nextInt(20) + 1;
        int defClass = d20Def + defenceModifier;
        
        if (attRoll >= defClass) {
            return true;
        }
        
        return false;
    }
    
    public int attackDamage(int dSize, int attackBonus, int attackModifier) {
        int dAttack = this.random.nextInt(dSize) + 1;
        int modBonus = (int) attackModifier/10;
        return dAttack + attackBonus + modBonus;
    }
    
    /*
     * Fight actions
     */
    // Attack armed
    private int attack(int oppDexterityModifier, Item weapon) {
        
        boolean attSuccess = false;
        int attDamage = 0;
                
        attSuccess = this.attackRoll(character.getDexterity().getModifier(), oppDexterityModifier);

        if (attSuccess) {
            if (weapon.isMelee()) {
                attDamage = this.attackDamage(weapon.getDSize(), weapon.getAttackBonus(), character.getStrength().getModifier());
            } else if (weapon.isRanged()) {
                attDamage = this.attackDamage(weapon.getDSize(), weapon.getAttackBonus(), character.getConstitution().getModifier());
            }
        }
        
        return attDamage;
    }
    
    // Attack unarmed
    private int attack(int oppDexterityModifier, BodyPart bodyPart) {
        
        boolean attSuccess = false;
        int attDamage = 0;
                
        attSuccess = this.attackRoll(character.getDexterity().getModifier(), oppDexterityModifier);

        if (attSuccess) {
            attDamage = this.attackDamage(bodyPart.getDSize(), bodyPart.getAttackBonus(), character.getStrength().getModifier());
        }
        
        return attDamage;
    }
    
    // Attack on armed
    public int attackOn(Character opponent, BodyPart opponentsBodyPart, Item weapon, int distance) {
        // Calculate Damage
        int oppDefModifier = getOppDefModifier(opponent, distance);
        int damage = this.attack(oppDefModifier, weapon);
        
        // Do the Damage
        if (damage > 0) {
            int newHealth = opponentsBodyPart.getHealth() - damage;
            opponentsBodyPart.setHealth(newHealth);
        }
        
        return damage;
    }
    
    // Attack on unarmed
    public int attackOn(Character opponent, BodyPart opponentsBodyPart, BodyPart attackersBodyPart) {
        
        // Calculate Damage
        // (we take instantly the dexterity modifier as unarmed attacks have by default 0 distance)
        int damage = this.attack(opponent.getDexterity().getModifier(), attackersBodyPart);
        
        // Do the Damage
        if (damage > 0) {
            int newHealth = opponentsBodyPart.getHealth() - damage;
            opponentsBodyPart.setHealth(newHealth);
        }
        
        return damage;
    }
    
    private int getOppDefModifier(Character opponent, int distance) {
        int oppDefModifier;
        if (distance == 0 || !opponent.isCovered()) {
            oppDefModifier = opponent.getDexterity().getModifier();
        } else {
            System.out.println(opponent.getName() + " is covered!");
            oppDefModifier = opponent.getDexterity().getModifier()
                    + (int) (opponent.getDexterity().getModifier() * opponent.getCover().getCoverValue()/100);
        
                    System.out.println("Simple modifier: " + opponent.getDexterity().getModifier());
                    System.out.println("Added modifier: " + (opponent.getDexterity().getModifier()
                                + (int) (opponent.getDexterity().getModifier() * opponent.getCover().getCoverValue()/100)));
        }
        return oppDefModifier;
    }
    
    /*
     * Item Actions
     */ 
    public void equipInventory(Inventory inventory) {
        this.inventory = inventory;
    }
    
    public Inventory getInventory() {
        return inventory;
    }
    
    public String addToInventory(Item item) {
        return this.inventory.addItem(item);
    }
    
    public Item getFromInventory(Item item) {
        if (this.inventory.getItems().contains(item)) {
            this.inventory.removeItem(item);
        } else {
            return null;
        }
        
        return item;
    }
    
    public ArrayList<Item> getEquippedItems() {
        ArrayList<Item> equippedItems = new ArrayList<>();
        for (BodyPart bodyPart : character.getBodyParts()) {
            if (bodyPart.canEquip() && bodyPart.hasEquippedItem()) {
                equippedItems.add(bodyPart.getEquippedItem());
            }
        }
        return equippedItems;
    }
    
    public String equipItem(Item item, BodyPart bodyPart) {
        // Safe check that the item exists in inventory and then remove it
        if (this.inventory.getItems().contains(item)) {
            this.inventory.removeItem(item);
        } else {
            return "NOT_IN_INVENTORY";
        }
        
        return bodyPart.setEquippedItem(item);
    }
    
    public String unequipItem(BodyPart bodyPart, boolean discard) {
        if (discard) {
            return bodyPart.removeEquippedItem();
        } else {
            Item item = bodyPart.getEquippedItem();
            String addResult = this.inventory.addItem(item);
            if (addResult.equals("OK")) {
                return bodyPart.removeEquippedItem();
            } else {
                return addResult;
            }
        }
    }
    
    /*
     * Clothes Actions
     */
    // Add clothes massively
    public void setClothes(ArrayList<Cloth> clothes) {
        for (Cloth cloth : clothes) {
            this.addCloth(cloth);
        }
    }
    
    public ArrayList<Cloth> getClothes() {
        return this.clothes;
    }
    
    public Cloth getCloth(String bodyPartName, String level) {
        Cloth cloth = null;
        BodyPart bodyPart = character.getBodyPartByName(bodyPartName);
        cloth = bodyPart.getWearableAtLevel(level);
        return cloth;
    }
    
    // Add cloth and return the previous one if exists
    public Cloth addCloth(Cloth cloth) {
        
        Cloth returnedCloth = null;
        
        BodyPart bodyPart = character.getBodyPartByName(cloth.getCompatibleWith());
        returnedCloth = bodyPart.getWearableAtLevel(cloth.getLevel());
        
        if (returnedCloth != null) {
            // Remove from body part
            bodyPart.removeWearable(returnedCloth);
            // Remove from clothes container
            this.clothes.remove(returnedCloth);
        }
        // Add to body part
        bodyPart.addWearable(cloth);
        // Add to clothes container
        this.clothes.add(cloth);
        
        // Update abilities values
        character.updateAbilitiesValues();
        
        return returnedCloth;
        
    }
    
    public void removeCloth(Cloth cloth) {
        BodyPart bodyPart = character.getBodyPartByName(cloth.getCompatibleWith());
        // Remove from body part
        bodyPart.removeWearable(cloth);
        // Remove from clothes container
        this.clothes.remove(cloth);
        
        // Update abilities values
        character.updateAbilitiesValues();
    }
    
    public void removeCloth(String bodyPartName, String level) {
        BodyPart bodyPart = character.getBodyPartByName(bodyPartName);
        Cloth cloth = bodyPart.getWearableAtLevel(level);
        
        if (cloth != null) {
            this.removeCloth(cloth);
        }
    }
}
