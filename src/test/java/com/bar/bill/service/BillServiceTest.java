package com.bar.bill.service;

import com.bar.bill.entity.Bill;
import com.bar.bill.repository.BillRepository;
import com.bar.bill.request.BillRequest;
import com.bar.order.entity.Order;
import com.bar.order.service.OrderService;
import com.bar.system.error.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BillServiceTest {

    @InjectMocks
    BillService billService;

    @Mock
    BillRepository billRepository;
    @Mock
    BillCalcService billCalcService;
    @Mock
    OrderService orderService;

    Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        order = Order.builder()
                .id(1L)
                .employeeId(1)
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .build();

    }

    @Test
    void getBill() {
        long id = 1;
        Bill bill = Bill.builder()
                .id(1L)
                .order(order)
                .company("test")
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .priceNet(1.00)
                .customerName("testCustomer")
                .nipNumber("111-111-11-11")
                .build();

        when(billCalcService.priceNetCalc(anyLong())).thenReturn(1.00);
        when(billRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bill));

        Bill result = billService.get(id);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(order, result.getOrder());
        assertEquals("test", result.getCompany());
        assertEquals(LocalDateTime.of(2021, 1, 1, 12, 30, 15), result.getDateTime());
        assertEquals(1.00, result.getPriceNet());
        assertEquals("testCustomer", result.getCustomerName());
        assertEquals("111-111-11-11", result.getNipNumber());
    }

    @Test
    void getBill_NotFoundException() {
        long id = 1;
        when(billCalcService.priceNetCalc(anyLong())).thenReturn(1.00);
        when(billRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));

        assertThrows(NotFoundException.class,
                () -> {
                    billService.get(id);
                });
    }

    @Test
    void getAllBills() {
        List<Bill> billList = new ArrayList<>();
        Bill billOne = Bill.builder()
                .id(1L)
                .order(order)
                .company("testOne")
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .priceNet(1.00)
                .customerName("testCustomerOne")
                .nipNumber("111-111-11-11")
                .build();
        Bill billTwo = Bill.builder()
                .id(2L)
                .order(order)
                .company("testTwo")
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .priceNet(1.00)
                .customerName("testCustomerTwo")
                .nipNumber("111-111-11-11")
                .build();

        billList.add(billOne);
        billList.add(billTwo);

        when(billCalcService.priceNetCalc(anyLong())).thenReturn(1.00);
        when(billRepository.findAll()).thenReturn(billList);

        List<Bill> result = billService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(1, result.get(0).getId());
        assertEquals(order, result.get(0).getOrder());
        assertEquals("testOne", result.get(0).getCompany());
        assertEquals(LocalDateTime.of(2021, 1, 1, 12, 30, 15), result.get(0).getDateTime());
        assertEquals(1.00, result.get(0).getPriceNet());
        assertEquals("testCustomerOne", result.get(0).getCustomerName());
        assertEquals("111-111-11-11", result.get(0).getNipNumber());

        assertEquals(2, result.get(1).getId());
        assertEquals(order, result.get(1).getOrder());
        assertEquals("testTwo", result.get(1).getCompany());
        assertEquals(LocalDateTime.of(2021, 1, 1, 12, 30, 15), result.get(1).getDateTime());
        assertEquals(1.00, result.get(1).getPriceNet());
        assertEquals("testCustomerTwo", result.get(1).getCustomerName());
        assertEquals("111-111-11-11", result.get(1).getNipNumber());
    }

    @Test
    void addBill() {
        Bill bill = Bill.builder()
                .id(1L)
                .order(order)
                .company("test")
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .priceNet(1.00)
                .customerName("testCustomer")
                .nipNumber("111-111-11-11")
                .build();

        BillRequest billRequest = BillRequest.builder()
                .orderId(1L)
                .company("test")
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .customerName("testCustomer")
                .nipNumber("111-111-11-11")
                .build();

        when(billCalcService.priceNetCalc(anyLong())).thenReturn(1.00);
        when(billRepository.save(any())).thenReturn(bill);

        Bill result = billService.add(billRequest);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(order, result.getOrder());
        assertEquals("test", result.getCompany());
        assertEquals(LocalDateTime.of(2021, 1, 1, 12, 30, 15), result.getDateTime());
        assertEquals(1.00, result.getPriceNet());
        assertEquals("testCustomer", result.getCustomerName());
        assertEquals("111-111-11-11", result.getNipNumber());
    }

    @Test
    void updateBill() {
        long id = 1;
        Bill bill = Bill.builder()
                .id(1L)
                .order(order)
                .company("test")
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .priceNet(1.00)
                .customerName("testCustomer")
                .nipNumber("111-111-11-11")
                .build();

        BillRequest billRequest = BillRequest.builder()
                .orderId(1L)
                .company("test")
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .customerName("testCustomer")
                .nipNumber("111-111-11-11")
                .build();

        when(billCalcService.priceNetCalc(anyLong())).thenReturn(1.00);
        when(billRepository.existsById(anyLong())).thenReturn(true);
        when(billRepository.save(any())).thenReturn(bill);

        Bill result = billService.update(id, billRequest);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(order, result.getOrder());
        assertEquals("test", result.getCompany());
        assertEquals(LocalDateTime.of(2021, 1, 1, 12, 30, 15), result.getDateTime());
        assertEquals(1.00, result.getPriceNet());
        assertEquals("testCustomer", result.getCustomerName());
        assertEquals("111-111-11-11", result.getNipNumber());
    }

    @Test
    void updateBill_NotFoundException() {
        long id = 1;
        BillRequest billRequest = BillRequest.builder()
                .orderId(1L)
                .company("test")
                .dateTime(LocalDateTime.of(2021, 1, 1, 12, 30, 15))
                .customerName("testCustomer")
                .nipNumber("111-111-11-11")
                .build();

        when(billRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> {
                    billService.update(id, billRequest);
                }
        );
    }

    @Test
    void deleteBill() {
        long id = 1;
        when(billRepository.existsById(anyLong())).thenReturn(true);

        billService.delete(1);

        verify(billRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteBill_NotFoundException() {
        long id = 1;
        when(billRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class,
                () -> {
                    billService.delete(id);
                });
    }
}
