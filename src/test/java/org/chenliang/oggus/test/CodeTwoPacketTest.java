package org.chenliang.oggus.test;

import org.chenliang.oggus.opus.Channel;
import org.chenliang.oggus.opus.Config;
import org.chenliang.oggus.opus.OpusPacket;
import org.chenliang.oggus.opus.OpusPackets;
import org.chenliang.oggus.util.Bytes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CodeTwoPacketTest {
    @Test
    void should_create_empty_packet_correctly() {
        OpusPacket opusPacket = OpusPackets.newPacket(Config.of(12), Channel.STEREO, 2);
        assertEquals(2, opusPacket.getCode());
        assertTrue(opusPacket.isVbr());
        assertFalse(opusPacket.hasPadding());
        assertEquals(2, opusPacket.getFrameCount());
        assertEquals(0, opusPacket.getPadLenBytesSum());
    }

    @Test
    void should_dump_to_standard_and_self_delimiting_format_correctly() {
        OpusPacket opusPacket = OpusPackets.newPacket(Config.of(12), Channel.STEREO, 2);
        byte[] frameData1 = TestUtil.createBinary(513, (byte) 1);
        byte[] frameData2 = TestUtil.createBinary(514, (byte) 2);
        opusPacket.addFrame(frameData1);
        opusPacket.addFrame(frameData2);
        byte[] standardBytes = opusPacket.dumpToStandardFormat();
        assertArrayEquals(Bytes.concat(new byte[]{102, (byte) 253, 65}, frameData1, frameData2), standardBytes);
        byte[] selfDelimitingBytes = opusPacket.dumpToSelfDelimitingFormat();
        assertArrayEquals(Bytes.concat(new byte[]{102, (byte) 253, 65, (byte) 254, 65}, frameData1, frameData2),
                selfDelimitingBytes);
    }

    @Test
    void should_dump_to_binary_given_a_zero_frame_packet() {
        OpusPacket opusPacket = OpusPackets.newPacket(Config.of(12), Channel.STEREO, 2);
        assertArrayEquals(new byte[]{102, 0}, opusPacket.dumpToStandardFormat());
        assertArrayEquals(new byte[]{102, 0, 0}, opusPacket.dumpToSelfDelimitingFormat());
    }
}
