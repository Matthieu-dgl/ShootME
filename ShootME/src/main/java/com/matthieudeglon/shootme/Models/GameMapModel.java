package com.matthieudeglon.shootme.Models;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameMapModel {

    /* List of tiles that will be used to fast access the blocks property by indices*/
    private List<Tile> tiles = new ArrayList<>();

    /* List of passable blocks */
    private final List<Tile> passableTiles;

    Pane cells = new Pane();


    private final double _width;
    private final double _height;
    private final int _columns;
    private final int _rows;


    private final Map<String, CoordinatesModel> playerAndBonusPositions;


    public GameMapModel(double width, double height,
                        Image tileSet, int cellSide,
                        int columns, int rows,
                        Set<Integer> passableCodes, Set<Integer> unpassableCodes,
                        List<String[]> mapTileComposition,
                        Map<String, CoordinatesModel> coordinateDictionary) {

        _width = width;
        _height = height;

        _columns = columns;
        _rows = rows;

        playerAndBonusPositions = coordinateDictionary;

        generateMap(columns, rows, cellSide, mapTileComposition, passableCodes, unpassableCodes, tileSet);

        passableTiles = tiles.stream().filter(Tile::isPassableForPlayer).collect(Collectors.toList());

    }

    /* Core function */
    public void generateMap(int horizontalCells, int verticalCells, int cellSide,
                            List<String[]> mapTileComposition,
                            Set<Integer> passableCodes, Set<Integer> unpassableCodes,
                            Image tileSet
    ) {
        int tilePerRow = (int) (tileSet.getWidth() / cellSide);

        IntStream.range(0, horizontalCells).mapToObj(i ->
                IntStream.range(0, verticalCells).mapToObj(j -> {

                    var code = Integer.parseInt(mapTileComposition.get(j)[i]);

                    int pos_row = code / tilePerRow;
                    int pos_col = code % tilePerRow;

                    pos_row *= cellSide;
                    pos_col *= cellSide;

                    boolean passable = passableCodes.contains(code);
                    boolean not_passable_for_p = unpassableCodes.contains(code);

                    Rectangle2D rectangle2D = new Rectangle2D(pos_col, pos_row, cellSide, cellSide);

                    return new Tile(i * getTileWidth(), j * getTileHeight(), getTileWidth(),
                            getTileHeight(), passable, not_passable_for_p, tileSet, rectangle2D);

                })
        ).flatMap(s -> s).forEach(cells.getChildren()::add);

        tiles = cells.getChildren().stream().parallel().map(s -> (Tile) s).collect(Collectors.toList());

    }


    /* Utils */
    public CoordinatesModel get_position_of(String id) {
        return convert_tiles_in_pixel(getPlayerAndBonusPositions().get(id));
    }

    public CoordinatesModel convert_tiles_in_pixel(CoordinatesModel tile_coordinates) {
        return new CoordinatesModel(tile_coordinates.getX() * getTileWidth(),
                tile_coordinates.getY() * getTileHeight());
    }

    public int single_index(int x, int y) {
        return (x * _rows) + y;
    }

    public double getTileWidth() {
        return _width / _columns;
    }

    public double getTileHeight() {
        return _height / _rows;
    }

    public CoordinatesModel getRandomLocation() {
        int index = new Random().nextInt(passableTiles.size());
        return passableTiles.get(index).getPixelPositionOfTheTile();
    }

    /* Getters  */
    public List<Tile> get_tile_matrix() {
        return tiles;
    }

    public Map<String, CoordinatesModel> getPlayerAndBonusPositions() {
        return playerAndBonusPositions;
    }

    public Pane getCells() {
        return cells;
    }

    public double get_width() {
        return _width;
    }

    public double get_height() {
        return _height;
    }

}
