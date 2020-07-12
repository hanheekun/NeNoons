package com.pixelro.nenoons;

public class Profile {

    public String email = "email";
    public String password = "password";
    public String name = "name";
    public String gender = Gender.OTHER;
    public String birthday = "20200601";
    public String job = Job.OTHER;
    public String phone = "01000000000";
    public String glasses = Glasses.NONE;
    public String left = "1.0";
    public String right = "1.0";
    public String status = "";
    public String surgery = "";
    public String exercise = Excercise.NO;
    public String food = Food.NO;

    public class Gender {
        public static final String MALE = "male";
        public static final String FEMALE = "female";
        public static final String OTHER = "other";
    }

    public class Job {
        public static final String MANAGEMENT = "1";
        public static final String SERVICE = "2";
        public static final String ARTS = "3";
        public static final String OTHER = "4";
    }

    public class Glasses {
        public static final String NONE = "1";
        public static final String GLASSESS = "2";
        public static final String FAR_VISION = "3";
        public static final String CONTACT = "4";
    }

    public class Status {
        public static final String MYOPIA = "1"; // 근시
        public static final String EMMETROPIA = "2"; // 정시
        public static final String ASTIGMATISM = "3"; // 난시
        public static final String HYPEROPIA = "4"; // 원시
        public static final String UNKNOWN = "5";

    }

    public class Surgery {
        public static final String LASIKLASEK = "1"; // 시력교정 시술
        public static final String OLD = "2"; // 노안 수술
        public static final String CATARACT = "3"; // 백내장 수술
    }

    public class Excercise {
        public static final String YES = "1";
        public static final String NO = "2";
        public static final String SOMETIMES = "3";
    }

    public class Food {
        public static final String YES = "1";
        public static final String NO = "2";
        public static final String SOMETIMES = "3";
    }



}

