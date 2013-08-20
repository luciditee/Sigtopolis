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

import java.util.ArrayList;
import java.util.List;

/**
 * A static class designed to track and execute an arbitrary number of
 * {@link Event} objects.  In order for the EventHandler to work properly, one
 * must place the following method call in the game's main update loop:
 * 
 * <pre>{@code EventHandler.update(delta);}</pre>
 * 
 * where {@code delta} equals the number of milliseconds that have elapsed since
 * the last frame, also known as the <i>delta-time</i> variable.
 * 
 * @author Will
 */
public final class EventHandler {
    /**
     * Tracks the actual {@link Event} objects.
     */
    private static List<Event> events = new ArrayList<>();
    
    /**
     * Adds the specified event to the event queue.
     * @param e 
     */
    public static void addEvent(Event e) {
        if (e != null) { events.add(e); }
    }
    
    /**
     * Intended to be called every frame and provided the delta-time, this
     * method iterates through and executes events whose time has elapsed.
     * @param delta 
     */
    public static void update(int delta) {
        for(int i=0; i<events.size(); i++){
            events.get(i).addToLifetime(delta);
            if (events.get(i).getLifetime() > events.get(i).getDelay()) {
                events.get(i).execute();
                if (events.get(i).isRecurring()) {
                    if (events.get(i).getRecurrences() < 0) {
                        events.get(i).resetLifetime();
                    } else if (events.get(i).getRecurrences() > 0) {
                        events.get(i).decrement();
                        events.get(i).resetLifetime();
                    }
                    if (events.get(i).getRecurrences() == 0) {
                        events.remove(i);
                    }
                } else {
                    events.remove(i);
                }
            }
        }
    }
}
