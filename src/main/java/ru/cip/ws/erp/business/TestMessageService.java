package ru.cip.ws.erp.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.cip.ws.erp.jms.MQMessageSender;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

/**
 * Author: Upatov Egor <br>
 * Date: 22.09.2016, 8:00 <br>
 * Company: Bars Group [ www.bars.open.ru ]
 * Description:
 */
@Repository
public class TestMessageService {
    @Autowired
    private MQMessageSender messageSender;

    private String loadMessage(final String path) {
        final String result = getFile(path);
        return String.format(result, UUID.randomUUID().toString(), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date()));
    }

    private String getFile(String fileName) {
        final StringBuilder result = new StringBuilder("");
        //Get file from resources folder
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource(fileName).getFile());
        Scanner scanner = null;
        try {
            scanner = new Scanner(file, "UTF-8");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
            scanner.close();
        } catch (Exception e) {
            if (scanner != null) {
                scanner.close();
            }
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return result.toString();
    }


    public void processProsecutorAsk(final int requestNumber) {
        messageSender.send(loadMessage("testMessages/001/Request.xml"));
    }


    public void processPlanRegular294Initialization(final int requestNumber) {
        messageSender.send(loadMessage("testMessages/002/Request.xml"));
    }

    public void processPlanRegular294Correction(final int requestNumber) {
        messageSender.send(loadMessage("testMessages/003/Request.xml"));
    }

    public void processPlanResult294Initialization(final int requestNumber) {

    }

    public void processPlanResult294Correction(final int requestNumber) {

    }

    public void processUplanUnRegular294Initialization(final int requestNumber) {

    }

    public void processUplanUnRegular294Correction(final int requestNumber) {

    }

    public void processUplanResult294Initialization(final int requestNumber) {

    }

    public void processUplanResult294Correction(final int requestNumber) {

    }
}
