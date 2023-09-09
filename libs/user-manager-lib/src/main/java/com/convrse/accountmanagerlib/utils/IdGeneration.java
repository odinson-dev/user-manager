package com.convrse.accountmanagerlib.utils;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

public class IdGeneration {

    public static String generateNanoId() {
        return NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 12);
    }

}
