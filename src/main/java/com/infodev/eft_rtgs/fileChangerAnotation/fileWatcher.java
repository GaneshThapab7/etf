package com.infodev.eft_rtgs.fileChangerAnotation;

import java.io.IOException;
import java.nio.file.*;

public class fileWatcher implements Runnable {

    public WatchService watchService;
    public WatchKey watchKey;
    String directoryPath;
    private volatile String changeFileName = null;
    public Boolean shutdown=true;


    public fileWatcher(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public fileWatcher() {

    }


    public String getChangeFileName() {
        return changeFileName;
    }



    String register(String directoryPath) throws IOException {
        //System.out.println("thread id   "+   this.dirMonitorThread.getId());
        Path faxFolder = Paths.get(directoryPath);
        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e1) {
            // handle the exception as per your convenience
            System.out.println("directoryPath NOT found");
            e1.printStackTrace();
        }
        try {
            faxFolder.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE);
        } catch (IOException e) {
            // handle the exception as per your convenience
            e.printStackTrace();
        }
        try {
            watchKey = watchService.take();


        } catch (InterruptedException e) {
            // handle the exception as per your convenience
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        String path=startWatcher(directoryPath);
        watchService.close();
        return path;
    }

    /**
     * @param directoryPath start the watcher process in background to monitor the files in this directoryPath.
     */
    public String startWatcher(String directoryPath) {

        boolean valid = true;

        do {
            for (WatchEvent event : watchKey.pollEvents()) {
                event.kind();
                String eventType = "";
                String tempPath = directoryPath;
                String fileName = "";

                if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {

                    fileName = event.context().toString();

                /*    System.out.println("File Created:" + fileName
                            + ", EventKind : " + event.kind());*/

                    eventType = event.kind().toString();
                    valid = false;
                    return directoryPath + "/" + fileName;


                } else if (StandardWatchEventKinds.ENTRY_DELETE.equals(event
                        .kind())) {

                    fileName = event.context().toString();

              /*      System.out.println("File Deleted:" + fileName
                            + ", EventKind : " + event.kind());*/

                    eventType = event.kind().toString();
                    valid = false;

                    return directoryPath + "/" + fileName;

                }
                // using this(directoryPath, modified/created filename and the
                // eventType on it) you can even update your any DOM
                // object/chache for any further processing.
                // XYZ.refreshFileObjectCache( directoryPath + fileName,
                // eventType);
            }
            // valid = watchKey.reset();

        } while (valid);
        return null;
    }

    /**
     * Interrupt/stop the fileWatcher thread process
     */
    public void stop() {


        try {
            //     dirMonitorThread.interrupt();
            this.watchService.close();
        } catch (IOException e) {
            // handle the exception as per your convenience
            e.printStackTrace();
        }
    }

    /**
     * Override the Runnable run method to initiate the watcher process. Here
     * provide the fileWatcher source i.e the directory path to be monitored
     */
    public void run() {

            try {

                this.changeFileName = register(directoryPath);
                if(this.changeFileName!=null){
                    stop();
                }

            } catch (IOException e) {
                // handle the exception as per your convenience
                e.printStackTrace();
            }

    }


}
