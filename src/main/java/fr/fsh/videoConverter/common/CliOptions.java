package fr.fsh.videoConverter.common;

import com.beust.jcommander.Parameter;

public class CliOptions {
    @Parameter(names = "-inputDir", description = "Input directory for video files to be encoded", required = true)
    private String inputDir;

    @Parameter(names = "-outputDir", description = "Output directory where encoded videos are written", required = true)
    private String outputDir;

    @Parameter(names = "-errorDir", description = "Error directory where files in error are moved to", required = true)
    private String errorDir;

    @Parameter(names = "-presetsFile", description = "Presets file for ffmpeg H264 encoding", required = true)
    private String presetsFile;
    
    public String inputDir(){
        return this.inputDir;
    }

    public String outputDir(){
        return this.outputDir;
    }

    public String presetsFile(){
        return this.presetsFile;
    }

    public String errorDir(){
        return this.errorDir;
    }
}