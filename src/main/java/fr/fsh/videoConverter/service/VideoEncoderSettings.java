package fr.fsh.videoConverter.service;

import java.nio.file.Path;

public class VideoEncoderSettings {

    private Path inputDirPath;
    private Path outputDirPath;
    private Path errorDirPath;
    private Path ffmpegPresets;

    public VideoEncoderSettings inputDirPath(Path _inputDirPath){
        this.inputDirPath = _inputDirPath;
        return this;
    }

    public Path inputDirPath(){
        return this.inputDirPath;
    }

    public VideoEncoderSettings outputDirPath(Path _outputDirPath){
        this.outputDirPath = _outputDirPath;
        return this;
    }

    public Path outputDirPath(){
        return this.outputDirPath;
    }

    public VideoEncoderSettings errorDirPath(Path _errorDirPath){
        this.errorDirPath = _errorDirPath;
        return this;
    }

    public Path errorDirPath(){
        return this.errorDirPath;
    }

    public VideoEncoderSettings ffmpegPresets(Path _ffmpegPresets){
        this.ffmpegPresets = _ffmpegPresets;
        return this;
    }

    public Path ffmpegPresets(){
        return this.ffmpegPresets;
    }
}
