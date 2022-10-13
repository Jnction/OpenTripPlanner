package org.opentripplanner.ext.vectortiles.layers.vehiclerental;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.opentripplanner.routing.vehicle_rental.RentalVehicleType.FormFactor.BICYCLE;
import static org.opentripplanner.routing.vehicle_rental.RentalVehicleType.FormFactor.SCOOTER;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import org.junit.jupiter.api.Test;
import org.opentripplanner.routing.vehicle_rental.RentalVehicleType;
import org.opentripplanner.routing.vehicle_rental.VehicleRentalStation;
import org.opentripplanner.routing.vehicle_rental.VehicleRentalVehicle;
import org.opentripplanner.transit.model.basic.NonLocalizedString;
import org.opentripplanner.transit.model.framework.FeedScopedId;

public class VehicleRentalLayerTest {

  public static final String NAME = "a rental";
  DigitransitVehicleRentalPropertyMapper mapper = new DigitransitVehicleRentalPropertyMapper();

  @Test
  public void floatingVehicle() {
    var vehicle = new VehicleRentalVehicle();
    vehicle.id = new FeedScopedId("A", "B");
    vehicle.latitude = 1;
    vehicle.longitude = 2;
    vehicle.name = new NonLocalizedString(NAME);
    vehicle.vehicleType = vehicleType(BICYCLE);

    Map<String, Object> map = new HashMap<>();
    mapper.map(vehicle).forEach(o -> map.put(o.first, o.second));

    assertEquals("bicycle", map.get("formFactors"));
    assertEquals(NAME, map.get("name"));
    assertEquals("floatingVehicle", map.get("type"));
    assertEquals("A", map.get("networks"));
  }

  @Test
  public void station() {
    var vehicle = new VehicleRentalStation();
    vehicle.id = new FeedScopedId("A", "B");
    vehicle.latitude = 1;
    vehicle.longitude = 2;
    vehicle.name = new NonLocalizedString(NAME);
    vehicle.vehicleTypesAvailable = Map.of(vehicleType(BICYCLE), 5, vehicleType(SCOOTER), 10);

    Map<String, Object> map = new HashMap<>();
    mapper.map(vehicle).forEach(o -> map.put(o.first, o.second));

    assertEquals("bicycle,scooter", map.get("formFactors"));
    assertEquals(NAME, map.get("name"));
    assertEquals("station", map.get("type"));
    assertEquals("A", map.get("networks"));
  }

  @Nonnull
  private static RentalVehicleType vehicleType(RentalVehicleType.FormFactor formFactor) {
    return new RentalVehicleType(
      new FeedScopedId("1", formFactor.name()),
      "bicycle",
      formFactor,
      RentalVehicleType.PropulsionType.HUMAN,
      1000d
    );
  }
}
