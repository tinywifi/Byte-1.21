package com.syuto.bytes.utils.impl.client;

import java.util.concurrent.ThreadLocalRandom;

public class ClientUtil {

    public static void crash() {
        long freeAddr = ReflectionUtil.theUnsafe.allocateMemory(1);
        ReflectionUtil.theUnsafe.freeMemory(freeAddr);
        ReflectionUtil.theUnsafe.setMemory(
                freeAddr-10 + ThreadLocalRandom.current().nextLong(-10, 11),
                ThreadLocalRandom.current().nextLong(1, 11),
                (byte) 0
        );
    }



}
