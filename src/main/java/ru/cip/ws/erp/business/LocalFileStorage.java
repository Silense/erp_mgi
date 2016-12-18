package ru.cip.ws.erp.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

/**
 * Author: Upatov Egor <br>
 * Date: 18.12.2016, 15:52 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */

@Component
public class LocalFileStorage {
    private final static Logger log = LoggerFactory.getLogger(LocalFileStorage.class);

    private final String basePath;


    @Autowired
    public LocalFileStorage(@Qualifier("app.properties")Properties props){
        this.basePath = props.getProperty("fileStorage.basePath", "../outfiles/");
    }


    public String createFile(final BigInteger checkId, final String uuid, final Date date, final String messageContent) {
        final StringBuilder sb = new StringBuilder("/").append(checkId).append('/').append(uuid).append('/').append(date.getTime()).append(".xml");
        final Path file = Paths.get(basePath, sb.toString());
        try {
            file.getParent().toFile().mkdirs();
            final Path createdFile = Files.write(file, messageContent.getBytes(StandardCharsets.UTF_8), CREATE_NEW);
            log.info("Created file: {}", createdFile);
        } catch (IOException e){
           log.error("Error create file[{}]: ", sb, e);
        }
        return sb.toString();
    }
}
