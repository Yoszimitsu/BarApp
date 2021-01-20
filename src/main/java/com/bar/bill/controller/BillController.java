package com.bar.bill.controller;

import com.bar.bill.dto.BillDto;
import com.bar.bill.request.BillRequest;
import com.bar.bill.service.BillService;
import com.bar.bill.service.mapper.BillMapperService;
import com.bar.system.endpoint.Endpoint;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = BillController.BILL_ENDPOINT)
@AllArgsConstructor
@Slf4j
public class BillController {

    static final String BILL_ENDPOINT = Endpoint.API_ROOT + Endpoint.URN_BILL;

    @Autowired
    BillService billService;
    @Autowired
    BillMapperService billMapperService;

    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<BillDto> get(@PathVariable long id) {
        log.info("GET " + BILL_ENDPOINT + "/" + id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(billMapperService.mapToDto(billService.get(id)));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<BillDto>> getAll() {
        log.info("GET " + BILL_ENDPOINT);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(billMapperService.mapToBillDtoList(billService.getAll()));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<BillDto> add(@Valid @RequestBody BillRequest billRequest) {
        log.info("POST " + BILL_ENDPOINT);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(billMapperService.mapToDto(billService.add(billRequest)));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<BillDto> update(@PathVariable long id, @Valid @RequestBody BillRequest billRequest) {
        log.info("PUT " + BILL_ENDPOINT + "/" + id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(billMapperService.mapToDto(billService.update(id, billRequest)));
    }

    @DeleteMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> delete(@PathVariable long id) {
        log.info("DELETE " + BILL_ENDPOINT + "/" + id);
        billService.delete(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
