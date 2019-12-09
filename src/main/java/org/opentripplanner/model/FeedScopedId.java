/* This file is based on code copied from project OneBusAway, see the LICENSE file for further information. */
package org.opentripplanner.model;

import java.io.Serializable;

public class FeedScopedId implements Serializable, Comparable<FeedScopedId> {

    /**
     * One Bus Away uses the underscore as a scope separator between Agency and ID. In OTP we use
     * feed IDs instead of agency IDs as scope, and they are separated with a colon when
     * represented together in String form.
     */
    private static final char ID_SEPARATOR = ':';

    private static final long serialVersionUID = 1L;

    private String feedId;

    private String id;

    public FeedScopedId() {

    }

    public FeedScopedId(String feedId, String id) {
        this.feedId = feedId;
        this.id = id;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean hasValues() {
        return this.feedId != null && this.id != null;
    }

    public int compareTo(FeedScopedId o) {
        int c = this.feedId.compareTo(o.feedId);
        if (c == 0)
            c = this.id.compareTo(o.id);
        return c;
    }

    /**
     * Given an id of the form "agencyId_entityId", parses into a
     * {@link FeedScopedId} id object.
     *
     * @param value id of the form "agencyId_entityId"
     * @return an id object
     */
    public static FeedScopedId convertFromString(String value, char separator) {
        int index = value.indexOf(separator);
        if (index == -1) {
            throw new IllegalStateException("invalid agency-and-id: " + value);
        } else {
            return new FeedScopedId(value.substring(0, index), value.substring(index + 1));
        }
    }

    @Override
    public int hashCode() {
        return feedId.hashCode() ^ id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof FeedScopedId))
            return false;
        FeedScopedId other = (FeedScopedId) obj;
        if (!feedId.equals(other.feedId))
            return false;
        if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return convertToString(this);
    }

    /**
     * Given an id of the form "agencyId_entityId", parses into a
     * {@link FeedScopedId} id object.
     *
     * @param value id of the form "agencyId_entityId"
     * @return an id object
     * @throws IllegalArgumentException if the id cannot be parsed
     */
    public static FeedScopedId convertFromString(String value) throws IllegalArgumentException {
        if (value == null || value.isEmpty())
            return null;
        int index = value.indexOf(ID_SEPARATOR);
        if (index == -1) {
            throw new IllegalArgumentException("invalid agency-and-id: " + value);
        } else {
            return new FeedScopedId(value.substring(0, index), value.substring(index + 1));
        }
    }

    public static boolean isValidString(String value) throws IllegalArgumentException {
        return value != null && value.indexOf(ID_SEPARATOR) > -1;
    }

    /**
     * Given an {@link FeedScopedId} object, creates a string representation of the
     * form "agencyId_entityId"
     *
     * @param aid an id object
     * @return a string representation of the form "agencyId_entityId"
     */
    public static String convertToString(FeedScopedId aid) {
        if (aid == null)
            return null;
        return concatenateId(aid.getFeedId(), aid.getId());
    }

    /**
     * Concatenate agencyId and id into a string.
     */
    public static String concatenateId(String agencyId, String id) {
        return agencyId + ID_SEPARATOR + id;
    }
}
