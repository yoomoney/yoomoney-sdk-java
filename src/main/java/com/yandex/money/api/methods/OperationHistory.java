package com.yandex.money.api.methods;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.api.model.Error;
import com.yandex.money.api.model.Operation;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.MethodRequest;
import com.yandex.money.api.net.MethodResponse;
import com.yandex.money.api.net.PostRequestBodyBuffer;

import org.joda.time.DateTime;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Operation history.
 * <p/>
 * If successful contains list of operations as well as token to a next record if there are more
 * operations in a user's history.
 *
 * @author Roman Tsirulnikov (romanvt@yamoney.ru)
 */
public class OperationHistory implements MethodResponse {

    private final Error error;
    private final String nextRecord;
    private final List<Operation> operations;

    /**
     * Constructor.
     *
     * @param error error code
     * @param nextRecord nextRecord marker used in subsequent request if needed
     * @param operations list of operations
     */
    public OperationHistory(Error error, String nextRecord, List<Operation> operations) {
        this.error = error;
        this.nextRecord = nextRecord;
        this.operations = operations;
    }

    @Override
    public String toString() {
        return "OperationHistory{" +
                "error=" + error +
                ", nextRecord='" + nextRecord + '\'' +
                ", operations=" + operations +
                '}';
    }

    public Error getError() {
        return error;
    }

    public String getNextRecord() {
        return nextRecord;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    /**
     * Requests for a list of operations in user's history.
     * <p/>
     * Authorized session required.
     *
     * @see com.yandex.money.api.net.OAuth2Session
     */
    public static class Request implements MethodRequest<OperationHistory> {

        private final Set<FilterType> types;
        private final String label;
        private final DateTime from;
        private final DateTime till;
        private final String startRecord;
        private final Integer records;
        private final Boolean details;

        /**
         * Use builder to create the request.
         */
        private Request(Set<FilterType> types, String label, DateTime from, DateTime till,
                        String startRecord, Integer records, Boolean details) {

            this.types = types;
            this.label = label;
            if (from != null && till != null && from.isAfter(till)) {
                throw new IllegalArgumentException("\'from\' should be before \'till\'");
            }
            this.from = from;
            this.till = till;
            this.startRecord = startRecord;
            if (records != null) {
                if (records < 1) {
                    records = 1;
                } else if (records > 100) {
                    records = 100;
                }
            }
            this.records = records;
            this.details = details;
        }

        @Override
        public URL requestURL(HostsProvider hostsProvider) throws MalformedURLException {
            return new URL(hostsProvider.getMoneyApi() + "/operation-history");
        }

        @Override
        public OperationHistory parseResponse(InputStream inputStream) {
            return buildGson().fromJson(new InputStreamReader(inputStream), OperationHistory.class);
        }

        @Override
        public PostRequestBodyBuffer buildParameters() throws IOException {
            PostRequestBodyBuffer requestBodyBuffer = new PostRequestBodyBuffer();
            if (types != null && !types.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                Iterator<FilterType> iterator = types.iterator();
                builder.append(iterator.next().getCode());
                while (iterator.hasNext()) {
                    builder.append(' ').append(iterator.next().getCode());
                }
                requestBodyBuffer.addParam("type", builder.toString());
            }
            return requestBodyBuffer
                    .addParamIfNotNull("label", label)
                    .addDateTimeIfNotNull("from", from)
                    .addDateTimeIfNotNull("till", till)
                    .addParamIfNotNull("start_record", startRecord)
                    .addParamIfNotNull("records", records)
                    .addParamIfNotNull("details", details);
        }

        private static Gson buildGson() {
            return new GsonBuilder()
                    .registerTypeAdapter(OperationHistory.class, new Deserializer())
                    .create();
        }

        /**
         * Builder for a {@link com.yandex.money.api.methods.OperationHistory.Request}.
         */
        public static class Builder {
            private Set<FilterType> types;
            private String label;
            private DateTime from;
            private DateTime till;
            private String startRecord;
            private Integer records;
            private Boolean details;

            /**
             * Specifies types of operations that respond should contain. Can be omitted if no
             * specific types are required: respond will contain every operation.
             *
             * @param types set of operation types
             */
            public Builder setTypes(Set<FilterType> types) {
                this.types = types;
                return this;
            }

            /**
             * Look up for a specific operations using theirs labels.
             *
             * @param label the label
             */
            public Builder setLabel(String label) {
                this.label = label;
                return this;
            }

            /**
             * Look for operations starting {@code from} specified time.
             *
             * @param from time
             */
            public Builder setFrom(DateTime from) {
                this.from = from;
                return this;
            }

            /**
             * Look for operations {@code till} specified time.
             *
             * @param till time
             */
            public Builder setTill(DateTime till) {
                this.till = till;
                return this;
            }

            /**
             * Marker to subsequent page of operation's list
             *
             * @param startRecord marker
             */
            public Builder setStartRecord(String startRecord) {
                this.startRecord = startRecord;
                return this;
            }

            /**
             * Limit number of records in response.
             *
             * @param records limit
             */
            public Builder setRecords(Integer records) {
                this.records = records;
                return this;
            }

            /**
             * Request operation details. If set to {@code true} list of operations will contain
             * extended operation details.
             *
             * @param details request detailed operations
             */
            public Builder setDetails(Boolean details) {
                this.details = details;
                return this;
            }

            /**
             * Creates the {@link com.yandex.money.api.methods.OperationHistory.Request}
             *
             * @return the request
             */
            public Request createRequest() {
                return new Request(types, label, from, till, startRecord, records, details);
            }
        }
    }

    /**
     * Filter types.
     */
    public enum FilterType {
        /**
         * Depositions.
         */
        DEPOSITION("deposition"),
        /**
         * Payments.
         */
        PAYMENT("payment"),
        /**
         * Unaccepted incoming transfers (e.g. p2p transfer with protection code).
         */
        INCOMING_TRANSFER_UNACCEPTED("incoming-transfers-unaccepted");

        private final String code;

        private FilterType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    private static final class Deserializer implements JsonDeserializer<OperationHistory> {
        @Override
        public OperationHistory deserialize(JsonElement json, Type typeOfT,
                                            JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();

            final String operationsMember = "operations";
            List<Operation> operations = null;
            if (object.has(operationsMember)) {
                operations = new ArrayList<>();
                for (JsonElement element : object.getAsJsonArray(operationsMember)) {
                    operations.add(Operation.createFromJson(element));
                }
            }

            return new OperationHistory(Error.parse(JsonUtils.getString(object, "error")),
                    JsonUtils.getString(object, "next_record"), operations);
        }
    }
}
