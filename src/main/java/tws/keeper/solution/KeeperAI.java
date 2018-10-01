package tws.keeper.solution;

import tws.keeper.model.*;
import tws.keeper.model.Observable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static tws.keeper.model.Action.*;

public class KeeperAI implements Keeper {

    private static final List<Action> availableActions = Arrays.asList(GO_DOWN, GO_LEFT, GO_RIGHT, GO_UP);


    /**
     * This Keeper Artificial Inteligence simply acts randomly
     *
     * @param maze
     * @return
     */
    public Action act(Observable maze) {
        return availableActions.get(ThreadLocalRandom.current().nextInt(availableActions.size()));
    }

}