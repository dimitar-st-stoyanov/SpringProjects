package com.dss_erp.dss_erp.config;

import com.dss_erp.dss_erp.models.Machine;
import com.dss_erp.dss_erp.models.MachineCutSpeed;
import com.dss_erp.dss_erp.repositories.MachineRepository;
import com.dss_erp.dss_erp.repositories.MachineCutSpeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MachineRepository machineRepository;
    private final MachineCutSpeedRepository machineCutSpeedRepository;

    @Override
    public void run(String... args) {

        // ✅ Avoid duplicate seeding
        if (machineRepository.findByName("Laser 6kW").isPresent()) return;

        /* ===============================
           1. CREATE MACHINES
        =============================== */
        Machine m4 = machineRepository.save(Machine.builder()
                .name("Laser 4kW")
                .power(4)
                .ratePerHour(40.0)
                .build());

        Machine m6 = machineRepository.save(Machine.builder()
                .name("Laser 6kW")
                .power(6)
                .ratePerHour(60.0)
                .build());

        Machine m12 = machineRepository.save(Machine.builder()
                .name("Laser 12kW")
                .power(12)
                .ratePerHour(120.0)
                .build());

        /* ===============================
           2. ADD CUT SPEEDS (6kW)
        =============================== */
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m6).thickness(1).speed(21500).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m6).thickness(1.5).speed(15000).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m6).thickness(2).speed(10000).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m6).thickness(2.5).speed(7500).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m6).thickness(3).speed(6000).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m6).thickness(4).speed(4000).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m6).thickness(5).speed(3250).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m6).thickness(6).speed(2500).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m6).thickness(8).speed(2000).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m6).thickness(10).speed(1400).build());

        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m4).thickness(1).speed(15000).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m4).thickness(1.5).speed(10000).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m4).thickness(2).speed(6500).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m4).thickness(2.5).speed(5000).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m4).thickness(3).speed(4000).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m4).thickness(4).speed(2750).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m4).thickness(5).speed(2000).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m4).thickness(6).speed(1500).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m4).thickness(8).speed(1000).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m4).thickness(10).speed(750).build());

        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m12).thickness(1).speed(35000).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m12).thickness(1.5).speed(26000).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m12).thickness(2).speed(18500).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m12).thickness(2.5).speed(15000).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m12).thickness(3).speed(12500).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m12).thickness(4).speed(10000).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m12).thickness(5).speed(8000).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m12).thickness(6).speed(6500).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m12).thickness(8).speed(5500).build());
        machineCutSpeedRepository.save(MachineCutSpeed.builder().machine(m12).thickness(10).speed(4500).build());

        /* ===============================
           (OPTIONAL) Add speeds for 4kW / 12kW later
        =============================== */
    }
}