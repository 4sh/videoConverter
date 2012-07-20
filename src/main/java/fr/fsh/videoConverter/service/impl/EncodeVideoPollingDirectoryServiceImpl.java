package fr.fsh.videoConverter.service.impl;

import fr.fsh.videoConverter.service.VideoEncoderService;
import fr.fsh.videoConverter.service.VideoEncoderSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class EncodeVideoPollingDirectoryServiceImpl extends AbstractDirectoryPollingServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(EncodeVideoPollingDirectoryServiceImpl.class);

    @Override
    protected void onCreateDoSomeStuff(VideoEncoderSettings settings, Path filename) {
        Path child = settings.inputDirPath().resolve(filename);
        File sourceFile = null;
        try {
            sourceFile = child.toFile();
            VideoEncoderService encoderService = new VideoH264EncoderServiceImpl(settings);
            encoderService.encodeVideo(sourceFile);
            Files.deleteIfExists(Paths.get(sourceFile.getAbsolutePath()));
        } catch (Exception e) {
            logger.error("Error occurred during encoding", e);
            try {
                Files.move(Paths.get(sourceFile.getAbsolutePath()), Paths.get(settings.errorDirPath().toAbsolutePath().toString(), filename.toFile().getName()), StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException e1) {
                logger.error("Error occurred during move into error dir", e);
            }
        }
    }
}
