package com.matthieudeglon.shootme.Models;

import com.matthieudeglon.shootme.Constants.Constants;
import com.matthieudeglon.shootme.Customs.CustomSettings;
import com.matthieudeglon.shootme.Customs.CustomUncheckedException;
import javafx.scene.image.Image;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class MapReaderModel {

    public GameMapModel makeMapFromFileContent(String URL, double width, double height) {


        List<String[]> lines = readLinesFromFile(URL);

        String tileSetURL = lines.get(CustomSettings.URL_TILESET_INDEX)[0];
        Image tileSet = getTilesetFromURL(tileSetURL);

        List<Integer> mapInfo = parseStringArrayToIntArray(lines.get(CustomSettings.MAP_INFO_INDEX));
        int columns = mapInfo.get(0);
        int rows = mapInfo.get(1);
        int cellSide = mapInfo.get(2);

        if (lines.size() != rows + CustomSettings.NUMBER_OF_METADATA_LINES) {
            throw new CustomUncheckedException.MapFileFormatException(" Format of map is not compliant to the standard Map format ");
        }

        Set<Integer> passableCodes = fromIntListToSet(parseStringArrayToIntArray(lines.get(CustomSettings.PASSABLE_TILES_INDEX)));
        Set<Integer> unpassableCodes = fromIntListToSet(parseStringArrayToIntArray(lines.get(CustomSettings.NOT_PASSABLE_TILES_FOR_P_INDEX)));


        Map<String, CoordinatesModel> coordinateDictionary = new HashMap<>();
        fillDictionaryPosition(coordinateDictionary, CustomSettings.PLAYER_CODE, parseStringArrayToIntArray(lines.get(CustomSettings.SPRITE_COORD_INDEX)));
        fillDictionaryPosition(coordinateDictionary, CustomSettings.TELEPORT_CODE, parseStringArrayToIntArray(lines.get(CustomSettings.TELEPORT_COORD_INDEX)));

        List<String[]> mapTileComposition = retrieveMapWithoutMetadata(lines, CustomSettings.NUMBER_OF_METADATA_LINES);

        return new GameMapModel(width, height,
                tileSet, cellSide,
                columns, rows,
                passableCodes, unpassableCodes,
                mapTileComposition, coordinateDictionary);
    }


    protected Set<Integer> fromIntListToSet(List<Integer> S) {
        return new HashSet<>(S);
    }

    public List<Integer> parseStringArrayToIntArray(String[] S) {
        List<Integer> A = new ArrayList<>();
        try {
            A = Arrays.stream(S).parallel().mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        } catch (NumberFormatException e) {
            System.out.println("Value read cannot be parsed into an integer" + e);
        }
        return A;
    }

    public List<String[]> readLinesFromFile(String url) {
        List<String[]> rows = new ArrayList<>();
        URL filePath = Optional.ofNullable(ClassLoader.getSystemResource(url)).orElseThrow(() ->
                new CustomUncheckedException.FileUrlException(" The csv file of the map [ " + url + " ] doesn't exist "));
        try (Stream<String> lines =
                     Files.lines(Paths.get(filePath.toURI()))) {
            rows = lines.parallel().map(l -> l.split(CustomSettings.FILE_SEPARATOR)).collect(Collectors.toList());
            if (rows.isEmpty()) {
                throw new CustomUncheckedException.EmptyFileException("Map file is empty");
            }
        } catch (IOException e) {
            System.out.println(url + ": problems interacting with the map file \n");
            e.printStackTrace();
        } catch (URISyntaxException e) {
            System.out.println("Wrong url format of map file \n");
            e.printStackTrace();
        }

        return rows;
    }

    public Image getTilesetFromURL(String URL) {
        try {
            return new Image(URL);
        } catch (IllegalArgumentException e) {
            System.out.println("Image of the tileset was not found. \n" + e);
            return new Image(URL);
        }
    }


    public List<String[]> retrieveMapWithoutMetadata(List<String[]> lines, int numberOfRowsToSkip) {
        return lines.stream().skip(numberOfRowsToSkip).collect(Collectors.toList());
    }

    public void fillDictionaryPosition(Map<String, CoordinatesModel> dictionaryToFill, char ID, List<Integer> l) {
        IntStream.range(0, l.size()).filter(i -> i % 2 == 0).mapToObj(i ->
                        new Pair<>(ID + String.valueOf(i / 2), new CoordinatesModel(l.get(i), l.get(i + 1))))
                .forEach(pair -> dictionaryToFill.put(pair.getKey(), pair.getValue()));
    }

}