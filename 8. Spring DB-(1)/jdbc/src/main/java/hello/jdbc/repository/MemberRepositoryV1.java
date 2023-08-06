package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;

/**
 * JDBC - DataSource 사용, JdbcUtils 사용
 */
@Slf4j
@RequiredArgsConstructor
public class MemberRepositoryV1 {
    private final DataSource dataSource;

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null; //?를 통한 바인딩, SQL injection을 막기위해 사

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql); //DB에 전달할 SQL, 파라미터 준비
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate(); //DB에 실행
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            //역순으로 close, 리소스 누수 문제 있으니 꼭 막기
            close(con, pstmt, null);
        }

    }

    private void close(Connection c, Statement s, ResultSet rs) {
        //스프링Jdbc 에서 제공
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeConnection(c);
        JdbcUtils.closeStatement(s);
    }

    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }
}
