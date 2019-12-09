package org.opentripplanner.routing.bike_rental;

import com.conveyal.r5.otp2.api.path.Path;
import com.conveyal.r5.otp2.api.path.PathLeg;
import org.opentripplanner.common.model.P2;
import org.opentripplanner.routing.algorithm.raptor.transit.TransitLayer;
import org.opentripplanner.routing.algorithm.raptor.transit.TripSchedule;
import org.opentripplanner.routing.core.Fare;
import org.opentripplanner.routing.core.Fare.FareType;
import org.opentripplanner.routing.core.WrappedCurrency;
import org.opentripplanner.routing.services.FareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Currency;
import java.util.List;

/**
 * This appears to be used in combination with transit using an AddingMultipleFareService.
 */
public class TimeBasedBikeRentalFareService implements FareService, Serializable {

    private static final long serialVersionUID = 5226621661906177942L;

    private static Logger log = LoggerFactory.getLogger(TimeBasedBikeRentalFareService.class);

    // Each entry is <max time, cents at that time>; the list is sorted in
    // ascending time order
    private List<P2<Integer>> pricing_by_second;

    private Currency currency;

    protected TimeBasedBikeRentalFareService(Currency currency, List<P2<Integer>> pricingBySecond) {
        this.currency = currency;
        this.pricing_by_second = pricingBySecond;
    }

    // FIXME we need to test if the leg is a bike rental leg.
    //       OTP2 doesn't handle non walk access or egress yet.
    private int getLegCost(PathLeg<TripSchedule> pathLeg) {
        int rideCost = 0;
        int rideTime = pathLeg.duration();
        for (P2<Integer> bracket : pricing_by_second) {
            int time = bracket.first;
            if (rideTime < time) {
                rideCost = bracket.second;
                // FIXME this break seems to exit at the first matching bracket rather than the last.
                break;
            }
        }
        return rideCost;
    }

    @Override
    public Fare getCost(Path<TripSchedule> path, TransitLayer transitLayer) {

        int rideCost = getLegCost(path.accessLeg());
        rideCost += getLegCost(path.egressLeg());

        Fare fare = new Fare();
        fare.addFare(FareType.regular, new WrappedCurrency(currency), rideCost);
        return fare;
    }
}
