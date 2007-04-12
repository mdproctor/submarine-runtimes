package org.acme.insurance;

/**
 * 
 * @author Michael Neale
 */
public class Rejection {

    private String reason;

    public Rejection(final String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(final String reason) {
        this.reason = reason;
    }

}
