package com.mytask.accountservice.statistics.aop;

import com.mytask.accountservice.accountservice.annotations.Auditable;
import org.springframework.stereotype.Component;

@Component
public class TestAuditComponent {
    @Auditable
    public void auditableMethod() {
    }

}
