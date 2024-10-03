package carcassonne.settings;

import java.awt.Color;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import carcassonne.model.Player;
import carcassonne.model.terrain.TerrainType;
import carcassonne.model.tile.TileDistribution;
import carcassonne.util.MinkowskiDistance;
import carcassonne.view.NotifiableView;
import carcassonne.view.PaintShop;

/**
 * Class for the management of the Carcassonne game settings.
 * @author Timur Saglam
 */
public class GameSettings {
    // THRESHOLD CONSTANTS:
    public static final int MAXIMAL_PLAYERS = 5;
    public static final int MAXIMAL_TILES_ON_HAND = 5;
    public static final int TILE_RESOLUTION = 500;
    public static final int HIGH_DPI_FACTOR = 2; // maximum supported DPI factor.
    public static final int MAXIMAL_MEEPLES = 7;

    // STRING CONSTANTS
	private String YOU;
	private String ALICE;
	private String BOB;
	private String CAROL;
	private String DAN;

    public static final String TILE_FILE_TYPE = ".png";
    public static final String TILE_FOLDER_PATH = "tiles/";
    private static final String EMPTY = "";
    private static final String MEEPLE_PATH = "meeple/meeple_";
    private static final String PNG = ".png";
    private static final String TEMPLATE = "_template";
    //private String[] DEFAULT_NAMES = {YOU, ALICE, BOB, CAROL, DAN};
    private String[] DEFAULT_NAMES = new String[5];

    // COLOR CONSTANTS:
    public static final Color UI_COLOR = new Color(190, 190, 190);
    private static final PlayerColor[] DEFAULT_COLORS = {new PlayerColor(30, 26, 197), new PlayerColor(151, 4, 12), new PlayerColor(14, 119, 25),
            new PlayerColor(216, 124, 0), new PlayerColor(96, 0, 147)};

    // COSMETIC:
    private final List<PlayerColor> colors;
    private final List<String> names;

    // GAME SETUP:
    private int gridWidth;
    private int gridHeight;
    private int numberOfPlayers;
    private int stackSizeMultiplier;
    private MinkowskiDistance distanceMeasure;
    private final TileDistribution tileDistribution;
    private final List<Boolean> playerTypes;

    // GAME RULES:
    private boolean allowFortifying;
    private boolean allowEnclaves;
    private int tilesPerPlayer;
    private final Map<TerrainType, Boolean> meepleRules;
    private boolean splitPatternScore;

    // OTHER/INTERNAL
    private boolean gridSizeChanged;
    private final List<NotifiableView> changeListeners;

    /**
     * Creates a settings instance. Instances hold different setting values when one is changed.
     */
    public GameSettings() {
		initResource();
        colors = new ArrayList<>(Arrays.asList(DEFAULT_COLORS));
        names = new ArrayList<>(Arrays.asList(DEFAULT_NAMES));
        playerTypes = new ArrayList<>(Arrays.asList(false, true, true, true, true));
        meepleRules = new HashMap<>();
        TerrainType.basicTerrain().forEach(it -> meepleRules.put(it, true));
        tileDistribution = new TileDistribution();
        setDistanceMeasure(MinkowskiDistance.ROUNDED_SQUARE);
        numberOfPlayers = 2;
        tilesPerPlayer = 1;
        stackSizeMultiplier = 1;
        gridWidth = 29;
        gridHeight = 19;
        allowEnclaves = true;
        changeListeners = new ArrayList<>();
    }

    /**
     * Returns the distance measure used for AI players.
     * @return the specific Minkowski distance.
     */
    public MinkowskiDistance getDistanceMeasure() {
        return distanceMeasure;
    }

    /**
     * Getter for the height of the grid.
     * @return the gridHeight the grid height in tiles.
     */
    public int getGridHeight() {
        return gridHeight;
    }

    /**
     * Getter for the width of the grid.
     * @return the gridWidth the grid width in tiles.
     */
    public int getGridWidth() {
        return gridWidth;
    }

    /**
     * Returns for a specific meeple type if meeple placement is enabled or disabled.
     * @param type it the specific meeple type to query.
     * @return true if placement is enabled.
     */
    public boolean getMeepleRule(TerrainType type) {
        return meepleRules.getOrDefault(type, false);
    }

    /**
     * Returns how many player are playing in the next round.
     * @return the amount of players.
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * Returns the {@link PlayerColor} of a specific {@link Player}.
     * @param playerNumber is the number of the {@link Player}.
     * @return the {@link PlayerColor}.
     */
    public PlayerColor getPlayerColor(int playerNumber) {
        return colors.get(playerNumber);
    }

    /**
     * Returns the name of a specific {@link Player}.
     * @param playerNumber is the number of the {@link Player}.
     * @return the name.
     */
    public String getPlayerName(int playerNumber) {
        return names.get(playerNumber);
    }

    /**
     * Returns the value for the split points option.
     * @return true if points of a pattern should be split instead of every player getting the score.
     */
    public boolean getSplitPatternScore() {
        return splitPatternScore;
    }

    /**
     * Returns the multiplier for the tile amounts in a tile stack. When a tile amount is 2 and the stack multiplier is 2
     * the tile stack contains for tiles of this type.
     * @return the stack size multiplier.
     */
    public int getStackSizeMultiplier() {
        return stackSizeMultiplier;
    }

    /**
     * Getter for the current tile distribution.
     * @return the tile distribution.
     */
    public TileDistribution getTileDistribution() {
        return tileDistribution;
    }

    /**
     * Specifies the tiles that each player can hold on his hand.
     * @return the tiles per player.
     */
    public int getTilesPerPlayer() {
        return tilesPerPlayer;
    }

    /**
     * @return determines whether it is legal to enclose free spot with tiles, leading to the free spots forming enclaves.
     */
    public boolean isAllowingEnclaves() {
        return allowEnclaves;
    }

    /**
     * Determines if players are allowed to directly place meeples on patterns they already own.
     * @return true if it is allowed.
     */
    public boolean isAllowingFortifying() {
        return allowFortifying;
    }

    /**
     * Gives information whether the user or the game changed the grid size settings.
     * @return the true if the size was changed.
     */
    public boolean isGridSizeChanged() {
        return gridSizeChanged;
    }

    /**
     * Checks if a player with a certain number is set to be computer-controlled.
     * @param playerNumber is the number of the player.
     * @return true if he is computer-controlled.
     */
    public boolean isPlayerComputerControlled(int playerNumber) {
        return playerTypes.get(playerNumber);
    }

    /**
     * Registers a view element that wants to listen to changes.
     * @param notifiable is the view element.
     */
    public void registerNotifiable(NotifiableView notifiable) {
        changeListeners.add(notifiable);
    }

    /**
     * Determines whether it is legal to enclose free spot with tiles, leading to the free spots forming enclaves.
     * @param allowEnclaves set to true if enclaves are allowed.
     */
    public void setAllowEnclaves(boolean allowEnclaves) {
        this.allowEnclaves = allowEnclaves;
    }

    /**
     * Changes whether players are allowed to directly place meeples on patterns they already own.
     * @param allowFortifying forbids or allows fortifying.
     */
    public void setAllowFortifying(boolean allowFortifying) {
        this.allowFortifying = allowFortifying;
    }

    /**
     * Sets the distance measure used for AI players.
     * @param distanceMeasure is the specific Minkowski distance to set.
     */
    public void setDistanceMeasure(MinkowskiDistance distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
    }

    /**
     * Setter for the height of grid.
     * @param gridHeight the grid height in tiles.
     */
    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
        gridSizeChanged = true;
    }

    /**
     * Sets the indicator of grid size change.
     * @param gridSizeChanged the value to set the indicator to.
     */
    public void setGridSizeChanged(boolean gridSizeChanged) {
        this.gridSizeChanged = gridSizeChanged;
    }

    /**
     * Setter for the width of grid.
     * @param gridWidth the grid width in tiles.
     */
    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
        gridSizeChanged = true;
    }

    /**
     * Specifies how many player are playing in the next round.
     * @param numberOfPlayers is the amount of players.
     */
    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    /**
     * Changes the {@link PlayerColor} of a specific {@link Player}.
     * @param color is the new base {@link Color}.
     * @param playerNumber is the number of the {@link Player}.
     */
    public void setPlayerColor(Color color, int playerNumber) {
        colors.set(playerNumber, new PlayerColor(color));
        notifyListeners();
    }

    /**
     * Sets a player with a certain number to be computer-controlled or not.
     * @param computerControlled determines whether he is computer-controlled.
     * @param playerNumber is the number of the player.F
     */
    public void setPlayerComputerControlled(boolean computerControlled, int playerNumber) {
        playerTypes.set(playerNumber, computerControlled);
    }

    /**
     * Changes the name of a specific {@link Player}.
     * @param name is the new name.
     * @param playerNumber is the number of the {@link Player}.
     */
    public void setPlayerName(String name, int playerNumber) {
        names.set(playerNumber, name);
        notifyListeners();
    }

    /**
     * Sets the value for the split points option.
     * @param splitPatternScore determines if points of a pattern should be split instead of every player getting the score.
     */
    public void setSplitPatternScore(boolean splitPatternScore) {
        this.splitPatternScore = splitPatternScore;
    }

    /**
     * Sets the multiplier for the tile amounts in a tile stack. When a tile amount is 2 and the stack multiplier is 2 the
     * tile stack contains for tiles of this type.
     * @param stackSizeMultiplier is the new stack size multiplier.
     */
    public void setStackSizeMultiplier(int stackSizeMultiplier) {
        this.stackSizeMultiplier = stackSizeMultiplier;
    }

    /**
     * Changes how many tiles each player can hold on his hand.
     * @param tilesPerPlayer is the new amount of tiles per player.
     */
    public void setTilesPerPlayer(int tilesPerPlayer) {
        this.tilesPerPlayer = tilesPerPlayer;
    }

    /**
     * Toggles whether for a specific meeple type if meeple placement is enabled or disabled.
     * @param type it the specific meeple type.
     */
    public void toggleMeepleRule(TerrainType type) {
        meepleRules.computeIfPresent(type, (key, enabled) -> !enabled);
    }

    private void notifyListeners() {
        PaintShop.clearCachedImages();
        for (NotifiableView notifiable : changeListeners) {
            EventQueue.invokeLater(notifiable::notifyChange);
        }
    }

    /**
     * Builds the path to the image of a specific meeple type.
     * @param type is the type of terrain the meeple occupies.
     * @param isTemplate specifies whether the template image should be loaded.
     * @return the path as a String.
     */
    public static String getMeeplePath(TerrainType type, boolean isTemplate) { // TODO (MEDIUM) [UTILS] move to image loading utility class?
        return MEEPLE_PATH + type.toString().toLowerCase(Locale.UK) + (isTemplate ? TEMPLATE : EMPTY) + PNG;
    }

	private void initResource() {
		LoadTextGameSettings resource = new LoadTextGameSettings();
		YOU		= resource.get("YOU");
		ALICE	= resource.get("ALICE");
		BOB		= resource.get("BOB");
		CAROL	= resource.get("CAROL");
		DAN		= resource.get("DAN");
    	DEFAULT_NAMES[0]=YOU;
    	DEFAULT_NAMES[1]=ALICE;
    	DEFAULT_NAMES[2]=BOB;
    	DEFAULT_NAMES[3]=CAROL;
    	DEFAULT_NAMES[4]=DAN;
	}
}
