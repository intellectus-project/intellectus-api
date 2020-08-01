package com.intellectus.services.call;

import com.intellectus.controllers.model.CallDto;
import com.intellectus.controllers.model.StatDto;
import com.intellectus.model.Call;
import com.intellectus.model.Stat;
import com.intellectus.model.configuration.User;
import com.intellectus.model.constants.SpeakerType;
import com.intellectus.repositories.CallRepository;
import com.intellectus.repositories.StatRepository;
import com.intellectus.security.UserPrincipal;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class CallService {
    private CallRepository callRepository;
    private StatRepository statRepository;

    public CallService(CallRepository callRepository, StatRepository statRepository) {
        this.callRepository = callRepository;
        this.statRepository = statRepository;
    }

    public void create(User user, CallDto callDto) {
        Call call = new Call(user,
                            callDto.getStartTime(),
                            callDto.getEndTime(),
                            callDto.getEmotion());
        callRepository.save(call);

        StatDto consultantDto = callDto.getConsultant();
        Stat consultantStat = new Stat(consultantDto.getSadness(),
                        consultantDto.getHappiness(),
                        consultantDto.getFear(),
                        consultantDto.getNeutrality(),
                        consultantDto.getAnger(),
                        call,
                        SpeakerType.SPEAKER_TYPE_CONSULTANT.getSpeakerType());
        statRepository.save(consultantStat);

        StatDto operatorDto = callDto.getOperator();
        Stat operatorStat = new Stat(operatorDto.getSadness(),
                operatorDto.getHappiness(),
                operatorDto.getFear(),
                operatorDto.getNeutrality(),
                operatorDto.getAnger(),
                call,
                SpeakerType.SPEAKER_TYPE_OPERATOR.getSpeakerType());
        statRepository.save(operatorStat);

    }
}
