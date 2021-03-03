package kr.co.serinusSM.controller.popbillAPI;

import com.popbill.api.cashbill.CashbillServiceImp;
import com.popbill.api.closedown.CloseDownServiceImp;
import com.popbill.api.fax.FaxServiceImp;
import com.popbill.api.hometax.HTCashbillServiceImp;
import com.popbill.api.hometax.HTTaxinvoiceServiceImp;
import com.popbill.api.kakao.KakaoServiceImp;
import com.popbill.api.message.MessageServiceImp;
import com.popbill.api.statement.StatementServiceImp;
import com.popbill.api.taxinvoice.TaxinvoiceServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PopbillConfig {

    private String TestCorpNum = "6173622290";
    private String TestUserID = "MansOfBeauty";
    private String LinkID = "MOB";
    private String SecretKey = "6C/QkhPLqmShhCXARjL3ALF2D2uXFIK4CxdGZ0Yy+p4=";
    private boolean IsTest = true;

    @Bean
    public TaxinvoiceServiceImp taxinvoiceServiceImp(){
        TaxinvoiceServiceImp taxinvoiceServiceImp = new TaxinvoiceServiceImp();
        taxinvoiceServiceImp.setLinkID(LinkID);
        taxinvoiceServiceImp.setSecretKey(SecretKey);
        taxinvoiceServiceImp.setTest(IsTest);

        return taxinvoiceServiceImp;
    }

    @Bean
    public StatementServiceImp statementService(){
        StatementServiceImp statementService = new StatementServiceImp();
        statementService.setLinkID(LinkID);
        statementService.setSecretKey(SecretKey);
        statementService.setTest(IsTest);

        return statementService;
    }
    @Bean
    public CashbillServiceImp cashbillService(){
        CashbillServiceImp cashbillService = new CashbillServiceImp();
        cashbillService.setLinkID(LinkID);
        cashbillService.setSecretKey(SecretKey);
        cashbillService.setTest(IsTest);

        return cashbillService;
    }
    @Bean
    public MessageServiceImp messageService(){
        MessageServiceImp messageService = new MessageServiceImp();
        messageService.setLinkID(LinkID);
        messageService.setSecretKey(SecretKey);
        messageService.setTest(IsTest);

        return messageService;
    }
    @Bean
    public FaxServiceImp faxService(){
        FaxServiceImp faxService = new FaxServiceImp();
        faxService.setLinkID(LinkID);
        faxService.setSecretKey(SecretKey);
        faxService.setTest(IsTest);

        return faxService;
    }
    @Bean
    public HTTaxinvoiceServiceImp htTaxinvoiceService(){
        HTTaxinvoiceServiceImp htTaxinvoiceService = new HTTaxinvoiceServiceImp();
        htTaxinvoiceService.setLinkID(LinkID);
        htTaxinvoiceService.setSecretKey(SecretKey);
        htTaxinvoiceService.setTest(IsTest);

        return htTaxinvoiceService;
    }
    @Bean
    public HTCashbillServiceImp htCashbillService(){
        HTCashbillServiceImp htCashbillService = new HTCashbillServiceImp();
        htCashbillService.setLinkID(LinkID);
        htCashbillService.setSecretKey(SecretKey);
        htCashbillService.setTest(IsTest);

        return htCashbillService;
    }
    @Bean
    public CloseDownServiceImp closedownService(){
        CloseDownServiceImp closedownService = new CloseDownServiceImp();
        closedownService.setLinkID(LinkID);
        closedownService.setSecretKey(SecretKey);
        closedownService.setTest(IsTest);

        return closedownService;
    }
    @Bean
    public KakaoServiceImp kakaoService(){
        KakaoServiceImp kakaoService = new KakaoServiceImp();
        kakaoService.setLinkID(LinkID);
        kakaoService.setSecretKey(SecretKey);
        kakaoService.setTest(IsTest);

        return kakaoService;
    }
}
