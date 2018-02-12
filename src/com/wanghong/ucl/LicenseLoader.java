package com.wanghong.ucl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LicenseLoader {

    public static List<License> loadLicenses() {
        List<License> licenses = new ArrayList<>();
        licenses.add(new License("license/apl2.txt"));
        licenses.add(new License("license/gpl2.txt"));
        licenses.add(new License("license/gpl3.txt"));
//        CodeSource codeSource = LicenseLoader.class.getProtectionDomain().getCodeSource();
//        if (codeSource != null) {
//            URL jar = codeSource.getLocation();
//            try {
//                ZipInputStream jarInputStream = new ZipInputStream(jar.openStream());
//
//                while (true) {
//                    ZipEntry zipEntry = jarInputStream.getNextEntry();
//                    if (zipEntry == null) {
//                        break;
//                    }
//
//                    if (zipEntry.getName().startsWith("license")) {
//                        licenses.add(new License(zipEntry.getName()));
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        return licenses;
    }
}
