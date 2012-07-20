package fr.fsh.videoConverter.service.impl;

import fr.fsh.videoConverter.service.DirectoryPollingService;
import fr.fsh.videoConverter.service.VideoEncoderSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;

public abstract class AbstractDirectoryPollingServiceImpl implements DirectoryPollingService {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDirectoryPollingServiceImpl.class);
    
    @Override
    public void startPolling(Path directory, VideoEncoderSettings settings) {
        WatchService watcher = null;
        
        try {
            watcher = FileSystems.getDefault().newWatchService();
            try {
                directory.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
            } catch (IOException e) {
                logger.error("Register failed", e);
            }
        } catch (IOException e) {
            logger.error("WatchService instantiation failed", e);
        }

        while (true) {
            // wait for key to be signaled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException e) {
                logger.error("Watcher take exception", e);
                return;
            }
            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                // This key is registered only
                // for ENTRY_CREATE events,
                // but an OVERFLOW event can
                // occur regardless if events
                // are lost or discarded.
                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }

                // The filename is the
                // context of the event.
                WatchEvent<Path> ev = (WatchEvent<Path>)event;
                Path filename = ev.context();

                // Resolve the filename
                // against the directory.
                // If the filename is "test"
                // and the directory is "foo",
                // the resolved name is "test/foo".
                onCreateDoSomeStuff(settings, filename);
            }

            // Reset the key -- this step
            // is critical if you want to
            // receive further watch events.
            // If the key is no longer valid,
            // the directory is inaccessible
            // so exit the loop.
            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }
    
    abstract protected void onCreateDoSomeStuff(VideoEncoderSettings settings, Path filename);
}
