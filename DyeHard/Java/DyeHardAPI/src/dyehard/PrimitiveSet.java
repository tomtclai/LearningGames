package dyehard;

import java.util.ArrayList;
import java.util.List;

import Engine.Primitive;

public class PrimitiveSet {
    protected List<Primitive> primitives;

    public PrimitiveSet() {
        primitives = new ArrayList<Primitive>();
    }

    public void addPrimitive(Primitive primitive) {
        if (primitive != null) {
            primitives.add(primitive);
        }
    }

    public void destroyAll() {
        for (Primitive p : primitives) {
            p.destroy();
        }
    }
}
