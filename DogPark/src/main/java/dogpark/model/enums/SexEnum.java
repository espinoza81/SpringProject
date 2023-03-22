package dogpark.model.enums;

import java.util.Random;

public enum SexEnum {
    M, F;

    private static final Random PRNG = new Random();

    public static SexEnum randomSex()  {
        SexEnum[] sex = values();
        return sex[PRNG.nextInt(sex.length)];
    }
}
