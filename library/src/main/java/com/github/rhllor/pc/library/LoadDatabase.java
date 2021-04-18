package com.github.rhllor.pc.library;


import java.util.Calendar;
import java.util.Random;

import com.github.rhllor.pc.library.entity.Consumption;
import com.github.rhllor.pc.library.entity.User;
import com.github.rhllor.pc.library.repository.ConsumptionRepository;
import com.github.rhllor.pc.library.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class LoadDatabase {

    private static Logger _log = LoggerFactory.getLogger(LoadDatabase.class);
    private Random _r = new Random();

    
    @Bean("initUser")
    CommandLineRunner initUser(UserRepository repository){
        return args -> {
            _log.info("Start: Inizializzazione tabella Utenti.");
            _log.info("Record: " + repository.save(new User(1L, "pagot")));
            _log.info("Record: " + repository.save(new User(2L, "fio")));
            _log.info("Record: " + repository.save(new User(3L, "curtis")));
            _log.info("Record: " + repository.save(new User(4L, "gina")));
            _log.info("End: Inizializzazione tabella Utenti.");
        };
    }
    
    @Bean("initConsumption")
    @DependsOn({"initUser"})
    CommandLineRunner initConsumption(ConsumptionRepository repository) {
        return args -> {
            _log.info("Start: Inizializzazione tabella Consumi.");

            int actualWN = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
            int actualYear = Calendar.getInstance().get(Calendar.YEAR);

            for (Long idUser = 1L; idUser <= 4L; idUser++) {

                for (int year = 2000; year <= 2020; year++) {
                    for (int weekNumber = 5; weekNumber <= 52; weekNumber++) {
                        _log.info("Record: " + repository.save(new Consumption(idUser, year, weekNumber, nextWeight())));
                    }
                }

                for (int weekNumber = 1; weekNumber < actualWN; weekNumber++) {
                    _log.info("Record: " + repository.save(new Consumption(idUser, actualYear, weekNumber, nextWeight())));
                }
            }

            _log.info("End: Inizializzazione tabella Consumi.");
        };
    }


    private float nextWeight() {
        float min = 0;
        float max = 2.5f;
        return min + this._r.nextFloat() * (max - min);
    }
}
