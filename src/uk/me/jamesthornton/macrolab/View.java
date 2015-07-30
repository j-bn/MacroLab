package uk.me.jamesthornton.macrolab;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class View extends JPanel {
    public static final int AGENT_RADIUS = 4;
    public static final int AGENT_DIAMETER = AGENT_RADIUS * 2;

    private Model model;
    public Entity selectedEntity;

    //Debug string variables
    private int debugStringIndex = 0;

    public View() {
        this.setFocusable(true);
    }

    public void updateModel(Model model) {
        this.model = model;
        triggerPaint();
    }
    public void triggerPaint() {
        //Main.log("Triggering paint...");
        this.repaint();
    }
    public void paint(Graphics g) {
        //Main.log("Painting...");

        if(model != null) {
            ArrayList<Entity> entities = model.currentState.entities;

            //Clear view panel
            g.clearRect(0, 0, this.getWidth(), this.getHeight());

            g.setColor(Color.black);
            for (Entity entity : entities) {
                boolean isSelected = entity == selectedEntity;
                if(isSelected) { g.setColor(Color.orange); }

                g.fillOval(entity.position.getIntX() - AGENT_RADIUS, entity.position.getIntY() - AGENT_RADIUS, AGENT_DIAMETER, AGENT_DIAMETER);

                if(isSelected) { g.setColor(Color.black); }
            }

            resetDebugStrings(g);
            drawDebugString(g, "Time", model.currentState.time + " steps");
            drawDebugString(g, "Population", model.currentState.entities.size() + "");
        }
        else {
            Main.log(" Paint aborted because model is not set");
        }
    }
    private void resetDebugStrings(Graphics g) {
        debugStringIndex = 0;
        g.setColor(Color.gray);
    }
    private void drawDebugString(Graphics g, String a, String b) {
        debugStringIndex ++;
        g.drawString(a + ": " + b, 5, 15 * debugStringIndex);
    }
}
