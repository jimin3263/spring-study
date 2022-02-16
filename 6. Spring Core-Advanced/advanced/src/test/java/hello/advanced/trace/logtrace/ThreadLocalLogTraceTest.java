package hello.advanced.trace.logtrace;

import hello.advanced.trace.TraceStatus;
import org.junit.jupiter.api.Test;

class ThreadLocalLogTraceTest {

    ThreadLocalLogTrace trace = new ThreadLocalLogTrace();

    @Test
    public void begin_end_level2() throws Exception{
        TraceStatus status1 = trace.begin("hello");
        TraceStatus status2 = trace.begin("hello");
        trace.end(status2);
        trace.end(status1);
    }

    @Test
    public void begin_exception_level2() throws Exception{
        TraceStatus status1 = trace.begin("hello1");
        TraceStatus status2 = trace.begin("hello2");
        trace.exception(status2, new IllegalStateException());
        trace.exception(status1, new IllegalStateException());
    }

}