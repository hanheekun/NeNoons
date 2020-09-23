package com.pixelro.nenoons;

public class ExProfile {

    public String date = "20200701000000";
    public int type = Type.TYPE_1;
    public int level = Level.LEVER_1;
    public int reserved1 = 0;
    public int reserved2 = 0;

    public class Type {
        public static final int TYPE_1 = 1;
        public static final int TYPE_2 = 2;
        public static final int TYPE_3 = 3;
        public static final int TYPE_4 = 4;
    }

    public class Level {
        public static final int LEVER_1 = 1;
        public static final int LEVER_2 = 2;
        public static final int LEVER_3 = 3;
    }
}

