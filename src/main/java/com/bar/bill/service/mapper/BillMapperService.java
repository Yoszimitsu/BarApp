package com.bar.bill.service.mapper;

import com.bar.bill.dto.BillDto;
import com.bar.bill.entity.Bill;
import com.bar.order.service.mapper.OrderMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillMapperService {

    private final OrderMapperService orderMapperService;

    public Bill mapToEntity(BillDto billDto) {
        var bill = Bill.builder()
                .id(billDto.getId())
                .company(billDto.getCompany())
                .order(orderMapperService.mapToEntity(billDto.getOrderDto()))
                .dateTime(billDto.getDateTime())
                .priceNet(billDto.getPriceNet())
                .customerName(Optional.of(billDto.getCustomerName()).get())
                .nipNumber(Optional.of(billDto.getNipNumber()).get())
                .build();
        return bill;
    }

    public BillDto mapToDto(Bill bill) {
        var billDto = BillDto.builder()
                .id(bill.getId())
                .company(bill.getCompany())
                .orderDto(orderMapperService.mapToDto(bill.getOrder()))
                .dateTime(bill.getDateTime())
                .priceNet(bill.getPriceNet())
                .customerName(Optional.ofNullable(bill.getCustomerName()).orElse(null))
                .nipNumber(Optional.ofNullable(bill.getNipNumber()).orElse(null))
                .build();
        return billDto;
    }

    public List<BillDto> mapToBillDtoList(List<Bill> billList) {
        List<BillDto> billDtoList = new ArrayList<>();
        for (Bill bill : billList) {
            billDtoList.add(mapToDto(bill));
        }
        return billDtoList;
    }

}
