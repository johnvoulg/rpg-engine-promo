/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package basicengine.character;

import java.util.Random;
import basicengine.items.Cloth;
import basicengine.items.Furniture;

/**
 *
 * @author JVoulg
 */
enum Gender {
    male, female;
}

enum HairColors {
    blond, brown, black, red;
}

enum HairStyles {
    longhair, bobcut, curlyhair, ponytail, wavy;
}

public class Character extends Body {
    
    private Random random = new Random();
    
    // Abilities
    private Ability strength;
    private Ability dexterity;
    private Ability constitution;
    private Ability intelligence;
    private Ability wisdom;
    private Ability charisma;
    
    // Characteristics
    private String gender;
    private String pronoun;
    private String name;
    private String surname;
    private String title;
    private int weight;
    private float height;
    private int age;
    private String[] hair = new String[2];
    
    // Survival contents
    private int stamina; // Affects actions
    private int breath; // Affects actions, strength
    private int fear; // Affects decisions
    private int anxiety; // Affects decisions

    // Character's condition
    private boolean dead;
    private boolean unconscious;
    private boolean poisoned;
    private boolean stunned;
    private boolean covered;
    
    // Character's personality
    private boolean player;
    private boolean against;
    
    // Character's actions
    private Actions actions;
    
    // Character's cover furniture
    private Furniture furniture;
    
    public Character(int gender, int age, int weight, float height, boolean player, boolean against) {
        
        // Init
        this.init(gender, age, weight, height);
        
        // Abilities
        this.strength = new Ability("strength", this.random.nextInt(91) + 10);
        this.dexterity = new Ability("dexterity", this.random.nextInt(91) + 10);
        this.constitution = new Ability("constitution", this.random.nextInt(91) + 10);
        this.intelligence = new Ability("intelligence", this.random.nextInt(91) + 10);
        this.wisdom = new Ability("wisdom", this.random.nextInt(91) + 10);
        this.charisma = new Ability("charisma", this.random.nextInt(91) + 10);
        
        this.setStrengthByAbilities();
        this.setDexterityByAbilities();
        this.setConstitutionByAbilities();
        this.setIntelligenceByAbilities();
        this.setWisdomByAbilities();
        this.setCharismaByAbilities();
        
        // Personality
        this.player = player;
        // Player is never against himself
        if (!this.player) {
            this.against = against;
        }
    }
    
    public Character(int gender, int age, int weight, float height, boolean player, boolean against,
            int strength, int dexterity, int constitution, int intelligence,
            int wisdom, int charisma) {
        
        // Init
        this.init(gender, age, weight, height);
        
        // Abilities
        this.strength = new Ability("strength", strength);
        this.dexterity = new Ability("dexterity", dexterity);
        this.constitution = new Ability("constitution", constitution);
        this.intelligence = new Ability("intelligence", intelligence);
        this.wisdom = new Ability("wisdom", wisdom);
        this.charisma = new Ability("charisma", charisma);
        
        this.setStrengthByAbilities();
        this.setDexterityByAbilities();
        this.setConstitutionByAbilities();
        this.setIntelligenceByAbilities();
        this.setWisdomByAbilities();
        this.setCharismaByAbilities();
        
        // Personality
        this.player = player;
        if (!this.player) {
            this.against = against;
        }
    }
    
    private void init(int gender, int age, int weight, float height) {
        
        // Characteristics
        this.setGender(gender);
        this.age = age;
        this.weight = weight;
        this.height = height;
        
        // Condition
        this.breath = 100;
        this.stamina = 100;
        
        // General information
        this.name = "Unknown";
        
        // Setup actions
        this.actions = new Actions(this);
    }
    
    /*
     * Characteristics Getters and Setters
     */
    public String getGender() {
        return gender;
    }
    
    public void setGender(int gender) {
        for (Gender g : Gender.values()) {
            if (gender == g.ordinal()) {
                this.gender = g.toString();
                break;
            }
        }
    }
    
    public String getPronoun() {
        return pronoun;
    }
    
    public void setPronoun(String pronoun) {
        this.pronoun = pronoun;
    }
    
    private void setPronoun() {
        if (gender.equals("male")) {
            pronoun = "he";
        } else {
            pronoun = "she";
        }
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSurname() {
        return surname;
    }
    
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getFullTitle() {
        return title+" "+surname;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public int getWeight() {
        return weight;
    }
    
    public void setWeight(int weight) {
        this.weight = weight;
    }
    
    public float getHeight() {
        return height;
    }
    
    public void setHeight(float height) {
        this.height = height;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public String[] getHair() {
        return hair;
    }
    
    public void setHair(int hairColor, int hairStyle) {
        for (HairColors hc : HairColors.values()) {
            if (hairColor == hc.ordinal()) {
                this.hair[0] = hc.toString();
            }
        }
        for (HairStyles hs : HairStyles.values()) {
            if (hairStyle == hs.ordinal()) {
                this.hair[1] = hs.toString();
            }
        }
    }
    
    /*
     * Character's condition Getters and Setters
     */
    public boolean isDead() {
        // If not dead then check
        if (!this.dead) {
            for (BodyPart bodyPart : this.getBodyParts()) {
                if (bodyPart.isVital() && bodyPart.getHealth() <= -20) {
                    this.dead = true;
                    break;
                }
            }
        }
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
    
    public int getAnxiety() {
        return anxiety;
    }
    
    public void setAnxiety(int anxiety) {
        this.anxiety = anxiety;
    }
    
    public int getFear() {
        return fear;
    }
    
    public void setFear(int fear) {
        this.fear = fear;
    }
    
    public int getBreath() {
        return breath;
    }
    
    public void setBreath(int breath) {
        this.breath = breath;
    }
    
    public int getStamina() {
        return stamina;
    }
    
    public void setStamina(int stamina) {
        this.stamina = stamina;
    }
    
    public boolean isUnconscious() {
        if (!unconscious) {
            for (BodyPart bodyPart : this.getBodyParts()) {
                if (bodyPart.isVital() && bodyPart.getHealth() <= 0 && bodyPart.getHealth() > -20) {
                    setUnconscious(true);
                    break;
                }
            }
        }
        return unconscious;
    }
    
    public void setUnconscious(boolean unconscious) {
        this.unconscious = unconscious;
    }
    
    public boolean isPoisoned() {
        return this.poisoned;
    }

    public void setPoisoned(boolean poisoned) {
        this.poisoned = poisoned;
    }

    public boolean isStunned() {
        if (!stunned) {
            for (BodyPart bodyPart : this.getBodyParts()) {
                if (bodyPart.isImmobilized()) {
                    setStunned(true);
                    break;
                }
            }
        }
        if (!stunned) {
            if ((this.getRightLeg().getHealth() <= 0 && this.getLeftLeg().getHealth() <= 0) ||
                    this.getLowerBody().getHealth() <= 0) {
                setStunned(true);
            }
        }
        return stunned;
    }

    public void setStunned(boolean stunned) {
        this.stunned = stunned;
    }
    
    public boolean isCovered() {
        return covered;
    }

    public void setCovered(boolean covered, Furniture furniture) {
        this.covered = covered;
        this.furniture = furniture;
    }
    
    public void unsetCovered() {
        this.covered = false;
        this.furniture = null;
    }
    
    public Furniture getCover() {
        return this.furniture;
    }

    public boolean canAttack() {
        // Check attack options
        for (BodyPart bodyPart : this.getBodyParts()) {
            // Unarmed options (excluding head as it is for special cases)
            if (bodyPart.isHarmful() && !bodyPart.isImmobilized() && bodyPart.getHealth() > 0 && !bodyPart.getName().equals("head")) {
                return true;
            }
            // Armed options
            if (bodyPart.canEquip() && bodyPart.hasEquippedItem() && bodyPart.getEquippedItem().isWeapon()
                    && !bodyPart.isImmobilized() && bodyPart.getHealth() > 0) {
                return true;
            }
        }
        
        return false;
    }

    public boolean canAttackMelee() {
        // Check attack options
        for (BodyPart bodyPart : this.getBodyParts()) {
            // Unarmed options (excluding head as it is for special cases)
            if (bodyPart.isHarmful() && !bodyPart.isImmobilized() && bodyPart.getHealth() > 0 && !bodyPart.getName().equals("head")) {
                return true;
            }
            // Armed options
            if (bodyPart.canEquip() && bodyPart.hasEquippedItem() && bodyPart.getEquippedItem().isWeapon()
                    && bodyPart.getEquippedItem().isMelee()
                    && !bodyPart.isImmobilized() && bodyPart.getHealth() > 0) {
                return true;
            }
        }
        
        return false;
    }

    public boolean canAttackRanged() {
        // Check attack options
        for (BodyPart bodyPart : this.getBodyParts()) {
            // Armed options
            if (bodyPart.canEquip() && bodyPart.hasEquippedItem() && bodyPart.getEquippedItem().isWeapon()
                    && bodyPart.getEquippedItem().isRanged()
                    && !bodyPart.isImmobilized() && bodyPart.getHealth() > 0) {
                return true;
            }
        }
        
        return false;
    }
    
    /*
     * Abilities Private
     */
    private void setStrengthByAbilities() {
        int ageWeight = 0;
        int weightWeight = 0;
        if (this.age > 50) {
            ageWeight = this.age - 50;
            ageWeight = (int)(ageWeight/5);
        }
        if (this.weight > 70) {
            weightWeight = this.weight - 70;
            weightWeight = (int)(weightWeight/5);
        }
        if (this.strength.getValue() - ageWeight > 0) {
            this.strength.setValue(this.strength.getValue() - ageWeight + weightWeight);
        } else {
            this.strength.setValue(this.strength.getValue() + weightWeight);
        }
    }

    private void setDexterityByAbilities() {
        int ageWeight = 0;
        if (this.age > 50) {
            ageWeight = this.age - 50;
            ageWeight = (int)(ageWeight/5);
        }
        if (this.dexterity.getValue() - ageWeight > 0) {
            this.dexterity.setValue(this.dexterity.getValue() - ageWeight);
        }
    }

    private void setConstitutionByAbilities() {
        int ageWeight = 0;
        if (this.age > 50) {
            ageWeight = this.age - 50;
            ageWeight = (int)(ageWeight/5);
        }
        if (this.constitution.getValue() - ageWeight > 0) {
            this.constitution.setValue(this.constitution.getValue() - ageWeight);
        }
    }

    private void setIntelligenceByAbilities() {
        int ageWeight = 0;
        if (this.age < 70) {
            ageWeight = (int)(ageWeight/10);
        } else if (this.age > 80) {
            ageWeight = -(int)(ageWeight/5);
        }
        if (this.intelligence.getValue() + ageWeight > 0) {
            this.intelligence.setValue(this.intelligence.getValue() + ageWeight);
        }
    }

    private void setWisdomByAbilities() {
        int ageWeight = 0;
        int intWeight = 0;
        intWeight = (int)(this.intelligence.getValue()/10);
        ageWeight = intWeight * (int)(ageWeight/5);
        this.wisdom.setValue(this.wisdom.getValue() + ageWeight);
    }

    private void setCharismaByAbilities() {
        int ageWeight = 0;
        int whWeight = 0;
        if (this.age > 60 && this.charisma.getValue() != 1) {
            ageWeight = this.age - 60;
            ageWeight = -(int)(ageWeight/10);
        }
        if (this.gender.equals("female") &&
                (Math.abs(this.height * 100) - this.weight) != 120) {
            whWeight = (int)(((this.height * 100) - this.weight) - 120);
            whWeight = Math.abs((int)(whWeight/10));
        } else if (this.gender.equals("male") &&
                (Math.abs(this.height * 100) - this.weight) < 100) {
            whWeight = (int)(((this.height * 100) - this.weight) - 100);
            whWeight = Math.abs((int)(whWeight/10));
        }
        if (this.charisma.getValue() + ageWeight + whWeight > 0) {
            this.charisma.setValue(this.charisma.getValue() + ageWeight - whWeight);
        }
    }
    
    /*
     * Abilities Public
     */
    public Ability getCharisma() {
        return charisma;
    }

    public void setCharisma(Ability charisma) {
        this.charisma = charisma;
    }

    public Ability getConstitution() {
        return constitution;
    }

    public void setConstitution(Ability constitution) {
        this.constitution = constitution;
    }

    public Ability getDexterity() {
        return dexterity;
    }

    public void setDexterity(Ability dexterity) {
        this.dexterity = dexterity;
    }

    public Ability getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Ability intelligence) {
        this.intelligence = intelligence;
    }

    public Ability getStrength() {
        return strength;
    }

    public void setStrength(Ability strength) {
        this.strength = strength;
    }

    public Ability getWisdom() {
        return wisdom;
    }

    public void setWisdom(Ability wisdom) {
        this.wisdom = wisdom;
    }
    
    protected void updateAbilitiesValues() {
        // Init cloth affections
        int clothStrAffect = 0;
        int clothDexAffect = 0;
        int clothConAffect = 0;
        int clothChaAffect = 0;
        
        // Loop through clothes and retrieve their affections
        for (BodyPart bodyPart : this.getBodyParts()) {
            for (Cloth cloth : bodyPart.getWearables()) {
                if (cloth.isAffectingStrength()) {
                    clothStrAffect += cloth.getStrengthAffect();
                }
                if (cloth.isAffectingDexterity()) {
                    clothDexAffect += cloth.getDexterityAffect();
                }
                if (cloth.isAffectingConstitution()) {
                    clothConAffect += cloth.getConstitutionAffect();
                }
                if (cloth.isAffectingCharisma()) {
                    clothChaAffect += cloth.getCharismaAffect();
                }
            }
        }
        
        // Calculate the new abilities values if there is a change
        if (clothStrAffect != 0) {
            this.getStrength().setValue(this.getStrength().getOriginalValue() + clothStrAffect);
        }
        if (clothDexAffect != 0) {
            this.getDexterity().setValue(this.getDexterity().getOriginalValue() + clothDexAffect);
        }
        if (clothConAffect != 0) {
            this.getConstitution().setValue(this.getConstitution().getOriginalValue() + clothConAffect);
        }
        if (clothChaAffect != 0) {
            this.getCharisma().setValue(this.getCharisma().getOriginalValue() + clothChaAffect);
        }
    }
    
    /*
     * Personality
     */
    public boolean isPlayer() {
        return player;
    }

    public void setPlayer(boolean player) {
        this.player = player;
    }
    
    public boolean isAgainst() {
        return against;
    }

    public void setAgainst(boolean against) {
        this.against = against;
    }
    
    /*
     * Actions
     */
    public Actions getActions() {
        return actions;
    }
    
}