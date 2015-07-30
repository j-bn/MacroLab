package uk.me.jamesthornton.macrolab.evo;

import com.rits.cloning.Cloner;
import uk.me.jamesthornton.macrolab.*;

import java.util.HashMap;
import java.util.Map;

public class Agent extends Entity {
    /* Values are based on a human timescale where 1 step = 5 years */

    public static final int LIFETIME = 16;
    public static final int REPRODUCTION_AGE = 3;
    public static final int REPRODUCTION_DELAY = 2;
    public static final float REPRODUCTION_RANGE = 10;

    public static final Map<String, Map<String, Float>> schema = createDefaultSchema();

    public final int birthTime;
    public final int uid;
    public int lastReproductionTime = -1;
    public Map<String, Float> features;
    public Map<String, Float> attributes;

    public Agent(State state) {
        super();
        birthTime = state.time;
        uid = state.nextUID();
        features = createDefaultFeatures();
        updateAttributes(schema);
    }
    //Copy constructor
    public Agent(Agent source) {
        super(source);
        this.birthTime = source.birthTime;
        this.lastReproductionTime = source.lastReproductionTime;
        this.uid = source.uid;

        Cloner cloner = new Cloner();
        this.features = cloner.deepClone(source.features);
        this.attributes = cloner.deepClone(source.attributes);
    }

    //Utility methods
    public static Map<String, Float> average(Map<String, Float> a, Map<String, Float> b) {
        Map<String, Float> r = new HashMap<>();
        float factor = 0.5f;

        //[TODO] This method may not work if there are mismatched keys

        //Add entries
        for(Map.Entry<String, Float> entry : a.entrySet()) {
            String attributeName = entry.getKey();

            if(b.containsKey(attributeName)) {
                float sum =  a.get(attributeName) + b.get(attributeName);
                r.put(attributeName, sum / 2);
            }
        }

        return r;
    }

    public void step(State state) {
        double angle = Math.random() * 2f * Math.PI;
        position.add(Vector2.unitAngle(angle).multiply(8));

        //Death by lifetime
        if(getAge(state.time) >= LIFETIME) {
            removeFlag = true;
        }
    }
    public void interactionStep(State state, Entity e) {
        int time = state.time;

        if(e instanceof Agent) {
            Agent a = (Agent) e;

            // state.log("Interaction step between agents " + this.uid + " and " + a.uid);

            double randA = Math.random() * 2, randB = Math.random() * 2;
            // Do both agents consent?
            if(randB < this.attributes.get("Attraction") && randA < a.attributes.get("Attraction")) {

                // Can both agents reproduce?
                if(this.canReproduce(time)
                        && a.canReproduce(time)
                        && Vector2.distance(this.position, a.position) <= REPRODUCTION_RANGE) {

                    // Reproduce
                    state.addDeferred(reproduce(state, this, a));
                }
            } else {
                state.log("COMBAT");

                Agent winner;
                if(this.attributes.get("Combat") > a.attributes.get("Combat")) {
                    winner = this;
                } else {
                    winner = a;
                }

                state.log("Agents " + this.uid + " and " + a.uid + " fought and " + winner.uid + " won");

                winner.removeFlag = true;
            }
        }
    }
    public int getAge(int time) {
        return time - birthTime;
    }
    public boolean canReproduce(int time) {
        /*
        Main.log("  Checking reproduction eligibility for agent " + uid);
        Main.log("   Age = " + getAge(time));
        Main.log("   LRT = " + lastReproductionTime);
        Main.log("   TSLR = " + (time - lastReproductionTime));
        */

        return getAge(time) >= REPRODUCTION_AGE
                && (lastReproductionTime == -1 || (time - lastReproductionTime) >= REPRODUCTION_DELAY);
    }
    public Map<String, Float> calculateAttributes(Map<String, Map<String, Float>> schema) {
        attributes = new HashMap<>();

        for(Map.Entry<String, Map<String, Float>> schemaAttribute : schema.entrySet()) {
            String attributeName = schemaAttribute.getKey();
            Map<String, Float> featureMap = schemaAttribute.getValue();

            float value = 0;
            for(Map.Entry<String, Float> schemaFeature : featureMap.entrySet()) {
                String featureName = schemaFeature.getKey();
                float featureDevelopment = 0;

                if(features.containsKey(featureName)) {
                    featureDevelopment = features.get(featureName);
                }

                value += featureDevelopment * schemaFeature.getValue();
            }

            attributes.put(attributeName, value);
        }

        return attributes;
    }
    public Map<String, Float> updateAttributes(Map<String, Map<String, Float>> schema) {
        return (this.attributes = calculateAttributes(schema));
    }
    public void printInfo(int time) {
        Main.log("");
        Main.log("| Agent #" + uid);
        Main.log("| Age: " + getAge(time) + " (Born " + birthTime + ")");
        Main.log("| Last Reproduced: " + lastReproductionTime);
        Main.log("| Position: " + position.toString());
        Main.log("|");

        //Features
        Main.log("| Features: ");
        if(features == null) {
            Main.log("| NULL");
        } else {
            for (Map.Entry<String, Float> entry : features.entrySet()) {
                Main.log("|  - " + entry.getKey() + ": " + entry.getValue());
            }
        }
        Main.log("|");

        //Attributes
        Main.log("| Attributes: ");
        if(attributes == null) {
            Main.log("| NULL");
        } else {
            for(Map.Entry<String, Float> entry : attributes.entrySet()) {
                Main.log("|  - " + entry.getKey() + ": " + entry.getValue());
            }
        }
        Main.log("|");
    }

    private static Map<String, Map<String, Float>> createDefaultSchema() {
        Main.log("Creating default schema...");

        Map<String, Map<String, Float>> s = new HashMap<>();

        //Proceeding by attribute...

        //Attraction
        Map<String, Float> mapA = new HashMap<>();
        mapA.put("Feathers", 1.0f);
        s.put("Attraction", mapA);

        //Movement
        Map<String, Float> mapB = new HashMap<>();
        mapB.put("Legs", 1.0f);
        s.put("Movement", mapB);

        //Combat
        Map<String, Float> mapC = new HashMap<>();
        mapC.put("Claws", 1.0f);
        mapC.put("Legs", 0.1f);
        s.put("Combat", mapC);

        return s;
    }
    private static Map<String, Float> createDefaultFeatures() {
        Map<String, Float> f = new HashMap<>();
        f.put("Feathers", 1.0f);
        f.put("Legs", 1.0f);
        f.put("Claws", 1.0f);

        return f;
    }

    //Named utility constructors
    public Agent clone() {
        return new Agent(this);
    }
    public static Agent reproduce(State state, Agent a, Agent b) {
        Agent child = new Agent(state);
        child.position = Vector2.add(a.position, b.position).divide(2);

        a.lastReproductionTime = state.time;
        b.lastReproductionTime = state.time;

        child.features = average(a.features, b.features);
        child.updateAttributes(schema);

        state.log("Agents " + a.uid + " and " + b.uid + " reproduced to have child " + child.uid);

        return child;
    }
}
