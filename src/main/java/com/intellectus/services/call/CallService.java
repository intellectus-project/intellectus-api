package com.intellectus.services.call;

import com.intellectus.controllers.model.CallRequestPostDto;
import com.intellectus.controllers.model.CallRequestPatchDto;
import com.intellectus.controllers.model.StatDto;
import com.intellectus.model.Call;
import com.intellectus.model.Stat;
import com.intellectus.model.configuration.User;
import com.intellectus.model.constants.SpeakerType;
import com.intellectus.repositories.CallRepository;
import com.intellectus.repositories.StatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CallService {
    private CallRepository callRepository;
    private StatRepository statRepository;

    @Autowired
    public CallService(CallRepository callRepository, StatRepository statRepository) {
        this.callRepository = callRepository;
        this.statRepository = statRepository;
    }

    public Long create(User user, CallRequestPostDto callDto) {
        Call call = new Call(user, callDto.getStartTime());
        callRepository.save(call);
        return call.getId();
    }

    public void update(CallRequestPatchDto callDto, Long id) throws Exception{
        Optional<Call> optionalCall = callRepository.findById((id));
        if(optionalCall.isEmpty())
            throw new Exception("Call does not exist");
        Call call = optionalCall.get();
        call.setEndTime(callDto.getEndTime());
        call.setEmotion(callDto.getEmotion());

        callRepository.save(call);

        StatDto consultantDto = callDto.getConsultantStats();
        Stat consultantStats = new Stat(consultantDto.getSadness(),
                        consultantDto.getHappiness(),
                        consultantDto.getFear(),
                        consultantDto.getNeutrality(),
                        consultantDto.getAnger(),
                        call,
                        SpeakerType.SPEAKER_TYPE_CONSULTANT.getSpeakerType());
        statRepository.save(consultantStats);

        StatDto operatorDto = callDto.getOperatorStats();
        Stat operatorStats = new Stat(operatorDto.getSadness(),
                operatorDto.getHappiness(),
                operatorDto.getFear(),
                operatorDto.getNeutrality(),
                operatorDto.getAnger(),
                call,
                SpeakerType.SPEAKER_TYPE_OPERATOR.getSpeakerType());
        statRepository.save(operatorStats);

    }
}
