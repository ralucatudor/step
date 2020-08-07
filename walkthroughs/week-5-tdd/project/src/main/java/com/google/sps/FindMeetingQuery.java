// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import com.google.sps.TimeRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** Class responsible for scheduling a meeting between multiple people. */
public final class FindMeetingQuery {
  /**
   * Return the time ranges when a new meeting could happen in a day.
   * 
   * @param events  The collection of all known events in that day. Each event has:
   *                a name, a time range and a collection of attendees.
   * @param request The new meeting request, which has: 
   *                a name, a duration in minutes and a collection of attendees.
   *                The attendees are either mandatory or optional.
   */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Set<Event> eventsSet = new HashSet<>(events);

    // Get the mandatory attendees of the new meeting.
    Set<String> mandatoryAttendees = new HashSet<>(request.getAttendees());
    
    // Get both mandatory and optional attendees.
    Set<String> allAttendees = new HashSet<>(request.getAttendees());
    allAttendees.addAll(request.getOptionalAttendees());

    List<TimeRange> timeRangesForAllAttendees = 
        solveQueryforAttendees(eventsSet, allAttendees, request.getDuration());
    
    // If one or more time slots exists so that both mandatory and optional attendees can attend, 
    // return those time slots. 
    if (!timeRangesForAllAttendees.isEmpty()) {
      return timeRangesForAllAttendees;
    }
    
    // Otherwise, return the time slots that fit just the mandatory attendees.
    return solveQueryforAttendees(eventsSet, mandatoryAttendees, request.getDuration());
  }

  /**
   * Return the times when a new meeting could happen in a day, so that all {@code attendees} can join.
   *
   * @param events    The set of all known events in that day.
   * @param attendees The set of required attendees for the new meeting.
   * @param duration  The time duration of the new meeting.
   */
  private static List<TimeRange> solveQueryforAttendees(Set<Event> events, Set<String> attendees, long duration) {
    // Get the time ranges when at least one attendee is not free 
    // (because for a particular time slot to work, all attendees must be free to attend the new meeting).
    List<TimeRange> occupiedTimeRanges = events
      .stream()
      .filter(event -> hasCommonAttendees(event, attendees))
      .map(event -> event.getWhen())
      .filter(timeRange -> checkIsWithinTheDay(timeRange))
      .collect(Collectors.toList());
    
    return getNewMeetingTimeRanges(occupiedTimeRanges, duration);
  }

  /**
   * Returns whether the {@code event} has at least one common attendee as the {@code attendees}.
   */
  private static boolean hasCommonAttendees(Event event, Set<String> attendees) {
    Set<String> eventAttendees = new HashSet<>(event.getAttendees());
    for (String attendee : attendees) {
      if (eventAttendees.contains(attendee)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns whether the {@code timeRange} is within the day.
   */
  private static boolean checkIsWithinTheDay(TimeRange timeRange) {
    return (timeRange.WHOLE_DAY.contains(timeRange));
  }

  /**
   * Returns the times slots when a new meeting taking {@code newMeetingDuration} minutes could happen 
   * in a day, considering the {@code occupiedTimeRanges}.
   * It uses a Greedy algorithm.
   */
  private static List<TimeRange> getNewMeetingTimeRanges(List<TimeRange> occupiedTimeRanges, 
                                                         long newMeetingDuration) {
    // Consider the cases when there are no constraints for the new meeting.
    if (occupiedTimeRanges.isEmpty()) {
      if (newMeetingDuration <= TimeRange.WHOLE_DAY.duration()) {
        return Arrays.asList(TimeRange.WHOLE_DAY);
      } else {
        return Collections.emptyList();
      }
    }

    // Add two dummy time ranges {@code beforeStartOfDay} and {@code afterEndOfDay}. This way, there 
    // is no need to check outside the loop for empty time slot before the first occupied time range.
    // Same for checking after the last occupied time range.
    TimeRange beforeStartOfDay = 
          TimeRange.fromStartEnd(TimeRange.START_OF_DAY - 1, TimeRange.START_OF_DAY, false);
    
    TimeRange afterEndOfDay = 
        TimeRange.fromStartEnd(TimeRange.END_OF_DAY + 1, TimeRange.END_OF_DAY + 2, false);

    occupiedTimeRanges.add(beforeStartOfDay);
    occupiedTimeRanges.add(afterEndOfDay);

    // Sort {@code occupiedTimeRanges} in ascending order by start.
    Collections.sort(occupiedTimeRanges, TimeRange.ORDER_BY_START);

    // Init the time slots list when the new meeting can take place. 
    List<TimeRange> newMeetingTimeRanges = new ArrayList<>();

    Iterator<TimeRange> occupiedTimeRangesIterator = occupiedTimeRanges.iterator();
    TimeRange currentOccupiedTimeRange = occupiedTimeRangesIterator.next();

    int previousOccupiedTimeRangeEnd = currentOccupiedTimeRange.end();

    while(occupiedTimeRangesIterator.hasNext()) {
      currentOccupiedTimeRange = occupiedTimeRangesIterator.next();

      if (previousOccupiedTimeRangeEnd < currentOccupiedTimeRange.start()) {
        // Found an empty time slot: [ previousOccupiedTimeRangeEnd, currentOccupiedTimeRange.start() )
        TimeRange newTimeRange = 
            TimeRange.fromStartEnd(previousOccupiedTimeRangeEnd, currentOccupiedTimeRange.start(), false);

        if (newTimeRange.duration() >= newMeetingDuration) {
          newMeetingTimeRanges.add(newTimeRange);
        }
      }
      
      if (previousOccupiedTimeRangeEnd < currentOccupiedTimeRange.end()) {
        previousOccupiedTimeRangeEnd = currentOccupiedTimeRange.end();
      }
    }

    return newMeetingTimeRanges;
  }
}
