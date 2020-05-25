/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package basicengine.battle;

import basicengine.character.Character;
import basicengine.items.Furniture;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EmptyStackException;
import java.util.Random;

/**
 *
 * @author JVoulg
 */
public class Battle {
    
    private static Random random = new Random();
    private ArrayList<ArrayList<Character>> arena = new ArrayList<>();
    private ArrayList<ArrayList<Furniture>> arenaFurnitures = new ArrayList<>();
    private ArrayList<Character> combatants;
    private ArrayList<Character> playerTeam = new ArrayList<>();
    private ArrayList<Character> enemyTeam = new ArrayList<>();
    private ArrayList<Character> realPlayers = new ArrayList<>();
    private ArrayList<Furniture> furnitures; 
    private Character[][] journal;
    private AI AI;
    
    public Battle(ArrayList<Character> combatants, int arenaSize) {
        // Call the next constructor with 0 handicap
        this(combatants, null, arenaSize, 0);
    }
    
    public Battle(ArrayList<Character> combatants, ArrayList<Furniture> furnitures, int arenaSize) {
        // Call the next constructor with 0 handicap
        this(combatants, furnitures, arenaSize, 0);
    }
    
    public Battle(ArrayList<Character> combatants, ArrayList<Furniture> furnitures, int arenaSize, int playerHandicap) {
        
        // Handicap must be less than arena size
        if (playerHandicap >= arenaSize) {
            throw new EmptyStackException();
        }
        
        // Setup the arena's length (characters + furnitures)
        for (int i = 0; i < arenaSize; i++) {
            this.arena.add(new ArrayList<>());
            this.arenaFurnitures.add(new ArrayList<>());
        }
        
        // Add the furnitures in the arena
        this.furnitures = furnitures;
        if ((this.furnitures != null) || !this.furnitures.isEmpty()) {
            int furniturePosition;
            for (Furniture furniture : this.furnitures) {
                furniturePosition = random.nextInt(arenaSize);
                this.arenaFurnitures.get(furniturePosition).add(furniture);
                System.out.println("Added furniture " + furniture.getName() + " to position " + furniturePosition);
            }
        }
        
        // Define the combatants
        this.combatants = combatants;
        
        // Add the combatants in the arena and in their teams
        int enemyPosition;
        for (Character combatant : this.combatants) {
            if (combatant.isPlayer() || !combatant.isAgainst()) {
                // Player or allies in first place
                this.arena.get(0).add(combatant);
                this.playerTeam.add(combatant);
                if (combatant.isPlayer()) {
                    this.realPlayers.add(combatant);
                }
            } else {
                // Enemies in the rest places (depending on player handicap)
                enemyPosition = random.nextInt(arenaSize);
                if (playerHandicap > 0 && enemyPosition < playerHandicap) {
                    enemyPosition = playerHandicap;
                }
                this.arena.get(enemyPosition).add(combatant);
                this.enemyTeam.add(combatant);
            }
        }
        
        // Initiatives
        this.sortCombatants();
        
        // Init journal
        this.initJournal();
        
        // Set AI
        this.AI = new AI(arena, arenaFurnitures, journal);
        
    }
    
    public Battle(ArrayList<Character> combatants, ArrayList<Furniture> furnitures, ArrayList<Integer> enemiesPosition, int arenaSize) {
        // TODO
    }
    
    /*
     * Initiatives
     */
    private void sortCombatants() {
        Collections.sort(this.combatants, new DexterityComparator());
    }
    
    /*
     * Initiatives comparator
     */
    private class DexterityComparator implements Comparator<Character> {
        @Override
        public int compare(Character c1, Character c2) {
            int c1d20 = random.nextInt(20) + 1;
            int c2d20 = random.nextInt(20) + 1;
            return (c2.getDexterity().getModifier() + c2d20) - (c1.getDexterity().getModifier() + c1d20);
        }
    }
    
    /*
     * Init Journal
     */
    private void initJournal() {
        int jSize = this.combatants.size();
        this.journal = new Character[jSize][3];
        
        // Set the journal init values
        // journal[x][0] = character
        // journal[x][1] = last "attack to" character
        // journal[x][2] = last "attack from" character
        int i = 0;
        for (Character c : this.combatants) {
            this.journal[i][0] = c;
            this.journal[i][1] = null;
            this.journal[i][2] = null;
            
            i++;
        }
    }
    
    /*
     * Getters and Setters
     */
    public ArrayList<ArrayList<Character>> getArena() {
        return arena;
    }

    public void setArena(ArrayList<ArrayList<Character>> arena) {
        this.arena = arena;
    }

    public ArrayList<Character> getCombatants() {
        return combatants;
    }

    public void setCombatants(ArrayList<Character> combatants) {
        this.combatants = combatants;
    }

    public ArrayList<Character> getEnemyTeam() {
        return enemyTeam;
    }

    public void setEnemyTeam(ArrayList<Character> enemyTeam) {
        this.enemyTeam = enemyTeam;
    }

    public ArrayList<Character> getPlayerTeam() {
        return playerTeam;
    }

    public void setPlayerTeam(ArrayList<Character> playerTeam) {
        this.playerTeam = playerTeam;
    }

    public Character[][] getJournal() {
        return journal;
    }

    public void setJournal(Character[][] journal) {
        this.journal = journal;
    }

    public AI getAI() {
        return AI;
    }

    public void setAI(AI AI) {
        this.AI = AI;
    }
    
    /*
     * Main method that starts battle
     */
    public void start() {
        int roundCount = 1;
        
        System.out.println("A fight has started!");
        while ((this.realPlayers.isEmpty() ? isTeamAlive(this.playerTeam) : isTeamAlive(this.realPlayers))
                    && isTeamAlive(this.enemyTeam)) {
            for (Character combatant : this.combatants) {
                turn(combatant);
            }
            roundCount++;
            
            // End of round
            // Check if any combatants left the fight
            ArrayList<Character> leftCombatants = new ArrayList<Character>();
            for (Character combatant : this.combatants) {
                if (combatant.isDead()) {
                    leftCombatants.add(combatant);
                }
            }
            this.combatants.removeAll(leftCombatants);
        }
        
        // Battle end message
        System.out.println("Total rounds: " + roundCount);
        if (!isTeamAlive(this.enemyTeam)) {
            System.out.println("You win!");
        } else {
            System.out.println("You lose!");
        }
        
        finish();
        
    }
    
    private void finish() {
        if (!isTeamAlive(this.enemyTeam)) {
            restoreTeam(this.playerTeam);
        } else {
            restoreTeam(this.enemyTeam);
        }
    }
    
    /*
     * Battle mechanics
     */
    private void turn(Character c) {
        System.out.println("It is " + c.getName() + "'s turn.");
        if (!c.isDead() && !c.isUnconscious()) {
            if (c.isStunned()) {
                System.out.println(c.getName() + " is stunned.");
            }
            if (c.isPlayer()) {
                // TODO
            } else {
                computerAI(c);
            }
        } else if (c.isDead()) {
            System.out.println(c.getName() + " is dead.");
        } else if (c.isUnconscious()) {
            System.out.println(c.getName() + " is unconscious.");
        }
        System.out.println("");
    }
    
    private void computerAI(Character c) {
        /*
         * Define opponents
         */
        ArrayList<Character> opponents;
        if (c.isAgainst()) {
            // If character is against the player, he will attack him or his allies
            opponents = this.playerTeam;
        } else {
            // Otherwise it will attack at the player's enemies
            opponents = this.enemyTeam;
        }
        
        AI.takeAction(c, opponents);
        
    }
    
    private boolean isTeamAlive(ArrayList<Character> team) {
        boolean isAlive = false;
        for (Character member : team) {
            if (!member.isDead()) {
                isAlive = true;
                break;
            }
        }
        return isAlive;
    }
    
    private void restoreTeam(ArrayList<Character> team) {
        for (Character member : team) {
            if (!member.isDead()) {
                member.setBreath(100);
                member.setStamina(100);
                member.setFear(0);
                member.setAnxiety(0);
                member.setUnconscious(false);
                member.setPoisoned(false);
                member.setStunned(false);
                member.unsetCovered();
            }
        }
    }
        
}
