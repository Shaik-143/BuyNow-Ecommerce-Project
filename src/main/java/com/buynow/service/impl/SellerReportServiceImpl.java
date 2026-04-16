package com.buynow.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;

import com.buynow.model.Seller;
import com.buynow.model.SellerReport;
import com.buynow.repository.SellerReportRepository;
import com.buynow.service.SellerReportService;

@Service
@RequiredArgsConstructor
public class SellerReportServiceImpl implements SellerReportService {

    private final SellerReportRepository sellerReportRepository;


    @Override
    public SellerReport getSellerReport(Seller seller) {
        SellerReport report = sellerReportRepository.findBySellerId(seller.getId());
        if(report == null){
            SellerReport newReport = new SellerReport();
            newReport.setSeller(seller);
            return sellerReportRepository.save(newReport);
        }
        return report;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) {
    	
        return sellerReportRepository.save(sellerReport);
        
    }

}
