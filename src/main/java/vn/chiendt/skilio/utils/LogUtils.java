package vn.chiendt.skilio.utils;

import lombok.extern.slf4j.Slf4j;
import vn.chiendt.skilio.domain.message.TraceLogMessage;

import java.util.Date;

@Slf4j
public class LogUtils {
    //    REPORT|SERVICE|TRACE_ID|SPAN_ID|PARENT_ID|DURATION|START_TIME|END_TIME|ERROR_CODE|SPAN_NAME
    private static final String LOGTRACE = "TRACING|payment-later|%s|%s|%s|%s|%s|%s|%s|%s";

    public static TraceLogMessage initLogTrace() {
        return initLogTrace(null, null, null, null);
    }

    public static TraceLogMessage initLogTrace(String traceId, String spanId, String parentId, String spanName) {
        TraceLogMessage traceLogMessage = new TraceLogMessage();
        Date startDate = new Date();
        if (traceId == null) traceId = DataUtils.genUUIDWithPrefix("TRACE");
        if (spanId == null) spanId = DataUtils.genUUIDWithPrefix("SPAN");
        traceLogMessage.setTraceId(traceId);
        traceLogMessage.setParentId(parentId);
        traceLogMessage.setSpanId(spanId);
        traceLogMessage.setStartTime(startDate);
        traceLogMessage.setSpanName(spanName);
        return traceLogMessage;
    }

    public static void logTrace(TraceLogMessage traceLogMessage) {
        if (traceLogMessage == null) return;
        Date endTime = new Date();
        Date start = traceLogMessage.getStartTime();
        if (start == null) return;
        traceLogMessage.setEndTime(endTime);
        traceLogMessage.setDuration(endTime.getTime() - start.getTime());
        //    REPORT|SERVICE|TRACE_ID|SPAN_ID|PARENT_ID|DURATION|START_TIME|END_TIME|ERROR_CODE|SPAN_NAME
        log.info(String.format(LOGTRACE,
                traceLogMessage.getTraceId() == null ? "" : traceLogMessage.getTraceId(),
                traceLogMessage.getSpanId() == null ? "" : traceLogMessage.getSpanId(),
                traceLogMessage.getParentId() == null ? "" : traceLogMessage.getParentId(),
                traceLogMessage.getDuration() == null ? 0L : traceLogMessage.getDuration(),
                traceLogMessage.getStartTime() == null ? "" : traceLogMessage.getStartTime(),
                traceLogMessage.getEndTime() == null ? "" : traceLogMessage.getEndTime(),
                traceLogMessage.getErrorCode() == null ? "" : traceLogMessage.getErrorCode(),
                traceLogMessage.getSpanName() == null ? "" : traceLogMessage.getSpanName()
        ));
    }
}

