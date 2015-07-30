package uk.me.jamesthornton.macrolab;

import java.util.ArrayList;
import java.util.Iterator;

public class Model implements Runnable {
    private View view;

    public State currentState;
    public int repeatMode = 0;
    public long sleepDuration = 100;

    public Model(View view) {
        this.view = view;
        this.currentState = new State();
    }

    public void step() {
        // Clone entities
        State newState = currentState.clone();
        ArrayList<Entity> es = newState.entities;

        newState.log("Step beginning ---------------------------------");

        //Individual step
        for (Entity entity : es) {
            entity.step(newState);
        }

        //Interaction step
        int size = es.size();
        for(int i = 0; i < size; i ++) {
            Entity a = es.get(i);
            for(int j = i + 1; j < size; j ++) {
                Entity b = es.get(j);
                a.interactionStep(newState, b);
            }
        }

        //Removal step
        Iterator<Entity> removalIterator = es.iterator();
        while(removalIterator.hasNext()) {
            Entity entity = removalIterator.next();

            if(entity.removeFlag) {
                removalIterator.remove();
            }
        }

        //Finalise new state
        newState.time ++;
        newState.integrateDeferred();
        currentState = newState;

        //Repaint
        view.triggerPaint();
    }
    public void clear() {
        currentState.entities.clear();
        view.triggerPaint();
    }

    //Thread control
    @Override
    public void run() {
        if(repeatMode == -1) {
            while(repeatMode == -1) {
                step();
                sleep(sleepDuration);
            }
        } else {
            for(int i = 0; i < repeatMode; i ++) {
                step();

                if(i < repeatMode - 1) {
                    sleep(sleepDuration);
                }
            }
        }
    }
    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
