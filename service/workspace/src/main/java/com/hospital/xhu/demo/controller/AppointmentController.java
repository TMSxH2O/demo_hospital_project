package com.hospital.xhu.demo.controller;

import com.hospital.xhu.demo.service.IAppointmentService;
import com.hospital.xhu.demo.utils.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * Created with IntelliJ IDEA.
 *
 * @author TMS_H2O
 * @version V1.0
 * @date 2021/5/1
 */
@RestController
public class AppointmentController {
    @Resource(name = "appointmentServiceImpl")
    private IAppointmentService appointmentService;

    @PostMapping("reservation")
    public CommonResult<?> reservation(
            @RequestParam("userId") Long userId, @RequestParam("doctorId") Long doctorId,
            @RequestParam("date") LocalDate date) {
        return appointmentService.reservation(userId, doctorId, date);
    }

    @GetMapping("capacity")
    public CommonResult<?> selectRemainingCapacity(
            @RequestParam("doctorId") Long doctorId, @RequestParam("date") LocalDate date) {
        return appointmentService.selectDoctorReservationAvailableCapacity(doctorId, date);
    }
}
