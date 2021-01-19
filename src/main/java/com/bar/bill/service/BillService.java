package com.bar.bill.service;

import com.bar.bill.entity.Bill;
import com.bar.bill.repository.BillRepository;
import com.bar.bill.request.BillRequest;
import com.bar.order.service.OrderService;
import com.bar.system.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final OrderService orderService;

    public Bill get(long id) {
        return Optional.of(billRepository.findById(id)).get()
                .orElseThrow(() -> new NotFoundException("Bill not found", Long.toString(id)));
    }

    public List<Bill> getAll() {
        return billRepository.findAll();
    }

    public Bill add(BillRequest billRequest) {
        var bill = Bill.builder()
                .order(orderService.get(billRequest.getOrderId()))
                .company(billRequest.getCompany())
                .dateTime(billRequest.getDateTime())
                .priceNet(billRequest.getPriceNet())
                .customerName(Optional.ofNullable(billRequest.getCustomerName()).orElse(null))
                .nipNumber(Optional.ofNullable(billRequest.getNipNumber()).orElse(null))
                .build();

        return billRepository.save(bill);
    }

    public Bill update(long id, BillRequest billRequest) {
        checkIfBillExistsInDatabase(id);
        var bill = Bill.builder()
                .id(id)
                .order(orderService.get(billRequest.getOrderId()))
                .company(billRequest.getCompany())
                .dateTime(billRequest.getDateTime())
                .priceNet(billRequest.getPriceNet())
                .customerName(Optional.of(billRequest.getCustomerName()).get())
                .nipNumber(Optional.of(billRequest.getNipNumber()).get())
                .build();
        return billRepository.save(bill);
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
}
