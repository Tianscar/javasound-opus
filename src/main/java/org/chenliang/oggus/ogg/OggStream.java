package org.chenliang.oggus.ogg;

import org.chenliang.oggus.util.IOUtil;
import org.chenliang.oggus.util.LittleEndianDataInputStream;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * An Ogg stream is made up of a sequence of Ogg pages. An Ogg stream stream could be multiplexed by several logical
 * Ogg stream which could be identified with the {@code serialNum}.
 */
public class OggStream {
    private LittleEndianDataInputStream in;

    private OggStream(InputStream inputStream) {
        this.in = new LittleEndianDataInputStream(inputStream);
    }

    /**
     * Create {@code OggStream} from a file.
     *
     * @param filePath path of an Ogg file
     * @throws FileNotFoundException if the Ogg file doesn't exist.
     * @return OggStream
     */
    public static OggStream from(String filePath) throws FileNotFoundException {
        return new OggStream(new BufferedInputStream(new FileInputStream(filePath)));
    }

    /**
     * Create {@code OggStream} from an {@code InputStream}.
     *
     * @param inputStream the underlying input stream.
     * @return OggStream
     */
    public static OggStream from(InputStream inputStream) {
        return new OggStream(inputStream);
    }

    /**
     * Read an Ogg page.
     * This method will skip invalid data.
     *
     * @return the next Ogg page, or {@code null} if there isn't page left
     * @throws IOException if an I/O error occurs
     */
    public OggPage readPage() throws IOException {
        if (hasNextPage()) {
            return nextPage();
        }
        return null;
    }

    /**
     * Read an Ogg page with the given serial number
     * This method will skip invalid data.
     *
     * @param serialNum the given serial number
     * @return the next Ogg page, or {@code null} if there isn't page left
     * @throws IOException if an I/O error occurs
     */
    public OggPage readPage(long serialNum) throws IOException {
        while (hasNextPage()) {
            OggPage oggPage = nextPage();
            if (oggPage.getSerialNum() == serialNum) {
                return oggPage;
            }
        }
        return null;
    }

    private boolean hasNextPage() throws IOException {
        int posOfPattern = 0;
        while (posOfPattern < OggPage.CAPTURE_PATTERN.length) {
            int b = in.read();
            if (b == -1) {
                return false;
            }
            if (b == OggPage.CAPTURE_PATTERN[posOfPattern]) {
                posOfPattern++;
            } else {
                posOfPattern = (b == OggPage.CAPTURE_PATTERN[0] ? 1 : 0);
            }
        }
        return true;
    }

    private OggPage nextPage() throws IOException {
        OggPage oggPage = OggPage.empty();
        int version = in.readUnsignedByte();
        if (version != 0) {
            throw new InvalidOggException("Unsupported Ogg page version: " + version);
        }
        oggPage.setFlag(in.readUnsignedByte());
        oggPage.setGranulePosition(in.readLong());
        oggPage.setSerialNum(Integer.toUnsignedLong(in.readInt()));
        oggPage.setSeqNum(Integer.toUnsignedLong(in.readInt()));
        oggPage.setCheckSum(in.readInt());
        int segCount = in.readUnsignedByte();
        byte[] laceValues = IOUtil.readNBytes(in, segCount);

        int packetLen = 0;
        for (byte laceValue : laceValues) {
            int segLen = Byte.toUnsignedInt(laceValue);
            packetLen += segLen;
            if (segLen < OggPage.MAX_LACE_VALUE) {
                byte[] data = IOUtil.readNBytes(in, packetLen);
                oggPage.addDataPacket(data);
                packetLen = 0;
            }
        }
        if (packetLen != 0) {
            byte[] data = IOUtil.readNBytes(in, packetLen);
            oggPage.addPartialDataPacket(data);
        }
        return oggPage;
    }
}
