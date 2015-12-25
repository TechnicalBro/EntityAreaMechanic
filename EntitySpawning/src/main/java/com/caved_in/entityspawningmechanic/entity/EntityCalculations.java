package com.caved_in.entityspawningmechanic.entity;

import com.caved_in.commons.utilities.NumberUtil;

import java.util.Random;

public class EntityCalculations {
    private static Random random = new Random();
    public static int generateHealth(int entityLevel, boolean isElite, boolean isBoss) {
        short multiplier = 1;
        multiplier += (isElite ? 2 : 0);
        multiplier += (isBoss ? 4 : 0);
        int baseHealth = (int) (10 + Math.pow(entityLevel, 1.5) * 5);
        int health = (int) (vrtns(baseHealth, baseHealth / 20));
        return health * multiplier;
    }

    //Calculates the variations in health of mobs that are the same lvl
    public static double vrtns(double base, double range) {
        double variant;

        if (NumberUtil.percentCheck(10)) {
            random.setSeed(System.currentTimeMillis());
        }

        boolean addOrSub = random.nextBoolean();
        if (addOrSub) {
            variant = (base + range * random.nextDouble());
        } else {
            variant = (base - range * random.nextDouble());
        }
        return variant;
    }
}



