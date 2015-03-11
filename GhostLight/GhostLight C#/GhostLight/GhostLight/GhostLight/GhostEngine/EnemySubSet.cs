using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CustomWindower.CoreEngine;

/**
 * @author Michael Letter
 */
namespace GhostFinder.GhostEngine{
    public class EnemySubSet {
	    private EnemySubSetNode firstNode = new EnemySubSetNode();
	
	    /**
	     * Will add a give baseEnemy to this set
	     * Linked List. nothing fancy here
	     * @param newEnemy
	     */
	    public EnemySubSetNode addEnemy(BaseEnemy newEnemy, bool informEnemy){
		    if(newEnemy != null){
			    EnemySubSetNode newNode = new EnemySubSetNode();
			    newNode.data = newEnemy;
			    if(informEnemy){
				    newEnemy.hostNode = newNode;
			    }
			    firstNode.addNode(newNode);
			    return newNode;
		    }
		    return null;
	    }
	    /**
	     * Will call the update function on all enemies in the set
	     * @param setAnimationTime
	     */
	    public void updateEnemiesInSet(int setAnimationTime){
		    firstNode.updateAll(setAnimationTime);
	    }
	    /**
	     * Will remove all EnemySubSetNodes from the set
	     * @param destroyData
	     */
	    public void removeAllEnemies(bool destroyData){
		    EnemySubSetNode target = firstNode.getNextNode();
		    while(target != null){
			    EnemySubSetNode nextTarget = target.getNextNode();
			    if(target.data != null && destroyData){
				    target.data.removeThis();
			    }
			    target.removeNode();
			    target = nextTarget;
		    }
	    }
	    /**
	     * Will attempt to find the given BaseEnemy in the set and if found will return the EnemySubSetNode that contains it.
	     * Otherwise will return null
	     * @param target
	     * @return
	     */
	    public EnemySubSetNode findEnemy(BaseEnemy target){
		    EnemySubSetNode retVal = null;
		    if(target != null){
			    EnemySubSetNode currentNode = firstNode.getNextNode();
			    while(currentNode != null && retVal == null){
				    if(currentNode.data == target){
					    retVal = currentNode;
				    }
				    currentNode = currentNode.getNextNode();
			    }
		    }
		    return retVal;
	    }
	    /**
	     * Will draw all of the nodes in the list
	     */
	    public void drawAll(Camera drawLocation){
		    firstNode.drawAll(drawLocation);
	    }
    }
}