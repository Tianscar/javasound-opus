package me.chenleon.media.audio.opus;

import me.chenleon.media.TestUtil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OpusPacketTest {
    @ParameterizedTest
    @CsvSource({"0,1", "1,2", "2,2"})
    void should_throw_exception_when_adding_frame_to_code_0_to_2_packet_with_full_of_frames(int code, int frameCount) {
        OpusPacket opusPacket = OpusPackets.newPacketOfCode(code);
        for (int i = 0; i < frameCount; i++) {
            opusPacket.addFrame(TestUtil.createBinary(1, (byte) 0));
        }
        InvalidOpusException exception = assertThrows(InvalidOpusException.class, () -> {
            opusPacket.addFrame(TestUtil.createBinary(1, (byte) 0));
        });
        assertEquals("The number of frames reaches limitation", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 10})
    void should_throw_exception_when_adding_frame_to_code_3_packet_with_full_of_frames(int frameCount) {
        OpusPacket opusPacket = OpusPackets.newPacketOfCode(3);
        opusPacket.setFrameCount(frameCount);
        for (int i = 0; i < frameCount; i++) {
            opusPacket.addFrame(TestUtil.createBinary(1, (byte) 0));
        }
        InvalidOpusException exception = assertThrows(InvalidOpusException.class, () -> {
            opusPacket.addFrame(TestUtil.createBinary(1, (byte) 0));
        });
        assertEquals("The number of frames reaches limitation", exception.getMessage());
    }
}
