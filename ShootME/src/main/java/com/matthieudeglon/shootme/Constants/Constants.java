package com.matthieudeglon.shootme.Constants;

public class Constants {

    /*  METADATA IN MAP */
    public static final int NUMBER_OF_METADATA_LINES = 6;
    public static final String FILE_SEPARATOR = ",";
    public static final int URL_TILESET_INDEX = 0;
    public static final int MAP_INFO_INDEX = 1;
    public static final int PASSABLE_TILES_INDEX = 2;
    public static final int NOT_PASSABLE_TILES_FOR_P_INDEX = 3;
    public static final int SPRITE_COORD_INDEX = 4;
    public static final int TELEPORT_COORD_INDEX = 5;

    /* CODES */
    public static final char PLAYER_CODE = 'P';
    public static final char TELEPORT_CODE = 'T';

    /* SPEEDS */
    public static final int PROJECTILE_SPEED = 7;

    /*  SCALES */
    public static final double PROJECTILE_SCALE = 1.0 / 19;
    public static final double TELEPORT_SCALE = 1.0 / 18;
    public static final double HEART_SCALE = 1.0 / 6;

    /* TELEPORT */
    public static final int TELEPORT_TIME_TO_ROTATE = 3000;

    /* BONUS */
    public static final int BONUS_COOLDOWN = 5;

    /* HEALTHBAR */
    public static final double HB_PROPORTIONAL_WIDTH = 0.1;
    public static final double PERCENTAGE_DAMAGE_PER_SHOOT = .26;

    /*  URLS */
    public static final String URL_CONFIG_FILE = "config.ini";

}
