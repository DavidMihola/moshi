/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.squareup.moshi.recipes;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.ToJson;

import java.io.IOException;

public final class CustomJsonAdapter {

    public void run() throws Exception {
        // for some reason our JSON has date and time as separate fields -
        // we will clean that up during parsing
        String json = ""
                + "{\n"
                + "  \"title\": \"Blackjack tournament\",\n"
                + "  \"beginDate\": \"20151006\",\n"
                + "  \"beginTime\": \"15:59\"\n"
                + "}\n";

        Moshi moshi = new Moshi.Builder()
                .add(new EventJsonAdapter())
                .add(new EventJson2Adapter())
                .build();
        JsonAdapter<Event> jsonAdapter = moshi.adapter(Event.class);

        Event event = jsonAdapter.fromJson(json);
        System.out.println(event);
        System.out.println(jsonAdapter.toJson(event));
    }

    public static void main(String[] args) throws Exception {
        new CustomJsonAdapter().run();
    }

    public static final class Event {
        public final String title;
        public final String beginDateAndTime;

        public Event(String title, String beginDateAndTime) {
            this.title = title;
            this.beginDateAndTime = beginDateAndTime;
        }

        @Override
        public String toString() {
            return "Event{" +
                    "title='" + title + '\'' +
                    ", beginDateAndTime=" + beginDateAndTime +
                    '}';
        }
    }

    static class EventJson {
        String title;
        String beginDate;
        String beginTime;
    }

    static class EventJson2 {
        String title;
        String beginDate;
        String beginTime;
    }

    static class EventJsonAdapter {

        @ToJson
        EventJson eventToJson(Event event) {
            EventJson json = new EventJson();
            json.title = event.title;
            json.beginDate = event.beginDateAndTime.substring(0, 8);
            json.beginTime = event.beginDateAndTime.substring(9, 14);
            return json;
        }

        @FromJson
        Event eventFromJson(EventJson json) {
            return new Event(
                    json.title,
                    json.beginDate + " " + json.beginTime
            );
        }
    }

    static class EventJson2Adapter {

        @ToJson
        EventJson2 eventToJson(EventJson event) {
            EventJson2 json = new EventJson2();
            json.title = event.title;
            json.beginDate = event.beginDate;
            json.beginTime = event.beginTime;
            return json;
        }

        @FromJson
        EventJson eventFromJson(EventJson2 event) {
            EventJson json = new EventJson();
            json.title = event.title;
            json.beginDate = event.beginDate;
            json.beginTime = event.beginTime;
            return json;
        }
    }
}
