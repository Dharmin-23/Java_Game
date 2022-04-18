package demo;

import java.util.*;

import common.Game;
import common.Machine;

public class Game_0014 extends Game {

    private ArrayList<Machine> machines;
    private int numFaulty;

    public Game_0014() {
        machines = new ArrayList<>();
    }

    @Override
    public void addMachines(ArrayList<Machine> machines, int numFaulty) {
        this.machines = machines;
        this.numFaulty = numFaulty;
    }

    @Override
    public void startPhase() {
        int nextLeader = (int) (Math.random() * machines.size());
        // int nextFaulty = 0;
        // int nextFaulty = (int) (Math.random() * (numFaulty + 1));
        int nextFaulty = numFaulty - 1;
        // int nextFaulty = numFaulty;
        ArrayList<Integer> IdArray = new ArrayList<>();
        for (int i = 0; i < machines.size(); i++) {
            IdArray.add(i);
        }
        Collections.shuffle(IdArray);
        for (int i = 0; i < IdArray.size(); i++) {
            machines.get(IdArray.get(i)).setMachines(machines);
            if (i >= nextFaulty) {
                machines.get(IdArray.get(i)).setState(true);
            } else {
                machines.get(IdArray.get(i)).setState(false);
            }
        }
        machines.get(nextLeader).setLeader();
    }

}
