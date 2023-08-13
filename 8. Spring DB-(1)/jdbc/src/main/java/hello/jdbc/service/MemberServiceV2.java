package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class MemberServiceV2 {
    private MemberRepositoryV2 memberRepository;
    private DataSource dataSource;

    public MemberServiceV2(MemberRepositoryV2 memberRepository, DataSource dataSource) {
        this.memberRepository = memberRepository;
        this.dataSource = dataSource;
    }

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            con.setAutoCommit(false); //트랜잭션 시작
            //비즈니스 로직 시작
            bizLogic(fromId, toId, money, con);
            //비즈니스 로직 끝
            con.commit(); //성공시 커밋
        } catch (Exception e) {
            con.rollback(); //실패시 롤백
            throw new IllegalStateException(e);
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true); //커넥션 풀 고려, 풀이므로 세팅 그대로 유지됨 ㄷㄷ
                    con.close();
                } catch (Exception e) {
                    log.info("error", e);
                }

            }
        }
    }

    private void bizLogic(String fromId, String toId, int money, Connection con) throws SQLException {
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);
        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(con, toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
