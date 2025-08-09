package io.gomint.server.network.compression;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Compression {

    public static ByteBuf compress(ByteBuf rawBuffer) {
        if (!rawBuffer.isReadable()) {
            return Unpooled.EMPTY_BUFFER;
        }

        Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION, true);

        byte[] rawData;
        int length = rawBuffer.readableBytes();

        if (rawBuffer.hasArray()) {
            rawData = rawBuffer.array();
            deflater.setInput(rawData, rawBuffer.arrayOffset() + rawBuffer.readerIndex(), length);
        } else {
            rawData = new byte[length];
            rawBuffer.getBytes(rawBuffer.readerIndex(), rawData);
            deflater.setInput(rawData);
        }

        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];

        try {
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer);
                outputStream.write(buffer, 0, count);
            }
        } finally {
            deflater.end();
        }

        return Unpooled.wrappedBuffer(outputStream.toByteArray());
    }

    public static ByteBuf decompress(ByteBuf compressedBuffer) throws DataFormatException {
        if (!compressedBuffer.isReadable()) {
            return Unpooled.EMPTY_BUFFER;
        }

        Inflater inflater = new Inflater(true);

        byte[] compressedData;
        int length = compressedBuffer.readableBytes();

        if (compressedBuffer.hasArray()) {
            compressedData = compressedBuffer.array();
            inflater.setInput(compressedData, compressedBuffer.arrayOffset() + compressedBuffer.readerIndex(), length);
        } else {
            compressedData = new byte[length];
            compressedBuffer.getBytes(compressedBuffer.readerIndex(), compressedData);
            inflater.setInput(compressedData);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];

        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                if (count == 0 && inflater.needsInput()) {
                    break;
                }
                outputStream.write(buffer, 0, count);
            }
        } finally {
            inflater.end();
        }

        return Unpooled.wrappedBuffer(outputStream.toByteArray());
    }
}
