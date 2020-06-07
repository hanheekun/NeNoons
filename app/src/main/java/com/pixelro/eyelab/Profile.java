package com.pixelro.eyelab;

public class Profile {

    public String email = "email";
    public String pass = "pass";
    public String name = "name";
    public String gender = Gender.OTHER;
    public int birthday = 20200601;
    public String job = Job.OTHER;
    public String phone = "01000000000";



    public class Gender {        public static final String MALE = "male";
        public static final String FEMALE = "female";
        public static final String OTHER = "other";
    }

    public class Job {
        public static final String MANAGEMENT = "management";
        public static final String SERVICE = "service";
        public static final String ARTS = "arts";
        public static final String OTHER = "other";
    }

}

