package demo;

import java.util.*;
import common.Location;
import common.Machine;

public class Machine_0014 extends Machine {

    private static int ID = 0;
    private int id;
    private int stepsize;
    private boolean isCorrect;
    private int RightRound1, LeftRound1, RightRound2, LeftRound2;
    private Location position;
    private ArrayList<Integer> prevDirectionVect;
    private ArrayList<Machine> machines = new ArrayList<>();
    private boolean velocitySet;
    private int phaseNum;
    private int current_decision; // 1 for right 0 for left

    public Machine_0014() {
        id = ID++;
        prevDirectionVect = new ArrayList<>();
        prevDirectionVect.add(0);
        prevDirectionVect.add(1);
        position = new Location(0, 0);
        velocitySet = false;
        phaseNum = 0;
        LeftRound1 = 0;
        LeftRound2 = 0;
        RightRound1 = 0;
        RightRound2 = 0;
        current_decision = 0;
    }

    @Override
    public void setMachines(ArrayList<Machine> machines) {
        this.machines = machines;

    }

    @Override
    public void setState(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    @Override
    public void setLeader() {
        int choice = (int) (2 * Math.random());
        if (isCorrect) {
            for (Machine m : machines) {
                m.sendMessage(id, phaseNum, 0, choice);
            }

        } else {
            int t = (machines.size() % 3 == 0) ? machines.size() / 3 - 1 : (int) Math.floor(machines.size() / 3);
            ArrayList<Integer> IdArray = new ArrayList<>();
            for (int i = 0; i < machines.size(); i++) {
                IdArray.add(i);
            }
            Collections.shuffle(IdArray);
            for (int i = 0; i < 2 * t + 1; i++) {
                machines.get(IdArray.get(i)).sendMessage(id, phaseNum, 0, choice);
            }
        }
    }

    @Override
    public void sendMessage(int sourceId, int phaseNum, int roundNum, int decision) {
        int t = (machines.size() % 3 == 0) ? machines.size() / 3 - 1 : (int) Math.floor(machines.size() / 3);
        switch (roundNum) {
            case 0:
                current_decision = (isCorrect) ? decision : (decision + 1) % 2;
                for (Machine ma : machines) {
                    ma.sendMessage(id, phaseNum, 1, current_decision);
                }
                break;
            case 1:
                if (decision == 1) {
                    RightRound1++;
                } else {
                    LeftRound1++;
                }
                if (RightRound1 + LeftRound1 >= 2 * t + 1 && !velocitySet) {
                    current_decision = (isCorrect ^ (RightRound1 > LeftRound1)) ? 0 : 1;
                    for (Machine machine : machines) {
                        machine.sendMessage(id, phaseNum, 2, current_decision);
                    }
                }
                break;
            case 2:
                if (decision == 1) {
                    RightRound2++;
                } else {
                    LeftRound2++;
                }
                if (RightRound2 + LeftRound2 >= 2 * t + 1 && !velocitySet) {
                    current_decision = (RightRound2 > LeftRound2) ? 1 : 0;
                    int multiplier = 2 * current_decision - 1;
                    int nextX = prevDirectionVect.get(1) * multiplier;
                    int nextY = (-prevDirectionVect.get(0)) * multiplier;
                    prevDirectionVect.set(0, nextX);
                    prevDirectionVect.set(1, nextY);
                    velocitySet = true;
                }
                break;
            default:
                System.out.println("Incorrect Round Num: " + roundNum + "\n");
        }
    }

    @Override
    public void setStepSize(int stepSize) {
        stepsize = stepSize;

    }

    @Override
    public void move() {
        position.setLoc(position.getX() + prevDirectionVect.get(0) * stepsize,
                position.getY() + prevDirectionVect.get(1) * stepsize);
        velocitySet = false;
        RightRound1 = 0;
        LeftRound1 = 0;
        LeftRound2 = 0;
        RightRound2 = 0;
        current_decision = 0;
    }

    @Override
    public String name() {
        return "DEMO_" + id;
    }

    @Override
    public Location getPosition() {
        return position;
    }

}
