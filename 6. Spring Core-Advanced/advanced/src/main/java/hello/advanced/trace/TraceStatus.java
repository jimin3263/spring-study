package hello.advanced.trace;

public class TraceStatus { //로그의 상태 정보
    private TraceId traceId; //트랜잭션 id, 레벨 가진다.
    private Long startTimeMs; //시작 시간 저장, 시작- 종료까지 전체 수행 시간 구할 수 있다.
    private String message; //시작시 사용한 메시지, 종료시에도 이 메시지를 이용해 종료

    public TraceStatus(TraceId traceId, Long startTimeMs, String message) {
        this.traceId = traceId;
        this.startTimeMs = startTimeMs;
        this.message = message;
    }

    public TraceId getTraceId() {
        return traceId;
    }

    public Long getStartTimeMs() {
        return startTimeMs;
    }

    public String getMessage() {
        return message;
    }
}
