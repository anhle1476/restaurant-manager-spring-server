package com.codegym.restaurant.service;

import com.codegym.restaurant.dto.UpdateAccountInfoDTO;
import com.codegym.restaurant.dto.UpdateAccountPasswordDTO;
import com.codegym.restaurant.dto.UpdateStaffPasswordDTO;
import com.codegym.restaurant.model.hr.Staff;

public interface StaffService extends BaseService<Staff, Integer> {
     void updateStaffPassword(UpdateStaffPasswordDTO updateStaffPasswordDTO);

     void updateAccountPassword(Integer accountId, UpdateAccountPasswordDTO updateAccountPasswordDTO);

     Staff updateAccountInfo(Integer integer, UpdateAccountInfoDTO updateAccountInfoDTO);

}
