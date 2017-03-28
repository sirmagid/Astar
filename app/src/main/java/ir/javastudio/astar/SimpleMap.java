package ir.javastudio.astar;

import ir.javastudio.astar.pathfinding.PathFindingContext;
import ir.javastudio.astar.pathfinding.TileBasedMap;
//http://stackoverflow.com/questions/7195056/how-do-i-programmatically-add-buttons-into-layout-one-by-one-in-several-lines
//http://stackoverflow.com/questions/9742039/a-pathfinding-java-slick2d-libraryhttp://stackoverflow.com/questions/9742039/a-pathfinding-java-slick2d-library
/**
 * Created by majid on 3/28/2017.
 */

class SimpleMap implements TileBasedMap {
    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;

    private static final int[][] MAP = {
            /*{1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0},
            {1,0,1,1,1,1,1,1,1,0},
            {1,1,1,1,1,1,0,0,1,0},
            {1,1,0,0,1,1,1,0,1,0},
            {1,1,1,0,1,1,1,0,0,0},
            {1,1,1,0,0,0,0,0,1,0},
            {1,0,1,1,1,1,1,1,1,0},
            {1,0,0,0,0,0,0,1,0,0},
            {1,1,1,1,1,1,1,1,1,0}*/
    };

    @Override
    public boolean blocked(PathFindingContext ctx, int x, int y) {
        return MAP[y][x] != 0;
    }

    @Override
    public float getCost(PathFindingContext ctx, int x, int y) {
        return 1.0f;
    }

    @Override
    public int getHeightInTiles() {
        return HEIGHT;
    }

    @Override
    public int getWidthInTiles() {
        return WIDTH;
    }

    @Override
    public void pathFinderVisited(int x, int y) {}

}