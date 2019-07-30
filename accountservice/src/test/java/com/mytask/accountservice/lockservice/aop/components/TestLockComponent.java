package com.mytask.accountservice.lockservice.aop.components;

import com.mytask.accountservice.accountservice.annotations.LockByParameter;
import com.mytask.accountservice.accountservice.annotations.LockKey;
import org.springframework.stereotype.Component;

@Component
public class TestLockComponent {

    @LockByParameter
    public void wrongAnnotatedMethod() {
    }

    @LockByParameter
    public void lockByFirstParameter(@LockKey String param1, String param2) {
    }

    @LockByParameter
    public void lockBySecondParameter(String param1, @LockKey String param2) {
    }
}
