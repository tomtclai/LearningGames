package dyehard;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import Engine.Vector2;
import dyehard.Enemies.Enemy;
import dyehard.Enemies.EnemyManager;
import dyehard.Player.Hero;

public class StudentObjManager {
    public static Object studentHero;
    public static Hero hero;
    public static HashMap<Object, Enemy> enemies;

    public static ClassReflector studentObjRef;
    private static boolean useStudentObj = false;

    static {
        studentHero = null;
        enemies = new HashMap<Object, Enemy>();
    }

    public static void validate() {
        studentObjRef = new ClassReflector("StudentObj");
        studentObjRef.reflect();
        String[] cs = { "public StudentObj()",
                "public StudentObj(Engine.Vector2,float,float)" };
        String[] ms = {
                "public float StudentObj.getWidth()",
                "public float StudentObj.getHeight()",
                "public void StudentObj.setWidth(float)",
                "public void StudentObj.setHeight(float)",
                "public void StudentObj.setCenter(float,float)",
                "public Engine.Vector2 StudentObj.getCenter()",
                "public void StudentObj.setTexture(java.awt.image.BufferedImage)",
                "public java.awt.image.BufferedImage StudentObj.getTexture()" };
        useStudentObj = studentObjRef.validate(cs, ms);
    }

    public static void update() {
        if (studentHero != null) {
            studentHeroUpdate();
        }
        studentEnemyUpdate();
    }

    public static Hero registerHero(Object h) {
        if ((h == null) || (!useStudentObj)) {
            return null;
        } else {
            studentHero = h;
        }

        if (hero == null) {
            hero = new Hero();
        }
        hero.center = (Vector2) studentObjRef.invokeMethod(h, "getCenter");
        hero.size.set((float) studentObjRef.invokeMethod(h, "getWidth"),
                (float) studentObjRef.invokeMethod(h, "getHeight"));
        hero.texture = (BufferedImage) studentObjRef.invokeMethod(h,
                "getTexture");

        return hero;
    }

    private static void studentHeroUpdate() {
        if (useStudentObj) {
            hero.center = (Vector2) studentObjRef.invokeMethod(studentHero,
                    "getCenter");
            hero.size
                    .set((float) studentObjRef.invokeMethod(studentHero,
                            "getWidth"), (float) studentObjRef.invokeMethod(
                            studentHero, "getHeight"));
            hero.texture = (BufferedImage) studentObjRef.invokeMethod(
                    studentHero, "getTexture");
        }
    }

    public static Enemy registerEnemy(Object e) {
        if ((e == null) || (!useStudentObj)) {
            return null;
        }

        Vector2 c = (Vector2) studentObjRef.invokeMethod(e, "getCenter");
        float x = (float) studentObjRef.invokeMethod(e, "getWidth");
        float y = (float) studentObjRef.invokeMethod(e, "getHeight");
        BufferedImage t = (BufferedImage) studentObjRef.invokeMethod(e,
                "getTexture");

        Enemy enemy = new Enemy(c, x, y, hero, t);
        enemy.speed = 0.15f;
        EnemyManager.registerEnemy(enemy);
        enemies.put(e, enemy);

        return enemy;
    }

    private static void studentEnemyUpdate() {
        for (Object e : enemies.keySet()) {
            float x = (float) studentObjRef.invokeMethod(e, "getWidth");
            float y = (float) studentObjRef.invokeMethod(e, "getHeight");
            studentObjRef.invokeMethod(e, "setWidth", x);
            studentObjRef.invokeMethod(e, "setHeight", y);
            x = (float) studentObjRef.invokeMethod(e, "getWidth");
            y = (float) studentObjRef.invokeMethod(e, "getHeight");
            Vector2 c = (Vector2) studentObjRef.invokeMethod(e, "getCenter");
            studentObjRef.invokeMethod(e, "setCenter", c.getX(), c.getY());
            Color color = (Color) studentObjRef.invokeMethod(e, "getColor");
            enemies.get(e).setSize(x, y);
            enemies.get(e).setCenter(c);
            enemies.get(e).setColor(color);
        }
    }

    public static void clear() {
        for (Enemy e : enemies.values()) {
            e.destroy();
        }
        enemies.clear();
    }

    public static HashMap<Object, Enemy> getEnemies() {
        return enemies;
    }

    public static boolean useStudentObj() {
        return useStudentObj;
    }
}
