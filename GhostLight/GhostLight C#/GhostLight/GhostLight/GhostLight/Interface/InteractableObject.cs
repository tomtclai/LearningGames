
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using GhostFinder.GhostEngine;

/**
 * Represents an object the players flashlight can interact with THis includes Ghosts, Pumpkins etc...
 * @author Michael Letter
 */
namespace GhostFinder.Interface{
    public class InteractableObject : Representative{

	    //nonStatic
        /// <summary>used to correlate IntravtableObjects to array positions in the Primitive set. For internal Use only. </summary>
	    internal int ID = 0;

        /// <summary> Current Object Types </summary>
	    public enum ObjectType {
	        GHOST, ANGRY, SPIDER, VAMPIRE, ZOMBIE, MUMMY, PUMPKIN, FRANKENSTEIN, CAT
	    };
        private static Random rand = new Random();
	    /// <summary>
        /// Will Create a new unrevealed InteractableObject of ObjectType.PUMPKIN with Default Values
	    /// </summary>
	    public InteractableObject() : base(){
		    setObjectType(ObjectType.PUMPKIN, true);
	    }
	    /// <summary>
        /// Will Create a new InteractableObject of ObjectType.PUMPKIN with Default Values
	    /// </summary>
        /// <param name="revealed">if TRUE, the InteractableObject will be revealed. else if FALSE, the InteractableObject will be hidden</param>
        public InteractableObject(bool revealed) : base(){
		    setRevealStatus(revealed);
	    }
	    /// <summary>
        /// Will Create a new unrevealed InteractableObject of the given ObjectType with Default Values
	    /// </summary>
        /// <param name="targetType">if not NULL will set this InteractableObject's ObjectType to match targetType</param>
	    public InteractableObject(ObjectType targetType) : base(){
		    setObjectType(targetType, true);
	    }
	    /// <summary>
        /// Will Create a new InteractableObject of the given ObjectType with Default Values
	    /// </summary>
        /// <param name="targetType">if not NULL will set this InteractableObject's ObjectType to match targetType </param>
        /// <param name="revealed">if TRUE, the InteractableObject will be revealed. else if FALSE, the InteractableObject will be hidden</param>
	    public InteractableObject(ObjectType targetType, bool revealed) : base(){
		    setObjectType(targetType, true);
		    setRevealStatus(revealed);
	    }
	    /// <summary>
        ///  Will Create a new unrevealed InteractableObject of the given ObjectType with Default Values and the specified Health
	    /// </summary>
        /// <param name="targetType">if not NULL will set this InteractableObject's ObjectType to match targetType </param>
        /// <param name="health">The current health of this enemy</param>
	    public InteractableObject(ObjectType targetType, int health) : base(){
		    setObjectType(targetType, true);
		    if(health > base.getMaximumHealth()){
			    setMaxHealth(health);
		    }
		    setCurrentHealth(health);
	    }
	    /// <summary>
        /// Will Create a new unrevealed InteractableObject of the given ObjectType with Default Values and the specified Health
	    /// </summary>
        /// <param name="targetType">if not NULL will set this InteractableObject's ObjectType to match targetType</param>
        /// <param name="maxHealth">The Maximum health this enemy can ever have</param>
        /// <param name="health">The current health of this enemy</param>
	    public InteractableObject(ObjectType targetType, int maxHealth, int health) : base(){
		    setObjectType(targetType, true);
		    setMaxHealth(maxHealth);
		    setCurrentHealth(health);
	    }
	    /// <summary>
        /// Will Create a new unrevealed InteractableObject of the given ObjectType with Default Values and the specified Health and score
	    /// </summary>
        /// <param name="targetType">if not NULL will set this InteractableObject's ObjectType to match targetType </param>
        /// <param name="maxHealth">The Maximum health this enemy can ever have</param>
        /// <param name="health">The current health of this enemy</param>
        /// <param name="score">The number of points this InteractableObject indicates it is worth</param>
	    public InteractableObject(ObjectType targetType, int maxHealth, int health, int score) : base(){
		    setObjectType(targetType, true);
		    setMaxHealth(maxHealth);
		    setCurrentHealth(health);
		    setScore(score);
	    }
	    /// <summary>
        /// Will Create a new InteractableObject of the given ObjectType with Default Values and the specified Health and score
	    /// </summary>
        /// <param name="targetType">if not NULL will set this InteractableObject's ObjectType to match targetType </param>
        /// <param name="maxHealth">The Maximum health this enemy can ever have</param>
        /// <param name="health">The current health of this enemy</param>
        /// <param name="score">The number of points this InteractableObject indicates it is worth</param>
        /// <param name="revealed">if TRUE, the InteractableObject will be revealed. else if FALSE, the InteractableObject will be hidden</param>
	    public InteractableObject(ObjectType targetType, int maxHealth, int health, int score, bool revealed) : base(){
		    setObjectType(targetType, true);
		    setMaxHealth(maxHealth);
		    setCurrentHealth(health);
		    setScore(score);
		    setRevealStatus(revealed);
	    }
	    /// <summary>
        /// Will Create a new InteractableObject with the specified state
	    /// </summary>
        /// <param name="targetType">if not NULL will set this InteractableObject's ObjectType to match targetType</param>
        /// <param name="infectedTimer">the number of turns, including this one, this enemy will be marked as infected</param>
        /// <param name="maxHealth">The Maximum health this enemy can ever have</param>
        /// <param name="health">The current health of this enemy</param>
        /// <param name="score">The number of points this InteractableObject indicates it is worth</param>
        /// <param name="revealed">if TRUE, the InteractableObject will be revealed. else if FALSE, the InteractableObject will be hidden</param>
        /// <param name="partialRevealed">if TRUE and this InteractableObject is not revealed, the InteractableObject will appear to be green if it is ObjectType is CAT or PUMPKIN, If its ObjectType is any other Type it will be drawn red. Otherwise if this is False or this InteractableObject is revealed, it will be appear as normal</param>
        /// <param name="invulnerable">if TRUE, this InteractableObject will not be removed if its currentHealth is zero, If FALSE, it will be removed when its currentHealth becomes zero</param>
        /// <param name="highlighted">if TRUE, this InteractableObject will appear on top off other enemies and will Sparcle. if FALSE, this InteractableObject will be appear as normal</param>
	    public InteractableObject(ObjectType targetType, int infectedTimer, int maxHealth, int health, int score, bool revealed, bool partialRevealed, bool invulnerable, bool highlighted) : base(){
		    setObjectType(targetType, true);
		    setInfectStatus(infectedTimer);
		    setMaxHealth(maxHealth);
		    setCurrentHealth(health);
		    setScore(score);
		    setRevealStatus(revealed);
		    setPartialRevealStatus(partialRevealed);
		    setHighlight(highlighted);
	    }
	    /// <summary>
        /// Will change what type of Object this InteractableObject is to the given type
	    /// </summary>
        /// <param name="type">the ObjectType this InteractableObject will be at the conclusion of this function</param>
	    /// <param name="setStateToDefualts">setStateToDefualts If true will set this objects health and score to the default heath and score of the given ObjectType If false will leave this objects health and score unchanged</param>
        public void setObjectType(ObjectType type, bool setStateToDefualts){
    	    base.setType(type);
    	    setScore(InteractableObject.getDefualtScore(type));
    	    if(setStateToDefualts){
    		    base.setScore(InteractableObject.getDefualtScore(type));
    		    base.setMaxHealth(InteractableObject.getDefualthealth(type));
    		    base.setCurrentHealth(base.getMaximumHealth());
    		    base.setVulnerability(InteractableObject.getDefualtInvulnerability(type));
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
        private static bool defualtInvulnerability = false; 
    
        //Enemy Defualts
        private static int enemyTypeCount = Enum.GetValues(typeof(ObjectType)).Length;
        private static int[] defualtEnemyMinHealth = new int[enemyTypeCount];
        private static int[] defualtEnemyMaxHealth = new int[enemyTypeCount];
        private static int[] defualtEnemyHealthDistribution = new int[enemyTypeCount];
        private static int[] defualtEnemyMinScore = new int[enemyTypeCount];
        private static int[] defualtEnemyMaxScore = new int[enemyTypeCount];
        private static int[] defualtEnemyScoreDistribution = new int[enemyTypeCount];
        private static bool[] defualtEnemyInvulnerability = new bool[enemyTypeCount];
    
    
        /// <summary>
        /// Will set the defualtHealth of all InteractableObjects to 3.
        /// Will set the defualtMinScore of all InteractableObjects to 20.
        /// Will set the defualtMaxScore of all InteractableObjects to 20.
        /// Will set the defualtDistribution of all InteractableObjects to 1.
        /// </summary>
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
        /// <summary>
        /// Will set the defualtHealth of all InteractableObjects of the given ObjectType to 3.
        /// Will set the defualtMinScore of all InteractableObjects of the given ObjectType to 20.
        /// Will set the defualtMaxScore of all InteractableObjects of the given ObjectType to 20.
        /// Will set the defualtDistribution of all InteractableObjects of the given ObjectType to 1.
        /// </summary>
        /// <param name="targetType"></param>
        public static void setDefualtsToDefualts(ObjectType targetType){
    	    if(targetType != null){
	    	    defualtEnemyMinHealth[(int)targetType] = defualtHealth;
			    defualtEnemyMaxHealth[(int)targetType] = defualtHealth;
                defualtEnemyHealthDistribution[(int)targetType] = defualtDistribution;
                defualtEnemyMinScore[(int)targetType] = defualtMinScore;
			    defualtEnemyMaxScore[(int)targetType] = defualtMaxScore;
			    defualtEnemyScoreDistribution[(int)targetType] = defualtDistribution;
			    defualtEnemyInvulnerability[(int)targetType] = defualtInvulnerability;
    	    }
        }
        /// <summary>
        /// Will Make it so by default all intractableObjects either invulnerable or vulnerable to damage when they are initially created.
        /// If an intractableObjects is Invulnerable it will not be destroyed when its health reaches zero. otherwise it will
        /// </summary>
        /// <param name="makeInvulnerable">if True, all InteractableObjects will be made invulnerable by default. If false all InteractableObjects will be made vulnerable by default.</param>
        public static void setDefualtInvulnerability(bool makeInvulnerable){
    	    for(int loop = 0; loop < enemyTypeCount; loop++){
    		    defualtEnemyInvulnerability[loop] = makeInvulnerable;
    	    }
        }
        /// <summary>
        /// Will Make it so by default target ObjectType intractableObjects either invulnerable or vulnerable to damage when they are initially created.
        /// If an intractableObjects is Invulnerable it will not be destroyed when its health reaches zero. otherwise it will
        /// </summary>
        /// <param name="makeInvulnerable">if True, target ObjectType InteractableObject will be made invulnerable by default. If false target ObjectType InteractableObject will be made vulnerable by default.</param>
        /// <param name="targetType"></param>
        public static void setDefualtInvulnerability(bool makeInvulnerable, ObjectType targetType){
    	    if(targetType != null){	
    		    defualtEnemyInvulnerability[(int)targetType] = makeInvulnerable;
    	    }
        }
        /// <summary>
        /// will return whether or not the InteractableObjects of the given ObjectType are invulnerable by default
        /// if an InteractableObject is invulnerable by default it will not be destroyed when its health reaches zero 
        /// </summary>
        /// <param name="targetType"></param>
        /// <returns>if true the InteractableObjects of the given ObjectType are invulnerable by default. If false it is not invulnerable.</returns>
        public static bool getDefualtInvulnerability(ObjectType targetType){
    	    if(targetType != null){
    		    return defualtEnemyInvulnerability[(int)targetType];
    	    }
    	    return false;
        }
        /// <summary>
        /// Will set the Default health of all InteractableObjects to the given amount
        /// </summary>
        /// <param name="newDefualtHealth">the health that will be set as the new defualt health. Note, this amount must be 0 or greater</param>
        /// <returns>if the operation was successful, otherwise returns false. in this case no changes are made</returns>
        public static bool setDefualtHealth(int newDefualtHealth){
    	    if(newDefualtHealth >= 0){
    		    for(int loop = 0; loop < enemyTypeCount; loop++){
        		    defualtEnemyMinHealth[loop] = newDefualtHealth;
        		    defualtEnemyMaxHealth[loop] = newDefualtHealth;
        	    }
                return true;
    	    }
    	    return false;
        }
        /// <summary>
        /// Will set the Default Maximum health of all InteractableObjects to the given amount
        /// </summary>
        /// <param name="newDefualtHealth">the Maximum health that will be set as the new defualt Maximum health. Note, this amount must be 0 or greater</param>
        /// <returns>true if the operation was successful, otherwise returns false. in this case no changes are made</returns>
        public static bool setDefualtMaxHealth(int newDefualtHealth){
    	    if(newDefualtHealth >= 0){
    		    for(int loop = 0; loop < enemyTypeCount; loop++){
        		    defualtEnemyMaxHealth[loop] = newDefualtHealth;
        	    }
                return true;
    	    }
    	    return false;
        }
        /// <summary>
        /// Will set the Default Minimum health of all InteractableObjects to the given amount
        /// </summary>
        /// <param name="newDefualtHealth">the Minimum health that will be set as the new defualt Minimum health. Note, this amount must be 0 or greater</param>
        /// <returns>true if the operation was successful, otherwise returns false. in this case no changes are made</returns>
        public static bool setDefualtMinHealth(int newDefualtHealth){
    	    if(newDefualtHealth >= 0){
    		    for(int loop = 0; loop < enemyTypeCount; loop++){
        		    defualtEnemyMinHealth[loop] = newDefualtHealth;
        	    }
                return true;
    	    }
    	    return false;
        }
        /// <summary>
        /// Will set the Default health Distribution of all InteractableObjects to the given amount
        /// </summary>
        /// <param name="newDefualtDistribution">the health Distribution that will be set as the new default health Distribution. Note, this amount must be 0 or greater </param>
        /// <returns>true if the operation was successful, otherwise returns false. in this case no changes are made</returns>
        public static bool setDefualtDistributionHealth(int newDefualtDistribution){
    	    if(newDefualtDistribution >= 0){
    		    for(int loop = 0; loop < enemyTypeCount; loop++){
        		    defualtEnemyHealthDistribution[loop] = defualtDistribution;
        	    }
                return true;
    	    }
    	    return false;
        }
        /// <summary>
        /// Will set the Default health of the given objectType to the given amount
        /// </summary>
        /// <param name="newDefualtHealth">the object type to be effected</param>
        /// <param name="targetType">the health that will be set as the new default health. Note, this amount must be 0 or greater </param>
        /// <returns>true if the operation was successful, otherwise returns false. in this case no changes are made</returns>
        public static bool setDefualtHealth(int newDefualtHealth, ObjectType targetType){
    	    if(newDefualtHealth >= 0){
    		    if(targetType != null){
    			    defualtEnemyMinHealth[(int)targetType] = newDefualtHealth;
        		    defualtEnemyMaxHealth[(int)targetType] = newDefualtHealth;
    		    }
                return true;
    	    }
    	    return false;
        }
        /// <summary>
        /// Will return the Default Maximum health of the given objectType
        /// </summary>
        /// <param name="targetType">the given objectType</param>
        /// <returns>the Default Maximum health of the given objectType</returns>
        public static int getDefualtMaxHealth(ObjectType targetType){
		    if(targetType != null){
			    return defualtEnemyMaxHealth[(int)targetType];
		    }
		    return -1;
        }
        /// <summary>
        /// Will return the Default Minimum health of the given objectType
        /// </summary>
        /// <param name="targetType">the given objectType </param>
        /// <returns>the Default health Minimum of the given objectType</returns>
        public static int getDefualtMinHealth(ObjectType targetType){
    	    if(targetType != null){
			    return defualtEnemyMinHealth[(int)targetType];
		    }
		    return -1;
        }
        /// <summary>
        /// Will return the Default health Distribution of the given objectType
        /// </summary>
        /// <param name="targetType">the given objectType </param>
        /// <returns>the Default health of Distribution the given objectType</returns>
        public static int getDefualtHealthDistribution(ObjectType targetType){
    	    if(targetType != null){
			    return defualtEnemyHealthDistribution[(int)targetType];
		    }
		    return -1;
        }
        /// <summary>
        /// Will Return the Default Health associated with the given ObjectType
        /// </summary>
        /// <param name="targetType">the given objectType</param>
        /// <returns>(((defualtMaxHealth - defualtMinHealth) / defualtHealth) * random * defualtHealthDistrobution) + defualtMinHealth</returns>
        public static int getDefualthealth(ObjectType targetType){
    	    if(targetType != null){
    		    return calculateScore(defualtEnemyHealthDistribution[(int)targetType], defualtEnemyMaxHealth[(int)targetType], defualtEnemyMinHealth[(int)targetType]);
    	    }
    	    return -1;
        }
        /// <summary>
        /// Will set the DefualtScore of all interactableObjects to the given amount
        /// </summary>
        /// <param name="newDefualtScore">the desired DefualtScore</param>
        public static void setDefualtScore(int newDefualtScore){
    	    for(int loop = 0; loop < enemyTypeCount; loop++){
    		    defualtEnemyMinScore[loop] = newDefualtScore;
    		    defualtEnemyMaxScore[loop] = newDefualtScore;
    	    }
        }
        /// <summary>
        /// Will set the DefualtMaxScore of all interactableObjects to the given amount
        /// Note if the defualtMinScore happens to be greater then the given DefualtMaxScore
        /// it will be treated as the DefualtMaxScore
        /// </summary>
        /// <param name="newDefualtMaxScore">the desired DefualtMaxScore</param>
        public static void setDefualtMaxScore(int newDefualtMaxScore){
    	    for(int loop = 0; loop < enemyTypeCount; loop++){
    		    defualtEnemyMaxScore[loop] = newDefualtMaxScore;
    	    }
        }
        /// <summary>
        /// Will set the DefualtMaxScore of all interactableObjects of the given ObjectType to the given amount
        /// Note if the defualtMinScore happens to be greater then the given DefualtMaxScore
        /// </summary>
        /// <param name="newDefualtMaxScore">targetType the object type to be effected</param>
        /// <param name="targetType">the health that will be set as the new default health. Note, this amount must be 0 or greater</param>
        public static void setDefualtMaxScore(int newDefualtMaxScore, ObjectType targetType){
		    if(targetType != null){
			    defualtEnemyMaxScore[(int)targetType] = newDefualtMaxScore;
		    }
        }
        /// <summary>
        /// Will get the DefualtMaxScore of the interactableObject of the given ObjectType
        /// </summary>
        /// <param name="targetType">the object type to be effected</param>
        /// <returns>returns the DefualtMaxScore of the interactableObject of the given ObjectType</returns>
        public static int getDefualtMaxScore(ObjectType targetType){
    	    if(targetType != null){
			    return defualtEnemyMaxScore[(int)targetType];
		    }
		    return 0;
        }
        /// <summary>
        /// Will set the DefualtMinScore of all interactableObjects to the given amount
        /// Note if the defualtMaxScore happens to be greater then the given DefualtMinScore
        /// it will be treated as the DefualtMinScore
        /// </summary>
        /// <param name="newDefualtMinScore">the desired DefualtMaxScore</param>
        public static void setDefualtMinScore(int newDefualtMinScore){
    	    for(int loop = 0; loop < enemyTypeCount; loop++){
    		    defualtEnemyMinScore[loop] = newDefualtMinScore;
    	    }
        }
        /// <summary>
        /// Will set the DefualtMinScore of all interactableObjects of the given ObjectType to the given amount
        /// Note if the defualtMaxScore happens to be greater then the given DefualtMinScore
        /// it will be treated as the DefualtMinScore
        /// </summary>
        /// <param name="newDefualtMinScore">the object type to be effected</param>
        /// <param name="targetType">the desired DefualtMaxScore</param>
        public static void setDefualtMinScore(int newDefualtMinScore, ObjectType targetType){
    	    if(targetType != null){
    		    defualtEnemyMinScore[(int)targetType] = newDefualtMinScore;
    	    }
        }
        /// <summary>
        /// Will get the DefualtMinScore of the interactableObjects of the given ObjectType
        /// </summary>
        /// <param name="targetType">the object type to be effected</param>
        /// <returns>the DefualtMinScore of the interactableObjects of the given ObjectType</returns>
        public static int getDefualtMinScore(ObjectType targetType){
    	    if(targetType != null){
    		    return defualtEnemyMinScore[(int)targetType];
    	    }
    	    return 0;
        }
        /// <summary>
        /// Will set the defualtDistribution of all interactableObjects to the given amount
        /// This comes into play when the difference between the defualtMinScore and the defualtMaxScore is greater
        /// then the defualtDistribution. In this case when a random score generarated between The defualtMaxScore
        /// and the defualtMinScore will be such that (generaratedScore - defualtMinScore)%defualtDistribution = 0
        /// </summary>
        /// <param name="newdefualtDistribution">the desired defualtDistribution</param>
        public static void setDefualtScoreInterval(int newdefualtDistribution){
    	    for(int loop = 0; loop < enemyTypeCount; loop++){
    		    defualtEnemyScoreDistribution[loop] = newdefualtDistribution;
    	    }
        }
        /// <summary>
        /// Will set the defualtDistribution of all interactableObjects of the given ObjectType to the given amount
        /// This comes into play when the difference between the defualtMinScore and the defualtMaxScore is greater
        /// then the defualtDistribution. In this case when a random score generarated between The defualtMaxScore
        /// and the defualtMinScore will be such that (generaratedScore - defualtMinScore)%defualtDistribution = 0
        /// </summary>
        /// <param name="newdefualtDistribution">the InteractableObject type to be effected</param>
        /// <param name="targetType">the desired defualtDistribution</param>
        public static void setDefualtScoreInterval(int newdefualtDistribution, ObjectType targetType){
    	    if(targetType != null){
    		    defualtEnemyScoreDistribution[(int)targetType] = newdefualtDistribution;
    	    }
        }
        /// <summary>
        /// Will get the defualtDistribution of the interactableObjects of the given ObjectType.
        /// This comes into play when the difference between the defualtMinScore and the defualtMaxScore is greater
        /// then the defualtDistribution. In this case when a random score generarated between The defualtMaxScore
        /// and the defualtMinScore will be such that (generaratedScore - defualtMinScore)%defualtDistribution = 0
        /// </summary>
        /// <param name="targetType">the InteractableObject type to be effected</param>
        /// <returns>the defualtDistribution of the interactableObjects of the given ObjectType</returns>
        public static int getdefualtScoreInterval(ObjectType targetType){
    	    if(targetType != null){
    		    return defualtEnemyScoreDistribution[(int)targetType];
    	    }
    	    return 0;
        }
        /// <summary>
        /// Will Return the Default score associated with the given ObjectType
        /// </summary>
        /// <param name="targetType">the given objectType</param>
        /// <returns>(((defualtMaxScore - defualtMinScore) / defualtDistribution) * random * defualtScoreDistrobution) + defualtMinScore</returns>
        public static int getDefualtScore(ObjectType targetType){
    	    if(targetType != null){
    		    return calculateScore(defualtEnemyScoreDistribution[(int)targetType], defualtEnemyMaxScore[(int)targetType], defualtEnemyMinScore[(int)targetType]);
    	    }
    	    return 0;
        }
        /// <summary>
        /// Caclulates a random value between the given min and max with the given distrobution
        /// </summary>
        /// <param name="distrobution"></param>
        /// <param name="max"></param>
        /// <param name="min"></param>
        /// <returns></returns>
        private static int calculateScore(int distrobution, int max, int min){
    	    if(max == min){
    		    return  max;
    	    }
    	    else if(distrobution != 0 && (int)((max - min)/distrobution) != 0){
                return (int)(((double)(max - min) / (double)distrobution) * rand.NextDouble()) * distrobution + min;
    	    }
    	    else{
                return (int)((((double)(max - min)) * rand.NextDouble()) + min);
    	    }
        }
    }
}
