package com.pixelro.eyelab;

public class EYELAB {

    public class LOGIN {
        public static final int SUCCESS = 1;
        public static final int ERROR = -1;
        public static final int ERROR_EMAIL = -2;
        public static final int ERROR_PASS = -3;
    }

    // sharedPreferences
    public class APPDATA {
        public static final String NAME_LOGIN = "LOGIN";
        public static final String NAME_TOKEN = "TOKEN";

        public class LOGIN {
            public static final String EMAIL = "EMAIL";
            public static final String PASS = "PASS";
            public static final String SAVE_LOGIN_DATA = "SAVE_LOGIN_DATA";
            public static final String LOGINNING = "LOGINNING";
            public static final String FIRST_LOGIN = "FIRST_LOGIN";
        }


    }



}
