package com.infodev.eft_rtgs.Controller.PaymentRequestDetailController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infodev.eft_rtgs.Controller.PaymentRequestDetailController.bankDetailMappingService.EftNchlRbbBankMappingInterface;
import com.infodev.eft_rtgs.pojo.PaymentRequestTransferPojo;
import com.infodev.eft_rtgs.Model.Qurtz.SchedulerJobInfo;
import com.infodev.eft_rtgs.Model.epayment.EftNchlRbbBankMapping;
import com.infodev.eft_rtgs.Model.epayment.PaymentRequest;
import com.infodev.eft_rtgs.Model.epayment.PaymentRequestDetail;
import com.infodev.eft_rtgs.Model.epayment.TsaDebtorsBankDetail;
import com.infodev.eft_rtgs.enms.CronStatus;
import com.infodev.eft_rtgs.enms.TransactionType;
import com.infodev.eft_rtgs.globalResponse.TsaApiResponse;
import com.infodev.eft_rtgs.Config.SchedulerJobService;
import com.infodev.eft_rtgs.view.PaymentRequestRepo;
import com.infodev.eft_rtgs.view.SchedulerRepository;
import com.infodev.eft_rtgs.view.TsaDebtorsBankDetailRepo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@AllArgsConstructor
public class paymentRequestService implements PaymentRequestInterface {

    @Value("${rtgs-payment-validation-minimum-threshold}")
    private long rtgThreshold;

    @Value("${new_cron_rundelay}")
    private long cronDelay;


    Gson gson = new Gson();
    @Autowired
    private TsaDebtorsBankDetailRepo tsaDebtorsBankDetailRepo;
    @Autowired
    private EftNchlRbbBankMappingInterface eftNchlRbbBankMappingInterface;
    @Autowired
    private PaymentRequestRepo paymentRequestRepo;
    @Autowired
    private SchedulerRepository schedulerRepository;
    @Autowired
    private SchedulerJobService schedulerJobService;
    @Override
    public ResponseEntity<TsaApiResponse> requestPayment(@RequestBody  PaymentRequestTransferPojo paymentRequestTransferPojo/*, BindingResult bindingResult*/) throws Exception {

        /*if (bindingResult.hasErrors()) {
            return new ResponseEntity<TsaApiResponse>(new TsaApiResponse("failure", bindingResult.getAllErrors().stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining()), null), HttpStatus.BAD_REQUEST);
        }*/
        Type PaymentRequestDetailListType = new TypeToken<ArrayList<PaymentRequestDetail>>() {
        }.getType();
        List<PaymentRequestDetail> paymentRequestDetails = gson.fromJson(gson.toJson(paymentRequestTransferPojo.getEftPaymentRequestDetail()), PaymentRequestDetailListType);
        PaymentRequest paymentRequest = gson.fromJson(gson.toJson(paymentRequestTransferPojo.getPaymentRequest()), PaymentRequest.class);
        paymentRequest.setDebtorNrbBankCode(paymentRequest.getDebtorAgent());

//make sure the devidtor account are registerd into our db before initing transaction
        TsaDebtorsBankDetail tsaDebtorsBankDetail = lookforDebitorAccountDetail(paymentRequest);
//split the trasnction between two type onus and ofus
        List<PaymentRequestDetail> paymentRequestDetailList = splitTheTrsactionAccordingToOnUsandOffUs(paymentRequestDetails, paymentRequest);
        paymentRequest.setPaymentRequestDetails(paymentRequestDetailList);
    //set the transction id for all the requsted detail of request so thaty can track later
        paymentRequest.setTrasnctionID(TrasnctionTrackerGenerator());
        paymentRequestRepo.save(paymentRequest);



        SchedulerJobInfo job=lastCronExpressionDetail();
        job.setJobGroup(paymentRequest.getTrasnctionID());
        job.setJobName(paymentRequest.getTrasnctionID());
        System.out.println("job detail :  "+job.getCronExpression());
        //schedulerRepository.save(job);
        schedulerJobService.saveOrupdate(job);
        TsaApiResponse respone=new TsaApiResponse("Pending","Request Added",paymentRequest.getTrasnctionID());
        return new ResponseEntity<>(respone,HttpStatus.CREATED);
    }







    public TsaDebtorsBankDetail lookforDebitorAccountDetail(PaymentRequest paymentRequest) {
        TsaDebtorsBankDetail tsaDebtorsBankDetail = tsaDebtorsBankDetailRepo.findFirstByTsaBankAccountNoOrderByTsaBankAccountNo(paymentRequest.getDebtorAccount());

        if (tsaDebtorsBankDetail != null) {
            paymentRequest.setDebtorAgent(tsaDebtorsBankDetail.getNchlDebtorBankCode());
            paymentRequest.setDebtorBranch(tsaDebtorsBankDetail.getNchlDebtorBranchCode());

        } else {

            throw new RuntimeException(" Debtor Bank Mapping Not Found " + paymentRequest.getDebtorAccount());
        }
        return tsaDebtorsBankDetail;
    }


    public List<PaymentRequestDetail> splitTheTrsactionAccordingToOnUsandOffUs(List<PaymentRequestDetail> paymentRequestDetails, PaymentRequest paymentRequest) {


        return paymentRequestDetails.stream().map(x -> {
            x.setCreditorNrbBankCode(x.getCreditorAgent());
            x.setCreditorNrbBranchCode(x.getCreditorBranch());

            // the getCreditorAgent is taken and look at db from bank code recorded
            EftNchlRbbBankMapping mapping= eftNchlRbbBankMappingInterface.findByNrbCode(x.getCreditorAgent());

                 if(mapping==null){
                     throw new RuntimeException("Creditor Bank Code NOT Found");
                 }

                    // seperate onus and offus transactions before saving to database.
                    if (x.getAmount().compareTo(BigDecimal.valueOf(rtgThreshold)) == 1) {
                        x.setTransactionType(TransactionType.RTGS);
                        x.setApprovedFlag('Y');
                        x.setStatus("ENTR");
                        x.setEntryEdate(new Date());
                        x.setEntryNdate(new Date());
                        x.setCreditorAgent(mapping.getNrbBankCode());
                        x.setCreditorBranch(mapping.getNrbBranchCode());

                    } else if ((paymentRequest.getDebtorAgent().equals(x.getCreditorAgent())) && (!x.getCreditorAgent().equals("0001"))) {

                        x.setTransactionType(TransactionType.ONUS);
                        x.setApprovedFlag('Y');
                        x.setStatus("ENTR");
                        x.setEntryEdate(new Date());
                        x.setEntryNdate(new Date());
                        x.setCreditorAgent(mapping.getNrbBankCode());
                        x.setCreditorBranch(mapping.getNrbBranchCode());

                    } else {
                        x.setTransactionType(TransactionType.OFFUS);
                        x.setApprovedFlag('Y');
                        x.setStatus("ENTR");
                        x.setEntryEdate(new Date());
                        x.setEntryNdate(new Date());
                        x.setCreditorAgent(mapping.getNpiBankCode());
                        x.setCreditorBranch(mapping.getNpiBranchCode());
                    }


                    return x;
                }
        ).collect(Collectors.toList());
    }



    public String TrasnctionTrackerGenerator(){
        Boolean duplicationTrackerId=true;
        String TrasnctionTrackerId="";
        while(duplicationTrackerId){
            TrasnctionTrackerId   =      UUID.randomUUID().toString().replace("-", "");
            if(paymentRequestRepo.findFirstByTrasnctionID(TrasnctionTrackerId)==null){
                duplicationTrackerId=false;
            }
        }
        return TrasnctionTrackerId;
    }

    private SchedulerJobInfo lastCronExpressionDetail() throws ParseException {
        SchedulerJobInfo job=new SchedulerJobInfo();
        job.setJobStatus("Started");







      SchedulerJobInfo schedulerJobInfo= schedulerRepository.findFirstByCronStatusOrderByCronStatusDesc(CronStatus.READY.getKey());
        String generateregex="";


     if(schedulerJobInfo==null){
         job.setCronStatus(CronStatus.READY.getKey());
         job.setCronExpression(schedulerJobService.newCronexpressionset(cronDelay));
         //CronExpression cron=new CronExpression(job.getCronExpression());
         return job;
     }
     else{
//this will be updated once  previous cron is done
         job.setCronStatus(CronStatus.PENDING.getKey());
         job.setCronExpression(schedulerJobService.Cronexpressionsetmonth(cronDelay));
         return job;
     }
    }
}
