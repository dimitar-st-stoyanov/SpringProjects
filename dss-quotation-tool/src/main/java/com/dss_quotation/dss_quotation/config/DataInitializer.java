package com.dss_quotation.dss_quotation.config;

import com.dss_quotation.dss_quotation.models.GasType;
import com.dss_quotation.dss_quotation.models.Machine;
import com.dss_quotation.dss_quotation.models.MachineCutParameters;
import com.dss_quotation.dss_quotation.models.Material;
import com.dss_quotation.dss_quotation.repositories.MachineRepository;
import com.dss_quotation.dss_quotation.repositories.MachineCutParametersRepository;
import com.dss_quotation.dss_quotation.repositories.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final MachineRepository machineRepository;
    private final MachineCutParametersRepository machineCutParametersRepository;
    private final MaterialRepository materialRepository;

    @Override
    public void run(String... args) {

        // ✅ Avoid duplicate seeding
        /* ===============================
           1. CREATE MACHINES
        =============================== */
        getOrCreateMachine("Laser 4kW", 4, 40.0, 0.8, 20.0);
        getOrCreateMachine("Laser 6kW", 6, 60.0, 0.8, 20.0);
        Machine m12 = getOrCreateMachine("Laser 12kW", 12, 120.0, 0.8, 20.0);

        /* ===============================
           STEEL (COLD ROLLED / GALV)
        =============================== */
        Material dc01 = getOrCreateMaterial("DC01", "steel", 7850, 1.2);
        Material dd11 = getOrCreateMaterial("DD11", "steel", 7850, 1.1);
        Material dx51 = getOrCreateMaterial("DX51 (Galvanized)", "steel", 7850, 1.4);

        /* ===============================
           STAINLESS STEEL
        =============================== */
        Material aisi304 = getOrCreateMaterial("AISI 304", "stainless", 8000, 2.5);
        Material aisi316 = getOrCreateMaterial("AISI 316", "stainless", 8000, 3.2);
        Material aisi430 = getOrCreateMaterial("AISI 430", "stainless", 7700, 2.0);

        /* ===============================
           ALUMINUM (COMMON SHEET)
        =============================== */
        Material alu1050 = getOrCreateMaterial("Aluminum 1050", "aluminum", 2700, 3.0);
        Material alu5754 = getOrCreateMaterial("Aluminum 5754", "aluminum", 2700, 3.2);
        Material alu6061 = getOrCreateMaterial("Aluminum 6061", "aluminum", 2700, 3.5);

//        /* ===============================
//           2. ADD CUT SPEEDS (6kW)
//        =============================== */

/* ===============================
   LASER 12KW
=============================== */

        List<Double> standardThicknesses = Arrays.asList(1.0, 1.5, 2.0, 2.5, 3.0, 5.0, 6.0, 8.0, 10.0, 12.0, 15.0);

// =====================================================
// STEEL - DC01 (Nitrogen)
// =====================================================
        for (Double thickness : standardThicknesses) {
            double speed;
            double pierceTime;
            double pierceHeight;

            if (thickness <= 1.0) {
                speed = 45000;
                pierceTime = 0.08;
                pierceHeight = 0.8;
            } else if (thickness <= 1.5) {
                speed = 32000;
                pierceTime = 0.10;
                pierceHeight = 0.9;
            } else if (thickness <= 2.0) {
                speed = 26000;
                pierceTime = 0.15;
                pierceHeight = 1.0;
            } else if (thickness <= 2.5) {
                speed = 21000;
                pierceTime = 0.20;
                pierceHeight = 1.1;
            } else if (thickness <= 3.0) {
                speed = 18000;
                pierceTime = 0.25;
                pierceHeight = 1.2;
            } else if (thickness <= 5.0) {
                speed = 11000;
                pierceTime = 0.40;
                pierceHeight = 1.5;
            } else if (thickness <= 6.0) {
                speed = 8500;
                pierceTime = 0.55;
                pierceHeight = 1.8;
            } else if (thickness <= 8.0) {
                speed = 6000;
                pierceTime = 0.90;
                pierceHeight = 2.0;
            } else if (thickness <= 10.0) {
                speed = 4200;
                pierceTime = 1.20;
                pierceHeight = 2.2;
            } else if (thickness <= 12.0) {
                speed = 3000;
                pierceTime = 1.60;
                pierceHeight = 2.5;
            } else {
                speed = 1800;
                pierceTime = 2.20;
                pierceHeight = 3.0;
            }

            saveCutParameters(MachineCutParameters.builder()
                    .machine(m12)
                    .material(dc01)
                    .thickness(thickness)
                    .gasType(GasType.NITROGEN)
                    .speed(speed)
                    .pierceTime(pierceTime)
                    .pierceHeight(pierceHeight)
                    .build());
        }

// =====================================================
// STEEL - DD11 (Oxygen)
// =====================================================
        for (Double thickness : standardThicknesses) {
            double speed;
            double pierceTime;
            double pierceHeight;

            if (thickness <= 1.0) {
                speed = 18000;
                pierceTime = 0.12;
                pierceHeight = 0.8;
            } else if (thickness <= 1.5) {
                speed = 14500;
                pierceTime = 0.15;
                pierceHeight = 0.9;
            } else if (thickness <= 2.0) {
                speed = 12000;
                pierceTime = 0.20;
                pierceHeight = 1.0;
            } else if (thickness <= 2.5) {
                speed = 9800;
                pierceTime = 0.25;
                pierceHeight = 1.1;
            } else if (thickness <= 3.0) {
                speed = 8500;
                pierceTime = 0.30;
                pierceHeight = 1.2;
            } else if (thickness <= 5.0) {
                speed = 7000;
                pierceTime = 0.45;
                pierceHeight = 1.5;
            } else if (thickness <= 6.0) {
                speed = 6500;
                pierceTime = 0.50;
                pierceHeight = 1.8;
            } else if (thickness <= 8.0) {
                speed = 4200;
                pierceTime = 0.80;
                pierceHeight = 2.0;
            } else if (thickness <= 10.0) {
                speed = 3000;
                pierceTime = 1.20;
                pierceHeight = 2.2;
            } else if (thickness <= 12.0) {
                speed = 2200;
                pierceTime = 1.60;
                pierceHeight = 2.5;
            } else {
                speed = 1400;
                pierceTime = 2.40;
                pierceHeight = 3.0;
            }

            saveCutParameters(MachineCutParameters.builder()
                    .machine(m12)
                    .material(dd11)
                    .thickness(thickness)
                    .gasType(GasType.OXYGEN)
                    .speed(speed)
                    .pierceTime(pierceTime)
                    .pierceHeight(pierceHeight)
                    .build());
        }

// =====================================================
// STEEL - DX51 (Nitrogen)
// =====================================================
        for (Double thickness : standardThicknesses) {
            double speed = Math.max(1200, 16000 - (thickness.intValue() * 1200));
            double pierceTime = 0.12 + (thickness * 0.08);
            double pierceHeight = 0.8 + (thickness * 0.15);

            saveCutParameters(MachineCutParameters.builder()
                    .machine(m12)
                    .material(dx51)
                    .thickness(thickness)
                    .gasType(GasType.NITROGEN)
                    .speed(speed)
                    .pierceTime(pierceTime)
                    .pierceHeight(pierceHeight)
                    .build());
        }

// =====================================================
// STAINLESS - AISI304 (Nitrogen)
// =====================================================
        for (Double thickness : standardThicknesses) {
            double speed;

            if (thickness <= 3.0) {
                speed = 18000 - (thickness.intValue() * 1800);
            } else if (thickness <= 8.0) {
                speed = 9000 - (thickness.intValue() * 500);
            } else {
                speed = 3500 - (thickness.intValue() * 120);
            }

            saveCutParameters(MachineCutParameters.builder()
                    .machine(m12)
                    .material(aisi304)
                    .thickness(thickness)
                    .gasType(GasType.NITROGEN)
                    .speed(Math.max(speed, 1200))
                    .pierceTime(0.15 + (thickness * 0.15))
                    .pierceHeight(0.8 + (thickness * 0.14))
                    .build());
        }

// =====================================================
// STAINLESS - AISI316 (Nitrogen)
// =====================================================
        for (Double thickness : standardThicknesses) {
            double speed = Math.max(1000, 14000 - (thickness.intValue() * 1100));

            saveCutParameters(MachineCutParameters.builder()
                    .machine(m12)
                    .material(aisi316)
                    .thickness(thickness)
                    .gasType(GasType.NITROGEN)
                    .speed(speed)
                    .pierceTime(0.18 + (thickness * 0.16))
                    .pierceHeight(0.9 + (thickness * 0.14))
                    .build());
        }

// =====================================================
// STAINLESS - AISI430 (Nitrogen)
// =====================================================
        for (Double thickness : standardThicknesses) {
            double speed = Math.max(1000, 13000 - (thickness.intValue() * 950));

            saveCutParameters(MachineCutParameters.builder()
                    .machine(m12)
                    .material(aisi430)
                    .thickness(thickness)
                    .gasType(GasType.NITROGEN)
                    .speed(speed)
                    .pierceTime(0.20 + (thickness * 0.15))
                    .pierceHeight(1.0 + (thickness * 0.13))
                    .build());
        }

// =====================================================
// ALUMINUM - ALU1050 (Nitrogen)
// =====================================================
        for (Double thickness : standardThicknesses) {
            double speed = Math.max(1200, 15000 - (thickness.intValue() * 1000));

            saveCutParameters(MachineCutParameters.builder()
                    .machine(m12)
                    .material(alu1050)
                    .thickness(thickness)
                    .gasType(GasType.NITROGEN)
                    .speed(speed)
                    .pierceTime(0.20 + (thickness * 0.14))
                    .pierceHeight(0.9 + (thickness * 0.15))
                    .build());
        }

// =====================================================
// ALUMINUM - ALU5754 (Nitrogen)
// =====================================================
        for (Double thickness : standardThicknesses) {
            double speed = Math.max(1000, 12000 - (thickness.intValue() * 850));

            saveCutParameters(MachineCutParameters.builder()
                    .machine(m12)
                    .material(alu5754)
                    .thickness(thickness)
                    .gasType(GasType.NITROGEN)
                    .speed(speed)
                    .pierceTime(0.25 + (thickness * 0.15))
                    .pierceHeight(1.0 + (thickness * 0.15))
                    .build());
        }

// =====================================================
// ALUMINUM - ALU6061 (Nitrogen)
// =====================================================
        for (Double thickness : standardThicknesses) {
            double speed = Math.max(900, 10000 - (thickness.intValue() * 700));

            saveCutParameters(MachineCutParameters.builder()
                    .machine(m12)
                    .material(alu6061)
                    .thickness(thickness)
                    .gasType(GasType.NITROGEN)
                    .speed(speed)
                    .pierceTime(0.30 + (thickness * 0.16))
                    .pierceHeight(1.0 + (thickness * 0.16))
                    .build());
        }
    }

    private Machine getOrCreateMachine(String name, double power, double ratePerHour, double efficiencyFactor,double minimumCharge) {
        return machineRepository.findByName(name)
                .orElseGet(() -> machineRepository.save(Machine.builder()
                        .name(name)
                        .power(power)
                        .ratePerHour(ratePerHour)
                        .efficiencyFactor(efficiencyFactor)
                        .minimumCharge(minimumCharge)
                        .build()));
    }

    private Material getOrCreateMaterial(String name, String type, double density, double pricePerKg) {
        return materialRepository.findByName(name)
                .orElseGet(() -> materialRepository.save(Material.builder()
                        .name(name)
                        .type(type)
                        .density(density)
                        .pricePerKg(pricePerKg)
                        .active(true)
                        .build()));
    }

    private void saveCutParameters(MachineCutParameters parameters) {
        boolean exists = machineCutParametersRepository.existsByMachineIdAndMaterialIdAndThicknessAndGasType(
                parameters.getMachine().getId(),
                parameters.getMaterial().getId(),
                parameters.getThickness(),
                parameters.getGasType()
        );

        if (!exists) {
            machineCutParametersRepository.save(parameters);
        }
    }
}
