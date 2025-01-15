package com.matthieudeglon.shooter2d.Models;

import com.matthieudeglon.shooter2d.Constants.Constants;
import com.matthieudeglon.shooter2d.Customs.CustomUncheckedException;
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

    public GameMapModel makeMapFromFileContent(String URL, double width, double height)
    {


        List<String[]> lines = readLinesFromFile(URL);
        String tileSetURL = lines.get(Constants.URL_TILESET_INDEX)[0];
        Image tileSet = getTilesetFromURL(tileSetURL);

        List<Integer> mapInfo = parseStringArrayToIntArray(lines.get(Constants.MAP_INFO_INDEX));
        int columns = mapInfo.get(0);
        int rows = mapInfo.get(1);
        int cellSide = mapInfo.get(2);

        if(lines.size() != rows + Constants.NUMBER_OF_METADATA_LINES){
            throw new CustomUncheckedException.MapFileFormatException(" Format of map is not compliant to the standard Map format ");
        }

        Set<Integer> passableCodes   = fromIntListToSet(parseStringArrayToIntArray(lines.get(Constants.PASSABLE_TILES_INDEX)));
        Set<Integer> unpassableCodes = fromIntListToSet(parseStringArrayToIntArray(lines.get(Constants.NOT_PASSABLE_TILES_FOR_P_INDEX)));

        /*   4. P1_start_X, P1_start_Y,P2_start_X, P2_start_X
             5. T1_start_X;T1_start_Y; T2_start_X, T2_start_Y */
        Map<String, CoordinatesModel> coordinateDictionary = new HashMap<>();
        fillDictionaryPosition(coordinateDictionary,Constants.PLAYER_CODE, parseStringArrayToIntArray(lines.get(Constants.SPRITE_COORD_INDEX)));
        fillDictionaryPosition(coordinateDictionary, Constants.TELEPORT_CODE, parseStringArrayToIntArray(lines.get(Constants.TELEPORT_COORD_INDEX)));

        /* 6 ... Map codes */
        List<String[]> mapTileComposition = retrieveMapWithoutMetadata(lines, Constants.NUMBER_OF_METADATA_LINES);

        return new GameMapModel(width, height,
                tileSet,cellSide,
                columns,rows,
                passableCodes,unpassableCodes,
                mapTileComposition, coordinateDictionary);
    }


    protected Set<Integer> fromIntListToSet(List<Integer> S) { return new HashSet<>(S); }

    protected List<Integer> parseStringArrayToIntArray(String[] S) {
        List<Integer> A = new ArrayList<>();
        try {
            A = Arrays.stream(S).parallel().mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        }catch (NumberFormatException e)
        {
            System.out.println("Value read cannot be parsed into an integer" + e);
        }
        return A;
    }

    protected List<String[]> readLinesFromFile(String url){
        List<String[]> rows = new ArrayList<>();
        URL filePath= Optional.ofNullable(ClassLoader.getSystemResource(url)).orElseThrow(()->
                new CustomUncheckedException.FileUrlException(" The csv file of the map [ "+ url+ " ] doesn't exist "));
        try(Stream<String> lines =
                    Files.lines(Paths.get(filePath.toURI()))){
            rows = lines.parallel().map(l -> l.split(Constants.FILE_SEPARATOR)).collect(Collectors.toList());
            if(rows.isEmpty()){ throw new CustomUncheckedException.EmptyFileException("Map file is empty"); }
        }
        catch(IOException e){ System.out.println(url + ": problems interacting with the map file \n"); e.printStackTrace();}
        catch(URISyntaxException e){ System.out.println("Wrong url format of map file \n"); e.printStackTrace();}

        return rows;
    }

    protected Image getTilesetFromURL(String URL) {
        try { return new Image(URL); }
        catch (IllegalArgumentException e)
        {
            System.out.println("Image of the tileset was not found. \n" + e);
            return new Image(URL);
        }
    }


    protected List<String[]> retrieveMapWithoutMetadata(List<String[]> lines, int numberOfRowsToSkip) {
        return lines.stream().skip(numberOfRowsToSkip).collect(Collectors.toList());
    }

    public void fillDictionaryPosition( Map<String, CoordinatesModel> dictionaryToFill, char ID, List<Integer> l){
        IntStream.range(0,l.size()).filter(i-> i%2 ==0).mapToObj(i ->
                        new Pair<>(ID+String.valueOf(i/2),new CoordinatesModel(l.get(i), l.get(i+1))))
                .forEach(pair->dictionaryToFill.put(pair.getKey(), pair.getValue()));
    }

}
