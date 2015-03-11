package dyehard;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ClassReflector {
    private final HashMap<String, Constructor<?>> constructors = new HashMap<String, Constructor<?>>();
    private final HashMap<String, Method> methods = new HashMap<String, Method>();

    private final String className;
    private boolean reflected;
    private boolean validated;

    public ClassReflector(String target) {
        reflected = false;
        validated = false;
        className = target;
    }

    // Get the class' constructors, fields, and methods, store in hashmaps
    public boolean reflect() {
        try {
            Class<?> c = Class.forName(className);
            // System.out.format("Class:%n  %s%n%n", c.getCanonicalName());

            // Package p = c.getPackage();
            // System.out.format("Package:%n  %s%n%n", (p != null ? p.getName()
            // : "-- No Package --"));

            int i = 0;
            for (Constructor<?> constructor : c.getConstructors()) {
                String temp = constructor.getName() + Integer.toString(i);
                constructors.put(temp, constructor);
                i++;
                // System.out.println(constructor.getName() + "   " +
                // constructor);
            }
            for (Method method : c.getMethods()) {
                methods.put(method.getName(), method);
                // System.out.println(method.getName() + "   "
                // + method.toGenericString());
            }

        } catch (ClassNotFoundException x) {
            System.out.println(x.getMessage());
            x.printStackTrace();
            return false;
        }
        reflected = true;
        return true;
    }

    // Validate the class to see if it has the appropriate constructors, fields,
    // and methods
    public boolean validate(String[] c, String[] m) {
        // must reflect first
        if (!reflected) {
            return false;
        }

        for (String cs : c) {
            int test = 0;
            for (Constructor<?> cs2 : constructors.values()) {
                if (cs.equals(cs2.toGenericString())) {
                    test++;
                }
            }
            if (test < 1) {
                // System.out.println(cs);
                return false;
            }
        }
        for (String ms : m) {
            int test = 0;
            for (Method ms2 : methods.values()) {
                if (ms.equals(ms2.toGenericString())) {
                    // System.out.println(ms);
                    test++;
                }
            }
            if (test < 1) {
                // System.out.println(ms);
                return false;
            }
        }
        validated = true;
        return true;
    }

    // create and return object by calling constructor by name
    public Object createObj(String construc, Object... arguments) {
        try {
            return constructors.get(construc).newInstance(arguments);
        } catch (InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    // invoke method by name
    public Object invokeMethod(Object obj, String method, Object... params) {
        if (!methods.containsKey(method)) {
            return false;
        } else {
            try {
                return methods.get(method).invoke(obj, params);
            } catch (IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean isReflected() {
        return reflected;
    }

    public boolean isValidated() {
        return validated;
    }

    public String getClassName() {
        return className;
    }
}