package fr.fsh.videoConverter.service;

import java.nio.file.Path;

public interface DirectoryPollingService {

    public void startPolling(Path directory, VideoEncoderSettings settings);
}
