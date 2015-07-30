package uk.me.jamesthornton.macrolab;

public class Entity {
    public Vector2 position;
    public double rotation;
    public boolean removeFlag = false;

    public Entity() {
        this.position = new Vector2();
        this.rotation = 0;
    }
    public Entity(Entity source) {
        this.position = source.position.clone();
        this.rotation = source.rotation;
    }

    public void step(State state) {
    }
    public void interactionStep(State state, Entity e) {
    }
    public void printInfo(int time) {

    }

    public Entity clone() {
        return new Entity(this);
    }
}
