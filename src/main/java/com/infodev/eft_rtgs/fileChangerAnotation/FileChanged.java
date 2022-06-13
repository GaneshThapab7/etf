package com.infodev.eft_rtgs.fileChangerAnotation;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.Gson;
import com.infodev.eft_rtgs.Model.TransactionDetail;
import com.infodev.eft_rtgs.enms.ResponseFileType;
import com.infodev.eft_rtgs.exception.NotFoundException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
/*@Component
@Service*/
@Component
public class FileChanged {

    Gson gson=new Gson();

    @Value("${inputDir}")
    String inputDir;
    @Value("${outputDir}")
    String outputDir;
    @Value("${errorDir}")
    String errorDir;
    @Value("${response-wating-time}")
    int response;

    @After("@annotation(FileChangeListner) && execution(public * *(..)) && args(body,..)")
    public void trace(JoinPoint joinPoint, Object body) throws Throwable {

        TransactionDetail tr=gson.fromJson(gson.toJson(body), TransactionDetail.class);


        fileWatcher inputfileWatcher = new fileWatcher(inputDir);
        fileWatcher outputfileWatcher = new fileWatcher(outputDir);
        fileWatcher errorfileWatcher = new fileWatcher(errorDir);
        Thread threadoutfile = new Thread(inputfileWatcher);
        Thread threadinputfile = new Thread(outputfileWatcher);
        Thread threaderrorfile = new Thread(errorfileWatcher);
        System.out.println(threadoutfile.getId());
        System.out.println(threadinputfile.getId());
        System.out.println(threaderrorfile.getId());



        threadinputfile.start();
        threadoutfile.start();
        threaderrorfile.start();
        int responseclock=response;

        while (inputfileWatcher.getChangeFileName() == null && outputfileWatcher.getChangeFileName() == null
                && errorfileWatcher.getChangeFileName() == null&&responseclock!=0) {
         Thread.sleep(TimeUnit.SECONDS.toMillis(1));
            responseclock--;
        }

        if (inputfileWatcher.getChangeFileName() != null) {
            System.out.println(inputfileWatcher.getChangeFileName());
        } else if (outputfileWatcher.getChangeFileName() != null) {
            System.out.println(outputfileWatcher.getChangeFileName());
            processTheResponse(tr,ResponseFileType.OUTPUT,outputfileWatcher.getChangeFileName());
        } else if (errorfileWatcher.getChangeFileName() != null) {
            System.out.println(errorfileWatcher.getChangeFileName());
            processTheResponse(tr,ResponseFileType.ERROR,errorfileWatcher.getChangeFileName());
        }else{
            System.out.println("time out !!!");
        }



       /* inputfileWatcher.shutdown();
        outputfileWatcher.shutdown();
        errorfileWatcher.shutdown();*/

      //  threadinputfile.stop();
     //   threadoutfile.stop();
     //   threaderrorfile.stop();
    }

     public void  processTheResponse(TransactionDetail tr, ResponseFileType responsetype, String file) throws NotFoundException, JsonProcessingException {
        if(file.contains(".xsd")||file.contains(".xml")){
            ReadFile readFile=new ReadFile();


            XmlMapper xmlMapper = new XmlMapper();
          //  HEADER logo=xmlMapper.readValue(readFile.readfile(file),HEADER.class);

        }

         else{
             throw new NotFoundException("The file type is not excepatable !!");
         }
     }


}
