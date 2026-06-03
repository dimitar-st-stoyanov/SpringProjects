package com.dss_erp.dss_erp.payload;

import com.dss_erp.dss_erp.models.PaymentTerms;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorDTO {

    @NotBlank
    @Size(max = 50)
    private String vendorCode;

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 50)
    private String vatNumber;

    @Size(max = 50)
    private String registrationNumber;

    @Email
    @Size(max = 255)
    private String email;

    @Size(max = 50)
    private String phone;

    @Size(min = 3, max = 3)
    private String currency;

    private PaymentTerms paymentTerms;

    private Boolean active;
}
