package fr.fsh.videoConverter;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import fr.fsh.videoConverter.common.CliOptions;
import fr.fsh.videoConverter.service.DirectoryPollingService;
import fr.fsh.videoConverter.service.VideoEncoderSettings;
import fr.fsh.videoConverter.service.impl.EncodeVideoPollingDirectoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {

        CliOptions options = new CliOptions();
        try {
            new JCommander(options, args);
        } catch (ParameterException e) {
            System.err.println(e.getMessage());
            new JCommander(options).usage();
            System.exit(-1);
        }

        // Loads and check command line options
        VideoEncoderSettings videoEncoderSettings = getVideoEncoderSettings(options);
        videoEncoderSettings.ffmpegPresets(Paths.get(options.presetsFile()));

        // Build video encoder service
        DirectoryPollingService pollingService = new EncodeVideoPollingDirectoryServiceImpl();
        pollingService.startPolling(videoEncoderSettings.inputDirPath(), videoEncoderSettings);
    }

    private static VideoEncoderSettings getVideoEncoderSettings(CliOptions options) {
        if (options == null) {
            logger.warn("No options specified to build video encoder settings");
            return null;
        }
        String inputDir = options.inputDir();
        if (inputDir == null) {
            logger.error("Input directory is null");
            throw new IllegalArgumentException("Input directory is null");
        }
        if (!Files.exists(Paths.get(inputDir))) {
            logger.error("Cannot find input directory: {}", inputDir);
            throw new IllegalArgumentException("Cannot find input directory");
        }

        String outputDir = options.outputDir();
        if (outputDir == null) {
            logger.error("Output directory is null");
            throw new IllegalArgumentException("Output directory is null");
        }
        if (!Files.exists(Paths.get(outputDir))) {
            logger.error("Cannot find output directory: {}", outputDir);
            throw new IllegalArgumentException("Cannot find output directory");
        }
        
        String errorDir = options.errorDir();
        if (errorDir == null) {
            logger.error("Error directory is null");
            throw new IllegalArgumentException("Error directory is null");
        }
        if (!Files.exists(Paths.get(errorDir))) {
            logger.error("Cannot find error directory: {}", errorDir);
            throw new IllegalArgumentException("Cannot find error directory");
        }
        return new VideoEncoderSettings()
                .inputDirPath(Paths.get(inputDir))
                .outputDirPath(Paths.get(outputDir))
                .errorDirPath(Paths.get(errorDir));
    }

}
