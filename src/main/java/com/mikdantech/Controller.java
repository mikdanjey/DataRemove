package com.mikdantech;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.IRepeatListener;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class Controller {

    public void run() {
        SpeedTestSocket speedTestSocket = new SpeedTestSocket();
        String URL = "https://services.gradle.org/distributions/gradle-6.1-bin.zip"; // Sample Download File
        speedTestSocket.startDownloadRepeat(URL,
                576000000, 1000, new
                        IRepeatListener() {
                            @Override
                            public void onCompletion(final SpeedTestReport report) {
                                System.out.println("\n\nCompleted");
                            }
                            @Override
                            public void onReport(final SpeedTestReport report) {
                                float dataSize = 0;
                                String value = humanReadableByteCountBin(report.getTemporaryPacketSize());
                                value = value.replace("GiB", "");
                                try {
                                    dataSize = Float.parseFloat(value);
                                    if (dataSize > 62) { // if Reach out 62 GB prg will exit;
                                        System.exit(0);
                                    }
                                } catch (Exception e) {
                                    // ignore
                                }

                                System.out.println("\nPercent:" + report.getProgressPercent() + "%");
                                System.out.println("Current Downloaded File: " + value);
                            }
                        });
    }

    public static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }
}
