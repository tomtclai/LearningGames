package GhostLight.Interface;

import java.util.HashMap;
import java.util.Map;

import GhostLight.GhostEngine.Representative;
import GhostLight.Interface.InteractableObject.ObjectType;

/**
 * Represents an object the players flashlight can interact with THis includes Ghosts, Pumpkins etc...
 * @author Michael Letter
 */
public class InteractableObject extends Representative{

	//nonStatic
	/** used to correlate IntravtableObjects to array positions in the Primitive set */
	int ID = 0;
	
	/** Current Object Types */
	public enum ObjectType {
	    GHOST, ANGRY, SPIDER, VAMPIRE, ZOMBIE, MUMMY, PUMPKIN, FRANKENSTEIN, CAT
	};
	/**
	 * Will Create a new unrevealed InteractableObject of ObjectType.PUMPKIN with Default Values
	 */
	public InteractableObject(){
		super();
		setObjectType(ObjectType.PUMPKIN, true);
		initializeMap();
	}
	/**
	 * Will Create a new InteractableObject of ObjectType.PUMPKIN with Default Values
	 * @param reavealed if TRUE, the InteractableObject will be revealed. else if FALSE, the InteractableObject will be hidden
	 */
	public InteractableObject(boolean revealed){
		super();
		setRevealStatus(revealed);
		initializeMap();
	}
	/**
	 * Will Create a new unrevealed InteractableObject of the given ObjectType with Default Values
	 * @param targetType if not NULL will set this InteractableObject's ObjectType to match targetType 
	 */
	public InteractableObject(ObjectType targetType){
		super();
		setObjectType(targetType, true);
		initializeMap();
	}
	/**
	 * Will Create a new InteractableObject of the given ObjectType with Default Values
	 * @param targetType if not NULL will set this InteractableObject's ObjectType to match targetType 
	 * @param reavealed if TRUE, the InteractableObject will be revealed. else if FALSE, the InteractableObject will be hidden
	 */
	public InteractableObject(ObjectType targetType, boolean revealed){
		super();
		setObjectType(targetType, true);
		setRevealStatus(revealed);
		initializeMap();
	}
	/**
	 * Will Create a new unrevealed InteractableObject of the given ObjectType with Default Values and the specified Health
	 * @param targetType if not NULL will set this InteractableObject's ObjectType to match targetType 
	 * @param health The current health of this enemy
	 */
	public InteractableObject(ObjectType targetType, int health){
		super();
		setObjectType(targetType, true);
		if(health > super.getMaximumHealth()){
			setMaxHealth(health);
		}
		setCurrentHealth(health);
		initializeMap();
	}
	/**
	 * Will Create a new unrevealed InteractableObject of the given ObjectType with Default Values and the specified Health
	 * @param targetType if not NULL will set this InteractableObject's ObjectType to match targetType 
	 * @param maxHealth The Maximum health this enemy can ever have
	 * @param health The current health of this enemy
	 */
	public InteractableObject(ObjectType targetType, int maxHealth, int health){
		super();
		setObjectType(targetType, true);
		setMaxHealth(maxHealth);
		setCurrentHealth(health);
		initializeMap();
	}
	/**
	 * Will Create a new unrevealed InteractableObject of the given ObjectType with Default Values and the specified Health and score
	 * @param targetType if not NULL will set this InteractableObject's ObjectType to match targetType 
	 * @param maxHealth The Maximum health this enemy can ever have
	 * @param health The current health of this enemy
	 * @param score The number of points this InteractableObject indicates it is worth
	 */
	public InteractableObject(ObjectType targetType, int maxHealth, int health, int score){
		super();
		setObjectType(targetType, true);
		setMaxHealth(maxHealth);
		setCurrentHealth(health);
		setScore(score);
		initializeMap();
	}
	/**
	 * Will Create a new InteractableObject of the given ObjectType with Default Values and the specified Health and score
	 * @param targetType if not NULL will set this InteractableObject's ObjectType to match targetType 
	 * @param maxHealth The Maximum health this enemy can ever have
	 * @param health The current health of this enemy
	 * @param score The number of points this InteractableObject indicates it is worth
	 * @param reavealed if TRUE, the InteractableObject will be revealed. else if FALSE, the InteractableObject will be hidden
	 */
	public InteractableObject(ObjectType targetType, int maxHealth, int health, int score, boolean revealed){
		super();
		setObjectType(targetType, true);
		setMaxHealth(maxHealth);
		setCurrentHealth(health);
		setScore(score);
		setRevealStatus(revealed);
		initializeMap();
	}
	/**
	 * Will Create a new InteractableObject with the specified state
	 * @param targetType if not NULL will set this InteractableObject's ObjectType to match targetType 
	 * @param infectedTimer the number of turns, including this one, this enemy will be marked as infected
	 * @param maxHealth The Maximum health this enemy can ever have
	 * @param health The current health of this enemy
	 * @param score The number of points this InteractableObject indicates it is worth
	 * @param reavealed if TRUE, the InteractableObject will be revealed. else if FALSE, the InteractableObject will be hidden
	 * @param partialRevealed if TRUE and this InteractableObject is not revealed, the InteractableObject will appear to be green if it is ObjectType is CAT or PUMPKIN, If its ObjectType is any other Type it will be drawn red. Otherwise if this is False or this InteractableObject is revealed, it will be appear as normal
	 * @param invulnerable if TRUE, this InteractableObject will not be removed if its currentHealth is zero, If FALSE, it will be removed when its currentHealth becomes zero
	 * @param highlighted if TRUE, this InteractableObject will appear on top off other enemies and will Sparcle. if FALSE, this InteractableObject will be appear as normal
	 */
	public InteractableObject(ObjectType targetType, int infectedTimer, int maxHealth, int health, int score, boolean revealed, boolean partialRevealed, boolean invulnerable, boolean highlighted){
		super();
		setObjectType(targetType, true);
		setInfectStatus(infectedTimer);
		setMaxHealth(maxHealth);
		setCurrentHealth(health);
		setScore(score);
		setRevealStatus(revealed);
		setPartialRevealStatus(partialRevealed);
		setHighlight(highlighted);
		initializeMap();
	}
	/**
	 * Will change what type of Object this InteractableObject is to the given type
	 * @param type the ObjectType this InteractableObject will be at the conclusion of this function
	 * @param setStateToDefualts If true will set this objects health and score to the default heath and score of the given ObjectType.
	 * 		  If false will leave this objects health and score unchanged
	 */
    public void setObjectType(ObjectType type, boolean setStateToDefualts){
    	super.setType(type);
    	setScore(InteractableObject.getDefualtScore(type));
    	if(setStateToDefualts){
    		super.setScore(InteractableObject.getDefualtScore(type));
    		super.setMaxHealth(InteractableObject.getDefualthealth(type));
    		super.setCurrentHealth(super.getMaximumHealth());
    		super.setVulnerability(InteractableObject.getDefualtInvulnerability(type));
    	}
    	
    }
    //Static
    //Defualt Defualts
    //Default health = health
    //Defualt score = (((defualtMaxScore - defualtMinScore) / defualtDistribution) * random * defualtDScoreDistrobution) + defualtMinScore
    private static int defualtHealth = 3;
    private static int defualtMinScore = 20;
    private static int defualtMaxScore = 20;
    private static int defualtDistribution = 1;
    private static boolean defualtInvulnerability = false; 
    
    //Enemy Defualts
    private static int enemyTypeCount = InteractableObject.ObjectType.values().length;
    private static int[] defualtEnemyMinHealth = new int[enemyTypeCount];
    private static int[] defualtEnemyMaxHealth = new int[enemyTypeCount];
    private static int[] defualtEnemyHealthDistribution = new int[enemyTypeCount];
    private static int[] defualtEnemyMinScore = new int[enemyTypeCount];
    private static int[] defualtEnemyMaxScore = new int[enemyTypeCount];
    private static int[] defualtEnemyScoreDistribution = new int[enemyTypeCount];
    private static boolean[] defualtEnemyInvulnerability = new boolean[enemyTypeCount];
    private static Map[] defaultOptions;
    
    /**
     * Creates an empty hashmap for each default option type of enemies.
     * 
     * Only created if default options not initialized yet.
     * 
     * Should be called in all constructors.
     */
    private static void initializeMap()
    {
    	if(defaultOptions == null)
    	{
    		defaultOptions = new HashMap[enemyTypeCount]; 
    		for(int i = 0; i < enemyTypeCount; i++)
    		{
    			defaultOptions[i] = new HashMap();
    		}
    	}
    }
    
    /**
     * Will set the defualtHealth of all InteractableObjects to 3.
     * Will set the defualtMinScore of all InteractableObjects to 20.
     * Will set the defualtMaxScore of all InteractableObjects to 20.
     * Will set the defualtDistribution of all InteractableObjects to 1.
     */
    public static void setDefualtsToDefualts(){
    	
    	for(int loop = 0; loop < enemyTypeCount; loop++){
    		defualtEnemyMinHealth[loop] = defualtHealth;
    		defualtEnemyMaxHealth[loop] = defualtHealth;
    		defualtEnemyHealthDistribution[loop] = defualtDistribution;
    		defualtEnemyMinScore[loop] = defualtMinScore;
    		defualtEnemyMaxScore[loop] = defualtMaxScore;
    		defualtEnemyScoreDistribution[loop] = defualtDistribution;
    		defualtEnemyInvulnerability[loop] = defualtInvulnerability;
    	}
    }
    /**
     * Will set the defualtHealth of all InteractableObjects of the given ObjectType to 3.
     * Will set the defualtMinScore of all InteractableObjects of the given ObjectType to 20.
     * Will set the defualtMaxScore of all InteractableObjects of the given ObjectType to 20.
     * Will set the defualtDistribution of all InteractableObjects of the given ObjectType to 1.
     */
    public static void setDefualtsToDefualts(ObjectType targetType){
    	if(targetType != null){
	    	defualtEnemyMinHealth[targetType.ordinal()] = defualtHealth;
			defualtEnemyMaxHealth[targetType.ordinal()] = defualtHealth;
			defualtEnemyHealthDistribution[targetType.ordinal()] = defualtDistribution;
			defualtEnemyMinScore[targetType.ordinal()] = defualtMinScore;
			defualtEnemyMaxScore[targetType.ordinal()] = defualtMaxScore;
			defualtEnemyScoreDistribution[targetType.ordinal()] = defualtDistribution;
			defualtEnemyInvulnerability[targetType.ordinal()] = defualtInvulnerability;
    	}
    }
    /**
     * Will Make it so by default all intractableObjects either invulnerable or vulnerable to damage when they are initially created.
     * If an intractableObjects is Invulnerable it will not be destroyed when its health reaches zero. otherwise it will
     * @param makeInvulnerable if True, all InteractableObjects will be made invulnerable by default.
     * 		 If false all InteractableObjects will be made vulnerable by default.
     */
    public static void setDefualtInvulnerability(boolean makeInvulnerable){
    	for(int loop = 0; loop < enemyTypeCount; loop++){
    		defualtEnemyInvulnerability[loop] = makeInvulnerable;
    	}
    }
    /**
     * Will Make it so by default target ObjectType intractableObjects either invulnerable or vulnerable to damage when they are initially created.
     * If an intractableObjects is Invulnerable it will not be destroyed when its health reaches zero. otherwise it will
     * @param makeInvulnerable if True, target ObjectType InteractableObject will be made invulnerable by default.
     * 		 If false target ObjectType InteractableObject will be made vulnerable by default.
     */
    public static void setDefualtInvulnerability(boolean makeInvulnerable, ObjectType targetType){
    	if(targetType != null){	
    		defualtEnemyInvulnerability[targetType.ordinal()] = makeInvulnerable;
    	}
    }
    /**
     * will return whether or not the InteractableObjects of the given ObjectType are invulnerable by default
     * if an InteractableObject is invulnerable by default it will not be destroyed when its health reaches zero 
     * @return if true the InteractableObjects of the given ObjectType are invulnerable by default. If false it is not invulnerable.
     */
    public static boolean getDefualtInvulnerability(ObjectType targetType){
    	if(targetType != null){
    		return defualtEnemyInvulnerability[targetType.ordinal()];
    	}
    	return false;
    }
    /**
     * Will set the Default health of all InteractableObjects to the given amount
     * @param newDefualtHealth the health that will be set as the new defualt health. Note, this amount must be 0 or greater 
     * @return returns true if the operation was successful, otherwise returns false. in this case no changes are made
     */
    public static boolean setDefualtHealth(int newDefualtHealth){
    	if(newDefualtHealth >= 0){
    		for(int loop = 0; loop < enemyTypeCount; loop++){
        		defualtEnemyMinHealth[loop] = newDefualtHealth;
        		defualtEnemyMaxHealth[loop] = newDefualtHealth;
        	}
            return true;
    	}
    	return false;
    }
    /**
     * Will set the Default Maximum health of all InteractableObjects to the given amount
     * @param newDefualtHealth the Maximum health that will be set as the new defualt Maximum health. Note, this amount must be 0 or greater 
     * @return returns true if the operation was successful, otherwise returns false. in this case no changes are made
     */
    public static boolean setDefualtMaxHealth(int newDefualtHealth){
    	if(newDefualtHealth >= 0){
    		for(int loop = 0; loop < enemyTypeCount; loop++){
        		defualtEnemyMaxHealth[loop] = newDefualtHealth;
        	}
            return true;
    	}
    	return false;
    }
    /**
     * Will set the Default Minimum health of all InteractableObjects to the given amount
     * @param newDefualtHealth the Minimum health that will be set as the new defualt Minimum health. Note, this amount must be 0 or greater 
     * @return returns true if the operation was successful, otherwise returns false. in this case no changes are made
     */
    public static boolean setDefualtMinHealth(int newDefualtHealth){
    	if(newDefualtHealth >= 0){
    		for(int loop = 0; loop < enemyTypeCount; loop++){
        		defualtEnemyMinHealth[loop] = newDefualtHealth;
        	}
            return true;
    	}
    	return false;
    }
    /**
     * Will set the Default health Distribution of all InteractableObjects to the given amount
     * @param newDefualtDistribution the health Distribution that will be set as the new default health Distribution. Note, this amount must be 0 or greater 
     * @return returns true if the operation was successful, otherwise returns false. in this case no changes are made
     */
    public static boolean setDefualtDistributionHealth(int newDefualtDistribution){
    	if(newDefualtDistribution >= 0){
    		for(int loop = 0; loop < enemyTypeCount; loop++){
        		defualtEnemyHealthDistribution[loop] = defualtDistribution;
        	}
            return true;
    	}
    	return false;
    }
    /**
     * Will set the Default health of the given objectType to the given amount
     * @param targetType the object type to be effected
     * @param newDefualtHealth the health that will be set as the new default health. Note, this amount must be 0 or greater 
     * @return returns true if the operation was successful, otherwise returns false. in this case no changes are made
     */
    public static boolean setDefualtHealth(int newDefualtHealth, ObjectType targetType){
    	if(newDefualtHealth >= 0){
    		if(targetType != null){
    			defualtEnemyMinHealth[targetType.ordinal()] = newDefualtHealth;
        		defualtEnemyMaxHealth[targetType.ordinal()] = newDefualtHealth;
    		}
            return true;
    	}
    	return false;
    }
    /**
     * Will return the Default Maximum health of the given objectType
     * @param targetType the given objectType 
     * @return the Default Maximum health of the given objectType
     */
    public static int getDefualtMaxHealth(ObjectType targetType){
		if(targetType != null){
			return defualtEnemyMaxHealth[targetType.ordinal()];
		}
		return -1;
    }
    /**
     * Will return the Default Minimum health of the given objectType
     * @param targetType the given objectType 
     * @return the Default health Minimum of the given objectType
     */
    public static int getDefualtMinHealth(ObjectType targetType){
    	if(targetType != null){
			return defualtEnemyMinHealth[targetType.ordinal()];
		}
		return -1;
    }
    /**
     * Will return the Default health Distribution of the given objectType
     * @param targetType the given objectType 
     * @return the Default health of Distribution the given objectType
     */
    public static int getDefualtHealthDistribution(ObjectType targetType){
    	if(targetType != null){
			return defualtEnemyHealthDistribution[targetType.ordinal()];
		}
		return -1;
    }
    /**
     * Will Return the Default Health associated with the given ObjectType
     * @param the given objectType
     * @return (((defualtMaxHealth - defualtMinHealth) / defualtHealth) * random * defualtHealthDistrobution) + defualtMinHealth
     */
    public static int getDefualthealth(ObjectType targetType){
    	if(targetType != null){
    		return calculateScore(defualtEnemyHealthDistribution[targetType.ordinal()], defualtEnemyMaxHealth[targetType.ordinal()], defualtEnemyMinHealth[targetType.ordinal()]);
    	}
    	return -1;
    }
    /**
     * Will set the DefualtScore of all interactableObjects to the given amount
     * @param newDefualtScore the desired DefualtScore
     */
    public static void setDefualtScore(int newDefualtScore){
    	for(int loop = 0; loop < enemyTypeCount; loop++){
    		defualtEnemyMinScore[loop] = newDefualtScore;
    		defualtEnemyMaxScore[loop] = newDefualtScore;
    	}
    }
    /**
     * Will set the DefualtMaxScore of all interactableObjects to the given amount
     * Note if the defualtMinScore happens to be greater then the given DefualtMaxScore
     * it will be treated as the DefualtMaxScore
     * @param newDefualtMaxScore the desired DefualtMaxScore
     */
    public static void setDefualtMaxScore(int newDefualtMaxScore){
    	for(int loop = 0; loop < enemyTypeCount; loop++){
    		defualtEnemyMaxScore[loop] = newDefualtMaxScore;
    	}
    }
    /**
     * Will set the DefualtMaxScore of all interactableObjects of the given ObjectType to the given amount
     * Note if the defualtMinScore happens to be greater then the given DefualtMaxScore
     * @param targetType the object type to be effected
     * @param newDefualtMaxScore the health that will be set as the new default health. Note, this amount must be 0 or greater 
     * @return returns true if the operation was successful, otherwise returns false. in this case no changes are made
     */
    public static void setDefualtMaxScore(int newDefualtMaxScore, ObjectType targetType){
		if(targetType != null){
			defualtEnemyMaxScore[targetType.ordinal()] = newDefualtMaxScore;
		}
    }
    /**
     * Will get the DefualtMaxScore of the interactableObject of the given ObjectType
     * @param targetType the object type to be effected
     * @return returns the DefualtMaxScore of the interactableObject of the given ObjectType
     */
    public static int getDefualtMaxScore(ObjectType targetType){
    	if(targetType != null){
			return defualtEnemyMaxScore[targetType.ordinal()];
		}
		return 0;
    }
    /**
     * Will set the DefualtMinScore of all interactableObjects to the given amount
     * Note if the defualtMaxScore happens to be greater then the given DefualtMinScore
     * it will be treated as the DefualtMinScore
     * @param setDefualtMinScore the desired DefualtMaxScore
     */
    public static void setDefualtMinScore(int newDefualtMinScore){
    	for(int loop = 0; loop < enemyTypeCount; loop++){
    		defualtEnemyMinScore[loop] = newDefualtMinScore;
    	}
    }
    /**
     * Will set the DefualtMinScore of all interactableObjects of the given ObjectType to the given amount
     * Note if the defualtMaxScore happens to be greater then the given DefualtMinScore
     * it will be treated as the DefualtMinScore
     * @param targetType the object type to be effected
     * @param setDefualtMinScore the desired DefualtMaxScore
     */
    public static void setDefualtMinScore(int newDefualtMinScore, ObjectType targetType){
    	if(targetType != null){
    		defualtEnemyMinScore[targetType.ordinal()] = newDefualtMinScore;
    	}
    }
    /**
     * Will get the DefualtMinScore of the interactableObjects of the given ObjectType
     * @param targetType the object type to be effected
     * @return the DefualtMinScore of the interactableObjects of the given ObjectType
     */
    public static int getDefualtMinScore(ObjectType targetType){
    	if(targetType != null){
    		return defualtEnemyMinScore[targetType.ordinal()];
    	}
    	return 0;
    }
    /**
     * Will set the defualtDistribution of all interactableObjects to the given amount
     * This comes into play when the difference between the defualtMinScore and the defualtMaxScore is greater
     * then the defualtDistribution. In this case when a random score generarated between The defualtMaxScore
     * and the defualtMinScore will be such that (generaratedScore - defualtMinScore)%defualtDistribution = 0
     * @param newdefualtDistribution the desired defualtDistribution
     */
    public static void setDefualtScoreInterval(int newdefualtDistribution){
    	for(int loop = 0; loop < enemyTypeCount; loop++){
    		defualtEnemyScoreDistribution[loop] = newdefualtDistribution;
    	}
    }
    /**
     * Will set the defualtDistribution of all interactableObjects of the given ObjectType to the given amount
     * This comes into play when the difference between the defualtMinScore and the defualtMaxScore is greater
     * then the defualtDistribution. In this case when a random score generarated between The defualtMaxScore
     * and the defualtMinScore will be such that (generaratedScore - defualtMinScore)%defualtDistribution = 0
     * @param ObjectType the InteractableObject type to be effected
     * @param newdefualtDistribution the desired defualtDistribution
     */
    public static void setDefualtScoreInterval(int newdefualtDistribution, ObjectType targetType){
    	if(targetType != null){
    		defualtEnemyScoreDistribution[targetType.ordinal()] = newdefualtDistribution;
    	}
    }
    /**
     * Will get the defualtDistribution of the interactableObjects of the given ObjectType.
     * This comes into play when the difference between the defualtMinScore and the defualtMaxScore is greater
     * then the defualtDistribution. In this case when a random score generarated between The defualtMaxScore
     * and the defualtMinScore will be such that (generaratedScore - defualtMinScore)%defualtDistribution = 0
     * @param targetType the InteractableObject type to be effected
     * &return the defualtDistribution of the interactableObjects of the given ObjectType
     */
    public static int getdefualtScoreInterval(ObjectType targetType){
    	if(targetType != null){
    		return defualtEnemyScoreDistribution[targetType.ordinal()];
    	}
    	return 0;
    }
    /**
     * Will Return the Default score associated with the given ObjectType
     * @param the given objectType
     * @return (((defualtMaxScore - defualtMinScore) / defualtDistribution) * random * defualtScoreDistrobution) + defualtMinScore
     */
    public static int getDefualtScore(ObjectType targetType){
    	if(targetType != null){
    		return calculateScore(defualtEnemyScoreDistribution[targetType.ordinal()], defualtEnemyMaxScore[targetType.ordinal()], defualtEnemyMinScore[targetType.ordinal()]);
    	}
    	return 0;
    }
    private static int calculateScore(int distrobution, int max, int min){
    	if(max == min){
    		return  max;
    	}
    	else if(distrobution != 0 && (int)((max - min)/distrobution) != 0){
    		return (int)(((double)(max - min) / (double)distrobution) * Math.random()) * distrobution + min;
    	}
    	else{
    		return (int)((((double)(max - min)) * Math.random()) + min);
    	}
    }
    /**
     * Adds additional options to objects
     * @param option	The option in string format.
     * @param value		The value of that option.
     * @param targetType The targeted object
     */
	@SuppressWarnings("unchecked")
	public static void setOption(String option, int value, ObjectType targetType) 
	{
		initializeMap();
		defaultOptions[targetType.ordinal()].put(option, value);
	}
	/**
	 * Will return the value of the option. Returns null if does not exist
	 * @param option The option.
	 * @param targetType The selected type.
	 * @return Value of option in Object static type.
	 */
	public static Object getOption(String option, ObjectType targetType)
	{
		return defaultOptions[targetType.ordinal()].get(option);
	}
}
