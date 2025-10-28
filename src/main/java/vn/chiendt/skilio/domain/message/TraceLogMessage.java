package vn.chiendt.skilio.domain.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TraceLogMessage {
    private String traceId;
    private String spanId;
    private String parentId;
    private Long duration;
    private Date startTime;
    private Date endTime;
    private String errorCode;
    private String spanName;
}