package hello.aop.exam;

import hello.aop.exam.annotation.Retry;
import hello.aop.exam.annotation.Trace;
import org.springframework.stereotype.Repository;

@Repository
public class ExamRepository {
    private static int seq = 0;

    /**
     * 5번에 1번 실패하도록
     */
    @Trace
    @Retry(4) //생략하면 디폴트
    public String save(String itemId) {
        seq++;
        if (seq % 5 == 0) {
            throw new IllegalArgumentException("예외 발생");
        }
        return "ok";
    }
}
