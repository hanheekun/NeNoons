package com.pixelro.nenoons;

public class PersonalProfile {

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
    public String sns_ID = "";

    public class Gender {
        public static final String MALE = "MALE";
        public static final String FEMALE = "FEMALE";
        public static final String OTHER = "OTHER";
    }

    public class Job {
        public static final String MANAGEMENT = "MANAGEMENT";
        public static final String SERVICE = "SERVICE";
        public static final String ARTS = "ARTS";
        public static final String OTHER = "OTHER";
    }

    public class Glasses {
        public static final String NONE = "NONE";
        public static final String GLASSESS = "GLASSESS";
        public static final String FAR_VISION = "FAR_VISION";
        public static final String CONTACT = "CONTACT";
    }

    public class Status {
        public static final String MYOPIA = "MYOPIA"; // 근시
        public static final String EMMETROPIA = "EMMETROPIA"; // 정시
        public static final String ASTIGMATISM = "ASTIGMATISM"; // 난시
        public static final String HYPEROPIA = "HYPEROPIA"; // 원시
        public static final String UNKNOWN = "UNKNOWN";

    }

    public class Surgery {
        public static final String LASIKLASEK = "LASIKLASEK"; // 시력교정 시술
        public static final String OLD = "OLD"; // 노안 수술
        public static final String CATARACT = "CATARACT"; // 백내장 수술
    }

    public class Excercise {
        public static final String YES = "YES";
        public static final String NO = "NO";
        public static final String SOMETIMES = "SOMETIMES";
    }

    public class Food {
        public static final String YES = "YES";
        public static final String NO = "NO";
        public static final String SOMETIMES = "SOMETIMES";
    }



}

