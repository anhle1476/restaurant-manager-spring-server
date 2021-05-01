package com.codegym.restaurant.controller;

import com.codegym.restaurant.dto.AuthInfoDTO;
import com.codegym.restaurant.dto.ProcessFoodDTO;
import com.codegym.restaurant.exception.BillNotFoundException;
import com.codegym.restaurant.exception.IdNotMatchException;
import com.codegym.restaurant.exception.InvalidDateInputException;
import com.codegym.restaurant.model.bussiness.Bill;
import com.codegym.restaurant.service.BillService;
import com.codegym.restaurant.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/bills")
public class BillController {
    @Autowired
    private BillService billService;

    @Autowired
    private AppUtils appUtils;

    @GetMapping("/{id}")
    public ResponseEntity<?> findByUUID(@PathVariable(value = "id") String id) {
        return new ResponseEntity<>(billService.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills(
            @RequestParam(name = "month", required = false) String month,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "search", required = false) String search) {
        if (month != null)
            return new ResponseEntity<>(billService.getAllBillOfMonth(month), HttpStatus.OK);
        if (date != null)
            return new ResponseEntity<>(billService.getAllBillOfDate(date), HttpStatus.OK);
        if (search != null)
            return new ResponseEntity<>(billService.findBillByUUID(search), HttpStatus.OK);
        return new ResponseEntity<>(billService.getAllCurrentBills(), HttpStatus.OK);
    }

    @GetMapping("/by-table")
    public ResponseEntity<Map<Integer, Bill>> mapBillByTableId() {
        return new ResponseEntity<>(billService.mapBillByTableId(), HttpStatus.OK);
    }

    @GetMapping("/report")
    public ResponseEntity<?> MonthReport(@RequestParam(name = "month", required = false) String month) {
        return new ResponseEntity<>(billService.monthReport(month), HttpStatus.OK);
    }

    @GetMapping("/income")
    public ResponseEntity<?> getTotalIncome(
            @RequestParam(value = "month", required = false) String month,
            @RequestParam(value = "date", required = false) String date
    ) {
        if (month != null)
            return new ResponseEntity<>(billService.totalProceedsInTheMonth(month), HttpStatus.OK);
        if (date != null)
            return new ResponseEntity<>(billService.totalProceedsInTheDate(date), HttpStatus.OK);
        throw new InvalidDateInputException("Ngày tháng yêu cầu không hợp lệ");
    }

    @PostMapping
    public ResponseEntity<?> createBIll(@Valid @RequestBody Bill bill, BindingResult result) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        try {
            return new ResponseEntity<>(billService.create(bill), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new BillNotFoundException("Một hóa đơn không thể có 2 món trùng nhau");
        }
    }

    @PostMapping("/{billId}/prepare-payment")
    public ResponseEntity<?> preparePayment(
            @Valid @RequestBody Bill bill,
            BindingResult result,
            @PathVariable(value = "billId") String billId
    ) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        if (!bill.getId().equals(billId))
            throw new IdNotMatchException("Id không trùng hợp");
        return new ResponseEntity<>(billService.preparePayment(bill), HttpStatus.CREATED);
    }

    @PostMapping("/{billId}/payment")
    public ResponseEntity<?> doPayment(@PathVariable(value = "billId") String billId, Principal principal) {
        AuthInfoDTO infoDTO = appUtils.extractUserInfoFromToken(principal);
        return new ResponseEntity<>(billService.doPayment(billId, infoDTO.getId()), HttpStatus.OK);
    }

    @PostMapping("/{id}/process-food")
    public ResponseEntity<?> processBillDoneQuantity(
            @Valid @RequestBody ProcessFoodDTO processFoodDTO,
            BindingResult result,
            @PathVariable(value = "id") String id
    ) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        if (!processFoodDTO.getBillId().equals(id))
            throw new IdNotMatchException("Id không trùng hợp");
        billService.processBillDoneQuantity(processFoodDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{billId}/moving-to/{tableId}")
    public ResponseEntity<Bill> changeTable(
            @PathVariable(value = "billId") String billId,
            @PathVariable(value = "tableId") Integer tableId
    ) {
        return new ResponseEntity<>(billService.changeTable(billId, tableId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBill(
            @Valid @RequestBody Bill bill,
            BindingResult result,
            @PathVariable(value = "id") String id
    ) {
        if (result.hasErrors())
            return appUtils.mapErrorToResponse(result);
        if (!bill.getId().equals(id))
            throw new IdNotMatchException("Id không trùng hợp");
        try {
            return new ResponseEntity<>(billService.update(bill), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            throw new BillNotFoundException("Trong 1 thời gian không thể có 2 bill giống nhau");
        }
    }

    @DeleteMapping("/{billId}")
    public ResponseEntity<?> delete(
            @PathVariable(value = "billId") String billId,
            @RequestParam(value = "force", defaultValue = "false") boolean force,
            Principal principal
    ) {
        if (force) {
            AuthInfoDTO infoDTO = appUtils.extractUserInfoFromToken(principal);
            billService.forceDelete(billId, infoDTO.getId());
        } else {
            billService.delete(billId);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}