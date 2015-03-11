package dyehard;

import Engine.Vector2;
import dyehard.UpdateManager.Updateable;

public class GameObject extends DyehardRectangle implements Updateable {
    private boolean speedUpSet = false;

    protected ManagerState updateState;

    public GameObject() {
        updateState = ManagerState.ACTIVE;
        UpdateManager.register(this);
    }

    @Override
    public ManagerState updateState() {
        return updateState;
    }

    @Override
    public void destroy() {
        super.destroy();
        updateState = ManagerState.DESTROYED;
    }

    @Override
    public void setSpeed(float factor) {
        // TODO For debug purposes, maybe remove for final version
        if ((factor > 1 && !speedUpSet) || (factor < 1 && speedUpSet)) {
            float curSpeed = velocity.getX();
            velocity = new Vector2(curSpeed * factor, 0f);
            if (factor > 1) {
                speedUpSet = true;
            } else if (factor < 1) {
                speedUpSet = false;
            }
        }
    }
}
