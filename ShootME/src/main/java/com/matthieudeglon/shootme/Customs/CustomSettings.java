package com.matthieudeglon.shootme.Customs;

public class CustomSettings {

    public static final int DEFAULT_X = 1000;
    public static final int DEFAULT_Y = 600;

    public static final int NUMBER_OF_METADATA_LINES = 6;
    public static final String FILE_SEPARATOR = ",";
    public static final int URL_TILESET_INDEX = 0;
    public static final int MAP_INFO_INDEX = 1;
    public static final int PASSABLE_TILES_INDEX = 2;
    public static final int NOT_PASSABLE_TILES_FOR_P_INDEX = 3;
    public static final int SPRITE_COORD_INDEX = 4;
    public static final int TELEPORT_COORD_INDEX = 5;

    public static final char PLAYER_CODE = 'P';
    public static final char TELEPORT_CODE = 'T';


    public static final int PLAYER_SPEED = 3;
    public static final int PROJECTILE_SPEED = 7;

    public static final double PLAYER_SCALE = 1.0 / 20;
    public static final double PROJECTILE_SCALE = 1.0 / 19;
    public static final double TELEPORT_SCALE = 1.0 / 18;
    public static final double HEART_SCALE = 1.0 / 6;

    public static final double WASD_SCALE = .5;
    public static final double ARROWS_SCALE = .4;
    public static final double MAP_SCALE = 1;

    public static final int TELEPORT_TIME_TO_ROTATE = 3000;


    public static final int BONUS_COOLDOWN = 5;

    public static final double SHOOTING_COOLDOWN = .5;


    public static final double HB_PROPORTIONAL_WIDTH = 0.1;
    public static final double PERCENTAGE_DAMAGE_PER_SHOOT = .26;

    public static final String URL_TELEPORT = "teleport.png";
    public static final String URL_PROJECTILE = "cannon_ball.png";
    public static final String URL_HEART = "bonus.png";

    public static final String URL_ARTIST = "soldier.png";
    public static final String URL_WARRIOR = "knight.png";
    public static final String URL_ASTROLOGER = "wizard.png";

    public static final String URL_MAP_ISLAND_PNG = "map_island.png";
    public static final String URL_MAP_DESERT_PNG = "map_desert.png";

    public static final String URL_MAP_ISLAND_CSV = "map_islands.csv";
    public static final String URL_MAP_DESERT_CSV = "map_desert.csv";

    public static final String URL_COMMANDS_P1 = "WASD.png";
    public static final String URL_COMMANDS_P2 = "arrows.png";

    public static final String URL_BACKGROUND_DARK = "menu_dark.jpeg";
    public static final String URL_BACKGROUND_LIGHT = "Toscana2.jpg";


    public static final String URL_WARNING_ICON = "alert.png";
    public static final String URL_FIREWORKS = "fireworks.png";

    public static final String URL_CONFIG_FILE = "config.ini";


    public static final String SOLDIER = "SOLDIER";
    public static final String KNIGHT = "KNIGHT";
    public static final String WIZARD = "WIZARD";

    public static final String DESERT = "DUNE";
    public static final String ISLAND = "PLAIN";

    public static final String WINDOW_NAME = "SHOOT ME";

}
