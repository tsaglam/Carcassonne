package carcassonne.testutils;

import static carcassonne.model.grid.GridDirection.CENTER;
import static carcassonne.model.grid.GridDirection.EAST;
import static carcassonne.model.grid.GridDirection.NORTH;
import static carcassonne.model.grid.GridDirection.NORTH_EAST;
import static carcassonne.model.grid.GridDirection.NORTH_WEST;
import static carcassonne.model.grid.GridDirection.SOUTH;
import static carcassonne.model.grid.GridDirection.SOUTH_EAST;
import static carcassonne.model.grid.GridDirection.SOUTH_WEST;
import static carcassonne.model.grid.GridDirection.WEST;

import java.awt.Color;
import java.util.List;

import carcassonne.model.grid.Grid;
import carcassonne.model.grid.GridDirection;
import carcassonne.model.grid.GridSpot;
import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.Tile;

/**
 * Utility class for printing ASCII representation of the Carcassonne game grid.
 */
public final class GridPrinter {

    private static final String EMPTY_CELL = "   |";
    private static final String ROW_SEPARATOR = "---+";

    private static final int HEADER_INDENT = 5;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_COLOR_TEMPLATE = "\u001B[38;2;%d;%d;%dm";

    private GridPrinter() {
        // private constructor ensures non-instantiability!
    }

    /**
     * Prints an ASCII representation of the grid to standard output. Each tile is displayed as a 3x3 character grid showing
     * terrain and meeple positions.
     */
    public static void printGrid(Grid grid) {
        printHeader(grid.getWidth());
        for (int y = 0; y < grid.getHeight(); y++) {
            // print row:
            printGridLine(grid, y, List.of(NORTH_WEST, NORTH, NORTH_EAST), false);
            printGridLine(grid, y, List.of(WEST, CENTER, EAST), true);
            printGridLine(grid, y, List.of(SOUTH_WEST, SOUTH, SOUTH_EAST), false);

            // print row separator:
            System.out.print(" ".repeat(HEADER_INDENT) + "+");
            System.out.println(ROW_SEPARATOR.repeat(grid.getWidth()));
        }

        System.out.println();
    }

    /**
     * Prints a single tile as a 3x3 character representation.
     */
    public static void printTile(Tile tile) {
        for (int i = 0; i < 9; i++) {
            TerrainType terrain = tile.getTerrain(GridDirection.byRow().get(i));
            System.out.print(terrain == TerrainType.FIELDS ? ' ' : terrain.toString().charAt(0));
            if ((i + 1) % 3 == 0) {
                System.out.println();
            }
        }
    }

    private static void printHeader(int width) {
        System.out.print(" ".repeat(HEADER_INDENT));
        for (int x = 0; x < width; x++) {
            System.out.printf(" %2d ", x);
        }
        System.out.println();

        // Top border:
        System.out.print(" ".repeat(HEADER_INDENT) + "+");
        System.out.println(ROW_SEPARATOR.repeat(width));
    }

    private static void printGridLine(Grid grid, int y, List<GridDirection> directions, boolean showRowNumber) {
        if (showRowNumber) {
            System.out.printf("  %2d |", y);
        } else {
            System.out.print(" ".repeat(HEADER_INDENT) + "|");
        }

        for (int x = 0; x < grid.getWidth(); x++) {
            GridSpot spot = grid.getSpot(x, y);
            if (spot.isFree()) {
                System.out.print(EMPTY_CELL);
            } else {
                directions.forEach(dir -> System.out.print(getTileChar(spot, dir)));
                System.out.print("|");
            }
        }
        System.out.println();
    }

    private static String getTileChar(GridSpot spot, GridDirection direction) {
        TerrainType terrain = spot.getTile().getTerrain(direction);

        // Meeples:
        if (spot.getTile().hasMeeple() && spot.getTile().getMeeple().getPosition() == direction) {
            Color playerColor = spot.getTile().getMeeple().getOwner().getColor().lightColor();
            String ansiMeeple = ansiColor(playerColor);
            return ansiMeeple + getMeepleSymbol(terrain) + ANSI_RESET;
        }

        // Terrain:
        if (terrain == TerrainType.FIELDS) {
            return " ";
        }

        Color terrainColor = getTerrainColor(terrain);
        String ansiTerrain = ansiColor(terrainColor);
        char symbol = terrain.toString().charAt(0);

        return ansiTerrain + symbol + ANSI_RESET;
    }

    private static char getMeepleSymbol(TerrainType terrain) {
        return switch (terrain) {
            case MONASTERY -> '♚';
            case CASTLE -> '♜';
            case ROAD -> '♟';
            case FIELDS -> '✳';
            case OTHER -> '?';
        };
    }

    private static String ansiColor(Color c) {
        return String.format(ANSI_COLOR_TEMPLATE, c.getRed(), c.getGreen(), c.getBlue());
    }

    private static Color getTerrainColor(TerrainType terrain) {
        return switch (terrain) {
            case MONASTERY -> new Color(204, 136, 70); // Old red monastery roofs
            case CASTLE -> new Color(184, 158, 122); // Classic castle beige dirt
            case ROAD -> new Color(200, 200, 200); // Light, readable grey
            case OTHER -> new Color(100, 100, 100); // Dark grey
            case FIELDS -> new Color(106, 140, 52); // green fields
        };
    }
}