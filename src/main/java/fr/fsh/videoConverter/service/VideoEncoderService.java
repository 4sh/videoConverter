package fr.fsh.videoConverter.service;

import org.apache.commons.cli.ParseException;

import java.io.File;

public interface VideoEncoderService {

    public void encodeVideo(File video) throws ParseException;

}
