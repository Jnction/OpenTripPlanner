package org.opentripplanner.model.plan;

import org.opentripplanner.framework.tostring.ToStringBuilder;

/**
 * This class is used to represent a stop arrival event mostly for intermediate visits to a stops
 * along a route.
 *
 * @param arrival          The time the rider will arrive at the place.
 * @param departure        The time the rider will depart the place.
 * @param stopPosInPattern For transit trips, the stop index (numbered from zero from the start of
 *                         the trip).
 * @param gtfsStopSequence For transit trips, the sequence number of the stop. Per GTFS, these
 *                         numbers are increasing.
 */
public record StopArrival(
  Place place,
  LegTimes arrival,
  LegTimes departure,
  Integer stopPosInPattern,
  Integer gtfsStopSequence
) {
  @Override
  public String toString() {
    return ToStringBuilder
      .of(StopArrival.class)
      .addObj("arrival", arrival)
      .addObj("departure", departure)
      .addObj("place", place)
      .toString();
  }
}
