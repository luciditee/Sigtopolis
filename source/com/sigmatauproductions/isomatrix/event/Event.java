/*
 * Copyright (C) 2013, Sigma-Tau Productions.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: 
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies, 
 * either expressed or implied, of the FreeBSD Project.
 */

package com.sigmatauproductions.isomatrix.event;

/**
 * A generic class for scheduled tasks that either need to happen after a set
 * time, or repeat more than once, independent of other game code.  Events can
 * be created either by extending this class, or by creating an anonymous inner
 * class:
 * <pre>
 * Event e;<br />
 * e = new Event(1000, true, 3) {<br />
 *  @Override<br />
 *  void operation() {<br />
 *   // put your code here, it will execute based on the rules established<br />
 *   // in the constructor<br />
 *  }<br />
 * </pre>
 * 
 * 
 * 
 * @author Will
 */
abstract public class Event {
    
    /**
     * The delay incurred before the event occurs (in milliseconds).
     */
    private int delay = 1;
    
    /**
     * The amount of milliseconds since the last recurrence of the event.
     */
    private int lifetime = 0;
    
    /**
     * Measures whether or not the event is capable of recurring.
     */
    private boolean recurring = false;
    
    /**
     * The number of recurrences left on this event.
     */
    private int recurrences = 0;
    
    /**
     * The primary constructor of an event, where the delay between recurrences,
     * whether or not the event is recurring, and the number of recurrences are
     * specified.
     * @param delay
     * @param recurring
     * @param recurrences 
     */
    public Event(int delay, boolean recurring, int recurrences) {
        this.delay = delay;
        this.recurring = recurring;
        this.recurrences = (recurrences >= -1) ? recurrences : 1;
    }
    
    /**
     * A constructor where the delay can be set, but the event is assumed to be
     * one-time.
     * @param delay 
     */
    public Event(int delay) {
        this(delay, false, 1);
    }
    
    /**
     * Called by {@link EventHandler} to run the event's operation.
     */
    protected final void execute() {
        operation();
    }
    
    /**
     * Returns the delay between recurrences for this event.
     * @return 
     */
    public int getDelay() {
        return delay;
    }
    
    /**
     * Returns whether or not this event is recurring.
     * @return 
     */
    public boolean isRecurring() {
        return recurring;
    }
    
    /**
     * Returns the remaining number of recurrences on this event.
     * @return 
     */
    public int getRecurrences() {
        return recurrences;
    }
    
    /**
     * Decrements the remaining number of recurrences by one.
     */
    protected void decrement() {
        recurrences = ((recurrences-1) >= 0) ? recurrences-1 : 0;
    }
    
    /**
     * Add the specified amount of milliseconds to the lifetime of the event.
     * @param l 
     */
    protected void addToLifetime(int l) {
        this.lifetime += (l >= 0) ? l : 0;
    }
    
    /**
     * Returns the current lifetime of the event.
     * @return 
     */
    public int getLifetime() {
        return lifetime;
    }
    
    /**
     * Resets the current lifetime of the event.
     */
    protected void resetLifetime() {
        this.lifetime = 0;
    }
    
    /**
     * The overridable function containing the operation code executed by the
     * event.
     */
    protected abstract void operation();
}
