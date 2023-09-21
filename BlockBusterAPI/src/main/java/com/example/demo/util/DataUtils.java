package com.example.demo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

import static com.example.demo.util.AppConstants.*;

public class DataUtils {
    public static String generateTempPwd(int length) {
        String numbers = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char otp[] = new char[length];
        Random getOtpNum = new Random();
        for (int i = 0; i < length; i++) {
            otp[i] = numbers.charAt(getOtpNum.nextInt(numbers.length()));
        }
        String optCode = "";
        for (int i = 0; i < otp.length; i++) {
            optCode += otp[i];
        }
        return optCode;
    }

    public static String getExtension(String fileName) {
        char ch;
        int len;
        if (fileName == null ||
                (len = fileName.length()) == 0 ||
                (ch = fileName.charAt(len - 1)) == '/' || ch == '\\' || //in the case of a directory
                ch == '.') //in the case of . or ..
            return "";
        int dotInd = fileName.lastIndexOf('.'),
                sepInd = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
        if (dotInd <= sepInd)
            return "";
        else
            return fileName.substring(dotInd + 1).toLowerCase();
    }

    public static String changeFileName(String currentPath, String newFileName, int type) {
        String extension = getExtension(currentPath);
        File newFile = null;
        if (type == DEFAULT_IMAGE_ACCOUNTS) {
            newFile = new File(DEFAULT_URL_DATA_IMAGE_ACCOUNT + File.separator + newFileName + "." + extension);
        } else if (type == DEFAULT_IMAGE_MOVIE) {
            newFile = new File(DEFAULT_URL_DATA_IMAGE_MOVIE + File.separator + newFileName + "." + extension);
        }
        File fileRootDir = new File(currentPath);
        if (fileRootDir.exists()) {
            if (fileRootDir.renameTo(newFile)) {
                assert newFile != null;
                return newFile.getPath();
            }
        }
        return null;
    }

    public static String decompressZLib(File file) {
        String image = null;
        String extension = getExtension(file.getName());
        String encodeBase64 = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStream.read(bytes);
            encodeBase64 = Base64.getEncoder().encodeToString(bytes);
            image = "data:image/"+extension+";base64,"+encodeBase64;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
