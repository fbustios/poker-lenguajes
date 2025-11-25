package com.poker.clientjava.repository.network;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class MessageCodec {
    public static byte[] encode(byte[] payload){
        ByteBuffer bb = ByteBuffer.allocate(4 + payload.length);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putInt(payload.length);
        bb.put(payload);
        return bb.array();
    }

    public static byte[] textToBytes(String text){
        return text.getBytes();
    }

    public static String bytesToText(byte[] payload){
        return new String(payload);
    }
}
