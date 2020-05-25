/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package basicengine.battle;

import basicengine.Constants;
import basicengine.character.BodyPart;
import java.util.ArrayList;
import java.util.Random;
import basicengine.character.Character;
import basicengine.items.Furniture;
import basicengine.items.Item;

/**
 *
 * @author JVoulg
 */
public class AI {
    
    private static Random random = new Random();
    private ArrayList<ArrayList<Character>> arena;
    private ArrayList<ArrayList<Furniture>> arenaFurnitures;
    private Character[][] journal;
    
    public AI(ArrayList<ArrayList<Character>> arena, ArrayList<ArrayList<Furniture>> arenaFurnitures, Character[][] journal) {
        this.arena = arena;
        this.arenaFurnitures = arenaFurnitures;
        this.journal = journal;
    }
    
    public void takeAction(Character c, ArrayList<Character> opponents) {
        // Available opponents
        ArrayList<Character> availableOpponents = Utility.getAvailableOpponents(opponents);
        
        // If there are available targets and can attack then continue
        if (!availableOpponents.isEmpty()) {
            /*
            Select opponent
            */
            Character opponent = selectOpponent(c, availableOpponents);
            System.out.println("Selected opponent = " + opponent.getName());
            
            // Opponent's INITIAL distance
            int distance = Utility.getDistanceBetweenCombatants(c, opponent, arena);
            System.out.println("Opponent's distance = " + distance);
            
            /*
            Check MOVE
            */
            // Check if CAN MOVE
            boolean canMove = false;
            if (!c.isStunned()) {
                canMove = true;
            }
            
            // Check if WANTS to MOVE
            boolean wantsToMove = false;
            // 0 = no, 1 = yes
            if (canMove && random.nextInt(2) == 1) {
                wantsToMove = true;
            }
            
            // Check if NEEDS to MOVE
            boolean needsToMove = false;
            if (canMove && distance != 0 && !c.canAttackRanged()) {
                needsToMove = true;
            }
            
            // Decide if MOVE or not
            if (wantsToMove || needsToMove) {
                System.out.println(c.getName() + " decides to move.");
                moveToOpponent(c, opponent, distance);
                // Opponent's NEW distance
                distance = Utility.getDistanceBetweenCombatants(c, opponent, arena);
                System.out.println("Opponent's new distance = " + distance);
            }
            
            /*
            Select cover furniture based on new position
            */
            // Available furnitures
            ArrayList<Furniture> availableFurnitures = Utility.getAvailableFurnitures(arenaFurnitures, Utility.getCombatantPosition(c, arena), availableOpponents);
            Furniture furniture = null;
            if (!availableFurnitures.isEmpty()) {
                furniture = selectFurniture(availableFurnitures);
                System.out.println("Selected furniture = " + furniture.getName() + ", value = " + furniture.getCoverValue());
            }
            
            /*
            Check ATTACK and TAKE COVER
            */
            
            int actionsNum = 0;
            
            // Check if CAN ATTACK
            boolean canAttack = false;
            if (distance == 0 && c.canAttack()) {
                canAttack = true;
                actionsNum++;
            } else if (distance != 0 && c.canAttackRanged()) {
                canAttack = true;
                actionsNum++;
            }
            
            // Check if CAN TAKE COVER
            boolean canTakeCover = true;
            boolean opponentsCanAttackRanged = false;
            for (Character op : availableOpponents) {
                if (op.canAttackRanged()) {
                    opponentsCanAttackRanged = true;
                    break;
                }
            }
            // If already covered and cover is the best then do not cover again
            if (furniture == null || c.isCovered() && (c.getCover().getCoverValue() >= furniture.getCoverValue())) {
                canTakeCover = false;
            }
            // If selected opponent is nearby do not cover
            else if (distance == 0) {
                canTakeCover = false;
            }
            // If opponents do not have ranged weapons then don't take cover
            else if (!opponentsCanAttackRanged) {
                canTakeCover = false;
            }
            else {
                actionsNum++;
            }
            
            /*
            Take ACTION
            */
            if (actionsNum != 0) {
                switch(random.nextInt(actionsNum)) {
                    case 0:
                        if (canAttack) {
                            // ATTACK
                            System.out.println(c.getName() + " decides to attack.");
                            attack(c, opponent, distance);
                        } else if (canTakeCover) {
                            // TAKE COVER
                            System.out.println(c.getName() + " decides to take cover.");
                            takeCover(c, furniture);
                        }
                        break;
                    case 1:
                        // TAKE COVER
                        System.out.println(c.getName() + " decides to take cover.");
                        takeCover(c, furniture);
                        break;
                }
            } else {
                System.out.println(c.getName() + " stopped fighting as " + c.getPronoun() + " can neither attack nor take cover.");
            }
            
        } else {
            System.out.println(c.getName() + " stopped fighting as there are no available targets.");
        }
    }
    
    private Character selectOpponent(Character c, ArrayList<Character> availableOpponents) {
        Character opponent = null;
        Character lastAttTo = Utility.getLastAttackTo(c, journal);
        Character lastAttFrom = Utility.getLastAttackFrom(c, journal);
        ArrayList<Character> nearestOpponents = Utility.getNearestOpponents(c, availableOpponents, arena);
        
        if (lastAttFrom != null && availableOpponents.contains(lastAttFrom) && nearestOpponents.contains(lastAttFrom)) {
            opponent = lastAttFrom;
        } else if (lastAttTo != null && availableOpponents.contains(lastAttTo) && nearestOpponents.contains(lastAttFrom)) {
            opponent = lastAttTo;
        } else {
            opponent = nearestOpponents.get(random.nextInt(nearestOpponents.size()));
        }
        return opponent;
    }
    
    private Furniture selectFurniture(ArrayList<Furniture> availableFurnitures) {
        ArrayList<Furniture> bestOptions = new ArrayList<>();
        for (Furniture f : availableFurnitures) {
            if (bestOptions.isEmpty()) {
                bestOptions.add(f);
            } else if (f.getCoverValue() == bestOptions.get(0).getCoverValue()) {
                bestOptions.add(f);
            } else if (f.getCoverValue() > bestOptions.get(0).getCoverValue()) {
                bestOptions.clear();
                bestOptions.add(f);
            }
        }
        return bestOptions.get(random.nextInt(bestOptions.size()));
    }
    
    private void attack(Character c, Character opponent, int distance) {
        // Check if opponent is already stunned
        boolean isAlreadyStunned = opponent.isStunned();

        /*
         * Define how will attack
         */
        // Get attack options
        ArrayList<Object> attackOptions = Utility.getAttackOptions(c);
        ArrayList<BodyPart> bodyParts = (ArrayList<BodyPart>) attackOptions.get(0);
        ArrayList<Item> equippedItems = (ArrayList<Item>) attackOptions.get(1);
        ArrayList<Item> rangedItems = (ArrayList<Item>) attackOptions.get(2);

        // For the time being we'll just leave the choise to luck
        BodyPart attackWithBodyPart = null;
        Item attackWithItem = null;
        
        // If we need to attack with ranged weapons then we select from the equipped items
        if (distance != 0 && !rangedItems.isEmpty()) {
            attackWithItem = rangedItems.get(random.nextInt(rangedItems.size()));
        }
        // If we have both unarmed and armed options choose one of them
        else if (!bodyParts.isEmpty() && !equippedItems.isEmpty()) {
            // The character should prefer to attack with weapons
            if (random.nextInt(10) >= Constants.preferWeapon ) {
                attackWithBodyPart = bodyParts.get(random.nextInt(bodyParts.size()));
            } else {
                attackWithItem = equippedItems.get(random.nextInt(equippedItems.size()));
            }
        } else if (!bodyParts.isEmpty()) {
            attackWithBodyPart = bodyParts.get(random.nextInt(bodyParts.size()));
        } else {
            attackWithItem = equippedItems.get(random.nextInt(equippedItems.size()));
        }

        /*
         * Define where will attack (excluding neck as it is for special cases)
         */
        ArrayList<BodyPart> oppBodyParts = new ArrayList<>();
        // Check if the character will attack only on vital parts
        boolean onlyVital = false;
        if (random.nextInt(10) >= Constants.preferNonVital) {
            onlyVital = true;
        }
        for (BodyPart oppbodyPart : opponent.getBodyParts()) {
            if (opponent.isUnconscious()) {
                // If unconscious attack on vital parts (or TODO special case)
                if (oppbodyPart.isVital() && oppbodyPart.getHealth() > Constants.bodyPartDeadLimit && !oppbodyPart.getName().equals("neck")) {
                    oppBodyParts.add(oppbodyPart);
                }
            } else {
                // If not unconscious attack wherever you want
                if (oppbodyPart.getHealth() > Constants.bodyPartDeadLimit && !oppbodyPart.getName().equals("neck")) {
                    if (!onlyVital) {
                        oppBodyParts.add(oppbodyPart);
                    } else if (oppbodyPart.isVital()) {
                        oppBodyParts.add(oppbodyPart);
                    }
                }
            }
        }
        BodyPart attackToBodyPart = oppBodyParts.get(random.nextInt(oppBodyParts.size()));

        /*
         * Attack
         */
        int damage = 0;
        if (attackWithBodyPart != null) {
            System.out.println(c.getName() + " attacks " + opponent.getName()
                    + " at " + attackToBodyPart.getName()
                    + " with " + attackWithBodyPart.getName() + "!");

            damage = c.getActions().attackOn(opponent, attackToBodyPart, attackWithBodyPart);
        } else if (attackWithItem != null) {
            System.out.println(c.getName() + " attacks " + opponent.getName()
                    + " at " + attackToBodyPart.getName()
                    + " with " + attackWithItem.getName() + "!");

            damage = c.getActions().attackOn(opponent, attackToBodyPart, attackWithItem, distance);
        }
        
        // Update Journal
        Utility.setLastAttackTo(c, opponent, journal);
        Utility.setLastAttackFrom(opponent, c, journal);
        
        // Print results
        System.out.println(c.getName() + " did " + damage + " damage!");
        System.out.println(opponent.getName() + "'s current condition.");
        System.out.println("Head condition: " + opponent.getHead().getHealth());
        System.out.println("Neck condition: " + opponent.getNeck().getHealth());
        System.out.println("Upper Body condition: " + opponent.getUpperBody().getHealth());
        System.out.println("Lower Body condition: " + opponent.getLowerBody().getHealth());
        System.out.println("Left Hand condition: " + opponent.getLeftHand().getHealth());
        System.out.println("Right Hand condition: " + opponent.getRightHand().getHealth());
        System.out.println("Left Leg condition: " + opponent.getLeftLeg().getHealth());
        System.out.println("Right Leg condition: " + opponent.getRightLeg().getHealth());

        // Opponents condition after attack
        Utility.setConditionAfterAttack(opponent, attackToBodyPart, damage, isAlreadyStunned);
    }
    
    /*
     * Move to opponent base on current distance
     */
    private void moveToOpponent(Character c, Character opponent, int distance) {
        // If character is covered set him as uncovered
        if (c.isCovered()) {
            c.unsetCovered();
        }
        
        // Character's current position
        int position = Utility.getCombatantPosition(c, arena);
        
        // Remove character from current position
        arena.get(position).remove(c);
        
        // Add character to new position
        if (distance > 0) {
            arena.get(position - 1).add(c);
        } else if (distance < 0) {
            arena.get(position + 1).add(c);
        }
        
        // Update Journal
        // Move is offensive, thus it is considered as an attack move
        Utility.setLastAttackTo(c, opponent, journal);
    }
    
    private void takeCover(Character c, Furniture f) {
        c.setCovered(true, f);
    }
    
}
