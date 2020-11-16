package com.intellectus.services;

import com.intellectus.model.Break;
import com.intellectus.model.Call;
import com.intellectus.model.configuration.User;
import com.intellectus.repositories.BreakRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CallBreakService {

    @Autowired
    public CallBreakService(BreakService breakService, CallService callService){
        this.breakService = breakService;
        this.callService = callService;
    }

    private BreakService breakService;
    private CallService callService;

    public Optional<Break> lastCallBreak(User operator) {
        Optional<Break> breakOpt = Optional.of(new Break());
        Call call = callService.actualOperatorCall(operator);
        if(call == null) {
            throw new RuntimeException("El operador no se encuentra en llamada");
        }
        return breakService.findByCall(call);
    }

    public void createBreakBySupervisor(User user, int minutesDuration) {
        Call call = callService.actualOperatorCall(user);
        breakService.create(call, minutesDuration, true, false);
    }

}
