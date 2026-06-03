package com.dss_erp.dss_erp.payload;

import com.dss_erp.dss_erp.models.PaymentTerms;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorResponse {

    private UUID id;
    private String vendorCode;
    private String name;
    private String vatNumber;
    private String registrationNumber;
    private String email;
    private String phone;
    private String currency;
    private PaymentTerms paymentTerms;
    private boolean active;
}
