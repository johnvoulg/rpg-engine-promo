/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package basicengine.battle;

import basicengine.Constants;
import basicengine.character.BodyPart;
import basicengine.character.Character;
import basicengine.items.Furniture;
import basicengine.items.Item;
import java.util.ArrayList;

/**
 *
 * @author JVoulg
 */
public class Utility {
    
    protected static void setConditionAfterAttack(Character character, BodyPart attackedBodyPart, int damage, boolean isAlreadyStunned) {
        if (damage != 0) {
            if (attackedBodyPart.isVital()) {
                if (attackedBodyPart.getHealth() <= 0 && attackedBodyPart.getHealth() > Constants.bodyPartDeadLimit) {
                    System.out.println(character.getName() + " is unconscious!");
                    character.setUnconscious(true);
                } else if (attackedBodyPart.getHealth() <= Constants.bodyPartDeadLimit) {
                    System.out.println(character.getName() + " is dead!");
                    character.setDead(true);
                }
            } else if (!isAlreadyStunned && character.isStunned()) {
                System.out.println(character.getName() + " is stunned!");
            }
        }
    }
    
    protected static ArrayList<Character> getAvailableOpponents(ArrayList<Character> opponents) {
        ArrayList<Character> availableOpponents = new ArrayList<Character>();
        for (Character op : opponents) {
            if (!op.isDead()) {
                availableOpponents.add(op);
                System.out.println("availableOpponents name = " + op.getName());
            }
        }
        System.out.println("availableOpponents size = " + availableOpponents.size());
        
        return availableOpponents;
    }
    
    protected static ArrayList<Furniture> getAvailableFurnitures(ArrayList<ArrayList<Furniture>> arenaFurnitures, int characterPosition, ArrayList<Character> opponents) {
        ArrayList<Furniture> furnitures = arenaFurnitures.get(characterPosition);
        ArrayList<Furniture> unavailableFurnitures = new ArrayList<>();
        for (Furniture furniture : furnitures) {
            for (Character c : opponents) {
                if (c.isCovered() && furniture.equals(c.getCover())) {
                    unavailableFurnitures.add(furniture);
                    break;
                }
            }
        }
        furnitures.removeAll(unavailableFurnitures);
        return furnitures;
    }
    
    protected static ArrayList<Character> getNearestOpponents(Character c, ArrayList<Character> opponents, ArrayList<ArrayList<Character>> arena) {
        ArrayList<Character> nearestOpponents = new ArrayList<Character>();
        int currentDistance = 0;
        int newDistance = 0;
        
        for (Character opponent : opponents) {
            if (nearestOpponents.isEmpty()) {
                nearestOpponents.add(opponent);
            } else {
                currentDistance = getDistanceBetweenCombatants(c, nearestOpponents.get(0), arena);
                newDistance = getDistanceBetweenCombatants(c, opponent, arena);
                
                if (currentDistance > newDistance) {
                    nearestOpponents.clear();
                    nearestOpponents.add(opponent);
                } else if (currentDistance == newDistance) {
                    nearestOpponents.add(opponent);
                }
            }
        }
        
        return nearestOpponents;
    }
    
    protected static ArrayList<Object> getAttackOptions(Character c) {
        ArrayList<Object> attackOptions = new ArrayList<>();
        ArrayList<BodyPart> bodyParts = new ArrayList<>();
        ArrayList<Item> equippedItems = new ArrayList<>();
        ArrayList<Item> rangedItems = new ArrayList<>();
        for (BodyPart bodyPart : c.getBodyParts()) {
            // Unarmed options (excluding head as it is for special cases AND body parts with equiped items that are weapons)
            if (bodyPart.isHarmful() && !bodyPart.isImmobilized() && bodyPart.getHealth() > 0 && !bodyPart.getName().equals("head")) {
                if (!bodyPart.hasEquippedItem()) {
                    bodyParts.add(bodyPart);
                } else if (!bodyPart.getEquippedItem().isWeapon()) {
                    bodyParts.add(bodyPart);
                }
            }
            // Armed options
            if (bodyPart.canEquip() && bodyPart.hasEquippedItem() && bodyPart.getEquippedItem().isWeapon()
                    && !bodyPart.isImmobilized() && bodyPart.getHealth() > 0) {
                
                equippedItems.add(bodyPart.getEquippedItem());
                
                if (bodyPart.getEquippedItem().isRanged()) {
                    rangedItems.add(bodyPart.getEquippedItem());
                }
            }
        }
        
        attackOptions.add(bodyParts);
        attackOptions.add(equippedItems);
        attackOptions.add(rangedItems);
        return attackOptions;
    }
    
    protected static int getCombatantPosition(Character combatant, ArrayList<ArrayList<Character>> arena) {
        int position = 0;
        for (ArrayList combatantsPerPosition : arena) {
            if (combatantsPerPosition != null && combatantsPerPosition.contains(combatant)) {
                position = arena.indexOf(combatantsPerPosition);
                break;
            }
        }
        return position;
    }
    
    /*
     * Characters' distance
     * It might return a negative number
     */
    protected static int getDistanceBetweenCombatants(Character comb1, Character comb2, ArrayList<ArrayList<Character>> arena) {
        int comb1Position = getCombatantPosition(comb1, arena);
        int comb2Position = getCombatantPosition(comb2, arena);
        int distance = comb1Position - comb2Position;
        return distance;
    }
    
    /*
     * Journal info
     */
    private static int getCharacterJournalPos(Character character, Character[][] journal) {
        int charPos = 0;
        for (int i = 0; i < journal.length; i++) {
            if (journal[i][0].equals(character)) {
                charPos = i;
                break;
            }
        }
        return charPos;
    }
    
    protected static Character getLastAttackTo(Character character, Character[][] journal) {
        int charPos = getCharacterJournalPos(character, journal);
        return journal[charPos][1];
    }
    
    protected static void setLastAttackTo(Character character, Character opponent, Character[][] journal) {
        int charPos = getCharacterJournalPos(character, journal);
        journal[charPos][1] = opponent;
    }
    
    protected static Character getLastAttackFrom(Character character, Character[][] journal) {
        int charPos = getCharacterJournalPos(character, journal);
        return journal[charPos][2];
    }
    
    protected static void setLastAttackFrom(Character character, Character opponent, Character[][] journal) {
        int charPos = getCharacterJournalPos(character, journal);
        journal[charPos][2] = opponent;
    }
    
}
