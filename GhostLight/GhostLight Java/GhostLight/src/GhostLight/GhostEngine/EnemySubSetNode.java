package GhostLight.GhostEngine;
/**
 * @author Michael Letter
 */
public class EnemySubSetNode {
	
	/** stores the last node in the list */
	private EnemySubSetNode previousNode = null;
	/** stores the next node in the list */
	private EnemySubSetNode nextNode = null;
	/** stores the stored base Enemy */
	public BaseEnemy data = null;
	/** Stores previously removed nodes for reuse */
	
	/**
	 * Will remove this node from the list and ling the node before and after this node togther
	 */
	public void removeNode(){
		//removing this node
		if(nextNode != null){
			nextNode.previousNode = previousNode;
		}
		if(previousNode != null){
			previousNode.nextNode = nextNode;
		}
		previousNode = null;
		nextNode = null;
	}
	/**
	 * Will add the given EnemySubSetNode to the list directly after this one
	 */
	public void addNode(EnemySubSetNode newNode){
		if(newNode != null){
			newNode.previousNode = this;
			newNode.nextNode = nextNode;
			if(nextNode != null){
				nextNode.previousNode = newNode;
			}
			nextNode = newNode;
		}
	}
	/**
	 * Will return the previous node in the list
	 * @return the previous node in the list
	 */
	public EnemySubSetNode getLastNode(){
		return previousNode;
	}
	/**
	 * Will return the next node in the list
	 * @return the next node in the list
	 */
	public EnemySubSetNode getNextNode(){
		return nextNode;
	}
	/**
	 * Will update this nodes BaseEnemy and all BaseEnemys contained in nodes after this one
	 */
	public void updateAll(int setAnimationTime){
		if(nextNode != null){
			nextNode.updateAll(setAnimationTime);
		}
		if(data != null){
			data.update(setAnimationTime);
		}
	}
	/**
	 * Will draw the data of this node and the data of all subsequent nodes
	 */
	public void drawAll(){
		if(data != null){
			data.draw();
		}
		if(nextNode != null){
			nextNode.drawAll();
		}
	}
}
