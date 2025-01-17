package com.matthieudeglon.shootme.Models;

import com.matthieudeglon.shootme.Customs.CustomCheckedException;
import com.matthieudeglon.shootme.Customs.CustomSettings;
import com.matthieudeglon.shootme.Direction;
import com.matthieudeglon.shootme.Server.GameServer;
import com.matthieudeglon.shootme.Views.GameMenu;
import com.matthieudeglon.shootme.Views.WinnerWindow;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Simulation extends Application {

    private final Pane root = new Pane();
    private Stage _stage;

    private Character Player_1;
    private Character Player_2;

    private Scene _scene;
    private AnimationTimer _timer;

    private double _height;
    private double _width;

    private Pair<Double, Double> scaling_factors;

    private GameMapModel _gameMap;

    private final List<String> _playersNames;
    private final List<String> _playersUrlsSprite;
    private final List<String> _mapUrl;

    private GameServer gameServer;


    public Simulation(List<String> players_names, List<String> players_urls_sprite, List<String> map_url) {
        this._playersNames = players_names;
        this._playersUrlsSprite = players_urls_sprite;
        this._mapUrl = map_url;
    }


    private void createContent() {
        create_frame();
        create_map();
        create_players();
        create_teleports();
        create_bonus();
    }

    private void create_bonus() {
        new Bonus(root, _gameMap, CustomSettings.URL_HEART, 1, 10, 10, scaling_factors);
    }

    private void create_teleports() {
        var T1 = new Teleporter(root, CustomSettings.URL_TELEPORT, _gameMap, scaling_factors, "" + CustomSettings.TELEPORT_CODE + '0');
        var T2 = new Teleporter(root, CustomSettings.URL_TELEPORT, _gameMap, scaling_factors, "" + CustomSettings.TELEPORT_CODE + '1');

        T1.setDestination(T2);
        T2.setDestination(T1);

    }

    private void create_map() {
        var MR = new MapReaderModel();
        _gameMap = MR.makeMapFromFileContent(getMapUrl(), _width, _height);
        root.getChildren().add(_gameMap.getCells());
    }

    private void create_players() {
        Player_1 = new Character(root, _gameMap, scaling_factors, getIUrlsSprite(0), 4, 1, "" + CustomSettings.PLAYER_CODE + '0', Direction.RIGHT, getIPlayerName(0));
        Player_2 = new Character(root, _gameMap, scaling_factors, getIUrlsSprite(1), 4, 1, "" + CustomSettings.PLAYER_CODE + '1', Direction.LEFT, getIPlayerName(1));
    }

    private void create_frame() {
        _width = _stage.getWidth();
        _height = _stage.getHeight();

        scaling_factors = new Pair<>(_width / CustomSettings.DEFAULT_X, _height / CustomSettings.DEFAULT_Y);
    }

    public Scene getScene() {
        return _scene;
    }

    public void start(Stage stage) {
        _stage = stage;
        stage.centerOnScreen();

        _stage.setTitle(CustomSettings.WINDOW_NAME);
        _stage.setResizable(false);

        createContent();
        _scene = new Scene(root);

        gameServer = new GameServer(12345);
        gameServer.start();

        GAME();

        addKeyHandler_PRESS(_scene, Player_1, Player_2);
        addKeyHandler_RELEASED(_scene, Player_1, Player_2);

        _stage.setScene(_scene);
        _stage.show();
    }

    private void GAME() {
        _timer = new AnimationTimer() {
            private long last_update = 0;

            @Override
            public void handle(long now) {
                if (now - last_update >= 4_000_000) {
                    all_sprites().forEach(
                            s -> {
                                if (s instanceof DynamicObject) ((DynamicObject) s).defaultMovement(_gameMap);
                                all_players().forEach(s::action);
                            });
                    try {
                        cleanDeadObjectsAndCheckForVictory();
                    } catch (CustomCheckedException.MissingMenuComponentException e) {
                        throw new RuntimeException(e);
                    }
                    last_update = now;
                }
            }
        };

        startSimulation();
    }

    private void cleanDeadObjectsAndCheckForVictory() throws CustomCheckedException.MissingMenuComponentException {
        root.getChildren().removeIf(node -> (node instanceof PicturedObject) && ((PicturedObject) node).hasToBeRemoved());
        if (all_players().size() == 1) launch_winner_window(all_players().iterator().next());
    }

    protected List<PicturedObject> all_sprites() {
        return root.getChildren().stream().parallel().filter(i -> i instanceof PicturedObject).map(n -> (PicturedObject) n).collect(Collectors.toList());
    }

    protected Set<Character> all_players() {
        return all_sprites().stream().filter(i -> i instanceof Character).map(n -> (Character) n).collect(Collectors.toSet());
    }


    private void launch_winner_window(Character winner) throws CustomCheckedException.MissingMenuComponentException {
        var win_screen = new WinnerWindow(winner);
        stopSimulation();
        win_screen.start(_stage);
    }

    private void addKeyHandler_PRESS(Scene scene, Character s, Character p) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, ke -> {
            switch (ke.getCode()) {
                case UP -> {
                    p.setGoNorth(true);
                    gameServer.broadcast("Player 2 moved UP");
                }
                case DOWN -> {
                    p.setGoSouth(true);
                    gameServer.broadcast("Player 2 moved DOWN");
                }
                case LEFT -> {
                    p.setGoWest(true);
                    gameServer.broadcast("Player 2 moved LEFT");
                }
                case RIGHT -> {
                    p.setGoEast(true);
                    gameServer.broadcast("Player 2 moved RIGHT");
                }
                case ENTER -> {
                    p.shoot(root);
                    gameServer.broadcast("Player 2 shooted");
                }

                case Z -> {
                    s.setGoNorth(true);
                    gameServer.broadcast("Player 1 moved UP");
                }
                case S -> {
                    s.setGoSouth(true);
                    gameServer.broadcast("Player 1 moved DOWN");
                }
                case Q -> {
                    s.setGoWest(true);
                    gameServer.broadcast("Player 1 moved LEFT");
                }
                case D -> {
                    s.setGoEast(true);
                    gameServer.broadcast("Player 1 moved RIGHT");
                }
                case SPACE -> {
                    s.shoot(root);
                    gameServer.broadcast("Player 1 shooted");
                }

                case ESCAPE -> {
                    try {
                        handleEscape();
                    } catch (CustomCheckedException.MissingMenuComponentException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void addKeyHandler_RELEASED(Scene scene, Character p, Character s) {
        scene.addEventHandler(KeyEvent.KEY_RELEASED, ke -> {
            {
                switch (ke.getCode()) {
                    case UP -> s.setGoNorth(false);
                    case DOWN -> s.setGoSouth(false);
                    case LEFT -> s.setGoWest(false);
                    case RIGHT -> s.setGoEast(false);

                    case Z -> p.setGoNorth(false);
                    case S -> p.setGoSouth(false);
                    case Q -> p.setGoWest(false);
                    case D -> p.setGoEast(false);
                }
            }
        });
    }

    private void handleEscape() throws CustomCheckedException.MissingMenuComponentException {
        var game_menu = new GameMenu(this);
        stopSimulation();
        try {
            game_menu.readProperties();
        } catch (CustomCheckedException.FileManagementException e) {
            System.out.println(e.getMessage() + " Using default settings.");
        }
        game_menu.start(_stage);
        startSimulation();
    }


    public static void main(String[] args) {
        launch(args);
    }


    public double get_height() {
        return _height;
    }

    public double get_width() {
        return _width;
    }

    public Pane getRoot() {
        return root;
    }

    public Character getPlayer_1() {
        return Player_1;
    }

    public Character getPlayer_2() {
        return Player_2;
    }

    public GameMapModel get_gameMap() {
        return _gameMap;
    }

    public String getIPlayerName(int index) {
        return _playersNames.get(index);
    }

    public String getIUrlsSprite(int index) {
        return _playersUrlsSprite.get(index);
    }

    public String getMapUrl() {
        return _mapUrl.get(0);
    }

    public void startSimulation() {
        _timer.start();
    }

    public void stopSimulation() {
        _timer.stop();
    }

}

