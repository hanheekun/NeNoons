package com.pixelro.nenoons;

public class TestProfile {

    public String date = "20200701000000";
    public int distance = 0;
    public int redgreen = Redgreen.RED;
    public int background = Background.BACKGROUND_1;
    public int font = Font.FONT_1;
    public int bright = Bright.BRIGHT_1;
    public int reserved1 = 0;
    public int reserved2 = 0;

    public class Redgreen {
        public static final int RED = 1;
        public static final int GREEN = 2;
    }

    public class Background {
        public static final int BACKGROUND_1 = 1;
        public static final int BACKGROUND_2 = 2;
        public static final int BACKGROUND_3 = 3;
        public static final int BACKGROUND_4 = 4;
        public static final int BACKGROUND_5 = 5;
        public static final int BACKGROUND_6 = 6;
    }

    public class Font {
        public static final int FONT_1 = 1;
        public static final int FONT_2 = 2;
        public static final int FONT_3 = 3;
        public static final int FONT_4 = 4;
        public static final int FONT_5 = 5;
    }

    public class Bright {
        public static final int BRIGHT_1 = 1;
        public static final int BRIGHT_2 = 20;
        public static final int BRIGHT_3 = 300;
        public static final int BRIGHT_4 = 4000;
        public static final int BRIGHT_5 = 50000;
        public static final int BRIGHT_6 = 600000;

    }
}

