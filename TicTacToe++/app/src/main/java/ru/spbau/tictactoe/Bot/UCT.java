package ru.spbau.tictactoe.Bot;

import java.util.Collections;
import java.util.Comparator;


public class UCT {
    public static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return (nodeWinScore / (double) nodeVisit) + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    static Tree.Node findBestNodeWithUCT(Tree.Node node) {
        final int parentVisit = node.getState().getVisitCount();
        return Collections.max(
                node.getChildArray(),
                new Comparator<Tree.Node>() {
                    @Override
                    public int compare(Tree.Node node, Tree.Node t1) {
                        return ((Double)uctValue(parentVisit, node.getState().getWinScore(),
                                node.getState().getVisitCount())).
                                compareTo(uctValue(parentVisit,
                                        t1.getState().getWinScore(), t1.getState().getVisitCount()));
                    }
                });
    }
}
