package fr.fsh.videoConverter.service.impl;

import com.xuggle.xuggler.Converter;
import fr.fsh.videoConverter.service.VideoEncoderService;
import fr.fsh.videoConverter.service.VideoEncoderSettings;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;

public class VideoH264EncoderServiceImpl implements VideoEncoderService {

    private static final Logger logger = LoggerFactory.getLogger(VideoH264EncoderServiceImpl.class);

    private final VideoEncoderSettings settings;

    public VideoH264EncoderServiceImpl(VideoEncoderSettings settings) {
        this.settings = settings;
    }

    private String getEncodedFilename(String sourceFilename) {
        if (sourceFilename == null || sourceFilename.isEmpty()) {
            throw new IllegalArgumentException("Failed to get the target encoded file name from: " + sourceFilename);
        }
        String root = sourceFilename;
        int extIndex = sourceFilename.lastIndexOf(".");
        if (extIndex != -1) {
            root = sourceFilename.substring(0, extIndex);
        }
        return root + ".mp4";
    }
    
    
    public void encodeVideo(File inputFile) throws ParseException {

        Converter converter = new Converter();

        //These are the arguments to pass to the converter object.
        //For H264 transcoding, the -vpreset option is very
        //important. Here, presetsFile is a File object corresponding
        //to a libx264 video presets file. These are in the
        // /usr/local/share/ffmpeg directory.

        logger.info("Start conversion for {}", inputFile.getName());
        String targetFilename = null;
        try {
            targetFilename = getEncodedFilename(inputFile.getName());
        } catch (IllegalArgumentException e) {
            targetFilename = new Date().toString() + ".mp4";
        }

        String[] arguments = {
                inputFile.getAbsolutePath(),
                "-asamplerate", "44100",
               // "-acodec", "libfaac",
                "-vcodec", "libx264",
                "-vpreset", settings.ffmpegPresets().toAbsolutePath().toString(),
                settings.outputDirPath().toAbsolutePath() + "/" + targetFilename
        };
        converter.run(converter.parseOptions(converter.defineOptions(), arguments));
        logger.info("Conversion of {} was successful, result: {}", inputFile.getName(), settings.outputDirPath().toAbsolutePath() + "/" + targetFilename);
    }
}
