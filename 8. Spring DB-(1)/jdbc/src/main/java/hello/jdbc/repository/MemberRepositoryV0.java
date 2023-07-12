package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * JDBC - DriverManager 사용!
 */
@Slf4j
public class MemberRepositoryV0 {
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
        //하나라도 잘못되면 다음 실행안되는 문제 막기 위함, 서로의 close가 영향주지 않음
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("error", e);
            }
        }

        if (s != null) {
            try {
                s.close();
            } catch (SQLException e) {
                log.error("error", e);
            }
        }

        if (c != null) {
            try {
                c.close();
            } catch (SQLException e) {
                log.error("error", e);
            }
        }
    }

    private Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
