package com.microservices.saliiov.resource.resource_service.service.impl;

import com.microservices.saliiov.resource.resource_service.dto.SongMetadata;
import com.microservices.saliiov.resource.resource_service.service.AudioService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class AudioServiceImpl implements AudioService {
    private static final String TITLE = "dc:title";
    private static final String ARTIST = "xmpDM:artist";
    private static final String ALBUM = "xmpDM:album";
    private static final String DURATION = "xmpDM:duration";
    private static final String RELEASE_DATE = "xmpDM:releaseDate";

    @Override
    public SongMetadata getSongMetadata(byte[] data) throws IOException, TikaException, SAXException {
        try (InputStream inputstream = new ByteArrayInputStream(data)) {
            Metadata metadata = getMetadata(inputstream);
            return SongMetadata.builder()
                    .name(metadata.get(TITLE))
                    .artist(metadata.get(ARTIST))
                    .album(metadata.get(ALBUM))
                    .length(metadata.get(DURATION))
                    .year(metadata.get(RELEASE_DATE))
                    .build();

        }
    }

    private Metadata getMetadata(InputStream inputstream) throws IOException, SAXException, TikaException {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext parseContext = new ParseContext();
        Mp3Parser mp3Parser = new Mp3Parser();
        mp3Parser.parse(inputstream, handler, metadata, parseContext);
        return metadata;
    }
}
