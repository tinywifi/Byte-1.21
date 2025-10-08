package com.syuto.bytes.utils.impl.player;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.*;

import static com.syuto.bytes.Byte.mc;

public class PathFinder {

    public static List<BlockPos> pathFind(BlockPos start, BlockPos goal) {
        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingDouble(n -> n.fCost));
        Map<BlockPos, Node> allNodes = new HashMap<>();

        Node startNode = new Node(start, null, 0, heuristic(start, goal));
        open.add(startNode);
        allNodes.put(start, startNode);

        while (!open.isEmpty()) {
            Node current = open.poll();

            if (current.pos.equals(goal)) {
                List<BlockPos> path = new ArrayList<>();
                while (current != null) {
                    path.add(current.pos);
                    current = current.parent;
                }
                Collections.reverse(path);
                return path;
            }

            for (Direction dir : Direction.values()) {
                if (dir == Direction.UP || dir == Direction.DOWN) continue;

                BlockPos base = current.pos.offset(dir);

                // Flat ground
                if (isWalkable(base)) {
                    tryAddNeighbor(current, base, goal, allNodes, open, 1.0);
                }

                // Step up
                BlockPos up = base.up();
                if (isWalkable(up) && isWalkable(up.up())) {
                    tryAddNeighbor(current, up, goal, allNodes, open, 1.4);
                }

                // Step down
                BlockPos down = base.down();
                if (isWalkable(down)) {
                    tryAddNeighbor(current, down, goal, allNodes, open, 1.1);
                }
            }
        }

        return Collections.emptyList();
    }

    private static void tryAddNeighbor(Node current, BlockPos neighborPos, BlockPos goal,
                                       Map<BlockPos, Node> allNodes, PriorityQueue<Node> open, double moveCost) {
        double gCost = current.gCost + moveCost;

        Node neighbor = allNodes.getOrDefault(neighborPos, new Node(neighborPos));
        if (gCost < neighbor.gCost) {
            neighbor.gCost = gCost;
            neighbor.hCost = heuristic(neighborPos, goal);
            neighbor.fCost = gCost + neighbor.hCost;
            neighbor.parent = current;
            open.add(neighbor);
            allNodes.put(neighborPos, neighbor);
        }
    }

    private static double heuristic(BlockPos a, BlockPos b) {
        return a.getManhattanDistance(b);
    }

    static class Node {
        BlockPos pos;
        Node parent;
        double gCost = Double.MAX_VALUE;
        double hCost;
        double fCost;

        Node(BlockPos pos) {
            this.pos = pos;
        }

        Node(BlockPos pos, Node parent, double gCost, double hCost) {
            this.pos = pos;
            this.parent = parent;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
        }
    }

    private static final Set<String> NON_WALKABLE_BLOCKS = Set.of(
            "lava", "water", "cactus", "fire", "web", "sweet_berry_bush", "campfire", "soul_campfire", "magma_block"
    );

    private static boolean isSafe(BlockState state) {
        String name = state.getBlock().getTranslationKey().toLowerCase();
        for (String bad : NON_WALKABLE_BLOCKS) {
            if (name.contains(bad)) return false;
        }
        return true;
    }

    private static boolean isWalkable(BlockPos pos) {
        BlockPos below = pos.down();
        BlockPos above = pos.up();

        BlockState state = mc.world.getBlockState(pos);
        BlockState stateBelow = mc.world.getBlockState(below);
        BlockState stateAbove = mc.world.getBlockState(above);

        if (!state.isAir()) return false;
        if (!stateAbove.isAir()) return false;

        if (stateBelow.isAir()) return false;
        if (!stateBelow.isSolidBlock(mc.world,below)) return false;
        if (!isSafe(stateBelow)) return false;

        return true;
    }
}
