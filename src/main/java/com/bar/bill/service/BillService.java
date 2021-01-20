package com.bar.bill.service;

import com.bar.bill.entity.Bill;
import com.bar.bill.repository.BillRepository;
import com.bar.bill.request.BillRequest;
import com.bar.order.service.OrderService;
import com.bar.system.error.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillService {

    @Autowired
    BillRepository billRepository;
    @Autowired
    OrderService orderService;
    @Autowired
    BillCalcService billCalcService;

    public Bill get(long id) {
        var bill = Optional.of(billRepository.findById(id)).get()
                .orElseThrow(() -> new NotFoundException("Bill not found", Long.toString(id)));

        return setPrice(bill);
    }

    public List<Bill> getAll() {
        var billList = billRepository.findAll();
        billList.stream().forEach(bill -> setPrice(bill)
        );

        return billList;
    }

    public Bill add(BillRequest billRequest) {
        var bill = Bill.builder()
                .order(orderService.get(billRequest.getOrderId()))
                .company(billRequest.getCompany())
                .dateTime(billRequest.getDateTime())
                .customerName(Optional.ofNullable(billRequest.getCustomerName()).orElse(null))
                .nipNumber(Optional.ofNullable(billRequest.getNipNumber()).orElse(null))
                .build();

        return setPrice(billRepository.save(bill));
    }

    public Bill update(long id, BillRequest billRequest) {
        checkIfBillExistsInDatabase(id);
        var bill = Bill.builder()
                .id(id)
                .order(orderService.get(billRequest.getOrderId()))
                .company(billRequest.getCompany())
                .dateTime(billRequest.getDateTime())
                .customerName(Optional.of(billRequest.getCustomerName()).get())
                .nipNumber(Optional.of(billRequest.getNipNumber()).get())
                .build();

        return setPrice(billRepository.save(bill));
    }

    public void delete(long id) {
        checkIfBillExistsInDatabase(id);
        billRepository.deleteById(id);
    }

    private void checkIfBillExistsInDatabase(long productId) {
        if (!billRepository.existsById(productId)) {
            throw new NotFoundException("Bill not found", Long.toString(productId));
        }
    }

    private Bill setPrice(Bill bill) {
        bill.setPriceNet(billCalcService.priceNetCalc(bill.getOrder().getId()));
        return bill;
    }
}
