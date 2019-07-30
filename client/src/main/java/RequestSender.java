import lombok.Value;

import java.util.Objects;

public class RequestSender {

    private static final String WELCOME_MESSAGE = "" +
            "Using: RequestSender rCount urlRead wCount urlWrite idRange triesCount\n" +
            "rCount     number of readers (not negative)\n" +
            "urlRead    resource location for read\n" +
            "wCount     number of writers (not negative)\n" +
            "urlWrite   resource location for write\n" +
            "idRange    id range separated by ':' (begin <= end)\n" +
            "triesCount repeat count (positive)\n" +
            "Example:   RequestSender 100 http://localhost:8080/account/getAmount 100 http://localhost:8080/account/addAmount 1:100 5";


    public static void main(String[] args) throws Exception {

        Properties properties = null;
        try {
            properties = parseCommandArgs(args);
        } catch (Exception ex) {
            System.out.println(WELCOME_MESSAGE);
            System.exit(-1);
        }

        new HttpSender(properties).start();
    }

    private static Properties parseCommandArgs(String[] args) {
        Objects.requireNonNull(args);
        if (args.length != 6) {
            throw new IllegalArgumentException("args length != 6");
        }
        int rCount = Integer.parseInt(args[0]);
        String urlRead = args[1];
        int wCount = Integer.parseInt(args[2]);
        String urlWrite = args[3];
        String[] range = args[4].split(":");
        int idRangeBegin = Integer.parseInt(range[0]);
        int idRangeEnd = Integer.parseInt(range[1]);
        int repeatCount = Integer.parseInt(args[5]);

        if (rCount < 0 || wCount < 0 || repeatCount < 1 || idRangeBegin > idRangeEnd) {
            throw new IllegalArgumentException("arguments validation fails");
        }

        return new Properties(rCount,
                wCount, idRangeBegin, idRangeEnd, urlRead, urlWrite, repeatCount);
    }

    @Value
    static class Properties {
        int rCount;
        int wCount;
        int idRangeBegin;
        int idRangeEnd;
        String urlRead;
        String urlWrite;

        int triesCount;
    }
}
