package me.dnevado.cabify.challenge.domain;

import java.util.Optional;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import me.dnevado.cabify.challenge.domain.model.Car;
import me.dnevado.cabify.challenge.domain.model.ReturnMessage;
/* import me.dnevado.cabify.challenge.domain.model.Journeys;
import me.dnevado.cabify.challenge.domain.model.Item; */

public interface CabifyRepository {

    Optional<ReturnMessage> createAvailableCars(JsonArray AvailableCars);
    Optional<ReturnMessage> assignJourney(JsonObject journey);
    Optional<ReturnMessage> locateJourney(String journeyId);
    Optional<ReturnMessage> serviceStatus();
    Optional<ReturnMessage> dropOff();

}
