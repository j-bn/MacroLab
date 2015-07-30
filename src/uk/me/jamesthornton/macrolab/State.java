package uk.me.jamesthornton.macrolab;

import java.util.ArrayList;

public class State {
    public int time;
    public int nextUID = 0;
    public ArrayList<Entity> entities;
    public ArrayList<Entity> deferredEntities = new ArrayList<>();

    public State() {
        time = 0;
        entities = new ArrayList<>();
    }
    public void add(Entity entity) {
        entities.add(entity);
    }
    public void addDeferred(Entity entity) {
        deferredEntities.add(entity);
    }
    public void integrateDeferred() {
        entities.addAll(deferredEntities);
        deferredEntities.clear();
    }
    public int nextUID() {
        int result = nextUID;
        nextUID ++;
        return result;
    }

    public State clone() {
        State clone = new State();
        clone.time = time;
        clone.nextUID = nextUID;

        //Clone entities
        for (Entity entity : entities) {
            clone.add(entity.clone());
        }

        return clone;
    }

    public void log(String s) {
        Main.log(time + " - " + s);
    }
}
