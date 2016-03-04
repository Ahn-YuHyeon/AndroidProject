package com.project.john.bef.manager;

import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class FileHandler {
    static String sRootPath = "";

    public static String makeDir(String dirName) {
        sRootPath = Environment.getExternalStorageDirectory( ).getAbsolutePath( ) + File.separator +
                    dirName;
        try {
            File file = new File(sRootPath);

            if (file.exists( ) == false) {
                if (file.mkdirs( ) == false) throw new Exception("");
            }
        } catch (Exception e) {
            sRootPath = "-1";
        }
        return sRootPath + "/";
    }

    public static String[] getFileList(String strPath) {
        File file = new File(strPath);

        if (file.isDirectory( ) == false) return null;

        String[] fileList = file.list( );
        return fileList;
    }

    public static void deleteFile(String root, String fileName) {
        File[] files = new File(root).listFiles( );

        for (File file : files) {
            String filename = file.getName( );

            if (filename.equals(fileName)) file.delete( );
        }
    }

    public static void fileList2Array(ListView lv, ArrayList<String> files, String[] fileList) {
        if (fileList == null) return;

        files.clear( );

        for (int i = 0; i < fileList.length; i++) {
            files.add(fileList[i]);
        }
        ArrayAdapter adapter = (ArrayAdapter) lv.getAdapter( );
        adapter.notifyDataSetChanged( );
    }
}
