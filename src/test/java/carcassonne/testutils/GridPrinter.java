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

    private static final int HEADER_INDENT = 5;

    private static final String COLUMN_LABEL = " %2d ";
    private static final String ROW_LABEL = "  %2d |";
    private static final String VERTICAL_BORDER = "|";
    private static final String INTERSECTION = "+";
    private static final String EMPTY_CELL = "   |";
    private static final String ROW_SEPARATOR = "---+";

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_COLOR_TEMPLATE = "\u001B[38;2;%d;%d;%dm";

    private GridPrinter() {
        // private constructor ensures non-instantiability!
    }

    /**
     * Returns an ASCII representation of the grid as a string. Each tile is displayed as a 3x3 character grid showing
     * terrain and meeple positions.
     */
    public static String printGrid(Grid grid) {
        StringBuilder builder = new StringBuilder();
        builder.append(printHeader(grid.getWidth()));
        for (int y = 0; y < grid.getHeight(); y++) {
            builder.append(printGridLine(grid, y, List.of(NORTH_WEST, NORTH, NORTH_EAST), false));
            builder.append(printGridLine(grid, y, List.of(WEST, CENTER, EAST), true));
            builder.append(printGridLine(grid, y, List.of(SOUTH_WEST, SOUTH, SOUTH_EAST), false));

            builder.append(" ".repeat(HEADER_INDENT)).append(INTERSECTION);
            builder.append(ROW_SEPARATOR.repeat(grid.getWidth()));
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }

    /**
     * Returns a single tile as a 3x3 character representation.
     */
    public static String printTile(Tile tile) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            TerrainType terrain = tile.getTerrain(GridDirection.byRow().get(i));
            builder.append(terrain == TerrainType.FIELDS ? ' ' : terrain.toString().charAt(0));
            if ((i + 1) % 3 == 0) {
                builder.append(System.lineSeparator());
            }
        }
        return builder.toString();
    }

    private static String printHeader(int width) {
        StringBuilder builder = new StringBuilder();
        builder.append(" ".repeat(HEADER_INDENT));
        for (int x = 0; x < width; x++) {
            builder.append(COLUMN_LABEL.formatted(x));
        }
        builder.append(System.lineSeparator());

        builder.append(" ".repeat(HEADER_INDENT)).append(INTERSECTION);
        builder.append(ROW_SEPARATOR.repeat(width));
        builder.append(System.lineSeparator());
        return builder.toString();
    }

    private static String printGridLine(Grid grid, int y, List<GridDirection> directions, boolean showRowNumber) {
        StringBuilder builder = new StringBuilder();
        if (showRowNumber) {
            builder.append(ROW_LABEL.formatted(y));
        } else {
            builder.append(" ".repeat(HEADER_INDENT)).append(VERTICAL_BORDER);
        }

        for (int x = 0; x < grid.getWidth(); x++) {
            GridSpot spot = grid.getSpot(x, y);
            if (spot.isFree()) {
                builder.append(EMPTY_CELL);
            } else {
                directions.forEach(dir -> builder.append(getTileChar(spot, dir)));
                builder.append(VERTICAL_BORDER);
            }
        }
        builder.append(System.lineSeparator());
        return builder.toString();
    }

    private static String getTileChar(GridSpot spot, GridDirection direction) {
        TerrainType terrain = spot.getTile().getTerrain(direction);

        if (spot.getTile().hasMeeple() && spot.getTile().getMeeple().getPosition() == direction) {
            Color playerColor = spot.getTile().getMeeple().getOwner().getColor().lightColor();
            String ansiMeeple = ansiColor(playerColor);
            return ansiMeeple + getMeepleSymbol(terrain) + ANSI_RESET;
        }

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
            case FIELDS -> '♞';
            case OTHER -> '?';
        };
    }

    private static String ansiColor(Color c) {
        return ANSI_COLOR_TEMPLATE.formatted(c.getRed(), c.getGreen(), c.getBlue());
    }

    private static Color getTerrainColor(TerrainType terrain) {
        return switch (terrain) {
            case MONASTERY -> new Color(204, 136, 70);
            case CASTLE -> new Color(184, 158, 122);
            case ROAD -> new Color(200, 200, 200);
            case OTHER -> new Color(100, 100, 100);
            case FIELDS -> new Color(106, 140, 52);
        };
    }
}