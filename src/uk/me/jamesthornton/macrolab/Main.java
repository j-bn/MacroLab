package uk.me.jamesthornton.macrolab;

import uk.me.jamesthornton.macrolab.evo.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

public class Main extends JFrame implements MouseListener, KeyListener {
    public static final Map<String, Map<String, Float>> schema = new HashMap<>();

    private Model model;
    private View view;

    public Main() {
        view = new View();
        model = new Model(view);

        view.updateModel(model);

        setupGUI();
    }

    //Initialisation methods
    private void setupGUI() {
        view.addMouseListener(this);
        view.addKeyListener(this);

        this.setLayout(new BorderLayout());
        this.add(view, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setVisible(true);
    }
    public static void main(String[] args) {
        new Main();
    }

    public void runModel(int repeatMode) {
        model.repeatMode = repeatMode;
        (new Thread(model)).start();
    }
    public Entity findEntityAtPoint(Vector2 p) {
        for(Entity entity : model.currentState.entities) {
            if(entity instanceof Agent) {
                Agent agent = (Agent) entity;
                if(Vector2.distance(agent.position, p) <= View.AGENT_RADIUS) {
                    return entity;
                }
            }
        }

        return null;
    }

    //Utility methods
    public static void log(String s) {
        System.out.println(s);
    }
    public static float random() {
        return (float) Math.random();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    //Listeners
    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Vector2 clickedPoint = new Vector2(e.getX(), e.getY());
        Entity clickedEntity = findEntityAtPoint(clickedPoint);

        view.selectedEntity = clickedEntity;

        if(clickedEntity == null) {
            //Create entity
            Agent a = new Agent(model.currentState);
            a.position.set(e.getX(), e.getY());

            //Spawn entity
            model.currentState.add(a);

            //Update view
            view.triggerPaint();
        } else {
            clickedEntity.printInfo(model.currentState.time);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case 82: //R
                runModel(model.repeatMode == -1 ? 0 : -1);

                break;
            case 83: //S
                runModel(1);

                break;
            case 67: //C
                model.clear();

                break;

            default:
                Main.log("Unbound key pressed: " + e.getKeyCode());
        }
    }
}